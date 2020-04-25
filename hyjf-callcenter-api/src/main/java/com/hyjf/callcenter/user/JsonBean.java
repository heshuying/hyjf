package com.hyjf.callcenter.user;

import java.io.Serializable;
import java.util.List;

import com.hyjf.callcenter.base.BaseFormBean;
import com.hyjf.mybatis.model.auto.CallcenterServiceUsers;

/**
 * 查询用户查询条件Bean
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
public class JsonBean extends BaseFormBean implements Serializable  {
	
	private static final long serialVersionUID = 256965480942133226L;
	
	List<CallcenterServiceUsers> userJsonArray;

	/**
	 * userJsonArray
	 * @return the userJsonArray
	 */
	
	public List<CallcenterServiceUsers> getUserJsonArray() {
		return userJsonArray;
	}

	/**
	 * @param userJsonArray the userJsonArray to set
	 */
	
	public void setUserJsonArray(List<CallcenterServiceUsers> userJsonArray) {
		this.userJsonArray = userJsonArray;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author LiuBin
	 */
		
	@Override
	public String toString() {
		return "JsonBean [userJsonArray=" + userJsonArray + "]";
	}

}
