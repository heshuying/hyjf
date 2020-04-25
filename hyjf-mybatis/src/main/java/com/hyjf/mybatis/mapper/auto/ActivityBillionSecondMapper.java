package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityBillionSecondMapper {
    int countByExample(ActivityBillionSecondExample example);

    int deleteByExample(ActivityBillionSecondExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityBillionSecond record);

    int insertSelective(ActivityBillionSecond record);

    List<ActivityBillionSecond> selectByExample(ActivityBillionSecondExample example);

    ActivityBillionSecond selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityBillionSecond record, @Param("example") ActivityBillionSecondExample example);

    int updateByExample(@Param("record") ActivityBillionSecond record, @Param("example") ActivityBillionSecondExample example);

    int updateByPrimaryKeySelective(ActivityBillionSecond record);

    int updateByPrimaryKey(ActivityBillionSecond record);
}