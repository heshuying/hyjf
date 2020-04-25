package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UsersContract;
import com.hyjf.mybatis.model.auto.UsersContractExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsersContractMapper {
    int countByExample(UsersContractExample example);

    int deleteByExample(UsersContractExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UsersContract record);

    int insertSelective(UsersContract record);

    List<UsersContract> selectByExample(UsersContractExample example);

    UsersContract selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UsersContract record, @Param("example") UsersContractExample example);

    int updateByExample(@Param("record") UsersContract record, @Param("example") UsersContractExample example);

    int updateByPrimaryKeySelective(UsersContract record);

    int updateByPrimaryKey(UsersContract record);
}