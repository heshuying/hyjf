package com.hyjf.web.user.rechargefee;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;

public class RechargeFeeBean extends RechargeFeeReconciliation implements Serializable{
	
	
	private static final long serialVersionUID = 3941884654720915922L;
	//查询
	private String statusSrch;
	
	private String startTimeSrch;
	
	private String endTimeSrch;
	
	private boolean status = false;
	/**
     * 手续费对账列表
     */
	private List<RechargeFeeReconciliation> rechargeFeeList;
	

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	public void success() {
        this.status = true;
    }
	
	
	public RechargeFeeBean() {
		super();
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

	public int getPageSize() {
		if (pageSize == 0) {
			pageSize = 10;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
	
    public List<RechargeFeeReconciliation> getRechargeFeeList() {
		return rechargeFeeList;
	}

	public void setRechargeFeeList(List<RechargeFeeReconciliation> rechargeFeeList) {
		this.rechargeFeeList = rechargeFeeList;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}

}
