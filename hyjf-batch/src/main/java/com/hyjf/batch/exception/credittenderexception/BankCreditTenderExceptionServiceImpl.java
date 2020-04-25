package com.hyjf.batch.exception.credittenderexception;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.mq.MqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.calculate.AccountManagementFeeUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankCreditEnd;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.mybatis.model.auto.CreditTenderLogExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankCreditTenderExceptionServiceImpl extends BaseServiceImpl implements BankCreditTenderExceptionService {
	

	Logger _log = LoggerFactory.getLogger(BankCreditTenderExceptionServiceImpl.class);

	@Autowired
	private MqService mqService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 获取债转出借异常记录数据
	 * 
	 * @return
	 */
	@Override
	public List<CreditTenderLog> selectCreditTenderLogs() {
		CreditTenderLogExample example = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(0);
		// 添加时间 <当前时间-5分钟
		cra.andAddTimeLessThan(String.valueOf(GetDate.getNowTime10()-300));
		cra.andAddTimeGreaterThanOrEqualTo(String.valueOf(GetDate.getNowTime10()-172800));//两天之前
		return creditTenderLogMapper.selectByExample(example);
	}

	/**
	 * 根据债转承接订单号查询承接表
	 * 
	 * @param assignNid
	 * @return
	 */
	@Override
	public List<CreditTender> selectCreditTender(String assignNid) {
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andAssignNidEqualTo(assignNid);
		return this.creditTenderMapper.selectByExample(example);
	}

	/**
	 * 调用江西银行购买债券查询接口
	 * 
	 * @param assignOrderId
	 * @param userId
	 * @return
	 */
	@Override
	public BankCallBean creditInvestQuery(String assignOrderId, Integer userId) {
		// 承接人用户Id
		BankOpenAccount tenderOpenAccount = this.getBankOpenAccount(userId);
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_INVEST_QUERY);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
		bean.setAccountId(tenderOpenAccount.getAccount());// 存管平台分配的账号
		bean.setOrgOrderId(assignOrderId);// 原购买债权订单号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogUserId(String.valueOf(userId));
		return BankCallUtils.callApiBg(bean);
	}

	/**
	 * 更新相应的债转出借log表
	 * 
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean updateCreditTenderLog(String logOrderId, Integer userId) {
		CreditTenderLogExample example = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria cra = example.createCriteria();
		cra.andAssignNidEqualTo(logOrderId);
		cra.andUserIdEqualTo(userId);
		CreditTenderLog record = new CreditTenderLog();
		record.setAssignNid(logOrderId);
		record.setUserId(userId);
		record.setAddTime(String.valueOf(GetDate.getNowTime10()));
		return this.creditTenderLogMapper.updateByExampleSelective(record, example) > 0 ? true : false;
	}

	/**
	 * 同步回调收到后,根据logOrderId检索出借记录表
	 * 
	 * @param logOrderId
	 * @return
	 */
	@Override
	public CreditTenderLog selectCreditTenderLogByOrderId(String logOrderId) {
		CreditTenderLogExample example = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria cra = example.createCriteria();
		cra.andLogOrderIdEqualTo(logOrderId);
		List<CreditTenderLog> list = this.creditTenderLogMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 债转汇付交易成功后回调处理 1.插入credit_tender 2.处理承接人account表和account_list表
	 * 3.处理出让人account表和account_list表 4.添加网站收支明细 5.更新borrow_credit
	 * 6.更新Borrow_recover 7.生成还款信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean updateTenderCreditInfo(String assignOrderId, Integer userId, String authCode) throws Exception {
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 检测响应状态
		// 获取CreditTenderLog信息
		CreditTenderLogExample tenderLogExample = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria tenderLogCra = tenderLogExample.createCriteria();
		tenderLogCra.andAssignNidEqualTo(assignOrderId).andUserIdEqualTo(userId);
		List<CreditTenderLog> creditTenderLogs = this.creditTenderLogMapper.selectByExample(tenderLogExample);
		if (creditTenderLogs != null && creditTenderLogs.size() == 1) {
			boolean tenderLogFlag = this.creditTenderLogMapper.deleteByExample(tenderLogExample) > 0 ? true : false;
			if (!tenderLogFlag) {
				throw new Exception("删除相应的承接log表失败，承接订单号：" + assignOrderId + ",用户userId:" + userId);
			}
			CreditTenderLog creditTenderLog = creditTenderLogs.get(0);
			// 债权结束标志位
			Integer debtEndFlag = 0;
			// 出让人userId
			int sellerUserId = creditTenderLog.getCreditUserId();
			// 原始出借订单号
			String tenderOrderId = creditTenderLog.getCreditTenderNid();
			// 项目编号
			String borrowNid = creditTenderLog.getBidNid();
			// 债转编号
			String creditNid = creditTenderLog.getCreditNid();
			// 取得债权出让人的用户在汇付天下的客户号
			BankOpenAccount sellerBankAccount = this.getBankOpenAccount(sellerUserId);
			// 出让人账户信息
			Account sellerAccount = this.getAccount(sellerUserId);
			// 取得承接债转的用户在汇付天下的客户号
			BankOpenAccount assignBankAccount = this.getBankOpenAccount(userId);
			// 承接人账户信息
			Account assignAccount = this.getAccount(userId);
			// 项目详情
			BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
			// 还款方式
			String borrowStyle = borrow.getBorrowStyle();
			// 项目总期数
			Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
			// 管理费率
			BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
			// 差异费率
			BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
			// 初审时间
			int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
			// 是否月标(true:月标, false:天标)
			boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
					|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
			// 管理费
			BigDecimal perManageSum = BigDecimal.ZERO;
			// 获取BorrowCredit信息
			BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
			BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
			borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid)).andCreditUserIdEqualTo(sellerUserId).andTenderNidEqualTo(tenderOrderId);
			List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
			// 5.更新borrow_credit
			if (borrowCreditList != null && borrowCreditList.size() == 1) {
				// 获取BorrowRecover信息
				BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
				BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
				borrowRecoverCra.andBorrowNidEqualTo(creditTenderLog.getBidNid()).andNidEqualTo(creditTenderLog.getCreditTenderNid());
				List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);

				BorrowCredit borrowCredit = borrowCreditList.get(0);
				borrowCredit.setCreditIncome(borrowCredit.getCreditIncome().add(creditTenderLog.getAssignPay()));// 总收入,本金+垫付利息
				borrowCredit.setCreditCapitalAssigned(borrowCredit.getCreditCapitalAssigned().add(creditTenderLog.getAssignCapital()));// 已认购本金
				borrowCredit.setCreditInterestAdvanceAssigned(borrowCredit.getCreditInterestAdvanceAssigned().add(creditTenderLog.getAssignInterestAdvance()));// 已垫付利息
				borrowCredit.setCreditInterestAssigned(borrowCredit.getCreditInterestAssigned().add(creditTenderLog.getAssignInterest()));// 已承接利息
				borrowCredit.setCreditFee(borrowCredit.getCreditFee().add(creditTenderLog.getCreditFee()));// 服务费
				borrowCredit.setAssignTime(GetDate.getNowTime10());// 认购时间
				borrowCredit.setAssignNum(borrowCredit.getAssignNum() + 1);// 出借次数
				// 完全承接的情况
				if (borrowCredit.getCreditCapitalAssigned().compareTo(borrowCredit.getCreditCapital()) == 0) {
					// add 合规数据上报 埋点 liubin 20181122 start
					// 推送数据到MQ 承接（完全）散
					JSONObject params = new JSONObject();
					params.put("creditNid", borrowCredit.getCreditNid()+"");
					params.put("flag", "1"); //1（散）2（智投）
					params.put("status", "2"); //2承接（完全）
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
					// add 合规数据上报 埋点 liubin 20181122 end

					if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
						BorrowRecover borrowRecover = borrowRecoverList.get(0);
						// 调用银行结束债权接口
						try {
							boolean isDebtEndFlag = this.requestDebtEnd(borrowRecover, sellerBankAccount.getAccount());
							if (isDebtEndFlag) {
								// 债权结束成功
								debtEndFlag = 1;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到相应的borrowRecover数据!" + "，用户userId：" + sellerUserId + "，出借订单号：" + tenderOrderId);
					}
					// 发送承接完成短信
					this.sendCreditFullMessage(borrowCredit);
					borrowCredit.setCreditStatus(2);
				}
				boolean borrowCreditFlag = borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
				if (!borrowCreditFlag) {
					throw new Exception("更新相应的borrowCredit表失败，承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 1.插入credit_tender
				CreditTender creditTender = new CreditTender();
				creditTender.setAssignCreateDate(creditTenderLog.getAssignCreateDate());// 认购日期
				creditTender.setAssignPay(creditTenderLog.getAssignPay());// 支付金额
				creditTender.setCreditFee(creditTenderLog.getCreditFee());// 服务费
				creditTender.setAddTime(String.valueOf(nowTime));// 添加时间
				creditTender.setAssignCapital(creditTenderLog.getAssignCapital());// 出借本金
				creditTender.setUserId(userId);// 用户名称
				creditTender.setCreditUserId(sellerUserId);// 出让人id
				creditTender.setStatus(0);// 状态
				creditTender.setBidNid(creditTenderLog.getBidNid());// 原标标号
				creditTender.setCreditNid(creditTenderLog.getCreditNid());// 债转标号
				creditTender.setCreditTenderNid(creditTenderLog.getCreditTenderNid());// 债转投标单号
				creditTender.setAssignNid(creditTenderLog.getAssignNid());// 认购单号
				creditTender.setAssignAccount(creditTenderLog.getAssignAccount());// 回收总额
				creditTender.setAssignInterest(creditTenderLog.getAssignInterest());// 债转利息
				creditTender.setAssignInterestAdvance(creditTenderLog.getAssignInterestAdvance());// 垫付利息
				creditTender.setAssignPrice(creditTenderLog.getAssignPrice());// 购买价格
				creditTender.setAssignRepayAccount(creditTenderLog.getAssignRepayAccount());// 已还总额
				creditTender.setAssignRepayCapital(creditTenderLog.getAssignRepayCapital());// 已还本金
				creditTender.setAssignRepayInterest(creditTenderLog.getAssignRepayInterest());// 已还利息
				creditTender.setAssignRepayEndTime(creditTenderLog.getAssignRepayEndTime());// 最后还款日
				creditTender.setAssignRepayLastTime(creditTenderLog.getAssignRepayLastTime());// 上次还款时间
				creditTender.setAssignRepayNextTime(creditTenderLog.getAssignRepayNextTime());// 下次还款时间
				creditTender.setAssignRepayYesTime(creditTenderLog.getAssignRepayYesTime());// 最终实际还款时间
				creditTender.setAssignRepayPeriod(creditTenderLog.getAssignRepayPeriod());// 还款期数
				creditTender.setAddip(creditTenderLog.getAddip());// ip
				creditTender.setClient(0);// 客户端
				creditTender.setCreateRepay(0);// 已增加还款信息
				creditTender.setAuthCode(authCode);// 银行存管新增授权码
				creditTender.setRecoverPeriod(borrowCredit.getRecoverPeriod());// 已还款期数
				creditTender.setWeb(0);// 服务费收支

				// add by hesy  添加承接人承接时推荐人信息-- 开始
				UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(userId);
				SpreadsUsers spreadsUsers = this.getSpreadsUsersByUserId(userId);
				if (spreadsUsers != null) {
					int refUserId = spreadsUsers.getSpreadsUserid();
					// 查找用户推荐人详情信息
					UserInfoCustomize userInfoCustomizeRef = userInfoCustomizeMapper.queryUserInfoByUserId(refUserId);
					if (Validator.isNotNull(userInfoCustomizeRef)) {
						creditTender.setInviteUserName(userInfoCustomizeRef.getUserName());
						creditTender.setInviteUserAttribute(userInfoCustomizeRef.getAttribute());
						creditTender.setInviteUserRegionname(userInfoCustomizeRef.getRegionName());
						creditTender.setInviteUserBranchname(userInfoCustomizeRef.getBranchName());
						creditTender.setInviteUserDepartmentname(userInfoCustomizeRef.getDepartmentName());
					}

				}else if(userInfoCustomize.getAttribute() == 2 || userInfoCustomize.getAttribute() ==3 ){
					creditTender.setInviteUserName(userInfoCustomize.getUserName());
					creditTender.setInviteUserAttribute(userInfoCustomize.getAttribute());
					creditTender.setInviteUserRegionname(userInfoCustomize.getRegionName());
					creditTender.setInviteUserBranchname(userInfoCustomize.getBranchName());
					creditTender.setInviteUserDepartmentname(userInfoCustomize.getDepartmentName());
				}

				// add by hesy  添加出让人承接时推荐人信息-- 开始
				UserInfoCustomize userInfoCustomizeSeller = userInfoCustomizeMapper.queryUserInfoByUserId(sellerUserId);
				SpreadsUsers spreadsUsersSeller = this.getSpreadsUsersByUserId(sellerUserId);
				if (spreadsUsersSeller != null) {
					int refUserId = spreadsUsersSeller.getSpreadsUserid();
					// 查找用户推荐人详情信息
					UserInfoCustomize userInfoCustomizeRef = userInfoCustomizeMapper.queryUserInfoByUserId(refUserId);
					if (Validator.isNotNull(userInfoCustomizeRef)) {
						creditTender.setInviteUserCreditName(userInfoCustomizeRef.getUserName());
						creditTender.setInviteUserCreditAttribute(userInfoCustomizeRef.getAttribute());
						creditTender.setInviteUserCreditRegionname(userInfoCustomizeRef.getRegionName());
						creditTender.setInviteUserCreditBranchname(userInfoCustomizeRef.getBranchName());
						creditTender.setInviteUserCreditDepartmentname(userInfoCustomizeRef.getDepartmentName());
					}

				}else if(userInfoCustomizeSeller.getAttribute() == 2 || userInfoCustomizeSeller.getAttribute() ==3 ){
					creditTender.setInviteUserCreditName(userInfoCustomizeSeller.getUserName());
					creditTender.setInviteUserCreditAttribute(userInfoCustomizeSeller.getAttribute());
					creditTender.setInviteUserCreditRegionname(userInfoCustomizeSeller.getRegionName());
					creditTender.setInviteUserCreditBranchname(userInfoCustomizeSeller.getBranchName());
					creditTender.setInviteUserCreditDepartmentname(userInfoCustomizeSeller.getDepartmentName());
				}
				
				// creditTender插入数据库
				boolean tenderLog = this.creditTenderMapper.insertSelective(creditTender) > 0 ? true : false;
				if (!tenderLog) {
					throw new Exception("插入credittender表失败，承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 2.处理承接人account表和account_list表
				// 承接人账户信息
				Account assignAccountNew = new Account();
				assignAccountNew.setUserId(userId);
				assignAccountNew.setBankBalance(creditTender.getAssignPay());
				assignAccountNew.setBankTotal(creditTender.getAssignCapital().add(creditTender.getAssignInterest()).subtract(creditTender.getAssignPay()));
				assignAccountNew.setBankAwait(creditTender.getAssignAccount());// 银行待收+承接金额
				assignAccountNew.setBankAwaitCapital(creditTender.getAssignCapital());// 银行待收本金+承接本金
				assignAccountNew.setBankAwaitInterest(creditTender.getAssignInterest());// 银行待收利息+承接利息
				assignAccountNew.setBankInvestSum(creditTender.getAssignCapital());// 累计出借+承接本金
				// 更新账户信息
				boolean isAccountCrediterFlag = this.adminAccountCustomizeMapper.updateCreditAssignSuccess(assignAccountNew) > 0 ? true : false;
				if (!isAccountCrediterFlag) {
					throw new Exception("承接人承接债转后,更新承接人账户账户信息失败!承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 重新获取出让人用户账户信息
				assignAccount = this.getAccount(userId);
				AccountList assignAccountList = new AccountList();
				assignAccountList.setNid(creditTender.getAssignNid());
				assignAccountList.setUserId(userId);
				assignAccountList.setAmount(creditTender.getAssignPay());
				assignAccountList.setType(2);
				assignAccountList.setTrade("creditassign");
				assignAccountList.setTradeCode("balance");
				assignAccountList.setTotal(assignAccount.getTotal());
				assignAccountList.setBalance(assignAccount.getBalance());
				assignAccountList.setBankBalance(assignAccount.getBankBalance());
				assignAccountList.setBankAwait(assignAccount.getBankAwait());
				assignAccountList.setBankAwaitCapital(assignAccount.getBankAwaitCapital());
				assignAccountList.setBankAwaitInterest(assignAccount.getBankAwaitInterest());
				assignAccountList.setBankInvestSum(assignAccount.getBankInvestSum());
				assignAccountList.setBankInterestSum(assignAccount.getBankInterestSum());
				assignAccountList.setBankFrost(assignAccount.getBankFrost());
				assignAccountList.setBankInterestSum(assignAccount.getBankInterestSum());
				assignAccountList.setPlanBalance(assignAccount.getPlanBalance());//汇计划账户可用余额
				assignAccountList.setPlanFrost(assignAccount.getPlanFrost());;
				assignAccountList.setBankTotal(assignAccount.getBankTotal());
				assignAccountList.setSeqNo(String.valueOf(creditTenderLog.getSeqNo()));
				assignAccountList.setTxDate(creditTenderLog.getTxDate());
				assignAccountList.setTxTime(creditTenderLog.getTxTime());
				assignAccountList.setBankSeqNo(String.valueOf(creditTenderLog.getTxDate()) + String.valueOf(creditTenderLog.getTxTime()) + String.valueOf(creditTenderLog.getSeqNo()));
				assignAccountList.setAccountId(assignBankAccount.getAccount());// 承接人电子账户号
				assignAccountList.setFrost(assignAccount.getFrost());
				assignAccountList.setAwait(assignAccount.getAwait());
				assignAccountList.setRepay(assignAccount.getRepay());
				assignAccountList.setRemark("购买债权");
				assignAccountList.setCreateTime(nowTime);
				assignAccountList.setBaseUpdate(nowTime);
				assignAccountList.setOperator(String.valueOf(userId));
				assignAccountList.setIp(creditTender.getAddip());
				assignAccountList.setIsUpdate(0);
				assignAccountList.setBaseUpdate(0);
				assignAccountList.setInterest(null);
				assignAccountList.setWeb(0);
				assignAccountList.setIsBank(1);
				assignAccountList.setCheckStatus(0);
				// 插入交易明细
				boolean assignAccountListFlag = this.accountListMapper.insertSelective(assignAccountList) > 0 ? true : false;
				if (!assignAccountListFlag) {
					throw new Exception("承接债转后,插入承接人交易明细accountList失败!承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 3.处理出让人account表和account_list表
				Account sellerAccountNew = new Account();
				sellerAccountNew.setUserId(sellerUserId);
				sellerAccountNew.setBankBalance(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));// 银行可用余额
				sellerAccountNew.setBankTotal(creditTender.getAssignPay().subtract(creditTender.getCreditFee()).subtract(creditTender.getAssignAccount()));// 银行总资产
				sellerAccountNew.setBankAwait(creditTender.getAssignAccount());// 出让人待收金额
				sellerAccountNew.setBankAwaitCapital(creditTender.getAssignCapital());// 出让人待收本金
				sellerAccountNew.setBankAwaitInterest(creditTender.getAssignInterest());// 出让人待收利息
				sellerAccountNew.setBankInterestSum(creditTender.getAssignInterestAdvance());// 出让人累计收益
				sellerAccountNew.setBankBalanceCash(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));
				// 更新账户信息
				boolean isAccountFlag = this.adminAccountCustomizeMapper.updateCreditSellerSuccess(sellerAccountNew) > 0 ? true : false;
				if (!isAccountFlag) {
					throw new Exception("出借人承接债转后,更新出让人账户账户信息失败!承接订单号：" + assignOrderId);
				}
				// 重新获取用户账户信息
				sellerAccount = this.getAccount(sellerUserId);
				AccountList sellerAccountList = new AccountList();
				sellerAccountList.setNid(creditTender.getAssignNid());
				sellerAccountList.setUserId(creditTender.getCreditUserId());
				sellerAccountList.setAmount(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));
				sellerAccountList.setType(1);
				sellerAccountList.setTrade("creditsell");
				sellerAccountList.setTradeCode("balance");
				sellerAccountList.setTotal(sellerAccount.getTotal());
				sellerAccountList.setBalance(sellerAccount.getBalance());
				sellerAccountList.setBankBalance(sellerAccount.getBankBalance());
				sellerAccountList.setBankAwait(sellerAccount.getBankAwait());
				sellerAccountList.setBankAwaitCapital(sellerAccount.getBankAwaitCapital());
				sellerAccountList.setBankAwaitInterest(sellerAccount.getBankAwaitInterest());
				sellerAccountList.setBankInterestSum(sellerAccount.getBankInterestSum());
				sellerAccountList.setBankInvestSum(sellerAccount.getBankInvestSum());
				sellerAccountList.setBankFrost(sellerAccount.getBankFrost());
				sellerAccountList.setBankTotal(sellerAccount.getBankTotal());
				sellerAccountList.setPlanBalance(sellerAccount.getPlanBalance());//汇计划账户可用余额
				sellerAccountList.setPlanFrost(sellerAccount.getPlanFrost());
				sellerAccountList.setSeqNo(String.valueOf(creditTenderLog.getSeqNo()));
				sellerAccountList.setTxDate(creditTenderLog.getTxDate());
				sellerAccountList.setTxTime(creditTenderLog.getTxTime());
				sellerAccountList.setBankSeqNo(String.valueOf(creditTenderLog.getTxDate()) + String.valueOf(creditTenderLog.getTxTime()) + String.valueOf(creditTenderLog.getSeqNo()));
				sellerAccountList.setAccountId(sellerBankAccount.getAccount());// 出让人电子账户号
				sellerAccountList.setFrost(sellerAccount.getFrost());
				sellerAccountList.setAwait(sellerAccount.getAwait());
				sellerAccountList.setRepay(sellerAccount.getRepay());
				sellerAccountList.setRemark("出让债权");
				sellerAccountList.setCreateTime(nowTime);
				sellerAccountList.setBaseUpdate(nowTime);
				sellerAccountList.setOperator(String.valueOf(creditTenderLog.getCreditUserId()));
				sellerAccountList.setIp(creditTenderLog.getAddip());
				sellerAccountList.setIsUpdate(0);
				sellerAccountList.setBaseUpdate(0);
				sellerAccountList.setInterest(null);
				sellerAccountList.setWeb(0);
				sellerAccountList.setIsBank(1);
				sellerAccountList.setCheckStatus(0);
				boolean sellerAccountListFlag = this.accountListMapper.insertSelective(sellerAccountList) > 0 ? true : false;// 插入交易明细
				if (!sellerAccountListFlag) {
					throw new Exception("承接债转后,插入出让人交易明细accountList失败!承接订单号：" + assignOrderId);
				}
				// 4.添加网站收支明细
				// 服务费大于0时,插入网站收支明细
				if (creditTender.getCreditFee().compareTo(BigDecimal.ZERO) > 0) {
					// 插入网站收支明细记录
					AccountWebList accountWebList = new AccountWebList();
					accountWebList.setOrdid(assignOrderId);
					accountWebList.setBorrowNid(creditTender.getBidNid());
					accountWebList.setAmount(creditTender.getCreditFee());
					accountWebList.setType(1);
					accountWebList.setTrade("CREDITFEE");
					accountWebList.setTradeType("债转服务费");
					accountWebList.setUserId(creditTender.getUserId());
					accountWebList.setUsrcustid(assignBankAccount.getAccount());
					AccountWebListExample webListExample = new AccountWebListExample();
					webListExample.createCriteria().andOrdidEqualTo(assignOrderId).andTradeEqualTo(CustomConstants.TRADE_LOANFEE);
					int webListCount = this.accountWebListMapper.countByExample(webListExample);
					if (webListCount == 0) {
						UsersInfo usersInfo = getUsersInfoByUserId(userId);
						if (usersInfo != null) {
							Integer attribute = usersInfo.getAttribute();
							if (attribute != null) {
								// 查找用户的的推荐人
								Users users = getUsersByUserId(userId);
								Integer refUserId = users.getReferrer();
								SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
								SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
								spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
								List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
								if (sList != null && !sList.isEmpty()) {
									refUserId = sList.get(0).getSpreadsUserid();
								}
								// 如果是线上员工或线下员工，推荐人的userId和username不插
								if (users != null && (attribute == 2 || attribute == 3)) {
									// 查找用户信息
									EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
									if (employeeCustomize != null) {
										accountWebList.setRegionName(employeeCustomize.getRegionName());
										accountWebList.setBranchName(employeeCustomize.getBranchName());
										accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
									}
								}
								// 如果是无主单，全插
								else if (users != null && (attribute == 1)) {
									// 查找用户推荐人
									EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
									if (employeeCustomize != null) {
										accountWebList.setRegionName(employeeCustomize.getRegionName());
										accountWebList.setBranchName(employeeCustomize.getBranchName());
										accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
									}
								}
								// 如果是有主单
								else if (users != null && (attribute == 0)) {
									// 查找用户推荐人
									EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
									if (employeeCustomize != null) {
										accountWebList.setRegionName(employeeCustomize.getRegionName());
										accountWebList.setBranchName(employeeCustomize.getBranchName());
										accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
									}
								}
							}
							accountWebList.setTruename(usersInfo.getTruename());
						}
						accountWebList.setRemark(creditTender.getCreditNid());
						accountWebList.setNote(null);
						accountWebList.setCreateTime(nowTime);
						accountWebList.setOperator(null);
						accountWebList.setFlag(1);
						boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
						if (!accountWebListFlag) {
							throw new Exception("网站收支记录(huiyingdai_account_web_list)插入失败!" + "[承接订单号：" + assignOrderId + "]");
						}
					} else {
						throw new Exception("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + tenderOrderId + "]");
					}
				}

				// 6.更新Borrow_recover
				if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
					BorrowRecover borrowRecover = borrowRecoverList.get(0);
					// 不分期
					if (!isMonth) {
						// 管理费
						BigDecimal perManage = BigDecimal.ZERO;
						// 如果是完全承接
						if (borrowCredit.getCreditStatus() == 2) {
							perManage = borrowRecover.getRecoverFee().subtract(borrowRecover.getCreditManageFee());
						} else {
							// 按月计息，到期还本还息end
							if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
								perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(creditTender.getAssignCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
							}
							// 按天计息到期还本还息
							else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
								perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(creditTender.getAssignCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
							}
						}
						perManageSum = perManage;
						CreditRepay creditRepay = new CreditRepay();
						creditRepay.setUserId(userId);// 用户名称
						creditRepay.setCreditUserId(creditTender.getCreditUserId());// 出让人id
						creditRepay.setStatus(0);// 状态
						creditRepay.setBidNid(creditTender.getBidNid());// 原标标号
						creditRepay.setCreditNid(creditTender.getCreditNid());// 债转标号
						creditRepay.setCreditTenderNid(creditTender.getCreditTenderNid());// 债转投标单号
						creditRepay.setAssignNid(creditTender.getAssignNid());// 认购单号
						creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
						creditRepay.setAssignAccount(creditTender.getAssignAccount());// 应还总额
						creditRepay.setAssignInterest(creditTender.getAssignInterest());// 应还利息
						creditRepay.setAssignInterestAdvance(creditTender.getAssignInterestAdvance());// 垫付利息
						creditRepay.setAssignPrice(creditTender.getAssignPrice());// 购买价格
						creditRepay.setAssignPay(creditTender.getAssignPay());// 支付金额
						creditRepay.setAssignRepayAccount(BigDecimal.ZERO);// 已还总额
						creditRepay.setAssignRepayCapital(BigDecimal.ZERO);// 已还本金
						creditRepay.setAssignRepayInterest(BigDecimal.ZERO);// 已还利息
						creditRepay.setAssignRepayEndTime(creditTender.getAssignRepayEndTime());// 最后还款日
						creditRepay.setAssignRepayLastTime(creditTender.getAssignRepayLastTime());// 上次还款时间
						creditRepay.setAssignRepayNextTime(creditTender.getAssignRepayNextTime());// 下次还款时间
						creditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
						creditRepay.setAssignRepayPeriod(1);// 还款期数
						creditRepay.setAssignCreateDate(creditTender.getAssignCreateDate());// 认购日期
						creditRepay.setAddTime(String.valueOf(nowTime));// 添加时间
						creditRepay.setAddip(creditTender.getAddip());// ip
						creditRepay.setClient(0);// 客户端
						creditRepay.setRecoverPeriod(1);// 原标还款期数
						creditRepay.setAdvanceStatus(0);
						creditRepay.setChargeDays(0);
						creditRepay.setChargeInterest(BigDecimal.ZERO);
						creditRepay.setDelayDays(0);
						creditRepay.setDelayInterest(BigDecimal.ZERO);
						creditRepay.setLateDays(0);
						creditRepay.setLateInterest(BigDecimal.ZERO);
						creditRepay.setUniqueNid(creditTender.getAssignNid() + "_1");// 唯一nid
						creditRepay.setManageFee(perManage);// 管理费
						creditRepay.setAuthCode(authCode);// 授权码
						creditRepayMapper.insertSelective(creditRepay);
					} else {
						// 管理费
						if (creditTender.getAssignRepayPeriod() > 0) {
							// 先息后本
							if (CalculatesUtil.STYLE_ENDMONTH.equals(borrowStyle)) {
								// 总的利息
								BigDecimal sumMonthInterest = BigDecimal.ZERO;
								// 每月偿还的利息
								BigDecimal perMonthInterest = BigDecimal.ZERO;
								for (int i = 1; i <= creditTender.getAssignRepayPeriod(); i++) {
									BigDecimal perManage = BigDecimal.ZERO;
									int periodNow = borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + i;
									// 获取borrow_recover_plan更新每次还款时间
									BorrowRecoverPlanExample borrowRecoverPlanExample = new BorrowRecoverPlanExample();
									BorrowRecoverPlanExample.Criteria borrowRecoverPlanCra = borrowRecoverPlanExample.createCriteria();
									borrowRecoverPlanCra.andBorrowNidEqualTo(creditTender.getBidNid()).andNidEqualTo(creditTender.getCreditTenderNid()).andRecoverPeriodEqualTo(periodNow);
									List<BorrowRecoverPlan> borrowRecoverPlanList = this.borrowRecoverPlanMapper.selectByExample(borrowRecoverPlanExample);
									if (borrowRecoverPlanList != null && borrowRecoverPlanList.size() > 0) {
										BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlanList.get(0);
										CreditRepay creditRepay = new CreditRepay();
										if (borrowCredit.getCreditStatus() == 2) {
											// 如果是最后一笔
											perManage = borrowRecoverPlan.getRecoverFee().subtract(borrowRecoverPlan.getCreditManageFee());
											perMonthInterest = borrowRecoverPlan.getRecoverInterest().subtract(borrowRecoverPlan.getCreditInterest());
											if (i == creditTender.getAssignRepayPeriod()) {
												creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
												creditRepay.setAssignAccount(creditTender.getAssignCapital().add(perMonthInterest));// 应还总额
												creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											} else {
												creditRepay.setAssignCapital(BigDecimal.ZERO);// 应还本金
												creditRepay.setAssignAccount(perMonthInterest);// 应还总额
												creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											}
										} else {
											// 如果不是最后一笔
											if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
												if (periodNow == borrowPeriod.intValue()) {
													perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate, borrowPeriod,
															borrowPeriod, differentialRate, 1, borrowVerifyTime);
												} else {
													perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate, borrowPeriod,
															borrowPeriod, differentialRate, 0, borrowVerifyTime);
												}
											}
											if (i == creditTender.getAssignRepayPeriod()) {
												BigDecimal lastPeriodInterest = creditTender.getAssignInterest().subtract(sumMonthInterest);
												creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
												creditRepay.setAssignAccount(creditTender.getAssignCapital().add(lastPeriodInterest));// 应还总额
												creditRepay.setAssignInterest(lastPeriodInterest);// 应还利息
											} else {
												// 每月偿还的利息
												perMonthInterest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditTender.getAssignCapital(),
														borrow.getBorrowApr().divide(new BigDecimal(100)), borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
												sumMonthInterest = sumMonthInterest.add(perMonthInterest);// 总的还款利息
												creditRepay.setAssignCapital(BigDecimal.ZERO);// 应还本金
												creditRepay.setAssignAccount(perMonthInterest);// 应还总额
												creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											}
										}

										creditRepay.setUserId(userId);// 用户名称
										creditRepay.setCreditUserId(creditTender.getCreditUserId());// 出让人id
										creditRepay.setStatus(0);// 状态
										creditRepay.setBidNid(creditTender.getBidNid());// 原标标号
										creditRepay.setCreditNid(creditTender.getCreditNid());// 债转标号
										creditRepay.setCreditTenderNid(creditTender.getCreditTenderNid());// 债转投标单号
										creditRepay.setAssignNid(creditTender.getAssignNid());// 认购单号

										if (i == 1) {
											creditRepay.setAssignInterestAdvance(creditTender.getAssignInterestAdvance());// 垫付利息
										} else {
											creditRepay.setAssignInterestAdvance(BigDecimal.ZERO);// 垫付利息
										}
										creditRepay.setAssignPrice(creditTender.getAssignPrice());// 购买价格
										creditRepay.setAssignPay(creditTender.getAssignPay());// 支付金额
										creditRepay.setAssignRepayAccount(BigDecimal.ZERO);// 已还总额
										creditRepay.setAssignRepayCapital(BigDecimal.ZERO);// 已还本金
										creditRepay.setAssignRepayInterest(BigDecimal.ZERO);// 已还利息
										creditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
										creditRepay.setAssignRepayPeriod(i);// 还款期数
										creditRepay.setAssignCreateDate(creditTender.getAssignCreateDate());// 认购日期
										creditRepay.setAddTime(String.valueOf(nowTime));// 添加时间
										creditRepay.setAddip(creditTender.getAddip());// ip
										creditRepay.setClient(0);// 客户端
										creditRepay.setManageFee(BigDecimal.ZERO);// 管理费
										creditRepay.setUniqueNid(creditTender.getAssignNid() + "_" + String.valueOf(i));// 唯一nid
										creditRepay.setAuthCode(authCode);// 授权码
										creditRepay.setAdvanceStatus(0);
										creditRepay.setChargeDays(0);
										creditRepay.setChargeInterest(BigDecimal.ZERO);
										creditRepay.setDelayDays(0);
										creditRepay.setDelayInterest(BigDecimal.ZERO);
										creditRepay.setLateDays(0);
										creditRepay.setLateInterest(BigDecimal.ZERO);
										creditRepay.setAssignRepayEndTime(creditTender.getAssignRepayEndTime());// 最后还款日
										creditRepay.setAssignRepayLastTime(creditTender.getAssignRepayLastTime());// 上次还款时间
										creditRepay.setAssignRepayNextTime(Integer.parseInt(borrowRecoverPlan.getRecoverTime()));// 下次还款时间
										creditRepay.setRecoverPeriod(borrowRecoverPlan.getRecoverPeriod());// 原标还款期数
										creditRepay.setManageFee(perManage);// 管理费
										creditRepayMapper.insertSelective(creditRepay);
										// 更新borrowRecover
										// 承接本金
										borrowRecoverPlan.setCreditAmount(borrowRecoverPlan.getCreditAmount().add(creditRepay.getAssignCapital()));
										// 垫付利息
										borrowRecoverPlan.setCreditInterestAmount(borrowRecoverPlan.getCreditInterestAmount().add(creditRepay.getAssignInterestAdvance()));
										// 债转状态
										borrowRecoverPlan.setCreditStatus(borrowCredit.getCreditStatus());
										borrowRecoverPlan.setCreditManageFee(borrowRecoverPlan.getCreditManageFee().add(perManage));
										borrowRecoverPlan.setCreditInterest(borrowRecoverPlan.getCreditInterest().add(perMonthInterest));//
										this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan);
									}
									perManageSum = perManageSum.add(perManage);
								}
							}
						}
					}
					borrowRecover.setCreditAmount(borrowRecover.getCreditAmount().add(creditTender.getAssignCapital()));
					borrowRecover.setCreditInterestAmount(borrowRecover.getCreditInterestAmount().add(creditTender.getAssignInterestAdvance()));
					borrowRecover.setCreditStatus(borrowCredit.getCreditStatus());
					borrowRecover.setCreditManageFee(borrowRecover.getCreditManageFee().add(perManageSum));// 已收债转管理费
					borrowRecover.setDebtStatus(debtEndFlag);// 债权是否结束状态
					boolean borrowRecoverFlag = borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
					if (!borrowRecoverFlag) {
						throw new Exception("更新相应的放款信息表borrowrecover失败!" + "[出借订单号：" + tenderOrderId + "]");
					}
					// 更新渠道统计用户累计出借
					// 出借人信息
					Users users = getUsers(userId);
					if (Validator.isNull(users)) {
						throw new Exception("查询相应的承接用户user失败!" + "[用户userId：" + userId + "]");
					}
					// 更新渠道统计用户累计出借
					AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
					AppChannelStatisticsDetailExample.Criteria crt = channelExample.createCriteria();
					crt.andUserIdEqualTo(userId);
					List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(channelExample);
					if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
						AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("id", channelDetail.getId());
						// 认购本金
						params.put("accountDecimal", creditTenderLog.getAssignCapital());
						// 出借时间
						params.put("investTime", nowTime);
						// 项目类型
						params.put("projectType", "汇转让");
						// 首次投标项目期限
						String investProjectPeriod = borrowCredit.getCreditTerm() + "天";
						params.put("investProjectPeriod", investProjectPeriod);
						// 更新渠道统计用户累计出借
						if (users.getInvestflag() == 1) {
							// 更新相应的累计出借金额
							this.appChannelStatisticsDetailCustomizeMapper.updateAppChannelStatisticsDetail(params);
						} else if (users.getInvestflag() == 0) {
							// 更新首投出借
							this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
						}
						_log.info("用户:" + userId + "*******************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + creditTenderLog.getAssignNid());
					} else {
						// 更新huiyingdai_utm_reg的首投信息
						UtmRegExample utmRegExample = new UtmRegExample();
						UtmRegExample.Criteria utmRegCra = utmRegExample.createCriteria();
						utmRegCra.andUserIdEqualTo(userId);
						List<UtmReg> utmRegList = this.utmRegMapper.selectByExample(utmRegExample);
						if (utmRegList != null && utmRegList.size() > 0) {
							UtmReg utmReg = utmRegList.get(0);
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("id", utmReg.getId());
							params.put("accountDecimal", creditTenderLog.getAssignCapital());
							// 出借时间
							params.put("investTime", nowTime);
							// 项目类型
							params.put("projectType", "汇转让");
							// 首次投标项目期限
							String investProjectPeriod = borrowCredit.getCreditTerm() + "天";
							// 首次投标项目期限
							params.put("investProjectPeriod", investProjectPeriod);
							// 更新渠道统计用户累计出借
							if (users.getInvestflag() == 0) {
								// 更新huiyingdai_utm_reg的首投信息
								this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
							}
						}
					}
					// 更新新手标志位
					users.setInvestflag(1);
					boolean userFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
					if (!userFlag) {
						throw new Exception("更新相应的用户新手标志位失败!" + "[用户userId：" + userId + "]");
					}
					this.sendCreditSuccessMessage(creditTender);

					// add 合规数据上报 埋点 liubin 20181122 start
					JSONObject params = new JSONObject();
					params.put("assignOrderId", creditTender.getAssignNid());
					params.put("flag", "1");//1（散）2（智投）
					params.put("status", "1"); //1承接（每笔）
					// 推送数据到MQ 承接（每笔）散
					mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_SINGLE_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
					// add 合规数据上报 埋点 liubin 20181122 end

					return true;
				} else {
					throw new Exception("未查询到相应的borrowRecover数据!" + "，用户userId：" + sellerUserId + "，出借订单号：" + tenderOrderId);
				}
			} else {
				throw new Exception("未查询到相应的borrowCredit数据!" + "，用户userId：" + sellerUserId + "，出借订单号：" + tenderOrderId);
			}
		} else {
			throw new Exception("查询相应的承接log表失败，承接订单号：" + assignOrderId);
		}
	}

	
	/**
	 * 向出让人推送债转完全承接消息
	 * 
	 * @param borrowCredit
	 */
	private void sendCreditFullMessage(BorrowCredit borrowCredit) {
		// 满标
		Users webUser = this.getUsers(borrowCredit.getCreditUserId());
		UsersInfo usersInfo = this.getUsersInfoByUserId(borrowCredit.getCreditUserId());
		if (webUser != null) {
			Map<String, String> param = new HashMap<String, String>();
			if (usersInfo.getTruename() != null && usersInfo.getTruename().length() > 1) {
				param.put("val_name", usersInfo.getTruename().substring(0, 1));
			} else {
				param.put("val_name", usersInfo.getTruename());
			}
			if (usersInfo.getSex() == 1) {
				param.put("val_sex", "先生");
			} else if (usersInfo.getSex() == 2) {
				param.put("val_sex", "女士");
			} else {
				param.put("val_sex", "");
			}
			param.put("val_amount", borrowCredit.getCreditCapital() + "");
			param.put("val_profit", borrowCredit.getCreditInterestAdvanceAssigned() + "");
			// 发送短信验证码
			SmsMessage smsMessage = new SmsMessage(null, param, webUser.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZZQBZRCG,
					CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			AppMsMessage appMsMessage = new AppMsMessage(null, param, webUser.getMobile(), MessageDefine.APPMSSENDFORMOBILE, CustomConstants.JYTZ_TPL_ZHUANRANGJIESHU);
			appMsProcesser.gather(appMsMessage);
		}
	}
	/**
	 * 向承接人推送承接成功消息
	 * 
	 * @param creditTender
	 */
	private void sendCreditSuccessMessage(CreditTender creditTender) {
		Users webUser = this.getUsers(creditTender.getUserId());
		UsersInfo usersInfo2 = this.getUsersInfoByUserId(creditTender.getUserId());
		// 发送承接成功消息
		if (webUser != null) {
			Map<String, String> param = new HashMap<String, String>();
			if (usersInfo2.getTruename() != null && usersInfo2.getTruename().length() > 1) {
				param.put("val_name", usersInfo2.getTruename().substring(0, 1));
			} else {
				param.put("val_name", usersInfo2.getTruename());
			}
			if (usersInfo2.getSex() == 1) {
				param.put("val_sex", "先生");
			} else if (usersInfo2.getSex() == 2) {
				param.put("val_sex", "女士");
			} else {
				param.put("val_sex", "");
			}
			param.put("val_title", creditTender.getCreditNid() + "");
			param.put("val_balance", creditTender.getAssignPay() + "");
			param.put("val_profit", creditTender.getAssignInterest() + "");
			param.put("val_amount", creditTender.getAssignAccount() + "");
			AppMsMessage appMsMessage = new AppMsMessage(null, param, webUser.getMobile(), MessageDefine.APPMSSENDFORMOBILE, CustomConstants.JYTZ_TPL_CJZQ);
			appMsProcesser.gather(appMsMessage);
		}
	}
	/**
	 * 获取用户的账户信息
	 *
	 * @param userId
	 * @return 获取用户的账户信息
	 */
	private Account getAccount(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<Account> listAccount = accountMapper.selectByExample(example);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	private Users getUsers(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId);
	}

	@Override
	public CreditTender selectCreditTenderByAssignNid(String logOrderId, Integer userId) {
		// 获取债转出借信息
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andAssignNidEqualTo(logOrderId).andUserIdEqualTo(userId);
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			CreditTender creditTender = creditTenderList.get(0);
			return creditTender;
		} else {
			return null;
		}
	}

	/**
	 * 调用银行结束债权接口
	 * 
	 * @param borrowRecover
	 * @return
	 */
	private boolean requestDebtEnd(BorrowRecover borrowRecover, String tenderAccountId) {
		// 出借人用户Id
		Integer tenderUserId = borrowRecover.getUserId();
		// 借款人用户Id
		Integer borrowUserId = borrowRecover.getBorrowUserid();
		BankOpenAccount borrowUserAccount = this.getBankOpenAccount(borrowUserId);
		String orderId = GetOrderIdUtils.getOrderId2(tenderUserId);
//		BankCallBean bean = new BankCallBean();
//		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
//		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_END);
//		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
//		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
//		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
//		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
//		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
//		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
//		bean.setAccountId(borrowUserAccount.getAccount());// 融资人电子账号
//		bean.setOrderId(orderId);// 订单号
//		bean.setForAccountId(tenderAccountId);// 对手电子账号
//		bean.setProductId(borrowRecover.getBorrowNid());// 标的号
//		bean.setAuthCode(borrowRecover.getAuthCode());// 授权码
//		bean.setLogUserId(String.valueOf(tenderUserId));// 出借人用户Id
//		bean.setLogOrderId(orderId);
//		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
//		BankCallBean resultBean = BankCallUtils.callApiBg(bean);
//		resultBean.convert();
//		if (resultBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
//			return true;
//		} else {
//			return false;
//		}
		_log.info(borrowRecover.getBorrowNid()+" 异常修复承接结束债权  借款人: "+borrowUserId+"-"+borrowUserAccount.getAccount()+" 出借人: "+tenderUserId+"-"+tenderAccountId+" 授权码: "+borrowRecover.getAuthCode()+" 原始订单号: "+borrowRecover.getNid());
		
		BankCreditEnd record = new BankCreditEnd();
		record.setUserId(borrowUserId);
//		record.setUsername(borrowRecover);
		record.setTenderUserId(tenderUserId);
//		record.setTenderUsername(tenderUsername);
		record.setAccountId(borrowUserAccount.getAccount());
		record.setTenderAccountId(tenderAccountId);
		record.setOrderId(orderId);
		record.setBorrowNid(borrowRecover.getBorrowNid());
		record.setAuthCode(borrowRecover.getAuthCode());
		record.setCreditEndType(3); // 结束债权类型（1:还款，2:散标债转，3:计划债转）'
		record.setStatus(0);
		record.setOrgOrderId(borrowRecover.getNid());
		
		int nowTime = GetDate.getNowTime10();
		record.setCreateUser(tenderUserId);
		record.setCreateTime(nowTime);
		record.setUpdateUser(tenderUserId);
		record.setUpdateTime(nowTime);
		
		this.bankCreditEndMapper.insertSelective(record);
		return true;
		
	}

	/**
	 * 发送法大大PDF生成MQ处理
	 * @param tenderUserId
	 * @param borrowNid
	 * @param assignOrderId
	 * @param creditNid
	 * @param creditTenderNid
	 */
	@Override
	public void sendPdfMQ(Integer tenderUserId,String borrowNid, String assignOrderId, String creditNid, String creditTenderNid) {
		FddGenerateContractBean bean = new FddGenerateContractBean();
		bean.setAssignOrderId(assignOrderId);
		bean.setOrdid(assignOrderId);
		bean.setCreditNid(creditNid);
		bean.setCreditTenderNid(creditTenderNid);
		bean.setTenderUserId(tenderUserId);
		bean.setBorrowNid(borrowNid);
		bean.setTransType(3);
		bean.setTenderType(1);
		this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
	}

	/**
	 * 根据主key更新债转出借log表
	 * @param creditTenderLog
	 * @return
	 */
	@Override
	public boolean updateByPrimaryKeySelective(CreditTenderLog creditTenderLog) {
		return this.creditTenderLogMapper.updateByPrimaryKeySelective(creditTenderLog) > 0 ? true : false;
	}
}
