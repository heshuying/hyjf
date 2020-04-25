package com.hyjf.admin.htl.productredeemdetail;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.ProductRedeemDetailCustomize;

/**
  * @ClassName: ProductRedeemCustomizeBean
  * @Description: TODO
  * @author Michael
  * @date 2015年11月30日 下午2:38:14
 */
public class ProductRedeemDetailBean extends ProductRedeemDetailCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<ProductRedeemDetailCustomize> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	//查询用
	private String listIdSrh;
	private String usernameSrh;
	private String statusSrh;
	

	public String getStatusSrh() {
		return statusSrh;
	}

	public void setStatusSrh(String statusSrh) {
		this.statusSrh = statusSrh;
	}

	public String getListIdSrh() {
		return listIdSrh;
	}

	public void setListIdSrh(String listIdSrh) {
		this.listIdSrh = listIdSrh;
	}

	public String getUsernameSrh() {
		return usernameSrh;
	}

	public void setUsernameSrh(String usernameSrh) {
		this.usernameSrh = usernameSrh;
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

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<ProductRedeemDetailCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ProductRedeemDetailCustomize> recordList) {
		this.recordList = recordList;
	}

}
