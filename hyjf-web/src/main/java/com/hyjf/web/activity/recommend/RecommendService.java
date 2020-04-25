package com.hyjf.web.activity.recommend;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.web.BaseService;

/**
 * 
 * 注册送68元代金券活动Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:36:36
 */
public interface RecommendService extends BaseService {

    void getRecommendInfo(ModelAndView modelAndView, Integer userId);

    void getUserRecommendStarPrizeList(ModelAndView modelAndView, Integer userId);

    void getUserRecommendStarUsedPrizeList(ModelAndView modelAndView, Integer userId);
    
    void getPrizeChangeList(ModelAndView modelAndView, String userId);

    void getPrizeDrawList(ModelAndView modelAndView, String userId);

    JSONObject prizeChangeCheck(String userId, String groupCode, String changeCount);

    JSONObject doPrizeChange(String userId, String groupCode, String changeCount);

    JSONObject doPrizeDraw(String userId);

}
