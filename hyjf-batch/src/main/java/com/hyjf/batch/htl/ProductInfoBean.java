package com.hyjf.batch.htl;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ProductInfo;

/**
  * @ClassName: HtlProductBean
  * @Description: TODO
  * @author Michael
  * @date 2015年11月24日 下午2:38:14
 */
public class ProductInfoBean extends ProductInfo implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<ProductInfo> recordList;
	
    private String timeStartSrch;//查询用  时间（页面查询）
    
    private String timeEndSrch;//查询用  时间（页面查询）


	private Integer timeStart; // 开始时间（后台查询用）
    private Integer timeEnd; //结束时间（后台查询用）
	

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public Integer getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Integer timeStart) {
		this.timeStart = timeStart;
	}

	public Integer getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Integer timeEnd) {
		this.timeEnd = timeEnd;
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

	public List<ProductInfo> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ProductInfo> recordList) {
		this.recordList = recordList;
	}

}
