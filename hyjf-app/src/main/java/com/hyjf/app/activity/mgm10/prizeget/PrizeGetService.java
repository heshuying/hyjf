package com.hyjf.app.activity.mgm10.prizeget;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;

/**
 * 
 * 抽奖兑奖
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月10日
 * @see 下午3:18:17
 */
public interface PrizeGetService extends BaseService {

    void getPrizeChangeList(ModelAndView modelAndView, Integer userId);

    void getPrizeDrawList(ModelAndView modelAndView, Integer userId);

    JSONObject prizeChangeCheck(String userId, String groupCode, String changeCount);

    JSONObject doPrizeChange(String userId, String groupCode, String changeCount);

    JSONObject doPrizeDraw(String userId);

}
