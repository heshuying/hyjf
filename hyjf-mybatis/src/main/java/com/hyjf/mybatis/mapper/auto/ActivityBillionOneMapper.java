package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityBillionOne;
import com.hyjf.mybatis.model.auto.ActivityBillionOneExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityBillionOneMapper {
    int countByExample(ActivityBillionOneExample example);

    int deleteByExample(ActivityBillionOneExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityBillionOne record);

    int insertSelective(ActivityBillionOne record);

    List<ActivityBillionOne> selectByExample(ActivityBillionOneExample example);

    ActivityBillionOne selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityBillionOne record, @Param("example") ActivityBillionOneExample example);

    int updateByExample(@Param("record") ActivityBillionOne record, @Param("example") ActivityBillionOneExample example);

    int updateByPrimaryKeySelective(ActivityBillionOne record);

    int updateByPrimaryKey(ActivityBillionOne record);
}