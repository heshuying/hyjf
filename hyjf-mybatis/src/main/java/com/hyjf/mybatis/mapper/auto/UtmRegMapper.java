package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UtmRegMapper {
    int countByExample(UtmRegExample example);

    int deleteByExample(UtmRegExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UtmReg record);

    int insertSelective(UtmReg record);

    List<UtmReg> selectByExample(UtmRegExample example);

    UtmReg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UtmReg record, @Param("example") UtmRegExample example);

    int updateByExample(@Param("record") UtmReg record, @Param("example") UtmRegExample example);

    int updateByPrimaryKeySelective(UtmReg record);

    int updateByPrimaryKey(UtmReg record);
}