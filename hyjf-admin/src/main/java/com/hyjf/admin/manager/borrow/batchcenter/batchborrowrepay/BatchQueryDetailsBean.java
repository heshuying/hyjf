package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrepay;

import java.io.Serializable;
import java.math.BigDecimal;


public class BatchQueryDetailsBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1851603819021188167L;
	
	private String authCode;// 授权码
	private String txState ;// 交易状态
	private String orderId ;// 还款订单号
	private BigDecimal txAmount;// 操作金额
	private String forAccountId;// 借款人银行账户
	private String productId;// 标的号
	private String fileMsg;//错误描述
	/*------add by LSY START------*/
	private BigDecimal sumTxAmount; //操作金额合计
	/*------add by LSY END------*/
	
	
	public String getFileMsg() {
		return fileMsg;
	}
	public void setFileMsg(String fileMsg) {
		this.fileMsg = fileMsg;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getTxState() {
		return txState;
	}
	public void setTxState(String txState) {
		this.txState = txState;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(BigDecimal txAmount) {
		this.txAmount = txAmount;
	}
	public String getForAccountId() {
		return forAccountId;
	}
	public void setForAccountId(String forAccountId) {
		this.forAccountId = forAccountId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * sumTxAmount
	 * @return the sumTxAmount
	 */
		
	public BigDecimal getSumTxAmount() {
		return sumTxAmount;
			
	}
	/**
	 * @param sumTxAmount the sumTxAmount to set
	 */
		
	public void setSumTxAmount(BigDecimal sumTxAmount) {
		this.sumTxAmount = sumTxAmount;
			
	}
	
}
