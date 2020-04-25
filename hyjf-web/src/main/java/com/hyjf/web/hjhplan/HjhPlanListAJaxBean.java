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
package com.hyjf.web.hjhplan;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class HjhPlanListAJaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -7415746042913857105L;

	private List<HjhPlanCustomize> planList;
	
	private int nowTime;

    public List<HjhPlanCustomize> getPlanList() {
        return planList;
    }

    public void setPlanList(List<HjhPlanCustomize> planList) {
        this.planList = planList;
    }

    public int getNowTime() {
        return nowTime;
    }

    public void setNowTime(int nowTime) {
        this.nowTime = nowTime;
    }
	
	
}
