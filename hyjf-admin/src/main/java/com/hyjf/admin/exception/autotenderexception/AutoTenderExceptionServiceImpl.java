package com.hyjf.admin.exception.autotenderexception;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.bank.service.borrow.send.RedisBorrow;
import com.hyjf.bank.service.hjh.borrow.tender.BankAutoTenderService;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanBorrowTmp;
import com.hyjf.mybatis.model.auto.HjhPlanBorrowTmpExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
/**
 * 汇计划加入明细Service实现类
 *
 * @ClassName AccedeListServiceImpl
 * @author LIBIN
 * @date 2017年8月16日 上午9:48:22
 */
@Service
public class AutoTenderExceptionServiceImpl extends BaseServiceImpl implements AutoTenderExceptionService {

	Logger _log = LoggerFactory.getLogger(AutoTenderExceptionServiceImpl.class);

	@Autowired
	private MqService mqService;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	private BankAutoTenderService autoTenderService;

	@Autowired
	private CreditService creditService;

	/**
	 * 检索加入明细的件数
	 *
	 * @Title countAccedeRecord
	 * @param form
	 * @return
	 */
	@Override
	public int countAccedeRecord(AutoTenderExceptionBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 加入订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 订单状态
		param.put("orderStatus", 80);
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入开始时间(推送开始时间)
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间(推送结束时间)
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}

		return this.adminPlanAccedeListCustomizeMapper.countAccedeExceptionRecord(param);// 原 adminPlanAccedeDetailCustomizeMapper
	}

	/**
	 * 检索加入明细列表
	 *
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	@Override
	public List<AdminPlanAccedeListCustomize> selectAccedeRecordList(AutoTenderExceptionBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 加入订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 订单状态
		param.put("orderStatus", 80);
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入开始时间(推送开始时间)
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间(推送结束时间)
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}
		param.put("limitStart", form.getLimitStart());
		param.put("limitEnd", form.getLimitEnd());
		return this.adminPlanAccedeListCustomizeMapper.selectAccedeExceptionList(param);
	}

	/**
	 * 异常处理
	 * 从batch 拷贝改以下地方
	 * 1. saveCreditTenderLog 去除更新逻辑
	 * 2. hcommonservice 两方法重新引入
	 * @param userId
	 * @param planOrderId
	 * @param debtPlanNid
	 * @return
	 */
	@Override
	public String tenderExceptionAction(String userId, String planOrderId, String debtPlanNid) {
		_log.info("===自动出借异常处理开始。。。 userId:"+userId+",planOrderId:"+planOrderId+",debtPlanNid:"+debtPlanNid);
		String retMessage = "系统异常";
		try {
			int userIdint = Integer.parseInt(userId);
			HjhAccede hjhAccede = this.selectHjhAccede(planOrderId,debtPlanNid,userIdint);
			if(hjhAccede == null){
				return (planOrderId+" 没有加入明细");
			}

			if(hjhAccede.getOrderStatus() == null ||
					hjhAccede.getOrderStatus().intValue() >= 90){
				return (planOrderId+" >90 失败(联系管理员) 手动处理");
			}


			// 原始标，异常先查异常表，访问银行接口，如果订单成功，查看表是否OK, 修复后，更新状态。没修复不更新
			// 债转也是一样。如失败则直接更新
			HjhPlanBorrowTmp hjhPlanBorrowTmp = this.selectBorrowJoinList(planOrderId,debtPlanNid,userIdint);

			if(hjhPlanBorrowTmp == null){
				_log.info(planOrderId+" 没有出借异常明细");
				return (planOrderId+" 没有出借异常明细");
			}
			_log.info(planOrderId+"， "+hjhPlanBorrowTmp.getBorrowNid()+",  异常返回码："+hjhPlanBorrowTmp.getRespCode());

			if(StringUtils.isBlank(hjhPlanBorrowTmp.getOrderId())){
				return (planOrderId+" 没有出借异常订单");
			}

			// CA101121 账户可用余额不足,或者可能导致继续投也会失败的状态
//			if("CA101121".equals(hjhPlanBorrowTmp.getRespCode())){
//				return (planOrderId+" 账户可用余额不足");
//			}

			BankOpenAccount borrowUserAccount = this.getBankOpenAccount(userIdint);
			if (borrowUserAccount == null || StringUtils.isBlank(borrowUserAccount.getAccount())) {
				_log.info("出借人未开户:" + userId);
				return (("出借人未开户?:" + userId));
			}
			// 借款人账户
			String borrowUserAccountId = borrowUserAccount.getAccount();
			// 为了判断
			int orderStatus = hjhAccede.getOrderStatus()%10;
			hjhAccede.setOrderStatus(orderStatus);

			// 原始标出借
			if(hjhPlanBorrowTmp.getBorrowType() == 0){

				Borrow borrow = this.autoTenderService.selectBorrowByNid(hjhPlanBorrowTmp.getBorrowNid());
				if (borrow == null) {
					_log.error("[" + hjhPlanBorrowTmp.getBorrowNid() + "]" + "标的号不存在 ");
					return (planOrderId+" 标的号不存在 "+hjhPlanBorrowTmp.getBorrowNid());
				}

				// 目前处理 510000 银行系统返回异常
				// CA101141	投标记录不存在
				BankCallBean debtQuery = debtStatusQuery(userIdint, borrowUserAccountId,hjhPlanBorrowTmp.getOrderId());
				if(debtQuery == null || StringUtils.isBlank(debtQuery.getRetCode())){
					_log.info(userIdint+"  "+borrowUserAccountId+"  "+hjhPlanBorrowTmp.getOrderId()+" 请求银行查询无响应");
					return (planOrderId+" 请求银行查询无响应");
				}
				String queryRetCode = debtQuery.getRetCode();
				boolean bankQueryisOK = false;
				if (BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)) {
					String state = StringUtils.isNotBlank(debtQuery.getState()) ? debtQuery.getState() : "";
					_log.info(("查询出借请求成功:" + hjhPlanBorrowTmp.getOrderId())+ " 状态 "+state);
					// 1：投标中； 2：计息中；4：本息已返还；9：已撤销；
					if (StringUtils.isNotBlank(state)) {
						if (state.equals("1")) {
							bankQueryisOK = true;
						} else if (state.equals("2")) {

						}
					}
				}

				if(bankQueryisOK){
					_log.info("查询银行记录成功:" + userId);
//					// 撤销
//					boolean result = bidCancel(userIdint, hjhPlanBorrowTmp.getBorrowNid(), hjhPlanBorrowTmp.getOrderId(), hjhPlanBorrowTmp.getAccount().toString(), borrowUserAccountId);
//					if(result){
//						// 更改加入明细状态和出借临时表状态
//						this.updateTender(planOrderId, hjhPlanBorrowTmp);
//					}

					//更新表
					// bean.getOrderId();// 订单id
					debtQuery.setOrderId(hjhPlanBorrowTmp.getOrderId());

					// 如果有tmp表，说明第一接口调用了，调用成功，表更新失败，无如都是需要调用下面的方法
					boolean isOK = autoTenderService.updateBorrow(borrow, hjhAccede, hjhPlanBorrowTmp.getAccount(), debtQuery);
					if(isOK){
						// 更改加入明细状态和出借临时表状态
						this.updateTender(hjhAccede, hjhPlanBorrowTmp,orderStatus);

						// 只有不是接口成功表失败的情况才会退回队列
						// 出借成功后减掉redis 钱
						String queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_INVEST + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();
						RedisBorrow redisBorrow = new RedisBorrow();
						redisBorrow.setBorrowAccountWait(borrow.getBorrowAccountWait().subtract(hjhPlanBorrowTmp.getAccount()));
						redisBorrow.setBorrowNid(borrow.getBorrowNid());

						// 银行成功后，如果标的可投金额非0，推回队列的头部，标的可投金额为0，不再推回队列
						if (redisBorrow.getBorrowAccountWait().compareTo(BigDecimal.ZERO) != 0) {
							String redisStr = JSON.toJSONString(redisBorrow);
							_log.info("退回队列 " + queueName + redisStr);
							RedisUtils.rightpush(queueName,redisStr);
						} else {
							// add by liushouyi nifa2 20181204 start
							// 满标发送满标状态埋点
							// 发送发标成功的消息队列到互金上报数据
							Map<String, String> params = new HashMap<String, String>();
							params.put("borrowNid", borrow.getBorrowNid());
							this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTED_DELAY_KEY, JSONObject.toJSONString(params));
							// add by liushouyi nifa2 20181204 end
						}

					}

					// 投标记录不存在才会继续，不然属于未知情况
				}else if ("CA101141".equals(queryRetCode)){
					// 更改加入明细状态和出借临时表状态
					_log.info(hjhPlanBorrowTmp.getRespCode()+" 查询银行记录成功，但是出借失败:" + userId);
					this.updateTender(hjhAccede, hjhPlanBorrowTmp,orderStatus);

					// 只有不是接口成功表失败的情况才会退回队列
					if(!BankCallConstant.RESPCODE_SUCCESS.equals(hjhPlanBorrowTmp.getRespCode())){
						// 出借成功后减掉redis 钱
						String queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_INVEST + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();
						RedisBorrow redisBorrow = new RedisBorrow();
						redisBorrow.setBorrowAccountWait(borrow.getBorrowAccountWait());
						redisBorrow.setBorrowNid(borrow.getBorrowNid());

						// 银行成功后，如果标的可投金额非0，推回队列的头部，标的可投金额为0，不再推回队列
						if (redisBorrow.getBorrowAccountWait().compareTo(BigDecimal.ZERO) != 0) {
							String redisStr = JSON.toJSONString(redisBorrow);
							_log.info("退回队列 " + queueName + redisStr);
							RedisUtils.rightpush(queueName,redisStr);
						}

						// 删除临时表
						this.autoTenderService.deleteBorrowTmp(borrow.getBorrowNid(), hjhAccede.getAccedeOrderId());
					}else{
						_log.info(planOrderId+ "银行投标记录不存在（" + queryRetCode + "），但接口成功（" + hjhPlanBorrowTmp.getRespCode() + "）表失败。 ");
						return (planOrderId + "银行投标记录不存在，但接口成功表失败，暂时没有处理该异常，需要协调开发处理  "+ queryRetCode);
					}

				}else{

					return (planOrderId+" 暂时没有处理该异常，需要协调开发处理  "+ queryRetCode);
				}


				// 债转标出借
			}else if(hjhPlanBorrowTmp.getBorrowType() == 1){

				HjhDebtCredit credit = this.autoTenderService.selectCreditByNid(hjhPlanBorrowTmp.getBorrowNid());
				if (credit == null) {
					_log.error("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]" + "债转号不存在 "+hjhPlanBorrowTmp.getBorrowNid());
					return (planOrderId+" 债转号不存在 "+hjhPlanBorrowTmp.getBorrowNid());
				}
				// 为了即信同步银行的结果，先调用下“出借人投标申请查询”接口，返回的txAmount不带承接利息
				BankCallBean bean = debtStatusQuery(userIdint, borrowUserAccountId,hjhPlanBorrowTmp.getOrderId());
				// 调用下“出借人购买债权查询”接口，返回的txAmount带承接利息
				bean = creditStatusQuery(userIdint, borrowUserAccountId, hjhPlanBorrowTmp.getOrderId());
				String queryRetCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
				if(BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)){
					_log.info("查询银行记录成功:" + userId);

					//更新表
					HjhPlan hjhPlan =  autoTenderService.selectHjhPlanByPlanNid(hjhAccede.getPlanNid());
					BankOpenAccount sellerBankOpenAccount = this.autoTenderService.getBankOpenAccount(credit.getUserId());
					if (sellerBankOpenAccount == null) {
						_log.info("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]" + "转出用户没开户 "+credit.getUserId());
						return (planOrderId+" 债转号不存在 "+hjhPlanBorrowTmp.getBorrowNid());
					}
					String sellerUsrcustid = sellerBankOpenAccount.getAccount();//出让用户的江西银行电子账号
					// 计算实际金额 保存creditTenderLog表

					// 生成承接日志
					// 债权承接订单日期
					String orderDate = GetDate.getServerDateTime(1, new Date());
					//TODO:
					Map<String, Object> resultMap = this.autoTenderService.saveCreditTenderLogNoSave(credit, hjhAccede, hjhPlanBorrowTmp.getOrderId(), orderDate, hjhPlanBorrowTmp.getAccount(), hjhPlanBorrowTmp.getIsLast());
					if (Validator.isNull(resultMap)) {
						_log.info("保存creditTenderLog表失败，智投订单号：" + hjhAccede.getAccedeOrderId());
						return ("保存creditTenderLog表失败，智投订单号：" + hjhAccede.getAccedeOrderId());
					}
					// add 汇计划三期 汇计划自动出借(收债转服务费) liubin 20180515 start
					_log.info("[" + hjhAccede.getAccedeOrderId() + "]" + "承接用计算完成"
							+ "\n,分期数据结果:" + resultMap.get("assignResult")
							+ "\n,承接总额:" + resultMap.get("assignAccount")
							+ "\n,承接本金:" + resultMap.get("assignCapital")
							+ "\n,承接利息:" + resultMap.get("assignInterest")
							+ "\n,承接支付金额:" + resultMap.get("assignPay")
							+ "\n,承接垫付利息:" + resultMap.get("assignAdvanceMentInterest")
							+ "\n,承接延期利息:" + resultMap.get("assignRepayDelayInterest")
							+ "\n,承接逾期利息:" + resultMap.get("assignRepayLateInterest")
							+ "\n,分期本金:" + resultMap.get("assignPeriodCapital")
							+ "\n,分期利息:" + resultMap.get("assignPeriodInterest")
							+ "\n,分期垫付利息:" + resultMap.get("assignPeriodAdvanceMentInterest")
							+ "\n,分期承接延期利息:" + resultMap.get("assignPeriodRepayDelayInterest")
							+ "\n,分期承接延期利息:" + resultMap.get("assignPeriodRepayLateInterest")
							+ "\n,承接服务率:" + resultMap.get("serviceApr")
							+ "\n,承接服务费:" + resultMap.get("serviceFee"));
					// add 汇计划三期 汇计划自动出借(收债转服务费) liubin 20180515 end

					// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 start
					if(!this.autoTenderService.checkAutoPayment(credit.getCreditNid())){
//						serviceFee = BigDecimal.ZERO;//承接服务费
						resultMap.put("serviceFee",BigDecimal.ZERO);
						_log.info("[" + hjhAccede.getAccedeOrderId() + "]" + "债权转让人未做缴费授权,该笔债权的承接服务费置为" +resultMap.get("serviceFee"));
					}
					// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 end

					bean.setOrderId(hjhPlanBorrowTmp.getOrderId());
					boolean isOK = autoTenderService.updateCredit(credit, hjhAccede, hjhPlan, bean, borrowUserAccountId, sellerUsrcustid, resultMap);
					if(isOK){
						// 更改加入明细状态和出借临时表状态
						this.updateTender(hjhAccede, hjhPlanBorrowTmp,orderStatus);

						// add 合规数据上报 埋点 liubin 20181122 start
						// 推送数据到MQ 承接（每笔）
						Map<String, String> params = new HashMap<String, String>();
						params.put("assignOrderId", bean.getOrderId());
						params.put("flag", "2"); //1（散）2（智投）
						params.put("status", "1"); //1承接（每笔）
						this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_SINGLE_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
						// add 合规数据上报 埋点 liubin 20181122 end

						// 更新后重新查
						credit = this.autoTenderService.selectCreditByNid(hjhPlanBorrowTmp.getBorrowNid());
						if (credit == null) {
							_log.error("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]" + "债转号不存在 "+hjhPlanBorrowTmp.getBorrowNid());
							return (planOrderId+" 债转号不存在 结束债权 "+hjhPlanBorrowTmp.getBorrowNid());
						}

						// 只有不是接口成功表失败的情况才会退回队列
						// 出借成功后减掉redis 钱
						String queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_CREDIT + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();
						RedisBorrow redisBorrow = new RedisBorrow();

						// liquidation_fair_value - credit_price
						BigDecimal await = credit.getLiquidationFairValue().subtract(credit.getCreditPrice());
						redisBorrow.setBorrowAccountWait(await);
						redisBorrow.setBorrowNid(credit.getCreditNid());
						_log.info(credit.getCreditNid()+" 更新表退回队列  "+await);

						// 银行成功后，如果标的可投金额非0，推回队列的头部，标的可投金额为0，不再推回队列
						if (await.compareTo(BigDecimal.ZERO) >= 0) {
							String redisStr = JSON.toJSONString(redisBorrow);
							_log.info("退回队列 " + queueName + redisStr);
							RedisUtils.rightpush(queueName,redisStr);
						}


						/** 4.7. 完全承接时，结束债券  */
						if (credit.getCreditAccountWait().compareTo(BigDecimal.ZERO) == 0) {
							//获取出让人投标成功的授权号
							String sellerAuthCode = this.autoTenderService.getSellerAuthCode(credit.getSellOrderId(), credit.getSourceType());
							if (sellerAuthCode == null) {
								_log.info("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]未取得出让人" + credit.getUserId() + "的债权类型" +
										credit.getSourceType() + "(1原始0原始)的授权码，结束债权失败。");
								return (planOrderId+"(1原始0原始)的授权码，结束债权失败。");
							}

							// add 合规数据上报 埋点 liubin 20181122 start
							// 推送数据到MQ 承接（完全）
							params = new HashMap<String, String>();
							params.put("creditNid", credit.getCreditNid());
							params.put("flag", "2"); //1（散）2（智投）
							params.put("status", "2"); //2承接（完全）
							this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
							// add 合规数据上报 埋点 liubin 20181122 end

							//调用银行结束债权接口
							boolean ret = this.creditService.requestDebtEnd(credit, sellerUsrcustid, sellerAuthCode);
							if (!ret) {
								_log.info("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]被承接标的" + hjhPlanBorrowTmp.getBorrowNid() +"被完全承接，银行结束债权失败。");
							}
							_log.info("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]被承接标的" + hjhPlanBorrowTmp.getBorrowNid() +"被完全承接，银行结束债权成功。");
							//银行结束债权后，更新债权表为完全承接
							ret = this.autoTenderService.updateCreditForEnd(credit);
							if (!ret) {
								_log.info("[" + hjhPlanBorrowTmp.getAccedeOrderId() + "]银行结束债权后，更新债权表为完全承接失败。");
							}
						}


					}


				} else if ("CA110112".equals(queryRetCode)) {
					// 更改加入明细状态和出借临时表状态
					this.updateTender(hjhAccede, hjhPlanBorrowTmp, orderStatus);

					// 只有不是接口成功表失败的情况才会退回队列
					if (!BankCallConstant.RESPCODE_SUCCESS.equals(hjhPlanBorrowTmp.getRespCode())) {
						// 出借成功后减掉redis 钱
						String queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_CREDIT + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();
						RedisBorrow redisBorrow = new RedisBorrow();

						// liquidation_fair_value - credit_price
						BigDecimal await = credit.getLiquidationFairValue().subtract(credit.getCreditPrice());
						redisBorrow.setBorrowAccountWait(await);
						redisBorrow.setBorrowNid(credit.getCreditNid());
						_log.info(credit.getCreditNid() + " 更新表退回队列  " + await);

						// 银行成功后，如果标的可投金额非0，推回队列的头部，标的可投金额为0，不再推回队列
						if (await.compareTo(BigDecimal.ZERO) >= 0) {
							String redisStr = JSON.toJSONString(redisBorrow);
							_log.info("退回队列 " + queueName + redisStr);
							RedisUtils.rightpush(queueName, redisStr);
						}

						// 数据恢复，删除临时表
						this.autoTenderService.deleteBorrowTmp(credit.getCreditNid(), hjhAccede.getAccedeOrderId());
					} else {
						_log.info(planOrderId + "银行投标记录不存在（" + queryRetCode + "），但接口成功（" + hjhPlanBorrowTmp.getRespCode() + "）表失败。 ");
						return (planOrderId + "银行投标记录不存在，但接口成功表失败，暂时没有处理该异常，需要协调开发处理  " + queryRetCode);
					}
				}else{

					return (planOrderId+" 暂时没有处理该异常，需要协调开发处理  "+ queryRetCode);
				}


			}


			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMessage;
	}


	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public UsersInfo getUsersInfoByUserId(Integer userId) {
		if (userId != null) {
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
			if (usersInfoList != null && usersInfoList.size() > 0) {
				return usersInfoList.get(0);
			}
		}
		return null;
	}

	/**
	 * 查询计划加入明细临时表
	 * @param accedeOrderId
	 * @param planNid
	 * @param userId
	 * @return
	 */
	private HjhPlanBorrowTmp selectBorrowJoinList(String accedeOrderId, String planNid, Integer userId) {

		HjhPlanBorrowTmpExample example = new HjhPlanBorrowTmpExample();
		HjhPlanBorrowTmpExample.Criteria crt = example.createCriteria();
		crt.andAccedeOrderIdEqualTo(accedeOrderId);
		crt.andPlanNidEqualTo(planNid);
		crt.andUserIdEqualTo(userId);

		List<HjhPlanBorrowTmp> list = this.hjhPlanBorrowTmpMapper.selectByExample(example);
		if(list != null && list.size() >= 1){
			if(list.size() > 1){
				_log.info("加入明细临时表 "+accedeOrderId+" 记录大于1！");
			}
			return list.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 查询计划加入明细
	 * @param accedeOrderId
	 * @param planNid
	 * @param userId
	 * @return
	 */
	private HjhAccede selectHjhAccede(String accedeOrderId, String planNid, Integer userId) {

		HjhAccedeExample example = new HjhAccedeExample();
		HjhAccedeExample.Criteria crt = example.createCriteria();
		crt.andAccedeOrderIdEqualTo(accedeOrderId);
		crt.andPlanNidEqualTo(planNid);
		crt.andUserIdEqualTo(userId);

		List<HjhAccede> list = this.hjhAccedeMapper.selectByExample(example);
		if(list != null && list.size() >= 1){
			if(list.size() > 1){
				_log.info("加入明细表 "+accedeOrderId+" 记录大于1！");
			}
			return list.get(0);
		}else {
			return null;
		}
	}


	/**
	 * 更改状态
	 */
	private boolean updateTender(HjhAccede hjhAccede,HjhPlanBorrowTmp hjhPlanBorrowTmp,int status){

		int nowTime = GetDate.getNowTime10();

		HjhAccede newHjhAccede = new HjhAccede();
		newHjhAccede.setOrderStatus(status); // 加入计划成功
		newHjhAccede.setUpdateTime(nowTime);
		newHjhAccede.setUpdateUser(1);
		newHjhAccede.setId(hjhAccede.getId());
		boolean accedeOk = this.hjhAccedeMapper.updateByPrimaryKeySelective(newHjhAccede)>0?true:false;

		return accedeOk;
	}

	/**
	 *
	 * @param userId
	 * @param accedeOrderId
	 * @param hjhPlanBorrowTmp
	 * @param borrowUserAccountId
	 * @return
	 */
	private boolean debtStatusCheck(int userId, String accedeOrderId,HjhPlanBorrowTmp hjhPlanBorrowTmp, String borrowUserAccountId) {
		// 查询相应的债权状态
		BankCallBean debtQuery = this.debtStatusQuery(userId, borrowUserAccountId, hjhPlanBorrowTmp.getOrderId());

		// 请求接口，是否出借成功，成功则撤销
		String queryRetCode = StringUtils.isNotBlank(debtQuery.getRetCode()) ? debtQuery.getRetCode() : "";
		if (BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)) {
			String state = StringUtils.isNotBlank(debtQuery.getState()) ? debtQuery.getState() : "";
			_log.info(("查询出借请求成功:" + accedeOrderId)+ " 状态 "+state);
			// 1：投标中； 2：计息中；4：本息已返还；9：已撤销；
			if (StringUtils.isNotBlank(state)) {
				if (state.equals("1")) {
					return true;
				} else if (state.equals("2")) {

				}
			}
		}

		return false;
	}

	/**
	 * 查询相应的债权的状态
	 *
	 * @param userId
	 * @param accountId
	 * @param orderId
	 * @return
	 */
	private BankCallBean debtStatusQuery(int userId, String accountId, String orderId) {
		// 获取共同参数
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型
		bean.setLogUserId(String.valueOf(userId));
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setOrgOrderId(orderId);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));

		BankCallBean statusResult = BankCallUtils.callApiBg(bean);

		return statusResult;
	}

	/**
	 * 查询相应的债权的状态
	 *
	 * @param userId
	 * @param accountId
	 * @param orderId
	 * @return
	 */
	private BankCallBean creditStatusQuery(int userId, String accountId, String orderId) {
		// 获取共同参数
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_CREDIT_INVEST_QUERY);// 消息类型
		bean.setLogUserId(String.valueOf(userId));
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setOrgOrderId(orderId);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));

		BankCallBean statusResult = BankCallUtils.callApiBg(bean);

		return statusResult;
	}

	private boolean bidCancel(int investUserId, String productId, String orgOrderId, String txAmount,String tenderAccountId) {
		// 出借人的账户信息
		_log.info("bidCancel","出借失败,开始撤销订单号为:" + orgOrderId + "的出借!"+investUserId);
		// 调用交易查询接口(出借撤销)
		BankCallBean queryTransStatBean = bidCancelApi(investUserId, tenderAccountId, productId, orgOrderId, txAmount);
		if (queryTransStatBean == null) {
			_log.info("调用投标申请撤销失败。" + ",[出借订单号：" + orgOrderId + "]");
		} else {
			String queryRespCode = queryTransStatBean.getRetCode();
			_log.info("bidCancel","出借失败交易接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (BankCallConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				return true;
			}else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) || queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2)) {
				_log.info("===============冻结记录不存在,不予处理========");
				return false;
			} else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)) {
				_log.info("===============只能撤销投标状态为投标中的标的============");
				return false;
			}
		}

		return false;
	}

	/**
	 * 投标失败后,调用出借撤销接口
	 *
	 * @param investUserId
	 * @param investUserAccountId
	 * @param orgOrderId
	 * @param txAmount
	 * @return
	 */
	private BankCallBean bidCancelApi(Integer investUserId, String investUserAccountId, String productId, String orgOrderId, String txAmount) {

		// 调用汇付接口(交易状态查询)
		BankCallBean bean = new BankCallBean();
		String orderId = GetOrderIdUtils.getOrderId2(investUserId);
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		bean.setVersion(BankCallConstant.VERSION_10); // 版本号(必须)
		bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL); // 交易代码
		bean.setInstCode(instCode);
		bean.setBankCode(bankCode);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6)); // 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(investUserAccountId);// 电子账号
		bean.setOrderId(orderId); // 订单号(必须)
		bean.setTxAmount(CustomUtil.formatAmount(txAmount));// 交易金额
		bean.setProductId(productId);// 标的号
		bean.setOrgOrderId(orgOrderId);// 原标的订单号
		bean.setLogOrderId(orderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
		bean.setLogUserId(String.valueOf(investUserId));// 用户Id
//		bean.setLogUserName(investUser == null ? "" : investUser.getUsername()); // 用户名
		bean.setLogRemark("投标申请撤销"); // 备注
		// 调用汇付接口
		BankCallBean chinapnrBean = BankCallUtils.callApiBg(bean);
		if (chinapnrBean == null) {

			return null;
		}
		return chinapnrBean;
	}
}
