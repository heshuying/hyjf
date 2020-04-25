package com.hyjf.app.bank.user.openaccount;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.UsersInfo;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.transpassword.TransPasswordController;
import com.hyjf.app.bank.user.transpassword.TransPasswordDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.cache.RedisUtils;
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


/**
 * Created by yaoyong on 2017/12/8.
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
    private AppUserService appUserService;

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
    @RequestMapping(value = OpenAccountDefine.BANKOPEN_OPEN_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject openAccountInit(HttpServletRequest request, @ModelAttribute() OpenAccountBean form) {
        LogUtil.startLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACTION);
        JSONObject ret = new JSONObject();
        ret.put("status", "000");
        ret.put("statusDesc", "请求成功");

        // 获取登陆用户userId
        String sign = request.getParameter("sign");
        // 平台
        //ios 传 安卓不传
        String platform = request.getParameter("realPlatform");
        if(StringUtils.isBlank(platform)){
            platform = request.getParameter("platform");
        }
        if (Validator.isNull(sign)) {
            ret.put("status", "99");
            ret.put("statusDesc", "用户未登录，请先登录！");
            return ret;
        }
        Integer userId = SecretUtil.getUserId(sign); // 用户ID
        ret.put("userId", userId);

        if (Validator.isNull(userId)) {
            ret.put("status", "99");
            ret.put("statusDesc", "用户未登录，请先登录！");
            return ret;
        } else {
            logger.info("用户平台platform==="+platform);
            Users users = appUserService.getUsersByUserId(userId);
            UsersInfo usersInfo = appUserService.getUsersInfoByUserId(userId);
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(3);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(Integer.valueOf(platform));
            userOperationLogEntity.setRemark("");
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(users.getUsername());
            userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
            appUserService.sendUserLogMQ(userOperationLogEntity);
            String mobile = "";
            try {
                mobile = this.openAccountService.getUsersMobile(userId);
            } catch (Exception e) {
                mobile = "";
            }
            if (StringUtils.isBlank(mobile)) {
                mobile = "";
            }
            //手机号只显示前3和后4位
            ret.put(OpenAccountDefine.MOBILE, mobile);
            // 合规审批 用以区分企业用户或者个人用户已达到企业用户跳转开户指南画面 add by huanghui 20181128 start
            ret.put("userType", users.getUserType());
            // 合规审批 用以区分企业用户或者个人用户已达到企业用户跳转开户指南画面 add by huanghui 20181128 end
        }
        LogUtil.endLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACTION);
        return ret;
    }


    /**
     * 江西银行开户协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(OpenAccountDefine.JX_BANK_SERVICE_ACTION)
    public ModelAndView jxBankService(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.JX_BANK_SERVICE_PATH);
        String userIdStr = request.getParameter("userId");
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
     * 
     * 页面开户请求地址
     * @author sunss
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION)
    public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(TransPasswordDefine.THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        String sign = request.getParameter("sign");
        // 开户需要的参数
        String name = request.getParameter("name");
        String idNo = request.getParameter("idNo");
        String mobile = request.getParameter("phone");
        String platform = request.getParameter("platform");

        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
            return modelAndView; 
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if(userId==null||userId<=0){
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
            return modelAndView; 
        }
        
        // 检查提交的参数是否正确
        Map<String, String> result = accountPageService.checkParm(userId,mobile,name);
        // 失败
        if("0".equals(result.get("success"))){
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, result.get("message"));
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
            return modelAndView; 
        }
        
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  OpenAccountDefine.REQUEST_MAPPING
                    + OpenAccountDefine.RETURL_SYN_ACTION + ".do?sign="+sign+"&platform="+platform+"&userId="+userId;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  OpenAccountDefine.REQUEST_MAPPING
                    + OpenAccountDefine.RETURL_ASY_ACTION + ".do?phone="+mobile;
            // 拼装参数 调用江西银行
            OpenAccountPageBean openBean = new OpenAccountPageBean();
            openBean.setMobile(mobile);
            openBean.setIdNo(idNo);
            openBean.setPlatform(platform);
            openBean.setTrueName(name);
            openBean.setUserId(userId);
            openBean.setIp(CustomUtil.getIpAddr(request));
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            openBean.setCoinstName("汇盈金服");
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
            openBean.setPlatform(platform);
            modelAndView = accountPageService.getCallbankMV(openBean);
            Users user = this.openAccountService.getUsers(userId);
            //保存开户日志
            boolean isUpdateFlag = this.openAccountService.updateUserAccountLog(userId, user.getUsername(), openBean.getMobile(), openBean.getOrderId(),platform ,openBean.getTrueName(),openBean.getIdNo(),openBean.getCardNo());
            if (!isUpdateFlag) {
                logger.info("保存开户日志失败,手机号:[" + openBean.getMobile() + "],用户ID:[" + userId + "]");
                modelAndView.addObject("message", "操作失败！");
                return modelAndView;
            }
            logger.info("开户end");
            LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION);
            return modelAndView;
        } catch (Exception e) {
            logger.error("调用银行接口失败",e);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.errorLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION, e);
        }
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
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        String frontParams = request.getParameter("frontParams");
        String userId = request.getParameter("userId");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        
     
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BaseMapBean baseMapBean=new BaseMapBean();
        bean.convert();
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        logger.info("开户同步返回值,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
        String isSuccess = request.getParameter("isSuccess");
        // 成功了
        if("1".equals(isSuccess)){
        	 Users user= openAccountService.getUsers(new Integer(userId));
        	 logger.info("用户状态,用户ID:[" + userId + "],bankOpenAccount:[" + user.getBankOpenAccount() + "]");
        	 // 成功
        	 baseMapBean.set(OpenAccountDefine.SET_PAWWWORD_URL, OpenAccountDefine.BANK_PASSWORD_URL + ".do?sign=" + sign + "&platform=" + platform);
             baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
             baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");
             baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_SUCCESS_PATH);
             modelAndView.addObject("callBackForm", baseMapBean);
             LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
             return modelAndView;
            /*modelAndView = new ModelAndView("redirect:"+OpenAccountDefine.BANK_PASSWORD_URL + ".do?sign=" + sign + "&platform=" + platform);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
			return modelAndView;*/
        }
        // 银行卡与姓名不符
        if ("CP9919".equals(retCode)) {
            logger.info("失败原因：银行卡与姓名不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因:银行卡与姓名不符！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION);
            return modelAndView; 
        }
        // 银行卡与证件不符
        if ("CP9920".equals(retCode)) {
            logger.info("银行卡与证件不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因：银行卡与证件不符！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION);
            return modelAndView; 
        }
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = openAccountService.getBankRetMsg(retCode);
            logger.info("失败原因：银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因："+retMsg);
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION);
            return modelAndView; 
        }
        logger.info("开户成功,用户ID:[" + bean.getLogUserId() + "]");

        if (bean != null 
                && ((BankCallConstant.RESPCODE_SUCCESS.equals(retCode))
                        || "JX900703".equals(retCode))) {
            // 成功
        	baseMapBean.set(OpenAccountDefine.SET_PAWWWORD_URL, OpenAccountDefine.BANK_PASSWORD_URL + ".do?sign=" + sign + "&platform=" + platform);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        	/*Users user= openAccountService.getUsers(new Integer(userId));
       	 	logger.info("用户状态,用户ID:[" + userId + "],bankOpenAccount:[" + user.getBankOpenAccount() + "]");
            modelAndView = new ModelAndView("redirect:"+OpenAccountDefine.BANK_PASSWORD_URL + ".do?sign=" + sign + "&platform=" + platform);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
			return modelAndView;*/
        } else {
            String retMsg = openAccountService.getBankRetMsg(retCode);
            modelAndView.addObject("statusDesc", "失败原因："+retMsg);
            
            logger.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因："+retMsg);
            baseMapBean.setCallBackAction(CustomConstants.HOST+OpenAccountDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION);
            return modelAndView; 
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
        String openclient = request.getParameter("openclient");
        bean.setLogClient(Integer.parseInt(openclient));
        logger.info("页面开户异步回调start");
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
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

