package com.hyjf.admin.finance.statistics.rechargefee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.RechargeFeeStatistics;

public class RechargeFeeStatisticsBean extends RechargeFeeStatistics implements Serializable {

	/**
	 * serialVersionUID:
	 */
		
	private static final long serialVersionUID = 2561663838042185965L;
	
    private List<RechargeFeeStatistics> recordList;
    //总计金额
    private BigDecimal rechargeAmountSum;
    
    private BigDecimal quickAmountSum;
    
    private BigDecimal personalAmountSum;
    
    private BigDecimal comAmountSum;
    
    private BigDecimal feeSum;
    
    private String updateTimeView;
    /**
     * 查询
     */
    private String startDateSrch;
    private String endDateSrch;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

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

	public List<RechargeFeeStatistics> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<RechargeFeeStatistics> recordList) {
		this.recordList = recordList;
	}

	public String getStartDateSrch() {
		return startDateSrch;
	}

	public void setStartDateSrch(String startDateSrch) {
		this.startDateSrch = startDateSrch;
	}

	public String getEndDateSrch() {
		return endDateSrch;
	}

	public void setEndDateSrch(String endDateSrch) {
		this.endDateSrch = endDateSrch;
	}

	public BigDecimal getRechargeAmountSum() {
		return rechargeAmountSum;
	}

	public void setRechargeAmountSum(BigDecimal rechargeAmountSum) {
		this.rechargeAmountSum = rechargeAmountSum;
	}

	public BigDecimal getQuickAmountSum() {
		return quickAmountSum;
	}

	public void setQuickAmountSum(BigDecimal quickAmountSum) {
		this.quickAmountSum = quickAmountSum;
	}

	public BigDecimal getPersonalAmountSum() {
		return personalAmountSum;
	}

	public void setPersonalAmountSum(BigDecimal personalAmountSum) {
		this.personalAmountSum = personalAmountSum;
	}

	public BigDecimal getComAmountSum() {
		return comAmountSum;
	}

	public void setComAmountSum(BigDecimal comAmountSum) {
		this.comAmountSum = comAmountSum;
	}

	public BigDecimal getFeeSum() {
		return feeSum;
	}

	public void setFeeSum(BigDecimal feeSum) {
		this.feeSum = feeSum;
	}

	public String getUpdateTimeView() {
		return updateTimeView;
	}

	public void setUpdateTimeView(String updateTimeView) {
		this.updateTimeView = updateTimeView;
	}

}

