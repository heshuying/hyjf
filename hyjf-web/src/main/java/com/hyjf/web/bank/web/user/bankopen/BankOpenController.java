/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.bank.web.user.bankopen;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(BankOpenDefine.CONTROLLER_NAME)
@RequestMapping(value = BankOpenDefine.REQUEST_MAPPING)
public class BankOpenController extends BaseController {
	Logger _log = LoggerFactory.getLogger(BankOpenController.class);
	@Autowired
	private BankOpenService openAccountService;

	@Autowired
    private UserOpenAccountPageService accountPageService;
	
	@Autowired
    private LoginService loginService;

    @Autowired
    private AgreementService agreementService;
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = BankOpenController.class.getName();

	/**
	 * 用户开户
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(BankOpenDefine.BANKOPEN_INIT_ACTION)
	public ModelAndView initOpenAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {

		LogUtil.startLog(THIS_CLASS, BankOpenDefine.BANKOPEN_INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankOpenDefine.BANKOPEN_INIT_PATH);
		WebViewUser user = WebUtils.getUser(request);
        int userId = user.getUserId();

        // 合规审批需求 add by huanghui  20181128 start
		if (user.getUserType() == 1){
		    // 企业用户 跳转到开户指南页面
            response.sendRedirect(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/user/regist/businessUsersGuide.do");
            return null;
        }
        // 合规审批需求 add by huanghui  20181128 start

        //协议名称 动态获得
        List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
        _log.info("设置交易密码错误进入");
        if(CollectionUtils.isNotEmpty(list)){
            //是否在枚举中有定义
            for (ProtocolTemplate p : list) {
                String protocolType = p.getProtocolType();
                String alia = ProtocolEnum.getAlias(protocolType);
                if (alia != null){
                    modelAndView.addObject(alia, p.getDisplayName());
                }
            }
        }
		boolean accountFlag = user.isBankOpenAccount();
		if (accountFlag) {
			modelAndView = new ModelAndView("redirect:/user/pandect/pandect.do");
			return modelAndView;
		}
		_log.info("设置交易密码错误不进入");
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(3);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(0);
        userOperationLogEntity.setRemark("");
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(user.getUsername());
        userOperationLogEntity.setUserRole(user.getRoleId());
        loginService.sendUserLogMQ(userOperationLogEntity);
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		modelAndView.addObject("logOrderId", logOrderId);
		modelAndView.addObject("mobile", user.getMobile());
		return modelAndView;
	}

	/**
	 * 检查手机号码或用户名唯一性
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BankOpenDefine.MOBILE_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean mobileCheck(HttpServletRequest request) {

		String param = request.getParameter("param");
		JSONObject info = new JSONObject();
		boolean mobileFlag = ValidatorCheckUtil.validateMobile(info, null, null, param, 11, false);
		if (mobileFlag) {
			boolean isMobile = Validator.isMobile(param);
			if (isMobile) {
				if (!openAccountService.existMobile(param)) {
					return true;
				}
			}
		}
		return false;
	}


	// 开户
    @RequestMapping(method = RequestMethod.POST,value =BankOpenDefine.BANKOPEN_OPEN_ACTION)
    public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankOpenBean accountBean) {
	    _log.info("web请求页面开户，参数为："+JSONObject.toJSONString(accountBean));
	    ModelAndView modelAndView = new ModelAndView(BankOpenDefine.BANKOPEN_ERROR_PATH);
	    try {
	        // 获取登陆用户userId
	        Integer userId = WebUtils.getUserId(request);
            // 验证请求参数
	        if (Validator.isNull(userId)) {
	            modelAndView.addObject("message", "用户未登陆，请先登陆！");
                return modelAndView;
	        }
	        Users user = this.openAccountService.getUsers(userId);
	        if (Validator.isNull(user)) {
	            modelAndView.addObject("message", "获取用户信息失败！");
                return modelAndView;
	        }
	        
            // 手机号
            if (Validator.isNull(accountBean.getMobile())) {
                modelAndView.addObject("message", "手机号不能为空！");
                return modelAndView;
            }
            // 姓名
            if (Validator.isNull(accountBean.getTrueName())) {
                modelAndView.addObject("message", "真实姓名不能为空！");
                return modelAndView;
            }else{
                //判断真实姓名是否包含空格
                if (!ValidatorCheckUtil.verfiyChinaFormat(accountBean.getTrueName())) {
                    modelAndView.addObject("message", "真实姓名不能包含空格！");
                    return modelAndView;
                }
                //判断真实姓名的长度,不能超过10位
                if (accountBean.getTrueName().length() > 10) {
                    modelAndView.addObject("message", "真实姓名不能超过10位！");
                    return modelAndView;
                }
            }
            // 身份证号
            if (Validator.isNull(accountBean.getIdNo())) {
                modelAndView.addObject("message", "身份证号不能为空！");
                return modelAndView;
            }

            if (accountBean.getIdNo().length() != 18) {
                modelAndView.addObject("message", "身份证号格式不正确！");
                return modelAndView;
            }
            String idNo = StringUtils.upperCase(accountBean.getIdNo().trim());
            accountBean.setIdNo(idNo);
            //增加身份证唯一性校验
            boolean isOnly = openAccountService.checkIdNo(idNo);
            if (!isOnly) {
                modelAndView.addObject("message", "身份证已存在！");
                return modelAndView;
            }
            if(!Validator.isMobile(accountBean.getMobile())){
                modelAndView.addObject("message", "手机号格式错误！");
                return modelAndView;
            }
            String mobile = this.openAccountService.getUsersMobile(userId);
            if (StringUtils.isBlank(mobile)) {
                if (StringUtils.isNotBlank(accountBean.getMobile())) {
                    if(!openAccountService.existMobile(accountBean.getMobile())){
                        mobile = accountBean.getMobile();
                    }else{
                        modelAndView.addObject("message", "用户信息错误，手机号码重复！");
                        return modelAndView;
                    }
                } else {
                    modelAndView.addObject("message", "用户信息错误，未获取到用户的手机号码！");
                    return modelAndView;
                }
            } else {
                if (StringUtils.isNotBlank(accountBean.getMobile()) && !mobile.equals(accountBean.getMobile())) {
                    modelAndView.addObject("message", "用户信息错误，用户的手机号码错误！");
                    return modelAndView;
                }
            }
            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankOpenDefine.REQUEST_MAPPING
                    + BankOpenDefine.RETURL_SYN_ACTION + ".do?phone="+accountBean.getMobile();
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankOpenDefine.REQUEST_MAPPING
                    + BankOpenDefine.RETURL_ASY_ACTION + ".do?phone="+accountBean.getMobile();

            OpenAccountPageBean openBean = new OpenAccountPageBean();
            PropertyUtils.copyProperties(openBean, accountBean);
            openBean.setChannel(BankCallConstant.CHANNEL_PC);
            openBean.setUserId(userId);
            openBean.setIp(CustomUtil.getIpAddr(request));
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            openBean.setCoinstName("汇盈金服");
            openBean.setPlatform("0");
            // 账户用途 写死
            /*00000-普通账户
            10000-红包账户（只能有一个）
            01000-手续费账户（只能有一个）
            00100-担保账户*/
            openBean.setAcctUse("00000");
            /**
             *  1：出借角色
                2：借款角色
                3：代偿角色
             */
            openBean.setIdentity("1");
            modelAndView = accountPageService.getCallbankMV(openBean);
            //保存开户日志
            boolean isUpdateFlag = this.openAccountService.updateUserAccountLog(userId, user.getUsername(), openBean.getMobile(), openBean.getOrderId(),CustomConstants.CLIENT_PC ,openBean.getTrueName(),openBean.getIdNo(),openBean.getCardNo());
            if (!isUpdateFlag) {
                _log.info("保存开户日志失败,手机号:[" + openBean.getMobile() + "],用户ID:[" + userId + "]");
                modelAndView.addObject("message", "操作失败！");
                return modelAndView;
            }
            _log.info("开户end");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("开户异常,异常信息:[" + e.toString() + "]");
            modelAndView.addObject("message", "开户异常！");
            return modelAndView;
        }
	}
	
    /**
     * 
     * 同步回调
     * @author sunss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(BankOpenDefine.RETURL_SYN_ACTION)
    public ModelAndView bankOpenReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
        _log.info("页面开户同步回调start,请求参数为：【" + JSONObject.toJSONString(bean, true) + "】");
        ModelAndView modelAndView = new ModelAndView(BankOpenDefine.BANKOPEN_ERROR_PATH);
        String isSuccess = request.getParameter("isSuccess");
        // 成功了
        if("1".equals(isSuccess)){
         // 成功
            try {
                Thread.sleep(1000);
                WebViewUser webUser = loginService.getWebViewUserByUserId(Integer.parseInt(bean.getLogUserId()));
                WebUtils.sessionLogin(request, response, webUser);
            } catch (Exception e) {
                LogUtil.errorLog(BankOpenDefine.RETURL_SYN_ACTION, "开户异步可能比同步先到", e);
            }
            modelAndView = new ModelAndView(BankOpenDefine.BANKOPEN_ERROR_SUCCESS);
            modelAndView.addObject("message", "开户成功");
            _log.info("开户成功,用户ID:[" + bean.getLogUserId() + "]");
            return modelAndView;
        }
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 银行卡与姓名不符
        if ("CP9919".equals(retCode)) {
            _log.info("开户失败,银行卡与姓名不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            modelAndView.addObject("message", "银行卡与姓名不符！");
            return modelAndView;
        }
        // 银行卡与证件不符
        if ("CP9920".equals(retCode)) {
            _log.info("开户失败,银行卡与证件不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            modelAndView.addObject("message", "银行卡与证件不符");
            return modelAndView;
        }
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = openAccountService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            modelAndView.addObject("message", retMsg);
            return modelAndView;
        }
        if (bean != null 
                && ((BankCallConstant.RESPCODE_SUCCESS.equals(retCode))
                        || "JX900703".equals(retCode))) {
            // 成功
            try {
                Thread.sleep(1000);
                WebViewUser webUser = loginService.getWebViewUserByUserId(Integer.parseInt(bean.getLogUserId()));
                WebUtils.sessionLogin(request, response, webUser);
            } catch (Exception e) {
                LogUtil.errorLog(BankOpenDefine.RETURL_SYN_ACTION, "开户异步可能比同步先到", e);
            }
            modelAndView = new ModelAndView(BankOpenDefine.BANKOPEN_ERROR_SUCCESS);
            modelAndView.addObject("message", "开户成功");
            _log.info("开户成功,用户ID:[" + bean.getLogUserId() + "]");
            return modelAndView;
        } else {
            String retMsg = openAccountService.getBankRetMsg(retCode);
            modelAndView.addObject("message", retMsg);
            return modelAndView;
        }
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(BankOpenDefine.RETURL_ASY_ACTION)
    public BankCallResult bankOpenBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        BankCallResult result = new BankCallResult();
        String phone = request.getParameter("phone");
        _log.info("页面开户异步回调start");
        String openclient = request.getParameter("openclient");
        bean.setLogClient(Integer.parseInt(openclient));
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 将开户记录状态改为4
            this.accountPageService.updateUserAccountLog(userId, bean.getLogOrderId(), 4);
            // 根据银行相应代码,查询错误信息
            String retMsg = accountPageService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,保存用户的开户信息
        boolean saveBankAccountFlag = this.accountPageService.updateUserAccount(bean);
        if (!saveBankAccountFlag) {
            _log.info("开户失败,保存用户的开户信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 保存银行卡信息
        boolean saveBankCardFlag = this.accountPageService.updateCardNoToBank(bean);
        if (!saveBankCardFlag) {
            _log.info("开户失败,保存银行卡信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,发送CA认证MQ
        this.openAccountService.sendCAMQ(String.valueOf(userId));
        CommonSoaUtils.listOpenAcc(userId);
        _log.info("开户成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }
    
}
