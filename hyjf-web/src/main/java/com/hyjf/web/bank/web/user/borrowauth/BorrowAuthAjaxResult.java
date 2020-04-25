/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.bank.web.user.borrowauth;

import java.util.Date;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebBorrowAuthCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

/**
 * 
 * 借款人标的授权
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年11月29日
 * @see 上午11:35:04
 */
public class BorrowAuthAjaxResult extends WebBaseAjaxResultBean {

	/**
	 * 此处为属性说明
	 */
	private static final long serialVersionUID = 3091714256051407464L;

	// 用户角色
	public String roleId;
	// 当前时间（用于校验担保机构最多只能提前三天还款）
	public Date nowdate;
	//还款列表
	private List<WebBorrowAuthCustomize> borrowList;
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
	public List<WebBorrowAuthCustomize> getborrowList() {
		return borrowList;
	}
	public void setborrowList(List<WebBorrowAuthCustomize> borrowList) {
		this.borrowList = borrowList;
	}


}
