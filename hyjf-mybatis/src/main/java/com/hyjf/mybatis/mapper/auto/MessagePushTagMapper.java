package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessagePushTagMapper {
    int countByExample(MessagePushTagExample example);

    int deleteByExample(MessagePushTagExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MessagePushTag record);

    int insertSelective(MessagePushTag record);

    List<MessagePushTag> selectByExample(MessagePushTagExample example);

    MessagePushTag selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MessagePushTag record, @Param("example") MessagePushTagExample example);

    int updateByExample(@Param("record") MessagePushTag record, @Param("example") MessagePushTagExample example);

    int updateByPrimaryKeySelective(MessagePushTag record);

    int updateByPrimaryKey(MessagePushTag record);
}