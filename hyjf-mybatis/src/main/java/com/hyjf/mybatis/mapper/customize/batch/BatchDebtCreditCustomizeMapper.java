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

import com.hyjf.mybatis.model.auto.DebtCredit;

public interface BatchDebtCreditCustomizeMapper {

	/**
	 * 查询用户可承接的清算债转数据
	 * @param debtCreditParams
	 * @return
	 */
		
	List<DebtCredit> selectRuleDebtCredits(Map<String, Object> debtCreditParams);

	/**
	 * 查询相应的无规则债券信息
	 * @param debtCreditParams
	 * @return
	 */
	List<DebtCredit> selectUnruleDebtCredits(Map<String, Object> debtCreditParams);

	/**
	 * 查询所有的债权
	 * @param debtCreditParams
	 * @return
	 */
	int countDebtCreditsAll(Map<String, Object> debtCreditParams);

	/**
	 * 
	 * @param planNid
	 * @return
	 */
	BigDecimal countDebtCreditSum(Map<String, Object> debtCreditParams);

}
