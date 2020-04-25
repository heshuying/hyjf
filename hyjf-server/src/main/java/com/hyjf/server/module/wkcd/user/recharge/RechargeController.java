/**
 * Description:获取指定的项目类型的项目列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.server.module.wkcd.user.recharge;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.cache.SerializeUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.http.HttpRequest;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.DesECBUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ServerApplication;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.server.BaseController;

/**
 * 
 * App充值控制器
 * @author 朱晓东
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月18日
 * @see 下午2:14:02
 */
@Controller
@RequestMapping(value = RechargeDefine.REQUEST_MAPPING)
public class RechargeController extends BaseController {

	Logger _log = LoggerFactory.getLogger(RechargeController.class);
    @Autowired
    private RechargeService rechargeService;

    private static DecimalFormat df = new DecimalFormat("#######0.00");

    /** 发布地址 */
    private static String HOST = PropUtils.getSystem("hyjf.server.host");
    private static String HOST_HTTP = PropUtils.getSystem("http.hyjf.server.host").trim();

    /** 充值接口 */
    public static final String RECHARGE_URL = HOST + "/hyjf-server/user/recharge/rechargeAction?";

    /**
     * 
     * 用户充值
     * @author 朱晓东
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RechargeDefine.USER_RECHARGE_ACTION)
    public ModelAndView userRecharge(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(RechargeDefine.JSP_CHINAPNR_SEND);
        Object secretKey = request.getAttribute("secretKey");
        Object requestObject = request.getAttribute("requestObject");
        String appId = request.getParameter("appId");
        if(secretKey!=null){
            if(requestObject!=null){
                Map<String, String> requestMap = parseRequestJson(requestObject.toString());
                if(requestMap!=null){
                    String userId = requestMap.get("userId");//客户ID
                    String transAmt = requestMap.get("money");//充值金额
                    String cardNo = requestMap.get("cardNo");//卡号
                    String openBankCode = requestMap.get("code");//银行编号
                    String platform = requestMap.get("platform");// 终端类型
                    String rechargeType = requestMap.get("rechargeType");// 充值类型
                    _log.info("用户充值接口调用userId="+userId);
                    String error = "0";
                    String message = "";
                    JSONObject checkResult;
                    if (StringUtils.isEmpty(cardNo)) {
                        checkResult = checkParam(request, Integer.parseInt(userId), transAmt);
                        openBankCode = "";
                    } else {
                        // 检查参数
                        checkResult = checkParam(request, Integer.parseInt(userId), transAmt, openBankCode);
                    }
                    if (checkResult != null) {
                        error = "1";
                        message = String.valueOf(checkResult.get("message"));
                        modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                        modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                        modelAndView.addObject("status", 1);
                        modelAndView.addObject("statusDesc", message);
                        return modelAndView;
                    }
                    //获取用户信息
                    Users user = rechargeService.getUsers(Integer.parseInt(userId));
                    if(user!=null){
                        ChinapnrBean bean = new ChinapnrBean();
                        UsersInfo usersInfo = rechargeService.getUsersInfoByUserId(Integer.parseInt(userId));
                        int roleId = usersInfo.getRoleId();
                        String feeFrom;
                        JSONObject jobj = new JSONObject();
                        if (2 == roleId) {// 如果是借款用户需要判断用户是来自内部机构还是外部机构
                            if (null != usersInfo.getBorrowerType() && usersInfo.getBorrowerType() > 1) {// 如果是外部机构
                                feeFrom = "U";
                                jobj.put("FeeObjFlag", feeFrom);
                            } else {// 如果是内部机构 需要传递出账子账户
                                feeFrom = "M";
                                jobj.put("FeeObjFlag", feeFrom);
                                jobj.put("FeeAcctId", PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT07));
                            }
                        } else {// 如果是出借用户需则手续费从商户收取 需要传递出账子账户
                            feeFrom = "M";
                            jobj.put("FeeObjFlag", feeFrom);
                            jobj.put("FeeAcctId", PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT07));
                        }
                        String gateBusiId = "";
                        if(rechargeType!=null){
                             switch (rechargeType) {
                                case RechargeDefine.RECHARGETYPE_0:
                                    gateBusiId = "B2C";
                                    break;
                                case RechargeDefine.RECHARGETYPE_1:
                                    gateBusiId = "B2B";
                                    break;
                                case RechargeDefine.RECHARGETYPE_2:
                                    gateBusiId = "QP";
                                    // 查询用户快捷卡信息
                                    AccountBank accountBank = rechargeService.getBankInfo(Integer.parseInt(userId), null);
                                    if (accountBank == null || StringUtils.isEmpty(accountBank.getBank())) {
                                    } else {
                                        openBankCode = accountBank.getBank();
                                        bean.setOpenAcctId(accountBank.getAccount());// 设置快捷支付银行卡号
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        bean.setGateBusiId(gateBusiId);
                        // 获取用户在汇付天下的客户号
                        AccountChinapnr accountChinapnrTender = this.rechargeService.getAccountChinapnr(Integer.parseInt(userId));
                        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
                            modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                            modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                            modelAndView.addObject("status", 1);
                            modelAndView.addObject("statusDesc", "[用户未开户]");
                            return modelAndView;
                        }
                        Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();// 用户在汇付天下的客户号
                        String certId = this.rechargeService.getUserIdcard(Integer.parseInt(userId)); // 身份证号
                        // 调用出借接口
                        // 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
                        String retUrl =
                                HOST + RechargeDefine.REQUEST_MAPPING + RechargeDefine.RETURN_MAPPING+"?appId="+appId+"&secretKey="+String.valueOf(secretKey);
                        String bgRetUrl =
                                HOST_HTTP + RechargeDefine.REQUEST_MAPPING + RechargeDefine.CALLBACK_MAPPING+"?appId="+appId+"&secretKey="+String.valueOf(secretKey);// 支付工程路径
                        bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
                        bean.setCmdId(ChinaPnrConstant.CMDID_NET_SAVE); // 消息类型(充值)
                        bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
                        bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
                        bean.setOrdDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
                        bean.setGateBusiId(gateBusiId);// 支付网关业务代号(充值类型)
                        bean.setOpenBankId(openBankCode);// 开户银行代号
                        bean.setTransAmt(CustomUtil.formatAmount(transAmt));// 交易金额(必须)
                        bean.setRetUrl(retUrl); // 页面返回 URL
                        bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
                        bean.setCertId(certId); // 身份证号
                        bean.setDcFlag("D");
                        bean.setReqExt(jobj.toJSONString());
                        MerPriv merPriv = new MerPriv();
                        merPriv.setUserId(Integer.parseInt(userId));
                        merPriv.setFeeFrom(feeFrom);
                        bean.setMerPrivPo(merPriv);// 商户私有域
                        bean.setType("user_recharge"); // 日志类型(写日志用)

                        // 插值用参数
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userId", String.valueOf(userId));
                        params.put("ip", GetCilentIP.getIpAddr(request));
                        params.put("username", user.getUsername());
                        params.put("feeFrom", feeFrom);

                        // 插入充值记录
                        int ret = this.rechargeService.insertRechargeInfo(bean, params);
                        if (ret > 0) {
                            // 跳转到汇付天下画面
                            try {
                            	_log.info("调用汇付充值接口userId="+userId);
                                modelAndView = ChinapnrUtil.callApi(bean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                        return modelAndView;
                    }else{
                        modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                        modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                        modelAndView.addObject("status", 1);
                        modelAndView.addObject("statusDesc", "查询不到该用户信息");
                        return modelAndView;
                    }
                }else{
                    modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                    modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                    modelAndView.addObject("status", 1);
                    modelAndView.addObject("statusDesc", "业务参数数据JSON格式错误");
                    return modelAndView;
                }
            }else{
                modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                modelAndView.addObject("status", 1);
                modelAndView.addObject("statusDesc", "无法获取到业务参数信息数据");
                return modelAndView;
            }
        }else{
            modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
            modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
            modelAndView.addObject("status", 1);
            modelAndView.addObject("statusDesc", "无法获取到可变临时密钥");
            return modelAndView;
        }
    }

    /**
     * 用户充值后处理 (同步)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RechargeDefine.RETURN_MAPPING)
    public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
    	_log.info("充值同步回调---开始 userId="+bean.getMerPrivPo().getUserId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
        String appId = request.getParameter("appId");
        String secretKey = request.getParameter("secretKey");
        byte[] applicationByte = RedisUtils.get(("Third-Party-Application:"+appId).getBytes());
        ServerApplication serverApplication = (ServerApplication)SerializeUtils.unserialize(applicationByte);
        //得到appKey
        String appKey = serverApplication.getAppkey();
        _log.info("充值同步回调userId="+bean.getMerPrivPo().getUserId() + "验签结果:" +bean.getChkValueStatus());
        if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(bean.getChkValueStatus())) {
            bean.convert();
            LogUtil.debugLog(RechargeController.class.toString(), RechargeDefine.RETURN_MAPPING,
                    "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
            String ip = CustomUtil.getIpAddr(request);
            _log.info("充值同步回调userId="+bean.getMerPrivPo().getUserId() + "respcode:" +bean.getRespCode());
            //汇付处理成功
            if(bean.getRespCode()!=null && ("0".equals(bean.getRespCode()) || "000".equals(bean.getRespCode()))){
                // 更新充值的相关信息
                modelAndView =
                        rechargeService.handleRechargeInfo(ip, bean.getMerPrivPo().getFeeFrom() + "", bean.getMerPrivPo()
                                .getUserId(), bean, modelAndView, appKey, secretKey);
                modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
            }else{
                //处理失败
                modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
                modelAndView.addObject("status", 1);
                modelAndView.addObject("statusDesc", "汇付处理失败:"+bean.getRespDesc());
                _log.info(bean.getMerPrivPo().getUserId()+"汇付处理失败:"+bean.getRespDesc());
            }
        } else {
            String message = "响应结果验签失败！";
            modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("statusDesc", message);
        }
        return modelAndView;
    }
    
    /**
     * 用户充值后处理 (异步)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @RequestMapping(RechargeDefine.CALLBACK_MAPPING)
    public ModelAndView rechargeCallback(HttpServletRequest request, @ModelAttribute ChinapnrBean bean, Integer userId,
        String feeFrom) throws Exception {
    	_log.info("充值异步回调---开始userId="+bean.getMerPrivPo().getUserId());
        Map<String, Object> params = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView();
        String appId = request.getParameter("appId");
        String secretKey = request.getParameter("secretKey");
        byte[] applicationByte = RedisUtils.get(("Third-Party-Application:"+appId).getBytes());
        ServerApplication serverApplication = (ServerApplication)SerializeUtils.unserialize(applicationByte);
        //得到appKey
        String appKey = serverApplication.getAppkey();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        _log.info("充值异步回调userId="+bean.getMerPrivPo().getUserId() + "验签结果:" +bean.getChkValueStatus());
        if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(bean.getChkValueStatus())) {
            bean.convert();
            String ip = CustomUtil.getIpAddr(request);
            _log.info("充值异步回调userId="+bean.getMerPrivPo().getUserId() + "respcode:" +bean.getRespCode());
            //汇付处理成功
            if(bean.getRespCode()!=null && ("0".equals(bean.getRespCode()) || "000".equals(bean.getRespCode()))){
                // 更新充值的相关信息
                modelAndView =
                        rechargeService.handleRechargeInfo(ip, bean.getMerPrivPo().getFeeFrom() + "", bean.getMerPrivPo()
                                .getUserId(), bean, modelAndView, appKey, secretKey);
            }else{
                modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
                modelAndView.addObject("status", 1);
                modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                modelAndView.addObject("statusDesc", "汇付处理失败:"+bean.getRespDesc());
                _log.info(bean.getMerPrivPo().getUserId()+"汇付处理失败:"+bean.getRespDesc());
            }
        } else {
            String message = "响应结果验签失败！";
            modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
            modelAndView.addObject("statusDesc", message);
        }
        params.put("status", modelAndView.getModel().get("status")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("status")), "UTF-8"):"");
        params.put("secretKey", modelAndView.getModel().get("secretKey")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("secretKey")), "UTF-8"):"");
        params.put("statusDesc", modelAndView.getModel().get("statusDesc")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("statusDesc")), "UTF-8"):"");
        params.put("responseObject", modelAndView.getModel().get("responseObject")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("responseObject")), "UTF-8"):"");
        String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.async.callback"), params);
        LogUtil.infoLog(RechargeController.class.toString(), result);
        return modelAndView;
    }

    /**
     * 
     * 数据校验
     * @author renxingchen
     * @param request
     * @param userId
     * @param transAmtStr
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr) {
        if(userId==null){
            return jsonMessage("缺少用户信息", "1");
        }
        // 判断用户是否被禁用
        Users users = this.rechargeService.getUsers(userId);
        if (users == null || users.getStatus() == 1) {
            return jsonMessage("对不起,该用户已经被禁用。", "1");
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
            return jsonMessage("请输入充值金额。", "1");
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal transAmt = new BigDecimal(transAmtStr);
        if (transAmt.compareTo(BigDecimal.ZERO) < 0) {
            return jsonMessage("充值金额不能为负数。", "1");
        }
        if (transAmt.compareTo(new BigDecimal(99999999.99)) > 0) {
            return jsonMessage("充值金额不能大于99,999,999.99元。", "1");
        }
        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnrTender = this.rechargeService.getAccountChinapnr(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
            return jsonMessage("用户未开户，请开户后再充值。", "1");
        }
        return null;
    }

    /**
     * 
     * 数据校验
     * @author renxingchen
     * @param request
     * @param userId
     * @param transAmt
     * @param openBankId
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String openBankId) {
        // 判断用户是否被禁用
        Users users = this.rechargeService.getUsers(userId);
        if (users == null || users.getStatus() == 1) {
            return jsonMessage("对不起,该用户已经被禁用。", "1");
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
            return jsonMessage("请输入充值金额。", "1");
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal transAmt = new BigDecimal(transAmtStr);
        if (transAmt.compareTo(BigDecimal.ZERO) < 0) {
            return jsonMessage("充值金额不能为负数。", "1");
        }
        if (transAmt.compareTo(new BigDecimal(99999999.99)) > 0) {
            return jsonMessage("充值金额不能大于99,999,999.99元。", "1");
        }
        if (StringUtils.isEmpty(openBankId)) {
            return jsonMessage("银行编号不能为空", "1");
        }
        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnrTender = this.rechargeService.getAccountChinapnr(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
            return jsonMessage("用户未开户，请开户后再充值。", "1");
        }
        return null;
    }
}
