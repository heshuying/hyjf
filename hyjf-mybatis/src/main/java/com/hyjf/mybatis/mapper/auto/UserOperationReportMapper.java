package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UserOperationReport;
import com.hyjf.mybatis.model.auto.UserOperationReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserOperationReportMapper {
    int countByExample(UserOperationReportExample example);

    int deleteByExample(UserOperationReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserOperationReport record);

    int insertSelective(UserOperationReport record);

    List<UserOperationReport> selectByExample(UserOperationReportExample example);

    UserOperationReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserOperationReport record, @Param("example") UserOperationReportExample example);

    int updateByExample(@Param("record") UserOperationReport record, @Param("example") UserOperationReportExample example);

    int updateByPrimaryKeySelective(UserOperationReport record);

    int updateByPrimaryKey(UserOperationReport record);
}