package com.hyjf.batch.activity.actten2017;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActRewardListExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.admin.act.ActTen2017Customize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class TenderRewardCouponSendServiceImpl extends BaseServiceImpl implements TenderRewardCouponSendService{
    
    Logger _log = LoggerFactory.getLogger(TenderRewardCouponSendServiceImpl.class);
    
    /**
     * 
     * 校验并发优惠券
     * @author hsy
     */
    @Override
    public void updateSendCoupon(){
    	String activityId = PropUtils.getSystem("hyjf.actten2017.id");
        // 取得活动
        ActivityList activity = this.activityListMapper
                .selectByPrimaryKey(Integer.valueOf(activityId));
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
        List<ActTen2017Customize> recordList = this.selectRecordList(paraMap);

        for(ActTen2017Customize record : recordList){
        	
            if(record.getUserId() == null){
                _log.error("10月份出借奖励发券：用户id不存在");
                continue;
            }
            
            if(checkIsSend(record.getUserId())){
            	continue;
            }
            
            String couponCode = getCouponCode(record.getRewardName());
            if(StringUtils.isEmpty(couponCode)){
            	continue;
            }
            
            JSONObject sendResult = this.sendCoupon(record.getUserId(), couponCode, Integer.parseInt(activityId));
            // 发放是否成功状态
            int sendStatus = sendResult.getIntValue("status");
            // 发放优惠券的数量
            int sendCount = sendResult.getIntValue("couponCount");
            if (sendStatus == 0 && sendCount > 0) {
                int result = insertSendRecord(couponCode, record.getUserId(), record.getUserName(), 1);
                if(result <=0){
                    throw new RuntimeException("10月份活动加入发放记录失败，用户id" + record.getUserId());
                }
                _log.info("updateSendCoupon", "用户ID为：" + record.getUserId() + " 的用户优惠期发送成功，发送数量：" + sendCount+ "张");
            }else{
            	int result = insertSendRecord(couponCode, record.getUserId(), record.getUserName(), 2);
            	_log.error("updateSendCoupon", "用户ID为：" + record.getUserId() + " 的用户优惠券发送失败", null);
            	if(result <=0){
                    throw new RuntimeException("10月份活动加入发放记录失败，用户id" + record.getUserId());
                }
            }
            
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * 插入发放记录表
     * @param couponCode
     * @param userId
     * @param userName
     * @param sendFlg
     * @return
     */
    private int insertSendRecord(String couponCode, Integer userId, String userName, Integer sendFlg){
    	ActRewardList reward = new ActRewardList();
    	reward.setUserId(userId);
    	reward.setUserName(userName);
    	reward.setCouponCode(couponCode);
    	reward.setActType(3);
    	reward.setSendFlg(sendFlg);
    	reward.setCreateTime(GetDate.getNowTime10());
    	reward.setUpdateTime(GetDate.getNowTime10());
    	reward.setDelFlg(0);
    	return actRewardListMapper.insertSelective(reward);
    }
    
    private String getCouponCode(String key){
    	if(StringUtils.isEmpty(key)){
    		return null;
    	}
    	
    	if(key.equals("10元代金券")){
    		return "PD1534260";
    	}else if(key.equals("20元代金券")){
    		return "PD1907245";
    	}else if(key.equals("40元代金券")){
    		return "PD8407536";
    	}else if(key.equals("60元代金券")){
    		return "PD1493025";
    	}else if(key.equals("80元代金券")){
    		return "PD8251690";
    	}else if(key.equals("100元代金券")){
    		return "PD7154390";
    	}
    	
    	return null;
    }
    
    /**
     * 校验优惠券奖励是否已发放
     * @param userId
     * @return
     */
    private boolean checkIsSend(Integer userId){
    	ActRewardListExample example = new ActRewardListExample();
    	example.createCriteria().andUserIdEqualTo(userId).andActTypeEqualTo(3).andSendFlgEqualTo(1);
    	List<ActRewardList> rewardList = actRewardListMapper.selectByExample(example);
    	
    	if(rewardList == null || rewardList.isEmpty()){
    		return false;
    	}
    	
    	if(rewardList.get(0).getSendFlg() == 1){
    		return true;
    	}
    	
    	return true;
    }
    
    /**
     * 
     * 发放优惠券
     * 
     * @author hsy
     * @param userId
     * @return
     */
    private JSONObject sendCoupon(Integer userId, String couponCode, Integer actId) {
    	CommonParamBean paramBean = new CommonParamBean();
		paramBean.setUserId(String.valueOf(userId));
		paramBean.setCouponSource(2);
		paramBean.setCouponCode(couponCode);
		paramBean.setSendCount(1);
		paramBean.setActivityId(actId);
		paramBean.setRemark("10月份出借返现活动");
		paramBean.setSendFlg(0);
        
        _log.info("---------------开始调用发券接口（十月份出借返现活动），用户id：" + userId + " couponCode: " + couponCode);
        // 调用发放优惠券接口
        String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
        _log.info("--------------调用发券接口结束（十月份出借返现活动），用户id：" + userId + " couponCode: " + couponCode);
        JSONObject sendResult = JSONObject.parseObject(jsonStr);

        return sendResult;
    }
    
    

	private List<ActTen2017Customize> selectRecordList(Map<String, Object> paraMap) {
		return actTen2017CustomizeMapper.selectTenderReturnList(paraMap);

	}

}
