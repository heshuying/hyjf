package com.hyjf.batch.htj.liquidatewill;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;

public interface LiquidateWillService extends BaseService {
/**
 * 
 * @method: selectLiquidateWill
 * @description: 搜索出明日即将清算的计划			
 *  @return 
 * @return: List<DebtPlan>
* @mender: zhouxiaoshuai
 * @date:   2016年12月12日 下午1:55:08
 */
public List<DebtPlan> selectLiquidateWill();
/**
 * 
 * @method: selectAllExFireValue
 * @description: 	查询计划下所有的订单		
 *  @param debtPlanNid 
 * @return: void
* @mender: zhouxiaoshuai
 * @date:   2016年12月15日 下午2:52:23
 */
public List<DebtPlanAccede> selectAllExFireValue(String debtPlanNid);
}
