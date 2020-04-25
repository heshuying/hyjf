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
 * @author Administrator
 */

public class DebtBorrowApplyCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 姓名 检索条件
	 */
	private String nameSrch;
	/**
	 * 审核状态 检索条件
	 */
	private String stateSrch;
	/**
	 * 申请时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 申请时间 检索条件
	 */
	private String timeEndSrch;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 手机号
	 */
	private String tel;
	/**
	 * 所在地
	 */
	private String address;
	/**
	 * 融资金额
	 */
	private String money;
	/**
	 * 融资期限
	 */
	private String day;
	/**
	 * 审核状态
	 */
	private String state;

	/**
	 * 审核状态
	 */
	private String stateName;

	/**
	 * 申请时间
	 */
	private String addtime;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * nameSrch
	 * 
	 * @return the nameSrch
	 */

	public String getNameSrch() {
		return nameSrch;
	}

	/**
	 * @param nameSrch
	 *            the nameSrch to set
	 */

	public void setNameSrch(String nameSrch) {
		this.nameSrch = nameSrch;
	}

	/**
	 * stateSrch
	 * 
	 * @return the stateSrch
	 */

	public String getStateSrch() {
		return stateSrch;
	}

	/**
	 * @param stateSrch
	 *            the stateSrch to set
	 */

	public void setStateSrch(String stateSrch) {
		this.stateSrch = stateSrch;
	}

	/**
	 * timeStartSrch
	 * 
	 * @return the timeStartSrch
	 */

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	/**
	 * @param timeStartSrch
	 *            the timeStartSrch to set
	 */

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	/**
	 * timeEndSrch
	 * 
	 * @return the timeEndSrch
	 */

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	/**
	 * @param timeEndSrch
	 *            the timeEndSrch to set
	 */

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
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
	 * sex
	 * 
	 * @return the sex
	 */

	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */

	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * tel
	 * 
	 * @return the tel
	 */

	public String getTel() {
		return tel;
	}

	/**
	 * @param tel
	 *            the tel to set
	 */

	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * address
	 * 
	 * @return the address
	 */

	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * money
	 * 
	 * @return the money
	 */

	public String getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */

	public void setMoney(String money) {
		this.money = money;
	}

	/**
	 * day
	 * 
	 * @return the day
	 */

	public String getDay() {
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */

	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * state
	 * 
	 * @return the state
	 */

	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * addtime
	 * 
	 * @return the addtime
	 */

	public String getAddtime() {
		return addtime;
	}

	/**
	 * @param addtime
	 *            the addtime to set
	 */

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */

	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
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
	 * stateName
	 * 
	 * @return the stateName
	 */

	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName
	 *            the stateName to set
	 */

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

}
