package com.hyjf.admin.manager.activity.inviteact;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample.Criteria;

@Service
public class InviteSevenActivityServiceImpl extends BaseServiceImpl implements InviteSevenActivityService {

    @Override
    public Integer selectRecordCount(InviteSevenActivityBean form) {
        ActivityInviteSevenExample example = new ActivityInviteSevenExample();
        Criteria criteria = example.createCriteria();
        criteria.andRewardTypeNotEqualTo(0);
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            criteria.andUsernameEqualTo(form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            criteria.andMobileEqualTo(form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getCouponNameSrch())){
            criteria.andCouponNameEqualTo(form.getCouponNameSrch());
        }
        
        return activityInviteSevenMapper.countByExample(example);
    }

    @Override
    public List<ActivityInviteSeven> selectRecordList(InviteSevenActivityBean form, Paginator paginator) {
        ActivityInviteSevenExample example = new ActivityInviteSevenExample();
        Criteria criteria = example.createCriteria();
        criteria.andRewardTypeNotEqualTo(0);
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            criteria.andUsernameEqualTo(form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            criteria.andMobileEqualTo(form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getCouponNameSrch())){
            criteria.andCouponNameEqualTo(form.getCouponNameSrch());
        }
        
        example.setLimitStart(paginator.getOffset());
        example.setLimitEnd(paginator.getLimit());
        example.setOrderByClause("regist_time desc");
        
        return activityInviteSevenMapper.selectByExample(example);
        
    }
    

}
