package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecCorpsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecCorpsMapper {
    int countByExample(ActdecCorpsExample example);

    int deleteByExample(ActdecCorpsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecCorps record);

    int insertSelective(ActdecCorps record);

    List<ActdecCorps> selectByExample(ActdecCorpsExample example);

    ActdecCorps selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecCorps record, @Param("example") ActdecCorpsExample example);

    int updateByExample(@Param("record") ActdecCorps record, @Param("example") ActdecCorpsExample example);

    int updateByPrimaryKeySelective(ActdecCorps record);

    int updateByPrimaryKey(ActdecCorps record);
}