package com.hyjf.batch.exception.bankwithdraw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.result.CheckResult;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 江西银行提现掉单异常处理Service实现类
 * 
 * @author LiuBin
 *
 */

@Service
public class BankWithdrawExceptionServiceImpl extends BaseServiceImpl implements BankWithdrawExceptionService {
	// 提现状态:提现中
	private static final int WITHDRAW_STATUS_DEFAULT = 0;
	// 提现状态:提现中
	private static final int WITHDRAW_STATUS_WAIT = 1;
	// 提现状态:失败
	private static final int WITHDRAW_STATUS_FAIL = 3;
	// 提现状态:成功
	private static final int WITHDRAW_STATUS_SUCCESS = 2;

	/**
	 * 检索提现中的提现记录
	 * 
	 * @return
	 */
	@Override
	public List<Accountwithdraw> selectBankWithdrawList() {
		AccountwithdrawExample example = new AccountwithdrawExample();
		AccountwithdrawExample.Criteria cra = example.createCriteria();
		List<Integer> status = new ArrayList<Integer>();
		//mod by nixiaoling 状态为0的不查找 20180428
		status.add(0);
		status.add(1);
		//status.add(3);
		cra.andStatusIn(status);// 提现状态为提现中, 审核中（处理中）, 提现失败
		cra.andBankFlagEqualTo(1);// 提现平台:江西银行
		// 当前时间
		cra.andAddtimeGreaterThanOrEqualTo(String.valueOf(GetDate.getNowTime10() - 48*60*60));// TODO T-1天之前
		cra.andAddtimeLessThanOrEqualTo(String.valueOf(GetDate.getNowTime10() - 30*60));// 30分钟之前的充值订单TODO
		return this.accountwithdrawMapper.selectByExample(example);
	}

	/**
	 * 更新处理中的充值记录
	 * 
	 * @param accountRecharge
	 */
	@Override
	public void updateWithdraw(Accountwithdraw accountwithdraw) {
		try {
			//调用银行接口
			BankCallBean bean = bankCallFundTransQuery(accountwithdraw);
			if (bean != null) {
				//调用后平台操作
				handlerAfterCash(bean, accountwithdraw);	
			}else{
				throw new Exception("调用银行接口,银行返回NULL,充值订单号:" 
									+ accountwithdraw.getNid() 
									+ ",用户Id:" + accountwithdraw.getUserId());
			}
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "bankCallFundTransQuery", e);
		}
	}
	
	/**
	 * 调用银行接口
	 * @param accountwithdraw
	 * @return
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:42:05
	 */
	private BankCallBean bankCallFundTransQuery(Accountwithdraw accountwithdraw) {
		// 银行接口用BEAN
		BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,
											BankCallConstant.TXCODE_FUND_TRANS_QUERY,
											accountwithdraw.getUserId());
		//设置特有参数
		bean.setAccountId(accountwithdraw.getAccountId());// 借款人电子账号
		bean.setOrgTxDate(String.valueOf(accountwithdraw.getTxDate()));//原交易日期
		//时间补满6位
		bean.setOrgTxTime(String.format("%06d", accountwithdraw.getTxTime()));//原交易时间
		bean.setOrgSeqNo(String.valueOf(accountwithdraw.getSeqNo()));//原交易流水号
		bean.setLogRemark("单笔资金类业务交易查询（提现Batch）");
		try {
			BankCallBean result = BankCallUtils.callApiBg(bean);
			if (result != null) {
				if (StringUtils.isBlank(result.getRetMsg())) {
					//根据响应代码取得响应描述
					result.setRetMsg(this.getBankRetMsg(result.getRetCode()));
				}
			}
			return result;
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "bankCallFundTransQuery", e);
		}
		return null;
	}
		
	/**
	 * 用户提现回调方法
	 * 参见com.hyjf.bank.service.user.bankwithdraw.BankWithdrawServiceImpl 方法  handlerAfterCash
	 * @param bean
	 * @return
	 */
	private void handlerAfterCash(BankCallBean bean, Accountwithdraw accountWithdraw) throws Exception {
		// 当前时间
		int nowTime = GetDate.getNowTime10(); 
		// 用户ID
		int userId = accountWithdraw.getUserId();
		// 提现订单号
		String ordId = accountWithdraw.getNid();
		// 根据用户ID查询用户银行卡信息
		BankCard bankCard = this.selectBankCardByUserId(userId);

		// 1.该银行接口的 业务是否成功
		// 返回值=000成功 ,大额提现返回值为 CE999028
		// 并且Result = "00"
		// 冲正撤销标志为0
		// 返回retcode的错误码和result返回其他这两个都是有可能的，具体的是哪个和银行内部的操作有关，所以retcode和result这个你们都需要判断下 
		// 其它:无该交易或者处理失败
		if ((BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) 
				&& "00".equals(bean.getResult())
				&& !("1".equals(bean.getOrFlag()))) {

			//2.银行接口返回与本地记录匹配验证
			CheckResult rtCheck = checkCallRetAndHyjf(bean,accountWithdraw);
			if (!rtCheck.isResultBool()) {
				// 验证失败，异常信息抛出
				throw new Exception(rtCheck.getResultMsg());
			}
			
			//3.DB防并发处理
			rtCheck = checkConcurrencyDB(accountWithdraw, userId, ordId);;
			if (!rtCheck.isResultBool()) {
				// 记录被其他进程处理，日志信息输出
				LogUtil.infoLog(this.getClass().getName(), "handlerAfterCash", 
						rtCheck.getResultMsg());
				return;
			}
			
//			// 提现成功后,更新银行联行号
//			// 大额提现返回成功 并且银联行号不为空的情况,将正确的银联行号更新到bankCard表中
//			System.out.println("银联行号:" + payAllianceCode);
//			if ("CE999028".equals(bean.getRetCode()) && StringUtils.isNotEmpty(payAllianceCode)) {
//				BankCardExample bankCardExample = new BankCardExample();
//				BankCardExample.Criteria cra = bankCardExample.createCriteria();
//				cra.andIdEqualTo(bankCard.getId());
//				bankCard.setPayAllianceCode(payAllianceCode);
//				try {
//					boolean isBankCardUpdateFlag = this.bankCardMapper.updateByExampleSelective(bankCard, bankCardExample) > 0 ? true : false;
//					if (!isBankCardUpdateFlag) {
//						throw new Exception("大额提现成功后,更新用户银行卡的银联行号失败~~~!" + bankCard.getId());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			
			//冲正撤销标志为1：已冲正/撤销时，对交易进行回滚操作 TODO
			//临时按照成功处理 TODO
//			if ("1".equals(bean.getOrFlag())) {
//				//TODO
//			}
			
			//4.DB更新操作
			// 提现金额
			BigDecimal transAmt = new BigDecimal(bean.getTxAmount());
			String fee = this.getWithdrawFee(userId, bankCard == null ? "" : String.valueOf(bankCard.getBankId()), transAmt);
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
			boolean isAccountwithdrawFlag = this.accountwithdrawMapper.updateByPrimaryKeySelective(accountWithdraw) > 0 ? true : false;
			if (!isAccountwithdrawFlag) {
				throw new Exception("提现后,更新用户提现记录表失败!" + "提现订单号:" + ordId + ",用户ID:" + userId);
			}
			Account newAccount = new Account();
			// 更新账户信息
			newAccount.setUserId(userId);// 用户Id
			newAccount.setBankTotal(total); // 累加到账户总资产
			newAccount.setBankBalance(total); // 累加可用余额
			newAccount.setBankBalanceCash(total);// 江西银行可用余额
			boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankWithdrawSuccess(newAccount) > 0 ? true : false;
			if (!isAccountUpdateFlag) {
				throw new Exception("提现后,更新用户Account表失败!" + "提现订单号:" + ordId + ",用户ID:" + userId);
			}
			// 重新获取用户信息
			Account account = this.getAccount(userId);
			// 写入收支明细
			AccountList accountList = new AccountList();
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
			accountList.setRemark(accountWithdraw.getRemark());//TODO
			accountList.setCreateTime(nowTime);
			accountList.setBaseUpdate(nowTime);
			accountList.setOperator(String.valueOf(userId));
			accountList.setIp(accountWithdraw.getAddip());//TODO
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			accountList.setInterest(null);
			accountList.setIsBank(1);
			accountList.setWeb(0);
			accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
			accountList.setTradeStatus(1);// 0失败1成功2失败
			boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
			if (!isAccountListFlag) {
				throw new Exception("提现成功后,插入交易明细表失败~!" + "提现订单号:" + ordId + ",用户ID:" + userId);
			}
		} else {
			// 提现失败,更新处理中订单状态为失败
			AccountwithdrawExample example = new AccountwithdrawExample();
			AccountwithdrawExample.Criteria cra = example.createCriteria();
			cra.andNidEqualTo(ordId);
			List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				Accountwithdraw accountwithdraw = list.get(0);
				if (WITHDRAW_STATUS_DEFAULT == accountWithdraw.getStatus()
						|| WITHDRAW_STATUS_WAIT == accountWithdraw.getStatus()) {
					accountwithdraw.setStatus(WITHDRAW_STATUS_FAIL);// 提现失败
					accountwithdraw.setUpdateTime(nowTime);// 更新时间
					accountwithdraw.setReason(bean.getRetMsg());// 失败原因
					
					//冲正撤销标志为1：已冲正/撤销时
					//临时按照失败处理
					if ("1".equals(bean.getOrFlag())) {
						accountwithdraw.setReason("提现订单："+ bean.getOrFlag() + "：已冲正/撤销");
					}
					
					boolean isUpdateFlag = this.accountwithdrawMapper.updateByExample(accountwithdraw, example) > 0 ? true : false;
					if (!isUpdateFlag) {
						throw new Exception("提现失败后,更新提现记录表失败" + "提现订单号:" + ordId + ",用户ID:" + userId);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param accountWithdraw
	 * @param userId
	 * @param ordId
	 * @return 已被更新false，未更新true
	 */
	private CheckResult checkConcurrencyDB(Accountwithdraw accountWithdraw, int userId, String ordId) {
		CheckResult result = new CheckResult();
		
		Boolean resultBool = true;
		String resultMsg = null;
		String msg = "此笔充提现状态已发生变化,提现订单号:" + ordId + ",用户Id:" + userId;
		
		//匹配验证
		// 如果信息已被处理
		if (accountWithdraw.getStatus() == WITHDRAW_STATUS_SUCCESS) {
			resultBool = false;
			resultMsg = msg;
		}
		// 查询是否已经处理过
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(ordId).andTradeEqualTo("cash_success");
		int accountlistCnt = this.accountListMapper.countByExample(accountListExample);
		// 如果信息已被处理
		if (accountlistCnt != 0) {
			resultBool = false;
			resultMsg = msg;
		}
		
		//匹配结果
	    result.setResultBool(resultBool);
	    result.setResultMsg(resultMsg);
		return result;
	}
	
	/**
	 * 银行接口返回与平台记录匹配验证
	 * @param bean
	 * @param accountWithdraw
	 * @return
	 */
	private CheckResult checkCallRetAndHyjf(BankCallBean bean, Accountwithdraw accountWithdraw) {
		CheckResult result = new CheckResult();
		
		Boolean resultBool = true;
		String resultMsg = null;
		
		//匹配验证
		//提现金额
		BigDecimal txAmount = new BigDecimal(bean.getTxAmount());
		if (!txAmount.equals(accountWithdraw.getCredited())) {
			resultBool = false;
			resultMsg = "本地记录的提现金额与银行返回的交易金额不一致:本地记录的提现金额:" + accountWithdraw.getTotal() + ",银行返回的充值金额:" + txAmount;
		}
		
		//匹配结果
	    result.setResultBool(resultBool);
	    result.setResultMsg(resultMsg);
		return result;
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
	 * 获取用户的提现费率
	 *
	 * @param userId
	 * @param bankId
	 * @param type
	 */
	private String getWithdrawFee(Integer userId, String bankId, BigDecimal amount) {
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
	
	private BankCard getBankInfo(Integer userId, String cardNo) {
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
	 * 根据用户Id查询用户的账户信息
	 * 
	 * @param userId
	 * @return
	 */
	private Account getAccount(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Account> list = this.accountMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}
}

