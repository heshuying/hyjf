package com.hyjf.app.msgpush;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;

public interface MsgPushService extends BaseService{

	/**
	 * 获得标签列表数量
	 * @return
	 */
	public Integer countMsgPushTagRecord(Integer userId);
	

	/**
	 * 获得标签列表
	 * 
	 * @return
	 */
	public List<MessagePushTag> getMsgPushTagList(Integer userId);

	
	/**
	 * 获得标签信息
	 * 
	 * @return
	 */
	public MessagePushTag getMsgPushTagById(Integer tagId);
	
	
	/**
	 * 获得消息列表数量
	 * @return
	 */
	public Integer countMsgRecord(Integer tagId);

	/**
	 * 获得消息列表
	 * 
	 * @return
	 */
	public List<MessagePushMsg> getMsgList(Integer tagId,int limitStart,int limitEnd);
	
	
	/**
	 * 获得具体信息
	 * 
	 * @return
	 */
	public MessagePushMsgHistory getMsgPushMsgHistoryById(Integer msgId);

	
	/**
	 * 更新历史记录信息
	 * 
	 * @return
	 */
	public void updateMsgPushMsgHistory(MessagePushMsgHistory msgHistory);
	
	
	
	
	/**
	 * 获得消息列表数量
	 * @return
	 */
	public Integer countMsgHistoryRecord(Integer tagId,Integer userId,String platform);

	/**
	 *  获得消息列表
	 * @param tagId
	 * @param userId
	 * @param platform
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<MessagePushMsgHistory> getMsgHistoryList(Integer tagId,Integer userId,String platform,int limitStart,int limitEnd);

	
	
	
	/**
	 * 判断用户是否有未读消息
	 * @param userId 用户id
	 * @param tagId	标签id
	 * @return 0 有未读消息  1没有
	 */
	public String isHaveReadNotice(Integer userId,Integer tagId,String platform);
	
	
	/**
	 * 获取用户最新一条消息数据
	 * @param userId 用户id
	 * @param tagId	标签id
	 * @param platform	终端
	 * @return 
	 */
	public MessagePushMsgHistory getNewestNotice(Integer userId,Integer tagId,String platform);
	
	/**
	 * 更新用户读取时间
	 * @param userId
	 * @param tagId
	 * @param msgType  1获取消息盒子 2获取消息列表
	 */
	public void updateMsgReadTime(Integer userId,Integer tagId,int msgType);

	/**
	 * 消息全部已读
	 * @param userId
	 */
    void updateAllMsgPushMsgHistory(Integer userId, String platform);
}
