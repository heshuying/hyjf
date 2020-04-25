package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.ProtocolTemplateExample;
import java.util.List;

import com.hyjf.mybatis.model.auto.ProtocolVersion;
import org.apache.ibatis.annotations.Param;

public interface ProtocolTemplateMapper {
    int countByExample(ProtocolTemplateExample example);

    int deleteByExample(ProtocolTemplateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProtocolTemplate record);

    int insertSelective(ProtocolTemplate record);

    List<ProtocolTemplate> selectByExample(ProtocolTemplateExample example);

    ProtocolTemplate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProtocolTemplate record, @Param("example") ProtocolTemplateExample example);

    int updateByExample(@Param("record") ProtocolTemplate record, @Param("example") ProtocolTemplateExample example);

    int updateByPrimaryKeySelective(ProtocolTemplate record);

    int updateByPrimaryKey(ProtocolTemplate record);

    List<ProtocolTemplate> getdisplayNameDynamic();

    ProtocolVersion selectVersionById(Integer versionId);

    ProtocolTemplate selectTemplateById(String protocolId);

    void startUseExistProtocol(ProtocolTemplate protocolTemplate);
}