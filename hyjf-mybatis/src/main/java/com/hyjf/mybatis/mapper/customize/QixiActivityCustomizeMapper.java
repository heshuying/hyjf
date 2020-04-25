package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.admin.QixiActivityCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author by xiehuili on 2018/7/23.
 */
public interface QixiActivityCustomizeMapper {

    /**
     * 查询七夕活动单笔出借条数
     * @param map
     * @return
     */
    public Integer selectQixiActivityCount( Map<String, Object> map);

    /**
     * 查询七夕活动累计出借列表
     * @param map
     * @return
     */
    public List<QixiActivityCustomize> selectQixiActivityList(Map<String, Object> map);

    /**
     * 查询七夕活动累计出借条数
     * @param map
     * @return
     */
    public Integer selectQixiActivityTotalCount( Map<String, Object> map);

    /**
     * 查询七夕活动单笔出借列表
     * @param map
     * @return
     */
    public List<QixiActivityCustomize> selectQixiActivityTotalList(Map<String, Object> map);


    /**
     * 查询七夕活动奖励条数
     * @param map
     * @return
     */
    public Integer selectQixiActivityAwardCount( Map<String, Object> map);

    /**
     * 查询七夕活动奖励列表
     * @param map
     * @return
     */
    public List<QixiActivityCustomize> selectQixiActivityAwardList(Map<String, Object> map);
}
