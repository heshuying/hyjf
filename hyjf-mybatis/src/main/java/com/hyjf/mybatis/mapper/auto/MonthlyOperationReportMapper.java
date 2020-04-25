package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MonthlyOperationReport;
import com.hyjf.mybatis.model.auto.MonthlyOperationReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MonthlyOperationReportMapper {
    int countByExample(MonthlyOperationReportExample example);

    int deleteByExample(MonthlyOperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MonthlyOperationReport record);

    int insertSelective(MonthlyOperationReport record);

    List<MonthlyOperationReport> selectByExample(MonthlyOperationReportExample example);

    MonthlyOperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MonthlyOperationReport record, @Param("example") MonthlyOperationReportExample example);

    int updateByExample(@Param("record") MonthlyOperationReport record, @Param("example") MonthlyOperationReportExample example);

    int updateByPrimaryKeySelective(MonthlyOperationReport record);

    int updateByPrimaryKey(MonthlyOperationReport record);
}