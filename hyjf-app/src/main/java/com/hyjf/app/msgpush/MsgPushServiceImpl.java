package com.hyjf.app.msgpush;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushMsgExample;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistoryExample;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;
import com.hyjf.mybatis.model.auto.MessagePushUserRead;
import com.hyjf.mybatis.model.auto.MessagePushUserReadExample;

@Service
public class MsgPushServiceImpl extends BaseServiceImpl implements MsgPushService {
	private Logger logger = LoggerFactory.getLogger(MsgPushServiceImpl.class);
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countMsgPushTagRecord(Integer userId) {
		MessagePushTagExample example = new MessagePushTagExample();
		MessagePushTagExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);//启用状态
		if(userId == null){ //未登录
			cra.andIsLoginEqualTo(1);
		}
		int cnt = this.messagePushTagMapper.countByExample(example);
		if(cnt > 0){
			return cnt;
		}
		return 0;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<MessagePushTag> getMsgPushTagList(Integer userId) {
		MessagePushTagExample example = new MessagePushTagExample();
		MessagePushTagExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);//启用状态
		if(userId == null){ //未登录
			cra.andIsLoginEqualTo(1);
		}
		example.setOrderByClause("sort asc");
		return this.messagePushTagMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param tagId
	 * @return
	 * @author Michael
	 */
	@Override
	public MessagePushTag getMsgPushTagById(Integer tagId) {
		if(tagId != null){
			return this.messagePushTagMapper.selectByPrimaryKey(tagId);
		}
		return null;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param tagId
	 * @return
	 * @author Michael
	 */
	@Override
	public Integer countMsgRecord(Integer tagId) {
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria cra = example.createCriteria();
		if(tagId != null){
			cra.andTagIdEqualTo(tagId);
		}
		cra.andMsgSendStatusEqualTo(1);//启用状态
		int cnt = this.messagePushMsgMapper.countByExample(example);
		if(cnt > 0){
			return cnt;
		}
		return 0;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param tagId
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Michael
	 */
	@Override
	public List<MessagePushMsg> getMsgList(Integer tagId, int limitStart, int limitEnd) {
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria cra = example.createCriteria();
		if(tagId != null){
			cra.andTagIdEqualTo(tagId);
		}
		cra.andMsgSendStatusEqualTo(1);//启用状态
		example.setOrderByClause("sort asc");
		if(limitStart != -1){
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return this.messagePushMsgMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param msgId
	 * @return
	 * @author Michael
	 */
		
	@Override
	public MessagePushMsgHistory getMsgPushMsgHistoryById(Integer msgId) {
		 MessagePushMsgHistory msg = messagePushMsgHistoryMapper.selectByPrimaryKey(msgId);
	     return msg;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param msgHistory
	 * @author Michael
	 */
	@Override
	public void updateMsgPushMsgHistory(MessagePushMsgHistory msgHistory) {
		messagePushMsgHistoryMapper.updateByPrimaryKeySelective(msgHistory);
	}

	/**
	 * 判断用户是否有未读消息
	 * @param userId 用户id
	 * @param tagId	标签id
	 * @return 0 有未读消息  1没有
	 */
	@Override
	public String isHaveReadNotice(Integer userId,Integer tagId,String platform) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria cra = example.createCriteria();
        if(tagId != null){
        	 cra.andTagIdEqualTo(tagId);
        }
        if(userId != null){
        	 cra.andMsgUserIdEqualTo(userId);//用户id
        }
        if(platform != null){
        	cra.andMsgTerminalLike("%"+platform+"%");
        }
        int readTime = getMsgReadTime(userId, tagId);
        if(readTime > 0){
        	cra.andSendTimeGreaterThanOrEqualTo(readTime);
        }
        //推送到个人
        cra.andMsgDestinationTypeEqualTo(CustomConstants.MSG_PUSH_DESTINATION_TYPE_1);
        //有未读消息
        cra.andMsgReadCountAndroidEqualTo(0);
        cra.andMsgReadCountIosEqualTo(0);
        //发送成功
        cra.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_1);
        int cnt = this.messagePushMsgHistoryMapper.countByExample(example);
        if(cnt > 0){
        	return "0";
        }
		return "1";
	}

	/**
	 * 获取用户上次读取时间
	 * @param userId
	 * @param tagId
	 */
	public int getMsgReadTime(Integer userId, Integer tagId) {
		int readTime = 0;
		if(userId == null){
			return readTime;
		}
		if(tagId == null){
			return readTime;
		}
		MessagePushUserReadExample example = new MessagePushUserReadExample();
		MessagePushUserReadExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andTagIdEqualTo(tagId);
		cra.andTypeEqualTo(2);
		List<MessagePushUserRead> list = this.messagePushUserReadMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			readTime = list.get(0).getReadTime();
		}
		return readTime;
	}

	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param tagId
	 * @param userId
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countMsgHistoryRecord(Integer tagId, Integer userId,String platform) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria cra = example.createCriteria();
        MessagePushMsgHistoryExample.Criteria cra1 = example.createCriteria();
        if(tagId != null){
            //传入的tagId代表类型，如果是0 表示通知，1表示用户消息，对应数据库的msg_destination_type字段
        	cra.andMsgDestinationTypeEqualTo(tagId);
        	cra1.andMsgDestinationTypeEqualTo(tagId);
          }
        cra.andMsgUserIdEqualTo(0);
        cra.andMsgSendStatusEqualTo(1);//发送成功
        cra1.andMsgSendStatusEqualTo(1);//发送成功
        if(userId != null){
//        	cra1.andMsgUserIdIsNotNull();
        	cra1.andMsgUserIdEqualTo(userId);
        }
        if(platform != null){
        	cra.andMsgTerminalLike("%"+platform+"%");
        	cra1.andMsgTerminalLike("%"+platform+"%");
        }
        example.or(cra1);
        int cnt = this.messagePushMsgHistoryMapper.countByExample(example);
        if(cnt > 0){
        	return cnt;
        }
		return 0;
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param tagId
	 * @param userId
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<MessagePushMsgHistory> getMsgHistoryList(Integer tagId, Integer userId,String platform, int limitStart, int limitEnd) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria cra = example.createCriteria();
        MessagePushMsgHistoryExample.Criteria cra1 = example.createCriteria();
        if(tagId != null){
            //传入的tagId代表类型，如果是0 表示通知，1表示用户消息，对应数据库的msg_destination_type字段
        	cra.andMsgDestinationTypeEqualTo(tagId);
        	cra1.andMsgDestinationTypeEqualTo(tagId);
          }
        cra.andMsgUserIdEqualTo(0);
        cra.andMsgSendStatusEqualTo(1);//发送成功
        cra1.andMsgSendStatusEqualTo(1);//发送成功
        if(userId != null){
//        	cra1.andMsgUserIdIsNotNull();
        	cra1.andMsgUserIdEqualTo(userId);
        }
        if(platform != null){
        	cra.andMsgTerminalLike("%"+platform+"%");
        	cra1.andMsgTerminalLike("%"+platform+"%");
        }
        example.or(cra1);
        example.setOrderByClause("create_time desc");
		if(limitStart != -1){
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return this.messagePushMsgHistoryMapper.selectByExample(example);
	}

	/**
	 * 获取最新一条消息记录
	 * @param userId
	 * @param tagId
	 * @return
	 * @author Michael
	 */
		
	@Override
	public MessagePushMsgHistory getNewestNotice(Integer userId, Integer tagId,String platform) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria cra = example.createCriteria();
        MessagePushMsgHistoryExample.Criteria cra1 = example.createCriteria();
        if(tagId != null){
        	cra.andTagIdEqualTo(tagId);
        	cra1.andTagIdEqualTo(tagId);
          }
        cra.andMsgUserIdEqualTo(0);
        cra.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_1);//发送成功
        cra1.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_1);//发送成功
        if(userId != null){
//        	cra1.andMsgUserIdIsNotNull();
        	cra1.andMsgUserIdEqualTo(userId);
        }
        if(platform != null){
        	cra.andMsgTerminalLike("%"+platform+"%");
        	cra1.andMsgTerminalLike("%"+platform+"%");
        }
        example.or(cra1);
        example.setOrderByClause("send_time desc");
        List<MessagePushMsgHistory> message = this.messagePushMsgHistoryMapper.selectByExample(example);
        if(message != null && message.size() > 0){
        	return message.get(0);
        }
        return null;
        
	}

	/**
	 * 更新用户读取时间
	 * @param userId
	 * @param tagId
	 */
	@Override
	public void updateMsgReadTime(Integer userId, Integer tagId,int type) {
		MessagePushUserReadExample example = new MessagePushUserReadExample();
		MessagePushUserReadExample.Criteria cra = example.createCriteria();
		if(userId != null){
			cra.andUserIdEqualTo(userId);
		}
		if(tagId != null){
			cra.andTagIdEqualTo(tagId);
		}
		cra.andTypeEqualTo(type);
		List<MessagePushUserRead> list = this.messagePushUserReadMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			MessagePushUserRead record = list.get(0);
			record.setReadTime(GetDate.getNowTime10());
			this.messagePushUserReadMapper.updateByPrimaryKeySelective(record);
		}else{
			MessagePushUserRead record = new MessagePushUserRead();
			record.setTagId(tagId);
			record.setUserId(userId);
			record.setType(type);
			record.setReadTime(GetDate.getNowTime10());
			this.messagePushUserReadMapper.insertSelective(record);
		}
	}

	/**
	 * 消息全部已读
	 * @param userId
	 */
	@Override
	public void updateAllMsgPushMsgHistory(Integer userId, String platform) {
//		msgHistory.setMsgFirstreadPlat(Integer.valueOf(platform));
//		if(platform.equals(CustomConstants.CLIENT_ANDROID)){
//			msgHistory.setMsgReadCountAndroid(msgHistory.getMsgReadCountAndroid() + 1);
//
//		}else if(platform.equals(CustomConstants.CLIENT_IOS)){
//			msgHistory.setMsgReadCountIos(msgHistory.getMsgReadCountIos() + 1);
//		}
		logger.info("全部已读什么都不做，等二期处理....");

	}
}
