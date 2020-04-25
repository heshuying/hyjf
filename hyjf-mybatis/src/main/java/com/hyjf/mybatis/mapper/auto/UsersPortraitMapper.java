package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.auto.UsersPortraitExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsersPortraitMapper {
    int countByExample(UsersPortraitExample example);

    int deleteByExample(UsersPortraitExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(UsersPortrait record);

    int insertSelective(UsersPortrait record);

    List<UsersPortrait> selectByExample(UsersPortraitExample example);

    UsersPortrait selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") UsersPortrait record, @Param("example") UsersPortraitExample example);

    int updateByExample(@Param("record") UsersPortrait record, @Param("example") UsersPortraitExample example);

    int updateByPrimaryKeySelective(UsersPortrait record);

    int updateByPrimaryKey(UsersPortrait record);
}