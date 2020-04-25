package com.hyjf.admin.manager.user.userauth;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminUserPayAuthCustomize;

public class UserPayAuthListBean extends AdminUserPayAuthCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AdminUserPayAuthCustomize> recordList;

	/** 授权时间  开始*/
	private String authTimeStart;
	/** 授权时间  结束*/
	private String authTimeEnd;
	
	/** 签约时间  开始*/
	private String signTimeStart;
	/** 签约时间  结束*/
	private String signTimeEnd;
	
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

	public List<AdminUserPayAuthCustomize> getRecordList() {
		return recordList;
	}

	public Paginator getPaginator() {

		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public void setRecordList(List<AdminUserPayAuthCustomize> recordList) {
		this.recordList = recordList;
	}
	
	public String getAuthTimeStart() {
		return authTimeStart;
	}
	public void setAuthTimeStart(String authTimeStart) {
		this.authTimeStart = authTimeStart;
	}
	public String getAuthTimeEnd() {
		return authTimeEnd;
	}
	public void setAuthTimeEnd(String authTimeEnd) {
		this.authTimeEnd = authTimeEnd;
	}
	public String getSignTimeStart() {
		return signTimeStart;
	}
	public void setSignTimeStart(String signTimeStart) {
		this.signTimeStart = signTimeStart;
	}
	public String getSignTimeEnd() {
		return signTimeEnd;
	}
	public void setSignTimeEnd(String signTimeEnd) {
		this.signTimeEnd = signTimeEnd;
	}	

}
