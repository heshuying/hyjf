package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearSendPrize;
import com.hyjf.mybatis.model.auto.NewyearSendPrizeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearSendPrizeMapper {
    int countByExample(NewyearSendPrizeExample example);

    int deleteByExample(NewyearSendPrizeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearSendPrize record);

    int insertSelective(NewyearSendPrize record);

    List<NewyearSendPrize> selectByExample(NewyearSendPrizeExample example);

    NewyearSendPrize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearSendPrize record, @Param("example") NewyearSendPrizeExample example);

    int updateByExample(@Param("record") NewyearSendPrize record, @Param("example") NewyearSendPrizeExample example);

    int updateByPrimaryKeySelective(NewyearSendPrize record);

    int updateByPrimaryKey(NewyearSendPrize record);
}