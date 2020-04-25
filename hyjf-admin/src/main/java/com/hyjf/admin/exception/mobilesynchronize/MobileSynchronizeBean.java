package com.hyjf.admin.exception.mobilesynchronize;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.MobileSynchronizeCustomize;

/**
 * @author Administrator
 *
 */
public class MobileSynchronizeBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5878365400911813862L;

	/**
	 * 用户名(检索用)
	 */
	private String userNameSrch;

	/**
	 * 电子账号(检索用)
	 */
	private String accountIdSrch;

	/**
	 * 手机号(检索用)
	 */
	private String mobileSrch;
	/**
	 * 电子账号
	 */
	private String accountId;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 用户手机号
	 */
	private String mobile;

	private List<MobileSynchronizeCustomize> recordList;

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

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getAccountIdSrch() {
		return accountIdSrch;
	}

	public void setAccountIdSrch(String accountIdSrch) {
		this.accountIdSrch = accountIdSrch;
	}

	public String getMobileSrch() {
		return mobileSrch;
	}

	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	public List<MobileSynchronizeCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MobileSynchronizeCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
