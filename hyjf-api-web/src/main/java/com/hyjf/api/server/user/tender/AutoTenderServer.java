package com.hyjf.api.server.user.tender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.tender.AutoTenderDefine;
import com.hyjf.api.server.tender.AutoTenderRequestBean;
import com.hyjf.api.server.tender.AutoTenderResultBean;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 自动出借接口，针对散标
 * 
 * @author xiaojohn
 *
 */
@Controller
@RequestMapping(value = AutoTenderDefine.REQUEST_MAPPING)
public class AutoTenderServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(AutoTenderServer.class);

	@Autowired
	private TenderService tenderService;

	@Autowired
	private EvaluationService evaluationService;

	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value = AutoTenderDefine.AUTOTENDER_ACTION)
	public AutoTenderResultBean autoTender(@RequestBody AutoTenderRequestBean autoTenderRequestBean, HttpServletRequest request, HttpServletResponse response) {
		_log.info("出借输入参数.... autoTenderRequestBean is :{}", JSONObject.toJSONString(autoTenderRequestBean));
		AutoTenderResultBean resultBean = new AutoTenderResultBean();
		resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
		resultBean.setStatusDesc("请求参数非法");

		// ————> 验证请求参数
		if (Validator.isNull(autoTenderRequestBean) || Validator.isNull(autoTenderRequestBean.getInstCode())
				|| Validator.isNull(autoTenderRequestBean.getMoney()) 
				|| autoTenderRequestBean.getAccountId() == null 
				|| Validator.isNull(autoTenderRequestBean.getBorrowNid())
				|| Validator.isNull(autoTenderRequestBean.getChkValue())
				|| Validator.isNull(autoTenderRequestBean.getTimestamp())) {
			_log.info("请求参数非法-------" + autoTenderRequestBean);
			return resultBean;
		}

		// ————> 验签
		if (!this.verifyRequestSign(autoTenderRequestBean, AutoTenderDefine.REQUEST_MAPPING+AutoTenderDefine.AUTOTENDER_ACTION)) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
			_log.info("验签失败！---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid());
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		
		_log.info("开始自动出借，请求参数 " + JSON.toJSONString(autoTenderRequestBean));
		String couponGrantId = "";
		String bizAccount = autoTenderRequestBean.getAccountId();
		// 借款borrowNid
		String borrowNid = autoTenderRequestBean.getBorrowNid();
		// 出借金额
		String accountStr = autoTenderRequestBean.getMoney();
		if (accountStr == null || "".equals(accountStr)) {
			accountStr = "0";
		}
		
		// ————> 出借校验
		JSONObject result = tenderService.checkAutoTenderParam(borrowNid, accountStr, bizAccount, "0", couponGrantId);
		if (result == null) {
			_log.info("出借校验失败---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid());
			resultBean.setStatusForResponse("TZ000099");
			resultBean.setStatusDesc("出借失败,系统错误");
			return resultBean;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			_log.info("出借校验失败，原因: "+result.get("data").toString()+"  "+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid());
			resultBean.setStatusForResponse("TZ000096");
			resultBean.setStatusDesc("出借失败，原因: "+result.get("data").toString());
			return resultBean;
		}
		
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.tenderService.getBorrowByNid(borrowNid);
		if (borrow == null || borrow.getBorrowNid() == null) {
			resultBean.setStatusForResponse("TZ000001");
			resultBean.setStatusDesc("标的编号不存在！");
			return resultBean;
		}
		
		String account = accountStr;
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		String tenderUserName = result.getString("tenderUserName");
		String autoOrderId = result.getString("tenderAutoOrderId");
		Integer userId = Integer.parseInt(result.getString("userId"));
		
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		// ————> 写日志 调用出借接口
		Boolean flag = false;
		BankCallBean registResult = null;
		try {
			flag = tenderService.updateTenderLog(borrowNid, orderId, userId, account, GetCilentIP.getIpAddr(request), couponGrantId, tenderUserName);
			
			if (!flag) {
				resultBean.setStatusForResponse("TZ000099");
				resultBean.setStatusDesc("出借失败,系统错误");
				return resultBean;
			}
			_log.info("出借调用接口前---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid()+" ordId: "+orderId);
			BankCallBean bean = new BankCallBean();
			// 获取共同参数
			String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
			String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
			// 生成订单
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallConstant.TXCODE_BIDAUTO_APPLY);// 交易代码
			bean.setInstCode(instCode);
			bean.setBankCode(bankCode);
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
			
			bean.setAccountId(tenderUsrcustid);// 电子账号
			bean.setOrderId(orderId);// 订单号
			bean.setTxAmount(CustomUtil.formatAmount(account.toString()));// 交易金额
			bean.setProductId(borrowNid);// 标的号
//			bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_FREEZE);// 是否冻结金额
			bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_UNFREEZE);// 是否冻结金额
			bean.setContOrderId(autoOrderId);
			
			bean.setLogOrderId(orderId);// 订单号
			bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
			bean.setLogUserId(String.valueOf(userId));// 出借用户
			bean.setLogRemark("自动投标申请");
			bean.setLogClient(0);
			try {
				registResult = BankCallUtils.callApiBg(bean);
			} catch (Exception e) {
				_log.info("出借调用接口异常---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid()+" ordId: "+orderId);
				removeTenderTmp(borrowNid, userId, orderId);
				
				e.printStackTrace();
				resultBean.setStatusForResponse("TZ000098");
				resultBean.setStatusDesc("调用出借银行接口异常");
				return resultBean;
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			resultBean.setStatusForResponse("TZ000099");
			resultBean.setStatusDesc("出借失败,系统错误");
			return resultBean;
		}
		
		if(registResult == null || StringUtils.isBlank(registResult.getRetCode())){
			_log.info("出借调用接口返回为空---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid()+" ordId: "+orderId);
//			removeTenderTmp(borrowNid, userId, orderId);
			
			resultBean.setStatusForResponse("TZ000098");
			resultBean.setStatusDesc("调用出借银行接口异常");
			return resultBean;
		}
		
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(registResult.getRetCode())) {
			// 返回码提示余额不足，不结冻
			_log.info("用户:" + userId + " 出借接口调用失败，错误码：" + registResult.getRetCode());
			removeTenderTmp(borrowNid, userId, orderId);
			
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(registResult.getRetCode())) {
				resultBean.setStatusForResponse("TZ000097");
				resultBean.setStatusDesc("出借失败，可用余额不足！请联系客服.");
				return resultBean;
			} else {
				resultBean.setStatusForResponse("TZ000096");
				resultBean.setStatusDesc("出借失败，原因: "+registResult.getRetMsg());
				return resultBean;
			}
		}
		registResult.convert();
		String message ="";
		//用户测评逻辑开始++++++
		Users user = tenderService.getUsers(userId);
		Integer isAnswer = -1;
		if (user != null) {
			isAnswer = user.getIsEvaluationFlag();
		}
		if (isAnswer == 0) {
			//返回错误码
			resultBean.setStatusForResponse("CP000000");
			resultBean.setStatusDesc("用户未测评");
			return resultBean;
		} else {
			if(isAnswer==1 && null != user.getEvaluationExpiredTime()){
				//测评到期日
				Long lCreate = user.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//返回错误码
					resultBean.setStatusForResponse("CP000001");
					resultBean.setStatusDesc("用户评测已过期");
					return resultBean;
				}
			} else {
				//返回错误码
				resultBean.setStatusForResponse("CP000000");
				resultBean.setStatusDesc("用户未测评");
			}
		}
		//限额判断：从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
		UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
		if(userEvalationResultCustomize != null){
			EvaluationConfig evalConfig = new EvaluationConfig();
			//1.散标／债转出借者测评类型校验
			String debtEvaluationTypeCheck = "0";
			//2.散标／债转单笔出借金额校验
			String deptEvaluationMoneyCheck = "0";
			//3.散标／债转待收本金校验
			String deptCollectionEvaluationCheck = "0";
			//获取开关信息
			List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
			if(evalConfigList != null && evalConfigList.size() > 0){
				evalConfig = evalConfigList.get(0);
				//1.散标／债转出借者测评类型校验
				debtEvaluationTypeCheck = evalConfig.getDebtEvaluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getDebtEvaluationTypeCheck());
				//2.散标／债转单笔出借金额校验
				deptEvaluationMoneyCheck = evalConfig.getDeptEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getDeptEvaluationMoneyCheck());
				//3.散标／债转待收本金校验
				deptCollectionEvaluationCheck = evalConfig.getDeptCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getDeptCollectionEvaluationCheck());
				//7.投标时校验（二期）(预留二期开发)
			}
			//初始化金额返回值
			String revaluation_money,revaluation_money_principal;
			//根据类型从redis或数据库中获取测评类型和上限金额
			String eval_type = userEvalationResultCustomize.getType();
			switch (eval_type){
				case "保守型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
					}
					break;
				case "稳健型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
					}
					break;
				case "成长型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
					}
					break;
				case "进取型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getEnterprisingEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationPrincipalMoney());
					}
					break;
				default:
					revaluation_money = null;
					revaluation_money_principal = null;
			}
			//测评到期日
			Long lCreate = user.getEvaluationExpiredTime().getTime();
			//当前日期
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//返回错误码
				resultBean.setStatusForResponse("CP000001");
				resultBean.setStatusDesc("用户评测已过期");
				return resultBean;
			}
			if(CustomConstants.EVALUATION_CHECK.equals(debtEvaluationTypeCheck)){
				//计划类判断用户类型为稳健型以上才可以出借
				if(!CommonUtils.checkStandardInvestment(eval_type,"BORROW_SB",borrow.getInvestLevel())){
					//返回错误码
					resultBean.setStatusForResponse("CP000002");
					resultBean.setStatusDesc("当前用户测评等级为：“"+ eval_type +"”达到“"+borrow.getInvestLevel()+"”及以上才可以出借此项目");
					return resultBean;
				}
			}
			if(revaluation_money == null){
                _log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
			}else {
				if(CustomConstants.EVALUATION_CHECK.equals(deptEvaluationMoneyCheck)){
					//金额对比判断（校验金额 大于 设置测评金额）
					if (new BigDecimal(accountStr).compareTo(new BigDecimal(revaluation_money)) > 0) {
						//返回错误码
						resultBean.setStatusForResponse("CP000003");
						resultBean.setStatusDesc("单笔测评限额超额！当前用户测评等级为：“"+ eval_type +"”单笔测评限额为：“"+ StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()) +"”");
						return resultBean;
					}
				}
			}
			if(revaluation_money_principal == null){
                _log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
			}else {
				//代收本金限额校验
				if(CustomConstants.EVALUATION_CHECK.equals(deptCollectionEvaluationCheck)){
					//获取冻结金额和代收本金
					List<AccountDetailCustomize> accountInfos = evaluationService.queryAccountEvalDetail(userId);
					if(accountInfos!= null || accountInfos.size() >0){
						AccountDetailCustomize accountDetail =  accountInfos.get(0);
						BigDecimal planFrost = accountDetail.getPlanFrost();// plan_frost 汇添金计划真实冻结金额
						BigDecimal bankFrost = accountDetail.getBankFrost();// bank_frost 银行冻结金额
						BigDecimal bankAwaitCapital = accountDetail.getBankAwaitCapital();// bank_await_capital 银行待收本金
						BigDecimal accountDs = BigDecimal.ZERO;
						//加法运算
						accountDs = accountDs.add(planFrost).add(bankFrost).add(bankAwaitCapital).add(new BigDecimal(accountStr));
						//金额对比判断（校验金额 大于 设置测评金额）（代收本金）
						if (accountDs.compareTo(new BigDecimal(revaluation_money_principal)) > 0) {
							//返回错误码
							resultBean.setStatusForResponse("CP000004");
							resultBean.setStatusDesc("待收本金测评限额超额！当前用户测评等级为：“"+ eval_type +"”待收本金测评限额为：“"+ StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()) +"”");
							return resultBean;
						}
					}
				}
			}
		}else{
            _log.info("=============该用户测评总结数据为空! userId="+userId);
		}
		//出借逻辑开始+++++++++
		try {
			// 进行出借, tendertmp锁住
			JSONObject tenderResult = this.tenderService.userAutoTender(borrow, registResult);
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				_log.info("用户:" + userId + "  出借成功：" + account);
				message = "恭喜您出借成功!";
			}
			// 出借失败 回滚redis
			else {
				_log.info("用户:" + userId + "   出借失败：" + account);
				message = "出借失败";
				// 出借失败,出借撤销
				try {
					boolean canlflag = tenderService.bidCancel(userId, borrowNid, orderId, account);
					boolean updateFlag = tenderService.deleteBorrowTenderTmp(userId, borrowNid, orderId);
				} catch (Exception e) {
					e.printStackTrace();
					resultBean.setStatusForResponse("TZ000099");
					resultBean.setStatusDesc("出借失败,系统错误");
					return resultBean;
				}

				if(tenderResult != null && tenderResult.getString("message") != null){
					message = "出借失败, 原因："+tenderResult.getString("message");
				}
				resultBean.setStatusForResponse("TZ000096");
				resultBean.setStatusDesc(message);
				return resultBean;

			}
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setStatusForResponse("TZ000099");
			resultBean.setStatusDesc("出借失败,系统错误");
			return resultBean;
		}
		_log.info(autoTenderRequestBean.getInstCode()+" userid: "+userId+" 结束出借");
		resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
		resultBean.setStatusDesc(message);
		resultBean.setOrderId(orderId);

		return resultBean;
	}

	/**
	 * 出借失败,删除出借临时表
	 * @param borrowNid
	 * @param userId
	 * @param orderId
	 */
	private void removeTenderTmp(String borrowNid, Integer userId,
			String orderId) {
		// 出借失败,出借撤销
		try {
			boolean updateFlag = tenderService.deleteBorrowTenderTmp(userId, borrowNid, orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
