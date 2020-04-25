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

import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class PlanBorrowListAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**序列化id*/
	private static final long serialVersionUID = 5013753434843143557L;
	//计划加入详情
	private List<DebtPlanBorrowCustomize> planBorrowList;

	/**构造方法*/
	public PlanBorrowListAjaxBean() {
		super();
	}

	public List<DebtPlanBorrowCustomize> getPlanBorrowList() {
		return planBorrowList;
	}

	public void setPlanBorrowList(List<DebtPlanBorrowCustomize> planBorrowList) {
		this.planBorrowList = planBorrowList;
	}

}
