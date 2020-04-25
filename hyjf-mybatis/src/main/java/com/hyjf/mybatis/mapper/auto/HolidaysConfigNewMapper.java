package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HolidaysConfigNew;
import com.hyjf.mybatis.model.auto.HolidaysConfigNewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HolidaysConfigNewMapper {
    int countByExample(HolidaysConfigNewExample example);

    int deleteByExample(HolidaysConfigNewExample example);

    int insert(HolidaysConfigNew record);

    int insertSelective(HolidaysConfigNew record);

    List<HolidaysConfigNew> selectByExample(HolidaysConfigNewExample example);

    int updateByExampleSelective(@Param("record") HolidaysConfigNew record, @Param("example") HolidaysConfigNewExample example);

    int updateByExample(@Param("record") HolidaysConfigNew record, @Param("example") HolidaysConfigNewExample example);
}