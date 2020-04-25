package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomize;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomizeExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface STZHWhiteListCustomizeMapper {
    int countByExample(STZHWhiteListCustomizeExample example);

    int deleteByExample(STZHWhiteListCustomizeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(STZHWhiteListCustomize record);

    int insertSelective(STZHWhiteListCustomize record);

    List<STZHWhiteListCustomize> selectByExample(STZHWhiteListCustomizeExample example);

    STZHWhiteListCustomize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") STZHWhiteListCustomize record, @Param("example") STZHWhiteListCustomizeExample example);

    int updateByExample(@Param("record") STZHWhiteListCustomize record, @Param("example") STZHWhiteListCustomizeExample example);

    int updateByPrimaryKeySelective(STZHWhiteListCustomize record);

    int updateByPrimaryKey(STZHWhiteListCustomize record);
    
    STZHWhiteListCustomize selectByObject(STZHWhiteListCustomize sTZHWhiteList);
    
    HjhInstConfig getRegionName(HjhInstConfig hjhInstConfig);
    
}