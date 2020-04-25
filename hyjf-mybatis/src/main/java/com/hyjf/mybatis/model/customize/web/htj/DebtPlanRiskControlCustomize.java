/**
 * Description:汇添金计划安全保障
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.mybatis.model.customize.web.htj;

import java.io.Serializable;

public class DebtPlanRiskControlCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = -1272179572549203942L;
	/* 风控保障措施 */
	private String controlMeasures;
	/* 风险保证金措施*/
	private String controlBail;
	
	/**构造方法*/
	public DebtPlanRiskControlCustomize() {
		super();
	}

	public String getControlMeasures() {
		return controlMeasures;
	}

	public void setControlMeasures(String controlMeasures) {
		this.controlMeasures = controlMeasures;
	}

	public String getControlBail() {
		return controlBail;
	}

	public void setControlBail(String controlBail) {
		this.controlBail = controlBail;
	}


}
