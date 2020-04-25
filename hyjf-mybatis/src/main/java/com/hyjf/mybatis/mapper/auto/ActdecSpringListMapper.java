package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecSpringList;
import com.hyjf.mybatis.model.auto.ActdecSpringListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecSpringListMapper {
    int countByExample(ActdecSpringListExample example);

    int deleteByExample(ActdecSpringListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecSpringList record);

    int insertSelective(ActdecSpringList record);

    List<ActdecSpringList> selectByExample(ActdecSpringListExample example);

    ActdecSpringList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecSpringList record, @Param("example") ActdecSpringListExample example);

    int updateByExample(@Param("record") ActdecSpringList record, @Param("example") ActdecSpringListExample example);

    int updateByPrimaryKeySelective(ActdecSpringList record);

    int updateByPrimaryKey(ActdecSpringList record);
}