package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewChargeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowFinmanNewChargeMapper {
    int countByExample(BorrowFinmanNewChargeExample example);

    int deleteByExample(BorrowFinmanNewChargeExample example);

    int deleteByPrimaryKey(String manChargeCd);

    int insert(BorrowFinmanNewCharge record);

    int insertSelective(BorrowFinmanNewCharge record);

    List<BorrowFinmanNewCharge> selectByExample(BorrowFinmanNewChargeExample example);

    BorrowFinmanNewCharge selectByPrimaryKey(String manChargeCd);

    int updateByExampleSelective(@Param("record") BorrowFinmanNewCharge record, @Param("example") BorrowFinmanNewChargeExample example);

    int updateByExample(@Param("record") BorrowFinmanNewCharge record, @Param("example") BorrowFinmanNewChargeExample example);

    int updateByPrimaryKeySelective(BorrowFinmanNewCharge record);

    int updateByPrimaryKey(BorrowFinmanNewCharge record);
}