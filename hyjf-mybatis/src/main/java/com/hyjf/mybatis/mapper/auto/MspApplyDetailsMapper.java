package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspApplyDetails;
import com.hyjf.mybatis.model.auto.MspApplyDetailsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspApplyDetailsMapper {
    int countByExample(MspApplyDetailsExample example);

    int deleteByExample(MspApplyDetailsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspApplyDetails record);

    int insertSelective(MspApplyDetails record);

    List<MspApplyDetails> selectByExample(MspApplyDetailsExample example);

    MspApplyDetails selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspApplyDetails record, @Param("example") MspApplyDetailsExample example);

    int updateByExample(@Param("record") MspApplyDetails record, @Param("example") MspApplyDetailsExample example);

    int updateByPrimaryKeySelective(MspApplyDetails record);

    int updateByPrimaryKey(MspApplyDetails record);
}