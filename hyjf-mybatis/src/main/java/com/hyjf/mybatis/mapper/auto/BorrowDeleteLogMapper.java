package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowDeleteLog;
import com.hyjf.mybatis.model.auto.BorrowDeleteLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowDeleteLogMapper {
    int countByExample(BorrowDeleteLogExample example);

    int deleteByExample(BorrowDeleteLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowDeleteLog record);

    int insertSelective(BorrowDeleteLog record);

    List<BorrowDeleteLog> selectByExample(BorrowDeleteLogExample example);

    BorrowDeleteLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowDeleteLog record, @Param("example") BorrowDeleteLogExample example);

    int updateByExample(@Param("record") BorrowDeleteLog record, @Param("example") BorrowDeleteLogExample example);

    int updateByPrimaryKeySelective(BorrowDeleteLog record);

    int updateByPrimaryKey(BorrowDeleteLog record);
}