package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CouponCheck;
import com.hyjf.mybatis.model.auto.CouponCheckExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CouponCheckMapper {
    int countByExample(CouponCheckExample example);

    int deleteByExample(CouponCheckExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CouponCheck record);

    int insertSelective(CouponCheck record);

    List<CouponCheck> selectByExample(CouponCheckExample example);

    CouponCheck selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CouponCheck record, @Param("example") CouponCheckExample example);

    int updateByExample(@Param("record") CouponCheck record, @Param("example") CouponCheckExample example);

    int updateByPrimaryKeySelective(CouponCheck record);

    int updateByPrimaryKey(CouponCheck record);
}