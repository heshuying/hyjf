package com.hyjf.batch.hjh.borrow.plancapital;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 汇计划资本预估统计(每日)
 * @author liubin
 *
 */
public class PlanCapitalTask {
	
	Logger _log = LoggerFactory.getLogger(PlanCapitalTask.class);
	
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private PlanCapitalService planCapitalService;
	
	/**
	 * 调用任务实际方法接口
	 */
	public void run() {
		process();
	}

	/**
	 * 汇计划资本预估统计(每日)
	 *
	 * @return
	 */
	private boolean process() {
		if (isRun == 0) {
			_log.info("汇计划资本预估统计(每日)任务 开始... ");
			
			isRun = 1;
			try {
				Boolean result = false;
				// 取得当前日期为基准日期
				Date nowDate = GetDate.stringToDate2(GetDate.dateToString2(new Date()));
//				Date nowDate = GetDate.stringToDate2("2018-01-06");
				// 取得前一天日期
				Date yDate = GetDate.countDate(nowDate, 5, -1);
                Date date9 = GetDate.countDate(nowDate, 5, 9);
				// 删除汇计划资本按天统计及预估表的昨天今天及之后9天的记录
                result = this.planCapitalService.deleteHjhPlanCapital(yDate, date9);
                if (!result){
                    _log.error("删除汇计划资本按天统计及预估表的昨天今天及之后9天的记录失败 日期："
                            + GetDate.dateToString2(yDate) + " ~ " + GetDate.dateToString2(date9) );
                }

				// 更新前一天的汇计划资本统计
				List<HjhPlanCapital> actList = this.planCapitalService.getPlanCapitalForActList(yDate);
				if (actList != null && actList.size() > 0) {
					for (HjhPlanCapital hjhPlanCapital :actList) {
						if (hjhPlanCapital.getDate() == null){
							continue;
						}
						// 插入更新汇计划资本按天统计及预估表
						result = this.planCapitalService.updatePlanCapital(hjhPlanCapital);

						if (!result){
							_log.error("汇计划资本预估统计(每日)任务 更新前一天的汇计划资本统计(实际) 失败 日期："
									+ GetDate.dateToString(hjhPlanCapital.getDate())
									+ " 计划编号：" + hjhPlanCapital.getPlanNid());
						}
					}
				}

				// 更新当日到后9日的汇计划资本预估
				List<HjhPlanCapital> proformaList = this.planCapitalService.getPlanCapitalForProformaList(nowDate, date9);
				if (proformaList != null && proformaList.size() > 0) {
					for (HjhPlanCapital hjhPlanCapital :proformaList) {
						if (hjhPlanCapital.getDate() == null){
							continue;
						}
						// 插入更新汇计划资本按天统计及预估表
						result = this.planCapitalService.updatePlanCapital(hjhPlanCapital);

						if (!result){
							_log.error("汇计划资本预估统计(每日)任务 更新当日及后9天的汇计划资本统计(预估) 失败 日期："
									+ GetDate.dateToString(hjhPlanCapital.getDate())
									+ " 计划编号：" + hjhPlanCapital.getPlanNid());
						}
					}
				}

				// 更新前一天的汇计划日交易量
				List<HjhAccountBalance> hjhAccountBalanceList = this.planCapitalService.getHjhAccountBalanceForActList(yDate);
				if (hjhAccountBalanceList != null && hjhAccountBalanceList.size() > 0) {
					for (HjhAccountBalance hjhAccountBalance :hjhAccountBalanceList) {
						if (hjhAccountBalance.getDate() == null){
							continue;
						}
						// 插入更新汇计划按日对账统计表
						result = this.planCapitalService.updateAccountBalance(hjhAccountBalance);

						if (!result){
							_log.error("汇计划资本预估统计(每日)任务 更新前一天的汇计划资本统计(实际) 失败 日期："
									+ GetDate.dateToString(hjhAccountBalance.getDate()));
						}
					}
				}
			} catch (Exception e) {
				_log.error("汇计划资本预估统计(每日)任务 失败... ");
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			
			_log.info("汇计划资本预估统计(每日)任务 结束... ");
			
		}else{
			
			_log.info("汇计划资本预估统计(每日)任务 正在运行... ");
		}
		
		return false;
	}
}
