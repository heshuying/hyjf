package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/10/10.
 */
public interface TwoelevenCustomizeMapper {


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
     * 累计年华出借金额 和用户信息查询
     * @param map
     * @return
     */
    List<TwoelevenCustomize> selectInvestReward(Map<String, Object> map);
    /**
     * 查询累计年华出借实物奖励记录
     * @param list
     * @return
     */
    List<Integer> selectInvestRewardSW(List<TwoelevenCustomize> list);
    /**
     * 插入累计年华出借金额 和用户信息
     * @param list
     * @return
     */
    void batchInsertInvestReward(List<TwoelevenCustomize> list);

    /**
     * 获得优惠券编码的数量
     * @param couponCode 优惠编码
     * @return
     */
    Integer getCouponQuantity(@Param("couponCode") String couponCode);

    /**
     * 获得用户累计年化出借金额
     * @param userId
     * @return
     */
    BigDecimal getYearAmount(@Param("userId") Integer userId);

    /**
     * 获得用户已经中奖数
     * @param userId
     * @return
     */
    Integer getUserAwardTime(@Param("userId") Integer userId);

    /**
     * 获得用户信息
     * @return
     */
    TwoelevenCustomize getUserInfos(@Param("userId") Integer userId);
}
