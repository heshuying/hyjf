package com.hyjf.bank.service.borrowloan.zhitou;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBorrow;
import com.hyjf.mybatis.model.auto.AccountBorrowExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 自动扣款(放款服务)
 *
 * @author Administrator
 *
 */
@Service
//@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BatchBorrowLoanServiceImpl extends BaseServiceImpl implements BatchBorrowLoanService {

	/** 项目标号 */
	private static final String VAL_TITLE = "val_title";

	/** 用户ID */
	private static final String VAL_USERID = "userId";

	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";

	/** 出借本金 */
	private static final String VAL_BALANCE = "val_balance";

	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";

	/** 放款时间 */
	private static final String VAL_RECOVERTIME = "val_recovertime";
	
	/**分期下次还款时间*/
	private static final String VAL_NEXTRECOVERTIME = "val_nextrecovertime";
	
//	@Autowired
//	private PlatformTransactionManager transactionManager;

//	@Autowired
//	private TransactionDefinition transactionDefinition;
	
	Logger _log = LoggerFactory.getLogger(BatchBorrowLoanServiceImpl.class);


	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;
	
    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	@Override
	public List<BorrowApicron> getBorrowApicronList(Integer apiType) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andApiTypeEqualTo(apiType);
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(0);
		statusList.add(6);
		criteria.andStatusNotIn(statusList);
		criteria.andFailTimesLessThanOrEqualTo(2);
		//modify by cwyang 查询非计划任务
		criteria.andPlanNidIsNull();
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean updateBorrowApicron(BorrowApicron apicron, int status) throws Exception {
		
		int nowTime = GetDate.getNowTime10();
		String borrowNid = apicron.getBorrowNid();
		BorrowApicronExample example = new BorrowApicronExample();
		example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
		apicron.setStatus(status);
		apicron.setUpdateTime(nowTime);
		boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
		if (!apicronFlag) {
			throw new Exception("更新放款任务失败。[项目编号：" + borrowNid + "]");
		}
		BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
		borrow.setReverifyStatus(status);
		boolean borrowFlag = this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrow) > 0 ? true : false;
		if (!borrowFlag) {
			throw new Exception("更新borrow失败。[项目编号：" + borrowNid + "]");
		}
		
		return borrowFlag;
	}

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	@Override
	public List<BorrowTender> getBorrowTenderList(String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andStatusNotEqualTo(1);
		example.setOrderByClause(" id asc ");
		List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
		return list;
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	@Override
	public BankOpenAccount getBankOpenAccount(Integer userId) {
		BankOpenAccountExample accountExample = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria crt = accountExample.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<BankOpenAccount> bankAccounts = this.bankOpenAccountMapper.selectByExample(accountExample);
		if (bankAccounts != null && bankAccounts.size() == 1) {
			return bankAccounts.get(0);
		}
		return null;
	}

	@Override
	public BankCallBean batchQuery(BorrowApicron apicron) {

		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String batchNo = apicron.getBatchNo();// 放款请求批次号
		String batchTxDate = String.valueOf(apicron.getTxDate());// 放款请求日期
		int userId = apicron.getUserId();
		String channel = BankCallConstant.CHANNEL_PC;
		for (int i = 0; i < 3; i++) {
			String logOrderId = GetOrderIdUtils.getOrderId2(userId);
			String orderDate = GetOrderIdUtils.getOrderDate();
			String txDate = GetOrderIdUtils.getTxDate();
			String txTime = GetOrderIdUtils.getTxTime();
			String seqNo = GetOrderIdUtils.getSeqNo(6);
			// 调用放款接口
			BankCallBean loanBean = new BankCallBean();
			loanBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			loanBean.setTxCode(BankCallConstant.TXCODE_BATCH_QUERY);// 消息类型(批量放款)
			loanBean.setInstCode(instCode);// 机构代码
			loanBean.setBankCode(bankCode);
			loanBean.setTxDate(txDate);
			loanBean.setTxTime(txTime);
			loanBean.setSeqNo(seqNo);
			loanBean.setChannel(channel);
			loanBean.setBatchNo(batchNo);
			loanBean.setBatchTxDate(batchTxDate);
			loanBean.setLogUserId(String.valueOf(apicron.getUserId()));
			loanBean.setLogOrderId(logOrderId);
			loanBean.setLogOrderDate(orderDate);
			loanBean.setLogRemark("批次状态查询");
			loanBean.setLogClient(0);
			BankCallBean queryResult = BankCallUtils.callApiBg(loanBean);
			if (Validator.isNotNull(queryResult)) {
				String retCode = StringUtils.isNotBlank(queryResult.getRetCode()) ? queryResult.getRetCode() : "";
				if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode) || BankCallConstant.RESPCODE_BATCHNO_NOTEXIST.equals(retCode)) {
					return queryResult;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public Map startRequestLoans(BorrowApicron apicron) {
		boolean delFlag = false;
		Map map = new HashMap<>();
		int borrowUserId = apicron.getUserId();// 放款用户userId
		String borrowNid = apicron.getBorrowNid();// 项目编号
		try {
			// 取得借款人账户信息
			Account borrowAccount = this.getAccountByUserId(borrowUserId);
			if (borrowAccount == null) {
				throw new Exception("借款人账户不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
			}
			// 借款人在汇付的账户信息
			BankOpenAccount borrowerAccount = this.getBankOpenAccount(borrowUserId);
			if (borrowerAccount == null) {
				throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
			}
			String borrowAccountId = borrowerAccount.getAccount();// 借款人相应的银行账号
			// 取得借款详情
			BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
			if (borrow == null) {
				throw new Exception("借款详情不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
			}
			// 取得出借详情列表
			List<BorrowTender> tenderList = this.getBorrowTenderList(borrowNid);
			if (tenderList != null && tenderList.size() > 0) {
				int txCounts = tenderList.size();// 交易笔数
				BigDecimal txAmountSum = BigDecimal.ZERO;// 交易总金额
				BigDecimal serviceFeeSum = BigDecimal.ZERO;// 交易总服务费
				BigDecimal loanAmountSum = BigDecimal.ZERO;// 放款总本金
				JSONArray subPacksJson = new JSONArray();
				/** 循环出借详情列表 */
				for (int i = 0; i < tenderList.size(); i++) {
					// 结果集对象
					JSONObject subJson = new JSONObject();
					// 放款信息
					BorrowTender borrowTender = tenderList.get(i);
					int userId = borrowTender.getUserId();// 出借用户userId
					String loanOrderId = borrowTender.getLoanOrdid();
					BigDecimal txAmount = borrowTender.getAccount();// 交易金额
					BigDecimal loanAmount = borrowTender.getLoanAmount();// 放款金额
					txAmountSum = txAmountSum.add(txAmount);// 交易总金额
					loanAmountSum = loanAmountSum.add(loanAmount);// 放款本金
					BigDecimal serviceFee = borrowTender.getLoanFee();// 放款服务费
					serviceFeeSum = serviceFeeSum.add(serviceFee);// 总服务费
					String authCode = borrowTender.getAuthCode();// 出借授权码
					BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
					if (Validator.isNotNull(bankOpenAccount) && StringUtils.isNotBlank(bankOpenAccount.getAccount())) {
						String accountId = bankOpenAccount.getAccount();// 银行账户
						subJson.put(BankCallConstant.PARAM_ACCOUNTID, accountId);
						subJson.put(BankCallConstant.PARAM_ORDERID, loanOrderId);
						subJson.put(BankCallConstant.PARAM_TXAMOUNT, txAmount.toString());
						subJson.put(BankCallConstant.PARAM_DEBTFEE, serviceFee.toString());
						subJson.put(BankCallConstant.PARAM_FORACCOUNTID, borrowAccountId);
						subJson.put(BankCallConstant.PARAM_PRODUCTID, borrowNid);
						subJson.put(BankCallConstant.PARAM_AUTHCODE, authCode);
						subPacksJson.add(subJson);
					} else {
						throw new Exception("放款时未查询到用户的银行开户信息。[用户userId：" + userId + "]");
					}
				}
				// 拼接相应的放款参数
				if (apicron.getFailTimes() == 0) {
					apicron.setBatchAmount(loanAmountSum);
					apicron.setBatchCounts(txCounts);
					apicron.setBatchServiceFee(serviceFeeSum);
					apicron.setSucAmount(BigDecimal.ZERO);
					apicron.setSucCounts(0);
					apicron.setFailAmount(loanAmountSum);
					apicron.setFailCounts(txCounts);
				}
				apicron.setServiceFee(serviceFeeSum);
				apicron.setTxAmount(txAmountSum);
				apicron.setTxCounts(txCounts);
				apicron.setData(" ");
				
				Map resultMap = this.requestLoans(apicron, subPacksJson);
				BankCallBean loanResult = (BankCallBean) resultMap.get("result");
				delFlag = (boolean) resultMap.get("delFlag");
				if (Validator.isNotNull(loanResult)) {
					String received = loanResult.getReceived();
					if (StringUtils.isNotBlank(received)) {
						if (BankCallConstant.RECEIVED_SUCCESS.equals(received)) {
							map.put("result", true);
							map.put("delFlag", delFlag);
							return map;
						} else {
							throw new Exception("更新放款任务为放款请求失败失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
						}
					} else {
						throw new Exception("放款请求失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					}
				} else {
					throw new Exception("放款请求失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
				}
			} else {
				map.put("result", true);
				map.put("delFlag", false);
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("result", false);
		map.put("delFlag", delFlag);
		return map;
	}

	/**
	 * 自动扣款（放款）(调用汇付天下接口)
	 *
	 * @param outCustId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map requestLoans(BorrowApicron apicron, JSONArray subPacksJson) {
		Map map = new HashMap<>();
		boolean delFalg = false;
		int userId = apicron.getUserId();// 放款用户userId
		String borrowNid = apicron.getBorrowNid();// 项目编号
		// 放款子参数
		String subPacks = subPacksJson.toJSONString();
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String notifyUrl = CustomConstants.BANK_LOAN_VERIFY_URL;
		String retNotifyURL = CustomConstants.BANK_LOAN_RESULT_URL;
		String channel = BankCallConstant.CHANNEL_PC;
		for (int i = 0; i < 3; i++) {
			try {
				String logOrderId = GetOrderIdUtils.getOrderId2(apicron.getUserId());
				String orderDate = GetOrderIdUtils.getOrderDate();
				String batchNo = GetOrderIdUtils.getBatchNo();// 获取放款批次号
				String txDate = GetOrderIdUtils.getTxDate();
				String txTime = GetOrderIdUtils.getTxTime();
				String seqNo = GetOrderIdUtils.getSeqNo(6);
				apicron.setBatchNo(batchNo);
				apicron.setTxDate(Integer.parseInt(txDate));
				apicron.setTxTime(Integer.parseInt(txTime));
				apicron.setSeqNo(Integer.parseInt(seqNo));
				apicron.setBankSeqNo(txDate + txTime + seqNo);
				// 更新任务API状态为进行中
				boolean apicronFlag = this.updateBorrowApicron(apicron, CustomConstants.BANK_BATCH_STATUS_SENDING);
				if (!apicronFlag) {
					throw new Exception("更新放款任务为进行中失败。[用户ID：" + userId + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 调用放款接口
				BankCallBean loanBean = new BankCallBean();
				loanBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				loanBean.setTxCode(BankCallConstant.TXCODE_BATCH_LEND_PAY);// 消息类型(批量放款)
				loanBean.setInstCode(instCode);// 机构代码
				loanBean.setBankCode(bankCode);
				loanBean.setTxDate(txDate);
				loanBean.setTxTime(txTime);
				loanBean.setSeqNo(seqNo);
				loanBean.setChannel(channel);
				loanBean.setBatchNo(apicron.getBatchNo());
				loanBean.setTxAmount(String.valueOf(apicron.getTxAmount()));
				loanBean.setTxCounts(String.valueOf(apicron.getTxCounts()));
				loanBean.setNotifyURL(notifyUrl);
				loanBean.setRetNotifyURL(retNotifyURL);
				loanBean.setSubPacks(subPacks);
				loanBean.setLogUserId(String.valueOf(userId));
				loanBean.setLogOrderId(logOrderId);
				loanBean.setLogOrderDate(orderDate);
				loanBean.setLogRemark("批次放款请求");
				loanBean.setLogClient(0);
				BankCallBean loanResult = BankCallUtils.callApiBg(loanBean);
				if (Validator.isNotNull(loanResult)) {
					String received = StringUtils.isNotBlank(loanResult.getReceived()) ? loanResult.getReceived() : "";
					if (BankCallConstant.RECEIVED_SUCCESS.equals(received)) {
						try { 
							// 更新任务API状态
							boolean apicronResultFlag = this.updateBorrowApicron(apicron, CustomConstants.BANK_BATCH_STATUS_SENDED);
							if (apicronResultFlag) {
								map.put("result", loanResult);
								map.put("delFlag", delFalg);
								return map;
							} else {
								delFalg = true;
							}
						} catch (Exception e) {
							delFalg = true;
						}
						map.put("result", loanResult);
						map.put("delFlag", delFalg);
						return map;
					} else {
						continue;
					}
				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.put("result", null);
		map.put("delFlag", delFalg);
		return map;
	}

	@Override
	public boolean batchDetailsQuery(BorrowApicron apicron) {

		String borrowNid = apicron.getBorrowNid();// 項目编号
		BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
		// 调用批次查询接口查询批次返回结果
		List<BankCallBean> resultBeans = this.queryBatchDetails(apicron);
		// 放款成功后后续操作
		try {
			boolean loanFlag = this.borrowLoans(apicron, borrow, resultBeans);
			if (loanFlag) {
				try {
					boolean borrowFlag = this.updateBorrowStatus(apicron, borrow);
					if (borrowFlag) {
						return true;
					} else {
						throw new Exception("放款后，更新相应的标的状态失败,项目编号：" + borrowNid);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * 自动放款
	 *
	 * @throws Exception
	 */
	private boolean borrowLoans(BorrowApicron apicron, BorrowWithBLOBs borrow, List<BankCallBean> resultBeans) throws Exception {

		/** 基本变量 */
		int nowTime = GetDate.getNowTime10();
		String borrowNid = apicron.getBorrowNid();// 借款编号
		BorrowApicronExample example = new BorrowApicronExample();
		example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
		apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_DOING);
		apicron.setUpdateTime(nowTime);
		boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
		if (!apicronFlag) {
			throw new Exception("更新放款任务失败。[项目编号：" + borrowNid + "]");
		}
		borrow.setReverifyStatus(CustomConstants.BANK_BATCH_STATUS_DOING);
		boolean borrowFlag = this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrow) > 0 ? true : false;
		if (!borrowFlag) {
			throw new Exception("更新borrow失败。[项目编号：" + borrowNid + "]");
		}
		if (Validator.isNotNull(resultBeans) && resultBeans.size() > 0) {
			Map<String, JSONObject> loanResults = new HashMap<String, JSONObject>();
			for (int i = 0; i < resultBeans.size(); i++) {
				BankCallBean resultBean = resultBeans.get(i);
				String subPacks = resultBean.getSubPacks();
				if (StringUtils.isNotBlank(subPacks)) {
					JSONArray loanDetails = JSONObject.parseArray(subPacks);
					for (int j = 0; j < loanDetails.size(); j++) {
						JSONObject loanDetail = loanDetails.getJSONObject(j);
						String loanOrderId = loanDetail.getString(BankCallConstant.PARAM_ORDERID);
						loanResults.put(loanOrderId, loanDetail);
					}
				}
			}
			// 取得出借详情列表
			List<BorrowTender> tenderList = this.getBorrowTenderList(borrowNid);
			if (tenderList != null && tenderList.size() > 0) {
				for (int i = 0; i < tenderList.size(); i++) {
					try {
						// 出借明细
						BorrowTender borrowTender = tenderList.get(i);
						String loanOrderId = borrowTender.getLoanOrdid();
						String tenderOrderId = borrowTender.getNid();// 出借订单号
						JSONObject result = loanResults.get(loanOrderId);
						boolean tenderFlag = this.updateBorrowLoans(apicron, borrow, borrowTender, result);
						if (!tenderFlag) {
							throw new Exception("更新相应的出借记录失败！项目编号:" + borrowNid + "]" + "[出借订单号：" + tenderOrderId + "]");
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				return true;
			} else {
				System.out.println("未查询到相应的出借记录，项目编号:" + borrowNid + "]");
				return true;
			}
		} else {
			throw new Exception("银行交易明细查询失败！，项目编号:" + borrowNid + "]");
		}
	}

	private boolean updateBorrowLoans(BorrowApicron apicron, BorrowWithBLOBs borrow, BorrowTender borrowTender, JSONObject loanDetail) throws Exception {

		int nowTime = GetDate.getNowTime10();
		String borrowNid = apicron.getBorrowNid();// 借款编号
		Integer borrowUserid = apicron.getUserId();// 借款人ID
		String nid = apicron.getNid();// 标识ID
		String batchNo = apicron.getBatchNo();// 放款批次号
		String txDate = Validator.isNotNull(apicron.getTxDate()) ? String.valueOf(apicron.getTxDate()) : null;// 批次时间yyyyMMdd
		String txTime = Validator.isNotNull(apicron.getTxTime()) ? String.valueOf(apicron.getTxTime()) : null;// 批次时间hhmmss
		String seqNo = Validator.isNotNull(apicron.getSeqNo()) ? String.valueOf(apicron.getSeqNo()) : null;// 流水号
		String bankSeqNo = Validator.isNotNull(apicron.getBankSeqNo()) ? String.valueOf(apicron.getBankSeqNo()) : null;// 银行唯一订单号
		String loanOrdId = borrowTender.getLoanOrdid();// 放款订单号
		String tenderOrderId = borrowTender.getNid();// 出借订单号
		int tenderUserId = borrowTender.getUserId();// 出借人userId
		BigDecimal serviceFee = borrowTender.getLoanFee();// 服务费
		BigDecimal tenderAccount = borrowTender.getAccount();// 出借金额
		/** 标的基本数据 */
		String borrowStyle = borrow.getBorrowStyle();// 还款方式
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		BankOpenAccount borrowOpenAccount = this.getBankOpenAccount(borrowUserid);
		String borrowAccountId = borrowOpenAccount.getAccount();
		if (Validator.isNull(loanDetail)) {
			throw new Exception("银行端未查詢到相应的放款明细!" + "[出借订单号：" + tenderOrderId + "]");
		}
		System.out.println("-----------放款开始，--项目编号" + borrowNid + "---------" + tenderOrderId);
		String authCode = loanDetail.getString(BankCallConstant.PARAM_AUTHCODE);// 授权码
		String txState = loanDetail.getString(BankCallConstant.PARAM_TXSTATE);// 交易状态
		String orderId = loanDetail.getString(BankCallConstant.PARAM_ORDERID);// 还款订单号
		BigDecimal txAmount = loanDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT);// 操作金额
		String forAccountId = loanDetail.getString(BankCallConstant.PARAM_FORACCOUNTID);// 借款人银行账户
		String productId = loanDetail.getString(BankCallConstant.PARAM_PRODUCTID);// 标的号
		// 如果处理状态为成功
		if (txState.equals(BankCallConstant.BATCH_TXSTATE_TYPE_SUCCESS)) {
			// 取出冻结订单信息
			FreezeList freezeList = getFreezeList(tenderOrderId);
			if (Validator.isNull(freezeList)) {
				throw new Exception("冻结订单表(huiyingdai_freezeList)查询失败！, " + "出借订单号[" + tenderOrderId + "]");
			}
			// 若此笔订单已经解冻
			if (freezeList.getStatus() == 1) {
				return true;
			}
			freezeList.setStatus(1);
			boolean flag = this.freezeListMapper.updateByPrimaryKeySelective(freezeList) > 0 ? true : false;// 更新订单为已经解冻
			if (!flag) {
				throw new Exception("冻结订单表(huiyingdai_freezeList)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// 写入还款明细表(huiyingdai_borrow_recover)
			BorrowRecover borrowRecover = this.selectBorrowRecover(tenderOrderId);
			if (Validator.isNull(borrowRecover)) {
				throw new Exception("未查詢到相应的放款明细总表(borrowRecover)!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// 如果放款状态已经更新
			if (borrowRecover.getStatus() == 1) {
				return true;
			}
			BigDecimal awaitCapital = borrowRecover.getRecoverCapital();// 待收本金
			BigDecimal awaitInterest = borrowRecover.getRecoverInterest();// 待收利息
			BigDecimal awaitAccount = borrowRecover.getRecoverAccount();// 待收本息
			BigDecimal managerFee = borrowRecover.getRecoverFee();// 管理费
			borrowRecover.setStatus(1); // 放款状态
			borrowRecover.setAuthCode(authCode);// 授權碼
			borrowRecover.setLoanBatchNo(batchNo);// 放款批次号
			borrowRecover.setAddtime(String.valueOf(nowTime));
			boolean borrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
			if (!borrowRecoverFlag) {
				throw new Exception("放款明细表(huiyingdai_borrow_recover)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// [principal: 等额本金, month:等额本息,month:等额本息,end:先息后本]
			if (isMonth) {
				// 更新分期放款计划表(huiyingdai_borrow_recover_plan)
				BorrowRecoverPlan recoverPlan = new BorrowRecoverPlan();
				recoverPlan.setStatus(1); // 状态
				recoverPlan.setAuthCode(authCode);
				recoverPlan.setAddtime(String.valueOf(nowTime));
				recoverPlan.setLoanBatchNo(batchNo);
				BorrowRecoverPlanExample recoverPlanExample = new BorrowRecoverPlanExample();
				recoverPlanExample.createCriteria().andNidEqualTo(tenderOrderId);
				boolean recoverPlanFlag = this.borrowRecoverPlanMapper.updateByExampleSelective(recoverPlan, recoverPlanExample) > 0 ? true : false;
				if (!recoverPlanFlag) {
					throw new Exception("分期放款计划表(huiyingdai_borrow_recover_plan)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
				}
			}
			// 更新出借详情表
			borrowTender.setStatus(1); // 状态 0，未放款，1，已放款
			borrowTender.setTenderStatus(1); // 出借状态 0，未放款，1，已放款
			borrowTender.setApiStatus(1); // 放款状态 0，未放款，1，已放款
			borrowTender.setWeb(2); // 写入网站收支明细
			boolean borrowTenderFlag = this.borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
			if (!borrowTenderFlag) {
				throw new Exception("出借详情(huiyingdai_borrow_tender)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// 写入借款满标日志(原复审业务)
			boolean isInsert = false;
			AccountBorrow accountBorrow = getAccountBorrow(borrowNid);
			if (accountBorrow == null) {
				isInsert = true;
				accountBorrow = new AccountBorrow();
				accountBorrow.setNid(nid); // 生成规则：BorrowNid_userid_期数
				accountBorrow.setBorrowNid(borrowNid); // 借款编号
				accountBorrow.setUserId(borrowUserid); // 借款人编号
				accountBorrow.setCreateTime(nowTime); // 创建时间
				accountBorrow.setMoney(tenderAccount);// 总收入金额
				accountBorrow.setFee(serviceFee);// 计算服务费
				accountBorrow.setBalance(tenderAccount.subtract(serviceFee)); // 实际到账金额
				accountBorrow.setInterest(awaitInterest);
				accountBorrow.setManageFee(managerFee);
				accountBorrow.setRemark("借款成功[" + borrow.getBorrowNid() + "]，扣除服务费{" + accountBorrow.getFee().toString() + "}元");
				accountBorrow.setUpdateTime(nowTime); // 更新时间
			} else {
				accountBorrow.setMoney(accountBorrow.getMoney().add(tenderAccount));// 总收入金额
				accountBorrow.setFee(accountBorrow.getFee().add(serviceFee));// 计算服务费
				accountBorrow.setBalance(accountBorrow.getBalance().add(tenderAccount.subtract(serviceFee))); // 实际到账金额
				accountBorrow.setInterest(accountBorrow.getInterest().add(awaitInterest));
				accountBorrow.setManageFee(accountBorrow.getManageFee().add(managerFee));
				accountBorrow.setRemark("借款成功[" + borrow.getBorrowNid() + "]，扣除服务费{" + accountBorrow.getFee().toString() + "}元");
				accountBorrow.setUpdateTime(nowTime); // 更新时间
			}
			boolean accountBorrowFlag = false;
			if (isInsert) {
				accountBorrowFlag = this.accountBorrowMapper.insertSelective(accountBorrow) > 0 ? true : false;
			} else {
				accountBorrowFlag = this.accountBorrowMapper.updateByPrimaryKeySelective(accountBorrow) > 0 ? true : false;
			}
			if (!accountBorrowFlag) {
				throw new Exception("借款满标日志(huiyingdai_account_borrow)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// 更新账户信息(出借人)
			Account accountTender = new Account();
			accountTender.setUserId(tenderUserId);
			accountTender.setBankTotal(awaitInterest);// 出借人资金总额 +利息
			accountTender.setBankFrost(tenderAccount);// 出借人冻结金额+出借金额(等额本金时出借金额可能会大于计算出的本金之和)
			accountTender.setBankAwait(awaitAccount);// 出借人待收金额+利息+ 本金
			accountTender.setBankAwaitCapital(awaitCapital);// 出借人待收本金
			accountTender.setBankAwaitInterest(awaitInterest);// 出借人待收利息
			accountTender.setBankInvestSum(tenderAccount);// 出借人累计出借
			accountTender.setBankFrostCash(tenderAccount);// 江西银行冻结金额
			boolean investaccountFlag = this.adminAccountCustomizeMapper.updateOfLoansTender(accountTender) > 0 ? true : false;
			if (!investaccountFlag) {
				throw new Exception("出借人资金记录(huiyingdai_account)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// 取得账户信息(出借人)
			accountTender = this.getAccountByUserId(tenderUserId);
			if (Validator.isNull(accountTender)) {
				throw new Exception("出借人账户信息不存在。[出借人ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrderId + "]");
			}
			BankOpenAccount tenderOpenAccount = this.getBankOpenAccount(tenderUserId);
			// 写入收支明细
			AccountList accountList = new AccountList();
			/** 出借人银行相关 */
			accountList.setBankAwait(accountTender.getBankAwait());
			accountList.setBankAwaitCapital(accountTender.getBankAwaitCapital());
			accountList.setBankAwaitInterest(accountTender.getBankAwaitInterest());
			accountList.setBankBalance(accountTender.getBankBalance());
			accountList.setBankFrost(accountTender.getBankFrost());
			accountList.setBankInterestSum(accountTender.getBankInterestSum());
			accountList.setBankInvestSum(accountTender.getBankInvestSum());
			accountList.setBankTotal(accountTender.getBankTotal());
			accountList.setBankWaitCapital(accountTender.getBankWaitCapital());
			accountList.setBankWaitInterest(accountTender.getBankWaitInterest());
			accountList.setBankWaitRepay(accountTender.getBankWaitRepay());
			accountList.setPlanBalance(accountTender.getPlanBalance());//汇计划账户可用余额
			accountList.setPlanFrost(accountTender.getPlanFrost());
			accountList.setAccountId(tenderOpenAccount.getAccount());
			if (productId.equals(borrowNid) && forAccountId.equals(borrowAccountId) && loanOrdId.equals(orderId) && txAmount.compareTo(tenderAccount) == 0) {
				accountList.setCheckStatus(1);
			} else {
				accountList.setCheckStatus(0);
			}
			accountList.setTradeStatus(1);
			accountList.setIsBank(1);
			accountList.setTxDate(Integer.parseInt(txDate));
			accountList.setTxTime(Integer.parseInt(txTime));
			accountList.setSeqNo(seqNo);
			accountList.setBankSeqNo(bankSeqNo);
			/** 出借人非银行相关 */
			accountList.setNid(tenderOrderId); // 出借订单号
			accountList.setUserId(tenderUserId); // 出借人
			accountList.setAmount(tenderAccount); // 出借本金
			accountList.setType(2); // 2支出
			accountList.setTrade("tender_success"); // 出借成功
			accountList.setTradeCode("balance"); // 余额操作
			accountList.setTotal(accountTender.getTotal()); // 出借人资金总额
			accountList.setBalance(accountTender.getBalance()); // 出借人可用金额
			accountList.setFrost(accountTender.getFrost()); // 出借人冻结金额
			accountList.setAwait(accountTender.getAwait()); // 出借人待收金额
			accountList.setRemark("出借放款");
			accountList.setCreateTime(nowTime); // 创建时间
			accountList.setBaseUpdate(nowTime); // 更新时间
			accountList.setOperator(CustomConstants.OPERATOR_AUTO_LOANS); // 操作者
			accountList.setIp(borrow.getAddip()); // 操作IP
			accountList.setRemark(borrowNid);
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			accountList.setInterest(BigDecimal.ZERO); // 利息
			accountList.setWeb(0); // PC
			boolean tenderAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
			if (!tenderAccountListFlag) {
				throw new Exception("出借人收支明细(huiyingdai_account_list)写入失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			// 服务费大于0时,插入网站收支明细
			if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
				// 插入网站收支明细记录
				AccountWebList accountWebList = new AccountWebList();
				accountWebList.setOrdid(tenderOrderId);// 订单号
				accountWebList.setBorrowNid(borrowNid); // 出借编号
				accountWebList.setUserId(borrowTender.getUserId()); // 出借者
				accountWebList.setAmount(serviceFee); // 服务费
				accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入2支出
				accountWebList.setTrade(CustomConstants.TRADE_LOANFEE); // 服务费
				accountWebList.setTradeType(CustomConstants.TRADE_LOANFEE_NM); // 服务费
				accountWebList.setCreateTime(nowTime);//modify by cwyang 2017-10-24  网站收支时间取放款完成时间
				accountWebList.setRemark(borrowNid);
				AccountWebListExample webListExample = new AccountWebListExample();
				webListExample.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(CustomConstants.TRADE_LOANFEE);
				int webListCount = this.accountWebListMapper.countByExample(webListExample);
				if (webListCount == 0) {
					Integer userId = accountWebList.getUserId();
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
						accountWebList.setFlag(1);
					}
					boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
					if (!accountWebListFlag) {
						throw new Exception("网站收支记录(huiyingdai_account_web_list)插入失败!" + "[出借订单号：" + tenderOrderId + "]");
					}
				} else {
					throw new Exception("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + tenderOrderId + "]");
				}
			}
			List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
			if (calculates != null && calculates.size() > 0) {
				CalculateInvestInterest calculateNew = new CalculateInvestInterest();
				calculateNew.setInterestSum(borrowRecover.getRecoverInterest());
				calculateNew.setId(calculates.get(0).getId());
				this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
			}
			apicron.setSucAmount(apicron.getSucAmount().add(borrowTender.getLoanAmount()));
			apicron.setSucCounts(apicron.getSucCounts() + 1);
			apicron.setFailAmount(apicron.getFailAmount().subtract(borrowTender.getLoanAmount()));
			apicron.setFailCounts(apicron.getFailCounts() - 1);
			boolean apicronSuccessFlag = this.borrowApicronMapper.updateByPrimaryKeySelective(apicron) > 0 ? true : false;
			if (!apicronSuccessFlag) {
				throw new Exception("批次放款记录(borrowApicron)更新失败!" + "[项目编号：" + borrowNid + "]");
			}
			// add by zhangjp 放款更新用户V值 start
//			CommonSoaUtils.updateVipValue(borrowTender.getNid(), borrowTender.getUserId());
			// add by zhangjp 放款更新用户V值 end
			// 调用CRM接口删除 del by liuyang 20180112 start
//			int tenderId = borrowTender.getId();
//			try {
//				System.out.println("===============crm同步borrowTender 开始! borrID is " + tenderId);
//				InvestSysUtils.insertBorrowTenderSys(String.valueOf(tenderId));
//			} catch (Exception e) {
//				System.out.println("===============crm同步borrowTender 异常! borrID is " + tenderId);
//			}
			// 调用CRM接口删除 del by liuyang 20180112 end
			try {
				this.sendMail(borrowRecover);
				this.sendMessage(borrowRecover);
				this.sendSms(borrowRecover);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("-----------放款结束，放款成功---" + borrowNid + "---------出借订单号" + tenderOrderId);
			return true;
		} else if (txState.equals(BankCallConstant.BATCH_TXSTATE_TYPE_FAIL)) {
			// 更新出借详情表
			borrowTender.setStatus(2); // 状态 0，未放款，1，已放款 2放款失败
			borrowTender.setTenderStatus(2); // 出借状态 0，未放款，1，已放款 2放款失败
			borrowTender.setApiStatus(2); // 放款状态 0，未放款，1，已放款 2放款失败
			boolean borrowTenderFlag = this.borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
			if (!borrowTenderFlag) {
				throw new Exception("出借详情(huiyingdai_borrow_tender)更新失败!" + "[出借订单号：" + tenderOrderId + "]");
			}
			System.out.println("-----------放款结束，放款失败---" + borrowNid + "--------出借订单号" + tenderOrderId);
		}
		return false;
	}

	/**
	 * 更新放款完成状态
	 * 
	 * @param borrow
	 *
	 * @param borrowNid
	 * @param periodNow
	 * @throws Exception
	 */
	private boolean updateBorrowStatus(BorrowApicron apicron, BorrowWithBLOBs borrow) throws Exception {

		int nowTime = GetDate.getNowTime10();// 当前时间
		apicron = this.borrowApicronMapper.selectByPrimaryKey(apicron.getId());
		String borrowNid = apicron.getBorrowNid();// 项目编号
		String nid = apicron.getNid();// 放款唯一号
		int borrowUserId = apicron.getUserId();// 借款人
		int txDate = Validator.isNotNull(apicron.getTxDate()) ? apicron.getTxDate() : 0;// 批次时间yyyyMMdd
		int txTime = Validator.isNotNull(apicron.getTxTime()) ? apicron.getTxTime() : 0;// 批次时间hhmmss
		String seqNo = Validator.isNotNull(apicron.getSeqNo()) ? String.valueOf(apicron.getSeqNo()) : null;// 流水号
		String bankSeqNo = Validator.isNotNull(apicron.getBankSeqNo()) ? String.valueOf(apicron.getBankSeqNo()) : null;// 银行唯一订单号
		int borrowId = borrow.getId();// 标的记录主键
		String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		int tenderCount = apicron.getTxCounts();// 放款总笔数
		// 查询失败的放款订单
		BorrowTenderExample tenderFailExample = new BorrowTenderExample();
		tenderFailExample.createCriteria().andBorrowNidEqualTo(borrowNid).andStatusNotEqualTo(1);
		int failCount = this.borrowTenderMapper.countByExample(tenderFailExample);
		BorrowWithBLOBs newBorrow = new BorrowWithBLOBs();
		BorrowRepay newBorrowRepay = new BorrowRepay();
		BorrowRepayPlan newBorrowRepayPlan = new BorrowRepayPlan();
		int status = 3;
		if (failCount == 0) {
			status = 4;
			// 更新BorrowRepay
			newBorrowRepay.setStatus(1); // 已放款
			BorrowRepayExample repayExample = new BorrowRepayExample();
			repayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			boolean borrowRepayFlag = this.borrowRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample) > 0 ? true : false;
			if (!borrowRepayFlag) {
				throw new Exception("更新还款总表borrowrepay失败，项目编号:" + borrowNid + "]");
			}
			if (isMonth) {
				// 更新BorrowRepayPlan
				newBorrowRepayPlan.setStatus(1); // 已放款
				BorrowRepayPlanExample repayPlanExample = new BorrowRepayPlanExample();
				repayPlanExample.createCriteria().andBorrowNidEqualTo(borrowNid);
				boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByExampleSelective(newBorrowRepayPlan, repayPlanExample) > 0 ? true : false;
				if (!borrowRepayPlanFlag) {
					throw new Exception("更新还款分期表borrowrepayplan失败，项目编号:" + borrowNid + "]");
				}
			}
			// 放款总信息表
			AccountBorrow accountBorrow = getAccountBorrow(borrowNid);
			if (Validator.isNull(accountBorrow)) {
				throw new Exception("查询相应的放款日志accountBorrow失败，项目编号:" + borrowNid + "]");
			}
			BigDecimal amount = accountBorrow.getMoney();
			BigDecimal realAcmount = accountBorrow.getBalance();
			BigDecimal interest = accountBorrow.getInterest();
			BigDecimal manageFee = accountBorrow.getManageFee();
			// 更新借款人账户表(原复审业务)
			Account borrowAccount = new Account();
			borrowAccount.setUserId(borrowUserId);
			borrowAccount.setBankTotal(realAcmount);// 累加到账户总资产
			borrowAccount.setBankBalance(realAcmount); // 累加到可用余额
			borrowAccount.setBankWaitRepay(amount.add(interest).add(manageFee)); // 待还金额
			borrowAccount.setBankWaitCapital(amount); // 待还本金
			borrowAccount.setBankWaitInterest(interest);// 待还利息
			borrowAccount.setBankBalanceCash(realAcmount);// 江西银行可用余额
			boolean borrowAccountFlag = this.adminAccountCustomizeMapper.updateOfLoansBorrow(borrowAccount) > 0 ? true : false;
			if (!borrowAccountFlag) {
				throw new Exception("借款人资金记录(huiyingdai_account)更新失败!" + "[项目编号：" + borrowNid + "]");
			}
			// 取得借款人账户信息
			borrowAccount = getAccountByUserId(borrowUserId);
			BankOpenAccount borrowOpenAccount = this.getBankOpenAccount(borrowUserId);
			// 插入借款人的收支明细表(原复审业务)
			AccountList accountList = new AccountList();
			accountList.setBankAwait(borrowAccount.getBankAwait());
			accountList.setBankAwaitCapital(borrowAccount.getBankAwaitCapital());
			accountList.setBankAwaitInterest(borrowAccount.getBankAwaitInterest());
			accountList.setBankBalance(borrowAccount.getBankBalance());
			accountList.setBankFrost(borrowAccount.getBankFrost());
			accountList.setBankInterestSum(borrowAccount.getBankInterestSum());
			accountList.setBankTotal(borrowAccount.getBankTotal());
			accountList.setBankWaitCapital(borrowAccount.getBankWaitCapital());
			accountList.setBankWaitInterest(borrowAccount.getBankWaitInterest());
			accountList.setBankWaitRepay(borrowAccount.getBankWaitRepay());
			accountList.setPlanBalance(borrowAccount.getPlanBalance());//汇计划账户可用余额
			accountList.setPlanFrost(borrowAccount.getPlanFrost());
			accountList.setAccountId(borrowOpenAccount.getAccount());
			accountList.setTxDate(txDate);
			accountList.setTxTime(txTime);
			accountList.setSeqNo(seqNo);
			accountList.setBankSeqNo(bankSeqNo);
			accountList.setCheckStatus(1);
			accountList.setTradeStatus(1);
			accountList.setIsBank(1);
			// 非银行相关
			accountList.setNid(nid); // 交易凭证号生成规则BorrowNid_userid_期数
			accountList.setUserId(borrowUserId); // 借款人id
			accountList.setAmount(realAcmount); // 操作金额
			accountList.setType(1); // 收支类型1收入2支出3冻结
			accountList.setTrade("borrow_success"); // 交易类型
			accountList.setTradeCode("balance"); // 操作识别码
			accountList.setTotal(borrowAccount.getTotal()); // 资金总额
			accountList.setBalance(borrowAccount.getBalance()); // 可用金额
			accountList.setFrost(borrowAccount.getFrost()); // 冻结金额
			accountList.setAwait(borrowAccount.getAwait()); // 待收金额
			accountList.setRepay(borrowAccount.getRepay()); // 待还金额
			accountList.setCreateTime(nowTime); // 创建时间
			accountList.setOperator(CustomConstants.OPERATOR_AUTO_LOANS); // 操作员
			accountList.setRemark(borrowNid);
			accountList.setIp(""); // 操作IP
			accountList.setBaseUpdate(0);
			boolean accountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
			if (!accountListFlag) {
				throw new Exception("插入借款人放款交易明細accountList表失败，项目编号:" + borrowNid + "]");
			}
			// 更新Borrow
			newBorrow.setRecoverLastTime(nowTime); // 最后一笔放款时间
			newBorrow.setStatus(status);
			newBorrow.setReverifyStatus(CustomConstants.BANK_BATCH_STATUS_SUCCESS);
			BorrowExample borrowExample = new BorrowExample();
			borrowExample.createCriteria().andIdEqualTo(borrowId);
			boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
			if (!borrowFlag) {
				throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
			}
			BorrowApicronExample example = new BorrowApicronExample();
			example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
			apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_SUCCESS);
			apicron.setUpdateTime(nowTime);
			boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
			if (!apicronFlag) {
				throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
			}
			
			// add by hsy 优惠券放款请求加入到消息队列 start
            Map<String, String> params = new HashMap<String, String>();
            params.put("mqMsgId", GetCode.getRandomCode(10));
            // 借款项目编号
            params.put("borrowNid", borrowNid);
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_COUPONLOANS, JSONObject.toJSONString(params));
            // add by hsy 优惠券放款请求加入到消息队列 end
			
			// insert by zhangjp 增加优惠券放款区分 start
//			CommonSoaUtils.couponLoans(borrowNid);
			// insert by zhangjp 增加优惠券放款区分 end
			//发送短信
			try {
				this.sendSmsForBorrower(borrowUserId, borrowNid);
				this.sendSmsForManager(borrowNid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (failCount == tenderCount) {
			// 更新Borrow
			newBorrow.setStatus(status);
			newBorrow.setReverifyStatus(CustomConstants.BANK_BATCH_STATUS_FAIL);
			BorrowExample borrowExample = new BorrowExample();
			borrowExample.createCriteria().andIdEqualTo(borrowId);
			boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
			if (!borrowFlag) {
				throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
			}
			BorrowApicronExample example = new BorrowApicronExample();
			example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
			apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_FAIL);
			apicron.setUpdateTime(nowTime);
			boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
			if (!apicronFlag) {
				throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
			}
		} else {
			// 更新Borrow
			newBorrow.setStatus(status);
			newBorrow.setReverifyStatus(CustomConstants.BANK_BATCH_STATUS_PART_FAIL);
			BorrowExample borrowExample = new BorrowExample();
			borrowExample.createCriteria().andIdEqualTo(borrowId);
			boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
			if (!borrowFlag) {
				throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
			}
			BorrowApicronExample example = new BorrowApicronExample();
			example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
			apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_PART_FAIL);
			apicron.setUpdateTime(nowTime);
			boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
			if (!apicronFlag) {
				throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
			}
		}
		return true;
	}

	private void sendSmsForManager(String borrowNid) {
		// 管理员发送成功短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("val_title", borrowNid);
		replaceStrs.put("val_time", GetDate.formatTime());
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_FANGKUAN_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}

	private void sendSmsForBorrower(int borrowUserId, String borrowNid) {
		// 借款人发送成功短信
		AccountBorrow accountBorrow = this.getAccountBorrow(borrowNid);
		Map<String, String> borrowerReplaceStrs = new HashMap<String, String>();
		borrowerReplaceStrs.put("val_borrownid", borrowNid);
		borrowerReplaceStrs.put("val_amount", accountBorrow.getBalance().toString());
		SmsMessage borrowerSmsMessage = new SmsMessage(borrowUserId, borrowerReplaceStrs, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_JIEKUAN_SUCCESS,
				CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(borrowerSmsMessage);

	}

	/**
	 * 根据出借订单号查询相应的放款信息
	 * 
	 * @param orderId
	 * @return
	 */
	private BorrowRecover selectBorrowRecover(String orderId) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		example.createCriteria().andNidEqualTo(orderId);
		List<BorrowRecover> borrowRecovers = this.borrowRecoverMapper.selectByExample(example);
		if (borrowRecovers != null && borrowRecovers.size() == 1) {
			return borrowRecovers.get(0);
		}
		return null;
	}

	/**
	 * 查询相应的放款详情
	 * 
	 * @param apicron
	 * @return
	 */
	private List<BankCallBean> queryBatchDetails(BorrowApicron apicron) {

		int txCounts = apicron.getTxCounts();// 总交易笔数
		String batchTxDate = String.valueOf(apicron.getTxDate());
		String batchNo = apicron.getBatchNo();// 批次号
		int pageSize = 50;// 每页笔数
		int pageTotal = txCounts / pageSize + 1;// 总页数
		List<BankCallBean> results = new ArrayList<BankCallBean>();
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		for (int i = 1; i <= pageTotal; i++) {
			// 循环三次查询结果
			for (int j = 0; j < 3; j++) {
				String logOrderId = GetOrderIdUtils.getOrderId2(apicron.getUserId());
				String orderDate = GetOrderIdUtils.getOrderDate();
				String txDate = GetOrderIdUtils.getTxDate();
				String txTime = GetOrderIdUtils.getTxTime();
				String seqNo = GetOrderIdUtils.getSeqNo(6);
				BankCallBean loanBean = new BankCallBean();
				loanBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				loanBean.setTxCode(BankCallConstant.TXCODE_BATCH_DETAILS_QUERY);// 消息类型(批量放款)
				loanBean.setInstCode(instCode);// 机构代码
				loanBean.setBankCode(bankCode);
				loanBean.setTxDate(txDate);
				loanBean.setTxTime(txTime);
				loanBean.setSeqNo(seqNo);
				loanBean.setChannel(channel);
				loanBean.setBatchTxDate(batchTxDate);
				loanBean.setBatchNo(batchNo);
				loanBean.setType(BankCallConstant.DEBT_BATCH_TYPE_ALL);
				loanBean.setPageNum(String.valueOf(i));
				loanBean.setPageSize(String.valueOf(pageSize));
				loanBean.setLogUserId(String.valueOf(apicron.getUserId()));
				loanBean.setLogOrderId(logOrderId);
				loanBean.setLogOrderDate(orderDate);
				loanBean.setLogRemark("查询批次交易明细！");
				loanBean.setLogClient(0);
				// 调用放款接口
				BankCallBean loanResult = BankCallUtils.callApiBg(loanBean);
				if (Validator.isNotNull(loanResult)) {
					String retCode = StringUtils.isNotBlank(loanResult.getRetCode()) ? loanResult.getRetCode() : "";
					if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
						results.add(loanResult);
						break;
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		}
		return results;
	}

	/**
	 * 取出冻结订单
	 *
	 * @return
	 */
	private FreezeList getFreezeList(String ordId) {
		FreezeListExample example = new FreezeListExample();
		FreezeListExample.Criteria criteria = example.createCriteria();
		criteria.andOrdidEqualTo(ordId);
		List<FreezeList> list = this.freezeListMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得满标日志
	 *
	 * @return
	 */
	private AccountBorrow getAccountBorrow(String borrowNid) {
		AccountBorrowExample example = new AccountBorrowExample();
		AccountBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<AccountBorrow> list = this.accountBorrowMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 发送短信(出借成功)
	 *
	 * @param userId
	 */
	private void sendSms(BorrowRecover borrowRecover) {
		
		int userId = borrowRecover.getUserId();
		BigDecimal capital = borrowRecover.getRecoverCapital();
		BigDecimal interest = borrowRecover.getRecoverInterest();
		int repayTime = Integer.parseInt(borrowRecover.getRecoverTime());
		String dateStr = GetDate.getDateMyTimeInMillis(repayTime);
		//判断是否分期 增加分期短信模板 add by cwyang 2017-8-8
		String nid = borrowRecover.getNid();
		_log.info("==========cwyang开始发送短信=========订单号:" + nid);
		BorrowRecoverPlan planInfo = isRecoverPlan(nid);
		String planDateStr = null; //下一期时间
		String lastPlanDateStr = null;//最后一期时间
		boolean isPlan = false;
		if (planInfo != null) {
			isPlan = true;
			int planTime = Integer.parseInt(planInfo.getRecoverTime());
			planDateStr = GetDate.getDateMyTimeInMillis(planTime);
			BorrowRecoverPlan lastPlanInfo = getBorrowgetrecoverLastTime(nid,borrowRecover.getBorrowNid());
			int lastPlantime = Integer.parseInt(lastPlanInfo.getRecoverTime());
			lastPlanDateStr = GetDate.getDateMyTimeInMillis(lastPlantime);
			_log.info("==========cwyang启用分期短信模板=========订单号:" + nid + ",下期还款时间:" + planDateStr + ",最后还款时间:" + lastPlanDateStr);
		}else{
			_log.info("==========cwyang不启用分期短信模板=========订单号:" + nid);
		}
		Map<String, String> msg = new HashMap<String, String>();
		msg.put(VAL_BALANCE, capital.toString());
		msg.put(VAL_PROFIT, interest.toString());
		msg.put(VAL_RECOVERTIME, dateStr);
		msg.put(VAL_USERID, String.valueOf(userId));
		if (isPlan) {
			msg.put(VAL_NEXTRECOVERTIME, planDateStr);
			msg.put(VAL_RECOVERTIME, lastPlanDateStr);
		}
		Users users = getUsersByUserId(userId);
		if (users == null || Validator.isNull(users.getMobile()) || (users.getInvestSms() != null && users.getInvestSms() == 1)) {
			return;
		}
		System.err.println("userid=" + msg.get(VAL_USERID) + ";开始发送短信,出借金额" + msg.get(VAL_BALANCE));
		SmsMessage smsMessage = null;
		System.err.println("判断是否为分期模板:planDateStr = "+ planDateStr);
		if (isPlan) {
			smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TOUZI_PLAN_SUCCESS,
					CustomConstants.CHANNEL_TYPE_NORMAL);
		}else{
			smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TOUZI_SUCCESS,
					CustomConstants.CHANNEL_TYPE_NORMAL);
		}
		smsProcesser.gather(smsMessage);
	}

	/**
	 * 获得分期最后一期还款时间
	 * @param nid
	 * @param borrowNid 
	 * @return
	 */
	private BorrowRecoverPlan getBorrowgetrecoverLastTime(String nid, String borrowNid) {
		
		BorrowExample example = new BorrowExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrow = this.borrowMapper.selectByExample(example);
		Integer borrowPeriod = borrow.get(0).getBorrowPeriod();
		
		BorrowRecoverPlanExample planExample = new BorrowRecoverPlanExample();
		planExample.createCriteria().andNidEqualTo(nid).andRecoverPeriodEqualTo(borrowPeriod);
		List<BorrowRecoverPlan> planRecover = this.borrowRecoverPlanMapper.selectByExample(planExample);
		return planRecover.get(0);
	}

	/**
	 * 判断是否分期标的
	 * @param nid
	 * @return
	 */
	private BorrowRecoverPlan isRecoverPlan(String nid) {
		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
		example.createCriteria().andNidEqualTo(nid).andRecoverPeriodEqualTo(1);
		List<BorrowRecoverPlan> borrowRecoverPlanList = this.borrowRecoverPlanMapper.selectByExample(example);
		if (borrowRecoverPlanList != null && borrowRecoverPlanList.size() > 0) {
			return borrowRecoverPlanList.get(0);
		}
		return null;
	}

	/**
	 * 发送邮件(出借成功)
	 *
	 * @param userId
	 */
	private void sendMail(BorrowRecover borrowRecover) {

		String borrowNid = borrowRecover.getBorrowNid();
		int userId = borrowRecover.getUserId();
		String orderId = borrowRecover.getNid();
		Map<String, String> msg = new HashMap<String, String>();
		msg.put(VAL_USERID, String.valueOf(userId));
		try {
			// 向每个出借人发送邮件
			if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
				Users users = getUsersByUserId(userId);
				if (users == null || Validator.isNull(users.getEmail())) {
					return;
				}
				String email = users.getEmail();
				//发送邮件限制
				if (StringUtils.isBlank(email) || users.getIsSmtp()==1) {
					return;
				}
				System.out.println("开始发送邮件。出借订单号:" + orderId);
				msg.put(VAL_NAME, users.getUsername());
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() % 2 == 0) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				String fileName = borrowNid + "_" + orderId + ".pdf";
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
				// 查询借款人用户名
				BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
				// 借款编码
				borrowCommonCustomize.setBorrowNidSrch(borrowNid);
				List<BorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
				if (recordList != null && recordList.size() == 1) {
					Map<String, Object> contents = new HashMap<String, Object>();
					contents.put("borrowNid", borrowNid);
					contents.put("nid", borrowRecover.getNid());
					// 借款人用户名
					contents.put("borrowUsername", recordList.get(0).getUsername());
					// 本笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
					// 用户出借列表
					List<WebUserInvestListCustomize> userInvestList = this.selectUserInvestList(borrowNid, userId, orderId, -1, -1);
					if (userInvestList != null && userInvestList.size() == 1) {
						contents.put("userInvest", userInvestList.get(0));
					} else {
						System.out.println("标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。出借订单号:" + orderId);
						return;
					}
					// 如果是分期还款，查询分期信息
					String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
					if (borrowStyle != null) {
						if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
							int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, orderId);
							if (recordTotal > 0) {
								Paginator paginator = new Paginator(1, recordTotal);
								List<WebProjectRepayListCustomize> repayList = this.selectProjectRepayPlanList(borrowNid, userId, orderId, paginator.getOffset(), paginator.getLimit());
								contents.put("paginator", paginator);
								contents.put("repayList", repayList);
							} else {
								Paginator paginator = new Paginator(1, recordTotal);
								contents.put("paginator", paginator);
								contents.put("repayList", "");
							}
						}
					}
					String pdfUrl = "";
					// 融通宝居然协议不同
					if (recordList.get(0).getProjectType().equals("13")) {
						UsersInfo userinfo = this.getUsersInfoByUserId(userId);
						if (userInvestList != null && userInvestList.size() > 0) {
							contents.put("investDeatil", userInvestList.get(0));
						}
						contents.put("projectDeatil", recordList.get(0));
						contents.put("truename", userinfo.getTruename());
						contents.put("idcard", userinfo.getIdcard());
						contents.put("borrowNid", borrowNid);// 标的号
						contents.put("assetNumber", recordList.get(0).getBorrowAssetNumber());// 资产编号
						contents.put("projectType", recordList.get(0).getProjectType());// 项目类型
						String moban = CustomConstants.RTB_TENDER_CONTRACT;
						if (recordList.get(0) != null && recordList.get(0).getBorrowPublisher() != null && recordList.get(0).getBorrowPublisher().equals("中商储")) {
							moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
						}
						if (recordList.get(0) != null) {
							recordList.get(0).setBorrowPeriod(recordList.get(0).getBorrowPeriod().substring(0, recordList.get(0).getBorrowPeriod().length() - 1));
						}
						pdfUrl = PdfGenerator.generateLocal(fileName, moban, contents);
					} else {
						pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
					}
					if (StringUtils.isNotEmpty(pdfUrl)) {
						File path = new File(filePath);
						if (!path.exists()) {
							path.mkdirs();
						}
						FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
					}
					String[] emails = { email };
					MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇盈金服互联网金融服务平台居间服务协议", null, new String[] { filePath + fileName }, emails, CustomConstants.EMAILPARAM_TPL_LOANS,
							MessageDefine.MAILSENDFORMAILINGADDRESS);
					mailMessageProcesser.gather(message);
					// 更新BorrowRecover邮件发送状态
					borrowRecover.setSendmail(1);
					this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
					System.out.println("结束发送邮件。出借订单号:" + orderId);
				} else {
					System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 推送消息
	 * 
	 * @param msgList
	 * @author Administrator
	 */

	private void sendMessage(BorrowRecover borrowRecover) {
		int userId = borrowRecover.getUserId();
		BigDecimal amount = borrowRecover.getRecoverAccount();
		BigDecimal capital = borrowRecover.getRecoverCapital();
		BigDecimal interest = borrowRecover.getRecoverInterest();
		Map<String, String> msg = new HashMap<String, String>();
		msg.put(VAL_TITLE, borrowRecover.getBorrowNid());
		msg.put(VAL_AMOUNT, amount.toString());// 待收金额
		msg.put(VAL_BALANCE, capital.toString()); // 出借本金
		msg.put(VAL_PROFIT, interest.toString()); // 预期收益
		msg.put(VAL_USERID, String.valueOf(userId));
		if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT)) && new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
			Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
			if (users == null) {
				return;
			} else {
				UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
				if (StringUtils.isEmpty(userInfo.getTruename())) {
					msg.put(VAL_NAME, users.getUsername());
				} else if (userInfo.getTruename().length() > 1) {
					msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
				} else {
					msg.put(VAL_NAME, userInfo.getTruename());
				}
				Integer sex = userInfo.getSex();
				if (Validator.isNotNull(sex)) {
					if (sex.intValue() == 2) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TOUZI_SUCCESS);
				appMsProcesser.gather(smsMessage);
			}
		}
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	private List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
	}

	private List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, int userId, String nid, int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}

	private int countProjectRepayPlanRecordTotal(String borrowNid, int userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		int total = webUserInvestListCustomizeMapper.countProjectRepayPlanRecordTotal(params);
		return total;
	}

	private List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, int userId, String nid, int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper.selectProjectRepayPlanList(params);
		return projectRepayList;
	}

	@Override
	public BorrowApicron getBorrowApicron(Integer id) {
		BorrowApicron apicron = this.borrowApicronMapper.selectByPrimaryKey(id);
		return apicron;
	}
}
