package com.hyjf.admin.manager.plan.planlock;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllplanCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class PlanLockBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 排序列
	 */
	private String col;

	/**
	 * 检索条件 画面迁移标识
	 */
	private String type;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 计划编号
	 */
	private String planNidSrch;

	/**
	 * 检索条件 计划标题
	 */
	private String planNameSrch;
	/**
	 * 检索条件 计划类型 “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
	 */
	private String planTypeSrch;

	/**
	 * 检索条件 状态 0 发起中；1
	 * 待审核；2审核不通过；3待开放；4募集中；5募集完成；6锁定中；7清算中；8清算完成，9还款，10还款中，11还款完成
	 */
	private String planStatusSrch;

	/**
	 * 检索条件 发起时间
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 满标/到期时间
	 */
	private String fullExpireTime;
	/**
	 * 检索条件 满标/到期时间
	 */
	private String fullExpireTimeEnd;
	/**
	 * 检索条件 实际清算时间
	 */
	private String liquidateFactTime;
	/**
	 * 检索条件 应清算时间
	 */
	private String liquidateShouldTime;
	/**
	 * 检索条件 应清算时间
	 */
	private String liquidateShouldTimeEnd;

	// 锁定中列表所用字段开始
	/**
	 * 检索条件 用户名
	 */
	private String userName;
	/**
	 * 检索条件 计划余额最小
	 */
	private String planWaitMoneyMin;
	/**
	 * 检索条件 计划余额最大
	 */
	private String planWaitMoneyMax;
	/**
	 * 检索条件 加入时间开始
	 */
	private String joinTimeStart;
	/**
	 * 检索条件 加入时间结束
	 */
	private String joinTimeEnd;
	/**
	 * 检索条件 加入明细所需订单号
	 */
	private String accedeOrderId;
	/**
	 * 检索条件 项目编号
	 */
	private String borrowNidSrch;
	/**
	 * 检索条件 项目类型
	 */
	private String projectTypeSrch;
	/**
	 * 检索条件 还款方式
	 */
	private String borrowStyleSrch;
	/**
	 * 检索条件 计划订单号
	 */
	private String planOrderId;
	/**
	 * 检索条件 回款状态
	 */
	private String repayStatus;
	/**
	 * 检索条件 应回款日期开始
	 */
	private String repayTimeStart;
	/**
	 * 检索条件 应回款日期结束
	 */
	private String repayTimeEnd;
	// 锁定中列表所用字段结束

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getPlanNidSrch() {
		return planNidSrch;
	}

	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}

	public String getPlanNameSrch() {
		return planNameSrch;
	}

	public void setPlanNameSrch(String planNameSrch) {
		this.planNameSrch = planNameSrch;
	}

	public String getPlanTypeSrch() {
		return planTypeSrch;
	}

	public void setPlanTypeSrch(String planTypeSrch) {
		this.planTypeSrch = planTypeSrch;
	}

	public String getPlanStatusSrch() {
		return planStatusSrch;
	}

	public void setPlanStatusSrch(String planStatusSrch) {
		this.planStatusSrch = planStatusSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
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

	public String getFullExpireTime() {
		return fullExpireTime;
	}

	public void setFullExpireTime(String fullExpireTime) {
		this.fullExpireTime = fullExpireTime;
	}

	public String getLiquidateFactTime() {
		return liquidateFactTime;
	}

	public void setLiquidateFactTime(String liquidateFactTime) {
		this.liquidateFactTime = liquidateFactTime;
	}

	public String getLiquidateShouldTime() {
		return liquidateShouldTime;
	}

	public void setLiquidateShouldTime(String liquidateShouldTime) {
		this.liquidateShouldTime = liquidateShouldTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlanWaitMoneyMin() {
		return planWaitMoneyMin;
	}

	public void setPlanWaitMoneyMin(String planWaitMoneyMin) {
		this.planWaitMoneyMin = planWaitMoneyMin;
	}

	public String getPlanWaitMoneyMax() {
		return planWaitMoneyMax;
	}

	public void setPlanWaitMoneyMax(String planWaitMoneyMax) {
		this.planWaitMoneyMax = planWaitMoneyMax;
	}

	public String getJoinTimeStart() {
		return joinTimeStart;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}

	public String getJoinTimeEnd() {
		return joinTimeEnd;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getProjectTypeSrch() {
		return projectTypeSrch;
	}

	public void setProjectTypeSrch(String projectTypeSrch) {
		this.projectTypeSrch = projectTypeSrch;
	}

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	public String getAccedeOrderId() {
		return accedeOrderId;
	}

	public void setAccedeOrderId(String accedeOrderId) {
		this.accedeOrderId = accedeOrderId;
	}

	public String getPlanOrderId() {
		return planOrderId;
	}

	public void setPlanOrderId(String planOrderId) {
		this.planOrderId = planOrderId;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	public String getRepayTimeStart() {
		return repayTimeStart;
	}

	public void setRepayTimeStart(String repayTimeStart) {
		this.repayTimeStart = repayTimeStart;
	}

	public String getRepayTimeEnd() {
		return repayTimeEnd;
	}

	public void setRepayTimeEnd(String repayTimeEnd) {
		this.repayTimeEnd = repayTimeEnd;
	}

	public String getFullExpireTimeEnd() {
		return fullExpireTimeEnd;
	}

	public void setFullExpireTimeEnd(String fullExpireTimeEnd) {
		this.fullExpireTimeEnd = fullExpireTimeEnd;
	}

	public String getLiquidateShouldTimeEnd() {
		return liquidateShouldTimeEnd;
	}

	public void setLiquidateShouldTimeEnd(String liquidateShouldTimeEnd) {
		this.liquidateShouldTimeEnd = liquidateShouldTimeEnd;
	}

}
