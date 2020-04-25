package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtPlanMapper {
    int countByExample(DebtPlanExample example);

    int deleteByExample(DebtPlanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtPlanWithBLOBs record);

    int insertSelective(DebtPlanWithBLOBs record);

    List<DebtPlanWithBLOBs> selectByExampleWithBLOBs(DebtPlanExample example);

    List<DebtPlan> selectByExample(DebtPlanExample example);

    DebtPlanWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtPlanWithBLOBs record, @Param("example") DebtPlanExample example);

    int updateByExampleWithBLOBs(@Param("record") DebtPlanWithBLOBs record, @Param("example") DebtPlanExample example);

    int updateByExample(@Param("record") DebtPlan record, @Param("example") DebtPlanExample example);

    int updateByPrimaryKeySelective(DebtPlanWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(DebtPlanWithBLOBs record);

    int updateByPrimaryKey(DebtPlan record);
}