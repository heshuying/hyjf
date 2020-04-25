package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MerchantTransfer;
import com.hyjf.mybatis.model.auto.MerchantTransferExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantTransferMapper {
    int countByExample(MerchantTransferExample example);

    int deleteByExample(MerchantTransferExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MerchantTransfer record);

    int insertSelective(MerchantTransfer record);

    List<MerchantTransfer> selectByExample(MerchantTransferExample example);

    MerchantTransfer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MerchantTransfer record, @Param("example") MerchantTransferExample example);

    int updateByExample(@Param("record") MerchantTransfer record, @Param("example") MerchantTransferExample example);

    int updateByPrimaryKeySelective(MerchantTransfer record);

    int updateByPrimaryKey(MerchantTransfer record);
}