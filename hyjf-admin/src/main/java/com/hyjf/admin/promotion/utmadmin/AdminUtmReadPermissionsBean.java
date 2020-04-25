package com.hyjf.admin.promotion.utmadmin;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminUtmReadPermissionsCustomize;

/**
 * 渠道账户管理
 * @author Michael
 */
public class AdminUtmReadPermissionsBean extends AdminUtmReadPermissionsCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;
	/**
	 * 用户名查询
	 */
	private String adminUserNameSrch;
	/**
	 * 关键字查询
	 */
	private String keyCodeSrch;
	/**
	 * 真实姓名查询
	 */
	private String adminTrueNameSrch;

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

	public String getAdminUserNameSrch() {
		return adminUserNameSrch;
	}

	public void setAdminUserNameSrch(String adminUserNameSrch) {
		this.adminUserNameSrch = adminUserNameSrch;
	}

	public String getKeyCodeSrch() {
		return keyCodeSrch;
	}

	public void setKeyCodeSrch(String keyCodeSrch) {
		this.keyCodeSrch = keyCodeSrch;
	}

	public String getAdminTrueNameSrch() {
		return adminTrueNameSrch;
	}

	public void setAdminTrueNameSrch(String adminTrueNameSrch) {
		this.adminTrueNameSrch = adminTrueNameSrch;
	}

}
