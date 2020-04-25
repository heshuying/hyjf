/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.activity.mgm10.recommend;

import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.customize.recommend.InviteInfoCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteRecommendPrizeCustomize;

public interface RecommendService extends BaseService {

    InviteRecommend getRecommendInfo(Map<String, Object> paraMap);

    List<InviteInfoCustomize> getUserRecommendStarPrizeList(Map<String, Object> paraMap);
    
    List<InviteRecommendPrizeCustomize> getUserRecommendStarUsedPrizeList(Map<String, Object> paraMap);

    void getUserFlag(UserFlagResultBean resultBean, Integer userId);
    
}
