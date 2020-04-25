package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecListedThree;
import com.hyjf.mybatis.model.auto.ActdecListedThreeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecListedThreeMapper {
    int countByExample(ActdecListedThreeExample example);

    int deleteByExample(ActdecListedThreeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecListedThree record);

    int insertSelective(ActdecListedThree record);

    List<ActdecListedThree> selectByExample(ActdecListedThreeExample example);

    ActdecListedThree selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecListedThree record, @Param("example") ActdecListedThreeExample example);

    int updateByExample(@Param("record") ActdecListedThree record, @Param("example") ActdecListedThreeExample example);

    int updateByPrimaryKeySelective(ActdecListedThree record);

    int updateByPrimaryKey(ActdecListedThree record);
}