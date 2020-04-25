package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProductInterest;
import com.hyjf.mybatis.model.auto.ProductInterestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductInterestMapper {
    int countByExample(ProductInterestExample example);

    int deleteByExample(ProductInterestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductInterest record);

    int insertSelective(ProductInterest record);

    List<ProductInterest> selectByExample(ProductInterestExample example);

    ProductInterest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductInterest record, @Param("example") ProductInterestExample example);

    int updateByExample(@Param("record") ProductInterest record, @Param("example") ProductInterestExample example);

    int updateByPrimaryKeySelective(ProductInterest record);

    int updateByPrimaryKey(ProductInterest record);
}