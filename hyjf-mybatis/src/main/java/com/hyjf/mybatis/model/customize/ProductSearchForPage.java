package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;
/**
  * @ClassName: ProductIntoRecordCustomize
  * @Description: 汇天利转入记录
  * @author Michael
  * @date 2015年11月25日 上午10:42:44
 */
public class ProductSearchForPage implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	    private BigDecimal amount;//出借金额
	    private BigDecimal interest;//利息
	    private String time;//出借时间
		private String status;//出借状态
		private Integer userId;//用户id
		protected int limitStart = -1;
	    protected int limitEnd = -1;
	    
	    private BigDecimal principal;//本金
	    private BigDecimal restInterest;//未提取利息
	    private BigDecimal alreadyInterest;//已提取利息
	    //前台展示用
	    private String amountStr;//出借金额
	    private String interestStr;//利息
	    
	    //时间检索
	    private String dateStartStr;
	    private String dateEndStr;
	    
		public BigDecimal getPrincipal() {
			return principal;
		}
		public void setPrincipal(BigDecimal principal) {
			this.principal = principal;
		}
		public BigDecimal getRestInterest() {
			return restInterest;
		}
		public String getAmountStr() {
			return amountStr;
		}
		public void setAmountStr(String amountStr) {
			this.amountStr = amountStr;
		}
		public String getInterestStr() {
			return interestStr;
		}
		public void setInterestStr(String interestStr) {
			this.interestStr = interestStr;
		}
		public void setRestInterest(BigDecimal restInterest) {
			this.restInterest = restInterest;
		}
		public BigDecimal getAlreadyInterest() {
			return alreadyInterest;
		}
		public void setAlreadyInterest(BigDecimal alreadyInterest) {
			this.alreadyInterest = alreadyInterest;
		}
		public BigDecimal getAmount() {
			return amount;
		}
		public BigDecimal getInterest() {
			return interest;
		}
		public void setInterest(BigDecimal interest) {
			this.interest = interest;
		}
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public int getLimitStart() {
			return limitStart;
		}
		public void setLimitStart(int limitStart) {
			this.limitStart = limitStart;
		}
		public int getLimitEnd() {
			return limitEnd;
		}
		public void setLimitEnd(int limitEnd) {
			this.limitEnd = limitEnd;
		}
		public String getDateStartStr() {
			return dateStartStr;
		}
		public void setDateStartStr(String dateStartStr) {
			this.dateStartStr = dateStartStr;
		}
		public String getDateEndStr() {
			return dateEndStr;
		}
		public void setDateEndStr(String dateEndStr) {
			this.dateEndStr = dateEndStr;
		}

	
	}
