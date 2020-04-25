package com.hyjf.admin.msgpush.error;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;

public interface MessagePushErrorService extends BaseService{

    /**
     * 获取列表
     * 
     * @return
     */
    public List<MessagePushMsgHistory> getRecordList(MessagePushErrorBean bean, int limitStart, int limitEnd);

    /**
     * 获取列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(MessagePushErrorBean form);

    
    /**
     * 获取单个信息
     * 
     * @return
     */
    public MessagePushMsgHistory getRecord(Integer record);   
    
    
    /**
     * 获取标签列表
     * 
     * @return
     */
    public List<MessagePushTag> getTagList();

    
    
}