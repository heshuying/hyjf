package com.hyjf.app.user.transfer;


import java.util.List;

import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.project.RepayPlanBean;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.app.AppTransferDetailCustomize;

public class TransferDetailResultBean extends BaseResultBeanFrontEnd {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 5825234430507653546L;
    private Object userValidation;
    private AppTransferDetailCustomize  projectInfo;
    private List<Object> projectDetail;
    private List<RepayPlanBean> repayPlan;
    private AppRiskControlCustomize riskControl ;

    public Object getUserValidation() {
        return userValidation;
    }

    public void setUserValidation(Object userValidation) {
        this.userValidation = userValidation;
    }

    public AppTransferDetailCustomize getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(AppTransferDetailCustomize projectInfo) {
        this.projectInfo = projectInfo;
    }

    public List<Object> getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(List<Object> projectDetail) {
        this.projectDetail = projectDetail;
    }

    public List<RepayPlanBean> getRepayPlan() {
        return repayPlan;
    }

    public void setRepayPlan(List<RepayPlanBean> repayPlan) {
        this.repayPlan = repayPlan;
    }

    public AppRiskControlCustomize getRiskControl() {
        return riskControl;
    }

    public void setRiskControl(AppRiskControlCustomize riskControl) {
        this.riskControl = riskControl;
    }
}
