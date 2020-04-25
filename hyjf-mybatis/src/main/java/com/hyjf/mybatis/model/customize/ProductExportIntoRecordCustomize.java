package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;
/**
  * @ClassName: ProductIntoRecordCustomize
  * @Description: 汇天利转入记录
  * @author Michael
  * @date 2015年11月25日 上午10:42:44
 */
public class ProductExportIntoRecordCustomize implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	    private Integer id;//主键
		private String username;//用户名
		private Integer curReferId;//推荐人id
		private String curReferName;//推荐人
	    private Integer ivtReferId;//产品id
	    private String ivtReferName;//推荐人
	    private BigDecimal amount;//出借金额
	    private String investTime;//出借时间
	    private String orderId;//订单号
	    private String investStatus;//出借状态
	    private BigDecimal balance;//本金
	    private BigDecimal realAmount;//实际到账金额
	    private String regionName;//一级部门
	    private String branceName;//二级部门
	    private String departmentName;//团队
	    private String client;//操作平台

		protected int limitStart = -1;
	    protected int limitEnd = -1;
	    
	    private String timeStartSrch;//查询用  时间
	    private String timeEndSrch;//查询用  时间
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public Integer getCurReferId() {
			return curReferId;
		}
		public void setCurReferId(Integer curReferId) {
			this.curReferId = curReferId;
		}
		public String getCurReferName() {
			return curReferName;
		}
		public void setCurReferName(String curReferName) {
			this.curReferName = curReferName;
		}
		public Integer getIvtReferId() {
			return ivtReferId;
		}
		public void setIvtReferId(Integer ivtReferId) {
			this.ivtReferId = ivtReferId;
		}
		public String getIvtReferName() {
			return ivtReferName;
		}
		public void setIvtReferName(String ivtReferName) {
			this.ivtReferName = ivtReferName;
		}
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		public String getInvestTime() {
			return investTime;
		}
		public void setInvestTime(String investTime) {
			this.investTime = investTime;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getInvestStatus() {
			return investStatus;
		}
		public void setInvestStatus(String investStatus) {
			this.investStatus = investStatus;
		}
		public BigDecimal getBalance() {
			return balance;
		}
		public void setBalance(BigDecimal balance) {
			this.balance = balance;
		}
		public BigDecimal getRealAmount() {
			return realAmount;
		}
		public void setRealAmount(BigDecimal realAmount) {
			this.realAmount = realAmount;
		}
		public String getRegionName() {
			return regionName;
		}
		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}
		public String getBranceName() {
			return branceName;
		}
		public void setBranceName(String branceName) {
			this.branceName = branceName;
		}
		public String getDepartmentName() {
			return departmentName;
		}
		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
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
		public String getTimeStartSrch() {
			return timeStartSrch;
		}
		public void setTimeStartSrch(String timeStartSrch) {
			this.timeStartSrch = timeStartSrch;
		}
		public String getTimeEndSrch() {
			return timeEndSrch;
		}
		public void setTimeEndSrch(String timeEndSrch) {
			this.timeEndSrch = timeEndSrch;
		}
		public String getClient() {
			return client;
		}
		public void setClient(String client) {
			this.client = client;
		}

	
	}
