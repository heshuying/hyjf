package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;

public class OrganizationStructure implements Serializable{

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	private Integer id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 父节点ID
	 */
	private Integer parentid;
	
	/**
	 * 部门提成发放方式（1线上2线下）
	 */
	private Integer cuttype;
	
	/**
	 * 删除标记 0：已删除 1：未删除 
	 */
	private Integer flag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getCuttype() {
		return cuttype;
	}

	public void setCuttype(Integer cuttype) {
		this.cuttype = cuttype;
	}

	@Override
	public String toString() {
		return "OrganizationStructure [id=" + id + ", name=" + name
				+ ", parentid=" + parentid + ", cuttype=" + cuttype + ", flag="
				+ flag + "]";
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	
}
