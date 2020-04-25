package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspShixinInfos;
import com.hyjf.mybatis.model.auto.MspShixinInfosExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspShixinInfosMapper {
    int countByExample(MspShixinInfosExample example);

    int deleteByExample(MspShixinInfosExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspShixinInfos record);

    int insertSelective(MspShixinInfos record);

    List<MspShixinInfos> selectByExample(MspShixinInfosExample example);

    MspShixinInfos selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspShixinInfos record, @Param("example") MspShixinInfosExample example);

    int updateByExample(@Param("record") MspShixinInfos record, @Param("example") MspShixinInfosExample example);

    int updateByPrimaryKeySelective(MspShixinInfos record);

    int updateByPrimaryKey(MspShixinInfos record);
}