package com.hyjf.app.bank.user.autoplus;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AppAutoController;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;


/**
 * 
 * 自动投标自动债转授权
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月20日
 * @see 上午10:16:01
 */
@Controller
@RequestMapping(value = AutoPlusDefine.REQUEST_MAPPING)
public class AutoPlusController extends BaseController {

    private Logger _log = LoggerFactory.getLogger(AutoPlusController.class);
    
    @Autowired
    private AutoPlusService autoPlusService;

	@Autowired
	private BankOpenService openAccountService;

	/**
	 * 发送出借授权和债转授权短信验证码
	 * 
	 * @param request
	 * @param userAutoType
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = AutoPlusDefine.USER_AUTH_SENDSMS_ACTION)
    @ResponseBody
	public AutoPlusResultBean sendSmsCode(HttpServletRequest request, @RequestParam String userAutoType,
			@RequestParam(value = "mobile", required = false) String mobile) {

		String returnRequest = BaseDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING
				+ AutoPlusDefine.USER_AUTH_SENDSMS_ACTION;
		AutoPlusResultBean result = new AutoPlusResultBean(returnRequest);
		String sign = request.getParameter("sign");

		if (StringUtils.isBlank(sign)) {
			result.setStatusDesc("sign不能空...");
			return result;
		}
        Integer userId = SecretUtil.getUserId(sign);
		_log.info("userId is:{}, userAutoType is: {}", userId, userAutoType);

		HjhUserAuth userAuth = autoPlusService.getHjhUserAuthByUserId(userId);
		if (userAuth != null) {
			if ("0".equals(userAutoType) && userAuth.getAutoInvesStatus() == 1) {
				// 自动投标检查
				result.setUserAutoStatus(1);
				result.setStatusDesc("自动投标已授权");
				return result;
			}
			if ("1".equals(userAutoType) && userAuth.getAutoCreditStatus() == 1) {
				// 自动债转检查
				result.setUserAutoStatus(1);
				result.setStatusDesc("自动债转已授权");
				return result;
			}
		}

		// 银行接口调用需要的业务交易代码
		String srvTxCode;
		if ("0".equals(userAutoType)) {
			srvTxCode = BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS;
		} else if ("1".equals(userAutoType)) {
			srvTxCode = BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUS;
		} else {
			result.setStatusDesc("userAutoType参数类型错误");
			return result;
		}

		if (StringUtils.isBlank(mobile)) {
			mobile = openAccountService.getUsersMobile(userId);
		}

		// 调用短信发送接口
		BankCallBean mobileBean = this.openAccountService.sendSms(userId, srvTxCode, mobile,
				BankCallConstant.CHANNEL_APP);
		if (Validator.isNull(mobileBean)) {
			result.setStatusDesc("短信验证码发送失败，请稍后再试！");
			return result;
		}
		// 短信发送返回结果码
		String retCode = mobileBean.getRetCode();
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
				&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			result.setStatusDesc("短信验证码发送失败！");
			return result;
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode())) {
			result.setStatusDesc("短信验证码发送失败！");
			return result;
		}

		// 业务授权码
		String srvAuthCode = mobileBean.getSrvAuthCode();
		result.setSrvAuthCode(srvAuthCode);

		result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
        result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
		return result;
	}

	/**
	 * 自动投标授权url获取
	 *
	 * @return
	 */
	@RequestMapping(AutoPlusDefine.GET_USER_AUTH_INVEST_URL)
    @ResponseBody
	public AutoPlusResultBean getUserAuthInvesUrl(HttpServletRequest request, @RequestParam String srvAuthCode,
			@RequestParam String code) {

		String returnRequest = BaseDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING
				+ AutoPlusDefine.GET_USER_AUTH_INVEST_URL;
		AutoPlusResultBean result = new AutoPlusResultBean(returnRequest);
		String sign = request.getParameter("sign");

		if (StringUtils.isBlank(sign)) {
			result.setStatusDesc("sign不能空");
			return result;
		}

        if (StringUtils.isBlank(code) || StringUtils.isBlank(srvAuthCode)) {
            result.setStatusDesc("验证码或前导业务码不能为空");
            return result;
        }

        Integer userId = SecretUtil.getUserId(sign);
		_log.info("userId is:{}", userId);
		_log.info("自动投标授权url获取参数列表，srvAuthCode is:{}, code is:{}", srvAuthCode, code);

		HjhUserAuth userAuth = autoPlusService.getHjhUserAuthByUserId(userId);
		if (userAuth != null && userAuth.getAutoInvesStatus() == 1) {
			result.setUserAutoStatus(1);
			result.setStatusDesc("自动投标已授权");
			return result;
		}

		String url = CustomConstants.HOST + BaseDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING + AutoPlusDefine.USER_AUTH_INVES_ACTION + "?code=" + code
				+ "&srvAuthCode=" + srvAuthCode;
		result.setAuthUrl(url);

		result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
        result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
		return result;
	}

	/**
	 * 自动债转授权url获取
	 *
	 * @return
	 */
	@RequestMapping(AutoPlusDefine.GET_USER_AUTH_CREDIT_URL)
    @ResponseBody
	public AutoPlusResultBean getUserAuthCreditUrl(HttpServletRequest request, @RequestParam String srvAuthCode,
			@RequestParam String code) {

		String returnRequest = BaseDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING
				+ AutoPlusDefine.GET_USER_AUTH_CREDIT_URL;
		AutoPlusResultBean result = new AutoPlusResultBean(returnRequest);
		String sign = request.getParameter("sign");

		if (StringUtils.isBlank(sign)) {
			result.setStatusDesc("sign不能空");
			return result;
		}

        if (StringUtils.isBlank(code) || StringUtils.isBlank(srvAuthCode)) {
            result.setStatusDesc("验证码或前导业务码不能为空");
            return result;
        }

        Integer userId = SecretUtil.getUserId(sign);
		_log.info("userId is:{}", userId);
		_log.info("自动债转授权url获取参数列表，srvAuthCode is:{}, code is:{}", srvAuthCode, code);

		HjhUserAuth userAuth = autoPlusService.getHjhUserAuthByUserId(userId);
		if (userAuth != null && userAuth.getAutoCreditStatus() == 1) {
			result.setUserAutoStatus(1);
			result.setStatusDesc("自动投标已授权");
			return result;
		}

		String url =  CustomConstants.HOST + BaseDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING + AutoPlusDefine.USER_AUTH_CREDIT_ACTION + "?code=" + code
				+ "&srvAuthCode=" + srvAuthCode;
		result.setAuthUrl(url);

		result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
        result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
		return result;
	}



    
    /**
     * 用户授权自动出借
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_ACTION)
    public ModelAndView userAuthInves(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        
        String srvAuthCode = request.getParameter("srvAuthCode");
        String code = request.getParameter("code");
        JSONObject checkResult = checkParam(request);
        if (checkResult != null) {
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "非法参数！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 唯一标识
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign);
        if (userId == null || userId == 0) {
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 检查数据是否完整
        if (Validator.isNull(srvAuthCode) || Validator.isNull(code)) {
            
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "验证码或前导业务码不能为空");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        Users users = autoPlusService.getUsers(userId);
        if (users.getBankOpenAccount()==0) {// 未开户
            
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未设置交易密码");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 判断是否授权过
        HjhUserAuth hjhUserAuth=autoPlusService.getHjhUserAuthByUserId(users.getUserId());
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1){
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户自动出借已授权,无需重复授权");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // token
        String token = request.getParameter("token");
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(users,1,srvAuthCode,code,sign,token);
        
        // 插入日志
        this.autoPlusService.insertUserAuthLog(users.getUserId(), bean,2,BankCallConstant.QUERY_TYPE_1);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_AUTH_INVES_ACTION);
        } catch (Exception e) {
            _log.error("调用江西银行接口异常...", e);
            
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.errorLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_AUTH_INVES_ACTION, e);
        }
        return modelAndView;
    }



    /**
     * 用户授权自动出借同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(@ModelAttribute BankCallBean bean) {
        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION, "[出借人自动投标签约增强同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();

        _log.info("bean: {}",JSONObject.toJSONString(bean));
        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set("userAutoType","0");
            baseMapBean.set(CustomConstants.APP_STATUS_DESC,"用户授权自动出借失败,失败原因：" + autoPlusService.getBankRetMsg(bean.getRetCode()));
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "请联系客服");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }

        String logUserId = bean.getLogUserId();
        if(StringUtils.isNotBlank(logUserId)){
            Integer userId = Integer.parseInt(logUserId); // 用户ID
            HjhUserAuth hjhUserAuth = this.autoPlusService.getHjhUserAuthByUserId(userId);
            // 判断用户授权自动出借是否已成功
            if (hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1) {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set("message","用户授权自动出借成功");
                baseMapBean.set("userAutoType","0");
                baseMapBean.set("autoInvesStatus",hjhUserAuth.getAutoInvesStatus()+"");
                baseMapBean.set("autoCreditStatus",hjhUserAuth.getAutoCreditStatus()+"");
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_SUCCESS_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }

            //出借人签约状态查询
            BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, BankCallConstant.QUERY_TYPE_1);
            LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);

            try {
				if (retBean != null && "1".equals(retBean.getState())) {
                    modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                    hjhUserAuth = this.autoPlusService.getHjhUserAuthByUserId(userId);
                    Integer autoCreditStatus = 0;
                    if (hjhUserAuth != null){
                        _log.info("hjhUserAuth为空");
                        autoCreditStatus = hjhUserAuth.getAutoCreditStatus();
                    }
                    BaseMapBean baseMapBean = new BaseMapBean();
                    baseMapBean.set("autoInvesStatus", 1 + "");
                    baseMapBean.set("autoCreditStatus", autoCreditStatus + "");
                    baseMapBean.set("message", "用户授权自动出借成功");
                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
                    baseMapBean.setCallBackAction(CustomConstants.HOST + AutoDefine.JUMP_HTML_SUCCESS_PATH);
                    modelAndView.addObject("callBackForm", baseMapBean);
                    return modelAndView;
                }
            } catch (Exception e) {
                _log.error("更新签约状态和日志表异常...", e);
                LogUtil.errorLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, e);
            }

            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS_DESC,"");
            baseMapBean.set("userAutoType","0");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "请联系客服");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[用户授权自动出借完成后,回调结束]");
        }

        return modelAndView;
    }

    


    /**
     * 用户授权自动出借异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    public String userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动出借异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);

        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                //出借人签约状态查询
				BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, BankCallConstant.QUERY_TYPE_1);
				LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
				if (retBean != null && "1".equals(retBean.getState())) {
					// 更新签约状态和日志表
					this.autoPlusService.updateUserAuthInves(userId, bean);
				}
               
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION, e);
            }
        }
        LogUtil.endLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动出借完成后,回调结束]");
        result.setMessage("用户授权自动出借成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
    
    
    
    /**
     * 用户授权自动债转
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AutoPlusDefine.USER_AUTH_CREDIT_ACTION)
    public ModelAndView userAuthCredit(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_CREDIT_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        
        String srvAuthCode = request.getParameter("srvAuthCode");
        String code = request.getParameter("code");
        JSONObject checkResult = checkParam(request);
        if (checkResult != null) {
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "非法参数！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 唯一标识
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign);
        if (userId == null || userId == 0) {
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 检查数据是否完整
        if (Validator.isNull(srvAuthCode) || Validator.isNull(code)) {
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "验证码或前导业务码不能为空");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        Users users = autoPlusService.getUsers(userId);
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未设置交易密码");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        // 判断是否授权过
        HjhUserAuth hjhUserAuth=autoPlusService.getHjhUserAuthByUserId(users.getUserId());
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoCreditStatus()==1){
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户已授权,无需重复授权");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // token
        String token = request.getParameter("token");
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(users,2,srvAuthCode,code,sign,token);
        // 插入日志
        this.autoPlusService.insertUserAuthLog(users.getUserId(), bean,2,BankCallConstant.QUERY_TYPE_2);
        // 跳转到江西银行画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_AUTH_CREDIT_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.errorLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_AUTH_CREDIT_ACTION, e);
        }
        return modelAndView;
    }

    /**
     * 用户授权自动债转同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AutoPlusDefine.USER_AUTH_CREDIT_RETURN_ACTION)
    public ModelAndView userAuthCreditReturn(@ModelAttribute BankCallBean bean) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION, "[出借人自动债转签约增强同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set("userAutoType","1");
            baseMapBean.set("message","用户授权自动债转失败,失败原因：" + autoPlusService.getBankRetMsg(bean.getRetCode()));
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "请联系客服");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        HjhUserAuth hjhUserAuth = this.autoPlusService.getHjhUserAuthByUserId(userId);
        // 判断用户授权自动出借是否已成功
        if (hjhUserAuth!=null&&hjhUserAuth.getAutoCreditStatus()==1) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set("userAutoType","1");
            baseMapBean.set("autoInvesStatus",hjhUserAuth.getAutoInvesStatus()+"");
            baseMapBean.set("autoCreditStatus",hjhUserAuth.getAutoCreditStatus()+"");
            baseMapBean.set("message","用户授权自动债转成功");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        
        
        //出借人签约状态查询
        BankCallBean retBean=autoPlusService.getUserAuthQUery(userId,BankCallConstant.QUERY_TYPE_2);
        LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
        
        try {
			if (retBean != null && "1".equals(retBean.getState())) {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                hjhUserAuth = this.autoPlusService.getHjhUserAuthByUserId(userId);
                baseMapBean.set("userAutoType","1");
                Integer autoInvesStatus = 0;
                if(hjhUserAuth!=null){
                    autoInvesStatus = hjhUserAuth.getAutoInvesStatus();
                }
                baseMapBean.set("autoInvesStatus",autoInvesStatus +"");
                baseMapBean.set("autoCreditStatus",1+"");
                baseMapBean.set("message","用户授权自动债转成功");
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_SUCCESS_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.errorLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, e);
        }

        modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        baseMapBean.set("message","");
        baseMapBean.set("userAutoType","1");
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, "请联系客服");
        baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[用户授权自动出借完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 用户授权自动债转异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AutoPlusDefine.USER_AUTH_CREDIT_BGRETURN_ACTION)
    public String userAuthCreditBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_CREDIT_BGRETURN_ACTION, "[用户授权自动债转异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);

        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                //出借人签约状态查询
				BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, BankCallConstant.QUERY_TYPE_2);
                LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
				if (retBean != null && "1".equals(retBean.getState())) {
                    // 更新签约状态和日志表
                    this.autoPlusService.updateUserAuthInves(userId,bean);
                }
               
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION, e);
            }
        }
        LogUtil.endLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_CREDIT_BGRETURN_ACTION, "[用户授权自动债转完成后,回调结束]");
        result.setMessage("用户授权自动债转成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
    
    
    /**
     * 检查参数的正确性
     *
     * @param transAmt
     * @param openBankId
     * @param rechargeType
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request) {

        //String version = request.getParameter("version");
        // 网络状态
        // String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // token
        String token = request.getParameter("token");
        // 唯一标识
        String sign = request.getParameter("sign");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // Order
        String order = request.getParameter("order");

        // 检查参数正确性  || Validator.isNull(netStatus)
        if (Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
                || Validator.isNull(order)) {
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
    
    
    // 组装发往江西银行参数
    private BankCallBean getCommonBankCallBean(Users users,int type,String lastSrvAuthCode,String smsCode, String sign, String token) {
        
        String remark = "";
        String txcode = "";
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoPlusDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoPlusDefine.REQUEST_HOME + AutoPlusDefine.REQUEST_MAPPING;
        String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token;
        
        BankCallBean bean = new BankCallBean();
        if(type==1){// 2.4.4.出借人自动投标签约增强
            retUrl +=AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION + ".do";
            bgRetUrl+=AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do";
            remark = "出借人自动投标签约增强";
            txcode = BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS;
            HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH);
            bean.setDeadline(GetDate.date2Str(GetDate.countDate(1,config.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));// 签约到期时间
            bean.setTxAmount(config.getPersonalMaxAmount()+"");// 单笔投标金额的上限
            bean.setTotAmount("1000000000");// 自动投标总金额上限（不算已还金额）
        } else if(type==2){// 2.4.8.出借人自动债权转让签约增强
            retUrl +=AutoPlusDefine.USER_AUTH_CREDIT_RETURN_ACTION + ".do";
            bgRetUrl+=AutoPlusDefine.USER_AUTH_CREDIT_BGRETURN_ACTION + ".do";
            remark = "出借人自动债权转让签约增强";
            txcode = BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS;
        }
        String orderId=GetOrderIdUtils.getOrderId2(users.getUserId());
        // 取得用户在江西银行的客户号
        BankOpenAccount bankOpenAccount = autoPlusService.getBankOpenAccount(users.getUserId());
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_PLUS);
        bean.setTxCode(txcode);
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_APP);
        
        
        bean.setAccountId(bankOpenAccount.getAccount());// 电子账号
        bean.setOrderId(orderId);
        bean.setForgotPwdUrl(forgetPassworedUrl);//忘记密码通知地址
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setNotifyUrl(bgRetUrl);//异步通知地址
        bean.setLastSrvAuthCode(lastSrvAuthCode);// 前导业务授权码
        bean.setSmsCode(smsCode);// 手机验证码
       
        
        // 操作者ID
        bean.setLogUserId(String.valueOf(users.getUserId()));
        bean.setLogOrderId(orderId);
        bean.setLogRemark(remark);
        bean.setLogClient(0);
        return bean;
    }

    
}
