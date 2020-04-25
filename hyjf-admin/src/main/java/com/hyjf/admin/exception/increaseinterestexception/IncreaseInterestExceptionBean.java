package com.hyjf.admin.exception.increaseinterestexception;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestExceptionCustomize;

/**
 * 融通宝加息异常处理Bean
 * 
 * @ClassName IncreaseInterestExceptionBean
 * @author liuyang
 * @date 2017年1月5日 下午5:33:43
 */
public class IncreaseInterestExceptionBean extends AdminIncreaseInterestExceptionCustomize implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 5455374974609460893L;

	/** 检索列表 */
	private List<AdminIncreaseInterestExceptionCustomize> recordList;

	/** 项目编号(检索用) */
	private String borrowNidSrch;
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

	public List<AdminIncreaseInterestExceptionCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminIncreaseInterestExceptionCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

}
