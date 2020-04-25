package com.hyjf.batch.interestsum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

/**
 * 债权迁移后,更新累计收益
 * 
 * @author liuyang
 *
 */
@Service
public class InterestSumServiceImpl extends CustomizeMapper implements InterestSumService {
	private static final Logger LOG = LoggerFactory.getLogger(InterestSumServiceImpl.class);

	@Override
	public Integer countOpenAccountUsers() {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andOpenAccountEqualTo(1);// 已开户
		cra.andIsDataUpdateEqualTo(0);// 未同步
		return this.usersMapper.countByExample(example);
	}

	/**
	 * 获取已开户用户列表
	 * 
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<Users> selectOpenAccountUsers(int limitStart, int limitEnd) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andOpenAccountEqualTo(1);// 已开户
		cra.andIsDataUpdateEqualTo(0);// 未同步
		/*example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);*/
		return this.usersMapper.selectByExample(example);
	}

	/**
	 * 更新用户的累计收益
	 * 
	 * @param users
	 * @throws Exception
	 */
	@Override
	public Integer updateInterestSum(List<Users> openAccountList) throws RuntimeException {
		Integer successCount = 0;
		try {
			for (Users users : openAccountList) {

				// 用户Id
				Integer userId = users.getUserId();
				Map<String, String> param = new HashMap<String, String>();
				param.put("userId", String.valueOf(userId));
				// 根据用户Id查询用户汇天利的累计收益
				BigDecimal htlInterestSum = this.interestSumCustomizeMapper.getHtlInvestInterestSum(param);
				// 查询用户债转的累计收益
				BigDecimal hzrInterestSum = this.interestSumCustomizeMapper.getHzrInvestInterestSum(param);
				// 查询用户汇直投的累计收益
				BigDecimal hztInterestSum = this.interestSumCustomizeMapper.getHztInvestInterestSum(param);
				// 查询用户出让的累计收益
				BigDecimal hztCreditInterestSum = this.interestSumCustomizeMapper.getHztCreditInterestSum(param);
				// 查询用户融通宝加息部分的收益
				BigDecimal increaseInterestSum = this.interestSumCustomizeMapper.getIncreaseInterestSum(param);
				// 查询优惠券已还部分的收益
				BigDecimal couponInterestSum = this.interestSumCustomizeMapper.getCouponInterestSum(param);
				// 出让人出让债权承接收到的垫付利息
				BigDecimal creditInterestAdvance = this.interestSumCustomizeMapper.getHzrCreditInterestAdvance(param);
				// 总的累计收益
				BigDecimal totalInterestSum = (htlInterestSum.add(hzrInterestSum).add(increaseInterestSum)
						.add(couponInterestSum).add(hztInterestSum).add(creditInterestAdvance)
						.subtract(hztCreditInterestSum)).setScale(2, BigDecimal.ROUND_DOWN);
				LOG.info("用户ID:" + userId + ",累计收益:" + totalInterestSum);
				if (totalInterestSum.compareTo(BigDecimal.ZERO) > 0) {

					// 判断用户账户是否存在
					Account account = this.isExistAccount(userId);
					if (account == null) {
						throw new RuntimeException("用户账户信息不存在,用户ID:" + userId);
					}
					// 更新用户的累计收益
					Account newAccount = new Account();
					newAccount.setUserId(userId);
					newAccount.setBankInterestSum(totalInterestSum);
					boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateUserAccountInterestSum(newAccount) > 0
							? true : false;
					if (!isUpdateFlag) {
						throw new RuntimeException("更新用户的累计收益失败~~~~,userId = " + userId);
					}
					// 更新用户表
					users.setIsDataUpdate(1);
					boolean isUsersUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
					if (!isUsersUpdateFlag) {
						throw new RuntimeException("用户账户更新后,更新users表的is_data_update失败~~~~,userId = " + userId);
					}
					successCount++;

				} else {
					LOG.info("累计收益为0,更新用户的更新标示,用户ID:" + userId);
					// 更新用户表
					users.setIsDataUpdate(1);
					boolean isUsersUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
					if (!isUsersUpdateFlag) {
						throw new RuntimeException("用户账户更新后,更新users表的is_data_update失败~~~~,userId = " + userId);
					}
					successCount++;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("累计收益更新失败，抛出异常，事物回滚~~~~");
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
