package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspApply;
import com.hyjf.mybatis.model.auto.MspApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspApplyMapper {
    int countByExample(MspApplyExample example);

    int deleteByExample(MspApplyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspApply record);

    int insertSelective(MspApply record);

    List<MspApply> selectByExample(MspApplyExample example);

    MspApply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspApply record, @Param("example") MspApplyExample example);

    int updateByExample(@Param("record") MspApply record, @Param("example") MspApplyExample example);

    int updateByPrimaryKeySelective(MspApply record);

    int updateByPrimaryKey(MspApply record);
}