package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearPrizeConfig;
import com.hyjf.mybatis.model.auto.NewyearPrizeConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearPrizeConfigMapper {
    int countByExample(NewyearPrizeConfigExample example);

    int deleteByExample(NewyearPrizeConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearPrizeConfig record);

    int insertSelective(NewyearPrizeConfig record);

    List<NewyearPrizeConfig> selectByExample(NewyearPrizeConfigExample example);

    NewyearPrizeConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearPrizeConfig record, @Param("example") NewyearPrizeConfigExample example);

    int updateByExample(@Param("record") NewyearPrizeConfig record, @Param("example") NewyearPrizeConfigExample example);

    int updateByPrimaryKeySelective(NewyearPrizeConfig record);

    int updateByPrimaryKey(NewyearPrizeConfig record);
}