package com.hyjf.admin.promotion.app.reconciliation;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AppChannelReconciliationCustomize;

/**
 * app渠道对账
 * @author Michael
 */
public class AppChannelReconciliationBean extends AppChannelReconciliationCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;
	/**
	 * 用户名查询
	 */
	private String userNameSrch;
	/**
	 * 订单号查询
	 */
	private String orderCodeSrch;
	/**
	 * 标的编号查询
	 */
	private String borrowNidSrch;
	/**
	 * app渠道查询
	 */
	private String utmNameSrch;

	/**
	 * app渠道列表
	 */
	private List<UtmPlat> utmtTypeList;

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

	public String getOrderCodeSrch() {
		return orderCodeSrch;
	}

	public void setOrderCodeSrch(String orderCodeSrch) {
		this.orderCodeSrch = orderCodeSrch;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public List<UtmPlat> getUtmtTypeList() {
		return utmtTypeList;
	}

	public void setUtmtTypeList(List<UtmPlat> utmtTypeList) {
		this.utmtTypeList = utmtTypeList;
	}

	public String getUtmNameSrch() {
		return utmNameSrch;
	}

	public void setUtmNameSrch(String utmNameSrch) {
		this.utmNameSrch = utmNameSrch;
	}



}
