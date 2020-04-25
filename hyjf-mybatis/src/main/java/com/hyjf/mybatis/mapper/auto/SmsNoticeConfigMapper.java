package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SmsNoticeConfig;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigExample;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigKey;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsNoticeConfigMapper {
    int countByExample(SmsNoticeConfigExample example);

    int deleteByExample(SmsNoticeConfigExample example);

    int deleteByPrimaryKey(SmsNoticeConfigKey key);

    int insert(SmsNoticeConfigWithBLOBs record);

    int insertSelective(SmsNoticeConfigWithBLOBs record);

    List<SmsNoticeConfigWithBLOBs> selectByExampleWithBLOBs(SmsNoticeConfigExample example);

    List<SmsNoticeConfig> selectByExample(SmsNoticeConfigExample example);

    SmsNoticeConfigWithBLOBs selectByPrimaryKey(SmsNoticeConfigKey key);

    int updateByExampleSelective(@Param("record") SmsNoticeConfigWithBLOBs record, @Param("example") SmsNoticeConfigExample example);

    int updateByExampleWithBLOBs(@Param("record") SmsNoticeConfigWithBLOBs record, @Param("example") SmsNoticeConfigExample example);

    int updateByExample(@Param("record") SmsNoticeConfig record, @Param("example") SmsNoticeConfigExample example);

    int updateByPrimaryKeySelective(SmsNoticeConfigWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SmsNoticeConfigWithBLOBs record);

    int updateByPrimaryKey(SmsNoticeConfig record);
}