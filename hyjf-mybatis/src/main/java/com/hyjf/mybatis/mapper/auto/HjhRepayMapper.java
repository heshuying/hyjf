package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.HjhRepayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhRepayMapper {
    int countByExample(HjhRepayExample example);

    int deleteByExample(HjhRepayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhRepay record);

    int insertSelective(HjhRepay record);

    List<HjhRepay> selectByExample(HjhRepayExample example);

    HjhRepay selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhRepay record, @Param("example") HjhRepayExample example);

    int updateByExample(@Param("record") HjhRepay record, @Param("example") HjhRepayExample example);

    int updateByPrimaryKeySelective(HjhRepay record);

    int updateByPrimaryKey(HjhRepay record);
}