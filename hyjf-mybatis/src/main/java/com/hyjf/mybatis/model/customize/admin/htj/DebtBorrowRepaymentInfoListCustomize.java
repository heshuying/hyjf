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

package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

/**
 * 还款明细
 * 
 * @author 孙亮
 * @since 2015年12月19日 上午9:29:09
 */
public class DebtBorrowRepaymentInfoListCustomize implements Serializable {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	/**
	 * 应还日期 检索条件
	 */
	private String recoverTimeStartSrch;
	/**
	 * 应还日期 检索条件
	 */
	private String recoverTimeEndSrch;

	// ========================参数=============================
	private String nid;// 出借nid,还款订单号
	private String borrowNid;// 借款编号
	private String userId;// 借款人ID
	private String borrowUserName;// 借款人用户名
	private String borrowName;// 借款标题
	private String projectType;// 项目类型id
	private String projectTypeName;// 项目类型名称
	private String borrowPeriod;// 借款期限
	private String borrowApr;// 年化收益
	private String borrowAccount;// 借款金额
	private String borrowAccountYes;// 借到金额
	private String repayType;// 还款方式
	private String recoverPeriod;// 还款期次
	private String recoverUserId;// 出借人ID
	private String recoverUserName;// 出借人用户名
	private String recoverTotal;// 出借金额
	private String recoverCapital;// 应还本金
	private String recoverInterest;// 应还利息
	private String recoverAccount;// 应还本息
	private String recoverFee;// 管理费
	private String tiqiantianshu;// 提前天数
	private String shaohuanlixi;// 少还利息
	private String yanqitianshu;// 延期天数
	private String yanqilixi;// 延期利息
	private String yuqitianshu;// 逾期天数
	private String yuqilixi;// 逾期利息
	private String yinghuanzonge;// 应还总额
	private String shihuanzonge;// 实还总额
	private String status;// 还款状态
	private String recoverActionTime;// 实际还款日期
	private String recoverLastTime;// 应还日期

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

	public String getRecoverTimeStartSrch() {
		return recoverTimeStartSrch;
	}

	public void setRecoverTimeStartSrch(String recoverTimeStartSrch) {
		this.recoverTimeStartSrch = recoverTimeStartSrch;
	}

	public String getRecoverTimeEndSrch() {
		return recoverTimeEndSrch;
	}

	public void setRecoverTimeEndSrch(String recoverTimeEndSrch) {
		this.recoverTimeEndSrch = recoverTimeEndSrch;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBorrowUserName() {
		return borrowUserName;
	}

	public void setBorrowUserName(String borrowUserName) {
		this.borrowUserName = borrowUserName;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getBorrowAccountYes() {
		return borrowAccountYes;
	}

	public void setBorrowAccountYes(String borrowAccountYes) {
		this.borrowAccountYes = borrowAccountYes;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRecoverPeriod() {
		return recoverPeriod;
	}

	public void setRecoverPeriod(String recoverPeriod) {
		this.recoverPeriod = recoverPeriod;
	}

	public String getRecoverUserId() {
		return recoverUserId;
	}

	public void setRecoverUserId(String recoverUserId) {
		this.recoverUserId = recoverUserId;
	}

	public String getRecoverUserName() {
		return recoverUserName;
	}

	public void setRecoverUserName(String recoverUserName) {
		this.recoverUserName = recoverUserName;
	}

	public String getRecoverTotal() {
		return recoverTotal;
	}

	public void setRecoverTotal(String recoverTotal) {
		this.recoverTotal = recoverTotal;
	}

	public String getRecoverCapital() {
		return recoverCapital;
	}

	public void setRecoverCapital(String recoverCapital) {
		this.recoverCapital = recoverCapital;
	}

	public String getRecoverInterest() {
		return recoverInterest;
	}

	public void setRecoverInterest(String recoverInterest) {
		this.recoverInterest = recoverInterest;
	}

	public String getRecoverAccount() {
		return recoverAccount;
	}

	public void setRecoverAccount(String recoverAccount) {
		this.recoverAccount = recoverAccount;
	}

	public String getRecoverFee() {
		return recoverFee;
	}

	public void setRecoverFee(String recoverFee) {
		this.recoverFee = recoverFee;
	}

	public String getTiqiantianshu() {
		return tiqiantianshu;
	}

	public void setTiqiantianshu(String tiqiantianshu) {
		this.tiqiantianshu = tiqiantianshu;
	}

	public String getShaohuanlixi() {
		return shaohuanlixi;
	}

	public void setShaohuanlixi(String shaohuanlixi) {
		this.shaohuanlixi = shaohuanlixi;
	}

	public String getYanqitianshu() {
		return yanqitianshu;
	}

	public void setYanqitianshu(String yanqitianshu) {
		this.yanqitianshu = yanqitianshu;
	}

	public String getYanqilixi() {
		return yanqilixi;
	}

	public void setYanqilixi(String yanqilixi) {
		this.yanqilixi = yanqilixi;
	}

	public String getYuqitianshu() {
		return yuqitianshu;
	}

	public void setYuqitianshu(String yuqitianshu) {
		this.yuqitianshu = yuqitianshu;
	}

	public String getYuqilixi() {
		return yuqilixi;
	}

	public void setYuqilixi(String yuqilixi) {
		this.yuqilixi = yuqilixi;
	}

	public String getYinghuanzonge() {
		return yinghuanzonge;
	}

	public void setYinghuanzonge(String yinghuanzonge) {
		this.yinghuanzonge = yinghuanzonge;
	}

	public String getShihuanzonge() {
		return shihuanzonge;
	}

	public void setShihuanzonge(String shihuanzonge) {
		this.shihuanzonge = shihuanzonge;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecoverActionTime() {
		return recoverActionTime;
	}

	public void setRecoverActionTime(String recoverActionTime) {
		this.recoverActionTime = recoverActionTime;
	}

	public String getRecoverLastTime() {
		return recoverLastTime;
	}

	public void setRecoverLastTime(String recoverLastTime) {
		this.recoverLastTime = recoverLastTime;
	}

}
