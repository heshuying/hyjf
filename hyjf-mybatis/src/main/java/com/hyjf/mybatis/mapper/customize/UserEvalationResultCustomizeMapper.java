package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserEvalationResultCustomizeMapper {
    int countByExample(UserEvalationResultExampleCustomize example);

    int deleteByExample(UserEvalationResultExampleCustomize example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserEvalationResultCustomize record);

    int insertSelective(UserEvalationResultCustomize record);

    List<UserEvalationResultCustomize> selectByExample(UserEvalationResultExampleCustomize example);

    UserEvalationResultCustomize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserEvalationResultCustomize record, @Param("example") UserEvalationResultExampleCustomize example);

    int updateByExample(@Param("record") UserEvalationResultCustomize record, @Param("example") UserEvalationResultExampleCustomize example);

    int updateByPrimaryKeySelective(UserEvalationResultCustomize record);

    int updateByPrimaryKey(UserEvalationResultCustomize record);
}