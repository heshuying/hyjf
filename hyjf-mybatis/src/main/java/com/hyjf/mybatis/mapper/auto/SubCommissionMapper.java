package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SubCommission;
import com.hyjf.mybatis.model.auto.SubCommissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SubCommissionMapper {
    int countByExample(SubCommissionExample example);

    int deleteByExample(SubCommissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SubCommission record);

    int insertSelective(SubCommission record);

    List<SubCommission> selectByExample(SubCommissionExample example);

    SubCommission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SubCommission record, @Param("example") SubCommissionExample example);

    int updateByExample(@Param("record") SubCommission record, @Param("example") SubCommissionExample example);

    int updateByPrimaryKeySelective(SubCommission record);

    int updateByPrimaryKey(SubCommission record);
}