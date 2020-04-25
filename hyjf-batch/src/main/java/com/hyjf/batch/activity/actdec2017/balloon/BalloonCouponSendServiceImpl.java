package com.hyjf.batch.activity.actdec2017.balloon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloonExample;

@Service
public class BalloonCouponSendServiceImpl extends BaseServiceImpl implements BalloonCouponSendService{
    
    Logger _log = LoggerFactory.getLogger(BalloonCouponSendServiceImpl.class);
    
    public static final String SOA_COUPON_KEY = PropUtils.getSystem("release.coupon.accesskey");
    
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	
    /**
     * 
     * 校验并发优惠券
     * @author hsy
     */
    @Override
    public void updateSendCoupon(){
    	String activityId = PropUtils.getSystem("hyjf.act.dec.2017.balloon.id");
        
        List<ActdecTenderBalloon> recordList = this.selectRecordList();

        for(ActdecTenderBalloon record : recordList){
        	
            if(record.getUserId() == null){
                _log.error("10月份出借奖励发券：用户id不存在");
                continue;
            }
            
            String couponCode = getCouponCode(record.getRewardName());
            if(StringUtils.isBlank(couponCode)){
            	continue;
            }
            
            this.sendCoupon(record.getUserId(), couponCode, Integer.parseInt(activityId));

            ActdecTenderBalloon newRecord = new ActdecTenderBalloon();
            newRecord.setSendStatus(1);
            newRecord.setUpdateTime(GetDate.getNowTime10());
            ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
            example.createCriteria().andUserIdEqualTo(record.getUserId());
            actdecTenderBalloonMapper.updateByExampleSelective(newRecord, example);
        }
    }
    
    /**
     * 根据奖品名称获取待发优惠券编号
     */
    private String getCouponCode(String key){
    	if(StringUtils.isEmpty(key)){
    		return "";
    	}
    	
    	if(key.equals("60元代金券一张")){
    		return "PD4536918";
    	}else if(key.equals("100元代金券一张")){
    		return "PD4176209";
    	}else if(key.equals("200元代金券一张")){
    		return "PD6954213";
    	}else if(key.equals("500元红包")){
    		return "PD6954213,PD1098476";
    	}else if(key.equals("1200元红包")){
    		return "PD1098476,PD1098476,PD6437895";
    	}else if(key.equals("3000元红包")){
    		return "PD6437895,PD6437895,PD6205934,PD2571068";
    	}else if(key.equals("6000元红包")){
    		return "PD6437895,PD6205934,PD6205934,PD6205934,PD2571068,PD7016459";
    	}
    	
    	return "";
    }
    
    
    /**
     * 
     * 发放优惠券
     * 
     * @author hsy
     * @param userId
     * @return
     */
    private void sendCoupon(Integer userId, String couponCode, Integer actId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", String.valueOf(userId));
		params.put("sendFlg", "0");
		params.put("couponSource", "2");
		params.put("couponCode", couponCode);
		params.put("sendCount", "1");
		params.put("activityId", String.valueOf(actId));
		params.put("remark", "双十二气球活动奖励");
		
		String sign = StringUtils.lowerCase(MD5.toMD5Code(SOA_COUPON_KEY + String.valueOf(userId) + 0 + SOA_COUPON_KEY));
		params.put("sign", sign);
		
		_log.info("【双十二活动】发券到mq消息:" + JSONObject.toJSONString(params));
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_COUPON_SEND, JSONObject.toJSONString(params));
    }
    

    /**
     * 获取未发放奖励的记录
     * @return
     */
	public List<ActdecTenderBalloon> selectRecordList() {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("sendStatus", '0');
		return actCustomizeMapper.selectBalloonList(paraMap);

	}

}
