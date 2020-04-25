package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrecover;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BatchBorrowRecoverBean implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 批次交易明细标的号
	 */
	private String deatilBorrowNid;
	/**
	 * 项目编号 检索条件
	 */
	private String borrowNid;
	/**
     * 批次提交时间开始  检索条件
     */
	private String submitTimeStartSrch;
	/**
     * 批次提交时间结束  检索条件
     */
	private String submitTimeEndSrch;
	
	/**
	 * 批次编号 检索条件
	 */
	private String batchNo;
	/**
	 * 状态 检索条件
	 */
	private String status;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/*----------add by LSY START--------------*/
	/**
	 * 资产来源 检索条件
	 */
	private String instCodeSrch;
	/*----------add by LSY END--------------*/
	
	public String getDeatilBorrowNid() {
		return deatilBorrowNid;
	}

	public void setDeatilBorrowNid(String deatilBorrowNid) {
		this.deatilBorrowNid = deatilBorrowNid;
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

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	/**
	 * instCodeSrch
	 * @return the instCodeSrch
	 */
		
	public String getInstCodeSrch() {
		return instCodeSrch;
			
	}

	/**
	 * @param instCodeSrch the instCodeSrch to set
	 */
		
	public void setInstCodeSrch(String instCodeSrch) {
		this.instCodeSrch = instCodeSrch;
			
	}


	

}
