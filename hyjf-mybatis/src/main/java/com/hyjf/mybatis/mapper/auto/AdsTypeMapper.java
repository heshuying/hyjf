package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.mybatis.model.auto.AdsTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdsTypeMapper {
    int countByExample(AdsTypeExample example);

    int deleteByExample(AdsTypeExample example);

    int deleteByPrimaryKey(Integer typeid);

    int insert(AdsType record);

    int insertSelective(AdsType record);

    List<AdsType> selectByExample(AdsTypeExample example);

    AdsType selectByPrimaryKey(Integer typeid);

    int updateByExampleSelective(@Param("record") AdsType record, @Param("example") AdsTypeExample example);

    int updateByExample(@Param("record") AdsType record, @Param("example") AdsTypeExample example);

    int updateByPrimaryKeySelective(AdsType record);

    int updateByPrimaryKey(AdsType record);
}