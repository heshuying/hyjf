package com.hyjf.admin.manager.hjhplan.accedelist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;

public class AccedeListBean implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 936711485188059822L;
	
	/**
	 * 计划编号（检索）
	 */
	private String debtPlanNidSrch;
	/**
	 * 加入订单号（检索）
	 */
	private String accedeOrderIdSrch;
	/**
	 * 用户名（检索）
	 */
	private String userNameSrch;
	/**
	 * 锁定期（检索）
	 */
	private String debtLockPeriodSrch;
	/**
	 * 推荐人用户名（检索）
	 */
	private String refereeNameSrch;
	/**
	 * 订单状态下拉框（检索）
	 */
	private String orderStatus;
	/**
	 * 平台下拉框（检索）
	 */
	private String platformSrch;
	/**
	 * 检索开始时间（画面推送开始时间）
	 */
	private String searchStartDate;
	/**
	 * 检索结束时间（画面推送结束时间）
	 */
	private String searchEndDate;
	/**
	 * 检索开始时间（计息开始时间）
	 */
	private String countInterestTimeStartDate;
	/**
	 * 检索结束时间（计息结束时间）
	 */
	private String countInterestTimeEndDate;
	/**
	 * 列表数据
	 */
	private List<AdminPlanAccedeListCustomize> recordList;
	/**
	 * 列表数据
	 */
	private List<BorrowInvestCustomize> recordLists;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	/**
	 * 匹配期查询
	 */
	private String matchDatesSrch;
	
	/**
	 * 出借笔数查询
	 */
	private String investCountsSrch;

	/**
	 * 项目编号(检索)
	 */
	private String borrowNidSrch;

	/**
	 * 复审通过开始时间(检索)
	 */
	private String reAuthPassStartSrch;

	/**
	 * 复审通过结束时间(检索)
	 */
	private String reAuthPassEndSrch;

	/**
	 * 资产来源
	 */
	private String instCodeSrch;

	/**
	 * 项目类型
	 */
	private String projectTypeSrch;

	/**
	 * 借款期限
	 */
	private String borrowPeriod;

	/**
	 * 还款方式
	 */
	private String borrowStyleSrch;

	/**
	 * 是否复投
	 */
	private String tenderType;

	/**
	 * 出借时间开始(检索)
	 */
	private String timeStartSrch;

	/**
	 * 出借时间结束(检索)
	 */
	private String timeEndSrch;

	/**
	 * 预计开始退出时间开始（检索）
	 */
	private String endDateStartSrch;

	/**
	 * 预计开始退出时间结束（检索）
	 */
	private String endDateEndSrch;

	/**
	 * 实际退出时间开始（检索）
	 */
	private String acctualPaymentTimeStartSrch;

	/**
	 * 实际退出时间结束（检索）
	 */
	private String acctualPaymentTimeEndSrch;


	public String getEndDateStartSrch() {
		return endDateStartSrch;
	}

	public void setEndDateStartSrch(String endDateStartSrch) {
		this.endDateStartSrch = endDateStartSrch;
	}

	public String getEndDateEndSrch() {
		return endDateEndSrch;
	}

	public void setEndDateEndSrch(String endDateEndSrch) {
		this.endDateEndSrch = endDateEndSrch;
	}

	public String getAcctualPaymentTimeStartSrch() {
		return acctualPaymentTimeStartSrch;
	}

	public void setAcctualPaymentTimeStartSrch(String acctualPaymentTimeStartSrch) {
		this.acctualPaymentTimeStartSrch = acctualPaymentTimeStartSrch;
	}

	public String getAcctualPaymentTimeEndSrch() {
		return acctualPaymentTimeEndSrch;
	}

	public void setAcctualPaymentTimeEndSrch(String acctualPaymentTimeEndSrch) {
		this.acctualPaymentTimeEndSrch = acctualPaymentTimeEndSrch;
	}

	public String getDebtPlanNidSrch() {
		return debtPlanNidSrch;
	}

	public void setDebtPlanNidSrch(String debtPlanNidSrch) {
		this.debtPlanNidSrch = debtPlanNidSrch;
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

	public String getRefereeNameSrch() {
		return refereeNameSrch;
	}

	public void setRefereeNameSrch(String refereeNameSrch) {
		this.refereeNameSrch = refereeNameSrch;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPlatformSrch() {
		return platformSrch;
	}

	public void setPlatformSrch(String platformSrch) {
		this.platformSrch = platformSrch;
	}

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public List<AdminPlanAccedeListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminPlanAccedeListCustomize> recordList) {
		this.recordList = recordList;
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

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<BorrowInvestCustomize> getRecordLists() {
		return recordLists;
	}

	public void setRecordLists(List<BorrowInvestCustomize> recordLists) {
		this.recordLists = recordLists;
	}

	public String getDebtLockPeriodSrch() {
		return debtLockPeriodSrch;
	}

	public void setDebtLockPeriodSrch(String debtLockPeriodSrch) {
		this.debtLockPeriodSrch = debtLockPeriodSrch;
	}

	public String getCountInterestTimeStartDate() {
		return countInterestTimeStartDate;
	}

	public void setCountInterestTimeStartDate(String countInterestTimeStartDate) {
		this.countInterestTimeStartDate = countInterestTimeStartDate;
	}

	public String getCountInterestTimeEndDate() {
		return countInterestTimeEndDate;
	}

	public void setCountInterestTimeEndDate(String countInterestTimeEndDate) {
		this.countInterestTimeEndDate = countInterestTimeEndDate;
	}

	public String getMatchDatesSrch() {
		return matchDatesSrch;
	}

	public void setMatchDatesSrch(String matchDatesSrch) {
		this.matchDatesSrch = matchDatesSrch;
	}

	public String getInvestCountsSrch() {
		return investCountsSrch;
	}

	public void setInvestCountsSrch(String investCountsSrch) {
		this.investCountsSrch = investCountsSrch;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getReAuthPassStartSrch() {
		return reAuthPassStartSrch;
	}

	public void setReAuthPassStartSrch(String reAuthPassStartSrch) {
		this.reAuthPassStartSrch = reAuthPassStartSrch;
	}

	public String getReAuthPassEndSrch() {
		return reAuthPassEndSrch;
	}

	public void setReAuthPassEndSrch(String reAuthPassEndSrch) {
		this.reAuthPassEndSrch = reAuthPassEndSrch;
	}

	public String getInstCodeSrch() {
		return instCodeSrch;
	}

	public void setInstCodeSrch(String instCodeSrch) {
		this.instCodeSrch = instCodeSrch;
	}

	public String getProjectTypeSrch() {
		return projectTypeSrch;
	}

	public void setProjectTypeSrch(String projectTypeSrch) {
		this.projectTypeSrch = projectTypeSrch;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	public String getTenderType() {
		return tenderType;
	}

	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}
}
