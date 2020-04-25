package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther:yangchangwei
 * @Date:2018/7/4
 * @Description: 3.0.9 版本以上app首页推荐标的
 */
public class AppHomePageCustomizeNew implements Serializable {

    private String title = "";

    private List<AppHomePageCustomize> projectlist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AppHomePageCustomize> getProjectlist() {
        return projectlist;
    }

    public void setProjectlist(List<AppHomePageCustomize> projectlist) {
        this.projectlist = projectlist;
    }
}
