package com.hyjf.mqreceiver.borrow.record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.record.AutoRecordService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.rabbitmq.client.Channel;

/**
 * 自动备案消息监听器
 * @author dxj
 *
 */
@Component(value="autoRecordMessageHandle")
public class AutoRecordMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(AutoRecordMessageHandle.class);

    @Autowired
    AutoRecordService autoRecordService;
   
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
    	
    	//String nowTime = GetDate.date2Str(new Date(),GetDate.yyyyMMdd);
    	
    	// --> 消息检查
        if(message == null || message.getBody() == null){
            _log.error("【自动备案任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
       // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【自动备案请求】接收到的消息：" + msgBody);
        
        HjhPlanAsset mqHjhPlanAsset;
        try {
        	mqHjhPlanAsset = JSONObject.parseObject(msgBody, HjhPlanAsset.class);
        	/*--------------upd by liushouyi HJH3 Start--------------*/
            //if(mqHjhPlanAsset == null || mqHjhPlanAsset.getAssetId() == null){
            if(mqHjhPlanAsset == null || (mqHjhPlanAsset.getAssetId() == null && mqHjhPlanAsset.getBorrowNid() == null)){
            /*--------------upd by liushouyi HJH3 End--------------*/
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
        	
        	/*--------------upd by liushouyi HJH3 Start--------------*/
        	// 手动推送标的
			if (StringUtils.isNotBlank(mqHjhPlanAsset.getBorrowNid())) {
				_log.info(mqHjhPlanAsset.getBorrowNid()+" 开始自动备案 "+ mqHjhPlanAsset.getInstCode());
				// redis 放重复检查
		        String redisKey = "borrowrecord:" + mqHjhPlanAsset.getInstCode()+mqHjhPlanAsset.getBorrowNid();
		        boolean result = RedisUtils.tranactionSet(redisKey, 300);
		        if(!result){
		            _log.info(mqHjhPlanAsset.getInstCode()+" 正在备案(redis) "+mqHjhPlanAsset.getBorrowNid());
		            return;
		        }
		        // 获取当前标的详情
				Borrow borrow = this.autoRecordService.getBorrowByBorrowNid(mqHjhPlanAsset.getBorrowNid());
				
				// 标的状态位判断
				if (null == borrow.getStatus() || borrow.getStatus() != 0 || 
						null == borrow.getRegistStatus() || borrow.getRegistStatus() != 0) {
					_log.info("标的："+borrow.getBorrowNid()+" 不是备案状态");
		            return;
				}
				
				//判断该资产是否可以自动备案，是否关联计划
				HjhAssetBorrowType hjhAssetBorrowType = this.autoRecordService.selectAssetBorrowType(borrow);
				if(hjhAssetBorrowType == null || hjhAssetBorrowType.getAutoRecord() == null || hjhAssetBorrowType.getAutoRecord() != 1){
					_log.info(borrow.getBorrowNid()+" 标的不能自动备案,原因自动备案未配置");
					return;
				}
				
				boolean flag = this.autoRecordService.updateRecordBorrow(borrow);
				if (!flag) {
					_log.error("自动备案失败！" + "[资产/标的借款编号：" + borrow.getBorrowNid() + "]");
				}else{
					// 成功后到初审队列
					if(borrow.getEntrustedFlg() != null && borrow.getEntrustedFlg().intValue() ==1){
						_log.info(borrow.getBorrowNid()+"  未推送，等待授权");
					}else{
			        	// 发送到初审队列
						if (null != hjhAssetBorrowType && null != hjhAssetBorrowType.getAutoAudit() && hjhAssetBorrowType.getAutoAudit() == 1) {
							// 加入到消息队列
							this.autoRecordService.sendToMQ(borrow,  RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
							_log.info(borrow.getBorrowNid()+" 成功发送到初审队列");
						}
					}

					// 备案成功后随机睡0.2到0.5秒
					try {
						Random random = new Random();
						int rand = (random.nextInt(4)+2)*100;
						Thread.sleep(rand);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
				_log.info(borrow.getBorrowNid()+" 结束自动备案");
				
			}
			/*--------------upd by liushouyi HJH3 End--------------*/
			
			// 原有三方资产推送处理不变
			if (StringUtils.isNotBlank(mqHjhPlanAsset.getAssetId())) {
				// 资产自动备案
				_log.info(mqHjhPlanAsset.getAssetId()+" 开始自动备案 "+ mqHjhPlanAsset.getInstCode());
				HjhPlanAsset hjhPlanAsset = this.autoRecordService.selectPlanAsset(mqHjhPlanAsset.getAssetId(), mqHjhPlanAsset.getInstCode());
				if(hjhPlanAsset == null){
					_log.info(mqHjhPlanAsset.getAssetId()+" 该资产在表里不存在！！");
					return;
				}
		        
				// redis 放重复检查
		        String redisKey = "borrowrecord:" + hjhPlanAsset.getInstCode()+hjhPlanAsset.getAssetId();
		        boolean result = RedisUtils.tranactionSet(redisKey, 300);
		        if(!result){
		            _log.info(hjhPlanAsset.getInstCode()+" 正在备案(redis) "+hjhPlanAsset.getAssetId());
		            return;
		        }
		        
				// 业务校验
				if(hjhPlanAsset.getStatus() != null && hjhPlanAsset.getStatus().intValue() != 3 && 
						hjhPlanAsset.getVerifyStatus() != null && hjhPlanAsset.getVerifyStatus().intValue() == 1){
					_log.info(mqHjhPlanAsset.getAssetId()+" 该资产状态不是备案状态");
					return;
				}
				
			    /** * 网站合规改造-自动备案添加还款授权校验 start 根据产品需求：只处理 资产来源为非汇盈金服的标的 校验 */
				/**因业务需求暂时注掉各种授权校验代码 */
				
	/*			_log.info("开始校验借款人的还款授权：" + hjhPlanAsset.getUserId());
				// 替换参数(短信用)
				Map<String, String> replaceMap = new HashMap<String, String>();
				if(StringUtils.isNotEmpty(hjhPlanAsset.getBorrowNid())){
					//通过资产中的标的查询此标的是否有担保机构
					Borrow borrow = autoRecordService.getBorrowByBorrowNid(hjhPlanAsset.getBorrowNid());
					if(borrow != null){
						// (1.1)担保机构id可以为空,不为空时校验，为空不校验授权
						if(borrow.getRepayOrgUserId() != null){
							HjhUserAuth hjhUserAuth = autoRecordService.getHjhUserAuthByUserID(borrow.getRepayOrgUserId());
							if(hjhUserAuth == null){
								_log.info("该资产无担保机构或者未做任何授权" + mqHjhPlanAsset.getAssetId() );
							} else {
								// 还款授权状态 DB 默认为 0
								if (hjhUserAuth.getAutoRepayStatus() == null || hjhUserAuth.getAutoRepayStatus().toString().equals("0")) {
									_log.info("该资产的担保机构未做还款授权" + mqHjhPlanAsset.getAssetId() );
								}
								// 缴费授权状态 DB 默认为 0
								if (hjhUserAuth.getAutoPaymentStatus() == null || hjhUserAuth.getAutoPaymentStatus().toString().equals("0")) {
									_log.info("该资产的担保机构未做缴费授权" + mqHjhPlanAsset.getAssetId() );
								}	
							}
						}
						// (1.2)受托人id可以为空,不为空时校验，为空不校验授权
						if(borrow.getEntrustedUserId() != null){
							HjhUserAuth hjhUserAuth1 = autoRecordService.getHjhUserAuthByUserID(borrow.getEntrustedUserId());
							if(hjhUserAuth1 == null){
								_log.info("该资产无收款人或者该收款人未做任何授权" + mqHjhPlanAsset.getAssetId() );
							} else {
								// 缴费授权状态 DB 默认为 0
								if (hjhUserAuth1.getAutoPaymentStatus() == null || hjhUserAuth1.getAutoPaymentStatus().toString().equals("0")) {
									_log.info("该资产的收款人未做缴费授权" + mqHjhPlanAsset.getAssetId() );
								}	
							}
						}
						// (1.3)借款人id必须非空
						if(borrow.getUserId() != null){
							HjhUserAuth hjhUserAuth2 = autoRecordService.getHjhUserAuthByUserID(borrow.getUserId());
							if(hjhUserAuth2 == null){
								_log.info("该资产无借款人或者借款未做任何授权" + mqHjhPlanAsset.getAssetId() );
							} else {
								// 缴费授权状态 DB 默认为 0
								if (hjhUserAuth2.getAutoPaymentStatus() == null || hjhUserAuth2.getAutoPaymentStatus().toString().equals("0")) {
									_log.info("该资产的借款人未做缴费授权" + mqHjhPlanAsset.getAssetId() );
								}
								// 是否可用担保机构还款(0:否,1:是) DB默认为0
								if(borrow.getIsRepayOrgFlag() != null && borrow.getIsRepayOrgFlag() == 1){
									如果是担保机构还款，还款授权可以不做
								} else {
									// 还款授权状态 DB 默认为 0
									if (hjhUserAuth2.getAutoRepayStatus() == null || hjhUserAuth2.getAutoRepayStatus().toString().equals("0")) {
										_log.info("该资产的借款人无担保机构垫付并且未做还款授权" + mqHjhPlanAsset.getAssetId() );
									}
								}
							}
						} else {
							_log.info("该资产无借款人" + mqHjhPlanAsset.getAssetId() );
						}
					} else {
						_log.info("该资产在borrow表中不存在：" + hjhPlanAsset.getAssetId());
					}
				} else {
					_log.info("此资产无标的编号：" + hjhPlanAsset.getAssetId());
				}
				_log.info("结束校验授权：" + hjhPlanAsset.getAssetId());*/
				
				
				/** * 网站合规改造-自动备案添加还款授权校验 end */
				
				
				//判断该资产是否可以自动备案，是否关联计划
				HjhAssetBorrowType hjhAssetBorrowType = this.autoRecordService.selectAssetBorrowType(hjhPlanAsset);
				if(hjhAssetBorrowType == null || hjhAssetBorrowType.getAutoRecord() == null || hjhAssetBorrowType.getAutoRecord() != 1){
					_log.info(hjhPlanAsset.getAssetId()+" 该资产不能自动备案,原因自动备案未配置");
					return;
				}
				
				boolean flag = this.autoRecordService.updateRecordBorrow(hjhPlanAsset, hjhAssetBorrowType);
				if (!flag) {
					_log.error("自动备案失败！" + "[资产编号：" + hjhPlanAsset.getAssetId() + "]");
				}else{
					// 成功后到初审队列
					if(hjhPlanAsset.getEntrustedFlg() != null && hjhPlanAsset.getEntrustedFlg().intValue() ==1){
						_log.info(hjhPlanAsset.getAssetId()+"  未推送，等待授权");
					}else{
						this.autoRecordService.sendToMQ(hjhPlanAsset,  RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
						_log.info(hjhPlanAsset.getAssetId()+" 成功发送到初审队列");
					}
	
					// 备案成功后随机睡0.2到0.5秒
					try {
						Random random = new Random();
						int rand = (random.nextInt(4)+2)*100;
						Thread.sleep(rand);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				_log.info(hjhPlanAsset.getAssetId()+" 结束自动备案");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
    }
}
