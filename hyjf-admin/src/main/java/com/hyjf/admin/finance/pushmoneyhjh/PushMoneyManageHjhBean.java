package com.hyjf.admin.finance.pushmoneyhjh;

import java.io.Serializable;
import java.util.List;

import org.openxmlformats.schemas.drawingml.x2006.diagram.STConnectorRouting;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.PushMoneyCustomize;

public class PushMoneyManageHjhBean extends PushMoneyCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 2561663838042185965L;

	private Integer ids;
	
	private String planOrderId;

    private String depIds;
    
    private String planNid;

	private List<PushMoneyCustomize> recordList;
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

	public List<PushMoneyCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<PushMoneyCustomize> recordList) {
		this.recordList = recordList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getIds() {
		return ids;
	}

	public void setIds(Integer ids) {
		this.ids = ids;
	}

    public String getDepIds() {
        return depIds;
    }

    public void setDepIds(String depIds) {
        this.depIds = depIds;
    }

	public String getPlanOrderId() {
		return planOrderId;
	}

	public void setPlanOrderId(String planOrderId) {
		this.planOrderId = planOrderId;
	}

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}




}















