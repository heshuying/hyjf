package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SmsLog;
import com.hyjf.mybatis.model.auto.SmsLogExample;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsLogMapper {
    int countByExample(SmsLogExample example);

    int deleteByExample(SmsLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsLogWithBLOBs record);

    int insertSelective(SmsLogWithBLOBs record);

    List<SmsLogWithBLOBs> selectByExampleWithBLOBs(SmsLogExample example);

    List<SmsLog> selectByExample(SmsLogExample example);

    SmsLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsLogWithBLOBs record, @Param("example") SmsLogExample example);

    int updateByExampleWithBLOBs(@Param("record") SmsLogWithBLOBs record, @Param("example") SmsLogExample example);

    int updateByExample(@Param("record") SmsLog record, @Param("example") SmsLogExample example);

    int updateByPrimaryKeySelective(SmsLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SmsLogWithBLOBs record);

    int updateByPrimaryKey(SmsLog record);
}