package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SpreadsUsersLog;
import com.hyjf.mybatis.model.auto.SpreadsUsersLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpreadsUsersLogMapper {
    int countByExample(SpreadsUsersLogExample example);

    int deleteByExample(SpreadsUsersLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SpreadsUsersLog record);

    int insertSelective(SpreadsUsersLog record);

    List<SpreadsUsersLog> selectByExample(SpreadsUsersLogExample example);

    SpreadsUsersLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SpreadsUsersLog record, @Param("example") SpreadsUsersLogExample example);

    int updateByExample(@Param("record") SpreadsUsersLog record, @Param("example") SpreadsUsersLogExample example);

    int updateByPrimaryKeySelective(SpreadsUsersLog record);

    int updateByPrimaryKey(SpreadsUsersLog record);
}