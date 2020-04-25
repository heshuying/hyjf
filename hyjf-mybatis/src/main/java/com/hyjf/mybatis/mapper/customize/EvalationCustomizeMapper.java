package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.EvalationExampleCustomize;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EvalationCustomizeMapper {
    int countByExample(EvalationExampleCustomize example);

    int deleteByExample(EvalationExampleCustomize example);

    int deleteByPrimaryKey(Integer id);

    int insert(EvalationCustomize record);

    int insertSelective(EvalationCustomize record);

    List<EvalationCustomize> selectByExample(EvalationExampleCustomize example);

    EvalationCustomize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EvalationCustomize record, @Param("example") EvalationExampleCustomize example);

    int updateByExample(@Param("record") EvalationCustomize record, @Param("example") EvalationExampleCustomize example);

    int updateByPrimaryKeySelective(EvalationCustomize record);

    int updateByPrimaryKey(EvalationCustomize record);
}