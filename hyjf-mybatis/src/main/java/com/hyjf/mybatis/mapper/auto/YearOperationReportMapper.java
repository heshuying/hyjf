package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.YearOperationReport;
import com.hyjf.mybatis.model.auto.YearOperationReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface YearOperationReportMapper {
    int countByExample(YearOperationReportExample example);

    int deleteByExample(YearOperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(YearOperationReport record);

    int insertSelective(YearOperationReport record);

    List<YearOperationReport> selectByExample(YearOperationReportExample example);

    YearOperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") YearOperationReport record, @Param("example") YearOperationReportExample example);

    int updateByExample(@Param("record") YearOperationReport record, @Param("example") YearOperationReportExample example);

    int updateByPrimaryKeySelective(YearOperationReport record);

    int updateByPrimaryKey(YearOperationReport record);
}