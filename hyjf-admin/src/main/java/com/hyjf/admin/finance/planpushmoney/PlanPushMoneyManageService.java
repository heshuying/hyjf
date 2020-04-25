package com.hyjf.admin.finance.planpushmoney;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtAccedeCommission;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.admin.AdminPlanPushMoneyDetailCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

/**
 * 汇添金提成管理Service
 * 
 * @ClassName PlanPushMoneyManageService
 * @author liuyang
 * @date 2016年10月24日 上午9:35:09
 */
public interface PlanPushMoneyManageService extends BaseService {

	/**
	 * 获取锁定中的计划列表
	 * 
	 * @Title selectLockPlanList
	 * @param form
	 * @return
	 */
	public List<DebtPlan> selectLockPlanList(PlanPushMoneyManageBean form);

	/**
	 * 汇添金计划件数
	 * 
	 * @Title countLockPlanList
	 * @param form
	 * @return
	 */
	public int countLockPlanList(PlanPushMoneyManageBean form);

	/**
	 * 根据计划编号检索计划
	 * 
	 * @Title selectDebtPlanByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	public DebtPlan selectDebtPlanByDebtPlanNid(String debtPlanNid);

	/**
	 * 更新汇添金提成表
	 * 
	 * @Title insertAccedeCommissionRecord
	 * @param form
	 * @return
	 */
	public int insertAccedeCommissionRecord(PlanPushMoneyManageBean form);

	/**
	 * 检索提成明细列表
	 * 
	 * @Title selectDebtAccedeCommission
	 * @param form
	 * @return
	 */
	public List<AdminPlanPushMoneyDetailCustomize> selectDebtAccedeCommission(PlanPushMoneyManageBean form);

	/**
	 * 检索提成明细件数
	 * 
	 * @Title countRecordTotal
	 * @param form
	 * @return
	 */
	public int countRecordTotal(PlanPushMoneyManageBean form);

	/**
	 * 根据id,加入订单号检索提成信息
	 * 
	 * @Title selectAccedeCommissionByIdAndAccedeOrderId
	 * @param ids
	 * @param accedeOrderId
	 * @return
	 */
	public DebtAccedeCommission selectAccedeCommissionByIdAndAccedeOrderId(String ids, String accedeOrderId);

	/**
	 * 根据用户id查询其在crm中的员工属性
	 * 
	 * @param id
	 * @return
	 */
	public Integer queryCrmCuttype(Integer userid);

	/**
	 * 发提成处理
	 * 
	 * @Title updateAccedeCommissionRecord
	 * @param commission
	 * @param chinapnrBean
	 * @return
	 */
	public int updateAccedeCommissionRecord(DebtAccedeCommission commission, ChinapnrBean chinapnrBean);
	
	/**
	 * 发提成出错是处理
	 * @Title updateAccedeCommissoinRecordError
	 * @param commission
	 * @param chinapnrBean
	 * @return
	 */
	public int updateAccedeCommissoinRecordError(DebtAccedeCommission commission, ChinapnrBean chinapnrBean);
}
