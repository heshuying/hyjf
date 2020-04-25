package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspZhixingInfos;
import com.hyjf.mybatis.model.auto.MspZhixingInfosExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspZhixingInfosMapper {
    int countByExample(MspZhixingInfosExample example);

    int deleteByExample(MspZhixingInfosExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspZhixingInfos record);

    int insertSelective(MspZhixingInfos record);

    List<MspZhixingInfos> selectByExample(MspZhixingInfosExample example);

    MspZhixingInfos selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspZhixingInfos record, @Param("example") MspZhixingInfosExample example);

    int updateByExample(@Param("record") MspZhixingInfos record, @Param("example") MspZhixingInfosExample example);

    int updateByPrimaryKeySelective(MspZhixingInfos record);

    int updateByPrimaryKey(MspZhixingInfos record);
}