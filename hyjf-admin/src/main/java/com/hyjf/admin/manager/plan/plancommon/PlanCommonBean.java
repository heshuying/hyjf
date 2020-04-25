package com.hyjf.admin.manager.plan.plancommon;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;

public class PlanCommonBean implements Serializable {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -3153570852662586404L;

	/**
	 * 项目编号(检索用)
	 */
	private String borrowNidSrch;

	/**
	 * 还款方式(检索用)
	 */
	private String borrowStyleSrch;

	/**
	 * 计划编号(检索用)
	 */
	private String debtPlanNidSrch;

	/**
	 * tab名
	 */
	private String tabName;

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

	/**
	 * 计划编号
	 */
	private String debtPlanNid;

	/**
	 * 计划预编号
	 */
	private String debtPlanPreNid;

	/**
	 * 计划预编号
	 */
	private String debtPlanPreNidHid;

	/**
	 * 计划类型
	 */
	private String debtPlanType;

	/**
	 * 计划类型名称
	 */
	private String debtPlanTypeName;

	/**
	 * 计划名称
	 */
	private String debtPlanName;

	/**
	 * 计划金额
	 */
	private String debtPlanMoney;

	/**
	 * 锁定期
	 */
	private String debtLockPeriod;

	/**
	 * 预期出借利率
	 */
	private String expectApr;

	/**
	 * 退出方式
	 */
	private String debtQuitStyle;

	/**
	 * 退出所需天数
	 */
	private String debtQuitPeriod;

	/**
	 * 计划概念
	 */
	private String planConcept;

	/**
	 * 计划原理
	 */
	private String planPrinciple;

	/**
	 * 风控保障措施
	 */
	private String safeguardMeasures;

	/**
	 * 风险保证金措施
	 */
	private String marginMeasures;

	/**
	 * 计划状态
	 */
	private String debtPlanStatus;

	/**
	 * 申购开始时间
	 */
	private String buyBeginTime;

	/**
	 * 申购期限(天)
	 */
	private String buyPeriodDay;

	/**
	 * 申购期限(小时)
	 */
	private String buyPeriodHour;

	/**
	 * 申购总期限
	 */
	private String buyPeriod;

	/**
	 * 最低加入金额
	 */
	private String debtMinInvestment;

	/**
	 * 递增金额
	 */
	private String debtInvestmentIncrement;

	/**
	 * 最大出借金额
	 */
	private String debtMaxInvestment;

	/**
	 * 审核状态
	 */
	private String isAudits;
	
	/**
	 * 可用券配置
	 */
	private String couponConfig;

	/**
	 * 关联资产
	 */
	private List<DebtPlanBorrowCustomize> debtPlanBorrowList;

	/**
	 * 关联资产添加Flg
	 */
	private String isAddFlg;

	/**
	 * 资产配置关联的标的编号
	 */
	private String debtPlanBorrowNid;

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getDebtPlanNidSrch() {
		return debtPlanNidSrch;
	}

	public void setDebtPlanNidSrch(String debtPlanNidSrch) {
		this.debtPlanNidSrch = debtPlanNidSrch;
	}

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
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

	public String getDebtPlanPreNid() {
		return debtPlanPreNid;
	}

	public void setDebtPlanPreNid(String debtPlanPreNid) {
		this.debtPlanPreNid = debtPlanPreNid;
	}

	public String getDebtPlanType() {
		return debtPlanType;
	}

	public void setDebtPlanType(String debtPlanType) {
		this.debtPlanType = debtPlanType;
	}

	public String getDebtPlanTypeName() {
		return debtPlanTypeName;
	}

	public void setDebtPlanTypeName(String debtPlanTypeName) {
		this.debtPlanTypeName = debtPlanTypeName;
	}

	public String getDebtPlanName() {
		return debtPlanName;
	}

	public void setDebtPlanName(String debtPlanName) {
		this.debtPlanName = debtPlanName;
	}

	public String getDebtPlanMoney() {
		return debtPlanMoney;
	}

	public void setDebtPlanMoney(String debtPlanMoney) {
		this.debtPlanMoney = debtPlanMoney;
	}

	public String getDebtLockPeriod() {
		return debtLockPeriod;
	}

	public void setDebtLockPeriod(String debtLockPeriod) {
		this.debtLockPeriod = debtLockPeriod;
	}

	public String getExpectApr() {
		return expectApr;
	}

	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}

	public String getDebtQuitStyle() {
		return debtQuitStyle;
	}

	public void setDebtQuitStyle(String debtQuitStyle) {
		this.debtQuitStyle = debtQuitStyle;
	}

	public String getDebtQuitPeriod() {
		return debtQuitPeriod;
	}

	public void setDebtQuitPeriod(String debtQuitPeriod) {
		this.debtQuitPeriod = debtQuitPeriod;
	}

	public String getPlanConcept() {
		return planConcept;
	}

	public void setPlanConcept(String planConcept) {
		this.planConcept = planConcept;
	}

	public String getPlanPrinciple() {
		return planPrinciple;
	}

	public void setPlanPrinciple(String planPrinciple) {
		this.planPrinciple = planPrinciple;
	}

	public String getSafeguardMeasures() {
		return safeguardMeasures;
	}

	public void setSafeguardMeasures(String safeguardMeasures) {
		this.safeguardMeasures = safeguardMeasures;
	}

	public String getMarginMeasures() {
		return marginMeasures;
	}

	public void setMarginMeasures(String marginMeasures) {
		this.marginMeasures = marginMeasures;
	}

	public String getDebtPlanStatus() {
		return debtPlanStatus;
	}

	public void setDebtPlanStatus(String debtPlanStatus) {
		this.debtPlanStatus = debtPlanStatus;
	}

	public String getBuyBeginTime() {
		return buyBeginTime;
	}

	public void setBuyBeginTime(String buyBeginTime) {
		this.buyBeginTime = buyBeginTime;
	}

	public String getBuyPeriodDay() {
		return buyPeriodDay;
	}

	public void setBuyPeriodDay(String buyPeriodDay) {
		this.buyPeriodDay = buyPeriodDay;
	}

	public String getBuyPeriodHour() {
		return buyPeriodHour;
	}

	public void setBuyPeriodHour(String buyPeriodHour) {
		this.buyPeriodHour = buyPeriodHour;
	}

	public String getBuyPeriod() {
		return buyPeriod;
	}

	public void setBuyPeriod(String buyPeriod) {
		this.buyPeriod = buyPeriod;
	}

	public String getDebtMinInvestment() {
		return debtMinInvestment;
	}

	public void setDebtMinInvestment(String debtMinInvestment) {
		this.debtMinInvestment = debtMinInvestment;
	}

	public String getDebtInvestmentIncrement() {
		return debtInvestmentIncrement;
	}

	public void setDebtInvestmentIncrement(String debtInvestmentIncrement) {
		this.debtInvestmentIncrement = debtInvestmentIncrement;
	}

	public String getDebtMaxInvestment() {
		return debtMaxInvestment;
	}

	public void setDebtMaxInvestment(String debtMaxInvestment) {
		this.debtMaxInvestment = debtMaxInvestment;
	}

	public String getIsAudits() {
		return isAudits;
	}

	public void setIsAudits(String isAudits) {
		this.isAudits = isAudits;
	}

	public List<DebtPlanBorrowCustomize> getDebtPlanBorrowList() {
		return debtPlanBorrowList;
	}

	public void setDebtPlanBorrowList(List<DebtPlanBorrowCustomize> debtPlanBorrowList) {
		this.debtPlanBorrowList = debtPlanBorrowList;
	}

	public String getDebtPlanBorrowNid() {
		return debtPlanBorrowNid;
	}

	public void setDebtPlanBorrowNid(String debtPlanBorrowNid) {
		this.debtPlanBorrowNid = debtPlanBorrowNid;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getIsAddFlg() {
		return isAddFlg;
	}

	public void setIsAddFlg(String isAddFlg) {
		this.isAddFlg = isAddFlg;
	}

	public String getDebtPlanPreNidHid() {
		return debtPlanPreNidHid;
	}

	public void setDebtPlanPreNidHid(String debtPlanPreNidHid) {
		this.debtPlanPreNidHid = debtPlanPreNidHid;
	}

    public String getCouponConfig() {
        return couponConfig;
    }

    public void setCouponConfig(String couponConfig) {
        this.couponConfig = couponConfig;
    }

}
