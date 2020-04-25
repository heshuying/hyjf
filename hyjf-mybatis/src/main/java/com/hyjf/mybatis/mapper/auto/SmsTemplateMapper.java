package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SmsTemplate;
import com.hyjf.mybatis.model.auto.SmsTemplateExample;
import com.hyjf.mybatis.model.auto.SmsTemplateKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsTemplateMapper {
    int countByExample(SmsTemplateExample example);

    int deleteByExample(SmsTemplateExample example);

    int deleteByPrimaryKey(SmsTemplateKey key);

    int insert(SmsTemplate record);

    int insertSelective(SmsTemplate record);

    List<SmsTemplate> selectByExampleWithBLOBs(SmsTemplateExample example);

    List<SmsTemplate> selectByExample(SmsTemplateExample example);

    SmsTemplate selectByPrimaryKey(SmsTemplateKey key);

    int updateByExampleSelective(@Param("record") SmsTemplate record, @Param("example") SmsTemplateExample example);

    int updateByExampleWithBLOBs(@Param("record") SmsTemplate record, @Param("example") SmsTemplateExample example);

    int updateByExample(@Param("record") SmsTemplate record, @Param("example") SmsTemplateExample example);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKeyWithBLOBs(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);
}