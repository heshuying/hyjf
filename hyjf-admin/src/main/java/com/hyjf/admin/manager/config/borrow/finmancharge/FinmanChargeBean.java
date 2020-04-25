package com.hyjf.admin.manager.config.borrow.finmancharge;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowFinmanCharge;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class FinmanChargeBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<BorrowFinmanCharge> recordList;

	private String manChargeCd;

	private String manChargeType;

	private String manChargePer;

	private String chargeTimeType;

	private String status;

	private String remark;

	/**
	 * manChargeCd
	 * 
	 * @return the manChargeCd
	 */

	public String getManChargeCd() {
		return manChargeCd;
	}

	/**
	 * @param manChargeCd
	 *            the manChargeCd to set
	 */

	public void setManChargeCd(String manChargeCd) {
		this.manChargeCd = manChargeCd;
	}

	/**
	 * manChargeType
	 * 
	 * @return the manChargeType
	 */

	public String getManChargeType() {
		return manChargeType;
	}

	/**
	 * @param manChargeType
	 *            the manChargeType to set
	 */

	public void setManChargeType(String manChargeType) {
		this.manChargeType = manChargeType;
	}

	/**
	 * manChargePer
	 * 
	 * @return the manChargePer
	 */

	public String getManChargePer() {
		return manChargePer;
	}

	/**
	 * @param manChargePer
	 *            the manChargePer to set
	 */

	public void setManChargePer(String manChargePer) {
		this.manChargePer = manChargePer;
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

	public List<BorrowFinmanCharge> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BorrowFinmanCharge> recordList) {
		this.recordList = recordList;
	}

}
