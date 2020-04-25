package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Submissions;
import com.hyjf.mybatis.model.auto.SubmissionsExample;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SubmissionsMapper {
    int countByExample(SubmissionsExample example);

    int deleteByExample(SubmissionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SubmissionsWithBLOBs record);

    int insertSelective(SubmissionsWithBLOBs record);

    List<SubmissionsWithBLOBs> selectByExampleWithBLOBs(SubmissionsExample example);

    List<Submissions> selectByExample(SubmissionsExample example);

    SubmissionsWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SubmissionsWithBLOBs record, @Param("example") SubmissionsExample example);

    int updateByExampleWithBLOBs(@Param("record") SubmissionsWithBLOBs record, @Param("example") SubmissionsExample example);

    int updateByExample(@Param("record") Submissions record, @Param("example") SubmissionsExample example);

    int updateByPrimaryKeySelective(SubmissionsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SubmissionsWithBLOBs record);

    int updateByPrimaryKey(Submissions record);
}