package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantity;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearCaishenCardQuantityMapper {
    int countByExample(NewyearCaishenCardQuantityExample example);

    int deleteByExample(NewyearCaishenCardQuantityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearCaishenCardQuantity record);

    int insertSelective(NewyearCaishenCardQuantity record);

    List<NewyearCaishenCardQuantity> selectByExample(NewyearCaishenCardQuantityExample example);

    NewyearCaishenCardQuantity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearCaishenCardQuantity record, @Param("example") NewyearCaishenCardQuantityExample example);

    int updateByExample(@Param("record") NewyearCaishenCardQuantity record, @Param("example") NewyearCaishenCardQuantityExample example);

    int updateByPrimaryKeySelective(NewyearCaishenCardQuantity record);

    int updateByPrimaryKey(NewyearCaishenCardQuantity record);
}