package com.hyjf.mybatis.model.customize.message.coupon;

import com.hyjf.mybatis.model.auto.SiteMsgLog;

public class SiteMsgLogCustomize extends SiteMsgLog{

    private static final long serialVersionUID = 1L;
    
    /**
     * 模板名称
     */
    public String tplName;
 
    /**
     * 添加时间字符串格式
     */
    public String addTimeString;
    
    /**
     * 更新时间字符串格式
     */
    public String updateTimeString;
    
    /**
     * 接收消息用户名
     */
    public String toUsername;
    
    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public String getAddTimeString() {
        return addTimeString;
    }

    public void setAddTimeString(String addTimeString) {
        this.addTimeString = addTimeString;
    }

    public String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
    
}
