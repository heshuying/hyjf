package com.hyjf.wechat.controller.user.bindcardpage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.bindcard.BindCardPageBean;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.util.ResultEnum;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

/**
 * 绑卡 add by pcc
 */
@Controller
@RequestMapping(value = BindCardPageDefine.REQUEST_MAPPING)
public class BindCardPageController extends BaseController {
	// 声明log日志
	private Logger logger = LoggerFactory.getLogger(BindCardPageController.class);

	/** THIS_CLASS */
	private static final String THIS_CLASS = BindCardPageController.class.getName();

	@Autowired
	private BindCardService userBindCardService;

	/**
     * 页面请求绑卡
     * @param request
     * @param response
     * @return
     */
	@SignValidate
	@RequestMapping(value = BindCardPageDefine.REQUEST_BINDCARDPAGE)
	public ModelAndView bindCardPage(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, BindCardPageDefine.REQUEST_BINDCARDPAGE);
		ModelAndView modelAndView = new ModelAndView();
		String sign = requestUtil.getRequestSign(request);
		Integer userId = requestUtil.getRequestUserId(request);

		BankOpenAccount bankOpenAccount = userBindCardService.getBankOpenAccount(userId);
		if (bankOpenAccount == null || Validator.isNull(bankOpenAccount.getAccount())) {
			//返回具体的错误信息 用户未开户
			return getErrorModelAndView(ResultEnum.USER_ERROR_200);
		}
		
		Users users=userBindCardService.getUsers(userId);
		if(users.getIsSetPassword()==0){
			//返回具体的错误信息 用户未设置交易密码
			return getErrorModelAndView(ResultEnum.USER_ERROR_200);
		}
		
		
		List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(userId + "");
		if (accountBankList != null && accountBankList.size() > 0) {
			//返回具体的错误信息 用户已绑卡
			return getErrorModelAndView(ResultEnum.USER_ERROR_217);
		}
		UsersInfo userInfo=userBindCardService.getUsersInfoByUserIdTrue(userId);
		
		try {
			// 同步调用路径
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ BindCardPageDefine.REQUEST_MAPPING + BindCardPageDefine.RETURL_SYN_ACTION + ".page?sign=" + sign+"&account="+bankOpenAccount.getAccount();
			// 异步调用路
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ BindCardPageDefine.REQUEST_MAPPING + BindCardPageDefine.RETURL_ASY_ACTION + ".do?sign=" + sign+"&phone="+users.getMobile();
			// 拼装参数 调用江西银行
			String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign;
			BindCardPageBean bean = new BindCardPageBean();
			bean.setTxCode(BankCallConstant.TXCODE_BIND_CARD_PAGE);
			bean.setChannel(BankCallConstant.CHANNEL_WEI);
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(userInfo.getIdcard());
			bean.setName(userInfo.getTruename());
			bean.setAccountId(bankOpenAccount.getAccount());
			bean.setUserIP(GetCilentIP.getIpAddr(request));
			bean.setUserId(userId);
			bean.setRetUrl(retUrl);
			bean.setSuccessfulUrl(retUrl+"&isSuccess=1");
			bean.setNotifyUrl(bgRetUrl);
			bean.setForgetPassworedUrl(forgetPassworedUrl);
			// 微官网 1
			bean.setPlatform("1");
			modelAndView = userBindCardService.getCallbankMV(bean);

			logger.info("绑卡调用页面end");
			LogUtil.endLog(THIS_CLASS, BindCardPageDefine.REQUEST_BINDCARDPAGE);
			return modelAndView;
		} catch (Exception e) {
			logger.error("调用银行接口失败", e);
			LogUtil.errorLog(THIS_CLASS, BindCardPageDefine.REQUEST_BINDCARDPAGE, e);
			return getErrorModelAndView(ResultEnum.ERROR_022);
		}
	}

    private ModelAndView getErrorModelAndView(ResultEnum param) {
        ModelAndView modelAndView = new ModelAndView(BindCardPageDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, param.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, param.getStatusDesc());
        baseMapBean.setCallBackAction(CustomConstants.HOST + BindCardPageDefine.BINDCARD_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }

    
    /**
     * 页面绑卡同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BindCardPageDefine.RETURL_SYN_ACTION)
    public ModelAndView bindCardReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, BindCardPageDefine.RETURL_SYN_ACTION, "[绑卡同步回调开始]");
        boolean checkTender = RedisUtils.tranactionSet("bindCard" + bean.getLogOrderId(), 600);
        ModelAndView modelAndView = new ModelAndView();
        String frontParams = request.getParameter("frontParams");
        String isSuccess = request.getParameter("isSuccess");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        BaseMapBean baseMapBean=new BaseMapBean();
        bean.convert();
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        logger.info("绑卡同步返回值,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
        // 绑卡后处理
  		try {
  			if(checkTender){
  				List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(bean.getLogUserId() + "");
  	 			if (accountBankList == null || accountBankList.size() == 0) {
  	 				Users users=userBindCardService.getUsersByUserId(Integer.parseInt(bean.getLogUserId()));
  	 				bean.setAccountId(request.getParameter("account"));
  	 				bean.setMobile(users.getMobile());
  		 			// 保存银行卡信息
  		 			userBindCardService.updateCardNoToBank(bean);
  	 			}
  			}else{
 	 				Thread.sleep(3000); 
 	 			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
        if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)||"1".equals(isSuccess)) {
            // 成功
        	modelAndView = new ModelAndView(BindCardPageDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS5.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, ResultEnum.SUCCESS5.getStatusDesc());
            baseMapBean.setCallBackAction(CustomConstants.HOST + BindCardPageDefine.BINDCARD_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, BindCardPageDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        } else {
            return getErrorModelAndView(ResultEnum.ERROR_045); 
        }
    }

    /**
     * 页面绑卡异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(BindCardPageDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        BankCallResult result = new BankCallResult();
        String phone = request.getParameter("phone");
        logger.info("页面绑卡异步回调start");
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());

		// 绑卡后处理
		try {
			// 保存银行卡信息
			userBindCardService.updateCardNoToBank(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
        logger.info("页面绑卡成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }
}
