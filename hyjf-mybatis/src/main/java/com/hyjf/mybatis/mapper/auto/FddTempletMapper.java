package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.auto.FddTempletExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FddTempletMapper {
    int countByExample(FddTempletExample example);

    int deleteByExample(FddTempletExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FddTemplet record);

    int insertSelective(FddTemplet record);

    List<FddTemplet> selectByExample(FddTempletExample example);

    FddTemplet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FddTemplet record, @Param("example") FddTempletExample example);

    int updateByExample(@Param("record") FddTemplet record, @Param("example") FddTempletExample example);

    int updateByPrimaryKeySelective(FddTemplet record);

    int updateByPrimaryKey(FddTemplet record);
}