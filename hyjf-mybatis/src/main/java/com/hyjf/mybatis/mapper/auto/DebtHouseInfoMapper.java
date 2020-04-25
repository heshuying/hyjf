package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtHouseInfo;
import com.hyjf.mybatis.model.auto.DebtHouseInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtHouseInfoMapper {
    int countByExample(DebtHouseInfoExample example);

    int deleteByExample(DebtHouseInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtHouseInfo record);

    int insertSelective(DebtHouseInfo record);

    List<DebtHouseInfo> selectByExample(DebtHouseInfoExample example);

    DebtHouseInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtHouseInfo record, @Param("example") DebtHouseInfoExample example);

    int updateByExample(@Param("record") DebtHouseInfo record, @Param("example") DebtHouseInfoExample example);

    int updateByPrimaryKeySelective(DebtHouseInfo record);

    int updateByPrimaryKey(DebtHouseInfo record);
}