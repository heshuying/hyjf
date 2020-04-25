package com.hyjf.admin.employee.employeeinfo;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;
import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;
import com.hyjf.mybatis.model.customize.EmployeeInfoCustomize;

public class EmployeeInfoBean extends EmployeeInfoCustomize implements Serializable{

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	
    private String ids;

	private String truenameSrch;
	private String mobileSrch;
	
	
	private List<EmployeeInfoCustomize> recordList;
	private List<EmployeeDimissionCustomize> dimissionList;
	private List<EmployeeEntryCustomize> entryList;

	
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

	public List<EmployeeInfoCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<EmployeeInfoCustomize> recordList) {
		this.recordList = recordList;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTruenameSrch() {
		return truenameSrch;
	}

	public void setTruenameSrch(String truenameSrch) {
		this.truenameSrch = truenameSrch;
	}

	public String getMobileSrch() {
		return mobileSrch;
	}

	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<EmployeeDimissionCustomize> getDimissionList() {
		return dimissionList;
	}

	public void setDimissionList(List<EmployeeDimissionCustomize> dimissionList) {
		this.dimissionList = dimissionList;
	}

	public List<EmployeeEntryCustomize> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<EmployeeEntryCustomize> entryList) {
		this.entryList = entryList;
	}


	
	
	

}
