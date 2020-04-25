package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DataErrorAccount;
import com.hyjf.mybatis.model.auto.DataErrorAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataErrorAccountMapper {
    int countByExample(DataErrorAccountExample example);

    int deleteByExample(DataErrorAccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DataErrorAccount record);

    int insertSelective(DataErrorAccount record);

    List<DataErrorAccount> selectByExample(DataErrorAccountExample example);

    DataErrorAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DataErrorAccount record, @Param("example") DataErrorAccountExample example);

    int updateByExample(@Param("record") DataErrorAccount record, @Param("example") DataErrorAccountExample example);

    int updateByPrimaryKeySelective(DataErrorAccount record);

    int updateByPrimaryKey(DataErrorAccount record);
}