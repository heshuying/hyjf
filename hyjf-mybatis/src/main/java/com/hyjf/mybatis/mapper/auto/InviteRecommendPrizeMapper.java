package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.auto.InviteRecommendPrizeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InviteRecommendPrizeMapper {
    int countByExample(InviteRecommendPrizeExample example);

    int deleteByExample(InviteRecommendPrizeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(InviteRecommendPrize record);

    int insertSelective(InviteRecommendPrize record);

    List<InviteRecommendPrize> selectByExample(InviteRecommendPrizeExample example);

    InviteRecommendPrize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") InviteRecommendPrize record, @Param("example") InviteRecommendPrizeExample example);

    int updateByExample(@Param("record") InviteRecommendPrize record, @Param("example") InviteRecommendPrizeExample example);

    int updateByPrimaryKeySelective(InviteRecommendPrize record);

    int updateByPrimaryKey(InviteRecommendPrize record);
}