package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Links;
import com.hyjf.mybatis.model.auto.LinksExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LinksMapper {
    int countByExample(LinksExample example);

    int deleteByExample(LinksExample example);

    int deleteByPrimaryKey(Short id);

    int insert(Links record);

    int insertSelective(Links record);

    List<Links> selectByExample(LinksExample example);

    Links selectByPrimaryKey(Short id);

    int updateByExampleSelective(@Param("record") Links record, @Param("example") LinksExample example);

    int updateByExample(@Param("record") Links record, @Param("example") LinksExample example);

    int updateByPrimaryKeySelective(Links record);

    int updateByPrimaryKey(Links record);
}