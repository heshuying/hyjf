package com.hyjf.admin.manager.hjhplan.daycreditdetail;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DayCreditDetailServiceImpl extends BaseServiceImpl implements DayCreditDetailService {

	/**
	 * COUNT
	 * 
	 * @param DebtCustomize
	 * @return
	 */
	public Integer countDebtCredit(Map<String, Object> params) {
		return this.hjhdebtCreditCustomizeMapper.countDebtCredit(params);
	}

	/**
	 * 汇转让列表
	 * 
	 * @return
	 */
	public List<HjhDebtCreditCustomize> selectDebtCreditList(Map<String, Object> params) {
		return this.hjhdebtCreditCustomizeMapper.selectDebtCreditList(params);
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

	/**
	 * 总计
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> sumRecord(Map<String, Object> params) {
		return this.hjhdebtCreditCustomizeMapper.selectSumRecord(params);
	}

}
