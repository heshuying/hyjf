package com.hyjf.app.find.contentarticle;


import com.hyjf.app.BaseResultBeanFrontEnd;

public class AppContentArticleResultBean extends BaseResultBeanFrontEnd {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 5825234430507653546L;
    private Object details;
    private String topTitle;
    public Object getDetails() {
        return details;
    }
    public void setDetails(Object details) {
        this.details = details;
    }
    public String getTopTitle() {
        return topTitle;
    }
    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }
    
    
}
