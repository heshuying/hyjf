package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtPlanInfoStatic;
import com.hyjf.mybatis.model.auto.DebtPlanInfoStaticExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtPlanInfoStaticMapper {
    int countByExample(DebtPlanInfoStaticExample example);

    int deleteByExample(DebtPlanInfoStaticExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtPlanInfoStatic record);

    int insertSelective(DebtPlanInfoStatic record);

    List<DebtPlanInfoStatic> selectByExample(DebtPlanInfoStaticExample example);

    DebtPlanInfoStatic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtPlanInfoStatic record, @Param("example") DebtPlanInfoStaticExample example);

    int updateByExample(@Param("record") DebtPlanInfoStatic record, @Param("example") DebtPlanInfoStaticExample example);

    int updateByPrimaryKeySelective(DebtPlanInfoStatic record);

    int updateByPrimaryKey(DebtPlanInfoStatic record);
}