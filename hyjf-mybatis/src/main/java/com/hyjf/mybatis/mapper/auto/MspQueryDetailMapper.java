package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MspQueryDetail;
import com.hyjf.mybatis.model.auto.MspQueryDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MspQueryDetailMapper {
    int countByExample(MspQueryDetailExample example);

    int deleteByExample(MspQueryDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspQueryDetail record);

    int insertSelective(MspQueryDetail record);

    List<MspQueryDetail> selectByExample(MspQueryDetailExample example);

    MspQueryDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspQueryDetail record, @Param("example") MspQueryDetailExample example);

    int updateByExample(@Param("record") MspQueryDetail record, @Param("example") MspQueryDetailExample example);

    int updateByPrimaryKeySelective(MspQueryDetail record);

    int updateByPrimaryKey(MspQueryDetail record);
}