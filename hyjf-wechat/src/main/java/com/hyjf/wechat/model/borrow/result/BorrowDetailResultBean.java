package com.hyjf.wechat.model.borrow.result;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.model.borrow.BorrowProjectDetailBean;
import com.hyjf.wechat.model.borrow.BorrowRepayPlanBean;
import com.hyjf.wechat.model.borrow.WxBorrowProjectInfoBean;

public class BorrowDetailResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 8959137696050695655L;

    private JSONObject userValidation;
    private WxBorrowProjectInfoBean projectInfo;
    private List<BorrowProjectDetailBean> projectDetail;
    private List<BorrowRepayPlanBean> repayPlan;
    //风控信息
    private AppRiskControlCustomize appRiskControlCustomize;
    
    public JSONObject getUserValidation() {
        return userValidation;
    }
    public String borrowMeasuresMea;//风控措施

    public void setUserValidation(JSONObject userValidation) {
        this.userValidation = userValidation;
    }
    public WxBorrowProjectInfoBean getProjectInfo() {
        return projectInfo;
    }
    public void setProjectInfo(WxBorrowProjectInfoBean projectInfo) {
        this.projectInfo = projectInfo;
    }
    public List<BorrowProjectDetailBean> getProjectDetail() {
        return projectDetail;
    }
    public void setProjectDetail(List<BorrowProjectDetailBean> projectDetail) {
        this.projectDetail = projectDetail;
    }
    public List<BorrowRepayPlanBean> getRepayPlan() {
        return repayPlan;
    }
    public void setRepayPlan(List<BorrowRepayPlanBean> repayPlan) {
        this.repayPlan = repayPlan;
    }
	public AppRiskControlCustomize getAppRiskControlCustomize() {
		return appRiskControlCustomize;
	}
	public void setAppRiskControlCustomize(AppRiskControlCustomize appRiskControlCustomize) {
		this.appRiskControlCustomize = appRiskControlCustomize;
	}
    public String getBorrowMeasuresMea() {
        return borrowMeasuresMea;
    }

    public void setBorrowMeasuresMea(String borrowMeasuresMea) {
        this.borrowMeasuresMea = borrowMeasuresMea;
    }
}
