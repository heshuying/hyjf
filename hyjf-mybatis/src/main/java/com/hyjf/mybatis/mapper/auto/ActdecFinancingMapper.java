package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecFinancing;
import com.hyjf.mybatis.model.auto.ActdecFinancingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecFinancingMapper {
    int countByExample(ActdecFinancingExample example);

    int deleteByExample(ActdecFinancingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecFinancing record);

    int insertSelective(ActdecFinancing record);

    List<ActdecFinancing> selectByExample(ActdecFinancingExample example);

    ActdecFinancing selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecFinancing record, @Param("example") ActdecFinancingExample example);

    int updateByExample(@Param("record") ActdecFinancing record, @Param("example") ActdecFinancingExample example);

    int updateByPrimaryKeySelective(ActdecFinancing record);

    int updateByPrimaryKey(ActdecFinancing record);
}