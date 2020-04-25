package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SmsMailTemplate;
import com.hyjf.mybatis.model.auto.SmsMailTemplateExample;
import com.hyjf.mybatis.model.auto.SmsMailTemplateKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsMailTemplateMapper {
    int countByExample(SmsMailTemplateExample example);

    int deleteByExample(SmsMailTemplateExample example);

    int deleteByPrimaryKey(SmsMailTemplateKey key);

    int insert(SmsMailTemplate record);

    int insertSelective(SmsMailTemplate record);

    List<SmsMailTemplate> selectByExampleWithBLOBs(SmsMailTemplateExample example);

    List<SmsMailTemplate> selectByExample(SmsMailTemplateExample example);

    SmsMailTemplate selectByPrimaryKey(SmsMailTemplateKey key);

    int updateByExampleSelective(@Param("record") SmsMailTemplate record, @Param("example") SmsMailTemplateExample example);

    int updateByExampleWithBLOBs(@Param("record") SmsMailTemplate record, @Param("example") SmsMailTemplateExample example);

    int updateByExample(@Param("record") SmsMailTemplate record, @Param("example") SmsMailTemplateExample example);

    int updateByPrimaryKeySelective(SmsMailTemplate record);

    int updateByPrimaryKeyWithBLOBs(SmsMailTemplate record);

    int updateByPrimaryKey(SmsMailTemplate record);
}