package com.hyjf.batch.increaseinterestsub;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoanExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

/**
 * 加息还款统计累计收益
 * 
 * @author liuyang
 *
 */
@Service
public class IncreaseInterestSubtractServiceImpl extends CustomizeMapper implements IncreaseInterestSubtractService {

	/**
	 * 检索未还款的加息收益
	 * 
	 * @return
	 */
	@Override
	public List<IncreaseInterestLoan> searchIncreaseInterestLoanList() {
		IncreaseInterestLoanExample example = new IncreaseInterestLoanExample();
		IncreaseInterestLoanExample.Criteria cra = example.createCriteria();
		cra.andRepayStatusEqualTo(0);
		return this.increaseInterestLoanMapper.selectByExample(example);
	}

	/**
	 * 循环更新出借人账户信息
	 * 
	 * @param increaseInterestLoan
	 * @throws Exception
	 */
	@Override
	public void updateTenderUserAccount(List<IncreaseInterestLoan> increaseInterestList){
	    if (increaseInterestList != null && increaseInterestList.size() > 0) {
            for (IncreaseInterestLoan increaseInterestLoan : increaseInterestList) {
                try {
                    // 取得出借用户ID
                    Integer tenderUserId = increaseInterestLoan.getUserId();
                    // 根据用户ID查询用户是否存在
                    Users users = this.selectUsersByUserId(tenderUserId);
                    if (users != null) {
                        // 如果待还利息大于0,更新用户的账户信息
                        if (increaseInterestLoan.getRepayInterestWait().compareTo(BigDecimal.ZERO) > 0) {
                            Account newAccount = new Account();
                            newAccount.setUserId(tenderUserId);
                            newAccount.setBankTotal(increaseInterestLoan.getRepayInterestWait());// 账户总资产
                            newAccount.setBankAwait(increaseInterestLoan.getRepayInterestWait());// 待收总额
                            newAccount.setBankAwaitInterest(increaseInterestLoan.getRepayInterestWait());// 待收收益
                            // 更新出借人加息还款的银行总资产,待收总额,待收收益
                            boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateIncreaseInterestSubtract(newAccount) > 0 ? true : false;
                            if (!isUpdateFlag) {
                                throw new RuntimeException("债权迁移后,更新出借人加息还款的银行总资产,待收总额,待收收益失败,出借用户ID:" + tenderUserId + ",出借订单号:" + increaseInterestLoan.getInvestOrderId());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}

	private Users selectUsersByUserId(Integer userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Users> userList = usersMapper.selectByExample(example);
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		}
		return null;
	}

	/**
	 * 检索2017-07-03 00:00:00 之后的还款
	 * @return
	 */
	@Override
	public List<IncreaseInterestLoan> searchIncreaseInterestLoanRepayList() {
		IncreaseInterestLoanExample example = new IncreaseInterestLoanExample();
		IncreaseInterestLoanExample.Criteria cra = example.createCriteria();
		cra.andRepayActionTimeGreaterThanOrEqualTo(String.valueOf(1499011200));
		return this.increaseInterestLoanMapper.selectByExample(example);
	}

	/**
	 * 更新2017-07-03 00:00:00之后的还款
	 * @param increaseInterestList
	 */
	@Override
	public void updateIncreaseInterestRepayList(List<IncreaseInterestLoan> increaseInterestList) {
		// TODO Auto-generated method stub
	    if (increaseInterestList != null && increaseInterestList.size() > 0) {
            for (IncreaseInterestLoan increaseInterestLoan : increaseInterestList) {
                try {
                    // 取得出借用户ID
                    Integer tenderUserId = increaseInterestLoan.getUserId();
                    // 根据用户ID查询用户是否存在
                    Users users = this.selectUsersByUserId(tenderUserId);
                    if (users != null) {
                        // 如果待还利息大于0,更新用户的账户信息
                        if (increaseInterestLoan.getRepayInterestYes().compareTo(BigDecimal.ZERO) > 0) {
                            Account newAccount = new Account();
                            newAccount.setUserId(tenderUserId);
                            newAccount.setBankTotal(increaseInterestLoan.getRepayInterestYes());// 账户总资产
                            newAccount.setBankAwait(increaseInterestLoan.getRepayInterestYes());// 待收总额
                            newAccount.setBankAwaitInterest(increaseInterestLoan.getRepayInterestYes());// 待收收益
                            // 更新出借人加息还款的银行总资产,待收总额,待收收益
                            boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateIncreaseInterestSubtract(newAccount) > 0 ? true : false;
                            if (!isUpdateFlag) {
                                throw new RuntimeException("债权迁移后,更新出借人加息还款的银行总资产,待收总额,待收收益失败,出借用户ID:" + tenderUserId + ",出借订单号:" + increaseInterestLoan.getInvestOrderId());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
