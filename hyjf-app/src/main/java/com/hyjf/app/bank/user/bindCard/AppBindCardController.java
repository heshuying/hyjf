package com.hyjf.app.bank.user.bindCard;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.StringUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * App江西银行绑卡
 * @author liuyang
 *
 */
@Controller
public class AppBindCardController extends BaseController {
	private Logger logger  = LoggerFactory.getLogger(AppBindCardController.class);
	/** THIS_CLASS */
	private static final String THIS_CLASS = AppBindCardController.class.getName();
	private final String TOKEN_ISINVALID_STATUS = "Token失效，请重新登录";

	@Autowired
	private AppBindCardService userBindCardService;


	/**
	 * 绑定银行卡发送短信验证码
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(AppBindCardDefine.REQUEST_SEND_SMS_CODE)
	public BaseResultBeanFrontEnd sendSmsCode(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, AppBindCardDefine.REQUEST_SEND_SMS_CODE);
		SendSmsResultBean result = new SendSmsResultBean();

		String sign = request.getParameter("sign");

		// 检查参数正确性
		if (Validator.isNull(sign)) {
			result.setStatus(SendSmsResultBean.FAIL);
			result.setStatusDesc("sign不能为空");
			return result;
		}

		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) { // token失效
			result.setStatus(SendSmsResultBean.FAIL);
			result.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return result;
		}
		if (userId == null) {
			result.setStatus(SendSmsResultBean.FAIL);
			result.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return result;
		}

		String mobile = request.getParameter("mobile"); // 手机号
        if (StringUtils.isEmpty(mobile)) {
            result.setStatus(SendSmsResultBean.FAIL);
            result.setStatusDesc("手机号不能为空");
            return result;
        }
        
        String cardNo = request.getParameter("cardNo"); // 银行卡号
        if (StringUtils.isEmpty(cardNo)) {
            result.setStatus(SendSmsResultBean.FAIL);
            result.setStatusDesc("银行卡号不能为空");
            return result;
        }
        // 请求发送短信验证码
        BankCallBean bean = this.userBindCardService.cardBindPlusSendSms(userId,
                BankCallMethodConstant.TXCODE_CARD_BIND_PLUS, mobile, BankCallConstant.CHANNEL_APP,cardNo);
        if (bean == null) {
            result.setStatus(SendSmsResultBean.FAIL);
            result.setStatusDesc("发送短信验证码异常");
            return result;
        }
        // 返回失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            if("JX900651".equals(bean.getRetCode())){

                result.setStatus(SendSmsResultBean.SUCCESS);
                result.setStatusDesc(SendSmsResultBean.SUCCESS_MSG);
                result.setSrvAuthCode(bean.getSrvAuthCode());
                return result;
            }
            result.setStatus(SendSmsResultBean.FAIL);
            result.setStatusDesc("发送短信验证码失败，失败原因：" + userBindCardService.getBankRetMsg(bean.getRetCode()));
            return result;
        }
        result.setStatus(SendSmsResultBean.SUCCESS);
        result.setStatusDesc(SendSmsResultBean.SUCCESS_MSG);
        result.setSrvAuthCode(bean.getSrvAuthCode());
        return result;
	}



	/**
	 * 检查参数的正确性
	 * @param request
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request) {
		// 唯一标识
		String sign = request.getParameter("sign");
		// 检查参数正确性
		if (Validator.isNull(sign)) {
			return jsonMessage("请求参数非法", "1");
		}
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			return jsonMessage("请求参数非法", "1");
		}
		Integer userId = SecretUtil.getUserId(sign);
		if (userId == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		return null;
	}
	
	/**
     * 用户绑卡
     *
     * @param request
     * @return
     */
    @RequestMapping(AppBindCardDefine.BINDCARD_ACTION)
    public ModelAndView bindCardPlus(HttpServletRequest request) {
        LogUtil.startLog(THIS_CLASS, AppBindCardDefine.REQUEST_BINDCARD);
        ModelAndView modelAndView = new ModelAndView();
        // 检查参数
        JSONObject checkResult = checkParam(request);
        if (checkResult != null) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "参数异常");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 唯一标识
        String sign = request.getParameter("sign");
        String cardNo = request.getParameter("cardNo");
        Integer userId = SecretUtil.getUserId(sign);
        if (Validator.isNull(cardNo)) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "获取银行卡号为空");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        if (userId == null || userId == 0) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 检查验证码是否正确
        String code = request.getParameter("code");
        logger.info("输入验证码code is: {}", code);
        if (Validator.isNull(code)) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "验证码无效");
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 检查验证码是否正确
        String lastSrvAuthCode = request.getParameter("lastSrvAuthCode");
        logger.info("输入验证码lastSrvAuthCode is: {}", lastSrvAuthCode);
        if (Validator.isNull(lastSrvAuthCode)) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "请先发送短信");
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }

        String mobile = request.getParameter("telNo");
        if (Validator.isNull(mobile)) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "手机号不能为空");
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 取得用户在汇付天下的客户号
        BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        UsersInfo usersInfo = userBindCardService.getUsersInfoByUserIdTrue(userId);
        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(StringUtil.valueOf(userId));
        bean.setLogRemark("用户绑卡增强");
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_CARD_BIND_PLUS);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(mobile);// 手机号
        bean.setCardNo(cardNo);// 银行卡号
        bean.setLastSrvAuthCode(lastSrvAuthCode);
        bean.setSmsCode(code);
        bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
        LogAcqResBean logAcq = new LogAcqResBean();
        logAcq.setCardNo(cardNo);
        bean.setLogAcqResBean(logAcq);
        BankCallBean retBean=null;
        // 跳转到江西银行画面
        try {
            retBean = BankCallUtils.callApiBg(bean);
        } catch (Exception e) {
            logger.error("调用银行接口失败", e);
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
            
        }
        
     // 回调数据处理
        if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
         // 执行结果(失败)
            String message = "";
            if(retBean==null){
                message = "绑卡失败，请重试";
            }else{
                message = this.userBindCardService.getBankRetMsg(retBean.getRetCode());
            }
             
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因:" + message);
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        try {
            // 绑卡后处理
            this.userBindCardService.updateAfterBindCard(bean);
            List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(userId+"");
            
            if (accountBankList != null && accountBankList.size() > 0) {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");
                baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_SUCCESS_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            } else {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "银行处理中，请稍后查看");
                baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_HANDLING_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                LogUtil.endLog(THIS_CLASS, AppBindCardDefine.REQUEST_BINDCARD);
                return modelAndView;
                
            }
            
            
        } catch (Exception e) {
            // 执行结果(失败)
            e.printStackTrace();
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "执行失败");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ AppBindCardDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, AppBindCardDefine.REQUEST_BINDCARD);
            return modelAndView;
        }
   
    }
	
}
