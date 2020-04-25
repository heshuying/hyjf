package com.hyjf.callcenter.user;

import java.io.Serializable;

import com.hyjf.callcenter.base.BaseFormBean;

/**
 * 查询用户查询条件Bean
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
public class UserBean extends BaseFormBean implements Serializable  {
	
	private static final long serialVersionUID = 2569541349422133226L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 检索记录开始条数（从0开始）
	 */
	private Integer limitStart;

	/**
	 * 检索记录条数
	 */
	private Integer limitSize;
	
	/**
	 * flag
	 */
	private String flag;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(Integer limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitSize
	 * @return the limitSize
	 */
	
	public Integer getLimitSize() {
		return limitSize;
	}

	/**
	 * @param limitSize the limitSize to set
	 */
	
	public void setLimitSize(Integer limitSize) {
		this.limitSize = limitSize;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author lb
	 */

	/**
	 * flag
	 * @return the flag
	 */
	
	public String getFlag() {
		return flag;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author lb
	 */
		
	@Override
	public String toString() {
		return "UserBean [userName=" + userName + ", mobile=" + mobile + ", limitStart=" + limitStart + ", limitSize="
				+ limitSize + ", flag=" + flag + "]";
	}

	/**
	 * @param flag the flag to set
	 */
	
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
