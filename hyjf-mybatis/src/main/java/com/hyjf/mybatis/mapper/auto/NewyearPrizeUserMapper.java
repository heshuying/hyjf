package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearPrizeUser;
import com.hyjf.mybatis.model.auto.NewyearPrizeUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearPrizeUserMapper {
    int countByExample(NewyearPrizeUserExample example);

    int deleteByExample(NewyearPrizeUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearPrizeUser record);

    int insertSelective(NewyearPrizeUser record);

    List<NewyearPrizeUser> selectByExample(NewyearPrizeUserExample example);

    NewyearPrizeUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearPrizeUser record, @Param("example") NewyearPrizeUserExample example);

    int updateByExample(@Param("record") NewyearPrizeUser record, @Param("example") NewyearPrizeUserExample example);

    int updateByPrimaryKeySelective(NewyearPrizeUser record);

    int updateByPrimaryKey(NewyearPrizeUser record);
}