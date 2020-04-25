package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AccountAccurate;
import com.hyjf.mybatis.model.auto.AccountAccurateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountAccurateMapper {
    int countByExample(AccountAccurateExample example);

    int deleteByExample(AccountAccurateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountAccurate record);

    int insertSelective(AccountAccurate record);

    List<AccountAccurate> selectByExample(AccountAccurateExample example);

    AccountAccurate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountAccurate record, @Param("example") AccountAccurateExample example);

    int updateByExample(@Param("record") AccountAccurate record, @Param("example") AccountAccurateExample example);

    int updateByPrimaryKeySelective(AccountAccurate record);

    int updateByPrimaryKey(AccountAccurate record);
}