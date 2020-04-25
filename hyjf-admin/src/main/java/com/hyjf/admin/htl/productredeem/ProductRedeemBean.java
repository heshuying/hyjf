package com.hyjf.admin.htl.productredeem;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

/**
  * @ClassName: ProductRedeemCustomizeBean
  * @Description: TODO
  * @author Michael
  * @date 2015年11月27日 下午2:38:14
 */
public class ProductRedeemBean extends ProductRedeemCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<ProductRedeemCustomize> recordList;

	//查询用
	private String usernameSrh;
	private String statusSrh;

	private String refernameSrh;
	private String clientSrh;
	private String orderIdSrh;
	
	private String recordId;
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

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

	public List<ProductRedeemCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ProductRedeemCustomize> recordList) {
		this.recordList = recordList;
	}
	public String getUsernameSrh() {
		return usernameSrh;
	}

	public void setUsernameSrh(String usernameSrh) {
		this.usernameSrh = usernameSrh;
	}

	public String getStatusSrh() {
		return statusSrh;
	}

	public void setStatusSrh(String statusSrh) {
		this.statusSrh = statusSrh;
	}

	public String getRefernameSrh() {
		return refernameSrh;
	}

	public void setRefernameSrh(String refernameSrh) {
		this.refernameSrh = refernameSrh;
	}

	public String getClientSrh() {
		return clientSrh;
	}

	public void setClientSrh(String clientSrh) {
		this.clientSrh = clientSrh;
	}
	public String getOrderIdSrh() {
		return orderIdSrh;
	}

	public void setOrderIdSrh(String orderIdSrh) {
		this.orderIdSrh = orderIdSrh;
	}
}
