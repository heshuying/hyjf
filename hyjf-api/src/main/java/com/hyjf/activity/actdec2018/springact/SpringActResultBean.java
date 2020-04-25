package com.hyjf.activity.actdec2018.springact;


import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import 
com.hyjf.mybatis.model.auto.ActdecSpringList;

public class SpringActResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    /**
     * 累计数量
     */
    int totalNumber;
    public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public int getAvailableNumber() {
		return availableNumber;
	}
	public void setAvailableNumber(int availableNumber) {
		this.availableNumber = availableNumber;
	}
	public int getNewRecharge() {
		return newRecharge;
	}
	public void setNewRecharge(int newRecharge) {
		this.newRecharge = newRecharge;
	}
	public int getNewInvestment() {
		return newInvestment;
	}
	public void setNewInvestment(int newInvestment) {
		this.newInvestment = newInvestment;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getQuan() {
		return quan;
	}
	public void setQuan(String quan) {
		this.quan = quan;
	}
	public List<ActdecSpringList> getZhongjiang() {
		return zhongjiang;
	}
	public void setZhongjiang(List<ActdecSpringList> zhongjiang) {
		this.zhongjiang = zhongjiang;
	}
	/**
     * 可用数量
     */
	int availableNumber;
    /**
     * 新增充值
     */
    int newRecharge;
	/**
     * 新增出借余数
     */
    int newInvestment;
	/**
     * 账户余额
     */
    String balance;
	/**
     *活动已领取券
     */
    String quan;
	/**
     * 活动中奖列表
     */
    List<ActdecSpringList> zhongjiang;
}
