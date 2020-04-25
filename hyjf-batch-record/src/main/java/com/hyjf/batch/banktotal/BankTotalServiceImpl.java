package com.hyjf.batch.banktotal;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

@Service
public class BankTotalServiceImpl extends CustomizeMapper implements BankTotalService {

	/**
	 * 查询已开户用户数量
	 * 
	 * @return
	 */
	@Override
	public Integer countOpenAccountUserCount() {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andOpenAccountEqualTo(1);// 汇付已开户
		cra.andIsDataUpdateEqualTo(2);// 累计出借已更新
		return this.usersMapper.countByExample(example);
	}

	/**
	 * 分页查询已开户用户的列表
	 * 
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<Users> selectOpenAccountUsers(int limitStart, int limitEnd) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andOpenAccountEqualTo(1); // 汇付已开户
		cra.andIsDataUpdateEqualTo(2);// 累计出借已更新
		example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);
		return this.usersMapper.selectByExample(example);
	}

	/**
	 * 更新用户的账户信息
	 * 
	 * @param users
	 * @throws Exception
	 */
	@Override
	public Integer updateUserAccountBankTotal(List<Users> openAccountList) throws Exception {
		Integer successCount = 0;
		for (Users users : openAccountList) {
			try {
				// 用户Id
				Integer userId = users.getUserId();
				// 根据用户Id查询用户账户信息
				Account account = this.isExistAccount(userId);
				if (account == null) {
					throw new Exception("用户账户信息不存在,用户Id:" + userId);
				}
				// 更新用户账户信息
				Account newAccount = new Account();
				newAccount.setUserId(userId);
				boolean isUpdateAccountFlag = this.adminAccountCustomizeMapper.updateBankTotal(newAccount) > 0 ? true : false;
				if (!isUpdateAccountFlag) {
					throw new Exception("更新用户账户总资产失败~~~~,用户Id:" + userId);
				}
				// 更新用户表
				users.setIsDataUpdate(3);
				boolean isUserUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
				if (!isUserUpdateFlag) {
					throw new Exception("更新用户账户后,更新users表失败~~~~~,用户Id:" + userId);
				}
				successCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
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
