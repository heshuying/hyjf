package com.hyjf.batch.msgpush;

import java.util.List;

import com.hyjf.common.util.PropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.MessagePushMsg;

/**
 * 
 * <p>
 * Title:MsgpushTask
 * </p>
 * <p>
 * Description: 消息推送定时任务
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 青狐小宝
 * @date 2016年8月9日 下午5:42:21
 */
public class MsgpushTask {
	
	private static final Logger logger = LoggerFactory.getLogger(MsgpushTask.class);
	@Autowired
	private MsgpushService msgpushService;
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	public void run() {
		logger.info("---------消息推送定时任务-----------isOver is :{}", isOver);
		
		if (isOver) {
			isOver = false;
			try {
				// 判断是否是测试环境
				boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
				if (env_test){
					return;
				}
				//获取定时发送的任务列表
				List<MessagePushMsg> list = msgpushService.getAllMessage();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						//添加到发送队列
						AppMsMessage appMsMessage = new AppMsMessage(MessageDefine.APPMSSENDFORMSG, list.get(i).getId());
						appMsProcesser.gather(appMsMessage);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(MsgpushTask.class + ":" + e.getMessage());
			}
			isOver = true;
		}
	}

}
