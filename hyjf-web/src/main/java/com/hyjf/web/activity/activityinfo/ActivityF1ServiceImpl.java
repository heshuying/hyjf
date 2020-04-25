package com.hyjf.web.activity.activityinfo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.mybatis.model.auto.ActivityF1Example;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class ActivityF1ServiceImpl extends BaseServiceImpl implements ActivityF1Service {

    /**
     * 根据活动Type获取活动详情列表
     * @author liuyang
     * @param activityType
     * @return
     */
    @Override
    public List<ActivityF1> getActivityF1ListByActivityType(String activityType) {
        ActivityF1Example example = new ActivityF1Example();
        example.setLimitStart(0);
        example.setLimitEnd(20);
        ActivityF1Example.Criteria cra = example.createCriteria();
        cra.andActivityTypeEqualTo(activityType);
        cra.andSpeedGreaterThanOrEqualTo(200);

        example.setOrderByClause("`speed` Desc,`tender_account_all` Desc,`update_time` Desc");

        return activityF1Mapper.selectByExample(example);
    }

    /**
     * 
     * 根据用户ID查询该用户参加活动情况
     * @author liuyang
     * @param user_id
     * @return
     */
    @Override
    public ActivityF1 getActivityF1ByUserId(Integer user_id) {

        return activityF1Mapper.selectByPrimaryKey(user_id);
    }

    /**
     * 
     * 活动情况插入
     * @author liuyang
     * @param activityF1
     * @return
     */
    @Override
    public int insertActivityF1(ActivityF1 activityF1) {

        // 根据用户ID获取用户名,真实姓名,手机号
        UserInfoCustomize userInfo = userInfoCustomizeMapper.queryUserInfoByUserId(activityF1.getUserId());
        // 用户名
        activityF1.setUserName(userInfo.getUserName());
        // 手机号
        activityF1.setMobile(userInfo.getMobile());
        // 用户真实姓名
        activityF1.setRealName(userInfo.getTrueName());
        // 活动类型
        activityF1.setActivityType("2");
        // 是否APP出借
        if ("1".equals(activityF1.getIsAppFlg())) {
            // APP出借的情况
            activityF1.setSpeed(activityF1.getSpeed() + 1);
        }
        // 是否首投>=5000
        if ("1".equals(activityF1.getIsFirstFlg())) {
            // 首投>=5000
            activityF1.setSpeed(activityF1.getSpeed() + 1);
        }
        // 五月份累计“直投”出借每满10000加1km/h
        activityF1.setSpeed((activityF1.getTenderAccountAll().divideToIntegralValue(new BigDecimal(10000))).intValue());
        //
        if (activityF1.getSpeed() >= 5 && activityF1.getSpeed() < 10) {
            activityF1.setReturnAmountActivity(new BigDecimal(60));
        } else if (activityF1.getSpeed() >= 10 && activityF1.getSpeed() < 30) {
            activityF1.setReturnAmountActivity(new BigDecimal(150));
        } else if (activityF1.getSpeed() >= 30 && activityF1.getSpeed() < 60) {
            activityF1.setReturnAmountActivity(new BigDecimal(480));
        } else if (activityF1.getSpeed() >= 60 && activityF1.getSpeed() < 80) {
            activityF1.setReturnAmountActivity(new BigDecimal(1000));
        } else if (activityF1.getSpeed() >= 80 && activityF1.getSpeed() < 100) {
            activityF1.setReturnAmountActivity(new BigDecimal(1400));
        } else if (activityF1.getSpeed() >= 100 && activityF1.getSpeed() < 120) {
            activityF1.setReturnAmountActivity(new BigDecimal(1800));
        } else if (activityF1.getSpeed() >= 120 && activityF1.getSpeed() < 150) {
            activityF1.setReturnAmountActivity(new BigDecimal(2300));
        } else if (activityF1.getSpeed() >= 150) {
            activityF1.setReturnAmountActivity(new BigDecimal(3000));
        }
        // 数据插入
        return activityF1Mapper.insertSelective(activityF1);
    }

    /**
     * 
     * 活动情况更新
     * @author yyc
     * @param activityF1
     * @return
     */
    @Override
    public int updateActivityF1(ActivityF1 activityF1) {
        // 根据用户id查询活动情况
        ActivityF1 activityF1Old = activityF1Mapper.selectByPrimaryKey(activityF1.getUserId());
        // 是否app出借
        if ("1".equals(activityF1.getIsAppFlg()) && "0".equals(activityF1Old.getIsAppFlg())) {
            // 活动期内使用APP首投加1km/h（仅一次）
            activityF1.setSpeed(activityF1.getSpeed() + 1);
        }
        //
        if (activityF1.getSpeed() >= 5 && activityF1.getSpeed() < 10) {
            activityF1.setReturnAmountActivity(new BigDecimal(60));
        } else if (activityF1.getSpeed() >= 10 && activityF1.getSpeed() < 30) {
            activityF1.setReturnAmountActivity(new BigDecimal(150));
        } else if (activityF1.getSpeed() >= 30 && activityF1.getSpeed() < 60) {
            activityF1.setReturnAmountActivity(new BigDecimal(480));
        } else if (activityF1.getSpeed() >= 60 && activityF1.getSpeed() < 80) {
            activityF1.setReturnAmountActivity(new BigDecimal(1000));
        } else if (activityF1.getSpeed() >= 80 && activityF1.getSpeed() < 100) {
            activityF1.setReturnAmountActivity(new BigDecimal(1400));
        } else if (activityF1.getSpeed() >= 100 && activityF1.getSpeed() < 120) {
            activityF1.setReturnAmountActivity(new BigDecimal(1800));
        } else if (activityF1.getSpeed() >= 120 && activityF1.getSpeed() < 150) {
            activityF1.setReturnAmountActivity(new BigDecimal(2300));
        } else if (activityF1.getSpeed() >= 150) {
            activityF1.setReturnAmountActivity(new BigDecimal(3000));
        }
        return activityF1Mapper.updateByPrimaryKey(activityF1);
    }

    /**
     * 根据用户id查询活动件数
     * 0件的情况:插入
     * 0件以外的情况:更新
     * @author liuyang
     * @param user_id
     * @return
     */
    @Override
    public int countActivityF1(Integer user_id) {
        ActivityF1Example example = new ActivityF1Example();
        ActivityF1Example.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(user_id);
        return activityF1Mapper.countByExample(example);
    }

}
