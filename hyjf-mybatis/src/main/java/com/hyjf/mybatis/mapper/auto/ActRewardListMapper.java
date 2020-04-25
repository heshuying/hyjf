package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActRewardListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActRewardListMapper {
    int countByExample(ActRewardListExample example);

    int deleteByExample(ActRewardListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActRewardList record);

    int insertSelective(ActRewardList record);

    List<ActRewardList> selectByExample(ActRewardListExample example);

    ActRewardList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActRewardList record, @Param("example") ActRewardListExample example);

    int updateByExample(@Param("record") ActRewardList record, @Param("example") ActRewardListExample example);

    int updateByPrimaryKeySelective(ActRewardList record);

    int updateByPrimaryKey(ActRewardList record);
}