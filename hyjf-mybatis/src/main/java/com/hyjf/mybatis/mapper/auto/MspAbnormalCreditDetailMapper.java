package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspAbnormalCreditDetailMapper {
    int countByExample(MspAbnormalCreditDetailExample example);

    int deleteByExample(MspAbnormalCreditDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspAbnormalCreditDetail record);

    int insertSelective(MspAbnormalCreditDetail record);

    List<MspAbnormalCreditDetail> selectByExample(MspAbnormalCreditDetailExample example);

    MspAbnormalCreditDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspAbnormalCreditDetail record, @Param("example") MspAbnormalCreditDetailExample example);

    int updateByExample(@Param("record") MspAbnormalCreditDetail record, @Param("example") MspAbnormalCreditDetailExample example);

    int updateByPrimaryKeySelective(MspAbnormalCreditDetail record);

    int updateByPrimaryKey(MspAbnormalCreditDetail record);
}