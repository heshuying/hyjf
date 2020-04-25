package com.hyjf.admin.manager.hjhplan.planrepay;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * 画面检索条件
 * @package com.hyjf.admin.manager.hjhplan.planrepay；
 * @author LIBIN
 * @date 2017/08/18
 * @version V1.0  
 */
public class PlanRepayListBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1058153880807193513L;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 排序列
	 */
	private String col;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	/**
	 * 检索条件 加入订单号
	 */
	private String accedeOrderIdSrch;
	/**
	 * 检索条件 计划编号
	 */
	private String planNidSrch;
	/**
	 * 检索条件 用户名
	 */
	private String userNameSrch;
	/**
	 * 检索条件 锁定期
	 */
	private String debtLockPeriodSrch;
	/**
	 * 检索条件 回款状态：0 未回款，1 部分回款 2 已回款'
	 */
	private String repayStatusSrch;
	/**
	 * 检索条件 订单状态：0 自动投标中 1锁定中 2退出中 3已退出'
	 */
	private String orderStatusSrch;
	/**
	 * 检索条件 还款方式
	 */
	private String borrowStyleSrch;
	/**
	 * 检索条件 应还日期开始
	 */
	private String repayTimeStart;
	/**
	 * 检索条件 应还日期结束
	 */
	private String repayTimeEnd;
	/**
	 * 检索条件 计划实际还款时间开始
	 */
	private String actulRepayTimeStart;
	/**
	 * 检索条件 计划实际还款时间结束
	 */
	private String actulRepayTimeEnd;
	/**
	 * 检索条件 推荐人
	 */
	private String refereeNameSrch;
	
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
	public int getPaginatorPage() {
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
	public String getAccedeOrderIdSrch() {
		return accedeOrderIdSrch;
	}
	public void setAccedeOrderIdSrch(String accedeOrderIdSrch) {
		this.accedeOrderIdSrch = accedeOrderIdSrch;
	}
	public String getPlanNidSrch() {
		return planNidSrch;
	}
	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}
	public String getUserNameSrch() {
		return userNameSrch;
	}
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}
	public String getRepayStatusSrch() {
		return repayStatusSrch;
	}
	public void setRepayStatusSrch(String repayStatusSrch) {
		this.repayStatusSrch = repayStatusSrch;
	}
	public String getOrderStatusSrch() {
		return orderStatusSrch;
	}
	public void setOrderStatusSrch(String orderStatusSrch) {
		this.orderStatusSrch = orderStatusSrch;
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
	public String getActulRepayTimeStart() {
		return actulRepayTimeStart;
	}
	public void setActulRepayTimeStart(String actulRepayTimeStart) {
		this.actulRepayTimeStart = actulRepayTimeStart;
	}
	public String getActulRepayTimeEnd() {
		return actulRepayTimeEnd;
	}
	public void setActulRepayTimeEnd(String actulRepayTimeEnd) {
		this.actulRepayTimeEnd = actulRepayTimeEnd;
	}
	public String getDebtLockPeriodSrch() {
		return debtLockPeriodSrch;
	}
	public void setDebtLockPeriodSrch(String debtLockPeriodSrch) {
		this.debtLockPeriodSrch = debtLockPeriodSrch;
	}
	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}
	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}
	public String getRefereeNameSrch() {
		return refereeNameSrch;
	}
	public void setRefereeNameSrch(String refereeNameSrch) {
		this.refereeNameSrch = refereeNameSrch;
	}
}
