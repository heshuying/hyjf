package com.hyjf.batch.invest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

@Service
public class BankInvestSumServiceImpl extends CustomizeMapper implements BankInvestSumService {

	/**
	 * 检索已在汇付开户的用户总数
	 * 
	 * @return
	 */
	@Override
	public Integer countOpenAccountUsers() {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andOpenAccountEqualTo(1);// 汇付已开户
		cra.andIsDataUpdateEqualTo(1);// 1:累计收益已同步
		return this.usersMapper.countByExample(example);
	}

	@Override
	public List<Users> selectOpenAccountUsers(int limitStart, int limitEnd) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andOpenAccountEqualTo(1);// 汇付已开户
		cra.andIsDataUpdateEqualTo(1);// 1:累计收益已同步
		/*example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);*/
		return this.usersMapper.selectByExample(example);
	}

	/**
	 * 更新用户的累计出借
	 * 
	 * @param users
	 */
	@Override
	public Integer updateBankInvestSum(List<Users> openAccountList) throws RuntimeException {
		Integer successCount = 0;
		try {
			for (Users users : openAccountList) {
				// 用户Id
				Integer userId = users.getUserId();
				// 根据用户Id查询用户的累计出借
				Map<String, String> param = new HashMap<String, String>();
				param.put("userId", String.valueOf(userId));
				// 汇直投出借金额
				BigDecimal hztTenderAccount = this.bankInvestSumCustomizeMapper.getHZTTenderAccountSum(param);
				// 汇消费出借金额
				BigDecimal hxfTenderAccount = this.bankInvestSumCustomizeMapper.getHXFTenderAccountSum(param);
				// 汇天利出借金额
				BigDecimal htlTenderAccount = this.bankInvestSumCustomizeMapper.getHTLTenderAccountSum(param);
				// 汇转让出借金额
				BigDecimal hzrTenderAccount = this.bankInvestSumCustomizeMapper.getHZRTenderAccountSum(param);
				// 汇添金出借金额
				BigDecimal htjTenderAccount = this.bankInvestSumCustomizeMapper.getHTJTenderAccountSum(param);
				// 总的出借总额
				BigDecimal investSum = hztTenderAccount.add(hxfTenderAccount).add(htlTenderAccount)
						.add(hzrTenderAccount).add(htjTenderAccount);
				// 更新用户的出借总额
				if (investSum.compareTo(BigDecimal.ZERO) > 0) {

					// 根据用户Id查询用户账户信息
					Account account = this.isExistAccount(userId);
					if (account == null) {
						throw new RuntimeException("用户账户信息不存在,用户ID:" + userId);
					}
					Account newAccount = new Account();
					newAccount.setUserId(userId);
					newAccount.setBankInvestSum(investSum);
					boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateUserAccountInvestSum(newAccount) > 0
							? true : false;
					if (!isUpdateFlag) {
						throw new RuntimeException("更新出借用户的账户信息失败~~~~,用户ID:" + userId);
					}
					users.setIsDataUpdate(2);
					boolean isUsersUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
					if (!isUsersUpdateFlag) {
						throw new RuntimeException("更新出借用户的账户信息后,更新users表的 is_data_update失败~~~~,用户Id:" + userId);
					}
					successCount++;

				} else {

					users.setIsDataUpdate(2);
					boolean isUsersUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
					if (!isUsersUpdateFlag) {
						throw new RuntimeException("更新出借用户的账户信息后,更新users表的 is_data_update失败~~~~,用户Id:" + userId);
					}
					successCount++;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("累计出借更新失败，抛出异常，事物回滚~~~~");
		}
		return successCount;
	}

	private Account isExistAccount(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Account> account = this.accountMapper.selectByExample(example);
		if (account != null && account.size() == 1) {
			return account.get(0);
		}
		return null;
	}
}
