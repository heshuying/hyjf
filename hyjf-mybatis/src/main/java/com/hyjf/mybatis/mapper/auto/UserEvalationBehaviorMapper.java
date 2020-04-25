package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UserEvalationBehavior;
import com.hyjf.mybatis.model.auto.UserEvalationBehaviorExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserEvalationBehaviorMapper {
    int countByExample(UserEvalationBehaviorExample example);

    int deleteByExample(UserEvalationBehaviorExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserEvalationBehavior record);

    int insertSelective(UserEvalationBehavior record);

    List<UserEvalationBehavior> selectByExample(UserEvalationBehaviorExample example);

    UserEvalationBehavior selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserEvalationBehavior record, @Param("example") UserEvalationBehaviorExample example);

    int updateByExample(@Param("record") UserEvalationBehavior record, @Param("example") UserEvalationBehaviorExample example);

    int updateByPrimaryKeySelective(UserEvalationBehavior record);

    int updateByPrimaryKey(UserEvalationBehavior record);
}