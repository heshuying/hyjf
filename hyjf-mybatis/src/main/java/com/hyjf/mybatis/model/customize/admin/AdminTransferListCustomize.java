/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class AdminTransferListCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = 5713585580578685080L;
	/** 唯一id */
	private String id;
	/** 订单id */
	private String orderId;
	/** 转出账户 */
	private String outUserName;
	/** 转入账户 */
	private String inUserName;
	/** 转账金额 */
	private String transferAmount;
	/** 转账状态 */
	private String status;
	/** 操作者 */
	private String createUserName;
	/** 操作时间 */
	private String createTime;
	/** 转账链接 */
	private String transferUrl;
	/** 转账时间 */
	private String transferTime;
	/** 说明 */
	private String remark;

	/**
	 * 构造方法
	 */

	public AdminTransferListCustomize() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOutUserName() {
		return outUserName;
	}

	public void setOutUserName(String outUserName) {
		this.outUserName = outUserName;
	}

	public String getInUserName() {
		return inUserName;
	}

	public void setInUserName(String inUserName) {
		this.inUserName = inUserName;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTransferUrl() {
		return transferUrl;
	}

	public void setTransferUrl(String transferUrl) {
		this.transferUrl = transferUrl;
	}

	public String getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
