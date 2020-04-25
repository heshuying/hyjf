package com.hyjf.admin.datacenter.adminstockinfo;

import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminStockInfoCustomize;

public class AdminStockInfoBean extends AdminStockInfoCustomize  {

	private static final long serialVersionUID = -5552862010978201046L;

	private List<AdminStockInfoCustomize> recordList;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	private String fid; //功能菜单id
	
	public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

	public List<AdminStockInfoCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminStockInfoCustomize> recordList) {
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
