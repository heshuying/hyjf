package com.hyjf.mqreceiver.borrow.issue;

import com.hyjf.common.util.PropUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.issue.AutoIssueService;
import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.rabbitmq.client.Channel;
import com.hyjf.mybatis.messageprocesser.MailMessage;

/**
 * 关联计划消息监听器
 * @author dxj
 *
 */
@Component(value="autoIssueMessageHandle")
public class AutoIssueMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(AutoIssueMessageHandle.class);

    @Autowired
    AutoIssueService autoIssueService;
    
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
    
	/**
	 * 邮件发送key
	 */
	public static String LABEL_MAIL_KEY = "labelmailkey";
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
    	// --> 消息检查
        if(message == null || message.getBody() == null){
            _log.error("【关联计划任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
       // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【关联计划请求】接收到的消息：" + msgBody);
        
        MQBorrow mQBorrow;
        try {
        	mQBorrow = JSONObject.parseObject(msgBody, MQBorrow.class);
            if(mQBorrow == null || (mQBorrow.getBorrowNid() == null && mQBorrow.getCreditNid() == null)){
            	 _log.info("解析为空：" + msgBody);
            	channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        // --> 消息处理
        
        try {
			// 资产关联计划
			
			// 原始标的情况
			if(StringUtils.isNotBlank(mQBorrow.getBorrowNid())){
				_log.info(mQBorrow.getBorrowNid()+" 原始标开始关联计划 ");
		        
				// redis 放重复检查
		        String redisKey = "borrowissue:" + mQBorrow.getBorrowNid();
		        boolean result = RedisUtils.tranactionSet(redisKey, 300);
		        if(!result){
		            _log.info(mQBorrow.getBorrowNid()+" 正在关联计划(redis)");
		            return;
		        }
				
				
		        BorrowWithBLOBs borrow = this.autoIssueService.getBorrowByNid(mQBorrow.getBorrowNid());
				if(borrow == null){
					_log.info(mQBorrow.getBorrowNid()+" 该标的在表里不存在");
					return;
				}
		        
				// 业务校验
				// 发标的状态
				if(borrow.getStatus() !=2 || borrow.getVerifyStatus() !=4){
					_log.info(mQBorrow.getBorrowNid()+" 该标的不是已经发标的状态 ");
					return;
				}
				if(StringUtils.isNotBlank(borrow.getPlanNid())){
					_log.info(mQBorrow.getBorrowNid()+" 该标的已经绑定计划 "+borrow.getLabelId());
					return;
				}
				
				// 第三方资产
				HjhPlanAsset asset = this.autoIssueService.selectPlanAssetByBorrowNid(borrow.getBorrowNid(),borrow.getInstCode());
				if(asset != null){
					if(StringUtils.isNotBlank(asset.getPlanNid()) || asset.getLabelId() == null || asset.getLabelId().intValue() == 0){
						_log.info(asset.getBorrowNid()+" 该标的对应资产已经绑定计划或无标签绑定 "+borrow.getLabelId());
						return;
					}
					if(asset.getStatus().intValue()!= 7){
						_log.info(asset.getBorrowNid()+" 该标的对应资产不是出借中状态 "+borrow.getLabelId());
						return;
					}
				}
				
				// 如果散标过来的标的，没有标签先打上
				if (asset == null && borrow.getLabelId() != null && borrow.getLabelId().intValue() == 0) {
					// 获取标签id
					HjhLabel label = autoIssueService.getLabelId(borrow,null);
					if(label == null || label.getId() == null){
						_log.info(mQBorrow.getBorrowNid()+" 该散标没有匹配到标签 ");

						/**汇计划三期邮件预警 BY LIBIN start*/
/*						// 如果redis不存在这个KEY(一天有效期)，那么可以发邮件
						if(!RedisUtils.exists(LABEL_MAIL_KEY + borrow.getBorrowNid())){
							StringBuffer msg = new StringBuffer();
							msg.append("借款标号：").append(borrow.getBorrowNid()).append("<br/>");
							msg.append("当前时间：").append(GetDate.formatTime()).append("<br/>");
							msg.append("错误信息：").append("该散标没有匹配到标签！").append("<br/>");
							// 邮箱集合
							String[] toMail = new String[] {};
							toMail = new String[] { 
									"liyuanshen@shizitegong.com", 
									"duyang@shizitegong.com",
									"zhulili@shizitegong.com",
									"yuanyuling@hyjf.com",
									"zhouduo@hyjf.com",
									"tanglei@hyjf.com" };
							MailMessage message1= new MailMessage(null, null, "借款编号为：" + borrow.getBorrowNid(), msg.toString(), null, toMail, null,
									MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
							int num = mailMessageProcesser.gather(message1);
							if( num > 0){
								// String key, String value, int expireSeconds 
								RedisUtils.set(LABEL_MAIL_KEY + borrow.getBorrowNid(), borrow.getBorrowNid(), 24 * 60 * 60);
							}
						} else {
							_log.info("此邮件key值还未过期(一天)");
						}*/
						/**汇计划三期邮件预警 BY LIBIN end*/
						return;
					}
					// 临时存着
					borrow.setLabelId(label.getId());
				}
				// 分配计划引擎
				String planNid = this.autoIssueService.getPlanNid(borrow.getLabelId());
				if(planNid == null || borrow.getLabelId() == null || borrow.getLabelId().intValue()==0){
					_log.info(mQBorrow.getBorrowNid()+" 该标的标签无计划关联 "+borrow.getLabelId());
					return;
				}
				
				// 关联计划
				boolean flag = this.autoIssueService.updateIssueBorrow(borrow,planNid,asset);
				if (!flag) {
					_log.error("关联计划失败！" + "[标的编号：" + mQBorrow.getBorrowNid() + "]");
				}
				
			// 债转标的情况	
			}else if (StringUtils.isNotBlank(mQBorrow.getCreditNid())) {
				_log.info(mQBorrow.getCreditNid()+" 债转开始关联计划 ");
		        
				// redis 放重复检查
		        String redisKey = "borrowissue:" + mQBorrow.getCreditNid();
		        boolean result = RedisUtils.tranactionSet(redisKey, 300);
		        if(!result){
		            _log.info(mQBorrow.getCreditNid()+" 正在关联计划(redis) ");
		            return;
		        }
				
				// 清算后债转数据从库不同步问题修复、查询数据从主库获取
		        HjhDebtCredit credit = this.autoIssueService.mainGetCreditByNid(mQBorrow.getCreditNid());
				if(credit == null){
					_log.info(mQBorrow.getCreditNid()+" 该债转在表里不存在");
					return;
				}
				if(credit.getCreditStatus() != null && credit.getCreditStatus().intValue() != 0){
					_log.info(mQBorrow.getCreditNid()+" 该债转状态不为0 初始状态");
					return;
				}
		        
				// 业务校验
				if(StringUtils.isNotBlank(credit.getPlanNidNew())){
					_log.info(mQBorrow.getCreditNid()+" 该债转已经绑定计划或无标签绑定 "+credit.getLabelId());
					return;
				}
				
				// 获取标签id
				HjhLabel label = autoIssueService.getLabelId(credit);
				if(label == null || label.getId() == null){
					_log.info(mQBorrow.getCreditNid()+" 该债转没有匹配标签 ");
					
					/**汇计划三期邮件预警 BY LIBIN start*/
					// 如果redis不存在这个KEY(一天有效期)，那么可以发邮件
					if(!RedisUtils.exists(LABEL_MAIL_KEY + mQBorrow.getCreditNid())){
						StringBuffer msg = new StringBuffer();
						msg.append("债转编号：").append(mQBorrow.getCreditNid()).append("<br/>");
						msg.append("当前时间：").append(GetDate.formatTime()).append("<br/>");
						msg.append("错误信息：").append("该债转没有匹配标签！").append("<br/>");
						// 邮箱集合

						Boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
						_log.info("债转没有匹配标签 evn_test is test ? " + env_test);
						String emailList= "";
						if (env_test){
							emailList = PropUtils.getSystem("hyjf.alerm.email.test");
						}else{
							emailList = PropUtils.getSystem("hyjf.alerm.email");
						}
						String [] toMail = emailList.split(",");
						MailMessage message1= new MailMessage(null, null, "债转编号为：" + mQBorrow.getCreditNid(), msg.toString(), null, toMail, null,
								MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
						int num = mailMessageProcesser.gather(message1);
						if( num > 0){
							// String key, String value, int expireSeconds 
							RedisUtils.set(LABEL_MAIL_KEY + mQBorrow.getCreditNid(), mQBorrow.getCreditNid(), 24 * 60 * 60);
						}
					} else {
						_log.info("此邮件key值还未过期(一天)");
					}
					/**汇计划三期邮件预警 BY LIBIN end*/
					
					return;
				}
				credit.setLabelId(label.getId());
				credit.setLabelName(label.getLabelName());
				
				// 分配计划引擎
				String planNid = this.autoIssueService.getPlanNid(credit.getLabelId());
				if(planNid == null){
					_log.info(mQBorrow.getCreditNid()+" 该债转标签无计划关联 "+credit.getLabelId());
					return;
				}
				
				// 关联计划
				boolean flag = this.autoIssueService.updateIssueCredit(credit,planNid);
				if (!flag) {
					_log.error("关联计划失败！" + "[债转标的编号：" + mQBorrow.getCreditNid() + "]");
				}
				
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
        
    }
    
   
}
