package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UserDeviceUniqueCode;
import com.hyjf.mybatis.model.auto.UserDeviceUniqueCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserDeviceUniqueCodeMapper {
    int countByExample(UserDeviceUniqueCodeExample example);

    int deleteByExample(UserDeviceUniqueCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserDeviceUniqueCode record);

    int insertSelective(UserDeviceUniqueCode record);

    List<UserDeviceUniqueCode> selectByExample(UserDeviceUniqueCodeExample example);

    UserDeviceUniqueCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserDeviceUniqueCode record, @Param("example") UserDeviceUniqueCodeExample example);

    int updateByExample(@Param("record") UserDeviceUniqueCode record, @Param("example") UserDeviceUniqueCodeExample example);

    int updateByPrimaryKeySelective(UserDeviceUniqueCode record);

    int updateByPrimaryKey(UserDeviceUniqueCode record);
}