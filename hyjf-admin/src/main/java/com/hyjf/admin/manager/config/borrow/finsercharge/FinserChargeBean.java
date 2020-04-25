package com.hyjf.admin.manager.config.borrow.finsercharge;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowFinserCharge;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class FinserChargeBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private String chargeCd;

	private String chargeType;
	
	private Integer projectType;

	private String chargeTime;

	private String chargeTimeType;

	private String chargeRate;

	private String status;

	private String remark;

	private List<BorrowFinserCharge> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * id接收
	 */
	private String id;
	/**
	 * 回显数据
	 */
	private List<Map<String, Object>> forBack;

	public List<Map<String, Object>> getForBack() {
		return forBack;
	}

	public void setForBack(List<Map<String, Object>> forBack) {
		this.forBack = forBack;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public List<BorrowFinserCharge> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BorrowFinserCharge> recordList) {
		this.recordList = recordList;
	}

	/**
	 * chargeCd
	 * 
	 * @return the chargeCd
	 */

	public String getChargeCd() {
		return chargeCd;
	}

	/**
	 * @param chargeCd
	 *            the chargeCd to set
	 */

	public void setChargeCd(String chargeCd) {
		this.chargeCd = chargeCd;
	}

	/**
	 * chargeType
	 * 
	 * @return the chargeType
	 */

	public String getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            the chargeType to set
	 */

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * chargeTime
	 * 
	 * @return the chargeTime
	 */

	public String getChargeTime() {
		return chargeTime;
	}

	/**
	 * @param chargeTime
	 *            the chargeTime to set
	 */

	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}

	/**
	 * chargeTimeType
	 * 
	 * @return the chargeTimeType
	 */

	public String getChargeTimeType() {
		return chargeTimeType;
	}

	/**
	 * @param chargeTimeType
	 *            the chargeTimeType to set
	 */

	public void setChargeTimeType(String chargeTimeType) {
		this.chargeTimeType = chargeTimeType;
	}

	/**
	 * chargeRate
	 * 
	 * @return the chargeRate
	 */

	public String getChargeRate() {
		return chargeRate;
	}

	/**
	 * @param chargeRate
	 *            the chargeRate to set
	 */

	public void setChargeRate(String chargeRate) {
		this.chargeRate = chargeRate;
	}

	/**
	 * status
	 * 
	 * @return the status
	 */

	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getProjectType() {
		return projectType;
	}

	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	
	

}
