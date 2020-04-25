package com.hyjf.activity.mgm10.prizechange;

import com.hyjf.base.bean.BaseResultBean;


public class PrizeListResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户推荐星可用数
     */
    private int recommendCount;

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }
    
    
}
