/**
 * Description:用户开户记录列表前端查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.finance.merchant.transfer;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MerchantTransfer;

/**
 * @author 王坤
 */

public class MerchantTransferListBean extends MerchantTransfer implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7768418442884796575L;
	/** 转账时间开始值 */
	private String transferTimeStart;
	/** 转账时间结束值 */
	private String transferTimeEnd;
	/** 转账列表*/
	private List<MerchantTransfer> recordList;

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

	public String getTransferTimeStart() {
		return transferTimeStart;
	}

	public void setTransferTimeStart(String transferTimeStart) {
		this.transferTimeStart = transferTimeStart;
	}

	public String getTransferTimeEnd() {
		return transferTimeEnd;
	}

	public void setTransferTimeEnd(String transferTimeEnd) {
		this.transferTimeEnd = transferTimeEnd;
	}

	public List<MerchantTransfer> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MerchantTransfer> recordList) {
		this.recordList = recordList;
	}

}
