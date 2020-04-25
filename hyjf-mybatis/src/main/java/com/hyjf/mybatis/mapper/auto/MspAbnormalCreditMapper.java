package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspAbnormalCredit;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspAbnormalCreditMapper {
    int countByExample(MspAbnormalCreditExample example);

    int deleteByExample(MspAbnormalCreditExample example);

    int deleteByPrimaryKey(String id);

    int insert(MspAbnormalCredit record);

    int insertSelective(MspAbnormalCredit record);

    List<MspAbnormalCredit> selectByExample(MspAbnormalCreditExample example);

    MspAbnormalCredit selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MspAbnormalCredit record, @Param("example") MspAbnormalCreditExample example);

    int updateByExample(@Param("record") MspAbnormalCredit record, @Param("example") MspAbnormalCreditExample example);

    int updateByPrimaryKeySelective(MspAbnormalCredit record);

    int updateByPrimaryKey(MspAbnormalCredit record);
}