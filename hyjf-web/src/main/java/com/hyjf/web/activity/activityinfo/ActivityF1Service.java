package com.hyjf.web.activity.activityinfo;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.web.BaseService;

/**
 * 
 * F1大师赛活动Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:36:36
 */
public interface ActivityF1Service extends BaseService {
    /**
     * 
     * 根据活动Type获取活动详情一览
     * @author liuyang
     * @param activityType
     * @return
     */
    public List<ActivityF1> getActivityF1ListByActivityType(String activityType);

    /**
     * 
     * 根据用户ID获取用户参与活动状况
     * @author liuyang
     * @param user_id
     * @return
     */
    public ActivityF1 getActivityF1ByUserId(Integer user_id);

    /**
     * 
     * 活动情况插入
     * @author liuyang
     * @param activityF1
     * @return
     */
    public int insertActivityF1(ActivityF1 activityF1);

    /**
     * 
     * 活动情况更新
     * @author yyc
     * @param activityF1
     * @return
     */
    public int updateActivityF1(ActivityF1 activityF1);

    /**
     * 
     * 根据用户id查询活动详情件数
     * @author liuyang
     * @param user_id
     * @return
     */
    public int countActivityF1(Integer user_id);
}
