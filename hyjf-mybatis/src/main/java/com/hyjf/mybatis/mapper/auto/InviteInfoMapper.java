package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.InviteInfo;
import com.hyjf.mybatis.model.auto.InviteInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InviteInfoMapper {
    int countByExample(InviteInfoExample example);

    int deleteByExample(InviteInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(InviteInfo record);

    int insertSelective(InviteInfo record);

    List<InviteInfo> selectByExample(InviteInfoExample example);

    InviteInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") InviteInfo record, @Param("example") InviteInfoExample example);

    int updateByExample(@Param("record") InviteInfo record, @Param("example") InviteInfoExample example);

    int updateByPrimaryKeySelective(InviteInfo record);

    int updateByPrimaryKey(InviteInfo record);
}