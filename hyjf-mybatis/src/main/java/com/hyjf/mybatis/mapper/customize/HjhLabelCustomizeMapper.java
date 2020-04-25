package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhLabelExample;
import com.hyjf.mybatis.model.customize.HjhLabelCustomize;
import com.hyjf.mybatis.model.customize.HjhLabelCustomizeExample;

public interface HjhLabelCustomizeMapper {
    int countByExample(HjhLabelExample example);

    int deleteByExample(HjhLabelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhLabel record);

    int insertSelective(HjhLabel record);

    List<HjhLabelCustomize> selectByExample(HjhLabelCustomizeExample example);

    HjhLabelCustomize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhLabel record, @Param("example") HjhLabelExample example);

    int updateByExample(@Param("record") HjhLabel record, @Param("example") HjhLabelExample example);

    int updateByPrimaryKeySelective(@Param("record") HjhLabel record);

    int updateByPrimaryKey(@Param("record") HjhLabel record);
}