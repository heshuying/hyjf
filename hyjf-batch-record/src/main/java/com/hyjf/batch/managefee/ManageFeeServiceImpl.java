package com.hyjf.batch.managefee;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.Users;

/**
 * 债权迁移后,更新借款人账户信息
 * 
 * @author liuyang
 *
 */
@Service
public class ManageFeeServiceImpl extends CustomizeMapper implements ManageFeeService {
	/**
	 * 检索借款人列表
	 * 
	 * @return
	 */
	@Override
	public List<Users> searchBorrowUsersList() {
		return this.borrowUserCustomizeMapper.searchBorrowUserList();
	}

	/**
	 * 更新借款人账户信息
	 * 
	 * @param users
	 * @throws Exception
	 */
	@Override
	public void updateBorrowUserAccount(Users users) throws Exception {
		// 借款人用户ID
		Integer borrowUserId = users.getUserId();
		// 根据用户ID查询用户账户信息
		Account account = this.isExistAccount(borrowUserId);
		if (account == null) {
			throw new Exception("用户账户信息不存在,用户Id:" + borrowUserId);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", String.valueOf(borrowUserId));
		// 借款人待还
		BigDecimal borrowUserBankWaitRepay = this.borrowUserCustomizeMapper.getBorrowUserBankWaitRepay(param);
		if (borrowUserBankWaitRepay != null) {
			borrowUserBankWaitRepay = borrowUserBankWaitRepay.setScale(2, BigDecimal.ROUND_DOWN);
			if (borrowUserBankWaitRepay.compareTo(BigDecimal.ZERO) > 0) {
				Account newAccount = new Account();
				newAccount.setUserId(borrowUserId);
				newAccount.setBankWaitRepay(borrowUserBankWaitRepay);
				boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateBorrowUserBankWaitRepay(newAccount) > 0 ? true : false;
				if (!isUpdateFlag) {
					throw new Exception("更新借款人账户信息失败~~~~,借款人用户ID:" + borrowUserId);
				}
			}
		}
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
