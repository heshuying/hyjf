package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BorrowTenderTmpInfo;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowTenderTmpInfoMapper {
    int countByExample(BorrowTenderTmpInfoExample example);

    int deleteByExample(BorrowTenderTmpInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BorrowTenderTmpInfo record);

    int insertSelective(BorrowTenderTmpInfo record);

    List<BorrowTenderTmpInfo> selectByExample(BorrowTenderTmpInfoExample example);

    BorrowTenderTmpInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BorrowTenderTmpInfo record, @Param("example") BorrowTenderTmpInfoExample example);

    int updateByExample(@Param("record") BorrowTenderTmpInfo record, @Param("example") BorrowTenderTmpInfoExample example);

    int updateByPrimaryKeySelective(BorrowTenderTmpInfo record);

    int updateByPrimaryKey(BorrowTenderTmpInfo record);
}