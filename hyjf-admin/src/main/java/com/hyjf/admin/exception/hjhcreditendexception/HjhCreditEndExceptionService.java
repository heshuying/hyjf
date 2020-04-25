package com.hyjf.admin.exception.hjhcreditendexception;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;

/**
 * 汇计划加入明细Service
 * 
 * @ClassName AccedeListService
 * @author LIBIN
 * @date 2017年8月16日 上午9:47:35
 */
public interface HjhCreditEndExceptionService extends BaseService {
	
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
}
