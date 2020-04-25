package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SubCommissionListConfig;
import com.hyjf.mybatis.model.auto.SubCommissionListConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SubCommissionListConfigMapper {
    int countByExample(SubCommissionListConfigExample example);

    int deleteByExample(SubCommissionListConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SubCommissionListConfig record);

    int insertSelective(SubCommissionListConfig record);

    List<SubCommissionListConfig> selectByExample(SubCommissionListConfigExample example);

    SubCommissionListConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SubCommissionListConfig record, @Param("example") SubCommissionListConfigExample example);

    int updateByExample(@Param("record") SubCommissionListConfig record, @Param("example") SubCommissionListConfigExample example);

    int updateByPrimaryKeySelective(SubCommissionListConfig record);

    int updateByPrimaryKey(SubCommissionListConfig record);
}