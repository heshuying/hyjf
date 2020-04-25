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

package com.hyjf.activity.mgm10.prizechange;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

public interface PrizeChangeService extends BaseService {

    JSONObject sendPrizeCoupon(String userId, String groupCode, int sendCount);

    boolean insertPrizeChange(InviteRecommendPrize recommendPrize, int recommendCost, int prizeCount);

    PrizeGetCustomize getPrizeConfByGroup(String groupCode);

    InviteRecommend getRecommendInfo(Map<String, Object> paraMap);

    List<PrizeGetCustomize> getPrizeList(Map<String, Object> paraMap);

    int updatePrizeSendById(InviteRecommendPrize recommendPrize);


}
