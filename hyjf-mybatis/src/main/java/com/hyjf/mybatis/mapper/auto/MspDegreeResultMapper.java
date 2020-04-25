package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspDegreeResult;
import com.hyjf.mybatis.model.auto.MspDegreeResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspDegreeResultMapper {
    int countByExample(MspDegreeResultExample example);

    int deleteByExample(MspDegreeResultExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspDegreeResult record);

    int insertSelective(MspDegreeResult record);

    List<MspDegreeResult> selectByExampleWithBLOBs(MspDegreeResultExample example);

    List<MspDegreeResult> selectByExample(MspDegreeResultExample example);

    MspDegreeResult selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspDegreeResult record, @Param("example") MspDegreeResultExample example);

    int updateByExampleWithBLOBs(@Param("record") MspDegreeResult record, @Param("example") MspDegreeResultExample example);

    int updateByExample(@Param("record") MspDegreeResult record, @Param("example") MspDegreeResultExample example);

    int updateByPrimaryKeySelective(MspDegreeResult record);

    int updateByPrimaryKeyWithBLOBs(MspDegreeResult record);

    int updateByPrimaryKey(MspDegreeResult record);
}