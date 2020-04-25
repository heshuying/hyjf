package com.hyjf.batch.htj.planActualApr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.DebtPlan;

public class PlanActualAprTask {

    /** 运行状态 */
    private static int isRun = 0;

    @Autowired
    PlanActualAprService planActualAprService;

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    @Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailMessageProcesser;

    public void run() {
        // 调用计划锁定中的当前年化接口
    	ActualApr();
    }

    /**
     * 调用放款接口
     *
     * @return
     */
    private boolean ActualApr() {
        if (isRun == 0) {
            isRun = 1;
            System.out.println("开始调用计划锁定中的当前年化接口");
            try {
            	//计划计算当前年化
            	List<DebtPlan> planLockList=planActualAprService.selectPlanAllInLock();
            	if (planLockList!=null&&planLockList.size()>0) {
            		for (int i = 0; i < planLockList.size(); i++) {
            			DebtPlan debtPlan=planLockList.get(i);
            			String planNid=debtPlan.getDebtPlanNid();
            			boolean aprFlag=planActualAprService.updatePlanActualApr(planNid)>0?true:false;
            			if (!aprFlag) {
            				System.out.println("计划计算当前年化未更改此次计划的当前年化，计划编号:"+planNid);
						}
					}
            		System.out.println("计划计算当前年化调用结束");
				}else {
					System.out.println("计划计算当前年化未查询到数据退出");
				}
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
        }
        return false;
    }

}
