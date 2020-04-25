package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Idfa;
import com.hyjf.mybatis.model.auto.IdfaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdfaMapper {
    int countByExample(IdfaExample example);

    int deleteByExample(IdfaExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Idfa record);

    int insertSelective(Idfa record);

    List<Idfa> selectByExample(IdfaExample example);

    Idfa selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Idfa record, @Param("example") IdfaExample example);

    int updateByExample(@Param("record") Idfa record, @Param("example") IdfaExample example);

    int updateByPrimaryKeySelective(Idfa record);

    int updateByPrimaryKey(Idfa record);
}