package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.BorrowUserStatisticExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowUserStatisticMapper {
    int countByExample(BorrowUserStatisticExample example);

    int deleteByExample(BorrowUserStatisticExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowUserStatistic record);

    int insertSelective(BorrowUserStatistic record);

    List<BorrowUserStatistic> selectByExample(BorrowUserStatisticExample example);

    BorrowUserStatistic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowUserStatistic record, @Param("example") BorrowUserStatisticExample example);

    int updateByExample(@Param("record") BorrowUserStatistic record, @Param("example") BorrowUserStatisticExample example);

    int updateByPrimaryKeySelective(BorrowUserStatistic record);

    int updateByPrimaryKey(BorrowUserStatistic record);
}