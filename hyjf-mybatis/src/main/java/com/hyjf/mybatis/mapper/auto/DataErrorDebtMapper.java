package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DataErrorDebt;
import com.hyjf.mybatis.model.auto.DataErrorDebtExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataErrorDebtMapper {
    int countByExample(DataErrorDebtExample example);

    int deleteByExample(DataErrorDebtExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DataErrorDebt record);

    int insertSelective(DataErrorDebt record);

    List<DataErrorDebt> selectByExample(DataErrorDebtExample example);

    DataErrorDebt selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DataErrorDebt record, @Param("example") DataErrorDebtExample example);

    int updateByExample(@Param("record") DataErrorDebt record, @Param("example") DataErrorDebtExample example);

    int updateByPrimaryKeySelective(DataErrorDebt record);

    int updateByPrimaryKey(DataErrorDebt record);
}