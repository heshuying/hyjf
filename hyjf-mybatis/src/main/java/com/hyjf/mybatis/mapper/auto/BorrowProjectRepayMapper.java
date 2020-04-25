package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectRepayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowProjectRepayMapper {
    int countByExample(BorrowProjectRepayExample example);

    int deleteByExample(BorrowProjectRepayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowProjectRepay record);

    int insertSelective(BorrowProjectRepay record);

    List<BorrowProjectRepay> selectByExample(BorrowProjectRepayExample example);

    BorrowProjectRepay selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowProjectRepay record, @Param("example") BorrowProjectRepayExample example);

    int updateByExample(@Param("record") BorrowProjectRepay record, @Param("example") BorrowProjectRepayExample example);

    int updateByPrimaryKeySelective(BorrowProjectRepay record);

    int updateByPrimaryKey(BorrowProjectRepay record);
}