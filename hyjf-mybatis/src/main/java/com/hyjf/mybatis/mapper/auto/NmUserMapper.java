package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NmUser;
import com.hyjf.mybatis.model.auto.NmUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NmUserMapper {
    int countByExample(NmUserExample example);

    int deleteByExample(NmUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NmUser record);

    int insertSelective(NmUser record);

    List<NmUser> selectByExample(NmUserExample example);

    NmUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NmUser record, @Param("example") NmUserExample example);

    int updateByExample(@Param("record") NmUser record, @Param("example") NmUserExample example);

    int updateByPrimaryKeySelective(NmUser record);

    int updateByPrimaryKey(NmUser record);
}