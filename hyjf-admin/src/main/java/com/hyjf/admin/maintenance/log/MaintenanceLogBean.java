package com.hyjf.admin.maintenance.log;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.MaintenanceLogCustomize;

public class MaintenanceLogBean extends MaintenanceLogCustomize implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ids;
		
	private List<MaintenanceLogCustomize> recordList;
	
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<MaintenanceLogCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MaintenanceLogCustomize> recordList) {
		this.recordList = recordList;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
