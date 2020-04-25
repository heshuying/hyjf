package com.hyjf.admin.finance.rechargefee;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;

public class RechargeFeeBean extends RechargeFeeReconciliation implements Serializable {

	/**
	 * serialVersionUID:
	 */
		
	private static final long serialVersionUID = 2561663838042185965L;
	
    private List<RechargeFeeReconciliation> recordList;
    
    /**
     * 查询
     */
    //用户名
    private String userNameSrch;
    //账单编号
    private String rechargeNidSrch;
    //状态
    private String statusSrch;
    //账单周期
    private String startTimeSrch;
    private String endTimeSrch;
    //生成时间
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

	public List<RechargeFeeReconciliation> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<RechargeFeeReconciliation> recordList) {
		this.recordList = recordList;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getRechargeNidSrch() {
		return rechargeNidSrch;
	}

	public void setRechargeNidSrch(String rechargeNidSrch) {
		this.rechargeNidSrch = rechargeNidSrch;
	}

	public String getStatusSrch() {
		return statusSrch;
	}

	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

	public String getStartTimeSrch() {
		return startTimeSrch;
	}

	public void setStartTimeSrch(String startTimeSrch) {
		this.startTimeSrch = startTimeSrch;
	}

	public String getEndTimeSrch() {
		return endTimeSrch;
	}

	public void setEndTimeSrch(String endTimeSrch) {
		this.endTimeSrch = endTimeSrch;
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

	
}

