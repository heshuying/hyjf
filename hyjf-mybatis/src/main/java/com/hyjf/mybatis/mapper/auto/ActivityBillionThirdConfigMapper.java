package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityBillionThirdConfigMapper {
    int countByExample(ActivityBillionThirdConfigExample example);

    int deleteByExample(ActivityBillionThirdConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityBillionThirdConfig record);

    int insertSelective(ActivityBillionThirdConfig record);

    List<ActivityBillionThirdConfig> selectByExample(ActivityBillionThirdConfigExample example);

    ActivityBillionThirdConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityBillionThirdConfig record, @Param("example") ActivityBillionThirdConfigExample example);

    int updateByExample(@Param("record") ActivityBillionThirdConfig record, @Param("example") ActivityBillionThirdConfigExample example);

    int updateByPrimaryKeySelective(ActivityBillionThirdConfig record);

    int updateByPrimaryKey(ActivityBillionThirdConfig record);
}