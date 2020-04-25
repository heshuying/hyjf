package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.SellDailyDistribution;
import com.hyjf.mybatis.model.auto.SellDailyDistributionExample;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomize;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomizeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SellDailyDistributionCustomizeMapper {
    int countByExample(SellDailyDistributionCustomizeExample example);

    int deleteByExample(SellDailyDistributionCustomizeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SellDailyDistributionCustomize record);

    int insertSelective(SellDailyDistributionCustomize record);

    List<SellDailyDistributionCustomize> selectByExample(SellDailyDistributionCustomizeExample example);

    SellDailyDistributionCustomize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SellDailyDistributionCustomize record, @Param("example") SellDailyDistributionCustomizeExample example);

    int updateByExample(@Param("record") SellDailyDistribution record, @Param("example") SellDailyDistributionCustomizeExample example);

    int updateByPrimaryKeySelective(SellDailyDistributionCustomize record);

    int updateByPrimaryKey(SellDailyDistributionCustomize record);
}