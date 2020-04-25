package com.hyjf.batch.msgpush;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.jpush.JPush;
import com.hyjf.common.jpush.JPushPro;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushMsgExample;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistoryExample;
import com.hyjf.mybatis.model.auto.MessagePushPlatStatics;
import com.hyjf.mybatis.model.auto.MessagePushPlatStaticsExample;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;
import com.hyjf.mybatis.model.auto.MessagePushTemplate;
import com.hyjf.mybatis.model.auto.MessagePushTemplateExample;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStaticsExample;

import cn.jpush.api.report.ReceivedsResult;

/**
 * 
 * <p>
 * Title:MsgpushServiceImpl
 * </p>
 * <p>
 * Description: 消息推送service实现类
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 青狐小宝
 * @date 2016年8月9日 下午5:43:39
 */
@Service
public class MsgpushServiceImpl extends BaseServiceImpl implements MsgpushService {

	/**
	 * 一、获取所有需要推送的消息定义
	 * 
	 * @return
	 */
	@Override
	public List<MessagePushMsg> getAllMessage() {
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria cra = example.createCriteria();
		MessagePushMsgExample.Criteria cra1 = example.createCriteria();
		cra.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_MSG_STATUS_0).andMsgSendTypeEqualTo(CustomConstants.MSG_PUSH_SEND_TYPE_0);
		cra1.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_MSG_STATUS_0).andMsgSendTypeEqualTo(CustomConstants.MSG_PUSH_SEND_TYPE_1).andPreSendTimeLessThanOrEqualTo(GetDate.getNowTime10());
		example.or(cra1);
		return messagePushMsgMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 根据时间取发送记录
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author Michael
	 */

	@Override
	public List<MessagePushMsg> getMsgStaticsListByTime(Integer startTime, Integer endTime) {
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria cra = example.createCriteria();
		cra.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_MSG_STATUS_1);// 已发送
		if (startTime != null) {
			cra.andSendTimeGreaterThanOrEqualTo(startTime);
		}
		if (endTime != null) {
			cra.andSendTimeLessThanOrEqualTo(endTime);
		}
		return this.messagePushMsgMapper.selectByExample(example);
	}

	/**
	 * 根据消息编码取发送历史记录
	 * 
	 * @param msgCode
	 * @return
	 * @author Michael
	 */

	@Override
	public List<MessagePushMsgHistory> getMsgHistoryListByMsgCode(String msgCode, Integer startTime, Integer endTime) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
		MessagePushMsgHistoryExample.Criteria cra = example.createCriteria();
		cra.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_1);// 发送成功
		if (StringUtils.isNotEmpty(msgCode)) {
			cra.andMsgCodeEqualTo(msgCode);
		}
		if (startTime != null) {
			cra.andSendTimeGreaterThanOrEqualTo(startTime);
		}
		if (endTime != null) {
			cra.andSendTimeLessThanOrEqualTo(endTime);
		}
		return this.messagePushMsgHistoryMapper.selectByExample(example);
	}

	@Override
	public List<MessagePushTag> getPushTags() {
		MessagePushTagExample example = new MessagePushTagExample();
		MessagePushTagExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);// 启用状态
		example.setOrderByClause("sort asc");
		return messagePushTagMapper.selectByExample(example);
	}

	@Override
	public List<MessagePushMsg> getMessagePushMsgs(MessagePushTag tag, Date beginTime, Date endTime) {
		MessagePushMsgExample example = new MessagePushMsgExample();
		// 推送数量取的是已发送的msg的推送数总和
		example.createCriteria().andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_MSG_STATUS_1).andTagIdEqualTo(tag.getId()).andSendTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(beginTime))).andSendTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(endTime)));
		List<MessagePushMsg> resultList = messagePushMsgMapper.selectByExample(example);
		return resultList;
	}

	@Override
	public List<MessagePushMsgHistory> getMessagePushMsgHistorys(MessagePushTag tag, Date beginTime, Date endTime) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
		// .andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_1)送达数通过极光推送id从极光处查
		example.createCriteria().andTagIdEqualTo(tag.getId()).andSendTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(beginTime))).andSendTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(endTime)));
		List<MessagePushMsgHistory> resultList = messagePushMsgHistoryMapper.selectByExample(example);
		return resultList;
	}

	@Override
	public void insertPushPlatStatics(MessagePushTag tag, List<MessagePushMsgHistory> msgHistorys, Date time) {
		MessagePushPlatStatics platStatics = new MessagePushPlatStatics();
		platStatics.setAndroidDestinationCount(0);
		platStatics.setAndroidReadCount(0);
		platStatics.setAndroidSendCount(0);
		platStatics.setIosDestinationCount(0);
		platStatics.setIosReadCount(0);
		platStatics.setIosSendCount(0);
		platStatics.setReadCount(0);
		platStatics.setSendCount(0);
		platStatics.setDestinationCount(0);
		platStatics.setStaDate(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(time)));
		platStatics.setTagId(tag.getId());

		if (msgHistorys != null && msgHistorys.size() != 0) {
			for (int i = 0; i < msgHistorys.size(); i++) {
				// =================送达数和阅读数从history表中取=================
				// =================以下是目标数=================
				platStatics.setAndroidDestinationCount(platStatics.getAndroidDestinationCount() + msgHistorys.get(i).getMsgDestinationCountAndroid());
				platStatics.setIosDestinationCount(platStatics.getIosDestinationCount() + msgHistorys.get(i).getMsgDestinationCountIos());
				platStatics.setDestinationCount(platStatics.getDestinationCount() + msgHistorys.get(i).getMsgDestinationCountAndroid());
				platStatics.setDestinationCount(platStatics.getDestinationCount() + msgHistorys.get(i).getMsgDestinationCountIos());
				// -----------------以下是阅读数-----------------
				// 推送给所有人
				if (msgHistorys.get(i).getMsgDestinationType().intValue() == CustomConstants.MSG_PUSH_DESTINATION_TYPE_0) {
					platStatics.setAndroidReadCount(platStatics.getAndroidReadCount() + msgHistorys.get(i).getMsgReadCountAndroid());
					platStatics.setIosReadCount(platStatics.getIosReadCount() + msgHistorys.get(i).getMsgReadCountIos());
					platStatics.setReadCount(platStatics.getReadCount() + msgHistorys.get(i).getMsgReadCountAndroid());
					platStatics.setReadCount(platStatics.getReadCount() + msgHistorys.get(i).getMsgReadCountIos());
				}
				// 推送给个人
				if (msgHistorys.get(i).getMsgDestinationType().intValue() == CustomConstants.MSG_PUSH_DESTINATION_TYPE_1) {
					if (msgHistorys.get(i).getMsgFirstreadPlat() != null) {
						// 如果首次阅读终端是android且安卓阅读数大于0,则安卓阅读数和总阅读数+1
						if (msgHistorys.get(i).getMsgFirstreadPlat().intValue() == Integer.parseInt(CustomConstants.CLIENT_ANDROID) && msgHistorys.get(i).getMsgReadCountAndroid() != null && msgHistorys.get(i).getMsgReadCountAndroid().intValue() > 0) {
							platStatics.setAndroidReadCount(platStatics.getAndroidReadCount() + 1);
							platStatics.setReadCount(platStatics.getReadCount() + 1);
						}
						// 如果首次阅读终端是ios且ios阅读数大于0,则ios阅读数和总阅读数+1
						if (msgHistorys.get(i).getMsgFirstreadPlat().intValue() == Integer.parseInt(CustomConstants.CLIENT_IOS) && msgHistorys.get(i).getMsgReadCountIos() != null && msgHistorys.get(i).getMsgReadCountIos().intValue() > 0) {
							platStatics.setIosReadCount(platStatics.getIosReadCount() + 1);
							platStatics.setReadCount(platStatics.getReadCount() + 1);
						}
					}
				}
				// -----------------以下是送达数-----------------
				if (StringUtils.isNotEmpty(msgHistorys.get(i).getMsgJpushId())) {
					ReceivedsResult result = JPush.getMessageReport(msgHistorys.get(i).getMsgJpushId());
					if (result != null && result.received_list != null && result.received_list.size() != 0) {
						if (result.received_list.get(0).android_received != 0) {
							// 如果安卓送达数不为0,则安卓送达数和总送达数都加上安卓送达数
							platStatics.setAndroidSendCount(platStatics.getAndroidSendCount() + result.received_list.get(0).android_received);
							platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).android_received);
						}
						if (result.received_list.get(0).ios_msg_received != 0) {
							// 如果ios送达数不为0,则ios送达数和总送达数都加上ios送达数
							platStatics.setIosSendCount(platStatics.getIosSendCount() + result.received_list.get(0).ios_msg_received);
							platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).ios_msg_received);
						}
					}
				}
				if (StringUtils.isNotEmpty(msgHistorys.get(i).getMsgJpushProId())) {
					ReceivedsResult result = JPushPro.getMessageReport(msgHistorys.get(i).getMsgJpushProId());
					if (result != null && result.received_list != null && result.received_list.size() != 0) {
						if (result.received_list.get(0).android_received != 0) {
							// 如果安卓送达数不为0,则安卓送达数和总送达数都加上安卓送达数
							platStatics.setAndroidSendCount(platStatics.getAndroidSendCount() + result.received_list.get(0).android_received);
							platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).android_received);
						}
						if (result.received_list.get(0).ios_msg_received != 0) {
							// 如果ios送达数不为0,则ios送达数和总送达数都加上ios送达数
							platStatics.setIosSendCount(platStatics.getIosSendCount() + result.received_list.get(0).ios_msg_received);
							platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).ios_msg_received);
						}
					}
				}
				if (StringUtils.isNotEmpty(msgHistorys.get(i).getMsgJpushYxbId())) {
                    ReceivedsResult result = JPushPro.getMessageReport(msgHistorys.get(i).getMsgJpushYxbId());
                    if (result != null && result.received_list != null && result.received_list.size() != 0) {
                        if (result.received_list.get(0).android_received != 0) {
                            // 如果安卓送达数不为0,则安卓送达数和总送达数都加上安卓送达数
                            platStatics.setAndroidSendCount(platStatics.getAndroidSendCount() + result.received_list.get(0).android_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).android_received);
                        }
                        if (result.received_list.get(0).ios_msg_received != 0) {
                            // 如果ios送达数不为0,则ios送达数和总送达数都加上ios送达数
                            platStatics.setIosSendCount(platStatics.getIosSendCount() + result.received_list.get(0).ios_msg_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).ios_msg_received);
                        }
                    }
                }
				if (StringUtils.isNotEmpty(msgHistorys.get(i).getMsgJpushZnbId())) {
                    ReceivedsResult result = JPushPro.getMessageReport(msgHistorys.get(i).getMsgJpushZnbId());
                    if (result != null && result.received_list != null && result.received_list.size() != 0) {
                        if (result.received_list.get(0).android_received != 0) {
                            // 如果安卓送达数不为0,则安卓送达数和总送达数都加上安卓送达数
                            platStatics.setAndroidSendCount(platStatics.getAndroidSendCount() + result.received_list.get(0).android_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).android_received);
                        }
                        if (result.received_list.get(0).ios_msg_received != 0) {
                            // 如果ios送达数不为0,则ios送达数和总送达数都加上ios送达数
                            platStatics.setIosSendCount(platStatics.getIosSendCount() + result.received_list.get(0).ios_msg_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).ios_msg_received);
                        }
                    }
                }
				if (StringUtils.isNotEmpty(msgHistorys.get(i).getMsgJpushZybId())) {
                    ReceivedsResult result = JPushPro.getMessageReport(msgHistorys.get(i).getMsgJpushZybId());
                    if (result != null && result.received_list != null && result.received_list.size() != 0) {
                        if (result.received_list.get(0).android_received != 0) {
                            // 如果安卓送达数不为0,则安卓送达数和总送达数都加上安卓送达数
                            platStatics.setAndroidSendCount(platStatics.getAndroidSendCount() + result.received_list.get(0).android_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).android_received);
                        }
                        if (result.received_list.get(0).ios_msg_received != 0) {
                            // 如果ios送达数不为0,则ios送达数和总送达数都加上ios送达数
                            platStatics.setIosSendCount(platStatics.getIosSendCount() + result.received_list.get(0).ios_msg_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).ios_msg_received);
                        }
                    }
                }
				if (StringUtils.isNotEmpty(msgHistorys.get(i).getMsgJpushZzbId())) {
                    ReceivedsResult result = JPushPro.getMessageReport(msgHistorys.get(i).getMsgJpushZzbId());
                    if (result != null && result.received_list != null && result.received_list.size() != 0) {
                        if (result.received_list.get(0).android_received != 0) {
                            // 如果安卓送达数不为0,则安卓送达数和总送达数都加上安卓送达数
                            platStatics.setAndroidSendCount(platStatics.getAndroidSendCount() + result.received_list.get(0).android_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).android_received);
                        }
                        if (result.received_list.get(0).ios_msg_received != 0) {
                            // 如果ios送达数不为0,则ios送达数和总送达数都加上ios送达数
                            platStatics.setIosSendCount(platStatics.getIosSendCount() + result.received_list.get(0).ios_msg_received);
                            platStatics.setSendCount(platStatics.getSendCount() + result.received_list.get(0).ios_msg_received);
                        }
                    }
                }
			}
		}
		// 更新数据库
		MessagePushPlatStaticsExample example = new MessagePushPlatStaticsExample();
		example.createCriteria().andTagIdEqualTo(tag.getId()).andStaDateEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(time)));
		List<MessagePushPlatStatics> resultList = messagePushPlatStaticsMapper.selectByExample(example);
		// 如果没有,则插入,否则更新
		if (resultList != null && resultList.size() != 0) {
			platStatics.setId(resultList.get(0).getId());
			messagePushPlatStaticsMapper.updateByPrimaryKey(platStatics);
		} else {
			messagePushPlatStaticsMapper.insertSelective(platStatics);
		}
	}

	/**
	 * 插入模板统计数据
	 * 
	 * @param msg
	 * @author Michael
	 */

	@Override
	public void insertTemplateStatics(MessagePushMsg msg) {
		MessagePushTemplateStatics msgSta = new MessagePushTemplateStatics();
		msgSta.setMsgId(msg.getId());
		msgSta.setMsgCode(msg.getMsgCode());
		msgSta.setMsgTitle(msg.getMsgTitle());
		msgSta.setTagId(msg.getTagId());
		msgSta.setSendTime(msg.getSendTime());
		msgSta.setAndroidDestinationCount(0);
		msgSta.setIosDestinationCount(0);
		msgSta.setAndroidReadCount(0);
		msgSta.setAndroidSendCount(0);
		msgSta.setIosReadCount(0);
		msgSta.setIosSendCount(0);
		msgSta.setCreateTime(GetDate.getNowTime10());
		List<MessagePushTemplateStatics> list = this.getMessagePushTemplateStaticsByMsgCode(msgSta.getMsgCode());
		if (list == null || list.size() == 0) {
			this.messagePushTemplateStaticsMapper.insertSelective(msgSta);
		}
		if (list.size() > 0) {
			msgSta.setId(list.get(0).getId());
			this.messagePushTemplateStaticsMapper.updateByPrimaryKeySelective(msgSta);
		}
	}

	/**
	 * 根据消息编码获取统计数据
	 * 
	 * @return
	 */
	public List<MessagePushTemplateStatics> getMessagePushTemplateStaticsByMsgCode(String msgCode) {
		MessagePushTemplateStaticsExample example = new MessagePushTemplateStaticsExample();
		MessagePushTemplateStaticsExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(msgCode)) {
			cra.andMsgCodeEqualTo(msgCode);
		}
		return this.messagePushTemplateStaticsMapper.selectByExample(example);
	}

	/**
	 * 更新模板统计数据
	 * 
	 * @param msgTemplateStatics
	 * @author Michael
	 */

	@Override
	public void updateTemplateStatics(MessagePushTemplateStatics msgTemplateStatics, Integer startTime, Integer endTime) {
		// ios目标数
		int iosDestinationCount = 0;
		// 安卓目标数
		int androidDestinationCount = 0;
		// ios阅读数
		int iosReadCount = 0;
		// ios送达数
		int iosSendCount = 0;
		// 安卓阅读数
		int androidReadCount = 0;
		// 安卓送达数
		int androidSendCount = 0;

		// 查询发送记录（成功）
		List<MessagePushMsgHistory> historyList = getMsgHistoryListByMsgCode(msgTemplateStatics.getMsgCode(), startTime, endTime);
		if (historyList == null || historyList.size() == 0) {
			return;
		}
		/**
		 * 如果推送到个人，消息可能为一条或多条 如果推送全部人，消息应该为一条记录
		 */
		for (int i = 0; i < historyList.size(); i++) {
			MessagePushMsgHistory hisInfo = historyList.get(i);
			// 推送给个人
			if (hisInfo.getMsgDestinationType() == CustomConstants.MSG_PUSH_DESTINATION_TYPE_1) {
				if (hisInfo.getMsgFirstreadPlat() != null) {
					if (hisInfo.getMsgFirstreadPlat().intValue() == Integer.parseInt(CustomConstants.CLIENT_ANDROID)) {
						androidReadCount = androidReadCount + 1;
					}
					if (hisInfo.getMsgFirstreadPlat().intValue() == Integer.parseInt(CustomConstants.CLIENT_IOS)) {
						iosReadCount = iosReadCount + 1;
					}
				}
			} else {
				iosReadCount = hisInfo.getMsgReadCountIos();
				androidReadCount = hisInfo.getMsgReadCountAndroid();
			}
			// 目标数
			iosDestinationCount = iosDestinationCount + hisInfo.getMsgDestinationCountIos();
			androidDestinationCount = androidDestinationCount + hisInfo.getMsgDestinationCountAndroid();
			// 推送数
			// -----------------调用极光接口-----------------
			if (StringUtils.isNotEmpty(hisInfo.getMsgJpushId())) {
				ReceivedsResult result = JPush.getMessageReport(hisInfo.getMsgJpushId());
				if (result != null && result.received_list != null && result.received_list.size() != 0) {
					androidSendCount = androidSendCount + result.received_list.get(0).android_received;
					iosSendCount = iosSendCount + result.received_list.get(0).ios_msg_received;
				}
			}
			if (StringUtils.isNotEmpty(hisInfo.getMsgJpushProId())) {
				ReceivedsResult result = JPushPro.getMessageReport(hisInfo.getMsgJpushProId());
				if (result != null && result.received_list != null && result.received_list.size() != 0) {
					androidSendCount = androidSendCount + result.received_list.get(0).android_received;
					iosSendCount = iosSendCount + result.received_list.get(0).ios_msg_received;
				}
			}
			if (StringUtils.isNotEmpty(hisInfo.getMsgJpushYxbId())) {
                ReceivedsResult result = JPushPro.getMessageReport(hisInfo.getMsgJpushYxbId());
                if (result != null && result.received_list != null && result.received_list.size() != 0) {
                    androidSendCount = androidSendCount + result.received_list.get(0).android_received;
                    iosSendCount = iosSendCount + result.received_list.get(0).ios_msg_received;
                }
            }
			if (StringUtils.isNotEmpty(hisInfo.getMsgJpushZnbId())) {
                ReceivedsResult result = JPushPro.getMessageReport(hisInfo.getMsgJpushZnbId());
                if (result != null && result.received_list != null && result.received_list.size() != 0) {
                    androidSendCount = androidSendCount + result.received_list.get(0).android_received;
                    iosSendCount = iosSendCount + result.received_list.get(0).ios_msg_received;
                }
            }
			if (StringUtils.isNotEmpty(hisInfo.getMsgJpushZybId())) {
                ReceivedsResult result = JPushPro.getMessageReport(hisInfo.getMsgJpushZybId());
                if (result != null && result.received_list != null && result.received_list.size() != 0) {
                    androidSendCount = androidSendCount + result.received_list.get(0).android_received;
                    iosSendCount = iosSendCount + result.received_list.get(0).ios_msg_received;
                }
            }
			if (StringUtils.isNotEmpty(hisInfo.getMsgJpushZzbId())) {
                ReceivedsResult result = JPushPro.getMessageReport(hisInfo.getMsgJpushZzbId());
                if (result != null && result.received_list != null && result.received_list.size() != 0) {
                    androidSendCount = androidSendCount + result.received_list.get(0).android_received;
                    iosSendCount = iosSendCount + result.received_list.get(0).ios_msg_received;
                }
            }
		}
		msgTemplateStatics.setAndroidReadCount(androidReadCount);
		msgTemplateStatics.setAndroidSendCount(androidSendCount);
		msgTemplateStatics.setIosReadCount(iosReadCount);
		msgTemplateStatics.setIosSendCount(iosSendCount);
		msgTemplateStatics.setIosDestinationCount(iosDestinationCount);
		msgTemplateStatics.setAndroidDestinationCount(androidDestinationCount);
		msgTemplateStatics.setSendTime(GetDate.getNowTime10());

		this.messagePushTemplateStaticsMapper.updateByPrimaryKeySelective(msgTemplateStatics);
	}

	/**
	 * 根据时间获取模板统计数据
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author Michael
	 */

	@Override
	public List<MessagePushTemplateStatics> getTemplateStaticsListByTime(Integer startTime, Integer endTime) {
		MessagePushTemplateStaticsExample example = new MessagePushTemplateStaticsExample();
		MessagePushTemplateStaticsExample.Criteria cra = example.createCriteria();
		if (startTime != null) {
			cra.andCreateTimeGreaterThanOrEqualTo(startTime);
		}
		if (endTime != null) {
			cra.andCreateTimeLessThanOrEqualTo(endTime);
		}
		return this.messagePushTemplateStaticsMapper.selectByExample(example);

	}

	/**
	 * 获取所有模板
	 * 
	 * @return
	 * @author Michael
	 */

	@Override
	public List<MessagePushTemplate> getAllTemplates() {
		MessagePushTemplateExample example = new MessagePushTemplateExample();
		MessagePushTemplateExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.MSG_PUSH_STATUS_1);// 启用
		return this.messagePushTemplateMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param template
	 * @author Michael
	 */

	@Override
	public void insertTemplateStatics(MessagePushTemplate template) {
		// 判断是否添加过
		List<MessagePushTemplateStatics> list = this.getMessagePushTemplateStaticsByMsgCode(template.getTemplateCode());
		if (list.size() > 0) {
			return;
		}
		MessagePushTemplateStatics msgSta = new MessagePushTemplateStatics();
		msgSta.setMsgId(template.getId());
		msgSta.setMsgCode(template.getTemplateCode());
		msgSta.setMsgTitle(template.getTemplateTitle());
		msgSta.setTagId(template.getTagId());
		msgSta.setSendTime(template.getCreateTime());
		msgSta.setAndroidDestinationCount(0);
		msgSta.setIosDestinationCount(0);
		msgSta.setAndroidReadCount(0);
		msgSta.setAndroidSendCount(0);
		msgSta.setIosReadCount(0);
		msgSta.setIosSendCount(0);
		msgSta.setCreateTime(GetDate.getNowTime10());
		// 插入数据
		this.messagePushTemplateStaticsMapper.insertSelective(msgSta);
	}

}
