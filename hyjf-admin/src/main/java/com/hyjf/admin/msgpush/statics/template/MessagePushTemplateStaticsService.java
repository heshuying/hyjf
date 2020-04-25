package com.hyjf.admin.msgpush.statics.template;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;

public interface MessagePushTemplateStaticsService extends BaseService{

    /**
     * 获取列表
     * 
     * @return
     */
    public List<MessagePushTemplateStatics> getRecordList(MessagePushTemplateStaticsBean bean, int limitStart, int limitEnd);

    /**
     * 获取列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(MessagePushTemplateStaticsBean form);

    
    /**
     * 获取单个信息
     * 
     * @return
     */
    public MessagePushTemplateStatics getRecord(Integer record);   
    
    
    /**
     * 获取标签列表
     * 
     * @return
     */
    public List<MessagePushTag> getTagList();

    
    
}