package com.hyjf.mybatis.model.customize.recommend;

import java.io.Serializable;
import java.util.List;

public class InviteInfoCustomize implements Serializable {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3968821686535757115L;
    
    //推荐星发放渠道
    private String recommendSource;
    //推荐好友名称
    private String inviteByUser;
    //推荐星发放数量
    private Integer recommendCount;
    //推荐星发放时间
    private String sendTime;
    
    private Integer source;
    
    private String groupCode;
    
    private List<String> inviteUserName;
    public String getRecommendSource() {
        return recommendSource;
    }
    public void setRecommendSource(String recommendSource) {
        this.recommendSource = recommendSource;
    }
   
    public String getInviteByUser() {
        return inviteByUser;
    }
    public void setInviteByUser(String inviteByUser) {
        this.inviteByUser = inviteByUser;
    }
    public Integer getRecommendCount() {
        return recommendCount;
    }
    public void setRecommendCount(Integer recommendCount) {
        this.recommendCount = recommendCount;
    }
    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public List<String> getInviteUserName() {
        return inviteUserName;
    }
    public void setInviteUserName(List<String> inviteUserName) {
        this.inviteUserName = inviteUserName;
    }
    
    public Integer getSource() {
        return source;
    }
    public void setSource(Integer source) {
        this.source = source;
    }
    public String getGroupCode() {
        return groupCode;
    }
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
    
    
    
    
}
