package com.hyjf.admin.manager.config.operationlog;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.FeerateModifyLogExample;
import com.hyjf.mybatis.model.auto.Product;

/**
 * 
 * 手续费
 */
public class OperationLogBean extends FeerateModifyLog implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	public List<FeerateModifyLog> recordList;
	// 资产来源
	String instCodeSrch;
	// 产品类型
	String assetTypeSrch;
	// 期限
	String borrowPeriodSrch;
	// 修改类型
	String modifyTypeSrch;
	// 操作人
	String userNameSrch;
	// 操作时间
	String recieveTimeStartSrch;
	// 操作时间
	String recieveTimeEndSrch;
	//状态名称
	String statusName;
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
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
	
	
	
	public List<FeerateModifyLog> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<FeerateModifyLog> recordList) {
		this.recordList = recordList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getInstCodeSrch() {
		return instCodeSrch;
	}
	public void setInstCodeSrch(String instCodeSrch) {
		this.instCodeSrch = instCodeSrch;
	}
	public String getAssetTypeSrch() {
		return assetTypeSrch;
	}
	public void setAssetTypeSrch(String assetTypeSrch) {
		this.assetTypeSrch = assetTypeSrch;
	}
	public String getBorrowPeriodSrch() {
		return borrowPeriodSrch;
	}
	public void setBorrowPeriodSrch(String borrowPeriodSrch) {
		this.borrowPeriodSrch = borrowPeriodSrch;
	}
	public String getModifyTypeSrch() {
		return modifyTypeSrch;
	}
	public void setModifyTypeSrch(String modifyTypeSrch) {
		this.modifyTypeSrch = modifyTypeSrch;
	}
	public String getUserNameSrch() {
		return userNameSrch;
	}
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}
	public String getRecieveTimeStartSrch() {
		return recieveTimeStartSrch;
	}
	public void setRecieveTimeStartSrch(String recieveTimeStartSrch) {
		this.recieveTimeStartSrch = recieveTimeStartSrch;
	}
	public String getRecieveTimeEndSrch() {
		return recieveTimeEndSrch;
	}
	public void setRecieveTimeEndSrch(String recieveTimeEndSrch) {
		this.recieveTimeEndSrch = recieveTimeEndSrch;
	}
	


}
