package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.ActSigninExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActSigninMapper {
    int countByExample(ActSigninExample example);

    int deleteByExample(ActSigninExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActSignin record);

    int insertSelective(ActSignin record);

    List<ActSignin> selectByExample(ActSigninExample example);

    ActSignin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActSignin record, @Param("example") ActSigninExample example);

    int updateByExample(@Param("record") ActSignin record, @Param("example") ActSigninExample example);

    int updateByPrimaryKeySelective(ActSignin record);

    int updateByPrimaryKey(ActSignin record);
}