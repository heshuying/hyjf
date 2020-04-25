package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DirectionalTransferAssociatedRecordsMapper {
    int countByExample(DirectionalTransferAssociatedRecordsExample example);

    int deleteByExample(DirectionalTransferAssociatedRecordsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DirectionalTransferAssociatedRecords record);

    int insertSelective(DirectionalTransferAssociatedRecords record);

    List<DirectionalTransferAssociatedRecords> selectByExample(DirectionalTransferAssociatedRecordsExample example);

    DirectionalTransferAssociatedRecords selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DirectionalTransferAssociatedRecords record, @Param("example") DirectionalTransferAssociatedRecordsExample example);

    int updateByExample(@Param("record") DirectionalTransferAssociatedRecords record, @Param("example") DirectionalTransferAssociatedRecordsExample example);

    int updateByPrimaryKeySelective(DirectionalTransferAssociatedRecords record);

    int updateByPrimaryKey(DirectionalTransferAssociatedRecords record);
}