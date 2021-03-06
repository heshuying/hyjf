package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * 会员更新操作时查询OA表用到的实体
 * 
 * @author 孙亮
 * @since 2016年1月5日 上午11:06:25
 */
public class UserUpdateParamCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户姓名
	 */
	private String truename;
	/**
	 * 部门类型,拿部门ID字段存(1线上,2线下)
	 */
	private String cuttype;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getCuttype() {
		return cuttype;
	}

	public void setCuttype(String cuttype) {
		this.cuttype = cuttype;
	}

}
