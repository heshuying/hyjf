package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.common.paginator.Paginator;

public class PlanStatisCustomize implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private BigDecimal waitInvest;

	private BigDecimal waitCredit;

	private Integer yesInvest;

	private Integer yesCredit;

	private BigDecimal waitRepay;

	private BigDecimal yesRepay;

	private BigDecimal planRepayWait;

	private BigDecimal planRepayYes;

	private BigDecimal expireFairValue;

	private BigDecimal planAccedeAll;

	private Integer investPeriodOne;

	private Integer investPeriodTwoFour;

	private Integer investPeriodFiveEight;

	private Integer investPeriodNineTwel;

	private Integer investPeriodTwelTf;

	private Integer investPeriodTf;

	private Integer creditPeriodOne;

	private Integer creditPeriodTwoFour;

	private Integer creditPeriodFiveEight;

	private Integer creditPeriodNineTwel;

	private Integer creditPeriodTwelTf;

	private Integer creditPeriodTf;

	private Integer createTime;

	private String dataDate;

	private String dataMonth;

	private String dataHour;

	// 日期选择查询
	private String timeStart;
	private String timeEnd;
	private String daySearch;
	private String hourSearch;

	private String vFlag;

	private Integer userId;// 用户id
	private BigDecimal principal;// 出借人本金
	/***
	 * 新老用户分布
	 */
	private BigDecimal amount;// 操作金额
	private Integer regTime;// 注册时间

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
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getWaitInvest() {
		return waitInvest;
	}

	public void setWaitInvest(BigDecimal waitInvest) {
		this.waitInvest = waitInvest;
	}

	public BigDecimal getWaitCredit() {
		return waitCredit;
	}

	public void setWaitCredit(BigDecimal waitCredit) {
		this.waitCredit = waitCredit;
	}

	public Integer getYesInvest() {
		return yesInvest;
	}

	public void setYesInvest(Integer yesInvest) {
		this.yesInvest = yesInvest;
	}

	public Integer getYesCredit() {
		return yesCredit;
	}

	public void setYesCredit(Integer yesCredit) {
		this.yesCredit = yesCredit;
	}

	public BigDecimal getWaitRepay() {
		return waitRepay;
	}

	public void setWaitRepay(BigDecimal waitRepay) {
		this.waitRepay = waitRepay;
	}

	public BigDecimal getYesRepay() {
		return yesRepay;
	}

	public void setYesRepay(BigDecimal yesRepay) {
		this.yesRepay = yesRepay;
	}

	public Integer getInvestPeriodOne() {
		return investPeriodOne;
	}

	public void setInvestPeriodOne(Integer investPeriodOne) {
		this.investPeriodOne = investPeriodOne;
	}

	public Integer getInvestPeriodTwoFour() {
		return investPeriodTwoFour;
	}

	public void setInvestPeriodTwoFour(Integer investPeriodTwoFour) {
		this.investPeriodTwoFour = investPeriodTwoFour;
	}

	public Integer getInvestPeriodFiveEight() {
		return investPeriodFiveEight;
	}

	public void setInvestPeriodFiveEight(Integer investPeriodFiveEight) {
		this.investPeriodFiveEight = investPeriodFiveEight;
	}

	public Integer getInvestPeriodNineTwel() {
		return investPeriodNineTwel;
	}

	public void setInvestPeriodNineTwel(Integer investPeriodNineTwel) {
		this.investPeriodNineTwel = investPeriodNineTwel;
	}

	public Integer getInvestPeriodTwelTf() {
		return investPeriodTwelTf;
	}

	public void setInvestPeriodTwelTf(Integer investPeriodTwelTf) {
		this.investPeriodTwelTf = investPeriodTwelTf;
	}

	public Integer getInvestPeriodTf() {
		return investPeriodTf;
	}

	public void setInvestPeriodTf(Integer investPeriodTf) {
		this.investPeriodTf = investPeriodTf;
	}

	public Integer getCreditPeriodOne() {
		return creditPeriodOne;
	}

	public void setCreditPeriodOne(Integer creditPeriodOne) {
		this.creditPeriodOne = creditPeriodOne;
	}

	public Integer getCreditPeriodTwoFour() {
		return creditPeriodTwoFour;
	}

	public void setCreditPeriodTwoFour(Integer creditPeriodTwoFour) {
		this.creditPeriodTwoFour = creditPeriodTwoFour;
	}

	public Integer getCreditPeriodFiveEight() {
		return creditPeriodFiveEight;
	}

	public void setCreditPeriodFiveEight(Integer creditPeriodFiveEight) {
		this.creditPeriodFiveEight = creditPeriodFiveEight;
	}

	public Integer getCreditPeriodNineTwel() {
		return creditPeriodNineTwel;
	}

	public void setCreditPeriodNineTwel(Integer creditPeriodNineTwel) {
		this.creditPeriodNineTwel = creditPeriodNineTwel;
	}

	public Integer getCreditPeriodTwelTf() {
		return creditPeriodTwelTf;
	}

	public void setCreditPeriodTwelTf(Integer creditPeriodTwelTf) {
		this.creditPeriodTwelTf = creditPeriodTwelTf;
	}

	public Integer getCreditPeriodTf() {
		return creditPeriodTf;
	}

	public void setCreditPeriodTf(Integer creditPeriodTf) {
		this.creditPeriodTf = creditPeriodTf;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getDataMonth() {
		return dataMonth;
	}

	public void setDataMonth(String dataMonth) {
		this.dataMonth = dataMonth;
	}

	public String getDataHour() {
		return dataHour;
	}

	public void setDataHour(String dataHour) {
		this.dataHour = dataHour;
	}

	public String getvFlag() {
		return vFlag;
	}

	public void setvFlag(String vFlag) {
		this.vFlag = vFlag;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getRegTime() {
		return regTime;
	}

	public void setRegTime(Integer regTime) {
		this.regTime = regTime;
	}

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

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getDaySearch() {
		return daySearch;
	}

	public void setDaySearch(String daySearch) {
		this.daySearch = daySearch;
	}

	public BigDecimal getPlanRepayWait() {
		return planRepayWait;
	}

	public void setPlanRepayWait(BigDecimal planRepayWait) {
		this.planRepayWait = planRepayWait;
	}

	public BigDecimal getPlanRepayYes() {
		return planRepayYes;
	}

	public void setPlanRepayYes(BigDecimal planRepayYes) {
		this.planRepayYes = planRepayYes;
	}

	public BigDecimal getExpireFairValue() {
		return expireFairValue;
	}

	public void setExpireFairValue(BigDecimal expireFairValue) {
		this.expireFairValue = expireFairValue;
	}

	public BigDecimal getPlanAccedeAll() {
		return planAccedeAll;
	}

	public void setPlanAccedeAll(BigDecimal planAccedeAll) {
		this.planAccedeAll = planAccedeAll;
	}

	public String getHourSearch() {
		return hourSearch;
	}

	public void setHourSearch(String hourSearch) {
		this.hourSearch = hourSearch;
	}

}