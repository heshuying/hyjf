package com.hyjf.app.bank.user.repayauthpage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.paymentauthpage.PaymentAuthPagController;
import com.hyjf.app.bank.user.paymentauthpage.PaymentAuthPagDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.repayauth.RepayAuthBean;
import com.hyjf.bank.service.user.repayauth.RepayAuthService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 还款授权
 * @author jun
 * @date 20180820
 */
@Controller
@RequestMapping(value = RepayAuthPageDefine.REQUEST_MAPPING)
public class RepayAuthPageController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(RepayAuthPageController.class);

    public static final String THIS_CLASS = RepayAuthPageController.class.getName();

	@Autowired
    private RepayAuthService repayAuthService;
	
	@Autowired
	private AutoPlusService autoPlusService;

    /**
     * 用户还款授权
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(RepayAuthPageDefine.USER_REPAY_AUTH_ACTION)
	public ModelAndView userRepayAuth(HttpServletRequest request, HttpServletResponse response) {

		logger.info(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if (token == null) {
            return getErrorMV("失败原因：用户未登录！",baseMapBean);
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if(userId==null||userId<=0){
            return getErrorMV("失败原因：用户未登录！",baseMapBean);
        }
        BankOpenAccount account = this.repayAuthService.getBankOpenAccount(userId);
        if (account == null) {
            return getErrorMV("失败原因：用户未开户！",baseMapBean);
        }

        // 判断用户是否设置过交易密码
        Users user = this.repayAuthService.getUsers(userId);
        if (user.getIsSetPassword() == 0) {// 未设置交易密码
            return getErrorMV("失败原因：用户未设置交易密码！",baseMapBean);
        }

        // 判断是否授权过
        if(autoPlusService.checkIsAuth(user.getUserId(),BankCallConstant.TXCODE_REPAY_AUTH_PAGE)){
            return getErrorMV("失败原因：用户已授权,无需重复授权！",baseMapBean);
        }

        // 跳转到汇付天下画面
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  RepayAuthPageDefine.REQUEST_MAPPING
                    + RepayAuthPageDefine.USER_REPAY_AUTH_RETURN_ACTION + ".do";
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  RepayAuthPageDefine.REQUEST_MAPPING
                    + RepayAuthPageDefine.USER_REPAY_AUTH_BGRETURN_ACTION + ".do";
            RepayAuthBean openBean = new RepayAuthBean();
            openBean.setUserId(user.getUserId());
            openBean.setIp(CustomUtil.getIpAddr(request));
            openBean.setAccountId(account.getAccount());
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            // 0：PC  1：微官网  2：Android  3：iOS  4：其他
            openBean.setPlatform(platform);
            openBean.setChannel(BankCallConstant.CHANNEL_APP);
            String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token;
            openBean.setForgotPwdUrl(forgetPassworedUrl);
            String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
            openBean.setOrderId(orderId);
            modelAndView = repayAuthService.getCallbankMV(openBean);
            repayAuthService.insertUserAuthLog(openBean.getUserId(), orderId,Integer.parseInt(openBean.getPlatform()),"6");
            LogUtil.endLog(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_ACTION);
            return modelAndView;
        } catch (Exception e) {
            logger.error("调用银行接口失败",e);
            logger.error(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_ACTION, e);
            return getErrorMV("失败原因：调用银行接口失败！",baseMapBean);
        }
    }


    /**
     * 用户还款授权同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RepayAuthPageDefine.USER_REPAY_AUTH_RETURN_ACTION)
    public ModelAndView userRepayAuthReturn(HttpServletRequest request, HttpServletResponse response,
                                            @ModelAttribute BankCallBean bean) {

        logger.info(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_RETURN_ACTION, "[还款授权同步回调开始]");
        BaseMapBean baseMapBean=new BaseMapBean();
        bean.convert();
        // 返回失败
        // 出借人签约状态查询
        logger.info("还款授权同步回调调用查询接口查询状态");
        BankCallBean retBean =
                autoPlusService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.CHANNEL_PC);
        logger.info("还款授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()));
        retBean.setOrderId(bean.getLogOrderId());
        bean=retBean;
        // 返回失败
        if (bean.getRetCode() != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())
                && "1".equals(bean.getRepayAuth())) {
            // 成功
            try {
                this.autoPlusService.updateUserAuth(Integer.parseInt(bean.getLogUserId()),retBean);
            } catch (Exception e) {
                // 判断是否授权过
                if(autoPlusService.checkIsAuth(Integer.parseInt(bean.getLogUserId()),BankCallConstant.TXCODE_REPAY_AUTH_PAGE)){
                    return getSuccessMV("服务费授权成功！",baseMapBean);
                }
                logger.info("还款授权同步插入数据库失败，错误原因:" + e.getMessage());
                return getErrorMV("请联系客服!",baseMapBean);
            }

        } else {
            return getErrorMV("失败原因:"+autoPlusService.getBankRetMsg(bean.getRetCode()),baseMapBean);
        }

        LogUtil.endLog(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_RETURN_ACTION, "[还款授权完成后,回调结束]");
        return getSuccessMV("还款授权成功！",baseMapBean);
    }


    /**
     * 还款授权异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(RepayAuthPageDefine.USER_REPAY_AUTH_BGRETURN_ACTION)
    public String userRepayAuthBgreturn(HttpServletRequest request, HttpServletResponse response,
                                        @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        logger.info(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_BGRETURN_ACTION, "[还款授权异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);
        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                bean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.autoPlusService.updateUserAuth(Integer.parseInt(bean.getLogUserId()),bean);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_BGRETURN_ACTION, e);
            }
        }
        logger.info(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_BGRETURN_ACTION, "[用户还款授权完成后,回调结束]");
        result.setMessage("还款授权成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }

    /**
     * 返回失败信息
     * @param msg
     * @param baseMapBean
     * @return
     */
    private ModelAndView getErrorMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.setCallBackAction(CustomConstants.HOST+RepayAuthPageDefine.JUMP_HTML_FAILED_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_ACTION);
        return modelAndView;
    }

    /**
     * 返回成功信息
     * @param msg
     * @param baseMapBean
     * @return
     */
    private ModelAndView getSuccessMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.setCallBackAction(CustomConstants.HOST+RepayAuthPageDefine.JUMP_HTML_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, RepayAuthPageDefine.USER_REPAY_AUTH_ACTION);
        return modelAndView;
    }
}
