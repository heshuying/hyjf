package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhUserAuthConfigLog;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigLogCustomize;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhUserAuthConfigLogMapper {
    int countByExample(HjhUserAuthConfigLogExample example);

    int deleteByExample(HjhUserAuthConfigLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhUserAuthConfigLog record);

    int insertSelective(HjhUserAuthConfigLog record);

    List<HjhUserAuthConfigLog> selectByExample(HjhUserAuthConfigLogExample example);

    HjhUserAuthConfigLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhUserAuthConfigLog record, @Param("example") HjhUserAuthConfigLogExample example);

    int updateByExample(@Param("record") HjhUserAuthConfigLog record, @Param("example") HjhUserAuthConfigLogExample example);

    int updateByPrimaryKeySelective(HjhUserAuthConfigLog record);

    int updateByPrimaryKey(HjhUserAuthConfigLog record);

    List<HjhUserAuthConfigLogCustomize> selectCustomizeAuthConfigLogList(@Param("limitStart") int limitStart,@Param("limitEnd") int limitEnd);
}