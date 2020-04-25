package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.IdCardToArea;
import com.hyjf.mybatis.model.auto.IdCardToAreaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdCardToAreaMapper {
    int countByExample(IdCardToAreaExample example);

    int deleteByExample(IdCardToAreaExample example);

    int deleteByPrimaryKey(Short id);

    int insert(IdCardToArea record);

    int insertSelective(IdCardToArea record);

    List<IdCardToArea> selectByExample(IdCardToAreaExample example);

    IdCardToArea selectByPrimaryKey(Short id);

    int updateByExampleSelective(@Param("record") IdCardToArea record, @Param("example") IdCardToAreaExample example);

    int updateByExample(@Param("record") IdCardToArea record, @Param("example") IdCardToAreaExample example);

    int updateByPrimaryKeySelective(IdCardToArea record);

    int updateByPrimaryKey(IdCardToArea record);
}