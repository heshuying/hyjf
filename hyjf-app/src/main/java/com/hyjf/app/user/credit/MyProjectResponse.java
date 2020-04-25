package com.hyjf.app.user.credit;

import com.hyjf.app.BaseResultBean;

import java.util.List;

/**
 * @author fuqiang
 */
public class MyProjectResponse extends BaseResultBean {
    // 债权总数
    private int projectTotal;
    // 债权总数列表
    private List<MyProjectVo> projectList;

    // 待收总额
    private String money = "";

    public MyProjectResponse(String request) {
        super(request);
    }

    public int getProjectTotal() {
        return projectTotal;
    }

    public void setProjectTotal(int projectTotal) {
        this.projectTotal = projectTotal;
    }

    public List<MyProjectVo> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<MyProjectVo> projectList) {
        this.projectList = projectList;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
