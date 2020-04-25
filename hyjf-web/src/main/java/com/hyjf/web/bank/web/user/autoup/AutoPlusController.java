package com.hyjf.web.bank.web.user.autoup;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;


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

    Logger _log = LoggerFactory.getLogger(AutoPlusController.class);
    
    @Autowired
    private AutoPlusService authService;
	/**
	 * 用户进入发送短信验证码页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AutoPlusDefine.USER_AUTH_ACTION)
	public ModelAndView init(HttpServletRequest request, String type,HttpServletResponse response) {

		//LogUtil.startLog(THIS_CLASS, BankOpenDefine.BANKOPEN_INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_TO_ACTION);
		WebViewUser user = WebUtils.getUser(request);

		modelAndView.addObject("authtype", type);
		// session过期后跳转回登陆页面
		if (null == user) {
			modelAndView = new ModelAndView("/user/login/login");
			return modelAndView;
		}
		modelAndView.addObject("mobile", user.getMobile());
		return modelAndView;
	}
    /**
     * 
     * 前导发送短信验证码
     * @author sss
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = AutoPlusDefine.SENDCODE_ACTION, produces = "application/json; charset=utf-8")
    public BaseAjaxResultBean sendCode(HttpServletRequest request,String type, HttpServletResponse response) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.SENDCODE_ACTION);
        BaseAjaxResultBean result = new BaseAjaxResultBean();
        String message = "验证码发送失败";
        String txcode = "";
        boolean status = false;
        if (Validator.isNull(type)) {
            result.setMessage("参数错误");
            result.setStatus(status);
            return result;
        }
        // 获取登陆用户userId
        WebViewUser user = WebUtils.getUser(request);
        if(user==null){
            result.setMessage("未登录,请先登录");
            result.setStatus(status);
            return result;
        }
        int userId = user.getUserId();
        if (Validator.isNull(userId)) {
            result.setMessage("未登录,请先登录");
            result.setStatus(status);
            return result;
        }
        // 获取用户的手机号
        Users users = this.authService.getUsers(userId);
        if (Validator.isNull(users)) {
            result.setMessage(message);
            result.setStatus(status);
            return result;
        }
        String mobile = users.getMobile();
        if (StringUtils.isBlank(mobile)) {
            result.setMessage("用户信息错误，未获取到用户的手机号码！");
            result.setStatus(status);
            return result;
        } 
        
        if("1".equals(type)){// 2.4.4.出借人自动投标签约增强
            txcode = BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS;
        } else if("2".equals(type)){// 2.4.8.出借人自动债权转让签约增强
            txcode = BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS;
        }
        
        _log.info("===================== "+txcode+"开始获取手机验证码,userID = " + userId);
        // 调用短信发送接口
        BankCallBean mobileBean = this.authService.sendSms(userId, txcode,
                mobile, BankCallConstant.CHANNEL_PC);
        if (Validator.isNull(mobileBean)) {
            message = "短信验证码发送失败，请稍后再试！";
            result.setMessage(message);
            result.setStatus(status);
            return result;
        }
        _log.info("====================="+txcode+"获取手机验证码完毕,userID = " + userId);
        // 短信发送返回结果码
        String retCode = mobileBean.getRetCode();
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
                && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            message = "短信验证码发送失败！";
            result.setMessage(message);
            result.setStatus(status);
            return result;
        }
        if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            message = "短信验证码发送失败！";
            result.setMessage(message);
            result.setStatus(status);
            return result;
        }
        // 业务授权码
        String srvAuthCode = mobileBean.getSrvAuthCode();
        status = true;
        result.setMessage(srvAuthCode);
        result.setStatus(status);
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
        
        String lastSrvAuthCode = request.getParameter("lastSrvAuthCode");
        String smsCode = request.getParameter("smsCode");
        
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        // 检查数据是否完整
        if (Validator.isNull(lastSrvAuthCode) || Validator.isNull(smsCode)) {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "参数错误，请重试");
            return modelAndView;
        }
        Users users=authService.getUsers(user.getUserId());
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }
        
        // 判断是否授权过  todo
        if(authService.checkIsAuth(user.getUserId(),BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS)){
        	_log.info("用户已经授权过自动出借,userID："+user.getUserId());
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户已授权,无需重复授权");
            return modelAndView;
        }
       
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(user,1,lastSrvAuthCode,smsCode);
        // 插入日志
        this.authService.insertUserAuthLog(user.getUserId(), bean,0,BankCallConstant.QUERY_TYPE_1);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_AUTH_INVES_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");

            LogUtil.errorLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_AUTH_INVES_ACTION, e);
        }

        return modelAndView;
    }

    // 组装发往江西银行参数
    private BankCallBean getCommonBankCallBean(WebViewUser users,int type,String lastSrvAuthCode,String smsCode) {
        
        String remark = "";
        String txcode = "";
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoPlusDefine.REQUEST_MAPPING;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoPlusDefine.REQUEST_MAPPING;
        // 构造函数已经设置
        // 版本号  交易代码  机构代码  银行代码  交易日期  交易时间  交易流水号   交易渠道
        BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,txcode,users.getUserId(),"000002");
        
        if(type==1){// 2.4.4.出借人自动投标签约增强
            remark = "出借人自动投标签约增强";
            retUrl +=AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION + ".do";
            bgRetUrl+=AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do";
            bean.setTxCode(BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS);
            HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH);
            bean.setDeadline(GetDate.date2Str(GetDate.countDate(1,config.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));// 签约到期时间
            bean.setTxAmount(config.getPersonalMaxAmount()+"");// 单笔投标金额的上限
            bean.setTotAmount("1000000000");// 自动投标总金额上限（不算已还金额）
        } else if(type==2){// 2.4.8.出借人自动债权转让签约增强
            remark = "出借人自动债权转让签约增强";
            retUrl +=AutoPlusDefine.USER_CREDIT_AUTH_INVES_RETURN_ACTION + ".do";
            bgRetUrl+=AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION + ".do";
            bean.setTxCode(BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS);
            
        }
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_PLUS);
        bean.setOrderId(bean.getLogOrderId());
        bean.setAccountId(users.getBankAccount());// 电子账号
        bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);//忘记密码通知地址
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setNotifyUrl(bgRetUrl);//异步通知地址
        bean.setLastSrvAuthCode(lastSrvAuthCode);// 前导业务授权码
        bean.setSmsCode(smsCode);// 
        bean.setLogRemark(remark);
        
        return bean;
    }

    /**
     * 用户授权自动出借同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION, "[出借人自动投标签约增强同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        
        // 返回失败
        // 出借人签约状态查询
        _log.info("自动出借授权同步回调调用查询接口查询状态");
        BankCallBean retBean = authService.getUserAuthQUery(user.getUserId(), BankCallConstant.QUERY_TYPE_1);
        _log.info("自动出借授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()));
        bean=retBean;
        // 返回失败
        if (bean.getRetCode() != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())
                && "1".equals(bean.getState())) {
            // 成功
            /*try {
                this.authService.updateUserAuthInves(Integer.parseInt(bean.getLogUserId()), bean);
            } catch (Exception e) {
                _log.info("自动出借授权同步插入数据库失败，错误原因:" + e.getMessage());
                modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
                modelAndView.addObject("message", "请联系客服!");
                return modelAndView;
            }*/

        } else {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("message", "自动投标开通成功");
        LogUtil.endLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION, "[出借人自动投标签约增强完成后,回调结束]");
        //WebViewUser user = WebUtils.getUser(request);
        HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(user.getUserId());
        if(hjhUserAuth==null){
            modelAndView.addObject("typeURL", "2");
        }else if(hjhUserAuth.getAutoCreditStatus()==0) {
        	modelAndView.addObject("typeURL", "2");
        }else {
        	modelAndView.addObject("typeURL", "0");
        }
        // add by liuyang 神策数据埋点追加 20180717 start
        // 授权类型:自动出借授权
        modelAndView.addObject("autoType", BankCallConstant.QUERY_TYPE_1);
        // add by liuyang 神策数据埋点追加 20180717 end
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
        Users user = this.authService.getUsers(userId);

        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
               // 更新签约状态和日志表
               this.authService.updateUserAuthInves(userId,bean);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION, e);
            }
        }
        LogUtil.endLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动出借完成后,回调结束]");
        result.setMessage("自动投标开通成功");
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
    @RequestMapping(AutoPlusDefine.USER_CREDIT_AUTH_INVES_ACTION)
    public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_CREDIT_AUTH_INVES_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        
        String lastSrvAuthCode = request.getParameter("lastSrvAuthCode");
        String smsCode = request.getParameter("smsCode");
        
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        // 检查数据是否完整
        if (Validator.isNull(lastSrvAuthCode) || Validator.isNull(smsCode)) {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "参数错误，请重试");
            return modelAndView;
        }
        Users users=authService.getUsers(user.getUserId());
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }
        
        // 判断是否授权过  todo
        HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(user.getUserId());
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoCreditStatus().intValue()==1){
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户已授权,无需重复授权");
            return modelAndView;
        }
       
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(user,2,lastSrvAuthCode,smsCode);
        // 插入日志
        this.authService.insertUserAuthLog(user.getUserId(), bean,0,BankCallConstant.QUERY_TYPE_2);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_CREDIT_AUTH_INVES_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
            LogUtil.errorLog(AutoPlusController.class.toString(), AutoPlusDefine.USER_CREDIT_AUTH_INVES_ACTION, e);
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
    @RequestMapping(AutoPlusDefine.USER_CREDIT_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userCreditAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_CREDIT_AUTH_INVES_RETURN_ACTION, "[出借人自动债转签约增强同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        
        //出借人签约状态查询
        _log.info("自动债转授权同步回调调用查询接口查询状态");
        BankCallBean retBean=authService.getUserAuthQUery(user.getUserId(),BankCallConstant.QUERY_TYPE_2);
        _log.info("自动债转授权同步回调调用查询接口查询状态结束  结果为:"+(retBean==null?"空":retBean.getRetCode()));
        bean = retBean ; 
        // 返回失败
        if (bean.getRetCode()!=null&&BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())&& "1".equals(bean.getState())) {
         // 成功
            /*try {
                this.authService.updateUserAuthInves(Integer.parseInt(bean.getLogUserId()),bean);
            } catch (Exception e) {
                _log.info("自动债转授权同步插入数据库失败，错误原因:"+e.getMessage());
                modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
                modelAndView.addObject("message", "失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
                return modelAndView;
            }*/
           
        }else{
            modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        modelAndView = new ModelAndView(AutoPlusDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("message", "自动债转开通成功");
        LogUtil.endLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_CREDIT_AUTH_INVES_RETURN_ACTION, "[出借人自动债转签约增强完成后,回调结束]");
        //WebViewUser user = WebUtils.getUser(request);
        HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(user.getUserId());
        if(hjhUserAuth == null){
            modelAndView.addObject("typeURL", "1");
        }else if(hjhUserAuth.getAutoInvesStatus()==0) {
        	modelAndView.addObject("typeURL", "1");
        }else {
        	modelAndView.addObject("typeURL", "0");
        }
        // add by liuyang 神策数据埋点追加 20180717 start
        // 授权类型:自动出借授权
        modelAndView.addObject("autoType", BankCallConstant.QUERY_TYPE_2);
        // add by liuyang 神策数据埋点追加 20180717 end
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
    @RequestMapping(AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION)
    public String userCreditAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动债转异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.authService.getUsers(userId);

        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
               // 更新签约状态和日志表
               this.authService.updateUserAuthInves(userId,bean);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION, e);
            }
        }
        LogUtil.endLog(AutoPlusDefine.THIS_CLASS, AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动债转完成后,回调结束]");
        result.setMessage("用户授权自动债转成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
    
}
