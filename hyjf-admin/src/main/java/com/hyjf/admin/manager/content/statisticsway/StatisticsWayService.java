package com.hyjf.admin.manager.content.statisticsway;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.StatisticsWayConfigure;
import com.hyjf.mybatis.model.customize.statisticsway.StatisticsWayConfigureCustomize;

import java.util.List;
import java.util.Map;

/**
 * 运营报告---统计方式配置
 * @author by xiehuili on 2018/6/20.
 */
public interface StatisticsWayService {

    /**
     * 根据条件查询数据
     *
     * @param map
     * @return
     */
    public Integer countRecordList(Map<String, Object> map);

    /**
     * 根据条件查询数据
     *
     * @param map
     * @return
     */
    public List<StatisticsWayConfigureCustomize> selectRecordList(Map<String, Object> map);
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    public StatisticsWayConfigureCustomize selectstatisticsWayById(Integer id);
    /**
     * 校验表单字段
     *
     * @return
     */
    public JSONObject validatFieldCheck(StatisticsWayConfigure form) ;

    /**
     * 新增
     *
     * @param form
     */
    public void updateStaticsWay(StatisticsWayConfigure form) ;

    /**
     * 删除统计方式
     *
     * @param id
     */
    public void deleteStaticsWay(Integer id) ;
}
