package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowFinhxfmanCharge;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanChargeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowFinhxfmanChargeMapper {
    int countByExample(BorrowFinhxfmanChargeExample example);

    int deleteByExample(BorrowFinhxfmanChargeExample example);

    int deleteByPrimaryKey(String manChargeCd);

    int insert(BorrowFinhxfmanCharge record);

    int insertSelective(BorrowFinhxfmanCharge record);

    List<BorrowFinhxfmanCharge> selectByExample(BorrowFinhxfmanChargeExample example);

    BorrowFinhxfmanCharge selectByPrimaryKey(String manChargeCd);

    int updateByExampleSelective(@Param("record") BorrowFinhxfmanCharge record, @Param("example") BorrowFinhxfmanChargeExample example);

    int updateByExample(@Param("record") BorrowFinhxfmanCharge record, @Param("example") BorrowFinhxfmanChargeExample example);

    int updateByPrimaryKeySelective(BorrowFinhxfmanCharge record);

    int updateByPrimaryKey(BorrowFinhxfmanCharge record);
}