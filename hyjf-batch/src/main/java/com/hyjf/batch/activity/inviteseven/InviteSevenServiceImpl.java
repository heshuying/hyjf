package com.hyjf.batch.activity.inviteseven;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityListExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.mybatis.model.customize.admin.InviteSevenCustomize;

/**
 * 
 * 七月份邀请奖励统计
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 * @see 上午9:54:28
 */
@Service
public class InviteSevenServiceImpl extends BaseServiceImpl implements InviteSevenService {
    
    Logger _log = LoggerFactory.getLogger(InviteSevenServiceImpl.class);

	@Override
    public void updateInviteStatistic() throws Exception {
		//获取出借夺宝活动id
	    String[] couponCodes = PropUtils.getSystem("activity.invite.seven.couponcode").split(",");
		int activityId = Integer.valueOf(PropUtils.getSystem("activity.invite.seven.id"));
		ActivityListExample example = new ActivityListExample();
		ActivityListExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(activityId);
		
		List<ActivityList> activityList = activityListMapper.selectByExample(example);
		if(activityList ==null || activityList.isEmpty()){
		    throw new RuntimeException("未获取到活动数据，活动配置ID：" + activityId);
		}
		
		//校验活动是否过期
		ActivityList activity = activityList.get(0);
		int nowTime = GetDate.getNowTime10();
		if(nowTime < activity.getTimeStart() || nowTime > activity.getTimeEnd()){
		    _log.info("活动尚未开始或已结束，开始时间：" + GetDate.formatDate(activity.getTimeStart()*1000));
		    return;
		}
		
		// 被邀请人首投≥2000元及被邀请人首投≥5000元统计
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("actStart", activity.getTimeStart());
		paraMap.put("actEnd", activity.getTimeEnd());
		List<InviteSevenCustomize> inviteList = activityInviteSevenCustomizeMapper.selectInviteSevenList(paraMap);
        if(inviteList != null && !inviteList.isEmpty()){
            for(InviteSevenCustomize invite : inviteList){
                BigDecimal investSum = invite.getInvestSum() == null? BigDecimal.ZERO : invite.getInvestSum();
                Integer investTime = StringUtils.isNotEmpty(invite.getInvestTime()) ? Integer.parseInt(invite.getInvestTime()) : null;
                Integer creditTime = StringUtils.isNotEmpty(invite.getCreditInvestTime()) ? Integer.parseInt(invite.getCreditInvestTime()) : null;
                ActivityInviteSeven inviteBean = new ActivityInviteSeven();
                inviteBean.setUpdateTime(GetDate.getNowTime10());
                inviteBean.setMoneyFirst(investSum);
                inviteBean.setInvestTime(investTime);
                if(StringUtils.isEmpty(invite.getCreditInvestTime())){
                    if(investSum.compareTo(new BigDecimal(5000)) >= 0){
                        inviteBean.setRewardType(2);
                        inviteBean.setCouponCode(couponCodes[1]);
                        inviteBean.setCouponName(getCouponName(couponCodes[1]));
                    }else if(investSum.compareTo(new BigDecimal(2000)) >= 0){
                        inviteBean.setRewardType(1);
                        inviteBean.setCouponCode(couponCodes[0]);
                        inviteBean.setCouponName(getCouponName(couponCodes[0]));
                    }else{
                        inviteBean.setRewardType(0);
                    }
                }else{
                    // 有债转出借有直投类出借的情况
                    if(investTime != null && investTime != 0 && investTime < creditTime){
                        if(investSum.compareTo(new BigDecimal(5000)) >= 0){
                            inviteBean.setRewardType(2);
                            inviteBean.setCouponCode(couponCodes[1]);
                            inviteBean.setCouponName(getCouponName(couponCodes[1]));
                        }else if(investSum.compareTo(new BigDecimal(2000)) >= 0){
                            inviteBean.setRewardType(1);
                            inviteBean.setCouponCode(couponCodes[0]);
                            inviteBean.setCouponName(getCouponName(couponCodes[0]));
                        }else{
                            inviteBean.setRewardType(0);
                        }
                    }else{
                        inviteBean.setRewardType(0);
                    }
                }
                
                ActivityInviteSevenExample inviteExample = new ActivityInviteSevenExample();
                inviteExample.createCriteria().andUseridInvitedEqualTo(invite.getUseridInvited());
                int result = activityInviteSevenMapper.updateByExampleSelective(inviteBean, inviteExample);
                
                if(result == 0){
                    inviteBean.setUserid(invite.getUserid());
                    inviteBean.setUsername(invite.getUsername());
                    inviteBean.setUserRealname(invite.getTruename());
                    inviteBean.setUseridInvited(invite.getUseridInvited());
                    inviteBean.setUsernameInvited(invite.getUsernameInvited());
                    inviteBean.setMobile(invite.getMobile());
                    inviteBean.setMobileInvited(invite.getMobileInvited());
                    inviteBean.setRegistTime(StringUtils.isNotEmpty(invite.getRegTime())?Integer.parseInt(invite.getRegTime()): null);
                    // 0 未发放
                    inviteBean.setSendFlg(0);
                    inviteBean.setAddTime(GetDate.getNowTime10());
                    inviteBean.setUpdateTime(GetDate.getNowTime10());
                    
                    activityInviteSevenMapper.insertSelective(inviteBean);
                }
            }
        }
        
        // 累计10人统计
        List<Map<String,Object>> tenList = activityInviteSevenCustomizeMapper.selectInviteSevenTenList();
        for(Map<String,Object> bean : tenList){
            Integer userid = (Integer)bean.get("userid");
            String username = (String)bean.get("username");
            String userRealname = (String)bean.get("userRealname");
            String mobile = (String)bean.get("mobile");
            Long validCount = (Long)bean.get("validCount");
            
            ActivityInviteSeven inviteBean = new ActivityInviteSeven();
            inviteBean.setInviteCount(validCount.intValue());
            inviteBean.setUpdateTime(GetDate.getNowTime10());
            
            ActivityInviteSevenExample inviteExample = new ActivityInviteSevenExample();
            inviteExample.createCriteria().andUseridEqualTo(userid).andRewardTypeEqualTo(3);
            int result = activityInviteSevenMapper.updateByExampleSelective(inviteBean, inviteExample);
            
            if(result == 0){
                inviteBean.setUserid(userid);
                inviteBean.setUsername(username);
                inviteBean.setUserRealname(userRealname);;
                inviteBean.setMobile(mobile);
                inviteBean.setRewardType(3);
                inviteBean.setInviteCount(validCount.intValue());
                inviteBean.setCouponCode(couponCodes[2]);
                inviteBean.setCouponName(getCouponName(couponCodes[2]));
                // 0 未发放
                inviteBean.setSendFlg(0);
                inviteBean.setAddTime(GetDate.getNowTime10());
                inviteBean.setUpdateTime(GetDate.getNowTime10());
                
                activityInviteSevenMapper.insertSelective(inviteBean);
            }
            
        }
		
	}
	
	/**
	 * 
	 * 根据优惠券编号获取优惠券名称
	 * @author hsy
	 * @param couponCode
	 * @return
	 */
	private String getCouponName(String couponCode){
	    if(StringUtils.isEmpty(couponCode)){
	        return "";
	    }
	    
	    CouponConfigExample example = new CouponConfigExample();
	    example.createCriteria().andCouponCodeEqualTo(couponCode);
	    List<CouponConfig> configList = couponConfigMapper.selectByExample(example);
	    if(configList != null && !configList.isEmpty()){
	        return configList.get(0).getCouponName();
	    }
	    
	    return "";
	}


    
}
