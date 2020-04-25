package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProductChinapnrSendLog;
import com.hyjf.mybatis.model.auto.ProductChinapnrSendLogExample;
import com.hyjf.mybatis.model.auto.ProductChinapnrSendLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductChinapnrSendLogMapper {
    int countByExample(ProductChinapnrSendLogExample example);

    int deleteByExample(ProductChinapnrSendLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductChinapnrSendLogWithBLOBs record);

    int insertSelective(ProductChinapnrSendLogWithBLOBs record);

    List<ProductChinapnrSendLogWithBLOBs> selectByExampleWithBLOBs(ProductChinapnrSendLogExample example);

    List<ProductChinapnrSendLog> selectByExample(ProductChinapnrSendLogExample example);

    ProductChinapnrSendLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductChinapnrSendLogWithBLOBs record, @Param("example") ProductChinapnrSendLogExample example);

    int updateByExampleWithBLOBs(@Param("record") ProductChinapnrSendLogWithBLOBs record, @Param("example") ProductChinapnrSendLogExample example);

    int updateByExample(@Param("record") ProductChinapnrSendLog record, @Param("example") ProductChinapnrSendLogExample example);

    int updateByPrimaryKeySelective(ProductChinapnrSendLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProductChinapnrSendLogWithBLOBs record);

    int updateByPrimaryKey(ProductChinapnrSendLog record);
}