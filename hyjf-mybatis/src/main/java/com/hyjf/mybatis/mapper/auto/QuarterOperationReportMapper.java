package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.QuarterOperationReport;
import com.hyjf.mybatis.model.auto.QuarterOperationReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuarterOperationReportMapper {
    int countByExample(QuarterOperationReportExample example);

    int deleteByExample(QuarterOperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(QuarterOperationReport record);

    int insertSelective(QuarterOperationReport record);

    List<QuarterOperationReport> selectByExample(QuarterOperationReportExample example);

    QuarterOperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QuarterOperationReport record, @Param("example") QuarterOperationReportExample example);

    int updateByExample(@Param("record") QuarterOperationReport record, @Param("example") QuarterOperationReportExample example);

    int updateByPrimaryKeySelective(QuarterOperationReport record);

    int updateByPrimaryKey(QuarterOperationReport record);
}