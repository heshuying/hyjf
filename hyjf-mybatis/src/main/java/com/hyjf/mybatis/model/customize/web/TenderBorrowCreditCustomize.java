/**
 * Description:债权转让提交实体类
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2016年3月30日 上午11:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.web;

/**
 * @author Administrator
 */

public class TenderBorrowCreditCustomize extends TenderCreditCustomize {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 折价率
	 */
	private String creditDiscount;
	/**
	 * 转让价格
	 */
	private String creditPrice;


	/**
	 * 转让服务费
	 */
	private String attornRate;
	/**
	 * 服务费
	 */
	private String creditFee;
	/**
	 * 手机验证码
	 */
	private String telcode;
	/**
	 * 持有天数
	 */
	private String tenderPeriod;

	public String getCreditDiscount() {
		return creditDiscount;
	}

	public void setCreditDiscount(String creditDiscount) {
		this.creditDiscount = creditDiscount;
	}

	public String getCreditPrice() {
		return creditPrice;
	}

	public void setCreditPrice(String creditPrice) {
		this.creditPrice = creditPrice;
	}

	public String getCreditFee() {
		return creditFee;
	}

	public void setCreditFee(String creditFee) {
		this.creditFee = creditFee;
	}

	public String getTelcode() {
		return telcode;
	}

	public void setTelcode(String telcode) {
		this.telcode = telcode;
	}

	public String getTenderPeriod() {
		return tenderPeriod;
	}

	public void setTenderPeriod(String tenderPeriod) {
		this.tenderPeriod = tenderPeriod;
	}

	public String getAttornRate() {
		return attornRate;
	}

	public void setAttornRate(String attornRate) {
		this.attornRate = attornRate;
	}
}
