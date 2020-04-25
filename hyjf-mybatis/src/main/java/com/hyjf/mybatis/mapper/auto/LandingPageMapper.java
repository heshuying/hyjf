package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.LandingPage;
import com.hyjf.mybatis.model.auto.LandingPageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LandingPageMapper {
    int countByExample(LandingPageExample example);

    int deleteByExample(LandingPageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LandingPage record);

    int insertSelective(LandingPage record);

    List<LandingPage> selectByExample(LandingPageExample example);

    LandingPage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LandingPage record, @Param("example") LandingPageExample example);

    int updateByExample(@Param("record") LandingPage record, @Param("example") LandingPageExample example);

    int updateByPrimaryKeySelective(LandingPage record);

    int updateByPrimaryKey(LandingPage record);
}