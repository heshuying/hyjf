package com.hyjf.admin.htl.productinto;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;

/**
  * @ClassName: HtlProductBean
  * @Description: TODO
  * @author Michael
  * @date 2015年11月24日 下午2:38:14
 */
public class ProductIntoRecordCustomizeBean extends ProductIntoRecordCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<ProductIntoRecordCustomize> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	//查询用                          
	private String usernameSrh;
	private String investStatusSrh;
	private String refernameSrh;
	private String clientSrh;
	private String recordId; //记录id
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getUsernameSrh() {
		return usernameSrh;
	}

	public void setUsernameSrh(String usernameSrh) {
		this.usernameSrh = usernameSrh;
	}

	public String getInvestStatusSrh() {
		return investStatusSrh;
	}

	public void setInvestStatusSrh(String investStatusSrh) {
		this.investStatusSrh = investStatusSrh;
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

	public List<ProductIntoRecordCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ProductIntoRecordCustomize> recordList) {
		this.recordList = recordList;
	}

}
