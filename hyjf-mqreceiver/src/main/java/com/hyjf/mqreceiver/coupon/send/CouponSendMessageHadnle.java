package com.hyjf.mqreceiver.coupon.send;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponDefine;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.rabbitmq.client.Channel;

/**
 * 发放优惠券服务处理类
 * @author hesy
 *
 */
public class CouponSendMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(CouponSendMessageHadnle.class);

    @Autowired
    UserCouponService userCouponService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------发放优惠券开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【优惠券发放】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【优惠券发放】接收到的消息：" + msgBody);
        
        UserCouponBean userCouponBean;
        try {
            userCouponBean = JSONObject.parseObject(msgBody, UserCouponBean.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(userCouponBean.getUserId()) && Validator.isNull(userCouponBean.getSendFlg())) {
            _log.error("【优惠券发放】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String redisKey = "couponsend:" + userCouponBean.getUserId() + (userCouponBean.getCouponCode()==null?"":userCouponBean.getCouponCode()) + (userCouponBean.getSendFlg()==null?"":userCouponBean.getSendFlg()) + (userCouponBean.getActivityId()==null?"":userCouponBean.getActivityId());
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【优惠券发放】正在发放优惠券....");
            return;
        }
        
        try {
			if(checkSign(userCouponBean)){
				userCouponService.insertUserCoupon(userCouponBean);
			}else{
				_log.error("用户："+userCouponBean.getUserId()+",验签失败！");
			}
		} catch (Exception e) {
			_log.error("用户："+userCouponBean.getUserId()+",发放优惠券---失败", e);
		}
        
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
         RedisUtils.del(redisKey);
         
        _log.info("----------------------------发放优惠券结束 (userid: " + userCouponBean.getUserId() + ")--------------------------------" + this.toString());
    }
    
    /**
     * 验证签名
     * 
     * @param paramBean
     * @return
     */
    private boolean checkSign(UserCouponBean paramBean) {

        String userId = paramBean.getUserId();
        Integer sendFlg = paramBean.getSendFlg();
        String accessKey = PropUtils.getSystem(UserCouponDefine.RELEASE_COUPON_ACCESSKEY);
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + sendFlg + accessKey));
        return StringUtils.equals(sign, paramBean.getSign()) ? true : false;

    }
}
