package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspAnliInfos;
import com.hyjf.mybatis.model.auto.MspAnliInfosExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspAnliInfosMapper {
    int countByExample(MspAnliInfosExample example);

    int deleteByExample(MspAnliInfosExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspAnliInfos record);

    int insertSelective(MspAnliInfos record);

    List<MspAnliInfos> selectByExample(MspAnliInfosExample example);

    MspAnliInfos selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspAnliInfos record, @Param("example") MspAnliInfosExample example);

    int updateByExample(@Param("record") MspAnliInfos record, @Param("example") MspAnliInfosExample example);

    int updateByPrimaryKeySelective(MspAnliInfos record);

    int updateByPrimaryKey(MspAnliInfos record);
}