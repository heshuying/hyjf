package com.hyjf.wechat.controller.user.myAsset;

import com.google.common.collect.Lists;
import com.hyjf.mybatis.model.customize.web.CurrentHoldPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentPlanListCustomize;

import java.util.List;

/**
 * Created by cuigq on 2018/2/8.
 */
public class QueryPlanedProjectVO {

    private List<CurrentHoldPlanListCustomize> lstHoldPlanProject;

    private List<RepayMentPlanListCustomize> lstRepayMentPlanProject;

    public List<CurrentHoldPlanListCustomize> getLstHoldPlanProject() {
        if (lstHoldPlanProject == null) {
            lstHoldPlanProject = Lists.newArrayList();
        }
        return lstHoldPlanProject;
    }

    public void setLstHoldPlanProject(List<CurrentHoldPlanListCustomize> lstHoldPlanProject) {
        this.lstHoldPlanProject = lstHoldPlanProject;
    }

    public List<RepayMentPlanListCustomize> getLstRepayMentPlanProject() {
        if (lstRepayMentPlanProject == null) {
            lstRepayMentPlanProject = Lists.newArrayList();
        }
        return lstRepayMentPlanProject;
    }

    public void setLstRepayMentPlanProject(List<RepayMentPlanListCustomize> lstRepayMentPlanProject) {
        this.lstRepayMentPlanProject = lstRepayMentPlanProject;
    }
}
