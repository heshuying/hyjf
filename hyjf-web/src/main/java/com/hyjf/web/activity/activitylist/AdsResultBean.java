package com.hyjf.web.activity.activitylist;

import java.util.List;

import com.hyjf.web.WebBaseAjaxResultBean;

public class AdsResultBean extends WebBaseAjaxResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -7274694682649646565L;

    private List<AdsBean> adsList;

    public List<AdsBean> getAdsList() {
        return adsList;
    }

    public void setAdsList(List<AdsBean> adsList) {
        this.adsList = adsList;
    }

}
