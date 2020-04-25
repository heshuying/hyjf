package com.hyjf.wechat.controller.user.bankopen;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.GetCilentIP;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.wechat.service.login.LoginService;
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
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 用户开户
 * @author jijun
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月27日
 * @see  09：10
 */

@Controller
@RequestMapping(value = OpenAccountDefine.REQUEST_MAPPING)
public class OpenAccountController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OpenAccountController.class);

    @Autowired
    private BankOpenService openAccountService;
    
    @Autowired
    private UserOpenAccountPageService accountPageService;
    @Autowired
    private LoginService loginService;
    /**
     * 当前controller名称
     */
    public static final String THIS_CLASS = OpenAccountController.class.getName();
    
    /**
     * 开户画面
     *
     * @param request
     * @param form
     * @return
     */
    @SignValidate
    @RequestMapping(value = OpenAccountDefine.BANKOPEN_INIT_OPEN_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean openAccountInit(HttpServletRequest request, OpenAccountBean form) {
    	
        LogUtil.startLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_INIT_OPEN_ACTION);
        OpenAccountResultVo result = new OpenAccountResultVo();
        //请求成功
        result.setEnum(ResultEnum.SUCCESS);
        
        // 获取登陆用户userId
        Integer userId = requestUtil.getRequestUserId(request);
        
        if (Validator.isNull(userId)) {
        	result.setEnum(ResultEnum.ERROR_021);
            return result;
        } else {
            String mobile = "";
            logger.info("开户===");
            Users users = loginService.getUsersByUserId(userId);
            UsersInfo usersInfo = loginService.getUsersInfoByUserId(userId);
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(3);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(1);
            userOperationLogEntity.setRemark("");
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(users.getUsername());
            userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
            loginService.sendUserLogMQ(userOperationLogEntity);
            try {
                mobile = this.openAccountService.getUsersMobile(userId);
            } catch (Exception e) {
                mobile = "";
            }
            if (StringUtils.isBlank(mobile)) {
                mobile = "";
            }
            //手机号只显示前3和后4位
            result.setMobile(mobile);
            //获取用户名返回给前端
            Users user  = openAccountService.getUsersByUserId(userId);
            //将用户名返回给前端
            result.setUserName(user.getUsername());

        }
        LogUtil.endLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_INIT_OPEN_ACTION);
        return result;
    }


    /**
     * 江西银行开户协议
     *
     * @param request
     * @param
     * @return
     */
    @SignValidate
    @RequestMapping(OpenAccountDefine.JX_BANK_SERVICE_ACTION)
    public ModelAndView jxBankService(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.JX_BANK_SERVICE_PATH);
        String userIdStr = String.valueOf(requestUtil.getRequestUserId(request));
        String userName = null;
        if (!StringUtils.isBlank(userIdStr)) {
            Integer userId = Integer.parseInt(userIdStr);
            Users users = openAccountService.getUsersByUserId(userId);
            if (users != null) {
                userName = users.getUsername();
            }
        }
        modelAndView.addObject("userName", userName);
        return modelAndView;
    }

    /**
     * 页面请求开户
     * @param request
     * @param response
     * @return
     */
	@SignValidate
	@RequestMapping(value = OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION)
	public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		String sign = requestUtil.getRequestSign(request);
		Integer userId = requestUtil.getRequestUserId(request);
		Users user = openAccountService.getUsers(userId);
		if (user.getBankOpenAccount().intValue() == 1) {// 已开户
			logger.info("用户已开户 ,userId:" + userId);
			return getErrorModelAndView(ResultEnum.USER_ERROR_214);
		}
		// 开户需要的参数
		String name = request.getParameter("name");
		String idNo = request.getParameter("idNo");
		String mobile = request.getParameter("phone");

		// 检查提交的参数是否正确
		Map<String, String> result = accountPageService.checkParm(userId, mobile, name);
		// 失败
		if ("0".equals(result.get("success"))) {
			logger.info("开户参数校验失败，userId:" + userId);
			JSONObject retInfo = new JSONObject();
			retInfo.put("status", "997");
			retInfo.put("statusDesc", result.get("message"));
			//返回具体的错误信息
			return getErrorModelAndViewDetail(retInfo);
		}
		try {
			// 同步调用路径
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.RETURL_SYN_ACTION + ".page?sign=" + sign;
			// 异步调用路
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.RETURL_ASY_ACTION + ".do?phone=" + mobile;
			// 拼装参数 调用江西银行
			OpenAccountPageBean openBean = new OpenAccountPageBean();
			openBean.setMobile(mobile);
			openBean.setIdNo(idNo);
			// 微官网 1
			openBean.setPlatform(CustomConstants.CLIENT_WECHAT);
			openBean.setChannel(BankCallConstant.CHANNEL_WEI);
			openBean.setTrueName(name);
			openBean.setUserId(userId);
			openBean.setIp(CustomUtil.getIpAddr(request));
			// 同步 异步
			openBean.setRetUrl(retUrl);
			openBean.setNotifyUrl(bgRetUrl);
			openBean.setCoinstName("汇盈金服");
			// 账户用途 写死
			//00000-普通账户 10000-红包账户（只能有一个） 01000-手续费账户（只能有一个） 00100-担保账户
			openBean.setAcctUse("00000");
			/**
			 * 1：出借角色 2：借款角色 3：代偿角色
			 */
			openBean.setIdentity("1");
			modelAndView = accountPageService.getCallbankMV(openBean);
			// 保存开户日志
			boolean isUpdateFlag = this.openAccountService.updateUserAccountLog(userId, user.getUsername(),
					openBean.getMobile(), openBean.getOrderId(), CustomConstants.CLIENT_WECHAT, openBean.getTrueName(), openBean.getIdNo(),
					openBean.getCardNo());
			if (!isUpdateFlag) {
				logger.info("保存开户日志失败,手机号:[" + openBean.getMobile() + "],用户ID:[" + userId + "]");
				return getErrorModelAndView(ResultEnum.ERROR_038);
			}
			logger.info("开户end");
			LogUtil.endLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
			return modelAndView;
		} catch (Exception e) {
			logger.error("调用银行接口失败", e);
			LogUtil.errorLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION, e);
			return getErrorModelAndView(ResultEnum.ERROR_022);
		}
	}

    private ModelAndView getErrorModelAndView(ResultEnum param) {
        ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, param.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, param.getStatusDesc());
        baseMapBean.setCallBackAction(CustomConstants.HOST + OpenAccountDefine.BANKOPEN_OPEN_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }

    /**
     * 开户失败返回详情 add by jijun 2018/04/08
     * @param param
     * @return
     */
    private ModelAndView getErrorModelAndViewDetail(JSONObject param) {
        ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, String.valueOf(param.get("status")));
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, String.valueOf(param.get("statusDesc")));
        baseMapBean.setCallBackAction(CustomConstants.HOST + OpenAccountDefine.BANKOPEN_OPEN_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }
    
    /**
     * 页面开户同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(OpenAccountDefine.RETURL_SYN_ACTION)
    public ModelAndView openAccountReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[开户同步回调开始]");
//        String sign = request.getParameter("sign");
//        String platform = request.getParameter("platform");
        ModelAndView modelAndView = new ModelAndView();
        String frontParams = request.getParameter("frontParams");
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
        logger.info("开户同步返回值,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
        
        String isSuccess = request.getParameter("isSuccess");
     // 成功了
        if("1".equals(isSuccess)){
        	// 成功
			CommonSoaUtils.listOpenAcc(Integer.valueOf(bean.getLogUserId()));
        	modelAndView = new ModelAndView(OpenAccountDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS2.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, ResultEnum.SUCCESS2.getStatusDesc());
            baseMapBean.setCallBackAction(CustomConstants.HOST + OpenAccountDefine.BANKOPEN_OPEN_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
        // 银行卡与姓名不符
        if ("CP9919".equals(retCode)) {
            logger.info("失败原因：银行卡与姓名不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            ResultEnum resultEnum = ResultEnum.ERROR_022;
            resultEnum.setStatusDesc("银行卡与姓名不符！");
            return getErrorModelAndView(resultEnum); 
        }
        // 银行卡与证件不符
        if ("CP9920".equals(retCode)) {
            logger.info("银行卡与证件不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            ResultEnum resultEnum = ResultEnum.ERROR_022;
            resultEnum.setStatusDesc("银行卡与证件不符！");
            return getErrorModelAndView(resultEnum); 
        }
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = openAccountService.getBankRetMsg(retCode);
            logger.info("失败原因：银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            ResultEnum resultEnum = ResultEnum.ERROR_022;
            resultEnum.setStatusDesc(retMsg);
            return getErrorModelAndView(resultEnum); 
        }

        Users user = loginService.getUsers(Integer.parseInt(bean.getLogUserId()));
        UsersInfo userInfo = loginService.getUsersInfoByUserId(Integer.parseInt(bean.getLogUserId()));
        logger.info("开户第一次设置密码==========================");
        // 用户id
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(6);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(0);
        userOperationLogEntity.setRemark("");
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(user.getUsername());
        userOperationLogEntity.setUserRole(String.valueOf(userInfo.getRoleId()));
        loginService.sendUserLogMQ(userOperationLogEntity);
        logger.info("开户第一次设置密码==========================结束");

        logger.info("开户成功,用户ID:[" + bean.getLogUserId() + "]");

        if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
                        || "JX900703".equals(retCode)) {
            // 成功
			CommonSoaUtils.listOpenAcc(Integer.valueOf(bean.getLogUserId()));
        	modelAndView = new ModelAndView(OpenAccountDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS2.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, ResultEnum.SUCCESS2.getStatusDesc());
            baseMapBean.setCallBackAction(CustomConstants.HOST + OpenAccountDefine.BANKOPEN_OPEN_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        } else {
            String retMsg = openAccountService.getBankRetMsg(retCode);
            modelAndView.addObject("statusDesc", "失败原因："+retMsg);
            
            logger.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            ResultEnum resultEnum = ResultEnum.ERROR_022;
            resultEnum.setStatusDesc(retMsg);
            return getErrorModelAndView(resultEnum); 
        }
    }

    /**
     * 页面开户异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(OpenAccountDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        BankCallResult result = new BankCallResult();
        String phone = request.getParameter("phone");
        logger.info("页面开户异步回调start");
        String openclient = request.getParameter("openclient");
        bean.setLogClient(Integer.parseInt(openclient));
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) || "0".equals(bean.getStatus())) {
            // 将开户记录状态改为4
            this.openAccountService.updateUserAccountLog(userId, bean.getLogOrderId(), 4);
            // 根据银行相应代码,查询错误信息
            String retMsg = openAccountService.getBankRetMsg(retCode);
            logger.info("失败原因:银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,保存用户的开户信息
        boolean saveBankAccountFlag = this.accountPageService.updateUserAccount(bean);
        if (!saveBankAccountFlag) {
            logger.info("开户失败,保存用户的开户信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,发送CA认证MQ
        this.openAccountService.sendCAMQ(String.valueOf(userId));
        CommonSoaUtils.listOpenAcc(userId);
        // 保存银行卡信息
        boolean saveBankCardFlag = this.accountPageService.updateCardNoToBank(bean);
        if (!saveBankCardFlag) {
            logger.info("开户失败,保存银行卡信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        
        logger.info("开户成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }
}

