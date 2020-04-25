package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdminPermissions;
import com.hyjf.mybatis.model.auto.AdminPermissionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminPermissionsMapper {
    int countByExample(AdminPermissionsExample example);

    int deleteByExample(AdminPermissionsExample example);

    int deleteByPrimaryKey(String permissionUuid);

    int insert(AdminPermissions record);

    int insertSelective(AdminPermissions record);

    List<AdminPermissions> selectByExampleWithBLOBs(AdminPermissionsExample example);

    List<AdminPermissions> selectByExample(AdminPermissionsExample example);

    AdminPermissions selectByPrimaryKey(String permissionUuid);

    int updateByExampleSelective(@Param("record") AdminPermissions record, @Param("example") AdminPermissionsExample example);

    int updateByExampleWithBLOBs(@Param("record") AdminPermissions record, @Param("example") AdminPermissionsExample example);

    int updateByExample(@Param("record") AdminPermissions record, @Param("example") AdminPermissionsExample example);

    int updateByPrimaryKeySelective(AdminPermissions record);

    int updateByPrimaryKeyWithBLOBs(AdminPermissions record);

    int updateByPrimaryKey(AdminPermissions record);
}