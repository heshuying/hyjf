package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.auto.LoginErrorLockUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoginErrorLockUserMapper {
    int countByExample(LoginErrorLockUserExample example);

    int deleteByExample(LoginErrorLockUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoginErrorLockUser record);

    int insertSelective(LoginErrorLockUser record);

    List<LoginErrorLockUser> selectByExample(LoginErrorLockUserExample example);

    LoginErrorLockUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LoginErrorLockUser record, @Param("example") LoginErrorLockUserExample example);

    int updateByExample(@Param("record") LoginErrorLockUser record, @Param("example") LoginErrorLockUserExample example);

    int updateByPrimaryKeySelective(LoginErrorLockUser record);

    int updateByPrimaryKey(LoginErrorLockUser record);
}