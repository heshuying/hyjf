package com.hyjf.web.bank.web.user.bindcardpage;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.web.user.login.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.bankopen.BankOpenDefine;
import com.hyjf.web.bank.web.user.bankwithdraw.BankWithdrawDefine;
import com.hyjf.web.bank.web.user.recharge.RechargeDefine;
import com.hyjf.web.bank.web.user.transpassword.TransPasswordDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginDefine;
import com.hyjf.web.util.WebUtils;

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
	@Autowired
	LoginService loginService;

	/**
     * 页面请求绑卡
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = BindCardPageDefine.REQUEST_BINDCARDPAGE)
	public ModelAndView bindCardPage(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, BindCardPageDefine.REQUEST_BINDCARDPAGE);
		ModelAndView modelAndView = new ModelAndView(BindCardPageDefine.BINDCARD_ERROR_PATH);
		WebViewUser user = WebUtils.getUser(request);// 用户ID
		String urlstatus = request.getParameter("urlstatus"); // 手机号
        if (user == null) {
            modelAndView.addObject("message", "用户未登陆");
            modelAndView.addObject("urlstatus", "login");
            modelAndView.addObject("url", BindCardPageDefine.HOST+LoginDefine.CONTROLLOR_REQUEST_MAPPING+LoginDefine.INIT+".do");
            modelAndView.addObject("buttonName", "去登录");
			return modelAndView;
        }
		logger.info("绑卡最新页面===");
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(10);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		loginService.sendUserLogMQ(userOperationLogEntity);
		BankOpenAccount bankOpenAccount = userBindCardService.getBankOpenAccount(user.getUserId());
		if (bankOpenAccount == null || Validator.isNull(bankOpenAccount.getAccount())) {
			modelAndView.addObject("message", "用户未开户");
			modelAndView.addObject("urlstatus", "open");
			modelAndView.addObject("url", BindCardPageDefine.HOST+BankOpenDefine.REQUEST_MAPPING+BankOpenDefine.BANKOPEN_INIT_ACTION+".do");
            modelAndView.addObject("buttonName", "去开户");
			return modelAndView;
		}
		Users users=userBindCardService.getUsersByUserId(user.getUserId());
		if(users.getIsSetPassword()==0){
			modelAndView.addObject("message", "未设置交易密码");
			modelAndView.addObject("urlstatus", "setpwd");
			modelAndView.addObject("url", BindCardPageDefine.HOST+TransPasswordDefine.REQUEST_MAPPING+TransPasswordDefine.SETPASSWORD_ACTION+".do");
            modelAndView.addObject("buttonName", "去设置交易密码");
			return modelAndView;
		}
		
		
		List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(user.getUserId() + "");
		if (accountBankList != null && accountBankList.size() > 0) {
			modelAndView.addObject("message", "已绑卡");
			modelAndView.addObject("urlstatus", urlstatus);
			if("recharge".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+RechargeDefine.REQUEST_MAPPING+
            			RechargeDefine.RECHARGEPAGE_MAPPING+".do");
                modelAndView.addObject("buttonName", "去充值");	
            }
            
            if("withdraw".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+BankWithdrawDefine.REQUEST_MAPPING+BankWithdrawDefine.TO_WITHDRAW+".do");
                modelAndView.addObject("buttonName", "去提现");	
            }
			return modelAndView;
		}
		UsersInfo userInfo=userBindCardService.getUsersInfoByUserIdTrue(user.getUserId());
		
		try {
			// 同步调用路径
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) 
					+ BindCardPageDefine.REQUEST_MAPPING + BindCardPageDefine.RETURL_SYN_ACTION + ".do?userId=" + user.getUserId()+"&urlstatus="+urlstatus+"&account="+bankOpenAccount.getAccount();
			// 异步调用路
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
					+ BindCardPageDefine.REQUEST_MAPPING + BindCardPageDefine.RETURL_ASY_ACTION + ".do?userId=" + user.getUserId()+"&urlstatus="+urlstatus+"&phone="+user.getMobile();
			// 拼装参数 调用江西银行
			String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL;
			BindCardPageBean bean = new BindCardPageBean();
			bean.setTxCode(BankCallConstant.TXCODE_BIND_CARD_PAGE);
			bean.setChannel(BankCallConstant.CHANNEL_PC);
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(userInfo.getIdcard());
			bean.setName(userInfo.getTruename());
			bean.setAccountId(bankOpenAccount.getAccount());
			bean.setUserIP(GetCilentIP.getIpAddr(request));
			bean.setUserId(user.getUserId());
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
			modelAndView.addObject("urlstatus", urlstatus);
			modelAndView.addObject("message", "调用银行接口异常");
			if("recharge".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+RechargeDefine.REQUEST_MAPPING+
            			RechargeDefine.RECHARGEPAGE_MAPPING+".do");
                modelAndView.addObject("buttonName", "返回重试");	
            }
            
            if("withdraw".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+BankWithdrawDefine.REQUEST_MAPPING+BankWithdrawDefine.TO_WITHDRAW+".do");
                modelAndView.addObject("buttonName", "返回重试");	
            }
			return modelAndView;
		}
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
        ModelAndView modelAndView = new ModelAndView(BindCardPageDefine.BINDCARD_ERROR_PATH);
        String frontParams = request.getParameter("frontParams");
        String isSuccess = request.getParameter("isSuccess");
        String urlstatus = request.getParameter("urlstatus");
        
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
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
        	modelAndView = new ModelAndView(BindCardPageDefine.BINDCARD_SUCCESS_PATH);
            modelAndView.addObject("urlstatus", urlstatus);
            if("recharge".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+RechargeDefine.REQUEST_MAPPING+
            			RechargeDefine.RECHARGEPAGE_MAPPING+".do");
                modelAndView.addObject("buttonName", "去充值");	
                /*ModelAndView mv = new ModelAndView("redirect:"+RechargeDefine.REQUEST_MAPPING+
            			RechargeDefine.RECHARGEPAGE_MAPPING+".do");
                return mv;*/
            }
            
            if("withdraw".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+BankWithdrawDefine.REQUEST_MAPPING+BankWithdrawDefine.TO_WITHDRAW+".do");
                modelAndView.addObject("buttonName", "去提现");	
                /*ModelAndView mv = new ModelAndView("redirect:"+BankWithdrawDefine.REQUEST_MAPPING+BankWithdrawDefine.TO_WITHDRAW+".do");
                return mv;*/
            }
            LogUtil.endLog(THIS_CLASS, BindCardPageDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        } else {
        	
        	modelAndView.addObject("urlstatus", urlstatus);
        	modelAndView.addObject("message", "绑卡失败");
        	if("recharge".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+RechargeDefine.REQUEST_MAPPING+
            			RechargeDefine.RECHARGEPAGE_MAPPING+".do");
                modelAndView.addObject("buttonName", "返回重试");	
            }
            
            if("withdraw".equals(urlstatus)){
            	modelAndView.addObject("url", BindCardPageDefine.HOST+BankWithdrawDefine.REQUEST_MAPPING+BankWithdrawDefine.TO_WITHDRAW+".do");
                modelAndView.addObject("buttonName", "返回重试");	
            }
            LogUtil.endLog(THIS_CLASS, BindCardPageDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
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
			boolean checkTender = RedisUtils.tranactionSet("bindCard" + bean.getLogOrderId(), 600);
			if(checkTender){
				// 保存银行卡信息
				userBindCardService.updateCardNoToBank(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        logger.info("页面绑卡成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }
}
