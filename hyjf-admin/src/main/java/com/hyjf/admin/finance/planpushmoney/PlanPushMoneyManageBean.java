package com.hyjf.admin.finance.planpushmoney;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.admin.AdminPlanPushMoneyDetailCustomize;

/**
 * 汇添金提成管理Bean
 * 
 * @ClassName PlanPushMoneyManageBean
 * @author liuyang
 * @date 2016年10月24日 上午9:31:05
 */
public class PlanPushMoneyManageBean implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 2371223027640268180L;

	/**
	 * 检索结果列表
	 */
	private List<DebtPlan> recordList;

	/**
	 * 提成明细结果列表
	 */
	private List<AdminPlanPushMoneyDetailCustomize> pushMoneyDetailList;

	/**
	 * 部门id
	 */
	private String depIds;
	/**
	 * 计划编号(检索用)
	 */
	private String debtPlanNidSrch;

	/**
	 * 计划名称(检索用)
	 */
	private String debtPlanNameSrch;

	/**
	 * 计划加入订单号
	 */
	private String accedeOrderIdSrch;

	/**
	 * 提成人用户名
	 */
	private String userNameSrch;

	/**
	 * 提成人部门
	 */
	private String combotreeSrch; // 部门
	/**
	 * 提成人部门
	 */
	private String[] combotreeListSrch; // 部门

	/**
	 * 出借人用户名
	 */
	private String accedeUserNameSrch;

	/**
	 * 提成发放
	 */
	private String statusSrch;

	/**
	 * 出借开始时间(检索用)
	 */
	private String accedeStartTimeSearch;

	/**
	 * 出借结束时间(检索用)
	 */
	private String accedeEndTimeSearch;

	/**
	 * 返现开始时间(检索用)
	 */
	private String returnStartTimeSearch;

	/**
	 * 返现结束时间(检索用)
	 */
	private String returnEndTimeSearch;

	/**
	 * 计划进入锁定期开始时间
	 */
	private String planLockStartTimeSrch;

	/**
	 * 计划进入锁定期结束时间
	 */
	private String planLockEndTimeSrch;

	/**
	 * 计划编号
	 */
	private String debtPlanNid;

	/**
	 * 加入订单号
	 */
	private String orderId;

	/**
	 * ids
	 */
	private String ids;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public List<DebtPlan> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<DebtPlan> recordList) {
		this.recordList = recordList;
	}

	public String getDebtPlanNidSrch() {
		return debtPlanNidSrch;
	}

	public void setDebtPlanNidSrch(String debtPlanNidSrch) {
		this.debtPlanNidSrch = debtPlanNidSrch;
	}

	public String getDebtPlanNameSrch() {
		return debtPlanNameSrch;
	}

	public void setDebtPlanNameSrch(String debtPlanNameSrch) {
		this.debtPlanNameSrch = debtPlanNameSrch;
	}

	public String getPlanLockStartTimeSrch() {
		return planLockStartTimeSrch;
	}

	public void setPlanLockStartTimeSrch(String planLockStartTimeSrch) {
		this.planLockStartTimeSrch = planLockStartTimeSrch;
	}

	public String getPlanLockEndTimeSrch() {
		return planLockEndTimeSrch;
	}

	public void setPlanLockEndTimeSrch(String planLockEndTimeSrch) {
		this.planLockEndTimeSrch = planLockEndTimeSrch;
	}

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getDebtPlanNid() {
		return debtPlanNid;
	}

	public void setDebtPlanNid(String debtPlanNid) {
		this.debtPlanNid = debtPlanNid;
	}

	public String getAccedeOrderIdSrch() {
		return accedeOrderIdSrch;
	}

	public void setAccedeOrderIdSrch(String accedeOrderIdSrch) {
		this.accedeOrderIdSrch = accedeOrderIdSrch;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String[] getCombotreeListSrch() {
		return combotreeListSrch;
	}

	public void setCombotreeListSrch(String[] combotreeListSrch) {
		this.combotreeListSrch = combotreeListSrch;
	}

	public String getAccedeUserNameSrch() {
		return accedeUserNameSrch;
	}

	public void setAccedeUserNameSrch(String accedeUserNameSrch) {
		this.accedeUserNameSrch = accedeUserNameSrch;
	}

	public String getStatusSrch() {
		return statusSrch;
	}

	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

	public String getAccedeStartTimeSearch() {
		return accedeStartTimeSearch;
	}

	public void setAccedeStartTimeSearch(String accedeStartTimeSearch) {
		this.accedeStartTimeSearch = accedeStartTimeSearch;
	}

	public String getAccedeEndTimeSearch() {
		return accedeEndTimeSearch;
	}

	public void setAccedeEndTimeSearch(String accedeEndTimeSearch) {
		this.accedeEndTimeSearch = accedeEndTimeSearch;
	}

	public String getReturnStartTimeSearch() {
		return returnStartTimeSearch;
	}

	public void setReturnStartTimeSearch(String returnStartTimeSearch) {
		this.returnStartTimeSearch = returnStartTimeSearch;
	}

	public String getReturnEndTimeSearch() {
		return returnEndTimeSearch;
	}

	public void setReturnEndTimeSearch(String returnEndTimeSearch) {
		this.returnEndTimeSearch = returnEndTimeSearch;
	}

	public List<AdminPlanPushMoneyDetailCustomize> getPushMoneyDetailList() {
		return pushMoneyDetailList;
	}

	public void setPushMoneyDetailList(List<AdminPlanPushMoneyDetailCustomize> pushMoneyDetailList) {
		this.pushMoneyDetailList = pushMoneyDetailList;
	}

	public String getCombotreeSrch() {
		return combotreeSrch;
	}

	public void setCombotreeSrch(String combotreeSrch) {
		this.combotreeSrch = combotreeSrch;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDepIds() {
		return depIds;
	}

	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}

}
