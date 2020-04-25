package com.hyjf.admin.manager.activity.worldcupactivity;

import com.hyjf.mybatis.model.customize.worldcup.GuessingActivitieCustomize;

import java.util.List;

/**
 * @author xiehuili on 2018/6/13.
 * 世界杯活动
 */
public interface WorldCupActivityService {

    /**
     * 按条件查询竞猜输赢个数
     *
     * @return
     */
    public Integer countRecordBySearchCon(WorldCupActivityBean form);

    /**
     * 获取竞猜输赢活动列表列表
     *
     * @return
     */
    public List<GuessingActivitieCustomize>  getRecordList(WorldCupActivityBean form, int limitStart, int limitEnd);


    /**
     * 按条件查询竞猜冠军个数
     *
     * @return
     */
    public Integer countChampionRecordBySearchCon(WorldCupActivityBean form);

    /**
     * 获取竞猜冠军活动列表列表
     *
     * @return
     */
    public List<GuessingActivitieCustomize>  getChampionRecordList(WorldCupActivityBean form, int limitStart, int limitEnd);


}
