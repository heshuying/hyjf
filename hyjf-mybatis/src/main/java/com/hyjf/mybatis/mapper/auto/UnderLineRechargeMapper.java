package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UnderLineRecharge;
import com.hyjf.mybatis.model.auto.UnderLineRechargeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UnderLineRechargeMapper {
    int countByExample(UnderLineRechargeExample example);

    int deleteByExample(UnderLineRechargeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UnderLineRecharge record);

    int insertSelective(UnderLineRecharge record);

    List<UnderLineRecharge> selectByExample(UnderLineRechargeExample example);

    UnderLineRecharge selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UnderLineRecharge record, @Param("example") UnderLineRechargeExample example);

    int updateByExample(@Param("record") UnderLineRecharge record, @Param("example") UnderLineRechargeExample example);

    int updateByPrimaryKeySelective(UnderLineRecharge record);

    int updateByPrimaryKey(UnderLineRecharge record);
}