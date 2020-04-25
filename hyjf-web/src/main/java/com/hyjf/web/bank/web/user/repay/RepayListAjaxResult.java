/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.bank.web.user.repay;

import java.util.Date;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserRepayTransferCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

/**
 * 用户借款列表ajax返回值对象
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月29日
 * @see 上午9:42:19
 */
public class RepayListAjaxResult extends WebBaseAjaxResultBean {

	/**
	 * 此处为属性说明
	 */
	private static final long serialVersionUID = 3091714256051407464L;

	// 用户角色
	public String roleId;
	// 当前时间（用于校验担保机构最多只能提前三天还款）
	public Date nowdate;
	//还款列表
	private List<WebUserRepayProjectListCustomize> repayList;

	private List<WebUserRepayTransferCustomize> transferList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Date getNowdate() {
		return nowdate;
	}
	public void setNowdate(Date nowdate) {
		this.nowdate = nowdate;
	}
	public List<WebUserRepayProjectListCustomize> getRepayList() {
		return repayList;
	}
	public void setRepayList(List<WebUserRepayProjectListCustomize> repayList) {
		this.repayList = repayList;
	}

	public List<WebUserRepayTransferCustomize> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<WebUserRepayTransferCustomize> transferList) {
		this.transferList = transferList;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public Paginator getPaginator() {
		return paginator;
	}

	@Override
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
}
