package com.hyjf.app.activity.mgm10.recommend;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseService;

/**
 * 
 * 注册送68元代金券活动Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:36:36
 */
public interface RecommendService extends BaseService {

    void getRecommendInfo(ModelAndView modelAndView, Integer userId, HttpServletRequest request);

    void getUserRecommendStarPrizeList(ModelAndView modelAndView, Integer userId);

    void getUserRecommendStarUsedPrizeList(ModelAndView modelAndView, Integer userId);

    void getUserFlag(ModelAndView modelAndView, Integer userId, HttpServletRequest request);

    void getShareInformation(ModelAndView modelAndView);

}
