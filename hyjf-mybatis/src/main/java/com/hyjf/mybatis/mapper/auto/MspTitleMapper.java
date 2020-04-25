package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspTitle;
import com.hyjf.mybatis.model.auto.MspTitleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspTitleMapper {
    int countByExample(MspTitleExample example);

    int deleteByExample(MspTitleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspTitle record);

    int insertSelective(MspTitle record);

    List<MspTitle> selectByExample(MspTitleExample example);

    MspTitle selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspTitle record, @Param("example") MspTitleExample example);

    int updateByExample(@Param("record") MspTitle record, @Param("example") MspTitleExample example);

    int updateByPrimaryKeySelective(MspTitle record);

    int updateByPrimaryKey(MspTitle record);
}