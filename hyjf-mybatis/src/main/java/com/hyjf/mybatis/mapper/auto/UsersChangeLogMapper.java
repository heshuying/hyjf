package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.auto.UsersChangeLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsersChangeLogMapper {
    int countByExample(UsersChangeLogExample example);

    int deleteByExample(UsersChangeLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UsersChangeLog record);

    int insertSelective(UsersChangeLog record);

    List<UsersChangeLog> selectByExample(UsersChangeLogExample example);

    UsersChangeLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UsersChangeLog record, @Param("example") UsersChangeLogExample example);

    int updateByExample(@Param("record") UsersChangeLog record, @Param("example") UsersChangeLogExample example);

    int updateByPrimaryKeySelective(UsersChangeLog record);

    int updateByPrimaryKey(UsersChangeLog record);
}