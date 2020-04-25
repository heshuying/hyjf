package com.hyjf.batch.htj.sendagreement;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanAccedeCustomize;

/**
 * 汇添金自动发送协议Service
 * 
 * @ClassName AutoSendAgreementService
 * @author liuyang
 * @date 2016年10月18日 上午9:29:08
 */
public interface AutoSendAgreementService extends BaseService {

	/**
	 * 获取锁定中的计划列表
	 * 
	 * @Title selectLockPlanList
	 * @return
	 */
	public List<DebtPlan> selectLockPlanList();

	/**
	 * 根据计划nid获取计划的加入列表
	 * 
	 * @Title selectPlanAccede
	 * @param planNid
	 * @return
	 */
	public List<DebtPlanAccede> selectPlanAccede(String planNid);

	/**
	 * 根据计划编号,订单号检索加入信息
	 * 
	 * @Title selectPlanAccedeInfo
	 * @param planAccedeInfo
	 * @return
	 */
	public BatchDebtPlanAccedeCustomize selectPlanAccedeInfo(DebtPlanAccede planAccedeInfo);
	

    /**
     * 发送邮件(出借成功)
     *
     * @param userId
     */
    public void sendMail(BatchDebtPlanAccedeCustomize batchDebtPlanAccedeCustomize);
    
}
