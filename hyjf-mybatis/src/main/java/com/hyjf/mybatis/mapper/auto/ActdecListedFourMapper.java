package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecListedFour;
import com.hyjf.mybatis.model.auto.ActdecListedFourExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecListedFourMapper {
    int countByExample(ActdecListedFourExample example);

    int deleteByExample(ActdecListedFourExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecListedFour record);

    int insertSelective(ActdecListedFour record);

    List<ActdecListedFour> selectByExample(ActdecListedFourExample example);

    ActdecListedFour selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecListedFour record, @Param("example") ActdecListedFourExample example);

    int updateByExample(@Param("record") ActdecListedFour record, @Param("example") ActdecListedFourExample example);

    int updateByPrimaryKeySelective(ActdecListedFour record);

    int updateByPrimaryKey(ActdecListedFour record);
}