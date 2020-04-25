package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ServerApplication;
import com.hyjf.mybatis.model.auto.ServerApplicationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServerApplicationMapper {
    int countByExample(ServerApplicationExample example);

    int deleteByExample(ServerApplicationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServerApplication record);

    int insertSelective(ServerApplication record);

    List<ServerApplication> selectByExample(ServerApplicationExample example);

    ServerApplication selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServerApplication record, @Param("example") ServerApplicationExample example);

    int updateByExample(@Param("record") ServerApplication record, @Param("example") ServerApplicationExample example);

    int updateByPrimaryKeySelective(ServerApplication record);

    int updateByPrimaryKey(ServerApplication record);
}