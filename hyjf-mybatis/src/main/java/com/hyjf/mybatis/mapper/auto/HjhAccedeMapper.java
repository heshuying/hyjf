package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhAccedeMapper {
    int countByExample(HjhAccedeExample example);

    int deleteByExample(HjhAccedeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhAccede record);

    int insertSelective(HjhAccede record);

    List<HjhAccede> selectByExample(HjhAccedeExample example);

    HjhAccede selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhAccede record, @Param("example") HjhAccedeExample example);

    int updateByExample(@Param("record") HjhAccede record, @Param("example") HjhAccedeExample example);

    int updateByPrimaryKeySelective(HjhAccede record);

    int updateByPrimaryKey(HjhAccede record);
}