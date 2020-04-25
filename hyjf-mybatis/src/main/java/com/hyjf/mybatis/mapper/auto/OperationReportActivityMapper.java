package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.OperationReportActivity;
import com.hyjf.mybatis.model.auto.OperationReportActivityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperationReportActivityMapper {
    int countByExample(OperationReportActivityExample example);

    int deleteByExample(OperationReportActivityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OperationReportActivity record);

    int insertSelective(OperationReportActivity record);

    List<OperationReportActivity> selectByExample(OperationReportActivityExample example);

    OperationReportActivity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OperationReportActivity record, @Param("example") OperationReportActivityExample example);

    int updateByExample(@Param("record") OperationReportActivity record, @Param("example") OperationReportActivityExample example);

    int updateByPrimaryKeySelective(OperationReportActivity record);

    int updateByPrimaryKey(OperationReportActivity record);
}