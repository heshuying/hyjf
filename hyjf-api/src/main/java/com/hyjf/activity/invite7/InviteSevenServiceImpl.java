package com.hyjf.activity.invite7;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample.Criteria;

@Service("inviteSevenService")
public class InviteSevenServiceImpl extends BaseServiceImpl implements InviteSevenService {

    /**
     * 
     * 用户活动奖励总数
     * @author hsy
     * @param investType
     * @param userId
     * @return
     */
    @Override
    public Integer selectRecordCount(String investType, Integer userId) {
        ActivityInviteSevenExample example = new ActivityInviteSevenExample();
        Criteria criteria = example.createCriteria();
        criteria.andRewardTypeNotEqualTo(3);
        if(userId != null && userId > 0){
            criteria.andUseridEqualTo(userId);
        }
        if(StringUtils.isNotEmpty(investType)){
            if(investType.equals("0")){
                // 全部
                criteria.andRewardTypeNotEqualTo(3);
            }else if(investType.equals("1")){
                // 已出借
                criteria.andRewardTypeNotEqualTo(0).andRewardTypeNotEqualTo(3);
            }else if(investType.equals("2")){
                // 未出借
                criteria.andRewardTypeEqualTo(0);
            }
        }

        
        return activityInviteSevenMapper.countByExample(example);
    }

    /**
     * 
     * 用户活动奖励列表
     * @author hsy
     * @param investType
     * @param userId
     * @param paginator
     * @return
     */
    @Override
    public List<ActivityInviteSeven> selectRecordList(String investType, Integer userId, Paginator paginator) {
        ActivityInviteSevenExample example = new ActivityInviteSevenExample();
        Criteria criteria = example.createCriteria();
        criteria.andRewardTypeNotEqualTo(3);
        if(userId != null && userId > 0){
            criteria.andUseridEqualTo(userId);
        }
        if(StringUtils.isNotEmpty(investType)){
            if(investType.equals("0")){
                // 全部
                criteria.andRewardTypeNotEqualTo(3);
            }else if(investType.equals("1")){
                // 已出借
                criteria.andRewardTypeNotEqualTo(0).andRewardTypeNotEqualTo(3);
            }else if(investType.equals("2")){
                // 未出借
                criteria.andRewardTypeEqualTo(0);
            }
        }
        
        example.setOrderByClause("regist_time desc");
        example.setLimitStart(paginator.getOffset());
        example.setLimitEnd(paginator.getLimit());
        
        List<ActivityInviteSeven> recordList = activityInviteSevenMapper.selectByExample(example);
        for(ActivityInviteSeven record : recordList){
            if(StringUtils.isNotEmpty(record.getMobile())){
                record.setMobileInvited(AsteriskProcessUtil.getAsteriskedValue(record.getMobileInvited(), 3, 4) + record.getMobileInvited().substring(record.getMobileInvited().length() -4));
            }
            record.setUsernameInvited(AsteriskProcessUtil.getAsteriskedValue(record.getUsernameInvited(), 1, 2));
            
            if(record.getRewardType() == 0){
                record.setMoneyFirst(BigDecimal.ZERO);
                record.setInvestTime(0);
            }
        }
        
        return recordList;
        
    }
    

}
