package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProductChinapnrLog;
import com.hyjf.mybatis.model.auto.ProductChinapnrLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductChinapnrLogMapper {
    int countByExample(ProductChinapnrLogExample example);

    int deleteByExample(ProductChinapnrLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductChinapnrLog record);

    int insertSelective(ProductChinapnrLog record);

    List<ProductChinapnrLog> selectByExampleWithBLOBs(ProductChinapnrLogExample example);

    List<ProductChinapnrLog> selectByExample(ProductChinapnrLogExample example);

    ProductChinapnrLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductChinapnrLog record, @Param("example") ProductChinapnrLogExample example);

    int updateByExampleWithBLOBs(@Param("record") ProductChinapnrLog record, @Param("example") ProductChinapnrLogExample example);

    int updateByExample(@Param("record") ProductChinapnrLog record, @Param("example") ProductChinapnrLogExample example);

    int updateByPrimaryKeySelective(ProductChinapnrLog record);

    int updateByPrimaryKeyWithBLOBs(ProductChinapnrLog record);

    int updateByPrimaryKey(ProductChinapnrLog record);
}