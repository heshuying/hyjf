package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HalfYearOperationReport;
import com.hyjf.mybatis.model.auto.HalfYearOperationReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HalfYearOperationReportMapper {
    int countByExample(HalfYearOperationReportExample example);

    int deleteByExample(HalfYearOperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HalfYearOperationReport record);

    int insertSelective(HalfYearOperationReport record);

    List<HalfYearOperationReport> selectByExample(HalfYearOperationReportExample example);

    HalfYearOperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HalfYearOperationReport record, @Param("example") HalfYearOperationReportExample example);

    int updateByExample(@Param("record") HalfYearOperationReport record, @Param("example") HalfYearOperationReportExample example);

    int updateByPrimaryKeySelective(HalfYearOperationReport record);

    int updateByPrimaryKey(HalfYearOperationReport record);
}