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
package com.hyjf.app.user.recharge;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.QpCardInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 *
 * App充值控制器
 *
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月19日
 * @see 下午2:14:02
 */
@Controller
@RequestMapping(value = RechargeDefine.REQUEST_MAPPING)
@Deprecated
public class RechargeController extends BaseController {

    @Autowired
    private AppHFRechargeService appHFRechargeService;

    /** 发布地址 */
    private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

    /** 充值接口 */
    public static final String RECHARGE_URL = HOST_URL + "/hyjf-app/recharge/userRecharge?";

    /**
     *
     * 获取充值规则H5页面
     *
     * @author renxingchen
     * @return
     */
    @RequestMapping(RechargeDefine.RECHARGE_RULE_MAPPING)
    public ModelAndView rechargeRule() {
        ModelAndView modelAndView = new ModelAndView(RechargeDefine.RECHARGE_RULE);
        // 快捷充值卡列表
        List<BankConfig> quickBankList = appHFRechargeService.getBankQuickList();
        modelAndView.addObject("quickBankList", quickBankList);
        // 充值卡限额列表
        List<BankRechargeLimitConfig> bankRechargeLimitList = appHFRechargeService.getRechargeLimitList();
        modelAndView.addObject("bankRechargeLimitList", bankRechargeLimitList);
        return modelAndView;
    }

    /**
     * app用户充值后处理 异步
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RechargeDefine.CALLBACK_MAPPING)
    public ModelAndView rechargeCallback(HttpServletRequest request, @ModelAttribute ChinapnrBean bean, Integer userId, String feeFrom) {
        ModelAndView modelAndView = new ModelAndView();
        if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(bean.getChkValueStatus())) {
            LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
            bean.convert();
            LogUtil.debugLog(RechargeController.class.toString(), RechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
            String ip = CustomUtil.getIpAddr(request);
            // 更新充值的相关信息
            modelAndView = appHFRechargeService.handleRechargeInfo(ip, bean.getMerPrivPo().getFeeFrom() + "", bean.getMerPrivPo().getUserId(), bean, modelAndView);
        } else {
            String message = "响应结果验签失败！";
            modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
            modelAndView.addObject(RechargeDefine.MESSAGE, message);
        }
        return modelAndView;
    }

    /**
     * 用户充值后处理
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RechargeDefine.RETURN_MAPPING)
    public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
        ModelAndView modelAndView = new ModelAndView();
        if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(bean.getChkValueStatus())) {
            LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
            bean.convert();
            LogUtil.debugLog(RechargeController.class.toString(), RechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
            String ip = CustomUtil.getIpAddr(request);
            // 更新充值的相关信息
            modelAndView = appHFRechargeService.handleRechargeInfo(ip, bean.getMerPrivPo().getFeeFrom() + "", bean.getMerPrivPo().getUserId(), bean, modelAndView);
        } else {
            String message = "响应结果验签失败！";
            modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
            modelAndView.addObject(RechargeDefine.MESSAGE, message);
        }
        return modelAndView;
    }

    /**
     *
     * app端用户充值
     *
     * @author renxingchen
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RechargeDefine.USER_RECHARGE_ACTION)
    public ModelAndView userRecharge(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.USER_RECHARGE_ACTION);
        ModelAndView modelAndView = new ModelAndView(RechargeDefine.JSP_CHINAPNR_SEND);

        String sign = request.getParameter("sign");

        Integer userId = SecretUtil.getUserId(sign); // 用户ID
        String username = SecretUtil.getUserName(sign);// 用户名
        String transAmt = request.getParameter("money");// 交易金额
        String openBankId = request.getParameter("code");// 开户银行代号
        String cardNo = request.getParameter("cardNo");// 开户银行代号
        // String rechargeType = request.getParameter("rechargeType");// 充值类型
        String platform = request.getParameter("platform");// 终端类型

        String message = "";
        JSONObject checkResult;
        if (StringUtils.isEmpty(cardNo)) {
            checkResult = checkParam(request, userId, transAmt);
            openBankId = "";
        } else {
            // 检查参数
            checkResult = checkParam(request, userId, transAmt, openBankId);
        }
        if (checkResult != null) {
            message = checkResult.getString("参数校验错误");
            modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
            modelAndView.addObject(RechargeDefine.MESSAGE, message);
            return modelAndView;
        }

        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnrTender = appHFRechargeService.getAccountChinapnr(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
            message = "您还未开户，请开户后重新操作";
            modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
            modelAndView.addObject(RechargeDefine.MESSAGE, message);
            LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.USER_RECHARGE_ACTION, "[用户未开户]");
            return modelAndView;
        }
        Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();

        String openAcctId = "";// 银行卡号
        String dcFlag = "";// 借贷记标记
        String pageType = "1"; // 页面类型

        // 支付网关业务代号
        String gateBusiId = "";
        UsersInfo usersInfo = appHFRechargeService.getUsersInfoByUserId(userId);
        int roleId = usersInfo.getRoleId();
        String feeFrom;
        ChinapnrBean bean = new ChinapnrBean();
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
        usersInfo.getBorrowerType();
        if (StringUtils.isEmpty(cardNo)) {
            cardNo = "";
            gateBusiId = "B2C";
        } else {
            // 快捷支付
            gateBusiId = "QP"; // 快捷支付
            openAcctId = cardNo;
        }
        dcFlag = "D";// 储蓄卡

        // 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + RechargeDefine.REQUEST_MAPPING + RechargeDefine.RETURN_MAPPING;
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + RechargeDefine.REQUEST_MAPPING + RechargeDefine.CALLBACK_MAPPING;// 支付工程路径
        bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
        bean.setCmdId(ChinaPnrConstant.CMDID_NET_SAVE); // 消息类型(主动投标)
        bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
        bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
        bean.setOrdDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setGateBusiId(gateBusiId);// 支付网关业务代号
        bean.setOpenBankId(openBankId);// 开户银行代号
        bean.setDcFlag(dcFlag);// 借贷记标记
        bean.setTransAmt(CustomUtil.formatAmount(transAmt));// 交易金额(必须)
        bean.setRetUrl(retUrl); // 页面返回 URL
        bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
        bean.setOpenAcctId(openAcctId);// 银行卡号
        bean.setReqExt(jobj.toJSONString());// 设置充值拓展域
        // bean.setCertId(certId); // 身份证号
        // 查询手续费收取方
        MerPriv merPriv = new MerPriv();
        merPriv.setUserId(userId);
        merPriv.setFeeFrom(feeFrom);
        bean.setMerPrivPo(merPriv); // 商户私有域
        bean.setPageType(pageType);// 页面类型
        bean.setType("user_recharge"); // 日志类型(写日志用)
        // 插值用参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", String.valueOf(userId));
        // params.put("userName", userName);
        params.put("ip", GetCilentIP.getIpAddr(request));
        params.put("client", platform);
        params.put("username", username);
        params.put("feeFrom", feeFrom);
        // 用户充值前处理
        int cnt = this.appHFRechargeService.updateBeforeRecharge(bean, params);
        if (cnt > 0) {
            // 跳转到汇付天下画面
            try {
                modelAndView = ChinapnrUtil.callApi(bean);
            } catch (Exception e) {
                message = ("系统异常");
                modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
                modelAndView.addObject(RechargeDefine.MESSAGE, message);
                return modelAndView;
            }
        } else {
            message = "请不要重复操作";
            modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
            modelAndView.addObject(RechargeDefine.MESSAGE, message);
            return modelAndView;
        }
        LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.USER_RECHARGE_ACTION);
        return modelAndView;
    }

    /**
     *
     * 数据校验
     *
     * @author renxingchen
     * @param request
     * @param userId
     * @param transAmtStr
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr) {
        // 判断用户是否被禁用
        Users users = this.appHFRechargeService.getUsers(userId);
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
        AccountChinapnr accountChinapnrTender = this.appHFRechargeService.getAccountChinapnr(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
            return jsonMessage("用户未开户，请开户后再充值。", "1");
        }
        return null;
    }

    /**
     *
     * 数据校验
     *
     * @author renxingchen
     * @param request
     * @param userId
     * @param transAmt
     * @param openBankId
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String openBankId) {
        // 判断用户是否被禁用
        Users users = this.appHFRechargeService.getUsers(userId);
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
        AccountChinapnr accountChinapnrTender = this.appHFRechargeService.getAccountChinapnr(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
            return jsonMessage("用户未开户，请开户后再充值。", "1");
        }
        return null;
    }

    /**
     *
     * app获取快捷充值地址的数据接口 需要将请求参数拼接到地址上并带回
     *
     * @author renxingchen
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RechargeDefine.GET_RECHARGE_URL_ACTION)
    public Object getRechargeUrl(AppRechargeVo vo) {
        RechargeUrlResultVo resultVo = new RechargeUrlResultVo(RechargeDefine.GET_RECHARGE_URL);
        // 校验数据并拼接回传地址
        if (Validator.isNull(vo.getMoney())) {
            resultVo.setStatusDesc("请求参数非法");
        } else {// 拼接充值地址并返回
            StringBuffer sb = new StringBuffer(RECHARGE_URL);
            sb.append("version=").append(vo.getVersion()).append(CustomConstants.APP_PARM_AND).append("netStatus=").append(vo.getNetStatus()).append(CustomConstants.APP_PARM_AND).append("platform=").append(vo.getPlatform()).append(CustomConstants.APP_PARM_AND).append("token=")
                    .append(strEncode(vo.getToken())).append(CustomConstants.APP_PARM_AND).append("sign=").append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("randomString=").append(vo.getRandomString()).append(CustomConstants.APP_PARM_AND).append("order=")
                    .append(strEncode(vo.getOrder())).append(CustomConstants.APP_PARM_AND).append("platform=").append(strEncode(vo.getPlatform())).append(CustomConstants.APP_PARM_AND).append("money=").append(vo.getMoney()).append(CustomConstants.APP_PARM_AND).append("cardNo=").append(vo.getCardNo())
                    .append(CustomConstants.APP_PARM_AND).append("code=").append(vo.getCode());
            resultVo.setRechargeUrl(sb.toString());
            resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
            resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
        }
        return resultVo;
    }

    /**
     *
     * app获取快捷充值卡及手续费的数据接口
     *
     * @author renxingchen
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RechargeDefine.GET_QP_RECHARGE_INFO_ACTION)
    public Object getQpRechargeInfo(AppRechargeVo vo) {
        RechargeInfoResultVo resultVo = new RechargeInfoResultVo(RechargeDefine.GET_QP_RECHARGE_INFO, HOST_URL + RechargeDefine.RECHARGE_RULE_URL);
        // 获取key
        String key = SecretUtil.getKey(vo.getSign());
        if (StringUtils.isEmpty(key)) {
            resultVo.setStatus(CustomConstants.SIGN_ERROR);
            resultVo.setStatusDesc("获取数据加密秘钥失败");
        } else {
            // 验证order
            if (SecretUtil.checkOrder(key, vo.getToken(), vo.getRandomString(), vo.getOrder())) {
                // 获取用户编号
                Integer userId = SecretUtil.getUserId(vo.getSign());
                if (null != userId) {

                    //add by xiashuqing 20171204 begin app改版2.1新增查询账户余额
                    Account account = this.appHFRechargeService.getAccount(userId);
                    if (account != null) {
                        resultVo.setAvailableAmount(account.getBankBalance().toString());
                    }
                    //add by xiashuqing 20171204 end app改版2.1新增查询账户余额

                    // 获取用户的快捷卡信息
                    QpCardInfo qpCardInfo = this.appHFRechargeService.getUserQpCard(userId);
                    if (null != qpCardInfo) {
                        resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
                        resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
                        resultVo.setBank(qpCardInfo.getBank());
                        resultVo.setLogo(HOST_URL + qpCardInfo.getLogo());
                        resultVo.setCardNo(qpCardInfo.getCardNo());
                        resultVo.setCode(qpCardInfo.getCode());
                        if (!StringUtils.isEmpty(vo.getMoney())) {// 根据快捷卡计算充值手续费
                            Long money;
                            try {
                                money = Long.parseLong(vo.getMoney());
                            } catch (Exception e) {
                                resultVo = new RechargeInfoResultVo(RechargeDefine.GET_QP_RECHARGE_INFO, HOST_URL + RechargeDefine.RECHARGE_RULE_URL);
                                resultVo.setStatusDesc("请输入有效的充值金额");
                                return resultVo;
                            }
                            // 获取银行卡快捷支付费率
                            FeeConfig feeConfig = this.appHFRechargeService.getQpCardFee(qpCardInfo.getCode());
                            BigDecimal fee;
                            BigDecimal balance;
                            UsersInfo usersInfo = this.appHFRechargeService.getUsersInfoByUserId(userId);
                            int roleId = usersInfo.getRoleId();
                            if ((2 == roleId) && (null != usersInfo.getBorrowerType() && usersInfo.getBorrowerType() > 1)) {// 如果是借款人充值，并且是外部借款人，则需要计算手续费
                                // 计算快捷充值手续费
                                fee = new BigDecimal(vo.getMoney()).multiply(new BigDecimal(feeConfig.getQuickPayment()));
                                balance = new BigDecimal(money).subtract(new BigDecimal(CustomConstants.DF_FOR_VIEW_V1.format(fee)));
                                resultVo.setBalance(CustomConstants.DF_FOR_VIEW.format(balance));
                                resultVo.setFee(CustomConstants.DF_FOR_VIEW.format(fee));
                            } else {
                                fee = BigDecimal.ZERO;
                                balance = new BigDecimal(money);
                                resultVo.setBalance(CustomConstants.DF_FOR_VIEW.format(balance));
                                resultVo.setFee(CustomConstants.DF_FOR_VIEW.format(fee));
                            }
                            // 拼接展示信息字符串
                            String moneyInfo = RechargeDefine.FEE + CustomConstants.DF_FOR_VIEW.format(fee) + RechargeDefine.RECHARGE_INFO_SUFFIX + RechargeDefine.BALANCE + CustomConstants.DF_FOR_VIEW.format(balance) + RechargeDefine.RECHARGE_INFO_SUFFIX;
                            resultVo.setMoneyInfo(moneyInfo);
                        } else {
                            String moneyInfo = RechargeDefine.FEE + "0.00" + RechargeDefine.RECHARGE_INFO_SUFFIX + RechargeDefine.BALANCE + "0.00" + RechargeDefine.RECHARGE_INFO_SUFFIX;
                            resultVo.setMoneyInfo(moneyInfo);
                        }

                    } else {
                        resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
                        resultVo.setStatusDesc("未查询到用户快捷卡信息");
                    }
                } else {
                    resultVo.setStatus(CustomConstants.TOKEN_ERROR);
                    resultVo.setStatusDesc("用户认证失败");
                }
            } else {
                resultVo.setStatusDesc(RechargeDefine.GET_QP_RECHARGE_INFO);
                resultVo.setStatusDesc("数据验证失败");
            }
        }
        return resultVo;
    }
}
