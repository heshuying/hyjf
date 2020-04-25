package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdminRoleMenuPermissions;
import com.hyjf.mybatis.model.auto.AdminRoleMenuPermissionsExample;
import com.hyjf.mybatis.model.auto.AdminRoleMenuPermissionsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminRoleMenuPermissionsMapper {
    int countByExample(AdminRoleMenuPermissionsExample example);

    int deleteByExample(AdminRoleMenuPermissionsExample example);

    int deleteByPrimaryKey(AdminRoleMenuPermissionsKey key);

    int insert(AdminRoleMenuPermissions record);

    int insertSelective(AdminRoleMenuPermissions record);

    List<AdminRoleMenuPermissions> selectByExample(AdminRoleMenuPermissionsExample example);

    AdminRoleMenuPermissions selectByPrimaryKey(AdminRoleMenuPermissionsKey key);

    int updateByExampleSelective(@Param("record") AdminRoleMenuPermissions record, @Param("example") AdminRoleMenuPermissionsExample example);

    int updateByExample(@Param("record") AdminRoleMenuPermissions record, @Param("example") AdminRoleMenuPermissionsExample example);

    int updateByPrimaryKeySelective(AdminRoleMenuPermissions record);

    int updateByPrimaryKey(AdminRoleMenuPermissions record);
}