package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowRepayMapper {
    int countByExample(BorrowRepayExample example);

    int deleteByExample(BorrowRepayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowRepay record);

    int insertSelective(BorrowRepay record);

    List<BorrowRepay> selectByExample(BorrowRepayExample example);

    BorrowRepay selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowRepay record, @Param("example") BorrowRepayExample example);

    int updateByExample(@Param("record") BorrowRepay record, @Param("example") BorrowRepayExample example);

    int updateByPrimaryKeySelective(BorrowRepay record);

    int updateByPrimaryKey(BorrowRepay record);
}