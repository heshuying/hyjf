package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdminMenuPermissions;
import com.hyjf.mybatis.model.auto.AdminMenuPermissionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMenuPermissionsMapper {
    int countByExample(AdminMenuPermissionsExample example);

    int deleteByExample(AdminMenuPermissionsExample example);

    int insert(AdminMenuPermissions record);

    int insertSelective(AdminMenuPermissions record);

    List<AdminMenuPermissions> selectByExample(AdminMenuPermissionsExample example);

    int updateByExampleSelective(@Param("record") AdminMenuPermissions record, @Param("example") AdminMenuPermissionsExample example);

    int updateByExample(@Param("record") AdminMenuPermissions record, @Param("example") AdminMenuPermissionsExample example);
}