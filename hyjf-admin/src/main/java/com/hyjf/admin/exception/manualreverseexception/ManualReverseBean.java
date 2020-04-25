package com.hyjf.admin.exception.manualreverseexception;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.ManualReverseCustomize;

/**
 * @author PC-LIUSHOUYI
 */

public class ManualReverseBean extends ManualReverseCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
		
	private static final long serialVersionUID = -7662470012754206625L;

	/**
	 * 原交易流水号
	 */
	private String seqNoSrch;
	
	/**
	 * 用户名
	 */
	private String userNameSrch;
	
	/**
	 * 电子账号
	 */
	private String accountIdSrch;
	
	/**
	 * 交易时间(开始)
	 */
	private String txTimeStartSrch;
	
	/**
	 * 交易时间(结束)
	 */
	private String txTimeEndSrch;
	
	private List<ManualReverseCustomize> recordList;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * seqNoSrch
	 * @return the seqNoSrch
	 */
	
	public String getSeqNoSrch() {
		return seqNoSrch;
	}


	/**
	 * @param seqNoSrch the seqNoSrch to set
	 */
	
	public void setSeqNoSrch(String seqNoSrch) {
		this.seqNoSrch = seqNoSrch;
	}


	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}


	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}


	/**
	 * accountIdSrch
	 * @return the accountIdSrch
	 */
	
	public String getAccountIdSrch() {
		return accountIdSrch;
	}


	/**
	 * @param accountIdSrch the accountIdSrch to set
	 */
	
	public void setAccountIdSrch(String accountIdSrch) {
		this.accountIdSrch = accountIdSrch;
	}


	/**
	 * txTimeStartSrch
	 * @return the txTimeStartSrch
	 */
	
	public String getTxTimeStartSrch() {
		return txTimeStartSrch;
	}


	/**
	 * @param txTimeStartSrch the txTimeStartSrch to set
	 */
	
	public void setTxTimeStartSrch(String txTimeStartSrch) {
		this.txTimeStartSrch = txTimeStartSrch;
	}


	/**
	 * txTimeEndSrch
	 * @return the txTimeEndSrch
	 */
	
	public String getTxTimeEndSrch() {
		return txTimeEndSrch;
	}


	/**
	 * @param txTimeEndSrch the txTimeEndSrch to set
	 */
	
	public void setTxTimeEndSrch(String txTimeEndSrch) {
		this.txTimeEndSrch = txTimeEndSrch;
	}


	/**
	 * recordList
	 * @return the recordList
	 */
	
	public List<ManualReverseCustomize> getRecordList() {
		return recordList;
	}


	/**
	 * @param recordList the recordList to set
	 */
	
	public void setRecordList(List<ManualReverseCustomize> recordList) {
		this.recordList = recordList;
	}


	/**
	 * paginatorPage
	 * @return the paginatorPage
	 */
	
	public int getPaginatorPage() {
		return paginatorPage;
	}


	/**
	 * @param paginatorPage the paginatorPage to set
	 */
	
	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}


	/**
	 * paginator
	 * @return the paginator
	 */
	
	public Paginator getPaginator() {
		return paginator;
	}


	/**
	 * @param paginator the paginator to set
	 */
	
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	
}

	