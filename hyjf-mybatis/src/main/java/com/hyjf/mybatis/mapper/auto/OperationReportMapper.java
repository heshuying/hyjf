package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.OperationReport;
import com.hyjf.mybatis.model.auto.OperationReportExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OperationReportMapper {

    int countByExample(OperationReportExample example);

    int deleteByExample(OperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OperationReport record);

    int insertSelective(OperationReport record);

    List<OperationReport> selectByExample(OperationReportExample example);

    OperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OperationReport record, @Param("example") OperationReportExample example);

    int updateByExample(@Param("record") OperationReport record, @Param("example") OperationReportExample example);

    int updateByPrimaryKeySelective(OperationReport record);

    int updateByPrimaryKey(OperationReport record);

}