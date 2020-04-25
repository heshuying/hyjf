package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdminRole;
import com.hyjf.mybatis.model.auto.AdminRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminRoleMapper {
    int countByExample(AdminRoleExample example);

    int deleteByExample(AdminRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AdminRole record);

    int insertSelective(AdminRole record);

    List<AdminRole> selectByExampleWithBLOBs(AdminRoleExample example);

    List<AdminRole> selectByExample(AdminRoleExample example);

    AdminRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AdminRole record, @Param("example") AdminRoleExample example);

    int updateByExampleWithBLOBs(@Param("record") AdminRole record, @Param("example") AdminRoleExample example);

    int updateByExample(@Param("record") AdminRole record, @Param("example") AdminRoleExample example);

    int updateByPrimaryKeySelective(AdminRole record);

    int updateByPrimaryKeyWithBLOBs(AdminRole record);

    int updateByPrimaryKey(AdminRole record);
}