package com.hyjf.admin.msgpush.statics.plat;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushPlatStatics;
import com.hyjf.mybatis.model.auto.MessagePushTag;

public interface MessagePushPlatStaticsService extends BaseService{

    /**
     * 获取列表
     * 
     * @return
     */
    public List<MessagePushPlatStatics> getRecordList(MessagePushPlatStaticsBean bean, int limitStart, int limitEnd);

    /**
     * 获取列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(MessagePushPlatStaticsBean form);

    
    /**
     * 获取单个信息
     * 
     * @return
     */
    public MessagePushPlatStatics getRecord(Integer record);   
    
    
    /**
     * 获取标签列表
     * 
     * @return
     */
    public List<MessagePushTag> getTagList();

    
    
}