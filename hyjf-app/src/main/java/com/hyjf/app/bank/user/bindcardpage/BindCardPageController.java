package com.hyjf.app.bank.user.bindcardpage;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
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
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.util.SecretUtil;
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
    private AppUserService appUserService;
	
	/**
     * 页面请求绑卡
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = BindCardPageDefine.REQUEST_BINDCARDPAGE)
	public ModelAndView bindCardPage(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, BindCardPageDefine.REQUEST_BINDCARDPAGE);
		ModelAndView modelAndView = new ModelAndView();
        String platform = request.getParameter("platform");
        String token = request.getParameter("token");
        // 检查参数
        JSONObject checkResult = checkParam(request);
        
        if (checkResult != null) {
        	logger.info("checkResult is:{}", checkResult.toJSONString());
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "参数异常");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 唯一标识
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign);
        if (userId == null || userId == 0) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        logger.info("realPlatform===="+request.getParameter("realPlatform"));
        logger.info("platform===="+request.getParameter("platform"));
        Users users = userBindCardService.getUsers(userId);
        UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(10);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(Integer.valueOf(platform));
        userOperationLogEntity.setRemark("");
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(users.getUsername());
        userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
        appUserService.sendUserLogMQ(userOperationLogEntity);
        // 取得用户开户信息
        BankOpenAccount bankOpenAccount = userBindCardService.getBankOpenAccount(userId);
        if (bankOpenAccount == null || Validator.isNull(bankOpenAccount.getAccount())) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }

		if(users.getIsSetPassword()==0){
			//返回具体的错误信息 用户未设置交易密码
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未设置交易密码");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
		}
		
		
		List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(userId + "");
		if (accountBankList != null && accountBankList.size() > 0) {
			//返回具体的错误信息 用户已绑卡
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "请先解绑银行卡");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
		}
		UsersInfo userInfo=userBindCardService.getUsersInfoByUserIdTrue(userId);
		
		try {
			// 同步调用路径
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ BindCardPageDefine.REQUEST_MAPPING + BindCardPageDefine.RETURL_SYN_ACTION + ".do?sign=" + sign+"&account="+bankOpenAccount.getAccount();
			// 异步调用路
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ BindCardPageDefine.REQUEST_MAPPING + BindCardPageDefine.RETURL_ASY_ACTION + ".do?sign=" + sign+"&phone="+users.getMobile();
			// 拼装参数 调用江西银行
			String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token+"&platform="+platform;
			BindCardPageBean bean = new BindCardPageBean();
			bean.setTxCode(BankCallConstant.TXCODE_BIND_CARD_PAGE);
			bean.setChannel(BankCallConstant.CHANNEL_APP);
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
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败");
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
		}
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
        if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)||"1".equals(isSuccess)) {
            // 成功
        	modelAndView = new ModelAndView(BindCardPageDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");//绑卡成功
            baseMapBean.setCallBackAction(CustomConstants.HOST + BindCardPageDefine.BINDCARD_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, BindCardPageDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        } else {
        	modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");//绑卡失败
            baseMapBean.setCallBackAction(CustomConstants.HOST+ BindCardPageDefine.BINDCARD_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
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
