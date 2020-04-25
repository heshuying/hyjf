package com.hyjf.admin.manager.plan.credit;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;

@Service
public class PlanCreditServiceImpl extends BaseServiceImpl implements PlanCreditService {

	/**
	 * COUNT
	 * 
	 * @param DebtCustomize
	 * @return
	 */
	public Integer countDebtCredit(Map<String, Object> params) {
		return this.debtCreditCustomizeMapper.countDebtCredit(params);
	}

	/**
	 * 汇转让列表
	 * 
	 * @return
	 */
	public List<DebtCreditCustomize> selectDebtCreditList(Map<String, Object> params) {
		return this.debtCreditCustomizeMapper.selectDebtCreditList(params);
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		return this.usersMapper.selectByExample(example).get(0);
	}
	
	/**
	 * 还款方式
	 * 
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowStyle> selectBorrowStyleList(String nid) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
		if (StringUtils.isNotEmpty(nid)) {
			cra.andNidEqualTo(nid);
		}
		return this.borrowStyleMapper.selectByExample(example);
	}
	
}
