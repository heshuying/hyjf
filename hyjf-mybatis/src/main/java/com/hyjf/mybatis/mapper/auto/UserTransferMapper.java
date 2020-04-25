package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.UserTransferExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserTransferMapper {
    int countByExample(UserTransferExample example);

    int deleteByExample(UserTransferExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserTransfer record);

    int insertSelective(UserTransfer record);

    List<UserTransfer> selectByExample(UserTransferExample example);

    UserTransfer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserTransfer record, @Param("example") UserTransferExample example);

    int updateByExample(@Param("record") UserTransfer record, @Param("example") UserTransferExample example);

    int updateByPrimaryKeySelective(UserTransfer record);

    int updateByPrimaryKey(UserTransfer record);
}