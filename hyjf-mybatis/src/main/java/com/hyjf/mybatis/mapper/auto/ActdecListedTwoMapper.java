package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecListedTwo;
import com.hyjf.mybatis.model.auto.ActdecListedTwoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecListedTwoMapper {
    int countByExample(ActdecListedTwoExample example);

    int deleteByExample(ActdecListedTwoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecListedTwo record);

    int insertSelective(ActdecListedTwo record);

    List<ActdecListedTwo> selectByExample(ActdecListedTwoExample example);

    ActdecListedTwo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecListedTwo record, @Param("example") ActdecListedTwoExample example);

    int updateByExample(@Param("record") ActdecListedTwo record, @Param("example") ActdecListedTwoExample example);

    int updateByPrimaryKeySelective(ActdecListedTwo record);

    int updateByPrimaryKey(ActdecListedTwo record);
}