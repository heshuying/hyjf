/**
 * Description:项目列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.plan;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.htj.DebtPlanCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class PlanListAJaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -7415746042913857105L;

	private List<DebtPlanCustomize> debtPlanList;
	
	private int nowTime;

	public List<DebtPlanCustomize> getDebtPlanList() {
		return debtPlanList;
	}

	public void setDebtPlanList(List<DebtPlanCustomize> debtPlanList) {
		this.debtPlanList = debtPlanList;
	}

	public int getNowTime() {
		return nowTime;
	}

	public void setNowTime(int nowTime) {
		this.nowTime = nowTime;
	}
	
	
}
