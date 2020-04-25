/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.mybatis.mapper.customize.batch;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanBorrowCustomize;

public interface BatchDebtPlanBorrowCustomizeMapper {

	/**
	 * 查询用户可出借的本计划关联汇添金专属标的
	 * 
	 * @param debtCreditParams
	 * @return
	 */

	List<BatchDebtPlanBorrowCustomize> selectRuleDebtPlanBorrow(Map<String, Object> params);

	/**
	 * 查询用户未出借过的相应汇添金标的的可投总额
	 * 
	 * @param params
	 * @return
	 */
	BigDecimal selectRuleDebtPlanBorrowSum(Map<String, Object> params);
	
	/**
	 * 查询相应的汇天金无规则标的
	 * 
	 * @param debtCreditParams
	 * @return
	 */
	List<BatchDebtPlanBorrowCustomize> selectUnruleDebtPlanBorrow(Map<String, Object> debtCreditParams);

	/**
	 * 查询相应的无规则的标的总额
	 * 
	 * @param params
	 * @return
	 */

	BigDecimal selectUnrulePlanBorrowSum(Map<String, Object> params);

	/**
	 * 查询相应的可复投标的
	 * 
	 * @param debtCreditParams
	 * @return
	 */

	List<BatchDebtPlanBorrowCustomize> selectReInvestDebtPlanBorrow(Map<String, Object> debtCreditParams);

	/**
	 * 查询相应的可复投的标的总额
	 * 
	 * @param params
	 * @return
	 */

	BigDecimal selectReInvestPlanBorrowSum(Map<String, Object> params);

	/**
	 * 查询相应的两天未满标的汇天金专属标的
	 * 
	 * @param params
	 * @return
	 */

	List<BatchDebtPlanBorrowCustomize> selectDebtPlanBorrowUnfull(@Param("num") int num);

}
