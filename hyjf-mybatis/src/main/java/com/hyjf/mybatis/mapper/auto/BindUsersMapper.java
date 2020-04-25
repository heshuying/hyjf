package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.BindUsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BindUsersMapper {
    int countByExample(BindUsersExample example);

    int deleteByExample(BindUsersExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BindUsers record);

    int insertSelective(BindUsers record);

    List<BindUsers> selectByExample(BindUsersExample example);

    BindUsers selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BindUsers record, @Param("example") BindUsersExample example);

    int updateByExample(@Param("record") BindUsers record, @Param("example") BindUsersExample example);

    int updateByPrimaryKeySelective(BindUsers record);

    int updateByPrimaryKey(BindUsers record);
}