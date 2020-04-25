package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityBillionThird;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityBillionThirdMapper {
    int countByExample(ActivityBillionThirdExample example);

    int deleteByExample(ActivityBillionThirdExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityBillionThird record);

    int insertSelective(ActivityBillionThird record);

    List<ActivityBillionThird> selectByExample(ActivityBillionThirdExample example);

    ActivityBillionThird selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityBillionThird record, @Param("example") ActivityBillionThirdExample example);

    int updateByExample(@Param("record") ActivityBillionThird record, @Param("example") ActivityBillionThirdExample example);

    int updateByPrimaryKeySelective(ActivityBillionThird record);

    int updateByPrimaryKey(ActivityBillionThird record);
}