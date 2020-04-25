package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.PoundageDetail;
import com.hyjf.mybatis.model.auto.PoundageDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PoundageDetailMapper {
    int countByExample(PoundageDetailExample example);

    int deleteByExample(PoundageDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PoundageDetail record);

    int insertSelective(PoundageDetail record);

    List<PoundageDetail> selectByExample(PoundageDetailExample example);

    PoundageDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PoundageDetail record, @Param("example") PoundageDetailExample example);

    int updateByExample(@Param("record") PoundageDetail record, @Param("example") PoundageDetailExample example);

    int updateByPrimaryKeySelective(PoundageDetail record);

    int updateByPrimaryKey(PoundageDetail record);
}