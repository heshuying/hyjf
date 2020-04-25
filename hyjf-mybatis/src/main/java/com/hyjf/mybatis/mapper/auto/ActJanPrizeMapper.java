package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActJanPrize;
import com.hyjf.mybatis.model.auto.ActJanPrizeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActJanPrizeMapper {
    int countByExample(ActJanPrizeExample example);

    int deleteByExample(ActJanPrizeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActJanPrize record);

    int insertSelective(ActJanPrize record);

    List<ActJanPrize> selectByExample(ActJanPrizeExample example);

    ActJanPrize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActJanPrize record, @Param("example") ActJanPrizeExample example);

    int updateByExample(@Param("record") ActJanPrize record, @Param("example") ActJanPrizeExample example);

    int updateByPrimaryKeySelective(ActJanPrize record);

    int updateByPrimaryKey(ActJanPrize record);
}