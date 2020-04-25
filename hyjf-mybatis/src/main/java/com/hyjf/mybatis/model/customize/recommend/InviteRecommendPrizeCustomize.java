package com.hyjf.mybatis.model.customize.recommend;

import java.io.Serializable;

public class InviteRecommendPrizeCustomize implements Serializable {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3968821686535757115L;
    
    //推荐星使用数量
    private String prizeCount;
    //奖品名称
    private String prizeName;
    //推荐星使用数量
    private String addTime;
    //奖品发放方式
    private String prizeKind;
    public String getPrizeCount() {
        return prizeCount;
    }
    public void setPrizeCount(String prizeCount) {
        this.prizeCount = prizeCount;
    }
    public String getPrizeName() {
        return prizeName;
    }
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getPrizeKind() {
        return prizeKind;
    }
    public void setPrizeKind(String prizeKind) {
        this.prizeKind = prizeKind;
    }
   
}
