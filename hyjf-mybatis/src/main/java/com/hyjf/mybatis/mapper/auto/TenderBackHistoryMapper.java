package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.TenderBackHistory;
import com.hyjf.mybatis.model.auto.TenderBackHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TenderBackHistoryMapper {
    int countByExample(TenderBackHistoryExample example);

    int deleteByExample(TenderBackHistoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TenderBackHistory record);

    int insertSelective(TenderBackHistory record);

    List<TenderBackHistory> selectByExample(TenderBackHistoryExample example);

    TenderBackHistory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TenderBackHistory record, @Param("example") TenderBackHistoryExample example);

    int updateByExample(@Param("record") TenderBackHistory record, @Param("example") TenderBackHistoryExample example);

    int updateByPrimaryKeySelective(TenderBackHistory record);

    int updateByPrimaryKey(TenderBackHistory record);
}