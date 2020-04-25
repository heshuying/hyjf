package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AdminMenu;
import com.hyjf.mybatis.model.auto.AdminMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMenuMapper {
    int countByExample(AdminMenuExample example);

    int deleteByExample(AdminMenuExample example);

    int deleteByPrimaryKey(String menuUuid);

    int insert(AdminMenu record);

    int insertSelective(AdminMenu record);

    List<AdminMenu> selectByExample(AdminMenuExample example);

    AdminMenu selectByPrimaryKey(String menuUuid);

    int updateByExampleSelective(@Param("record") AdminMenu record, @Param("example") AdminMenuExample example);

    int updateByExample(@Param("record") AdminMenu record, @Param("example") AdminMenuExample example);

    int updateByPrimaryKeySelective(AdminMenu record);

    int updateByPrimaryKey(AdminMenu record);
}