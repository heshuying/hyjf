package com.hyjf.api.server.nonwithdraw;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.HjhUserAuthMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallParamConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

/**
 * 外部服务接口:用户免密提现Service实现类
 * 
 * @author sunss
 *
 */
@Service
public class NonCashWithdrawServiceImpl extends BaseServiceImpl implements NonCashWithdrawService {

	Logger _log = LoggerFactory.getLogger(NonCashWithdrawServiceImpl.class);

	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private TransactionDefinition transactionDefinition;
	@Autowired
    private HjhUserAuthMapper hjhUserAuthMapper;
	// 提现状态:提现中
	private static final int WITHDRAW_STATUS_WAIT = 1;
	// 提现状态:失败
	private static final int WITHDRAW_STATUS_FAIL = 3;
	// 提现状态:成功
	private static final int WITHDRAW_STATUS_SUCCESS = 2;

	/**
	 * 根据电子账户号查询用户开户信息
	 * 
	 * @param accountId
	 * @return
	 */
	@Override
	public BankOpenAccount selectBankOpenAccountByAccountId(String accountId) {
		BankOpenAccountExample example = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria cra = example.createCriteria();
		cra.andAccountEqualTo(accountId);
		List<BankOpenAccount> list = this.bankOpenAccountMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据用户ID查询用户银行卡信息
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public BankCard getBankCardByUserId(Integer userId) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<BankCard> list = bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取用户的提现费率
	 *
	 * @param userId
	 * @param bankId
	 * @param type
	 */
	@Override
	public String getWithdrawFee(Integer userId, String bankId, BigDecimal amount) {
		BankCard bankCard = getBankInfo(userId, bankId);
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
	 * 获取用户的银行卡信息
	 *
	 * @param userId
	 * @return 用户的银行卡信息
	 */
	@Override
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

	/**
	 * 根据用户ID查询用户是否是企业用户
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId) {
		CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
		CorpOpenAccountRecordExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<CorpOpenAccountRecord> list = this.corpOpenAccountRecordMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 提现前,插入提现记录表
	 * 
	 * @param bean
	 * @param params
	 * @return
	 */
	@Override
	public int updateBeforeCash(BankCallBean bean, Map<String, String> params) {
		int ret = 0;
		String ordId = bean.getLogOrderId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getLogOrderId(); // 订单号
		AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
		accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
		List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
		if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
			return ret;
		}
		int nowTime = GetDate.getNowTime10(); // 当前时间
		// 5
		BigDecimal money = new BigDecimal(bean.getTxAmount()); // 提现金额
		BigDecimal fee = BigDecimal.ZERO; // 取得费率
		if (Validator.isNotNull(params.get("fee"))) {
			fee = new BigDecimal(params.get("fee")); // 取得费率
		}
		BigDecimal total = money.add(fee); // 实际出账金额
		Integer userId = GetterUtil.getInteger(params.get("userId")); // 用户ID
		String cardNo = params.get("cardNo"); // 银行卡号
		String bank = null;
		// 取得银行信息
		BankCard bankCard = getBankInfo(userId, cardNo);
		if (bankCard != null) {
			bank = bankCard.getBank();
		}
		System.out.println("money:"+money+"====total:"+total+"========fee:"+fee);
		Accountwithdraw record = new Accountwithdraw();
		record.setUserId(userId);
		record.setNid(bean.getLogOrderId()); // 订单号
		record.setStatus(WITHDRAW_STATUS_WAIT); // 状态: 0:处理中
		record.setAccount(cardNo);// 提现银行卡号
		record.setBank(bank); // 提现银行
		record.setBankId(bankCard.getId());
		record.setBranch(null);
		record.setProvince(0);
		record.setCity(0);
		record.setTotal(total);
		record.setCredited(money);
		record.setBankFlag(1);
		record.setFee(CustomUtil.formatAmount(fee.toString()));
		record.setAddtime(String.valueOf(nowTime));
		record.setAddip(params.get("ip"));
		record.setAccountId(bean.getAccountId());
		record.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
		record.setTxDate(Integer.parseInt(bean.getTxDate()));
		record.setTxTime(Integer.parseInt(bean.getTxTime()));
		record.setSeqNo(Integer.parseInt(bean.getSeqNo()));
		record.setRemark("第三方提现");
		record.setClient(GetterUtil.getInteger(params.get("client"))); // 0pc
		record.setWithdrawType(1); // 提现类型 0主动提现  1代提现
		// 插入用户提现记录表
		ret += this.accountwithdrawMapper.insertSelective(record);
		return ret;
	}

	/**
	 * 根据提现订单号查询提现记录
	 * 
	 * @param logOrderId
	 * @return
	 */
	@Override
	public Accountwithdraw getAccountWithdrawByOrdId(String logOrderId) {
		AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
		accountWithdrawExample.createCriteria().andNidEqualTo(logOrderId).andStatusEqualTo(WITHDRAW_STATUS_SUCCESS);
		List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
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
		// 查询账户信息
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCriteria = accountExample.createCriteria();
		accountCriteria.andUserIdEqualTo(userId);
		Account account = this.accountMapper.selectByExample(accountExample).get(0);
		// 根据用户ID查询用户银行卡信息
		BankCard bankCard = this.selectBankCardByUserId(userId);
		String ordId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId(); // 订单号
		int nowTime = GetDate.getNowTime10(); // 当前时间
		AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
		accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
		List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);

		if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
			// 提现信息
			Accountwithdraw accountWithdraw = listAccountWithdraw.get(0);
			// 返回值=000成功 ,大额提现返回值为 CE999028
			if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) {
				// 如果信息未被处理
				if (accountWithdraw.getStatus() == WITHDRAW_STATUS_SUCCESS) {
					// 如果是已经提现成功了
					return jsonMessage("提现成功", "0");
				} else {
					// 查询是否已经处理过
					AccountListExample accountListExample = new AccountListExample();
					accountListExample.createCriteria().andNidEqualTo(ordId).andTradeEqualTo("cash_success");
					int accountlistCnt = this.accountListMapper.countByExample(accountListExample);
					// 未被处理
					if (accountlistCnt == 0) {
						// 开启事务
						TransactionStatus txStatus = null;
						try {
							txStatus = this.transactionManager.getTransaction(transactionDefinition);
							// 提现金额
							BigDecimal transAmt = bean.getBigDecimal(BankCallParamConstant.PARAM_TXAMOUNT);
							// 提现手续费从数据库中读取
							String fee = params.get("fee");//this.getWithdrawFee(userId, bankCard.getCardNo(), transAmt);
							// 提现手续费
							BigDecimal feeAmt = new BigDecimal(fee);
							// 总的交易金额
							BigDecimal total = transAmt.add(feeAmt);
							// 更新订单信息
							accountWithdraw.setFee((CustomUtil.formatAmount(feeAmt.toString()))); // 更新手续费
							accountWithdraw.setCredited(transAmt); // 更新到账金额
							accountWithdraw.setTotal(total); // 更新到总额
							accountWithdraw.setStatus(WITHDRAW_STATUS_SUCCESS);// 4:成功
							accountWithdraw.setUpdateTime(nowTime);
							accountWithdraw.setAccount(bean.getAccountId());
							accountWithdraw.setReason("");
							boolean isAccountwithdrawFlag = this.accountwithdrawMapper.updateByExampleSelective(accountWithdraw, accountWithdrawExample) > 0 ? true : false;
							if (!isAccountwithdrawFlag) {
								throw new Exception("提现后,更新用户提现记录表失败!");
							}
							Account newAccount = new Account();
							// 更新账户信息
							newAccount.setUserId(userId);// 用户Id
							newAccount.setBankTotal(total); // 累加到账户总资产
							newAccount.setBankBalance(total); // 累加可用余额
							newAccount.setBankBalanceCash(total);// 江西银行可用余额
							boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankWithdrawSuccess(newAccount) > 0 ? true : false;
							if (!isAccountUpdateFlag) {
								throw new Exception("提现后,更新用户Account表失败!");
							}
							// 重新获取用户信息
							account = this.getAccount(userId);
							// 写入收支明细
							AccountList accountList = new AccountList();
							// 重新查询用户账户信息
							account = this.getAccount(userId);
							accountList.setNid(ordId);
							accountList.setUserId(userId);
							accountList.setAmount(total);
							accountList.setType(2);
							accountList.setTrade("cash_success");
							accountList.setTradeCode("balance");
							accountList.setTotal(account.getTotal());
							accountList.setBalance(account.getBalance());
							accountList.setFrost(account.getFrost());
							accountList.setAwait(account.getAwait());
							accountList.setRepay(account.getRepay());
							accountList.setBankTotal(account.getBankTotal()); // 银行总资产
							accountList.setBankBalance(account.getBankBalance()); // 银行可用余额
							accountList.setBankFrost(account.getBankFrost());// 银行冻结金额
							accountList.setBankWaitCapital(account.getBankWaitCapital());// 银行待还本金
							accountList.setBankWaitInterest(account.getBankWaitInterest());// 银行待还利息
							accountList.setBankAwaitCapital(account.getBankAwaitCapital());// 银行待收本金
							accountList.setBankAwaitInterest(account.getBankAwaitInterest());// 银行待收利息
							accountList.setBankAwait(account.getBankAwait());// 银行待收总额
							accountList.setBankInterestSum(account.getBankInterestSum()); // 银行累计收益
							accountList.setBankInvestSum(account.getBankInvestSum());// 银行累计出借
							accountList.setBankWaitRepay(account.getBankWaitRepay());// 银行待还金额
							accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
							accountList.setPlanFrost(account.getPlanFrost());
							accountList.setSeqNo(bean.getSeqNo());
							accountList.setTxDate(Integer.parseInt(bean.getTxDate()));
							accountList.setTxTime(Integer.parseInt(bean.getTxTime()));
							accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
							accountList.setAccountId(bean.getAccountId());
							accountList.setRemark("第三方提现");
							accountList.setCreateTime(nowTime);
							accountList.setBaseUpdate(nowTime);
							accountList.setOperator(params.get("userId"));
							accountList.setIp(params.get("ip"));
							accountList.setIsUpdate(0);
							accountList.setBaseUpdate(0);
							accountList.setInterest(null);
							accountList.setIsBank(1);
							accountList.setWeb(0);
							accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
							accountList.setTradeStatus(1);// 0失败1成功2失败
							boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
							if (!isAccountListFlag) {
								throw new Exception("提现成功后,插入交易明细表失败~!");
							}
							// 提交事务
							this.transactionManager.commit(txStatus);

							return jsonMessage("提现成功!", "0");
						} catch (Exception e) {
							// 回滚事务
							transactionManager.rollback(txStatus);
							e.printStackTrace();
							return jsonMessage("提现失败,请联系客服!", "1");
						}
					}
				}
			} else {
				// 提现失败,更新订单状态
				AccountwithdrawExample example = new AccountwithdrawExample();
				AccountwithdrawExample.Criteria cra = example.createCriteria();
				cra.andNidEqualTo(ordId);
				List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(example);
				if (list != null && list.size() > 0) {
					Accountwithdraw accountwithdraw = list.get(0);
					if (WITHDRAW_STATUS_WAIT == accountWithdraw.getStatus()) {
						accountwithdraw.setStatus(WITHDRAW_STATUS_FAIL);
						accountwithdraw.setUpdateTime(nowTime);
						// 失败原因
						String reason = this.getBankRetMsg(bean.getRetCode());
						accountwithdraw.setReason(reason);
						boolean isUpdateFlag = this.accountwithdrawMapper.updateByExample(accountwithdraw, example) > 0 ? true : false;
						if (!isUpdateFlag) {
							throw new Exception("提现失败后,更新提现记录表失败");
						}
					}
				}
				return jsonMessage(bean.getRetMsg() == null ? "" : bean.getRetMsg(), "1");
			}
		}
		return null;
	}

	/**
	 * 根据用户ID查询用户银行卡信息
	 * 
	 * @param userId
	 * @return
	 */
	private BankCard selectBankCardByUserId(Integer userId) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<BankCard> list = this.bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
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
	@Override
	public JSONObject jsonMessage(String errorDesc, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(errorDesc)) {
			jo = new JSONObject();
			jo.put("error", error);
			jo.put("errorDesc", errorDesc);
		}
		return jo;
	}

	/**
	 * 根据userid查询用户免密授权信息
	 * @author sunss
	 * @param userId
	 */
    @Override
    public HjhUserAuth getUserAuthByUserId(Integer userId) {
        HjhUserAuthExample userAuthExample = new HjhUserAuthExample();
        HjhUserAuthExample.Criteria cra = userAuthExample.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andDelFlgEqualTo(0);
        List<HjhUserAuth> list = this.hjhUserAuthMapper.selectByExample(userAuthExample);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

	@Override
	public BigDecimal getUserFee(String instCode) {
		 HjhInstConfigExample configExample = new HjhInstConfigExample();
		 HjhInstConfigExample.Criteria cra = configExample.createCriteria();
	        cra.andDelFlgEqualTo(0);
	        cra.andInstCodeEqualTo(instCode);
	        List<HjhInstConfig> list = this.hjhInstConfigMapper.selectByExample(configExample);
	        if (list != null && list.size() > 0) {
	            return list.get(0).getCommissionFee();
	        }
	        return new BigDecimal(1);
	}
}
