package com.hyjf.activity.landingpage.wx;

import com.hyjf.base.bean.BaseResultBean;

public class LandingPageResultBean extends BaseResultBean {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9174539916567994537L;
    
    /**
     * 着陆页开启状态：0 未开启  1 已开启
     */
    public Integer statusOn;

    public Integer getStatusOn() {
        return statusOn;
    }

    public void setStatusOn(Integer statusOn) {
        this.statusOn = statusOn;
    }
    
    
}