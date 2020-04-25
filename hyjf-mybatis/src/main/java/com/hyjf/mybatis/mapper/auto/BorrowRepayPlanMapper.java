package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowRepayPlanMapper {
    int countByExample(BorrowRepayPlanExample example);

    int deleteByExample(BorrowRepayPlanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowRepayPlan record);

    int insertSelective(BorrowRepayPlan record);

    List<BorrowRepayPlan> selectByExample(BorrowRepayPlanExample example);

    BorrowRepayPlan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowRepayPlan record, @Param("example") BorrowRepayPlanExample example);

    int updateByExample(@Param("record") BorrowRepayPlan record, @Param("example") BorrowRepayPlanExample example);

    int updateByPrimaryKeySelective(BorrowRepayPlan record);

    int updateByPrimaryKey(BorrowRepayPlan record);
}