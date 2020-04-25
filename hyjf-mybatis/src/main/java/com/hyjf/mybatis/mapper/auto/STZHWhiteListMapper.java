package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.STZHWhiteList;
import com.hyjf.mybatis.model.auto.STZHWhiteListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface STZHWhiteListMapper {
    int countByExample(STZHWhiteListExample example);

    int deleteByExample(STZHWhiteListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(STZHWhiteList record);

    int insertSelective(STZHWhiteList record);

    List<STZHWhiteList> selectByExample(STZHWhiteListExample example);

    STZHWhiteList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") STZHWhiteList record, @Param("example") STZHWhiteListExample example);

    int updateByExample(@Param("record") STZHWhiteList record, @Param("example") STZHWhiteListExample example);

    int updateByPrimaryKeySelective(STZHWhiteList record);

    int updateByPrimaryKey(STZHWhiteList record);
}