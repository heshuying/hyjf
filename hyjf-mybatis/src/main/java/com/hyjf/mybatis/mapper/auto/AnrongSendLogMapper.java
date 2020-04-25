package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AnrongSendLog;
import com.hyjf.mybatis.model.auto.AnrongSendLogExample;
import com.hyjf.mybatis.model.auto.AnrongSendLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AnrongSendLogMapper {
    int countByExample(AnrongSendLogExample example);

    int deleteByExample(AnrongSendLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AnrongSendLogWithBLOBs record);

    int insertSelective(AnrongSendLogWithBLOBs record);

    List<AnrongSendLogWithBLOBs> selectByExampleWithBLOBs(AnrongSendLogExample example);

    List<AnrongSendLog> selectByExample(AnrongSendLogExample example);

    AnrongSendLogWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AnrongSendLogWithBLOBs record, @Param("example") AnrongSendLogExample example);

    int updateByExampleWithBLOBs(@Param("record") AnrongSendLogWithBLOBs record, @Param("example") AnrongSendLogExample example);

    int updateByExample(@Param("record") AnrongSendLog record, @Param("example") AnrongSendLogExample example);

    int updateByPrimaryKeySelective(AnrongSendLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AnrongSendLogWithBLOBs record);

    int updateByPrimaryKey(AnrongSendLog record);
}