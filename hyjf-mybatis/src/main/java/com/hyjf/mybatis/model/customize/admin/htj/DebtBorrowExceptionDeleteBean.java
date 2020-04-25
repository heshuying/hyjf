/**
 * Description:保存删除的borrow信息的日志记录
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: zhuxiaodong
 * @version: 1.0
 * Created at: 2016年3月9日 下午7:51:30
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuxiaodong
 */

public class DebtBorrowExceptionDeleteBean implements Serializable{
	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/*借款的识别名*/
	private String borrow_nid;
	/*标题*/
	private String borrow_name;
	/*借款人人名*/
	private String username;
	/*借贷总金额*/
	private String account;
	/*已借到的金额*/
	private String borrow_account_yes;
	/*剩余金额*/
	private String borrow_account_wait;
	/*借贷的完成率*/
	private String borrow_account_scale;
	/*还款方式*/
	private String borrow_style;
	/*名称*/
	private String borrow_style_name;
	/*0汇保贷 1汇典贷 2汇小贷 3汇车贷 4新手标*/
	private int project_type;
	/*名称*/
	private String project_type_name;
	/*借款期限*/
	private String borrow_period;
	/*借款利率*/
	private String borrow_apr;
	/*借贷的完成率*/
	private String status;
	/*添加时间*/
	private String addtime;
	/*满标时间*/
	private String borrow_full_time;
	/*放款完成时间*/
	private String recover_last_time;
	/*保证金*/
	private BigDecimal bail_num;
	/*操作人*/
	private int operater_uid;
	/*操作人名称*/
	private String operater_user;
	/*操作时间*/
	private int operater_time;
	/*操作时间Str*/
	private String operater_time_str;
	
	/*操作开始时间*/
	private int operater_time_start;
	/*操作截止时间*/
	private int operater_time_end;
	/**
	 * borrow_nid
	 * @return the borrow_nid
	 */
	
	public String getBorrow_nid() {
		return borrow_nid;
	}
	/**
	 * @param borrow_nid the borrow_nid to set
	 */
	
	public void setBorrow_nid(String borrow_nid) {
		this.borrow_nid = borrow_nid;
	}
	/**
	 * borrow_name
	 * @return the borrow_name
	 */
	
	public String getBorrow_name() {
		return borrow_name;
	}
	/**
	 * @param borrow_name the borrow_name to set
	 */
	
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	/**
	 * username
	 * @return the username
	 */
	
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * account
	 * @return the account
	 */
	
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * borrow_account_yes
	 * @return the borrow_account_yes
	 */
	
	public String getBorrow_account_yes() {
		return borrow_account_yes;
	}
	/**
	 * @param borrow_account_yes the borrow_account_yes to set
	 */
	
	public void setBorrow_account_yes(String borrow_account_yes) {
		this.borrow_account_yes = borrow_account_yes;
	}
	/**
	 * borrow_account_wait
	 * @return the borrow_account_wait
	 */
	
	public String getBorrow_account_wait() {
		return borrow_account_wait;
	}
	/**
	 * @param borrow_account_wait the borrow_account_wait to set
	 */
	
	public void setBorrow_account_wait(String borrow_account_wait) {
		this.borrow_account_wait = borrow_account_wait;
	}
	/**
	 * borrow_account_scale
	 * @return the borrow_account_scale
	 */
	
	public String getBorrow_account_scale() {
		return borrow_account_scale;
	}
	/**
	 * @param borrow_account_scale the borrow_account_scale to set
	 */
	
	public void setBorrow_account_scale(String borrow_account_scale) {
		this.borrow_account_scale = borrow_account_scale;
	}
	/**
	 * borrow_style
	 * @return the borrow_style
	 */
	
	public String getBorrow_style() {
		return borrow_style;
	}
	/**
	 * @param borrow_style the borrow_style to set
	 */
	
	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}
	/**
	 * borrow_style_name
	 * @return the borrow_style_name
	 */
	
	public String getBorrow_style_name() {
		return borrow_style_name;
	}
	/**
	 * @param borrow_style_name the borrow_style_name to set
	 */
	
	public void setBorrow_style_name(String borrow_style_name) {
		this.borrow_style_name = borrow_style_name;
	}
	/**
	 * project_type
	 * @return the project_type
	 */
	
	public int getProject_type() {
		return project_type;
	}
	/**
	 * @param project_type the project_type to set
	 */
	
	public void setProject_type(int project_type) {
		this.project_type = project_type;
	}
	/**
	 * project_type_name
	 * @return the project_type_name
	 */
	
	public String getProject_type_name() {
		return project_type_name;
	}
	/**
	 * @param project_type_name the project_type_name to set
	 */
	
	public void setProject_type_name(String project_type_name) {
		this.project_type_name = project_type_name;
	}
	/**
	 * borrow_period
	 * @return the borrow_period
	 */
	
	public String getBorrow_period() {
		return borrow_period;
	}
	/**
	 * @param borrow_period the borrow_period to set
	 */
	
	public void setBorrow_period(String borrow_period) {
		this.borrow_period = borrow_period;
	}
	/**
	 * borrow_apr
	 * @return the borrow_apr
	 */
	
	public String getBorrow_apr() {
		return borrow_apr;
	}
	/**
	 * @param borrow_apr the borrow_apr to set
	 */
	
	public void setBorrow_apr(String borrow_apr) {
		this.borrow_apr = borrow_apr;
	}
	/**
	 * status
	 * @return the status
	 */
	
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * addtime
	 * @return the addtime
	 */
	
	public String getAddtime() {
		return addtime;
	}
	/**
	 * @param addtime the addtime to set
	 */
	
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	/**
	 * borrow_full_time
	 * @return the borrow_full_time
	 */
	
	public String getBorrow_full_time() {
		return borrow_full_time;
	}
	/**
	 * @param borrow_full_time the borrow_full_time to set
	 */
	
	public void setBorrow_full_time(String borrow_full_time) {
		this.borrow_full_time = borrow_full_time;
	}
	/**
	 * recover_last_time
	 * @return the recover_last_time
	 */
	
	public String getRecover_last_time() {
		return recover_last_time;
	}
	/**
	 * @param recover_last_time the recover_last_time to set
	 */
	
	public void setRecover_last_time(String recover_last_time) {
		this.recover_last_time = recover_last_time;
	}
	/**
	 * bail_num
	 * @return the bail_num
	 */
	
	public BigDecimal getBail_num() {
		return bail_num;
	}
	/**
	 * @param bail_num the bail_num to set
	 */
	
	public void setBail_num(BigDecimal bail_num) {
		this.bail_num = bail_num;
	}
	/**
	 * operater_uid
	 * @return the operater_uid
	 */
	
	public int getOperater_uid() {
		return operater_uid;
	}
	/**
	 * @param operater_uid the operater_uid to set
	 */
	
	public void setOperater_uid(int operater_uid) {
		this.operater_uid = operater_uid;
	}
	
	public String getOperater_user() {
		return operater_user;
	}
	public void setOperater_user(String operater_user) {
		this.operater_user = operater_user;
	}
	/**
	 * operater_time
	 * @return the operater_time
	 */
	
	public int getOperater_time() {
		return operater_time;
	}
	/**
	 * @param operater_time the operater_time to set
	 */
	
	public void setOperater_time(int operater_time) {
		this.operater_time = operater_time;
	}
	public int getOperater_time_start() {
		return operater_time_start;
	}
	public void setOperater_time_start(int operater_time_start) {
		this.operater_time_start = operater_time_start;
	}
	public int getOperater_time_end() {
		return operater_time_end;
	}
	public void setOperater_time_end(int operater_time_end) {
		this.operater_time_end = operater_time_end;
	}
	public String getOperater_time_str() {
		return operater_time_str;
	}
	public void setOperater_time_str(String operater_time_str) {
		this.operater_time_str = operater_time_str;
	}
}

	