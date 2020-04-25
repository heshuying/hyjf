package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UtmPlatMapper {
    int countByExample(UtmPlatExample example);

    int deleteByExample(UtmPlatExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UtmPlat record);

    int insertSelective(UtmPlat record);

    List<UtmPlat> selectByExample(UtmPlatExample example);

    UtmPlat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UtmPlat record, @Param("example") UtmPlatExample example);

    int updateByExample(@Param("record") UtmPlat record, @Param("example") UtmPlatExample example);

    int updateByPrimaryKeySelective(UtmPlat record);

    int updateByPrimaryKey(UtmPlat record);
}