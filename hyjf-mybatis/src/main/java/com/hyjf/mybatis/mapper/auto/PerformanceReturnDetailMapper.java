package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.PerformanceReturnDetail;
import com.hyjf.mybatis.model.auto.PerformanceReturnDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PerformanceReturnDetailMapper {
    int countByExample(PerformanceReturnDetailExample example);

    int deleteByExample(PerformanceReturnDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PerformanceReturnDetail record);

    int insertSelective(PerformanceReturnDetail record);

    List<PerformanceReturnDetail> selectByExample(PerformanceReturnDetailExample example);

    PerformanceReturnDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PerformanceReturnDetail record, @Param("example") PerformanceReturnDetailExample example);

    int updateByExample(@Param("record") PerformanceReturnDetail record, @Param("example") PerformanceReturnDetailExample example);

    int updateByPrimaryKeySelective(PerformanceReturnDetail record);

    int updateByPrimaryKey(PerformanceReturnDetail record);
}