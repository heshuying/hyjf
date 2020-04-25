package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FreezeListMapper {
    int countByExample(FreezeListExample example);

    int deleteByExample(FreezeListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FreezeList record);

    int insertSelective(FreezeList record);

    List<FreezeList> selectByExample(FreezeListExample example);

    FreezeList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FreezeList record, @Param("example") FreezeListExample example);

    int updateByExample(@Param("record") FreezeList record, @Param("example") FreezeListExample example);

    int updateByPrimaryKeySelective(FreezeList record);

    int updateByPrimaryKey(FreezeList record);
}