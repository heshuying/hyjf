package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhRegion;
import com.hyjf.mybatis.model.auto.HjhRegionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhRegionMapper {
    int countByExample(HjhRegionExample example);

    int deleteByExample(HjhRegionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhRegion record);

    int insertSelective(HjhRegion record);

    List<HjhRegion> selectByExample(HjhRegionExample example);

    HjhRegion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhRegion record, @Param("example") HjhRegionExample example);

    int updateByExample(@Param("record") HjhRegion record, @Param("example") HjhRegionExample example);

    int updateByPrimaryKeySelective(HjhRegion record);

    int updateByPrimaryKey(HjhRegion record);
}