package com.hyjf.activity.mgm10.regist;

import java.io.Serializable;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 返回结果
 * @author zhangjinpeng
 *
 */
public class RegistRecommendResultBean extends BaseResultBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3521682905895830206L;

	/**
	 * 异常编号
	 */
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
    
}
