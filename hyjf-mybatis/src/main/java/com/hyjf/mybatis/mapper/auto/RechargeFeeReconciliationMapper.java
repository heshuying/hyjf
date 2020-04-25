package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RechargeFeeReconciliationMapper {
    int countByExample(RechargeFeeReconciliationExample example);

    int deleteByExample(RechargeFeeReconciliationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeFeeReconciliation record);

    int insertSelective(RechargeFeeReconciliation record);

    List<RechargeFeeReconciliation> selectByExample(RechargeFeeReconciliationExample example);

    RechargeFeeReconciliation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RechargeFeeReconciliation record, @Param("example") RechargeFeeReconciliationExample example);

    int updateByExample(@Param("record") RechargeFeeReconciliation record, @Param("example") RechargeFeeReconciliationExample example);

    int updateByPrimaryKeySelective(RechargeFeeReconciliation record);

    int updateByPrimaryKey(RechargeFeeReconciliation record);
}