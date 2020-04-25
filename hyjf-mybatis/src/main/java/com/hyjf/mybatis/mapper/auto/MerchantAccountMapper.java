package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantAccountMapper {
    int countByExample(MerchantAccountExample example);

    int deleteByExample(MerchantAccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MerchantAccount record);

    int insertSelective(MerchantAccount record);

    List<MerchantAccount> selectByExample(MerchantAccountExample example);

    MerchantAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MerchantAccount record, @Param("example") MerchantAccountExample example);

    int updateByExample(@Param("record") MerchantAccount record, @Param("example") MerchantAccountExample example);

    int updateByPrimaryKeySelective(MerchantAccount record);

    int updateByPrimaryKey(MerchantAccount record);
}