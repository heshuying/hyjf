/**
 * Description:接口中间跳转bean
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: liubin
 * @version: 1.0
 *           Created at: 2017年9月20日 下午4:19:43
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.api.skip;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.math.NumberUtils;

import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.validator.Validator;

/**
 * @author Administrator
 */
public class ApiSkipFormBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -20545325472232432L;

	/** Action */
	private String action;

	/**
	 * 参数Map
	 */
	private TreeMap<String, String> paramMap;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 构造函数
	 */
	public ApiSkipFormBean() {
		paramMap = new TreeMap<String, String>();
	}

	/**
	 * 设置属性值
	 *
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		paramMap.put(key, value);
	}

	/**
	 * 设置属性值
	 *
	 * @param key
	 * @param value
	 */
	public void setAll(Map<String, String> map) {
		paramMap.putAll(map);
	}

	/**
	 * 根据Key取得值
	 *
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return paramMap.get(key);
	}

	/**
	 * 根据Key取得值
	 *
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		return GetterUtil.getInteger(paramMap.get(key));
	}

	/**
	 * 根据Key取得值
	 *
	 * @param key
	 * @return
	 */
	public Long getLong(String key) {
		return GetterUtil.getLong(paramMap.get(key));
	}

	/**
	 * 根据Key取得值
	 *
	 * @param key
	 * @return
	 */
	public BigDecimal getBigDecimal(String key) {
		String val = paramMap.get(key);
		if (Validator.isNotNull(val) && NumberUtils.isNumber(val)) {
			return new BigDecimal(val);
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 取得参数Map
	 *
	 * @return
	 */
	public Map<String, String> getAllParams() {
		return paramMap;
	}
}
