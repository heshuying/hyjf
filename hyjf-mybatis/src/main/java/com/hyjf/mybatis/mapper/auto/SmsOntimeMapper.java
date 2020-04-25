package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SmsOntime;
import com.hyjf.mybatis.model.auto.SmsOntimeExample;
import com.hyjf.mybatis.model.auto.SmsOntimeWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsOntimeMapper {
    int countByExample(SmsOntimeExample example);

    int deleteByExample(SmsOntimeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsOntimeWithBLOBs record);

    int insertSelective(SmsOntimeWithBLOBs record);

    List<SmsOntimeWithBLOBs> selectByExampleWithBLOBs(SmsOntimeExample example);

    List<SmsOntime> selectByExample(SmsOntimeExample example);

    SmsOntimeWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsOntimeWithBLOBs record, @Param("example") SmsOntimeExample example);

    int updateByExampleWithBLOBs(@Param("record") SmsOntimeWithBLOBs record, @Param("example") SmsOntimeExample example);

    int updateByExample(@Param("record") SmsOntime record, @Param("example") SmsOntimeExample example);

    int updateByPrimaryKeySelective(SmsOntimeWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SmsOntimeWithBLOBs record);

    int updateByPrimaryKey(SmsOntime record);
}