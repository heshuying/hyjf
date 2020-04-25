package com.hyjf.api.server.user.nonwithdraw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.nonwithdraw.NonCashWithdrawService;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallParamConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 
 * 外部服务接口:免密提现
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年9月6日
 * @see 下午4:44:36
 */
@Controller
@RequestMapping(NonCashWithdrawDefine.REQUEST_MAPPING)
public class NonCashWithdrawServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(NonCashWithdrawServer.class);

	@Autowired
	private NonCashWithdrawService userNonCashWithdrawService;

	@ResponseBody
	@RequestMapping(value = NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION , method = RequestMethod.POST)
	public JSONObject nonCashWithdraw(HttpServletRequest request, @RequestBody NonCashWithdrawRequestBean nonCashWithdrawRequestBean , HttpServletResponse response) {
		LogUtil.startLog(NonCashWithdrawDefine.THIS_CLASS, NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION);
		_log.info("外部服务接口:免密提现 参数为"+
		nonCashWithdrawRequestBean.getAccountId()+"---AccountId"+
		nonCashWithdrawRequestBean.getAccount()+"---CardNo"+
		nonCashWithdrawRequestBean.getCardNo()+"---channel"+
		nonCashWithdrawRequestBean.getChannel()+"---chkValue"+
		nonCashWithdrawRequestBean.getChkValue()+"---");
		//返回结果对象
		NonCashWithdrawResultBean resultBean = new NonCashWithdrawResultBean();
		try {
			// 用户电子账户号
			String accountId = nonCashWithdrawRequestBean.getAccountId();
			// 提现金额
			String account = nonCashWithdrawRequestBean.getAccount();
			// 提现银行卡
	        String cardNo = nonCashWithdrawRequestBean.getCardNo();
	        // 免密提现接口不能用了返回错误信息
	        if (true) {
	            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
	            resultBean.setStatusDesc("银行卡号不能为空");
	            JSONObject result = resultBean.toJson();
                return result;
	        }
			// 检查下游提交的参数
			boolean isCheck = checkParam(nonCashWithdrawRequestBean,resultBean);
			// 参数检查完毕
			if (!isCheck) {
			    JSONObject result = resultBean.toJson();
			    return result;
			}
			// 根据电子账户号查询用户ID
			BankOpenAccount bankOpenAccount = this.userNonCashWithdrawService.selectBankOpenAccountByAccountId(accountId);
			if (bankOpenAccount == null) {
				_log.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
				resultBean.setStatusDesc("根据电子账户号查询用户信息失败");
				return resultBean.toJson();
			}
			// 用户ID
			Integer userId = bankOpenAccount.getUserId();
			
			// 根据用户ID查询是否已经开通无密消费功能  以及查询订单号
			HjhUserAuth userAuth = this.userNonCashWithdrawService.getUserAuthByUserId(userId);
            if (userAuth == null || userAuth.getAutoWithdrawStatus() == null || userAuth.getAutoWithdrawStatus().equals(0)
                    || Validator.isNull(userAuth.getAutoOrderId())) {
                _log.info("该用户尚未开通无密消费功能,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000001);
                resultBean.setStatusDesc("用户暂未开通该服务");
                return resultBean.toJson();
            }
			String contOrderId = userAuth.getAutoOrderId();
			// 根据用户ID查询用户信息
			Users user = this.userNonCashWithdrawService.getUsersByUserId(userId);
			if (user == null) {
				_log.info("根据用户ID查询用户信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
				resultBean.setStatusDesc("查询用户失败");
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000003);
				return resultBean.toJson();
			}

			// 根据用户ID查询用户详情
			UsersInfo userInfo = this.userNonCashWithdrawService.getUsersInfoByUserId(userId);
			if (userInfo == null) {
				_log.info("根据用户ID查询用户详情失败,用户电子账户号:[" + accountId + ",用户ID:[" + userId + "]");
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000003);
				resultBean.setStatusDesc("查询用户详情失败");
				return resultBean.toJson();
			}
			
			// 根据用户ID查询用户平台银行卡信息
			BankCard bankCard = this.userNonCashWithdrawService.getBankCardByUserId(userId);
			if (bankCard == null) {
				_log.info("根据用户ID查询用户银行卡信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000008);
				resultBean.setStatusDesc("查询用户银行卡信息失败");
				return resultBean.toJson();
			}
			// 用户汇盈平台的银行卡卡号
			String localCardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
			// 如果两边银行卡号不一致
			if (!cardNo.equals(localCardNo)) {
				_log.info("用户银行卡信息不一致,用户电子账户号:[" + accountId + "],请求银行卡号:[" + cardNo + "],平台保存的银行卡号:[" + localCardNo + "].");
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000002);
				resultBean.setStatusDesc("用户银行卡信息不一致");
				return resultBean.toJson();
			}
			
			// 根据用户ID查询用户账户信息
            Account hyAccount = this.userNonCashWithdrawService.getAccount(userId);
            // 取得账户为空
            if (hyAccount == null) {
                _log.info("根据用户ID查询用户账户信息失败,用户ID:[" + userId + "],电子账户号:[" + accountId + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000009);
                resultBean.setStatusDesc("查询用户账户信息失败");
                return resultBean.toJson();

            }
            // 提现金额大于汇盈账户余额
            if (new BigDecimal(account).compareTo(hyAccount.getBankBalance()) > 0) {
                _log.info("提现金额大于汇盈账户可用余额,用户ID:[" + userId + "],电子账户号:[" + accountId + "],提现金额:[" + account + "],账户可用余额:[" + hyAccount.getBankBalance() + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000010);
                resultBean.setStatusDesc("用户账户余额不足");
                return resultBean.toJson();
            }
            
			// 取得手续费 默认1
			//String fee = this.userNonCashWithdrawService.getWithdrawFee(userId, cardNo, new BigDecimal(account));
			BigDecimal fee = userNonCashWithdrawService.getUserFee(nonCashWithdrawRequestBean.getInstCode());
			// 实际取现金额
			// 去掉一块钱手续费
			// 提现金额大于手续费
			if (!(new BigDecimal(account).compareTo(fee) > 0)) {
			    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000006);
			    resultBean.setStatusDesc("提现金额不能小于手续费");
			    return resultBean.toJson();
			}
			account = new BigDecimal(account).subtract(fee).toString();
			
			// 调用江西银行免密提现
			resultBean = sendToBank(request, nonCashWithdrawRequestBean , resultBean, userInfo , user , bankCard  , contOrderId,account);
			// 调用江西银行免密提现 end
		} catch (Exception e) {
		    e.printStackTrace();
			_log.info("免密提现发生异常,异常信息:[" + e.getMessage() + "].");
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
			resultBean.setStatusDesc("系统异常");
			return resultBean.toJson();
		}
		
        if (resultBean.getStatus() == ErrorCodeConstant.SUCCESS) {
            return resultBean.toSuccessJson();
        } else {
            return resultBean.toJson();
        }
		
	}

	/**
	 * 
	 * 调用江西银行免密提现接口进行提现
	 * @author yyc
	 * @param request
	 * @param nonCashWithdrawRequestBean
	 * @param resultBean
	 * @param userInfo
	 * @param user
	 * @param bankCard
	 * @param fee
	 * @param contOrderId 
	 * @param account 
	 * @throws Exception 
	 */
    private NonCashWithdrawResultBean sendToBank(HttpServletRequest request, NonCashWithdrawRequestBean nonCashWithdrawRequestBean,
        NonCashWithdrawResultBean resultBean, UsersInfo userInfo, Users user, BankCard bankCard, String contOrderId, String account) throws Exception {

        // 身份证号
        String idNo = userInfo.getIdcard();
        // 姓名
        String trueName = userInfo.getTruename();
        // 用户手机号
        String mobile = user.getMobile();
        // 提现用户名
        String userName = user.getUsername();
        // userId
        Integer userId = user.getUserId();
        // 用户电子账户号
        String accountId = nonCashWithdrawRequestBean.getAccountId();
        // 提现银行卡
        String cardNo = nonCashWithdrawRequestBean.getCardNo();
        // 渠道
        String channel = nonCashWithdrawRequestBean.getChannel();
        // 请求的IP
        String ip = CustomUtil.getIpAddr(request);
        // logOrderId
        String logOrderId =GetOrderIdUtils.getOrderId2(userId);
        // 查询手续费
        BigDecimal fee = userNonCashWithdrawService.getUserFee(nonCashWithdrawRequestBean.getInstCode());
        // 组装报文请求上游
        String routeCode = "";
        // 调用汇付接口(2.2.2.免密提现)
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(logOrderId);
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("外部服务接口:用户免密提现");
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_AGREE_WITHDRAW);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(channel);// 交易渠道
        bean.setAccountId(accountId);// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(idNo);// 证件号
        bean.setName(trueName);// 姓名
        bean.setMobile(mobile);// 手机号
        bean.setCardNo(cardNo);// 银行卡号
        bean.setTxAmount(CustomUtil.formatAmount(account));// 提现金额
        bean.setTxFee(fee+"");
        bean.setContOrderId(contOrderId);// 签约订单号  todo
        /*// 扣除手续费
        if ((new BigDecimal(account).compareTo(new BigDecimal(50000)) > 0)
                && StringUtils.isNotBlank(payAllianceCode)) {
            routeCode = "2";// 路由代码
            bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
        }
        if ("2".equals(routeCode)) {
            bean.setRouteCode(routeCode);
            LogAcqResBean logAcq = new LogAcqResBean();
            logAcq.setPayAllianceCode(payAllianceCode);
            bean.setLogAcqResBean(logAcq);
        }*/
        // 企业用户提现
       /* if (user.getUserType() == 1) { // 企业用户 传组织机构代码
            CorpOpenAccountRecord record = userNonCashWithdrawService.getCorpOpenAccountRecord(userId);
            bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType())
                    : BankCallConstant.ID_TYPE_COMCODE);// 证件类型
            bean.setIdNo(record.getBusiCode());
            bean.setName(record.getBusiName());
            bean.setRouteCode("2");
            bean.setCardBankCnaps(
                    StringUtils.isEmpty(payAllianceCode) ? bankCard.getPayAllianceCode() : payAllianceCode);
        }*/
        
        // 插值用参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", String.valueOf(userId));
        params.put("userName", userName);
        params.put("ip", ip);
        params.put("cardNo", cardNo);
        params.put("fee", fee+"");
        params.put("client", nonCashWithdrawRequestBean.getPlatform());
        // 用户提现前处理
        int cnt = this.userNonCashWithdrawService.updateBeforeCash(bean, params);
        // 插入日志表成功后，调用江西银行
        if (cnt > 0) {
            // 调用江西银行的接口
            BankCallBean openAccountResult = BankCallUtils.callApiBg(bean);
            if (openAccountResult == null) {
                _log.info("调用江西银行免密提现接口,银行返回空,电子账户号:[" + accountId + "],提现金额:[" + userId + "],银行卡号:[" + cardNo + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("系统异常");
                return resultBean;
            }
            // 免密提现成功
            _log.info("免密提现,提现订单号:[" + logOrderId + "],用户ID:[" + userId + ",用户电子账户号:[" + openAccountResult.getAccountId() + "]");
            
            //免密提现成功后操作
            doSuccess(resultBean, openAccountResult, String.valueOf(userId), ip, logOrderId,nonCashWithdrawRequestBean.getInstCode());
            
        } else {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("系统错误");
            return resultBean;
        }
        
        return resultBean;
    }

    /**
     * 
     * 提现成功后操作
     * @author yyc
     * @param resultBean
     * @param bean
     * @param userId
     * @param ip
     * @param logOrderId
     * @param code 
     * @return
     * @throws Exception
     */
    private NonCashWithdrawResultBean doSuccess(NonCashWithdrawResultBean resultBean, BankCallBean bean, String userId,
        String ip, String logOrderId, String code) throws Exception {
        // 插值用参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("ip", ip);
        // 手续费从数据库中获取
        BigDecimal fee = userNonCashWithdrawService.getUserFee(code);
        params.put("fee", fee.toString());

        JSONObject accountwithdraw = this.userNonCashWithdrawService.handlerAfterCash(bean, params);
        // 提现金额
        BigDecimal transAmt = bean.getBigDecimal(BankCallParamConstant.PARAM_TXAMOUNT);
        //String fee = userNonCashWithdrawService.getUserFee(Integer.parseInt(userId), bean.getCardNo(), transAmt);
        // 提现手续费
        BigDecimal feeAmt = fee;
        // 总的交易金额
        BigDecimal total = transAmt.add(feeAmt);
        // 更新订单信息
        if(BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())){
            // 提现成功
            _log.info("accountwithdraw:"+accountwithdraw==null?"":accountwithdraw.toString());
            if (accountwithdraw != null && accountwithdraw.getString("error").equals("0")) {
                resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
                resultBean.setStatusDesc("恭喜您，提现成功");
                resultBean.setAmt(String.valueOf(total));// 交易金额
                resultBean.setArrivalAmount(String.valueOf(transAmt));// 到账金额
                resultBean.setFee((CustomUtil.formatAmount(feeAmt.toString())));// 提现手续费
                resultBean.setOrderId(logOrderId+""); // orderId
            }else {
                _log.info("提现失败");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000007);
                resultBean.setStatusDesc("提现失败");
            }
        }else{
            _log.info("提现失败");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000007);
            resultBean.setStatusDesc("提现失败");
        }
        
        return resultBean;
    }

    /**
	 * 
	 * 检查下游提交参数
	 * @author sunss
	 * @param nonCashWithdrawRequestBean
	 * @param resultBean
	 * @return
	 */
    private boolean checkParam(NonCashWithdrawRequestBean nonCashWithdrawRequestBean, NonCashWithdrawResultBean resultBean) {
        // 用户电子账户号
        String accountId = nonCashWithdrawRequestBean.getAccountId();
        // 提现金额
        String account = nonCashWithdrawRequestBean.getAccount();
        // 提现银行卡
        String cardNo = nonCashWithdrawRequestBean.getCardNo();
        // 渠道
        String channel = nonCashWithdrawRequestBean.getChannel();
        // 机构编号
        String instCode = nonCashWithdrawRequestBean.getInstCode();
        // 提现平台
        String platform = nonCashWithdrawRequestBean.getPlatform();
        
        _log.info("下游上传银行卡号："+cardNo);
        // 银行卡号
        if (Validator.isNull(cardNo)) {
        	resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
        	resultBean.setStatusDesc("银行卡号不能为空");
            return false;
        }
        // 银行电子账户号
        if (Validator.isNull(accountId)) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
        	resultBean.setStatusDesc("银行电子账户号不能为空");
        	 return false;
        }
        // 渠道
        if (Validator.isNull(channel)) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
        	resultBean.setStatusDesc("渠道不能为空");
        	return false;
        }
        // 充值金额
        if (Validator.isNull(account)) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
        	resultBean.setStatusDesc("提现金额不能为空");
        	return false;
        }
        
        // 机构编号
        if (Validator.isNull(instCode)) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("机构编号不能为空");
            return false;
        }
        
        // 提现平台
        if (Validator.isNull(platform)) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("提现平台不能为空");
            return false;
        }
        
        // 充值金额校验
        if (!account.matches("-?[0-9]+.*[0-9]*")) {
        	_log.info("提现金额格式错误,充值金额:[" + account + "]");
        	resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
        	resultBean.setStatusDesc("提现金额格式错误");
        	return false;
        }
        
        //判断小数位数
        if(account.indexOf(".")>=0){
            String l = account.substring(account.indexOf(".")+1,account.length());
            if(l.length()>2){
                _log.info("提现金额不能大于两位小数,充值金额:[" + account + "]");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                resultBean.setStatusDesc("提现金额格式错误");
                return false;
            }
        }
        
       /* // 工行或人行
        if ((new BigDecimal(account).compareTo(new BigDecimal(50001)) > 0) && StringUtils.isBlank(payAllianceCode)) {
            _log.info("大额提现时,银行联行号为空,银行类型:[" + ("7".equals(bankType) ? "中国工商银行" : "中国银行") + "],电子账户号:[" + accountId + "],提现金额:[" + account + "].");
            resultBean.setStatus(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("大额提现时,银行联行号不能为空");
            return false;
        }*/
        // 验签
       /* if (!this.checkSign(nonCashWithdrawRequestBean, BaseDefine.METHOD_SERVER_NON_CASH_WITHDRAW)) {
            _log.info("----验签失败----");
            resultBean.setStatus(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("验签失败！");
            return false;
        }*/
        // 去掉验签  2017-11-24
        if (!this.verifyRequestSign(nonCashWithdrawRequestBean, NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION)) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
            _log.info("-------------------验签失败！--------------------");
            resultBean.setStatusDesc("验签失败！");
            return false;
        }
        
        return true;
    }
    
    @ResponseBody
    @RequestMapping(value = NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION_TEST , method = RequestMethod.POST)
    public JSONObject nonCashWithdrawTest(HttpServletRequest request, NonCashWithdrawRequestBean nonCashWithdrawRequestBean , HttpServletResponse response) {
        return nonCashWithdraw(request, nonCashWithdrawRequestBean, response);
    }
}
