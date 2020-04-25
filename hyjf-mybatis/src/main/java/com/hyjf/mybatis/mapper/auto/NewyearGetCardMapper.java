package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearGetCard;
import com.hyjf.mybatis.model.auto.NewyearGetCardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearGetCardMapper {
    int countByExample(NewyearGetCardExample example);

    int deleteByExample(NewyearGetCardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearGetCard record);

    int insertSelective(NewyearGetCard record);

    List<NewyearGetCard> selectByExample(NewyearGetCardExample example);

    NewyearGetCard selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearGetCard record, @Param("example") NewyearGetCardExample example);

    int updateByExample(@Param("record") NewyearGetCard record, @Param("example") NewyearGetCardExample example);

    int updateByPrimaryKeySelective(NewyearGetCard record);

    int updateByPrimaryKey(NewyearGetCard record);
}