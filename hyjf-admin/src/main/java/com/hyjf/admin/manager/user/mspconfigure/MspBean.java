package com.hyjf.admin.manager.user.mspconfigure;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MspRegion;
import com.hyjf.mybatis.model.customize.MspConfigure;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class MspBean extends MspConfigure implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 业务类型查询
	 */
	private String serviceTypeSrch;
	/**
	 * 借款类型查询
	 */
	private String loanTypeSrch;
	/**
	 * 担保类型查询
	 */
	private String sourceTypeSrch;
	

	private String ids;

	private String sourceId;

	private String sourceName;

	private String delFlag;

	private String remark;

	private Integer sourceType;
	
	private String loanMoneys;
	/**
	 * 借款城市
	 */
	private String creditAddress;
	
	

	public String getCreditAddress() {
		return creditAddress;
	}

	public void setCreditAddress(String creditAddress) {
		this.creditAddress = creditAddress;
	}


	/**
	 * sourceId
	 * 
	 * @return the sourceId
	 */

	public String getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId
	 *            the sourceId to set
	 */

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * sourceName
	 * 
	 * @return the sourceName
	 */

	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName
	 *            the sourceName to set
	 */

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * delFlag
	 * 
	 * @return the delFlag
	 */

	public String getDelFlag() {
		return delFlag;
	}

	/**
	 * @param delFlag
	 *            the delFlag to set
	 */

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * remark
	 * 
	 * @return the remark
	 */

	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

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


	public String getServiceTypeSrch() {
		return serviceTypeSrch;
	}

	public void setServiceTypeSrch(String serviceTypeSrch) {
		this.serviceTypeSrch = serviceTypeSrch;
	}

	public String getLoanTypeSrch() {
		return loanTypeSrch;
	}

	public void setLoanTypeSrch(String loanTypeSrch) {
		this.loanTypeSrch = loanTypeSrch;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */

	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceTypeSrch() {
		return sourceTypeSrch;
	}

	public void setSourceTypeSrch(String sourceTypeSrch) {
		this.sourceTypeSrch = sourceTypeSrch;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getLoanMoneys() {
		return loanMoneys;
	}

	public void setLoanMoneys(String loanMoneys) {
		this.loanMoneys = loanMoneys;
	}


}
