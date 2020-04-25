package com.hyjf.mybatis.mapper.customize.recommend;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.recommend.InviteInfoCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteRecommendPrizeCustomize;

public interface RecommendCustomizeMapper {

    List<InviteInfoCustomize> getUserRecommendStarPrizeList(Map<String, Object> paraMap);

    List<InviteRecommendPrizeCustomize> getUserRecommendStarUsedPrizeList(Map<String, Object> paraMap);

    List<String> getInviteUserName(Map<String, Object> paraMap);

	

}