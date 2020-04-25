package com.hyjf.batch.msgpush;

import java.util.Date;
import java.util.List;

import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTemplate;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;

/**
 * 
 * <p>
 * Title:MsgpushService
 * </p>
 * <p>
 * Description: 消息推送service
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 青狐小宝
 * @date 2016年8月9日 下午5:43:11
 */
public interface MsgpushService {
	/**
	 * 一、获取所有需要推送的消息定义
	 * 
	 * @return
	 */
	public List<MessagePushMsg> getAllMessage();


	/********************** 模板消息统计报表 *****************************/

	/**
	 * 根据时间取发送记录
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MessagePushMsg> getMsgStaticsListByTime(Integer startTime, Integer endTime);

	/**
	 * 根据消息编码取发送历史记录
	 * 
	 * @param msgCode
	 * @return
	 */
	public List<MessagePushMsgHistory> getMsgHistoryListByMsgCode(String msgCode,Integer startTime,Integer endTime);
	
	/**
	 * 插入模板统计数据
	 * @param msg
	 */
	public void insertTemplateStatics(MessagePushMsg msg);
	/**
	 * 更新模板统计数据
	 * @param msgTemplateStatics
	 */
	public void updateTemplateStatics(MessagePushTemplateStatics msgTemplateStatics,Integer startTime,Integer endTimes);
	/**
	 * 根据时间获取模板统计数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MessagePushTemplateStatics> getTemplateStaticsListByTime(Integer startTime,Integer endTime);
	
	/**
	 * 获取所有推送模板
	 * @return
	 */
	public List<MessagePushTemplate> getAllTemplates();
	
	
	/**
	 * 插入模板统计数据(根据模板插入)
	 * @param msg
	 */
	public void insertTemplateStatics(MessagePushTemplate template);
	
	
	/*************************** 平台消息统计报表 ****************************/

	/**
	 * 获取标签类型
	 * 
	 * @return
	 */
	public List<MessagePushTag> getPushTags();

	/**
	 * 根据标签类型,获取时间范围内所有的msg
	 * 
	 * @param tag
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<MessagePushMsg> getMessagePushMsgs(MessagePushTag tag, Date beginTime, Date endTime);

	/**
	 * 根据标签类型,获取时间范围内所有的msghistory
	 * 
	 * @param tag
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<MessagePushMsgHistory> getMessagePushMsgHistorys(MessagePushTag tag, Date beginTime, Date endTime);

	/**
	 * 根据msg和msghistory插入当天统计信息
	 * 
	 * @param tag
	 * @param msgHistorys
	 * @param time
	 * @return
	 */
	public void insertPushPlatStatics(MessagePushTag tag, List<MessagePushMsgHistory> msgHistorys, Date time);

}
