package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloonExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecTenderBalloonMapper {
    int countByExample(ActdecTenderBalloonExample example);

    int deleteByExample(ActdecTenderBalloonExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecTenderBalloon record);

    int insertSelective(ActdecTenderBalloon record);

    List<ActdecTenderBalloon> selectByExample(ActdecTenderBalloonExample example);

    ActdecTenderBalloon selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecTenderBalloon record, @Param("example") ActdecTenderBalloonExample example);

    int updateByExample(@Param("record") ActdecTenderBalloon record, @Param("example") ActdecTenderBalloonExample example);

    int updateByPrimaryKeySelective(ActdecTenderBalloon record);

    int updateByPrimaryKey(ActdecTenderBalloon record);
}