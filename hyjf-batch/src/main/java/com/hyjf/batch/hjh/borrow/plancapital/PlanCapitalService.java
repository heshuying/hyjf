package com.hyjf.batch.hjh.borrow.plancapital;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;

import java.util.Date;
import java.util.List;

public interface PlanCapitalService extends BaseService {

	/**
	 * 获取该日期的实际债转和复投金额
	 * @param date
	 * @return
	 */
	List<HjhPlanCapital> getPlanCapitalForActList(Date date);

	/**
	 * 获取该期间的预估债转和复投金额
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	List<HjhPlanCapital> getPlanCapitalForProformaList(Date fromDate, Date toDate);

	/**
	 * 插入更新汇计划资本按天统计及预估表
	 * @param hjhPlanCapital
	 * @return
	 */
	Boolean updatePlanCapital(HjhPlanCapital hjhPlanCapital);

	/**
	 *获取该期间的汇计划日交易量
	 * @param date
	 * @return
	 */
	List<HjhAccountBalance> getHjhAccountBalanceForActList(Date date);

	/**
	 * 插入更新汇计划按日对账统计表
	 * @param hjhAccountBalance
	 * @return
	 */
	Boolean updateAccountBalance(HjhAccountBalance hjhAccountBalance);

	/**
	 * 删除汇计划资本按天统计及预估表
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	Boolean deleteHjhPlanCapital(Date dateFrom, Date dateTo);
}
