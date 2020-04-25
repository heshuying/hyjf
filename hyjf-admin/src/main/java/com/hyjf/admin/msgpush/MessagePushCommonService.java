package com.hyjf.admin.msgpush;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;

public interface MessagePushCommonService extends BaseService{

    
    /**
     * 获取标签列表
     * 
     * @return
     */
    public List<MessagePushTag> getTagList();

    
	/**
	 * 推送极光消息
	 * @param msg
	 * @return 成功返回消息id  失败返回 error
	 * @author Michael
	 */
    public void sendMessage(MessagePushMsgHistory msg);
    
}