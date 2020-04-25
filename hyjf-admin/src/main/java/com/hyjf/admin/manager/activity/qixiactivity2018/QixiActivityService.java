package com.hyjf.admin.manager.activity.qixiactivity2018;

import com.hyjf.mybatis.model.customize.admin.QixiActivityCustomize;
import com.hyjf.mybatis.model.auto.ActivityQixi;
import java.util.List;
import java.util.Map;

/**
 * @author by xiehuili on 2018/7/23.
 */
public interface QixiActivityService {

    /**
     * 查询七夕活动条数
     * @param map
     * @return
     */
    Integer selectQixiActivityCount( Map<String, Object> map);
    /**
     * 查询七夕活动列表
     * @param map
     * @return
     */
    List<QixiActivityCustomize> selectQixiActivityList( Map<String, Object> map);
    /**
     * 查询七夕活动累计出借条数
     * @param map
     * @return
     */
    Integer selectQixiActivityTotalCount( Map<String, Object> map);
    /**
     * 查询七夕活动累计出借列表
     * @param map
     * @return
     */
    List<QixiActivityCustomize> selectQixiActivityTotalList( Map<String, Object> map);
    /**
     * 查询七夕活动奖励条数
     * @param map
     * @return
     */
    Integer selectQixiActivityAwardCount( Map<String, Object> map);
    /**
     * 查询七夕活动奖励明细列表
     * @param map
     * @return
     */
    List<QixiActivityCustomize> selectQixiActivityAwardList( Map<String, Object> map);

    /**
     * 修改七夕活动奖励状态
     * @param from
     * @return
     */
    void updateQixiActivityAward(ActivityQixi from);
}