package com.hyjf.activity.mgm10.recommend;

import com.hyjf.base.bean.BaseResultBean;


public class RecommendResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户推荐星可用数
     */
    private int prizeAllCount;
    private int prizeUsedCount;
    private int prizeSurplusCount;
    
    
    public int getPrizeAllCount() {
        return prizeAllCount;
    }
    public void setPrizeAllCount(int prizeAllCount) {
        this.prizeAllCount = prizeAllCount;
    }
    public int getPrizeUsedCount() {
        return prizeUsedCount;
    }
    public void setPrizeUsedCount(int prizeUsedCount) {
        this.prizeUsedCount = prizeUsedCount;
    }
    public int getPrizeSurplusCount() {
        return prizeSurplusCount;
    }
    public void setPrizeSurplusCount(int prizeSurplusCount) {
        this.prizeSurplusCount = prizeSurplusCount;
    }

    
    
}
