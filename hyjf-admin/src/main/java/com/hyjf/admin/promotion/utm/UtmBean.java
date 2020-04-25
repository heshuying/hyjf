package com.hyjf.admin.promotion.utm;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class UtmBean implements Serializable {

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
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;
	/**
	 * 类别查询
	 */
	private String sourceTypeSrch;

	private String id;

	private String sourceId;

	private String sourceName;

	private String delFlag;

	private String remark;

	private Integer sourceType;

	//add by xiashuqing 20171120 增加是否可债转标识 start
	private Integer attornFlag;

	private String sourceIdSrch;
	//add by xiashuqing 20171120 增加是否可债转标识 end

	/**
	 * id
	 * 
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	public void setId(String id) {
		this.id = id;
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

	/**
	 * timeStartSrch
	 * 
	 * @return the timeStartSrch
	 */

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	/**
	 * @param timeStartSrch
	 *            the timeStartSrch to set
	 */

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	/**
	 * timeEndSrch
	 * 
	 * @return the timeEndSrch
	 */

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	/**
	 * @param timeEndSrch
	 *            the timeEndSrch to set
	 */

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
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

	public Integer getAttornFlag() {
		return attornFlag;
	}

	public void setAttornFlag(Integer attornFlag) {
		this.attornFlag = attornFlag;
	}

	public String getSourceIdSrch() {
		return sourceIdSrch;
	}

	public void setSourceIdSrch(String sourceIdSrch) {
		this.sourceIdSrch = sourceIdSrch;
	}
}
