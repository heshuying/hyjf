package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DataErrorSubject;
import com.hyjf.mybatis.model.auto.DataErrorSubjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataErrorSubjectMapper {
    int countByExample(DataErrorSubjectExample example);

    int deleteByExample(DataErrorSubjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DataErrorSubject record);

    int insertSelective(DataErrorSubject record);

    List<DataErrorSubject> selectByExample(DataErrorSubjectExample example);

    DataErrorSubject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DataErrorSubject record, @Param("example") DataErrorSubjectExample example);

    int updateByExample(@Param("record") DataErrorSubject record, @Param("example") DataErrorSubjectExample example);

    int updateByPrimaryKeySelective(DataErrorSubject record);

    int updateByPrimaryKey(DataErrorSubject record);
}