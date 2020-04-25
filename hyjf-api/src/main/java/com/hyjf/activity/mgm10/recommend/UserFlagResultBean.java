package com.hyjf.activity.mgm10.recommend;

import com.hyjf.base.bean.BaseResultBean;


public class UserFlagResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    
    private String isInvest;
    private String isStaff;
    
    public String getIsInvest() {
        return isInvest;
    }
    public void setIsInvest(String isInvest) {
        this.isInvest = isInvest;
    }
    public String getIsStaff() {
        return isStaff;
    }
    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
    
    
    
}
