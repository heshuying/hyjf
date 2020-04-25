package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.TenthOperationReport;
import com.hyjf.mybatis.model.auto.TenthOperationReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TenthOperationReportMapper {
    int countByExample(TenthOperationReportExample example);

    int deleteByExample(TenthOperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TenthOperationReport record);

    int insertSelective(TenthOperationReport record);

    List<TenthOperationReport> selectByExample(TenthOperationReportExample example);

    TenthOperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TenthOperationReport record, @Param("example") TenthOperationReportExample example);

    int updateByExample(@Param("record") TenthOperationReport record, @Param("example") TenthOperationReportExample example);

    int updateByPrimaryKeySelective(TenthOperationReport record);

    int updateByPrimaryKey(TenthOperationReport record);
}