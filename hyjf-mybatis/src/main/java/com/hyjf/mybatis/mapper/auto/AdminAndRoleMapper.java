package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdminAndRole;
import com.hyjf.mybatis.model.auto.AdminAndRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminAndRoleMapper {
    int countByExample(AdminAndRoleExample example);

    int deleteByExample(AdminAndRoleExample example);

    int insert(AdminAndRole record);

    int insertSelective(AdminAndRole record);

    List<AdminAndRole> selectByExample(AdminAndRoleExample example);

    int updateByExampleSelective(@Param("record") AdminAndRole record, @Param("example") AdminAndRoleExample example);

    int updateByExample(@Param("record") AdminAndRole record, @Param("example") AdminAndRoleExample example);
}