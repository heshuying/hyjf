package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowFinmanCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanChargeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowFinmanChargeMapper {
    int countByExample(BorrowFinmanChargeExample example);

    int deleteByExample(BorrowFinmanChargeExample example);

    int deleteByPrimaryKey(String manChargeCd);

    int insert(BorrowFinmanCharge record);

    int insertSelective(BorrowFinmanCharge record);

    List<BorrowFinmanCharge> selectByExample(BorrowFinmanChargeExample example);

    BorrowFinmanCharge selectByPrimaryKey(String manChargeCd);

    int updateByExampleSelective(@Param("record") BorrowFinmanCharge record, @Param("example") BorrowFinmanChargeExample example);

    int updateByExample(@Param("record") BorrowFinmanCharge record, @Param("example") BorrowFinmanChargeExample example);

    int updateByPrimaryKeySelective(BorrowFinmanCharge record);

    int updateByPrimaryKey(BorrowFinmanCharge record);
}