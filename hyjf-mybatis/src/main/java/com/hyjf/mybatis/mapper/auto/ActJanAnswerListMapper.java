package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanAnswerListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActJanAnswerListMapper {
    int countByExample(ActJanAnswerListExample example);

    int deleteByExample(ActJanAnswerListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActJanAnswerList record);

    int insertSelective(ActJanAnswerList record);

    List<ActJanAnswerList> selectByExample(ActJanAnswerListExample example);

    ActJanAnswerList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActJanAnswerList record, @Param("example") ActJanAnswerListExample example);

    int updateByExample(@Param("record") ActJanAnswerList record, @Param("example") ActJanAnswerListExample example);

    int updateByPrimaryKeySelective(ActJanAnswerList record);

    int updateByPrimaryKey(ActJanAnswerList record);
}