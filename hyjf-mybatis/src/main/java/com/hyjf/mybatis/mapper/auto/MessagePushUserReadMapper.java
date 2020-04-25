package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MessagePushUserRead;
import com.hyjf.mybatis.model.auto.MessagePushUserReadExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessagePushUserReadMapper {
    int countByExample(MessagePushUserReadExample example);

    int deleteByExample(MessagePushUserReadExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MessagePushUserRead record);

    int insertSelective(MessagePushUserRead record);

    List<MessagePushUserRead> selectByExample(MessagePushUserReadExample example);

    MessagePushUserRead selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MessagePushUserRead record, @Param("example") MessagePushUserReadExample example);

    int updateByExample(@Param("record") MessagePushUserRead record, @Param("example") MessagePushUserReadExample example);

    int updateByPrimaryKeySelective(MessagePushUserRead record);

    int updateByPrimaryKey(MessagePushUserRead record);
}