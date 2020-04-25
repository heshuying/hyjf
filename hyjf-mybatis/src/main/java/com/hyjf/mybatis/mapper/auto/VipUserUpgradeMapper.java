package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.VipUserUpgrade;
import com.hyjf.mybatis.model.auto.VipUserUpgradeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VipUserUpgradeMapper {
    int countByExample(VipUserUpgradeExample example);

    int deleteByExample(VipUserUpgradeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VipUserUpgrade record);

    int insertSelective(VipUserUpgrade record);

    List<VipUserUpgrade> selectByExample(VipUserUpgradeExample example);

    VipUserUpgrade selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VipUserUpgrade record, @Param("example") VipUserUpgradeExample example);

    int updateByExample(@Param("record") VipUserUpgrade record, @Param("example") VipUserUpgradeExample example);

    int updateByPrimaryKeySelective(VipUserUpgrade record);

    int updateByPrimaryKey(VipUserUpgrade record);
}