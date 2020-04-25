package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.WeeklyReport;
import com.hyjf.mybatis.model.auto.WeeklyReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeeklyReportMapper {
    int countByExample(WeeklyReportExample example);

    int deleteByExample(WeeklyReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WeeklyReport record);

    int insertSelective(WeeklyReport record);

    List<WeeklyReport> selectByExample(WeeklyReportExample example);

    WeeklyReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WeeklyReport record, @Param("example") WeeklyReportExample example);

    int updateByExample(@Param("record") WeeklyReport record, @Param("example") WeeklyReportExample example);

    int updateByPrimaryKeySelective(WeeklyReport record);

    int updateByPrimaryKey(WeeklyReport record);
}