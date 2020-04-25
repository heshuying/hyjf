package com.hyjf.admin.manager.borrow.credit;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;

public interface HjhDebtCreditService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
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

	public List<BorrowStyle> selectBorrowStyleList(String string);

	/**
	 * 查询展示列表的合计数据
	 * @param params
	 * @return
     */
	public Map<String,Object> selectDebtCreditTotal(Map<String,Object> params);

}
