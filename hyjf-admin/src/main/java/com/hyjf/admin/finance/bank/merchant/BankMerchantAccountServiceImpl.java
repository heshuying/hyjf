package com.hyjf.admin.finance.bank.merchant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.customize.UserInfoCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.AdminAccountCustomizeMapper;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountInfo;
import com.hyjf.mybatis.model.auto.BankMerchantAccountInfoExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BankMerchantAccountListExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ChinapnrLogExample;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallParamConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

@Service
public class BankMerchantAccountServiceImpl extends BaseServiceImpl implements BankMerchantAccountService {
	
	Logger _log = LoggerFactory.getLogger(BankMerchantAccountServiceImpl.class);

	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private TransactionDefinition transactionDefinition;
	@Autowired
	private UserInfoCustomizeMapper userInfoCustomizeMapper;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 获取商户子账户列表
	 * 
	 * @return
	 */
	public List<BankMerchantAccount> selectRecordList(BankMerchantAccountListBean form, int limitStart, int limitEnd) {
		BankMerchantAccountExample example = new BankMerchantAccountExample();
		example.setOrderByClause("create_time DESC");
		return bankMerchantAccountMapper.selectByExample(example);
	}

	/**
	 * 查询商户子账户表相应的数据总数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int queryRecordTotal(BankMerchantAccountListBean form) {
		BankMerchantAccountExample example = new BankMerchantAccountExample();
		return bankMerchantAccountMapper.countByExample(example);
	}

	/**
	 * 更新商户子账户金额
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BankMerchantAccount> updateBankMerchantAccount(List<BankMerchantAccount> recordList) {
		int nowTime = GetDate.getNowTime10();
		// 返回数组
		List<BankMerchantAccount> accountList = new ArrayList<BankMerchantAccount>();

		for (int i = 0; i < recordList.size(); i++) {
			BankMerchantAccount record = recordList.get(i);
			// 调用存管接口
			BankCallBean bean = new BankCallBean();
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			// 银行存管
			bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码balanceQuery
			bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
			bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道000001手机APP
			bean.setAccountId(record.getAccountCode());// 存管平台分配的账号19位
			bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(ShiroUtil.getLoginUserId())));// 订单号
			bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
			bean.setLogUserId(String.valueOf(ShiroUtil.getLoginUserId()));
			bean.setLogClient(0);// 平台
			try {
				BankCallBean retBean = BankCallUtils.callApiBg(bean);
				if (retBean == null) {
					return recordList;
				}
				if (BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
					// 账面余额 账面余额-可用余额=冻结金额
					BigDecimal currBal = new BigDecimal(retBean.getCurrBal());
					// 可用余额
					BigDecimal availBal = new BigDecimal(retBean.getAvailBal());
					// 处理成功返回，更新数据
					record.setAccountBalance(currBal);
					record.setAvailableBalance(availBal);
					record.setFrost(currBal.subtract(availBal));
					record.setUpdateTime(nowTime);
					this.bankMerchantAccountMapper.updateByPrimaryKeySelective(record);
					accountList.add(record);
				}else{
				    accountList.add(record);
				}
			} catch (Exception e) {
				return recordList;
			}
		}
		return accountList;
	}

	@Autowired
	private AdminAccountCustomizeMapper adminAccountCustomizeMapper;

	/**
	 * 查询用户银行交易明细
	 * 
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param transType
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author Michael
	 */

	@Override
	public BankCallBean queryAccountDetails(Integer userId, String accountId, String startDate, String endDate, String type,
	    String transType, String pageNum, String pageSize,String inpDate,String inpTime,String relDate,String traceNo) {
		// 参数不正确
		if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate) || StringUtils.isEmpty(type)) {
			return null;
		}
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_DETAILS_QUERY);// 消息类型
																		// 修改手机号增强
																		// accountDetailsQuery2
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setStartDate(startDate);// 起始日期
		bean.setEndDate(endDate);// 结束日期
		bean.setType(type);// 交易种类 0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
		if ("9".equals(type)) {
			bean.setTranType(transType);// 交易类型
		}
		// 以上跟旧版本一样
        // 翻页标识  空：首次查询；1：翻页查询；
        if (StringUtils.isNotEmpty(pageNum)) {
            bean.setRtnInd("1");
        } else {
            bean.setRtnInd("");
        }
        bean.setInpDate(inpDate);
        bean.setInpTime(inpTime);
        bean.setRelDate(relDate);
        bean.setTraceNo(traceNo);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		// 调用接口
		return BankCallUtils.callApiBg(bean);
	}

	/**
	 * 处理线下充值
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public boolean insertAccountDetails(BankMerchantAccount bankMerchantAccount, SynBalanceBean synBalanceBean, String username, String ip) {

		// 添加重复校验
		try {

			BankMerchantAccountListExample bankMerchantAccountListExample = new BankMerchantAccountListExample();
			bankMerchantAccountListExample.createCriteria().andTxDateEqualTo(Integer.parseInt(synBalanceBean.getInpDate())).andTxTimeEqualTo(Integer.parseInt(synBalanceBean.getInpTime()))
					.andSeqNoEqualTo(synBalanceBean.getTraceNo() + "");
			List<BankMerchantAccountList> bankMerchantAccountLists = bankMerchantAccountListMapper.selectByExample(bankMerchantAccountListExample);
			if (bankMerchantAccountLists != null && bankMerchantAccountLists.size() != 0) {
				return false;
			}
			BankMerchantAccount updateBankMerchantAccount = new BankMerchantAccount();
			updateBankMerchantAccount.setAccountCode(bankMerchantAccount.getAccountCode());
			updateBankMerchantAccount.setAvailableBalance(synBalanceBean.getTxAmount());
			updateBankMerchantAccount.setAccountBalance(synBalanceBean.getTxAmount());

			// 更新账户信息
			adminAccountCustomizeMapper.updateOfBankMerchantAccountSynBalance(updateBankMerchantAccount);
			BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(bankMerchantAccount.getAccountCode());

			BankOpenAccount bankOpenAccount = this.getBankOpenAccountByCode(bankMerchantAccount.getAccountCode());
			UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(bankOpenAccount.getUserId());

			BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
			bankMerchantAccountList.setOrderId(GetOrderIdUtils.getOrderId2(bankOpenAccount.getUserId()));
			bankMerchantAccountList.setBorrowNid("");
			bankMerchantAccountList.setUserId(bankOpenAccount.getUserId());
			bankMerchantAccountList.setAccountId(synBalanceBean.getForAccountId());
			bankMerchantAccountList.setAmount(synBalanceBean.getTxAmount());
			bankMerchantAccountList.setBankAccountCode(bankMerchantAccount.getAccountCode());
			bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAvailableBalance());
			bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
			bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_MANUAL);
			bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_INCOME);
			bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
			bankMerchantAccountList.setTxDate(Integer.parseInt(synBalanceBean.getInpDate()));
			bankMerchantAccountList.setTxTime(Integer.parseInt(synBalanceBean.getInpTime()));
			bankMerchantAccountList.setSeqNo(synBalanceBean.getTraceNo() + "");
			bankMerchantAccountList.setCreateTime(new Date());
			bankMerchantAccountList.setUpdateTime(new Date());
			bankMerchantAccountList.setUpdateTime(new Date());
			bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
			bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
			bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
			bankMerchantAccountList.setCreateUserId(bankOpenAccount.getUserId());
			bankMerchantAccountList.setUpdateUserId(bankOpenAccount.getUserId());
			bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
			bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
			bankMerchantAccountList.setRemark(synBalanceBean.getDescribe());
			// this.accountMapper.updateByPrimaryKeySelective(account);
			// 生成交易明细

			this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public UserInfoCustomize queryUserInfoByUserId(Integer userId) {
		return userInfoCustomizeMapper.queryUserInfoByUserId(userId);
	}

	@Override
	public BankOpenAccount getBankOpenAccountByCode(String accountCode) {
		BankOpenAccountExample bankOpenAccountExample = new BankOpenAccountExample();
		bankOpenAccountExample.createCriteria().andAccountEqualTo(accountCode);
		List<BankOpenAccount> bankOpenAccounts = bankOpenAccountMapper.selectByExample(bankOpenAccountExample);
		if (bankOpenAccounts != null && bankOpenAccounts.size() != 0) {
			return bankOpenAccounts.get(0);
		} else {
			return null;
		}

	}

	@Override
	public BankMerchantAccount getBankMerchantAccount(String accountCode) {
		BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
		bankMerchantAccountExample.createCriteria().andAccountCodeEqualTo(accountCode);
		List<BankMerchantAccount> bankMerchantAccounts = bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample);
		if (bankMerchantAccounts != null && bankMerchantAccounts.size() != 0) {
			return bankMerchantAccounts.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String getWithdrawFee(Integer userId, int bankId, BigDecimal bigDecimal) {
		BankCard bankCard = this.getBankInfo(userId, bankId);
		String feetmp = PropUtils.getSystem(BankCallConstant.BANK_FEE);
		if (feetmp == null) {
			feetmp = "1";
		}
		if (bankCard != null) {
			String bankCode = bankCard.getBank();

			// 取得费率
			FeeConfigExample feeConfigExample = new FeeConfigExample();
			feeConfigExample.createCriteria().andBankCodeEqualTo(bankCode == null ? "" : bankCode);
			List<FeeConfig> listFeeConfig = feeConfigMapper.selectByExample(feeConfigExample);
			if (listFeeConfig != null && listFeeConfig.size() > 0) {
				FeeConfig feeConfig = listFeeConfig.get(0);
				BigDecimal takout = BigDecimal.ZERO;
				BigDecimal percent = BigDecimal.ZERO;
				// TODO 跟线上一致,全部用普通提现
				if (Validator.isNotNull(feeConfig.getNormalTakeout()) && NumberUtils.isNumber(feeConfig.getNormalTakeout())) {
					takout = new BigDecimal(feeConfig.getNormalTakeout());
				}

				return takout.add(percent).toString();
			} else {
				return feetmp;
			}
		} else {
			return feetmp;
		}
	}

	/**
	 * 取得用户银行卡信息
	 *
	 * @param userId
	 * @param bankId
	 * @return
	 */
	public BankCard getBankInfo(Integer userId, Integer bankId) {
		if (Validator.isNotNull(userId) && Validator.isNotNull(bankId)) {
			// 取得用户银行卡信息
			BankCardExample bankCardExample = new BankCardExample();
			bankCardExample.createCriteria().andUserIdEqualTo(userId).andIdEqualTo(bankId);
			List<BankCard> listBankCard = this.bankCardMapper.selectByExample(bankCardExample);
			if (listBankCard != null && listBankCard.size() > 0) {
				return listBankCard.get(0);
			}
		}
		return null;
	}

	@Override
	public String check(String param, String accountCode) {
		if (param == null || param.length() == 0 || "0".equals(param)) {
			return "金额不能为0";
		}
		BigDecimal amount = new BigDecimal("0");
		try {
			amount = new BigDecimal(param);
		} catch (Exception e) {
			return "请输入正确的金额";
		}
		if (amount.intValue() <= 1) {
			return "金额必须大于1元";
		}
		BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
		bankMerchantAccountExample.createCriteria().andAccountCodeEqualTo(accountCode);
		List<BankMerchantAccount> bankMerchantAccounts = this.bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample);
		BankMerchantAccount bankMerchantAccount = new BankMerchantAccount();
		if (bankMerchantAccounts != null && bankMerchantAccounts.size() > 0) {
			bankMerchantAccount = bankMerchantAccounts.get(0);
		} else {
			return "账户不存在";
		}

		if(bankMerchantAccount.getIsSetPassword()==0){
		    return "请先设置交易密码";
		}
		return null;
	}

	@Override
	public BankCard getBankCardByAccountCode(String accountCode) {
		if (Validator.isNotNull(accountCode)) {
			// 取得用户银行卡信息
			BankCardExample bankCardExample = new BankCardExample();
			bankCardExample.createCriteria().andCardNoEqualTo(accountCode);
			List<BankCard> listBankCard = this.bankCardMapper.selectByExample(bankCardExample);
			if (listBankCard != null && listBankCard.size() > 0) {
				return listBankCard.get(0);
			}
		}
		return null;
	}

	/**
	 * 用户提现前处理
	 *
	 * @param bean
	 * @return
	 */
	@Override
	public int updateBeforeCash(BankCallBean bean, Map<String, String> params, BankMerchantAccount bankMerchantAccount) {
		int ret = 0;
		String ordId = bean.getLogOrderId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getLogOrderId(); // 订单号

		BankMerchantAccountListExample accountWithdrawExample = new BankMerchantAccountListExample();
		accountWithdrawExample.createCriteria().andOrderIdEqualTo(ordId);
		List<BankMerchantAccountList> listAccountWithdraw = this.bankMerchantAccountListMapper.selectByExample(accountWithdrawExample);
		if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
			return ret;
		}
		BigDecimal money = new BigDecimal(bean.getTxAmount()); // 提现金额
		BigDecimal total = money; // 实际出账金额

		Integer userId = GetterUtil.getInteger(params.get("userId")); // 用户ID
		
		BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
		bankMerchantAccountList.setOrderId(ordId);
		bankMerchantAccountList.setUserId(userId);
		bankMerchantAccountList.setAccountId(bean.getAccountId());
		bankMerchantAccountList.setAmount(total);
		bankMerchantAccountList.setBankAccountCode(bean.getAccountId());
		bankMerchantAccountList.setBankAccountBalance(bankMerchantAccount.getAccountBalance());
		bankMerchantAccountList.setBankAccountFrost(bankMerchantAccount.getFrost());
		bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_MANUAL);
		bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
		bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_UNDERWAY);
		bankMerchantAccountList.setSeqNo(bean.getSeqNo());
		bankMerchantAccountList.setTxDate(Integer.parseInt(bean.getTxDate()));
		bankMerchantAccountList.setTxTime(Integer.parseInt(bean.getTxTime()));
		bankMerchantAccountList.setCreateUserId(userId);
		bankMerchantAccountList.setCreateUserName("0");
		bankMerchantAccountList.setCreateTime(new Date());
		bankMerchantAccountList.setUpdateUserId(userId);
		bankMerchantAccountList.setUpdateUserName("0");
		bankMerchantAccountList.setUpdateTime(new Date());
		bankMerchantAccountList.setRemark("红包账户或手续费账户提现");
		ret += bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);

		return ret;
	}

	/**
	 * 判断是否提现成功
	 *
	 * @param ordId
	 * @return
	 */
	@Override
	public BankMerchantAccountList getAccountWithdrawByOrdId(String ordId) {
		BankMerchantAccountListExample accountWithdrawExample = new BankMerchantAccountListExample();
		accountWithdrawExample.createCriteria().andOrderIdEqualTo(ordId).andStatusEqualTo(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
		List<BankMerchantAccountList> list = this.bankMerchantAccountListMapper.selectByExample(accountWithdrawExample);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 判断是否提现成功
	 *
	 * @param ordId
	 * @return S:成功 F:失败
	 */
	@Override
	public String checkCashResult(BankCallBean bean) {
		if (Validator.isNull(bean.getSeqNo())) {
			return null;
		}
		Integer userId = this.selectUserIdByUsrcustid(bean.getLong(BankCallParamConstant.PARAM_ACCOUNTID)); // 用户ID
		// 调用汇付接口(4.2.2 用户绑卡接口)
		BankCallBean bean1 = new BankCallBean();
		bean1.setLogOrderId(GetOrderIdUtils.getOrderId0(userId));
		bean1.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		// 银行存管
		bean1.setTxCode(BankCallMethodConstant.TXCODE_TRANSACTION_STATUS_QUERY);
		bean1.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean1.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean1.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
		bean1.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
		bean1.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean1.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
		bean1.setAccountId(bean.getAccountId());// 存管平台分配的账号
		bean1.setReqType("1");
		bean1.setReqTxCode(bean.getTxCode());
		bean1.setReqTxDate(bean.getTxDate());
		bean1.setReqTxTime(bean.getTxTime());
		bean1.setReqSeqNo(bean.getSeqNo());
		bean1.setLogUserId(String.valueOf(userId));
		// 调用汇付接口
		BankCallBean bankCallBean = BankCallUtils.callApiBg(bean1);
		if (bankCallBean != null) {
			return bankCallBean.getTxState();
		}
		return null;
	}

	/**
	 * 根据汇付账户查询user_id
	 *
	 * @param chinapnrUsrcustid
	 * @return
	 */
	public Integer selectUserIdByUsrcustid(Long chinapnrUsrcustid) {
		return this.accountChinapnrCustomizeMapper.selectUserIdByUsrcustid(chinapnrUsrcustid);
	}

	/**
	 * 取得成功信息
	 *
	 * @param ordId
	 * @return
	 */
	@Override
	public JSONObject getMsgData(String ordId) {
		if (Validator.isNull(ordId)) {
			return null;
		}

		List<String> respCode = new ArrayList<String>();
		respCode.add(BankCallStatusConstant.RESPCODE_SUCCESS);
		respCode.add(BankCallStatusConstant.RESPCODE_CHECK);
		ChinapnrLogExample example = new ChinapnrLogExample();
		example.createCriteria().andOrdidEqualTo(ordId).andMsgTypeEqualTo("Cash").andRespCodeIn(respCode);
		example.setOrderByClause(" resp_code ");
		List<ChinapnrLog> list = chinapnrLogMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			String msgData = list.get(0).getMsgdata();
			if (Validator.isNotNull(msgData)) {
				try {
					JSONObject jo = JSONObject.parseObject(msgData);
					return jo;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * 更新提现表
	 *
	 * @param ordId
	 * @return
	 */
	@Override
	public int updateAccountWithdrawByOrdId(String ordId, String reason) {
		
		BankMerchantAccountListExample accountWithdrawExample = new BankMerchantAccountListExample();
		accountWithdrawExample.createCriteria().andOrderIdEqualTo(ordId);


		BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
		bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_FAIL);
		bankMerchantAccountList.setUpdateUserName("0");
		bankMerchantAccountList.setUpdateTime(new Date());
		
		int ret = bankMerchantAccountListMapper.updateByExampleSelective(bankMerchantAccountList, accountWithdrawExample);
		return ret;
	}

	/**
	 * 用户提现回调方法
	 *
	 * @param bean
	 * @return
	 */
	@Override
	public synchronized JSONObject handlerAfterCash(BankCallBean bean, Map<String, String> params) throws Exception {
		// 用户ID
		int userId = Integer.parseInt(params.get("userId"));
		String ordId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId(); // 订单号
		int nowTime = GetDate.getNowTime10(); // 当前时间
		
		BankMerchantAccountListExample accountWithdrawExample = new BankMerchantAccountListExample();
		accountWithdrawExample.createCriteria().andOrderIdEqualTo(ordId);
		List<BankMerchantAccountList> listAccountWithdraw = this.bankMerchantAccountListMapper.selectByExample(accountWithdrawExample);
		
		if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
			// 提现信息
			BankMerchantAccountList accountWithdraw = listAccountWithdraw.get(0);
			// 如果信息未被处理
			if (accountWithdraw.getStatus() == CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS) {
				// 如果是已经提现成功了
				return jsonMessage("提现成功", "0");
			} else {

				BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
				BankMerchantAccountExample.Criteria bankMerchantAccountCriteria = bankMerchantAccountExample.createCriteria();
				bankMerchantAccountCriteria.andAccountCodeEqualTo(bean.getAccountId());
				BankMerchantAccount bankMerchantAccount = this.bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample).get(0);

				// 开启事务
				TransactionStatus txStatus = null;
				try {
					txStatus = this.transactionManager.getTransaction(transactionDefinition);
					// 提现金额
					BigDecimal transAmt = bean.getBigDecimal(BankCallParamConstant.PARAM_TXAMOUNT);
					// 提现手续费
					BigDecimal feeAmt = new BigDecimal(bean.getLogAcqResBean().getFee());
					// 支出金额
					BigDecimal outAmt = transAmt.add(feeAmt);


					// 更新账户信息
					bankMerchantAccount.setAccountBalance(bankMerchantAccount.getAccountBalance().subtract(outAmt));
					bankMerchantAccount.setAvailableBalance(bankMerchantAccount.getAvailableBalance().subtract(outAmt));
					bankMerchantAccountCriteria.andUpdateTimeEqualTo(bankMerchantAccount.getUpdateTime());
					bankMerchantAccount.setUpdateTime(nowTime);
					boolean isAccountUpdateFlag = this.bankMerchantAccountMapper.updateByExampleSelective(bankMerchantAccount, bankMerchantAccountExample) > 0 ? true : false;

					if (!isAccountUpdateFlag) {
						throw new Exception("提现成功后,插入交易明细表失败~!");
					}

					BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
					bankMerchantAccountList.setId(accountWithdraw.getId());
					bankMerchantAccountList.setOrderId(ordId);
					bankMerchantAccountList.setBankAccountBalance(bankMerchantAccount.getAvailableBalance());
					bankMerchantAccountList.setBankAccountFrost(bankMerchantAccount.getFrost());
					bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
					bankMerchantAccountList.setUpdateUserId(userId);
					bankMerchantAccountList.setUpdateTime(new Date());
					boolean isBankMerchantAccountList = bankMerchantAccountListMapper.updateByPrimaryKeySelective(bankMerchantAccountList) > 0 ? true : false;

					// 更新用户账户信息
					if (isBankMerchantAccountList) {
						// 提交事务
						this.transactionManager.commit(txStatus);
						//
						return jsonMessage("提现成功!", "0");
					} else {
						// 回滚事务
						transactionManager.rollback(txStatus);
						return jsonMessage("提现失败,请联系客服!", "1");
					}
				} catch (Exception e) {
					// 回滚事务
					transactionManager.rollback(txStatus);
					e.printStackTrace();
					return jsonMessage("提现失败,请联系客服!", "1");
				}
			
				
			}
			
			
		}
		return null;
	}

	/**
	 * 拼装返回信息
	 * 
	 * @param errorDesc
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String errorDesc, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(errorDesc)) {
			jo = new JSONObject();
			jo.put("error", error);
			jo.put("errorDesc", errorDesc);
		}
		return jo;
	}

	public BankCard getBankInfo(Integer userId, String cardNo) {
		if (Validator.isNotNull(userId) && Validator.isNotNull(cardNo)) {
			// 取得用户银行卡信息
			BankCardExample bankCardExample = new BankCardExample();
			bankCardExample.createCriteria().andUserIdEqualTo(userId).andCardNoEqualTo(cardNo);
			List<BankCard> listBankCard = this.bankCardMapper.selectByExample(bankCardExample);
			if (listBankCard != null && listBankCard.size() > 0) {
				return listBankCard.get(0);
			}
		}
		return null;
	}

    @Override
    public int updateBankMerchantAccount(String accountCode, BigDecimal currBalance, BigDecimal balance,
        BigDecimal frost) {
        BankMerchantAccountExample example=new BankMerchantAccountExample();
        example.createCriteria().andAccountCodeEqualTo(accountCode);

        BankMerchantAccount bankMerchantAccount=new BankMerchantAccount();
        bankMerchantAccount.setAccountBalance(currBalance);
        bankMerchantAccount.setAvailableBalance(balance);
        bankMerchantAccount.setFrost(frost);
        bankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
        return bankMerchantAccountMapper.updateByExampleSelective(bankMerchantAccount, example);
    }
    
    /**
	 * 根据电子账号查询用户在江西银行的可用余额
	 * 
	 * @param accountId
	 * @return
	 */
	@Override
	public BigDecimal getBankBalance(Integer userId, String accountId) {
		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return balance;
	}

    @Override
    public void updateBankMerchantAccountIsSetPassword(String accountId, int flag) {
        
        BankMerchantAccountExample example=new BankMerchantAccountExample();
        example.createCriteria().andAccountCodeEqualTo(accountId);
        BankMerchantAccount bankMerchantAccount=new BankMerchantAccount();
        bankMerchantAccount.setIsSetPassword(flag);
        bankMerchantAccountMapper.updateByExampleSelective(bankMerchantAccount, example);
    }

    @Override
    public BankMerchantAccountInfo getBankMerchantAccountInfoByCode(String accountCode) {
        BankMerchantAccountInfoExample example=new BankMerchantAccountInfoExample();
        example.createCriteria().andAccountCodeEqualTo(accountCode);
        List<BankMerchantAccountInfo> list=bankMerchantAccountInfoMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
    
    @Override
    public int updateBeforeRecharge(BankCallBean bean, Map<String, String> params, BankMerchantAccount bankMerchantAccount) {
        int ret = 0;
        String ordId = bean.getLogOrderId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getLogOrderId(); // 订单号

        BankMerchantAccountListExample accountWithdrawExample = new BankMerchantAccountListExample();
        accountWithdrawExample.createCriteria().andOrderIdEqualTo(ordId);
        List<BankMerchantAccountList> listAccountWithdraw = this.bankMerchantAccountListMapper.selectByExample(accountWithdrawExample);
        if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
            return ret;
        }
        BigDecimal money = new BigDecimal(bean.getTxAmount()); //充值金额
        Integer userId = GetterUtil.getInteger(params.get("userId")); // 用户ID
        BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
        bankMerchantAccountList.setOrderId(ordId);
        bankMerchantAccountList.setUserId(userId);
        bankMerchantAccountList.setAccountId(bean.getAccountId());
        bankMerchantAccountList.setAmount(money);
        bankMerchantAccountList.setBankAccountCode(bean.getAccountId());
        bankMerchantAccountList.setBankAccountBalance(bankMerchantAccount.getAccountBalance());
        bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_MANUAL);
        bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_INCOME);
        bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_UNDERWAY);
        bankMerchantAccountList.setSeqNo(bean.getSeqNo());
        bankMerchantAccountList.setTxDate(Integer.parseInt(bean.getTxDate()));
        bankMerchantAccountList.setTxTime(Integer.parseInt(bean.getTxTime()));
        bankMerchantAccountList.setCreateUserId(userId);
        bankMerchantAccountList.setCreateUserName("0");
        bankMerchantAccountList.setCreateTime(new Date());
        bankMerchantAccountList.setUpdateUserId(userId);
        bankMerchantAccountList.setUpdateUserName("0");
        bankMerchantAccountList.setBankAccountFrost(bankMerchantAccount.getFrost());
        bankMerchantAccountList.setUpdateTime(new Date());
        bankMerchantAccountList.setRemark("红包账户或手续费账户充值");
        ret += bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);

        return ret;
    }

    @Override
    public JSONObject handlerAfterRecharge(BankCallBean bean, Map<String, String> params) {

        // 用户ID
        int userId = Integer.parseInt(params.get("userId"));
        String ordId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId(); // 订单号
        int nowTime = GetDate.getNowTime10(); // 当前时间
        
        BankMerchantAccountListExample accountWithdrawExample = new BankMerchantAccountListExample();
        accountWithdrawExample.createCriteria().andOrderIdEqualTo(ordId);
        List<BankMerchantAccountList> listAccountWithdraw = this.bankMerchantAccountListMapper.selectByExample(accountWithdrawExample);
        
        if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
            // 提现信息
            BankMerchantAccountList accountWithdraw = listAccountWithdraw.get(0);
            // 如果信息未被处理
            if (accountWithdraw.getStatus() == CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS) {
                // 如果是已经提现成功了
                return jsonMessage("圈存成功", "0");
            } else {

                BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
                BankMerchantAccountExample.Criteria bankMerchantAccountCriteria = bankMerchantAccountExample.createCriteria();
                bankMerchantAccountCriteria.andAccountCodeEqualTo(bean.getAccountId());
                BankMerchantAccount bankMerchantAccount = this.bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample).get(0);

                // 开启事务
                TransactionStatus txStatus = null;
                try {
                    txStatus = this.transactionManager.getTransaction(transactionDefinition);
                    // 提现金额
                    BigDecimal transAmt = bean.getBigDecimal(BankCallParamConstant.PARAM_TXAMOUNT);

                    // 更新账户信息
                    bankMerchantAccount.setAccountBalance(bankMerchantAccount.getAccountBalance().add(transAmt));
                    bankMerchantAccount.setAvailableBalance(bankMerchantAccount.getAvailableBalance().add(transAmt));
                    bankMerchantAccountCriteria.andUpdateTimeEqualTo(bankMerchantAccount.getUpdateTime());
                    bankMerchantAccount.setUpdateTime(nowTime);
                    boolean isAccountUpdateFlag = this.bankMerchantAccountMapper.updateByExampleSelective(bankMerchantAccount, bankMerchantAccountExample) > 0 ? true : false;

                    if (!isAccountUpdateFlag) {
                        throw new Exception("圈存成功后,插入交易明细表失败~!");
                    }

                    BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
                    bankMerchantAccountList.setId(accountWithdraw.getId());
                    bankMerchantAccountList.setOrderId(ordId);
                    bankMerchantAccountList.setBankAccountBalance(bankMerchantAccount.getAvailableBalance());
                    bankMerchantAccountList.setBankAccountFrost(bankMerchantAccount.getFrost());
                    bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
                    bankMerchantAccountList.setUpdateUserId(userId);
                    bankMerchantAccountList.setUpdateTime(new Date());
                    boolean isBankMerchantAccountList = bankMerchantAccountListMapper.updateByPrimaryKeySelective(bankMerchantAccountList) > 0 ? true : false;

                    // 更新用户账户信息
                    if (isBankMerchantAccountList) {
                        // 提交事务
                        this.transactionManager.commit(txStatus);
                        //
                        return jsonMessage("圈存成功!", "0");
                    } else {
                        // 回滚事务
                        transactionManager.rollback(txStatus);
                        return jsonMessage("圈存失败,请联系客服!", "1");
                    }
                } catch (Exception e) {
                    // 回滚事务
                    transactionManager.rollback(txStatus);
                    e.printStackTrace();
                    return jsonMessage("圈存失败,请联系客服!", "1");
                }
            }
        }
        return null;
    }
}
