package com.hyjf.server.module.wkcd.user.withdraw;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.cache.SerializeUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.http.HttpRequest;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.DesECBUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ServerApplication;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.server.BaseController;
import com.hyjf.server.module.wkcd.user.recharge.RechargeController;

@Controller
@RequestMapping(value=UserWithdrawDefine.REQUEST_MAPPING)
public class UserWithdrawController  extends BaseController{
	/** 类名 */
	public static final String THIS_CLASS = UserWithdrawController.class.getName();
	
	private static String HOST = PropUtils.getSystem("hyjf.server.host");
	private static String HOST_HTTP = PropUtils.getSystem("http.hyjf.server.host").trim();

    private static DecimalFormat df = new DecimalFormat("#.00");
	
    @Autowired
    private UserWithdrawService userWithdrawService;

	/**
	 * 用户提现
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = UserWithdrawDefine.CASH_ACTION, method = RequestMethod.POST) 
    public ModelAndView cashAction(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.CASH_ACTION);
    	ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.JSP_CHINAPNR_SEND);//返回本地的视图
    	//JSONObject ret = new JSONObject();
    	//ret.put("request", UserWithdrawDefine.RETURN_REQUEST);
    	Object secretKey = request.getAttribute("secretKey");
    	Object requestObject = request.getAttribute("requestObject");
    	String appId = request.getParameter("appId");
    	if(secretKey!=null){
    	    if(requestObject!=null){
    	        Map<String, String> requestMap = parseRequestJson(requestObject.toString());
    	        if(requestMap!=null){
    	            String userId = requestMap.get("userId");//客户ID
    	            String cardNo = requestMap.get("cardNo");//提现银行卡号
    	            String total = requestMap.get("total");//提现金额
    	            String mode = requestMap.get("mode");//提现方式
    	            //检查参数
    	            JSONObject checkResult = checkParam(Integer.parseInt(userId), total, cardNo);
    	            if (checkResult != null) {
    	                //ret.put("status", "1");
                        //ret.put("statusDesc", checkResult.getString("message"));
    	                modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
    	                modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
    	                modelAndView.addObject("status", 1);
                        modelAndView.addObject("statusDesc", checkResult.getString("message"));
                        LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.REQUEST_MAPPING, checkResult.getString("message"));
                        return modelAndView;
    	            }
    	            //获取用户信息
    	            Users user = userWithdrawService.getUsers(Integer.parseInt(userId));
    	            
    	            //取得用户在汇付天下的客户号
    	            AccountChinapnr accountChinapnrTender = userWithdrawService.getAccountChinapnr(Integer.parseInt(userId));
    	            if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
    	                //ret.put("status", "1");
    	                //ret.put("statusDesc", "用户未开户");
    	                modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
    	                modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
    	                modelAndView.addObject("status", 1);
                        modelAndView.addObject("statusDesc", "用户未开户");
    	                LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.REQUEST_MAPPING, "[用户未开户]");
    	                return modelAndView;
    	            }
    	            Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();
    	            
    	            //取现渠道(FAST:快速取现 , GENERAL:一般取现 , IMMEDIATE:即时取现)
    	            if (Validator.isNull(mode)) {
    	                mode = "GENERAL";//默认是一般提现
    	            }else if("1".equals(mode)){
    	                mode = "GENERAL";//是一般提现
    	            }else if("2".equals(mode)){
                        mode = "FAST";//是快速取现
                    }else if("3".equals(mode)){
                        mode = "IMMEDIATE";//是即时取现
                    }
    	            //校验 银行卡号
    	            AccountBank accountBank = this.userWithdrawService.getBankInfo(Integer.parseInt(userId), cardNo);
    	            if (accountBank == null || Validator.isNull(accountBank.getAccount())) {
    	                //ret.put("status", "1");
                        //ret.put("statusDesc", "该银行卡信息不存在，请核实后重新操作");
                        modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
                        modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                        modelAndView.addObject("status", 1);
                        modelAndView.addObject("statusDesc", "该银行卡信息不存在，请核实后重新操作");
                        LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.REQUEST_MAPPING, "该银行卡信息不存在，请核实后重新操作");
                        return modelAndView;
    	            }
    	            
    	            //取得手续费
    	            String fee = this.userWithdrawService.getWithdrawFee(Integer.parseInt(userId), cardNo, new BigDecimal(total), mode);
    	            
    	            // 实际取现金额(洪刚提示跟线上保持一致)
    	            // 不去掉一块钱手续费
    	            // transAmt = new BigDecimal(transAmt).subtract(new
    	            // BigDecimal(Validator.isNull(fee) ? "0" : fee)).toString();
    	            if (new BigDecimal(total).compareTo(BigDecimal.ZERO) == 0) {
    	                modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
    	                modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
    	                modelAndView.addObject("status", 1);
    	                modelAndView.addObject("statusDesc", "提现金额不能小于一块");
    	                return modelAndView;
    	            }
    	            
    	            // 入参扩展域
    	            JSONArray reqExt = new JSONArray();
    	            JSONObject reqExtObj = new JSONObject();
    	            reqExtObj.put("FeeObjFlag", "U"); // 向用户收取
    	            reqExtObj.put("FeeAcctId", ""); // 忽略
    	            reqExtObj.put("CashChl", mode); // 取现渠道
    	            reqExt.add(reqExtObj);

    	            //调用汇付接口(提现)
                    String retUrl = HOST + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.RETURN_MAPPING+"?appId="+appId+"&secretKey="+String.valueOf(secretKey);// 支付工程路径
                    String bgRetUrl = HOST_HTTP + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.CALLBACK_MAPPING+"?appId="+appId+"&secretKey="+String.valueOf(secretKey);// 支付工程路径
                    ChinapnrBean bean = new ChinapnrBean();
    	            bean.setVersion(ChinaPnrConstant.VERSION_20);// 接口版本号
    	            bean.setCmdId(ChinaPnrConstant.CMDID_CASH); // 消息类型(提现)
    	            bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
    	            bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
    	            bean.setTransAmt(CustomUtil.formatAmount(total));// 交易金额(必须)
    	            bean.setOpenAcctId((accountBank != null && StringUtils.isNotBlank(accountBank.getAccount())) ? accountBank.getAccount() : ""); // 开户银行账号
    	            bean.setRetUrl(retUrl); // 页面返回 URL
    	            bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
    	            //bean.setMerPriv(CustomUtil.formatAmount(fee)); // 商户私有域
    	            MerPriv merPriv = new MerPriv();
                    merPriv.setUserId(Integer.parseInt(userId));
                    merPriv.setFeeFrom(fee);
                    bean.setMerPrivPo(merPriv);// 商户私有域
    	            bean.setReqExt(reqExt.toString());//去掉提现方式的选择，那么客户可以在汇付页面选择提现方式，并且计算手续费。
    	            bean.setPageType("1");// app 应用风格页面（无标题）

    	            // 插值用参数
    	            Map<String, String> params = new HashMap<String, String>();
    	            params.put("userId", String.valueOf(userId));
    	            params.put("userName", user.getUsername());
    	            params.put("ip", CustomUtil.getIpAddr(request));
    	            params.put("bankId", cardNo);
    	            params.put("fee", CustomUtil.formatAmount(fee));
    	            // 用户提现前处理
    	            int cnt = this.userWithdrawService.updateBeforeCash(bean, params);

    	            if (cnt > 0) {
    	                // 跳转到汇付天下画面
    	                try {
    	                    modelAndView = ChinapnrUtil.callApi(bean);
    	                } catch (Exception e) {
    	                    e.printStackTrace();
    	                }
    	            } else {
    	                modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
                        modelAndView.addObject("status", 1);
                        modelAndView.addObject("statusDesc", "请不要重复操作");
    	                return modelAndView;
    	            }

    	            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.CASH_ACTION);
    	            return modelAndView;
    	        }else{
    	            //ret.put("status", "1");
                    //ret.put("statusDesc", "业务参数数据JSON格式错误");
                    modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
                    modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                    modelAndView.addObject("status", 1);
                    modelAndView.addObject("statusDesc", "业务参数数据JSON格式错误");
                    return modelAndView;
    	        }
    	    }else{
    	        //ret.put("status", "1");
                //ret.put("statusDesc", "无法获取到业务参数信息数据");
                modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
                modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
                modelAndView.addObject("status", 1);
                modelAndView.addObject("statusDesc", "无法获取到业务参数信息数据");
                return modelAndView;
    	    }
    	}else{
    	    //ret.put("status", "1");
            //ret.put("statusDesc", "无法获取到可变临时密钥");
            modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
            modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
            modelAndView.addObject("status", 1);
            modelAndView.addObject("statusDesc", "无法获取到可变临时密钥");
            return modelAndView;
    	}
    }
    
    /**
     * 用户提现后处理(同步)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(UserWithdrawDefine.RETURN_MAPPING)
    public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
        LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
        bean.convert();
        LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
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

        String error = "0";
        String message = "";
        // 取得更新用UUID
        boolean updateFlag = false;
        String uuid = request.getParameter("uuid");
        if (Validator.isNotNull(uuid)) {
            // 取得检证数据
            ChinapnrExclusiveLogWithBLOBs record = userWithdrawService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
            // 如果检证数据状态为未发送
            if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
                // 将状态更新成[2:处理中]
                record.setId(Long.parseLong(uuid));
                record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
                int cnt = this.userWithdrawService.updateChinapnrExclusiveLog(record);
                if (cnt > 0) {
                    updateFlag = true;
                }
            }
        } else {
            updateFlag = true;
        }
        System.out.println(updateFlag);

        // 其他程序正在处理中,或者返回值错误
        if (!updateFlag) {
            JSONObject ret = new JSONObject();
            error = "0";
            message = "";
            ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
            modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
            ret.put("sn", "Withdraw");
            ret.put("userId", bean.getMerPrivPo().getUserId());
            ret.put("cardNo", bean.getOpenAcctId());
            ret.put("total", df.format(new BigDecimal(bean.getTransAmt())));
            ret.put("bank", bean.getOpenBankId());
            ret.put("credited", df.format(new BigDecimal(bean.getRealTransAmt())));
            ret.put("fee", bean.getFeeAmt());
            ret.put("addtime", GetDate.getNowTime10());
            modelAndView.addObject("status", 0);
            modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(),secretKey));
            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                message = "恭喜您，提现成功";
                //modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));// 暂时将显示的提现金额加1,将来手续费上线了再改回来
                //modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
            } else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                message = "汇付处理中，请稍后查询交易明细";
                modelAndView.addObject("statusDesc", message);
            } else {
                error = "1";
                try {
                    message = Validator.isNotNull(bean.getRespDesc()) ? URLDecoder.decode(bean.getRespDesc(), "UTF-8") : "很遗憾，提现失败";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                message = "汇付处理中，请稍后查询交易明细";
                modelAndView.addObject("statusDesc", message);
            }

            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
            return modelAndView;
        }

        // 发送状态
        String status = ChinaPnrConstant.STATUS_VERTIFY_OK;

        // 失败时去汇付查询交易状态
        if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            String transStat = userWithdrawService.checkCashResult(bean.getOrdId());
            if ("S".equals(transStat)) {

                // 取得成功时的信息
                JSONObject data = userWithdrawService.getMsgData(bean.getOrdId());
                if (data != null) {
                    // 设置状态
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPCODE))) {
                        bean.setRespCode(data.getString(ChinaPnrConstant.PARAM_RESPCODE));
                        bean.set(ChinaPnrConstant.PARAM_RESPCODE, data.getString(ChinaPnrConstant.PARAM_RESPCODE));
                    }
                    // 设置结果
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPDESC))) {
                        bean.setRespDesc(data.getString(ChinaPnrConstant.PARAM_RESPDESC));
                        bean.set(ChinaPnrConstant.PARAM_RESPDESC, data.getString(ChinaPnrConstant.PARAM_RESPDESC));
                    }

                    // 设置手续费
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_FEEAMT))) {
                        bean.setFeeAmt(data.getString(ChinaPnrConstant.PARAM_FEEAMT));
                        bean.set(ChinaPnrConstant.PARAM_FEEAMT, data.getString(ChinaPnrConstant.PARAM_FEEAMT));
                    }
                    // 设置取现银行
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_OPENBANKID))) {
                        bean.setOpenBankId(data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
                        bean.set(ChinaPnrConstant.PARAM_OPENBANKID, data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
                    }
                }
            }
        }

        // 成功或审核中或提现失败
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            try {
                Integer userId = userWithdrawService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID));
                // 用户ID
                // String userName = ShiroUtil.getLoginUsername(request); // 用户名

                // 插值用参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", String.valueOf(userId));
                // params.put("userName", userName);
                params.put("ip", CustomUtil.getIpAddr(request));

                // 执行提现后处理
                this.userWithdrawService.handlerAfterCash(bean, params);
                // 执行结果(成功)
                if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                    status = ChinaPnrConstant.STATUS_SUCCESS;
                } else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                    status = ChinaPnrConstant.STATUS_VERTIFY_OK;
                } else if (ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                    status = ChinaPnrConstant.STATUS_FAIL;
                }
                LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "成功");
            } catch (Exception e) {
                // 执行结果(失败)
                status = ChinaPnrConstant.STATUS_FAIL;
                LogUtil.errorLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, e);
            }

        } else {
            // 执行结果(失败)
            status = ChinaPnrConstant.STATUS_FAIL;
            // message = bean.getRespDesc();

            // 更新提现失败原因
            String reason = bean.getRespDesc();
            if(StringUtils.isNotEmpty(reason)){
                if(reason.contains("%")){
                    reason=URLCodec.decodeURL(reason);
                }
            }
            this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);

            LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "失败");
        }

        // 更新状态记录
        if (updateFlag && Validator.isNotNull(uuid)) {
            this.userWithdrawService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
        }
        if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
            error = "0";
            message = "恭喜您，提现成功";
        } else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
            error = "999";
            message = "汇付处理中，请稍后查询交易明细";
        } else {
            error = "1";
            message = "汇付处理中，请稍后查询交易明细";
//          message = "很遗憾，提现失败";//现在不论处理中还是提现失败,均返回处理中
        }
        ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
        modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
        modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
        JSONObject ret = new JSONObject();
        ret.put("sn", "Withdraw");
        ret.put("userId", bean.getMerPrivPo().getUserId());
        ret.put("cardNo", bean.getOpenAcctId());
        ret.put("total", df.format(new BigDecimal(bean.getTransAmt())));
        ret.put("bank", bean.getOpenBankId());
        ret.put("credited", df.format(new BigDecimal(bean.getRealTransAmt())));
        ret.put("fee", bean.getFeeAmt());
        ret.put("addtime", GetDate.getNowTime10());
        modelAndView.addObject("status", 0);
        
        if ("0".equals(error)) {
            //modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt()).add(BigDecimal.ONE)));
            //modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易成功,回调结束]");
            modelAndView.addObject("statusDesc", "[交易成功,回调结束]");
            modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(),secretKey));
            return modelAndView;
        } else if ("999".equals(error)) {
            modelAndView.addObject("message", message);
            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中,回调结束]");
            modelAndView.addObject("statusDesc", message);
            modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(),secretKey));
            return modelAndView;
        } else {
            modelAndView.addObject("status", 1);
            modelAndView.addObject("statusDesc", message);
            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易失败,回调结束]");
            return modelAndView;
        }
    }

    /**
     * 用户提现后处理(异步)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @RequestMapping(UserWithdrawDefine.CALLBACK_MAPPING)
    public ModelAndView cashCallBack(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) throws Exception {
        LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
        bean.convert();
        LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        Map<String, Object> returnParams = new HashMap<String, Object>();
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

        String error = "0";
        String message = "";
        // 取得更新用UUID
        boolean updateFlag = false;
        String uuid = request.getParameter("uuid");
        if (Validator.isNotNull(uuid)) {
            // 取得检证数据
            ChinapnrExclusiveLogWithBLOBs record = userWithdrawService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
            // 如果检证数据状态为未发送
            if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
                // 将状态更新成[2:处理中]
                record.setId(Long.parseLong(uuid));
                record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
                int cnt = this.userWithdrawService.updateChinapnrExclusiveLog(record);
                if (cnt > 0) {
                    updateFlag = true;
                }
            }
        } else {
            updateFlag = true;
        }
        System.out.println(updateFlag);

        // 其他程序正在处理中,或者返回值错误
        if (!updateFlag) {
            JSONObject ret = new JSONObject();
            error = "0";
            message = "";
            ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
            ret.put("sn", "Withdraw");
            ret.put("userId", bean.getMerPrivPo().getUserId());
            ret.put("cardNo", bean.getOpenAcctId());
            ret.put("total", df.format(new BigDecimal(bean.getTransAmt())));
            ret.put("bank", bean.getOpenBankId());
            ret.put("credited", df.format(new BigDecimal(bean.getRealTransAmt())));
            ret.put("fee", bean.getFeeAmt());
            ret.put("addtime", GetDate.getNowTime10());
            modelAndView.addObject("status", 0);
            modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(),secretKey));
            returnParams.put("status", 0);
            returnParams.put("secretKey", URLEncoder.encode(DesECBUtil.encrypt(secretKey, appKey), "UTF-8"));
            returnParams.put("responseObject", URLEncoder.encode(DesECBUtil.encrypt(ret.toString(),secretKey), "UTF-8"));
            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                message = "恭喜您，提现成功";
                //modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));// 暂时将显示的提现金额加1,将来手续费上线了再改回来
                //modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
            } else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                message = "汇付处理中，请稍后查询交易明细";
                modelAndView.addObject("statusDesc", message);
                returnParams.put("statusDesc", URLEncoder.encode(message, "UTF-8"));
            } else {
                error = "1";
                try {
                    message = Validator.isNotNull(bean.getRespDesc()) ? URLDecoder.decode(bean.getRespDesc(), "UTF-8") : "很遗憾，提现失败";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                message = "汇付处理中，请稍后查询交易明细";
                modelAndView.addObject("statusDesc", message);
                returnParams.put("statusDesc", URLEncoder.encode(message, "UTF-8"));
            }
            
            String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.async.callback"), returnParams);
            LogUtil.infoLog(RechargeController.class.toString(), result);

            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
            return modelAndView;
        }

        // 发送状态
        String status = ChinaPnrConstant.STATUS_VERTIFY_OK;

        // 失败时去汇付查询交易状态
        if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            String transStat = userWithdrawService.checkCashResult(bean.getOrdId());
            if ("S".equals(transStat)) {

                // 取得成功时的信息
                JSONObject data = userWithdrawService.getMsgData(bean.getOrdId());
                if (data != null) {
                    // 设置状态
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPCODE))) {
                        bean.setRespCode(data.getString(ChinaPnrConstant.PARAM_RESPCODE));
                        bean.set(ChinaPnrConstant.PARAM_RESPCODE, data.getString(ChinaPnrConstant.PARAM_RESPCODE));
                    }
                    // 设置结果
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPDESC))) {
                        bean.setRespDesc(data.getString(ChinaPnrConstant.PARAM_RESPDESC));
                        bean.set(ChinaPnrConstant.PARAM_RESPDESC, data.getString(ChinaPnrConstant.PARAM_RESPDESC));
                    }

                    // 设置手续费
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_FEEAMT))) {
                        bean.setFeeAmt(data.getString(ChinaPnrConstant.PARAM_FEEAMT));
                        bean.set(ChinaPnrConstant.PARAM_FEEAMT, data.getString(ChinaPnrConstant.PARAM_FEEAMT));
                    }
                    // 设置取现银行
                    if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_OPENBANKID))) {
                        bean.setOpenBankId(data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
                        bean.set(ChinaPnrConstant.PARAM_OPENBANKID, data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
                    }
                }
            }
        }

        // 成功或审核中或提现失败
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            try {
                Integer userId = userWithdrawService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID));
                // 用户ID
                // String userName = ShiroUtil.getLoginUsername(request); // 用户名

                // 插值用参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", String.valueOf(userId));
                // params.put("userName", userName);
                params.put("ip", CustomUtil.getIpAddr(request));

                // 执行提现后处理
                this.userWithdrawService.handlerAfterCash(bean, params);
                // 执行结果(成功)
                if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                    status = ChinaPnrConstant.STATUS_SUCCESS;
                } else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                    status = ChinaPnrConstant.STATUS_VERTIFY_OK;
                } else if (ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                    status = ChinaPnrConstant.STATUS_FAIL;
                }
                LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "成功");
            } catch (Exception e) {
                // 执行结果(失败)
                status = ChinaPnrConstant.STATUS_FAIL;
                LogUtil.errorLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, e);
            }

        } else {
            // 执行结果(失败)
            status = ChinaPnrConstant.STATUS_FAIL;
            // message = bean.getRespDesc();

            // 更新提现失败原因
            String reason = bean.getRespDesc();
            if(StringUtils.isNotEmpty(reason)){
                if(reason.contains("%")){
                    reason=URLCodec.decodeURL(reason);
                }
            }
            this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);

            LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "失败");
        }

        // 更新状态记录
        if (updateFlag && Validator.isNotNull(uuid)) {
            this.userWithdrawService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
        }
        if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
            error = "0";
            message = "恭喜您，提现成功";
        } else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
            error = "999";
            message = "汇付处理中，请稍后查询交易明细";
        } else {
            error = "1";
            message = "汇付处理中，请稍后查询交易明细";
//          message = "很遗憾，提现失败";//现在不论处理中还是提现失败,均返回处理中
        }
        ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_JSP);
        JSONObject ret = new JSONObject();
        ret.put("sn", "Withdraw");
        ret.put("userId", bean.getMerPrivPo().getUserId());
        ret.put("cardNo", bean.getOpenAcctId());
        ret.put("total", df.format(new BigDecimal(bean.getTransAmt())));
        ret.put("bank", bean.getOpenBankId());
        ret.put("credited", df.format(new BigDecimal(bean.getRealTransAmt())));
        ret.put("fee", bean.getFeeAmt());
        ret.put("addtime", GetDate.getNowTime10());
        modelAndView.addObject("status", 0);
        
        if ("0".equals(error)) {
            //modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt()).add(BigDecimal.ONE)));
            //modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易成功,回调结束]");
            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
            modelAndView.addObject("statusDesc", "[交易成功,回调结束]");
            modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(),secretKey));
            returnParams.put("status", 0);
            returnParams.put("secretKey", URLEncoder.encode(DesECBUtil.encrypt(secretKey, appKey), "UTF-8"));
            returnParams.put("statusDesc", URLEncoder.encode("[交易成功,回调结束]", "UTF-8"));
            returnParams.put("responseObject", URLEncoder.encode(DesECBUtil.encrypt(ret.toString(),secretKey), "UTF-8"));
            String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.async.callback"), returnParams);
            LogUtil.infoLog(RechargeController.class.toString(), result);
            return modelAndView;
        } else if ("999".equals(error)) {
            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
            modelAndView.addObject("statusDesc", message);
            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中,回调结束]");
            modelAndView.addObject("statusDesc", message);
            modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(),secretKey));
            returnParams.put("status", 0);
            returnParams.put("secretKey", URLEncoder.encode(DesECBUtil.encrypt(secretKey, appKey), "UTF-8"));
            returnParams.put("statusDesc", URLEncoder.encode(message, "UTF-8"));
            returnParams.put("responseObject", URLEncoder.encode(DesECBUtil.encrypt(ret.toString(),secretKey), "UTF-8"));
            String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.async.callback"), returnParams);
            LogUtil.infoLog(RechargeController.class.toString(), result);
            return modelAndView;
        } else {
            modelAndView.addObject("status", 1);
            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
            modelAndView.addObject("statusDesc", message);
            returnParams.put("status", 1);
            returnParams.put("secretKey", URLEncoder.encode(DesECBUtil.encrypt(secretKey, appKey), "UTF-8"));
            returnParams.put("statusDesc", URLEncoder.encode(message, "UTF-8"));
            returnParams.put("responseObject", "");
            String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.async.callback"), returnParams);
            LogUtil.infoLog(RechargeController.class.toString(), result);
            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易失败,回调结束]");
            return modelAndView;
        }
    }
    
    /**
     * 可取现金额查询
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value=UserWithdrawDefine.CASH_BALANCE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JSONObject cashBalance(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.CASH_BALANCE);
        JSONObject ret = new JSONObject();
        ret.put("request", UserWithdrawDefine.RETURN_REQUEST);
        Object secretKey = request.getAttribute("secretKey");
        Object requestObject = request.getAttribute("requestObject");
        if(secretKey!=null){
            if(requestObject!=null){
                Map<String, String> requestMap = parseRequestJson(requestObject.toString());
                if(requestMap!=null){
                    String userId = requestMap.get("userId");//客户ID
                    if(StringUtils.isNotEmpty(userId)){
                        //获取用户信息
                        Users user = userWithdrawService.getUsers(Integer.parseInt(userId));
                        if(user!=null){
                          //取得用户在汇付天下的客户号
                            AccountChinapnr accountChinapnrTender = userWithdrawService.getAccountChinapnr(Integer.parseInt(userId));
                            if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
                                ret.put("status", "1");
                                ret.put("statusDesc", "用户未开户");
                                return ret;
                            }
                            Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();
                            // 调用汇付接口(提现)
                            ChinapnrBean bean = new ChinapnrBean();
                            bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
                            bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG); // 消息类型(提现)
                            bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));
                            // 跳转到汇付天下画面
                            try {
                                JSONObject result = ChinapnrUtil.callApiBgForYuE(bean);
                                if(result.get("RespCode")!=null && 
                                   ("0".equals(String.valueOf(result.get("RespCode")))||"00".equals(String.valueOf(result.get("RespCode")))||"000".equals(String.valueOf(result.get("RespCode"))))){
                                    ret.put("status", "0");
                                    ret.put("statusDesc", String.valueOf(result.get("RespDesc")));
                                    JSONObject res = new JSONObject();
                                    res.put("userId", result.get("userId"));//用户ID
                                    res.put("avlBal", result.get("AvlBal"));//账户可以支取的余额
                                    res.put("acctBal", result.get("AcctBal"));//账户资金余额，该余额能真正反映账户的资金量
                                    res.put("frzBal", result.get("FrzBal"));//冻结余额
                                    ret.put("responseObject", DesECBUtil.encrypt(res.toString(),String.valueOf(secretKey)));
                                    return ret;
                                }else{
                                    ret.put("status", "1");
                                    ret.put("statusDesc", String.valueOf(result.get("RespDesc")));
                                    return ret;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.CASH_BALANCE);
                            return ret;
                        }else{
                            ret.put("status", "1");
                            ret.put("statusDesc", "没有该用户信息");
                            return ret;
                        } 
                    }else{
                        ret.put("status", "1");
                        ret.put("statusDesc", "用户ID不能为空");
                        return ret;
                    }
                }else{
                    ret.put("status", "1");
                    ret.put("statusDesc", "业务参数数据JSON格式错误");
                    return ret;
                }
            }else{
                ret.put("status", "1");
                ret.put("statusDesc", "无法获取到业务参数信息数据");
                return ret;
            }
        }else{
            ret.put("status", "1");
            ret.put("statusDesc", "无法获取到可变临时密钥");;
            return ret;
        }
    }
    
    /**
     * 
     * 银行卡信息查询接口
     * 
     * @author 朱晓东
     * @return
     */
    @RequestMapping(value=UserWithdrawDefine.GETACCOUNTBANKCARD, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JSONObject getAccountBankCard(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.GETACCOUNTBANKCARD);
        JSONObject ret = new JSONObject();
        ret.put("request", UserWithdrawDefine.RETURN_REQUEST);
        Object secretKey = request.getAttribute("secretKey");
        Object requestObject = request.getAttribute("requestObject");
        if(secretKey!=null){
            if(requestObject!=null){
                Map<String, String> requestMap = parseRequestJson(requestObject.toString());
                if(requestMap!=null){
                    String userId = requestMap.get("userId");//客户ID
                    if(StringUtils.isNotEmpty(userId)){
                        //获取用户信息
                        Users user = userWithdrawService.getUsers(Integer.parseInt(userId));
                        if(user!=null){
                            //查询页面上可以挂载的银行列表
                            List<AccountBank> banks = userWithdrawService.getBankCardByUserId(Integer.parseInt(userId));
                            List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
                            //是否有快捷提现卡
                            Boolean hasBindCard = false;//是否绑了卡
                            Boolean hasBindDefaultCard = false;//是否绑了默认卡
                            Boolean hasBindQuickCard = false;//是否绑了快捷支付卡
                            String defaultCardNo = "";//默认卡卡号
                            if (banks != null && banks.size() != 0) {
                                hasBindCard = true;//是否绑了卡
                                for (AccountBank bank : banks) {
                                    BankCardBean bankCardBean = new BankCardBean();
                                    bankCardBean.setId(bank.getId());
                                    bankCardBean.setBankCode(bank.getBank());
                                    BankConfig bankConfig = userWithdrawService.getBankInfo(bank.getBank());
                                    bankCardBean.setBank(bankConfig.getName());//银行名称 汉字
                                    bankCardBean.setCardNo(bank.getAccount());
                                    bankCardBean.setIsDefault(bank.getCardType());//卡类型 0普通提现卡1默认卡2快捷支付卡
                                    if (bank.getCardType().equals("1")) {
                                        hasBindDefaultCard = true;
                                        defaultCardNo = bank.getAccount();
                                    }
                                    if (bank.getCardType().equals("2")) {
                                        hasBindDefaultCard = true;
                                        hasBindQuickCard = true;
                                        defaultCardNo = bank.getAccount();
                                    }
                                    bankcards.add(bankCardBean);
                                }
                            } else {
                                hasBindCard = false;// 是否绑了卡
                            }
                            ret.put("status", "0");
                            ret.put("statusDesc", "查询完成");
                            JSONObject res = new JSONObject();
                            res.put("userType", user.getUserType());//是否为企业用户（ 0普通用户 1企业用户 企业用户不可绑定其他取现卡）
                            res.put("hasBindCard", hasBindCard);// 是否绑了卡
                            res.put("hasBindDefaultCard", hasBindDefaultCard);// 是否绑了默认卡
                            res.put("hasBindQuickCard", hasBindQuickCard);// 是否绑了快捷支付卡
                            res.put("defaultCardNo", defaultCardNo);// 默认卡卡号
                            res.put("bankcards", JSONObject.toJSON(bankcards));
                            ret.put("responseObject", DesECBUtil.encrypt(res.toString(),String.valueOf(secretKey)));
                            return ret;
                        }else{
                            ret.put("status", "1");
                            ret.put("statusDesc", "没有该用户信息");
                            return ret;
                        } 
                    }else{
                        ret.put("status", "1");
                        ret.put("statusDesc", "用户ID不能为空");
                        return ret;
                    }
                }else{
                    ret.put("status", "1");
                    ret.put("statusDesc", "业务参数数据JSON格式错误");
                    return ret;
                }
            }else{
                ret.put("status", "1");
                ret.put("statusDesc", "无法获取到业务参数信息数据");
                return ret;
            }
        }else{
            ret.put("status", "1");
            ret.put("statusDesc", "无法获取到可变临时密钥");;
            return ret;
        }
    }
    
    /**
     * 检查参数的正确性
     *
     * @param userId
     * @param transAmtStr
     * @param bankId
     * @return
     */
    private JSONObject checkParam(Integer userId, String transAmtStr, String bankId) {
        // 检查用户是否登录
        if (Validator.isNull(userId)) {
            return jsonMessage("您没有登录，请登录后再进行提现。", "1");
        }
        // 判断用户是否被禁用
        Users users = this.userWithdrawService.getUsers(userId);
        if (users == null || users.getStatus() == 1) {
            return jsonMessage("对不起,该用户已经被禁用。", "1");
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
            return jsonMessage("请输入提现金额。", "1");
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal transAmt = new BigDecimal(transAmtStr);
        if (transAmt.compareTo(BigDecimal.ZERO) < 0) {
            return jsonMessage("提现金额不能为负数。", "1");
        }
        // 取得用户当前余额
        Account account = this.userWithdrawService.getAccount(userId);
        // 投标金额大于可用余额
        if (account == null || transAmt.compareTo(account.getBalance()) > 0) {
            return jsonMessage("提现金额大于可用余额，请确认后再次提现。", "1");
        }
        // 检查参数(银行卡ID是否数字)
        if (Validator.isNotNull(bankId) && !NumberUtils.isNumber(bankId)) {
            return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
        }
        return null;
    }
}