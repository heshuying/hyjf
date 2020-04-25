package com.hyjf.admin.manager.config.hzrconfig;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HzrConfig;

/**
 * 
 * 手续费
 */
public class HzrConfigBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<HzrConfig> recordList;

	private String id;

	private String name;

	private String code;

	private String value;

	private String unit;

	private String remark;

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
	 * name
	 * 
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * code
	 * 
	 * @return the code
	 */

	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * value
	 * 
	 * @return the value
	 */

	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * unit
	 * 
	 * @return the unit
	 */

	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */

	public void setUnit(String unit) {
		this.unit = unit;
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

	public List<HzrConfig> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<HzrConfig> recordList) {
		this.recordList = recordList;
	}

}
