package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.MessagePushPlatStatics;
import com.hyjf.mybatis.model.auto.MessagePushPlatStaticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessagePushPlatStaticsMapper {
    int countByExample(MessagePushPlatStaticsExample example);

    int deleteByExample(MessagePushPlatStaticsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MessagePushPlatStatics record);

    int insertSelective(MessagePushPlatStatics record);

    List<MessagePushPlatStatics> selectByExample(MessagePushPlatStaticsExample example);

    MessagePushPlatStatics selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MessagePushPlatStatics record, @Param("example") MessagePushPlatStaticsExample example);

    int updateByExample(@Param("record") MessagePushPlatStatics record, @Param("example") MessagePushPlatStaticsExample example);

    int updateByPrimaryKeySelective(MessagePushPlatStatics record);

    int updateByPrimaryKey(MessagePushPlatStatics record);
}