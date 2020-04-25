package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecListedOne;
import com.hyjf.mybatis.model.auto.ActdecListedOneExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecListedOneMapper {
    int countByExample(ActdecListedOneExample example);

    int deleteByExample(ActdecListedOneExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecListedOne record);

    int insertSelective(ActdecListedOne record);

    List<ActdecListedOne> selectByExample(ActdecListedOneExample example);

    ActdecListedOne selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecListedOne record, @Param("example") ActdecListedOneExample example);

    int updateByExample(@Param("record") ActdecListedOne record, @Param("example") ActdecListedOneExample example);

    int updateByPrimaryKeySelective(ActdecListedOne record);

    int updateByPrimaryKey(ActdecListedOne record);
}