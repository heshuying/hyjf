/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.ContentHelp;

/**
 * @author gaolang
 */

public class ContentHelpCustomize extends ContentHelp implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 分类名称
	 */
	private String catetile;
	
	/**
	 * 旧分类ID
	 */
    private Integer cateID;
    
	/**
	 * 区间查询添加时间开始时间
	 */
	private Integer post_time_begin;

	/**
	 * 区间查询添加时间结束时间
	 */
	private Integer post_time_end;
	
	/**
	 * 格式化时间
	 */
	private String add_time;

	private String isZhiChi;
	
    protected int limitStart = -1;

    protected int limitEnd = -1;

	//特殊返回页面要生成的ID
	private String  itemId;
	//特殊返回页面要生成的分类ID
	private String  typeId;
	/**
	 * @return the limitStart
	 */
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart the limitStart to set
	 */
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * @return the limitEnd
	 */
	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd the limitEnd to set
	 */
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public Integer getPost_time_begin() {
		return post_time_begin;
	}

	public void setPost_time_begin(Integer post_time_begin) {
		this.post_time_begin = post_time_begin;
	}

	public Integer getPost_time_end() {
		return post_time_end;
	}


	public void setPost_time_end(Integer post_time_end) {
		this.post_time_end = post_time_end;
	}

	public String getCatetile() {
		return catetile;
	}

	public void setCatetile(String catetile) {
		this.catetile = catetile;
	}

	/**
	 * @return the add_time
	 */
	public String getAdd_time() {
		return add_time;
	}

	/**
	 * @param add_time the add_time to set
	 */
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public Integer getCateID() {
		return cateID;
	}

	public void setCateID(Integer cateID) {
		this.cateID = cateID;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getIsZhiChi() {
		return isZhiChi;
	}

	public void setIsZhiChi(String isZhiChi) {
		this.isZhiChi = isZhiChi;
	}
}
