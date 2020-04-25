package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.TenderMonthInfo;
import com.hyjf.mybatis.model.auto.TenderMonthInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TenderMonthInfoMapper {
    int countByExample(TenderMonthInfoExample example);

    int deleteByExample(TenderMonthInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TenderMonthInfo record);

    int insertSelective(TenderMonthInfo record);

    List<TenderMonthInfo> selectByExample(TenderMonthInfoExample example);

    TenderMonthInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TenderMonthInfo record, @Param("example") TenderMonthInfoExample example);

    int updateByExample(@Param("record") TenderMonthInfo record, @Param("example") TenderMonthInfoExample example);

    int updateByPrimaryKeySelective(TenderMonthInfo record);

    int updateByPrimaryKey(TenderMonthInfo record);
}