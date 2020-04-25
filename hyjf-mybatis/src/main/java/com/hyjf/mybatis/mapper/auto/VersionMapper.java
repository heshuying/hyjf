package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Version;
import com.hyjf.mybatis.model.auto.VersionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VersionMapper {
    int countByExample(VersionExample example);

    int deleteByExample(VersionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Version record);

    int insertSelective(Version record);

    List<Version> selectByExample(VersionExample example);

    Version selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Version record, @Param("example") VersionExample example);

    int updateByExample(@Param("record") Version record, @Param("example") VersionExample example);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);
}