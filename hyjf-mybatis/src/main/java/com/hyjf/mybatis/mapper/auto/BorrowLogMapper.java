package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowLog;
import com.hyjf.mybatis.model.auto.BorrowLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowLogMapper {
    int countByExample(BorrowLogExample example);

    int deleteByExample(BorrowLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowLog record);

    int insertSelective(BorrowLog record);

    List<BorrowLog> selectByExample(BorrowLogExample example);

    BorrowLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowLog record, @Param("example") BorrowLogExample example);

    int updateByExample(@Param("record") BorrowLog record, @Param("example") BorrowLogExample example);

    int updateByPrimaryKeySelective(BorrowLog record);

    int updateByPrimaryKey(BorrowLog record);
}