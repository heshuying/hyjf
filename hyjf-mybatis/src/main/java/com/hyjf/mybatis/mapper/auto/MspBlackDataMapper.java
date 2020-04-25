package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspBlackData;
import com.hyjf.mybatis.model.auto.MspBlackDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspBlackDataMapper {
    int countByExample(MspBlackDataExample example);

    int deleteByExample(MspBlackDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspBlackData record);

    int insertSelective(MspBlackData record);

    List<MspBlackData> selectByExample(MspBlackDataExample example);

    MspBlackData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspBlackData record, @Param("example") MspBlackDataExample example);

    int updateByExample(@Param("record") MspBlackData record, @Param("example") MspBlackDataExample example);

    int updateByPrimaryKeySelective(MspBlackData record);

    int updateByPrimaryKey(MspBlackData record);
}