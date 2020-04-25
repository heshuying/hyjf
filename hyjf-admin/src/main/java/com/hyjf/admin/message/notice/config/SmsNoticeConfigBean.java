package com.hyjf.admin.message.notice.config;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.SmsNoticeConfigWithBLOBs;

/**
 * @package com.hyjf.admin.message
 * @author Gaolang
 * @date 2015/11/26 17:00
 * @version V1.0  
 */
public class SmsNoticeConfigBean extends SmsNoticeConfigWithBLOBs implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 标识接受数据，获取时name会自动拼接字段，用这个参数做接受
	 */
	private String configName;

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	
}
