package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.auto.DebtPlanConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtPlanConfigMapper {
    int countByExample(DebtPlanConfigExample example);

    int deleteByExample(DebtPlanConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtPlanConfig record);

    int insertSelective(DebtPlanConfig record);

    List<DebtPlanConfig> selectByExampleWithBLOBs(DebtPlanConfigExample example);

    List<DebtPlanConfig> selectByExample(DebtPlanConfigExample example);

    DebtPlanConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtPlanConfig record, @Param("example") DebtPlanConfigExample example);

    int updateByExampleWithBLOBs(@Param("record") DebtPlanConfig record, @Param("example") DebtPlanConfigExample example);

    int updateByExample(@Param("record") DebtPlanConfig record, @Param("example") DebtPlanConfigExample example);

    int updateByPrimaryKeySelective(DebtPlanConfig record);

    int updateByPrimaryKeyWithBLOBs(DebtPlanConfig record);

    int updateByPrimaryKey(DebtPlanConfig record);
}