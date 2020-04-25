/**
 * Description:开户控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.bank.wechat.user.bankopen;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
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
import com.hyjf.web.BaseController;


/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(BankOpenDefine.CONTROLLER_NAME)
@RequestMapping(value = BankOpenDefine.REQUEST_MAPPING)
public class BankOpenController extends BaseController {

	@Autowired
	private BankOpenService openAccountService;

	@Autowired
    private UserOpenAccountPageService accountPageService;
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = BankOpenController.class.getName();
	
	Logger _log = LoggerFactory.getLogger(this.getClass());

	
    @RequestMapping(value =BankOpenDefine.BANKOPEN_OPEN_ACTION)
    public ModelAndView openAccountPage(HttpServletRequest request, HttpServletResponse response,
            BankOpenBean accountBean) throws Exception {
        _log.info("微信请求页面开户，参数为："+JSONObject.toJSONString(accountBean));
        ModelAndView modelAndView = new ModelAndView();
        // 获取登陆用户userId
        String userId = request.getParameter("userId");
        //返回结果url，微信端提供
        String callback = request.getParameter("callback");
        // 开户需要的参数
        String name = URLDecoder.decode(request.getParameter("name"),"utf-8");
        String idNo = request.getParameter("idNo");
        String mobile = request.getParameter("mobile");
        try {
            // 验证请求参数
            if (Validator.isNull(userId)) {
                modelAndView = new ModelAndView(returnUrl("1", "用户未登录", callback));
                return modelAndView;
            }
            Users user = this.openAccountService.getUsers(Integer.parseInt(userId));
            if (Validator.isNull(user)) {
                modelAndView = new ModelAndView(returnUrl("1", "获取用户信息失败", callback));
                return modelAndView;
            }
            
            Map<String, String> result = accountPageService.checkParm(Integer.parseInt(userId),mobile,name);
            // 失败
            if("0".equals(result.get("success"))){
                modelAndView = new ModelAndView(returnUrl("1", result.get("message"), callback));
                return modelAndView;
            }
            
            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankOpenDefine.REQUEST_MAPPING
                    + BankOpenDefine.RETURL_SYN_ACTION + ".do?callback="+callback;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + BankOpenDefine.REQUEST_MAPPING
                    + BankOpenDefine.RETURL_ASY_ACTION + ".do?phone="+mobile+"&callback="+callback;
            
            OpenAccountPageBean openBean = new OpenAccountPageBean();
            PropertyUtils.copyProperties(openBean, accountBean);
            openBean.setUserId(Integer.parseInt(userId));
            openBean.setIp(CustomUtil.getIpAddr(request));
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            openBean.setIdNo(idNo);
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
            openBean.setTrueName(name);
            openBean.setIdentity("1");
            openBean.setPlatform("1");
            modelAndView = accountPageService.getCallbankMV(openBean);
            //保存开户日志
            boolean isUpdateFlag = this.openAccountService.updateUserAccountLog(Integer.parseInt(userId), user.getUsername(), openBean.getMobile(), openBean.getOrderId(),CustomConstants.CLIENT_WECHAT ,openBean.getTrueName(),openBean.getIdNo(),openBean.getCardNo());
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
            modelAndView = new ModelAndView(returnUrl("1", "开户异常", callback));
            return modelAndView;
        }
    }
    
    public static void main(String[] args) {
        
        try {
            String n=URLEncoder.encode("%E6%AC%A7%E7%91%B6%E9%9C%96","utf-8");
            System.out.println(n);
            System.out.println(URLDecoder.decode(n,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        ModelAndView modelAndView = new ModelAndView();
        String frontParams = request.getParameter("frontParams");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        bean.convert();
        String callback = request.getParameter("callback");
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 银行卡与姓名不符
        if ("CP9919".equals(retCode)) {
            _log.info("开户失败,银行卡与姓名不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            modelAndView = new ModelAndView(returnUrl("1", "银行卡与姓名不符", callback));
            LogUtil.endLog(THIS_CLASS, BankOpenDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
            
        }
        // 银行卡与证件不符
        if ("CP9920".equals(retCode)) {
            _log.info("开户失败,银行卡与证件不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            modelAndView = new ModelAndView(returnUrl("1", "银行卡与证件不符", callback));
            LogUtil.endLog(THIS_CLASS, BankOpenDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = openAccountService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            modelAndView = new ModelAndView(returnUrl("1", retMsg, callback));
            LogUtil.endLog(THIS_CLASS, BankOpenDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
        _log.info("开户成功,用户ID:[" + bean.getLogUserId() + "]");

        if (bean != null 
                && ((BankCallConstant.RESPCODE_SUCCESS.equals(retCode))
                        || "JX900703".equals(retCode))) {
            // 成功
            modelAndView = new ModelAndView(returnUrl("0", "开户成功", callback));
            return modelAndView;
        } else {
            String retMsg = openAccountService.getBankRetMsg(retCode);
            modelAndView = new ModelAndView(returnUrl("1", retMsg, callback));
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
        
        _log.info("开户成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }
	

    /**
     * 拼接返回结果
     * 
     * @param error
     * @param errorDesc
     * @param callback
     * @return
     */
    public String returnUrl(String error, String errorDesc, String callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("error", error);
        map.put("errorDesc", errorDesc);
        String data = JSON.toJSONString(map);
        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = callback + "backinfo/" + data;
        return "redirect:" + url;
    }

}
