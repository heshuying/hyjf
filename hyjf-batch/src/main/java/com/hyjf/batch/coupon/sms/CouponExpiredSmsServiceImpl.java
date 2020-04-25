package com.hyjf.batch.coupon.sms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.batch.BatchCouponTimeoutCommonCustomize;

/**
 * 
 * 优惠券过期发送push消息
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月22日
 * @see 上午11:43:23
 */
@Service
public class CouponExpiredSmsServiceImpl extends BaseServiceImpl implements CouponExpiredSmsService {

    Logger _log = LoggerFactory.getLogger(CouponExpiredSmsServiceImpl.class);
    
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
    @Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
    
    /**
     * 检查优惠券的到期情况并发送短信提醒
     */
	@Override
	public void sendExpiredMsgAct() {
		//一天后到期的开始时间和截止时间
//		int oneBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf,1));
//		int oneEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 2));
		
        //三天后到期的开始时间和截止时间
        int threeBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 3));
        int threeEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 4));
		
//		Map<String,Object> paramMapOne = new HashMap<String,Object>();
//		paramMapOne.put("startTime", oneBeginDate);
//		paramMapOne.put("endTime", oneEndDate);
//		List<BatchCouponTimeoutCommonCustomize> userCouponOneList = couponTimeoutCustomizeMapper.selectCouponQuota(paramMapOne);
//		// 一日到期短信提醒
//        this.sendSmsCoupon(userCouponOneList,1);
        
        
        Map<String,Object> paramMapThr = new HashMap<String,Object>();
        paramMapThr.put("startTime", threeBeginDate);
        paramMapThr.put("endTime", threeEndDate);
		List<BatchCouponTimeoutCommonCustomize> userCouponThrList = couponTimeoutCustomizeMapper.selectCouponQuota(paramMapThr);
		// 三日到期短信提醒
        this.sendSmsCoupon(userCouponThrList,3);
        
	}
	
	/**
	 * 优惠券过期短信提醒
	 * 
	 * @param userId
	 */
	private void sendSmsCoupon(List<BatchCouponTimeoutCommonCustomize> userCouponList,int flag) {
		if (userCouponList != null && userCouponList.size() > 0) {
			for (BatchCouponTimeoutCommonCustomize userCoupon : userCouponList) {
				if (Validator.isNotNull(userCoupon.getUserId()) && Validator.isNotNull(userCoupon.getCouponQuota()) && userCoupon.getCouponQuota()>0&& Validator.isNotNull(userCoupon.getMobile())) {
					Map<String,String> msg = new HashMap<String,String>();
					msg.put("val_amount", userCoupon.getCouponQuota().toString());
					SmsMessage smsMessage = null;
					if(flag == 1){
						// 一日到期短信提醒
						smsMessage = new SmsMessage(userCoupon.getUserId(), msg, userCoupon.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null,
								CustomConstants.PARAM_TPL_ONE_DEADLINE, CustomConstants.CHANNEL_TYPE_NORMAL);
						smsProcesser.gather(smsMessage);
						_log.info("代金券一日到期短信提醒，用户编号："+userCoupon.getUserId()+"体验金面值总额："+userCoupon.getCouponQuota());
					}else{
						// 三日到期短信提醒
						smsMessage = new SmsMessage(userCoupon.getUserId(), msg, userCoupon.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null,
								CustomConstants.PARAM_TPL_THREE_DEADLINE, CustomConstants.CHANNEL_TYPE_NORMAL);
						smsProcesser.gather(smsMessage);
						_log.info("代金券三日到期短信提醒，用户编号："+userCoupon.getUserId()+"体验金面值总额："+userCoupon.getCouponQuota());
					}
					
				}
			}
		}
	}

	public static void main(String[] args) {
	    int yestodayBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 7));
        int yestodayEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf,8));
	    System.out.println(yestodayBeginDate);
	    System.out.println(yestodayEndDate);
    }
    
}
