package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspNormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspNormalCreditDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspNormalCreditDetailMapper {
    int countByExample(MspNormalCreditDetailExample example);

    int deleteByExample(MspNormalCreditDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspNormalCreditDetail record);

    int insertSelective(MspNormalCreditDetail record);

    List<MspNormalCreditDetail> selectByExample(MspNormalCreditDetailExample example);

    MspNormalCreditDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspNormalCreditDetail record, @Param("example") MspNormalCreditDetailExample example);

    int updateByExample(@Param("record") MspNormalCreditDetail record, @Param("example") MspNormalCreditDetailExample example);

    int updateByPrimaryKeySelective(MspNormalCreditDetail record);

    int updateByPrimaryKey(MspNormalCreditDetail record);
}