package com.hyjf.borrow.mytender;

import com.hyjf.base.bean.BaseResultBean;

public class MyTenderListResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -1730016219995523332L;
    
    /**
     * 列表记录数
     */
    private String projectTotal;

    public String getProjectTotal() {
        return projectTotal;
    }

    public void setProjectTotal(String projectTotal) {
        this.projectTotal = projectTotal;
    }
    
    

}
