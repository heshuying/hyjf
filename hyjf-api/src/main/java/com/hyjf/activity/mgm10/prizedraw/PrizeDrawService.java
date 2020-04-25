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

package com.hyjf.activity.mgm10.prizedraw;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

public interface PrizeDrawService extends BaseService {

    JSONObject sendPrizeCoupon(String userId, String groupCode, int sendCount);

    boolean insertPrizeDraw(InviteRecommendPrize recommendPrize);

    String generatePrize(Integer blackUser);

    InviteRecommend getRecommendInfo(Map<String, Object> paraMap);

    List<Map<String,Object>> getPrizeWinList();

    List<PrizeGetCustomize> getPrizeList(Map<String, Object> paraMap);

    PrizeGetCustomize getPrizeConfByGroup(String groupCode);

    int updatePrizeSendById(InviteRecommendPrize recommendPrize);
    
    

}
