package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.VipAuth;
import com.hyjf.mybatis.model.auto.VipAuthExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VipAuthMapper {
    int countByExample(VipAuthExample example);

    int deleteByExample(VipAuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VipAuth record);

    int insertSelective(VipAuth record);

    List<VipAuth> selectByExample(VipAuthExample example);

    VipAuth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VipAuth record, @Param("example") VipAuthExample example);

    int updateByExample(@Param("record") VipAuth record, @Param("example") VipAuthExample example);

    int updateByPrimaryKeySelective(VipAuth record);

    int updateByPrimaryKey(VipAuth record);
}