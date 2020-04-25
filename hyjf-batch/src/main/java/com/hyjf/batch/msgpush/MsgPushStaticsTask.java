package com.hyjf.batch.msgpush;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTemplate;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;

/**
 * 
 * <p>
 * Title:MsgpushTask
 * </p>
 * <p>
 * Description: 消息推送统计
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 李深强
 * @date 2016年8月9日 下午5:42:21
 */
public class MsgPushStaticsTask {
	private static final Logger logger = LoggerFactory.getLogger(MsgPushStaticsTask.class);

	@Autowired
	private MsgpushService msgpushService;
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;
	//判断模板消息是否已插入
	private static Boolean doFlag = false;

	/**
	 * 模板消息统计
	 * 
	 * 产品要求更新7天内统计数据
	 */
	public void msgTemplateStatics() {
		// 时间戳定义
		Integer startTime = null;
		Integer endTime = null;
		
		//模板数据统计
		if(!doFlag){
			doFlag = true;
			//获取所有模板
			List<MessagePushTemplate> templateList = this.msgpushService.getAllTemplates();
			// 插入统计数据
			for (int i = 0; i < templateList.size(); i++) {
				this.msgpushService.insertTemplateStatics(templateList.get(i));
			}
		}
		
		// 今天
		String curDate = GetDate.date2Str(GetDate.date_sdf);
		startTime =  GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(curDate));
		endTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(curDate));
		// 当天内的发送消息
		List<MessagePushMsg> msgList = this.msgpushService.getMsgStaticsListByTime(startTime, endTime);
		// 插入统计数据
		for (int i = 0; i < msgList.size(); i++) {
			this.msgpushService.insertTemplateStatics(msgList.get(i));
		}

		// 查询7天统计数据
		String yesDate = GetDate.getCountDate(5, -6);
		startTime = GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(yesDate));
		endTime = GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(curDate));
		List<MessagePushTemplateStatics> templateStaticsList = this.msgpushService.getTemplateStaticsListByTime(startTime, endTime);
		// 更新统计数据
		for (int i = 0; i < templateStaticsList.size(); i++) {
			this.msgpushService.updateTemplateStatics(templateStaticsList.get(i),startTime,endTime);
		}

	}

	/**
	 * 平台消息统计报表
	 */
	public void msgPlatStatics() {
		if (isOver) {
			isOver = false;
			try {
				int N = 7;// 更新或插入7天之内的数据
				Date today = GetDate.stringToDate(GetDate.dateToString2(new Date()) + " 00:00:00");
				Date todayStart = GetDate.stringToDate(GetDate.dateToString2(today) + " 00:00:00");
				Date todayEnd = GetDate.stringToDate(GetDate.dateToString2(today) + " 23:59:59");
				today = GetDate.getSomeDayBeforeOrAfter(today, 1);
				todayStart = GetDate.getSomeDayBeforeOrAfter(todayStart, 1);
				todayEnd = GetDate.getSomeDayBeforeOrAfter(todayEnd, 1);
				// 获取标签类型
				List<MessagePushTag> tags = msgpushService.getPushTags();
				for (int i = 0; i < N; i++) {
					today = GetDate.getSomeDayBeforeOrAfter(today, -1);
					todayStart = GetDate.getSomeDayBeforeOrAfter(todayStart, -1);
					todayEnd = GetDate.getSomeDayBeforeOrAfter(todayEnd, -1);
					if (tags != null && tags.size() != 0) {
						for (int j = 0; j < tags.size(); j++) {
							// 根据标签类型,获取时间范围内所有的msghistory
							List<MessagePushMsgHistory> msgHistoryList = msgpushService.getMessagePushMsgHistorys(tags.get(j), todayStart, todayEnd);
							// 根据msg和msghistory插入当天统计信息
							msgpushService.insertPushPlatStatics(tags.get(j), msgHistoryList, today);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			isOver = true;
		}
	}

}
