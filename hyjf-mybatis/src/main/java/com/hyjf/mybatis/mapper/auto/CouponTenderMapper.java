package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CouponTender;
import com.hyjf.mybatis.model.auto.CouponTenderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CouponTenderMapper {
    int countByExample(CouponTenderExample example);

    int deleteByExample(CouponTenderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CouponTender record);

    int insertSelective(CouponTender record);

    List<CouponTender> selectByExample(CouponTenderExample example);

    CouponTender selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CouponTender record, @Param("example") CouponTenderExample example);

    int updateByExample(@Param("record") CouponTender record, @Param("example") CouponTenderExample example);

    int updateByPrimaryKeySelective(CouponTender record);

    int updateByPrimaryKey(CouponTender record);
}