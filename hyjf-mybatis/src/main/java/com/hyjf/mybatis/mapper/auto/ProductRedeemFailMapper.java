package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProductRedeemFail;
import com.hyjf.mybatis.model.auto.ProductRedeemFailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductRedeemFailMapper {
    int countByExample(ProductRedeemFailExample example);

    int deleteByExample(ProductRedeemFailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductRedeemFail record);

    int insertSelective(ProductRedeemFail record);

    List<ProductRedeemFail> selectByExample(ProductRedeemFailExample example);

    ProductRedeemFail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductRedeemFail record, @Param("example") ProductRedeemFailExample example);

    int updateByExample(@Param("record") ProductRedeemFail record, @Param("example") ProductRedeemFailExample example);

    int updateByPrimaryKeySelective(ProductRedeemFail record);

    int updateByPrimaryKey(ProductRedeemFail record);
}