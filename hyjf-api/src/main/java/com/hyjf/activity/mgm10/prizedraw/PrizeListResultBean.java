package com.hyjf.activity.mgm10.prizedraw;

import java.util.List;
import java.util.Map;

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
    
    /**
     * 抽奖消耗推荐星数量
     */
    private int needCount;

    /**
     * 可参与抽奖次数
     */
    private int canDrawCount;
    
    private List<Map<String,Object>> prizeWinList;
    
    public int getRecommendCount() {
        return recommendCount;
    }
    
    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    public int getCanDrawCount() {
        return canDrawCount;
    }

    public void setCanDrawCount(int canDrawCount) {
        this.canDrawCount = canDrawCount;
    }

    public List<Map<String, Object>> getPrizeWinList() {
        return prizeWinList;
    }

    public void setPrizeWinList(List<Map<String, Object>> prizeWinList) {
        this.prizeWinList = prizeWinList;
    }

    public int getNeedCount() {
        return needCount;
    }

    public void setNeedCount(int needCount) {
        this.needCount = needCount;
    }



    
}
