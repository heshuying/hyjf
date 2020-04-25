package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTimeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityBillionSecondTimeMapper {
    int countByExample(ActivityBillionSecondTimeExample example);

    int deleteByExample(ActivityBillionSecondTimeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityBillionSecondTime record);

    int insertSelective(ActivityBillionSecondTime record);

    List<ActivityBillionSecondTime> selectByExample(ActivityBillionSecondTimeExample example);

    ActivityBillionSecondTime selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityBillionSecondTime record, @Param("example") ActivityBillionSecondTimeExample example);

    int updateByExample(@Param("record") ActivityBillionSecondTime record, @Param("example") ActivityBillionSecondTimeExample example);

    int updateByPrimaryKeySelective(ActivityBillionSecondTime record);

    int updateByPrimaryKey(ActivityBillionSecondTime record);
}