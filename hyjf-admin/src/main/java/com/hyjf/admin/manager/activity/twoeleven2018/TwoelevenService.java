package com.hyjf.admin.manager.activity.twoeleven2018;

import com.hyjf.mybatis.model.auto.TwoelevenReward;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/10/10.
 */
public interface TwoelevenService {


    /**
     * 查询秒杀条数
     * @param map
     * @return
     */
    Integer selectTwoelevenSeckillCount( Map<String, Object> map);
    /**
     * 查询秒杀列表
     * @param map
     * @return
     */
    List<TwoelevenCustomize> selectTwoelevenSeckillList(Map<String, Object> map);


    /**
     * 年化出借金额 查询条数
     * @param map
     * @return
     */
    Integer selectTwoelevenInvestCount( Map<String, Object> map);
    /**
     * 年化出借金额 查询列表
     * @param map
     * @return
     */
    List<TwoelevenCustomize> selectTwoelevenInvestList(Map<String, Object> map);


    /**
     * 奖励（新款iPhone）明细查询条数
     * @param map
     * @return
     */
    Integer selectTwoelevenRewardCount( Map<String, Object> map);
    /**
     * 奖励（新款iPhone）明细查询列表
     * @param map
     * @return
     */
    List<TwoelevenCustomize> selectTwoelevenRewardList(Map<String, Object> map);
    /**
     * 奖励（新款iPhone）明细修改
     * @param id
     * @return
     */
    int updateTwoelevenReward( TwoelevenReward record );


}
