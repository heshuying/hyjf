package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsCodeMapper {
    int countByExample(SmsCodeExample example);

    int deleteByExample(SmsCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsCode record);

    int insertSelective(SmsCode record);

    List<SmsCode> selectByExample(SmsCodeExample example);

    SmsCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsCode record, @Param("example") SmsCodeExample example);

    int updateByExample(@Param("record") SmsCode record, @Param("example") SmsCodeExample example);

    int updateByPrimaryKeySelective(SmsCode record);

    int updateByPrimaryKey(SmsCode record);
}