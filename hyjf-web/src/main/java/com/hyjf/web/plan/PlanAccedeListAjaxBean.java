/**
 * Description:项目出借用户列表查询所用vo
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

import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class PlanAccedeListAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**序列化id*/
	private static final long serialVersionUID = 5013753434843143557L;
	//计划加入详情
	private List<DebtPlanAccedeCustomize> planAccedeList;
	//计划加入总金额
	private String accedeTotal;
	//计划加入次数
	private String accedeTimes;

	/**构造方法*/
	public PlanAccedeListAjaxBean() {
		super();
	}

	public List<DebtPlanAccedeCustomize> getPlanAccedeList() {
		return planAccedeList;
	}

	public void setPlanAccedeList(List<DebtPlanAccedeCustomize> planAccedeList) {
		this.planAccedeList = planAccedeList;
	}

	public String getAccedeTotal() {
		return accedeTotal;
	}

	public void setAccedeTotal(String accedeTotal) {
		this.accedeTotal = accedeTotal;
	}

	public String getAccedeTimes() {
		return accedeTimes;
	}

	public void setAccedeTimes(String accedeTimes) {
		this.accedeTimes = accedeTimes;
	}


}
