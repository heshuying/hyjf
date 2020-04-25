package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import com.hyjf.mybatis.model.auto.HjhPlanCapitalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhPlanCapitalMapper {
    int countByExample(HjhPlanCapitalExample example);

    int deleteByExample(HjhPlanCapitalExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhPlanCapital record);

    int insertSelective(HjhPlanCapital record);

    List<HjhPlanCapital> selectByExample(HjhPlanCapitalExample example);

    HjhPlanCapital selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhPlanCapital record, @Param("example") HjhPlanCapitalExample example);

    int updateByExample(@Param("record") HjhPlanCapital record, @Param("example") HjhPlanCapitalExample example);

    int updateByPrimaryKeySelective(HjhPlanCapital record);

    int updateByPrimaryKey(HjhPlanCapital record);
}