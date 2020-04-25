package com.hyjf.admin.manager.config.borrow.sendtype;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowSendType;

/**
 * 文章管理实体类
 * 
 * @author
 *
 */
public class SendTypeBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	private String sendCd;

	private String sendName;

	private String afterTime;

	private String remark;

	private String modifyFlag;

	private List<BorrowSendType> recordList;

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

	public List<BorrowSendType> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BorrowSendType> recordList) {
		this.recordList = recordList;
	}

	/**
	 * modifyFlag
	 * 
	 * @return the modifyFlag
	 */

	public String getModifyFlag() {
		return modifyFlag;
	}

	/**
	 * @param modifyFlag
	 *            the modifyFlag to set
	 */

	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	/**
	 * sendCd
	 * 
	 * @return the sendCd
	 */

	public String getSendCd() {
		return sendCd;
	}

	/**
	 * @param sendCd
	 *            the sendCd to set
	 */

	public void setSendCd(String sendCd) {
		this.sendCd = sendCd;
	}

	/**
	 * sendName
	 * 
	 * @return the sendName
	 */

	public String getSendName() {
		return sendName;
	}

	/**
	 * @param sendName
	 *            the sendName to set
	 */

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	/**
	 * afterTime
	 * 
	 * @return the afterTime
	 */

	public String getAfterTime() {
		return afterTime;
	}

	/**
	 * @param afterTime
	 *            the afterTime to set
	 */

	public void setAfterTime(String afterTime) {
		this.afterTime = afterTime;
	}

	/**
	 * remark
	 * 
	 * @return the remark
	 */

	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
