package com.hyjf.batch.exception.account;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.customize.ExceptionAccountCustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.ExceptionAccount;
import com.hyjf.mybatis.model.auto.ExceptionAccountExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class ExceptionAccountServiceImpl extends BaseServiceImpl implements ExceptionAccountService {
	@Autowired
	private ExceptionAccountCustomizeMapper exceptionAccountCustomizeMapper;
	private final static int LIMIT = 1000;

	public void syncHuiFuAccounts() {
		// 数据总分页数量
		long page = 0;
		// 数据总量
		long count = exceptionAccountCustomizeMapper.countAllUserAccount();
		if (count > 0) {
			page = count / LIMIT + 1;
			for (int i = 0; i < page; i++) {
				try {
					long limitStart = i * LIMIT;
					// 1.找出所有已开户帐号
					List<ExceptionAccount> accounts = exceptionAccountCustomizeMapper.selectUserAccounts(limitStart, LIMIT);
					if (accounts != null && accounts.size() > 0) {
						for (ExceptionAccount exceptionAccount : accounts) {
							try {
								// 2. 获取到汇付数据
								ChinapnrBean result = this.userBalance(exceptionAccount);
								if(result!=null){
									// 3.更新时间过长，为了保证平台和汇付对比数据实时性，接收到汇付返回的数据时再次查询用户平台余额信息
									Account account = this.selectUserAccount(exceptionAccount.getUserId());
									if (account != null) {
										// 4.可用余额或冻结余额不一致的话插入到account_exception表中
										String balance_huifu = result.getAvlBal() == null ? "0.00" : result.getAvlBal();
										String frz_huifu = result.getFrzBal() == null ? "0.00" : result.getFrzBal();
										balance_huifu = StringUtils.replace(balance_huifu, ",", StringUtils.EMPTY);
										frz_huifu = StringUtils.replace(frz_huifu, ",", StringUtils.EMPTY);
										BigDecimal balance = Validator.isNotNull(account.getBalance()) ? account.getBalance() : BigDecimal.ZERO;
										BigDecimal planBalance = Validator.isNotNull(account.getPlanBalance()) ? account.getPlanBalance() : BigDecimal.ZERO;
										BigDecimal frost = Validator.isNotNull(account.getFrost()) ? account.getFrost() : BigDecimal.ZERO;
										BigDecimal planFrost = Validator.isNotNull(account.getPlanFrost()) ? account.getPlanFrost() : BigDecimal.ZERO;
										BigDecimal planBalanceHF = new BigDecimal(balance_huifu);
										BigDecimal planFrostHF = new BigDecimal(frz_huifu);
										if ((planBalanceHF.compareTo(balance.add(planBalance)) != 0) || (planFrostHF.compareTo(frost.add(planFrost)) != 0)) {
											exceptionAccount.setBalancePlat(balance.add(planBalance));
											exceptionAccount.setFrostPlat(frost.add(planFrost));
											exceptionAccount.setBalanceHuifu(new BigDecimal(balance_huifu));
											exceptionAccount.setFrostHuifu(new BigDecimal(frz_huifu));
											exceptionAccount.setCreateTime(GetDate.getMyTimeInMillis());
											// 校验错误数据当前是否存在
											List<ExceptionAccount> exceptionAccounts = this.selectUserExceptionAccount(exceptionAccount.getUserId());
											if (exceptionAccounts == null) {
												boolean exceptionAccountFlag = this.insertExceptionAccount(exceptionAccount);
												if (!exceptionAccountFlag) {
													throw new RuntimeException("插入相应的异常账户信息失败，异常账户userId：" + exceptionAccount.getUserId());
												}
											} else if (exceptionAccounts.size() == 0) {
												boolean exceptionAccountFlag = this.insertExceptionAccount(exceptionAccount);
												if (!exceptionAccountFlag) {
													throw new RuntimeException("插入相应的异常账户信息失败，异常账户userId：" + exceptionAccount.getUserId());
												}
											} else if (exceptionAccounts.size() == 1) {
												exceptionAccount.setId(exceptionAccounts.get(0).getId());
												boolean exceptionAccountFlag = this.updateExceptionAccount(exceptionAccount);
												if (!exceptionAccountFlag) {
													throw new RuntimeException("更新相应的异常账户信息失败，异常账户userId：" + exceptionAccount.getUserId());
												}
											} else {
												boolean exceptionAccountFlag = this.deleteAndInsertExceptionAccount(exceptionAccount);
												if (!exceptionAccountFlag) {
													throw new RuntimeException("更新相应的异常账户信息失败，异常账户userId：" + exceptionAccount.getUserId());
												}
											}
											System.out.println("用户" + exceptionAccount.getUsername() + "账户异常,用户userId:" + exceptionAccount.getUserId());
										} else {
											ExceptionAccountExample exceptionAccountExample = new ExceptionAccountExample();
											ExceptionAccountExample.Criteria exceptionAccountCra = exceptionAccountExample.createCriteria();
											exceptionAccountCra.andUserIdEqualTo(exceptionAccount.getUserId());
											exceptionAccountMapper.deleteByExample(exceptionAccountExample);
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						throw new RuntimeException("查询相应的账户信息失败，分页数：" + page);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean deleteAndInsertExceptionAccount(ExceptionAccount exceptionAccount) {
		ExceptionAccountExample exceptionAccountExample = new ExceptionAccountExample();
		ExceptionAccountExample.Criteria exceptionAccountCra = exceptionAccountExample.createCriteria();
		exceptionAccountCra.andUserIdEqualTo(exceptionAccount.getUserId());
		boolean deleteFlag = exceptionAccountMapper.deleteByExample(exceptionAccountExample) > 0 ? true : false;
		if (deleteFlag) {
			boolean insertFlag = exceptionAccountMapper.insertSelective(exceptionAccount) > 0 ? true : false;
			if (insertFlag) {
				return true;
			} else {
				throw new RuntimeException("插入相应的异常账户信息失败，用户userId:" + exceptionAccount.getUserId());
			}
		} else {
			throw new RuntimeException("删除异常账户信息exceptionaccount错误,用户userId:" + exceptionAccount.getUserId());
		}
	}

	private boolean updateExceptionAccount(ExceptionAccount exceptionAccount) {
		boolean exceptionAccountFlag = exceptionAccountMapper.updateByPrimaryKeySelective(exceptionAccount) > 0 ? true : false;
		return exceptionAccountFlag;
	}

	private boolean insertExceptionAccount(ExceptionAccount exceptionAccount) {
		boolean exceptionAccountFlag = exceptionAccountMapper.insertSelective(exceptionAccount) > 0 ? true : false;
		return exceptionAccountFlag;
	}

	/**
	 * 查询用户异常账户信息
	 * 
	 * @param userId
	 * @return
	 */
	private List<ExceptionAccount> selectUserExceptionAccount(Integer userId) {
		ExceptionAccountExample exceptionAccountExample = new ExceptionAccountExample();
		ExceptionAccountExample.Criteria exceptionAccountCra = exceptionAccountExample.createCriteria();
		exceptionAccountCra.andUserIdEqualTo(userId);
		List<ExceptionAccount> exceptionAccounts = this.exceptionAccountMapper.selectByExample(exceptionAccountExample);
		return exceptionAccounts;
	}

	/**
	 * 查詢用戶的账户信息
	 * 
	 * @param userId
	 * @return
	 */
	private Account selectUserAccount(Integer userId) {
		
		try {
			AccountExample example = new AccountExample();
			AccountExample.Criteria cra = example.createCriteria();
			cra.andUserIdEqualTo(userId);
			List<Account> accounts = accountMapper.selectByExample(example);
			if (accounts != null && accounts.size() == 1) {
				return accounts.get(0);
			} else {
				throw new RuntimeException("用户账户信息account错误,用户userId:" + userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询用户的汇付账户信息
	 * 
	 * @param exceptionAccount
	 * @return
	 */
	private ChinapnrBean userBalance(ExceptionAccount exceptionAccount) {
		// 2.调用汇付接口获取账户金额信息
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion("10");
		bean.setCmdId("QueryBalanceBg");
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
		bean.setUsrCustId(String.valueOf(exceptionAccount.getCustomId()));
		try {
			ChinapnrBean result = ChinapnrUtil.callApiBg(bean);
			if (result != null) {
				if (StringUtils.isNotBlank(result.getRespCode())) {
					if (result.getRespCode().equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
						return result;
					} else {
						throw new RuntimeException("调用汇付账户对账接口失败,返回码为：result.getRespCode(),用户userId:" + exceptionAccount.getUserId());
					}
				} else {
					throw new RuntimeException("调用汇付账户对账接口失败,返回码为空,用户userId:" + exceptionAccount.getUserId());
				}
			} else {
				throw new RuntimeException("未查询到相应的汇付账户信息,用户userId:" + exceptionAccount.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
