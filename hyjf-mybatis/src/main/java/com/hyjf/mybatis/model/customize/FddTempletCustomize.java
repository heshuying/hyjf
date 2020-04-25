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

package com.hyjf.mybatis.model.customize;


import com.hyjf.mybatis.model.auto.FddTemplet;

import java.io.Serializable;

/**
 * @author gaolang
 */

public class FddTempletCustomize extends FddTemplet implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 协议类型名称
	 */
	private String protocolTypeName;
	
	/**
	 * CA认证名称
	 */
    private String caFlagName;

    protected int limitStart = -1;

    protected int limitEnd = -1;

	public String getProtocolTypeName() {
		return protocolTypeName;
	}

	public void setProtocolTypeName(String protocolTypeName) {
		this.protocolTypeName = protocolTypeName;
	}

	public String getCaFlagName() {
		return caFlagName;
	}

	public void setCaFlagName(String caFlagName) {
		this.caFlagName = caFlagName;
	}

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
}
