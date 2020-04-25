package com.hyjf.activity.mgm10.prizechange;

import com.hyjf.base.bean.BaseBean;

public class PrizeChangeBean extends BaseBean {
    /**
     * 奖品兑换数量
     */
    private Integer changeCount;
    
    /**
     * 奖品分组编码
     */
    private String groupCode;

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
    
    
}
