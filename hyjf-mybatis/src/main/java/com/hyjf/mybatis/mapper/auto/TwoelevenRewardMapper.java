package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.TwoelevenReward;
import com.hyjf.mybatis.model.auto.TwoelevenRewardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TwoelevenRewardMapper {
    int countByExample(TwoelevenRewardExample example);

    int deleteByExample(TwoelevenRewardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TwoelevenReward record);

    int insertSelective(TwoelevenReward record);

    List<TwoelevenReward> selectByExample(TwoelevenRewardExample example);

    TwoelevenReward selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TwoelevenReward record, @Param("example") TwoelevenRewardExample example);

    int updateByExample(@Param("record") TwoelevenReward record, @Param("example") TwoelevenRewardExample example);

    int updateByPrimaryKeySelective(TwoelevenReward record);

    int updateByPrimaryKey(TwoelevenReward record);
}