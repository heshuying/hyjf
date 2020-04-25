package com.hyjf.admin.manager.statis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.customize.HjhAccountBalanceCustomize;

public class AccountBalanceBean extends HjhAccountBalanceCustomize  {

	private static final long serialVersionUID = -5552862010978201046L;



	private List<HjhAccountBalanceCustomize> recordList;
	private HjhAccountBalanceCustomize sumObj;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	private String addTimeStart;
	private String addTimeEnd;
	

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	public String getAddTimeStart() {
		return addTimeStart;
	}

	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}

	public String getAddTimeEnd() {
		return addTimeEnd;
	}

	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}

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

	public List<HjhAccountBalanceCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<HjhAccountBalanceCustomize> recordList) {
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

	public HjhAccountBalanceCustomize getSumObj() {
		return sumObj;
	}

	public void setSumObj(HjhAccountBalanceCustomize sumObj) {
		this.sumObj = sumObj;
	}

	
}
