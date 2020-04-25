package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStaticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessagePushTemplateStaticsMapper {
    int countByExample(MessagePushTemplateStaticsExample example);

    int deleteByExample(MessagePushTemplateStaticsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MessagePushTemplateStatics record);

    int insertSelective(MessagePushTemplateStatics record);

    List<MessagePushTemplateStatics> selectByExample(MessagePushTemplateStaticsExample example);

    MessagePushTemplateStatics selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MessagePushTemplateStatics record, @Param("example") MessagePushTemplateStaticsExample example);

    int updateByExample(@Param("record") MessagePushTemplateStatics record, @Param("example") MessagePushTemplateStaticsExample example);

    int updateByPrimaryKeySelective(MessagePushTemplateStatics record);

    int updateByPrimaryKey(MessagePushTemplateStatics record);
}