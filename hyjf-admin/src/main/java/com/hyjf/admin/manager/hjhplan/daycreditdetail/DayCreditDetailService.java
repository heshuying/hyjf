package com.hyjf.admin.manager.hjhplan.daycreditdetail;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;

import java.util.List;
import java.util.Map;

public interface DayCreditDetailService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param params
	 * @return
	 */
	public Integer countDebtCredit(Map<String, Object> params);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<HjhDebtCreditCustomize> selectDebtCreditList(Map<String, Object> params);

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId);

	/**
	 * 还款方式
	 * @param string
	 * @return
	 */
	public List<BorrowStyle> selectBorrowStyleList(String string);

	/**
	 * 总计
	 * @param params
	 * @return
	 */
    Map<String,Object> sumRecord(Map<String, Object> params);
}
