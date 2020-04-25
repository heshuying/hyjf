package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InviteRecommendMapper {
    int countByExample(InviteRecommendExample example);

    int deleteByExample(InviteRecommendExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(InviteRecommend record);

    int insertSelective(InviteRecommend record);

    List<InviteRecommend> selectByExample(InviteRecommendExample example);

    InviteRecommend selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") InviteRecommend record, @Param("example") InviteRecommendExample example);

    int updateByExample(@Param("record") InviteRecommend record, @Param("example") InviteRecommendExample example);

    int updateByPrimaryKeySelective(InviteRecommend record);

    int updateByPrimaryKey(InviteRecommend record);
}