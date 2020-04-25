package com.hyjf.batch.exception.chongzhensearch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.file.FileUtil;
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
public class ChongZhenSearchServiceImpl extends BaseServiceImpl implements ChongZhenSearchService {
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
//		List<Integer> status = new ArrayList<Integer>();
//		status.add(0);
//		status.add(1);
//		status.add(3);
		cra.andStatusEqualTo(2);// 提现状态为成功
		cra.andBankFlagEqualTo(1);// 提现平台:江西银行
		// 当前时间
		cra.andAddtimeGreaterThanOrEqualTo(String.valueOf(1502344800));// TODO T-1天之前
		cra.andAddtimeLessThanOrEqualTo(String.valueOf(1502357400));// 30分钟之前的充值订单TODO
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
				if ((BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) 
						&& "00".equals(bean.getResult())
						&& ("1".equals(bean.getOrFlag()))) {
					String str = accountwithdraw.getId()+ "," +
							accountwithdraw.getNid()+ "," +
							accountwithdraw.getUserId()+ "," +
							bean.getOrFlag() + "\r\n";
					
					FileUtil.writeFile("123test.txt", str.getBytes(), true);
				}
			}else{
				throw new Exception("调用银行接口,银行返回NULL,充值订单号:" 
									+ accountwithdraw.getNid() 
									+ ",用户Id:" + accountwithdraw.getUserId());
			}
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "bankCallFundTransQuery", e);
		}
	}
	
	public static void main(String[] args) {
		String str = "123321123a" + "\r\n";
		FileUtil.writeFile("123test.txt", str.getBytes(), true);
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

