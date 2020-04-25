package com.hyjf.admin.exception.bankrepayfreezeorg;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BankRepayFreezeLog;
import com.hyjf.mybatis.model.auto.BankRepayOrgFreezeLog;

import java.io.Serializable;
import java.util.List;

/**
 * @author wgx
 * @date 2018/10/12
 */
public class BankRepayFreezeOrgBean extends BankRepayOrgFreezeLog implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 2561663838042185968L;

	private List<BankRepayFreezeLog> recordList;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	private String orderId;

	private String submitTimeStartSrch;

	private String submitTimeEndSrch;

	private String orderIdSrch;

    private String retCode;
    private String state; // 0-正常 1-已撤销 2-失败
    private String msg;// 返回信息

	private int limitStart = -1;

	private int limitEnd = -1;

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

	public List<BankRepayFreezeLog> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BankRepayFreezeLog> recordList) {
		this.recordList = recordList;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubmitTimeStartSrch() {
		return submitTimeStartSrch;
	}

	public void setSubmitTimeStartSrch(String submitTimeStartSrch) {
		this.submitTimeStartSrch = submitTimeStartSrch;
	}

	public String getSubmitTimeEndSrch() {
		return submitTimeEndSrch;
	}

	public void setSubmitTimeEndSrch(String submitTimeEndSrch) {
		this.submitTimeEndSrch = submitTimeEndSrch;
	}

	public String getOrderIdSrch() {
		return orderIdSrch;
	}

	public void setOrderIdSrch(String orderIdSrch) {
		this.orderIdSrch = orderIdSrch;
	}

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
