package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.UserPrizeCode;
import com.hyjf.mybatis.model.auto.UserPrizeCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPrizeCodeMapper {
    int countByExample(UserPrizeCodeExample example);

    int deleteByExample(UserPrizeCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserPrizeCode record);

    int insertSelective(UserPrizeCode record);

    List<UserPrizeCode> selectByExample(UserPrizeCodeExample example);

    UserPrizeCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserPrizeCode record, @Param("example") UserPrizeCodeExample example);

    int updateByExample(@Param("record") UserPrizeCode record, @Param("example") UserPrizeCodeExample example);

    int updateByPrimaryKeySelective(UserPrizeCode record);

    int updateByPrimaryKey(UserPrizeCode record);
}