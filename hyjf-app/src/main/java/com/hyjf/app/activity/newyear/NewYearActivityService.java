package com.hyjf.app.activity.newyear;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;

/**
 * 纳觅旅游活动
 * @author zhangjp
 *
 */
public interface NewYearActivityService extends BaseService {

    void getPresentRiddles(ModelAndView modelAndView);

    void getUserPresentCumulativeCoupon(Integer userId,ModelAndView modelAndView);

    void getUserLanternIllumineList(Integer userId, ModelAndView modelAndView);

    void getTodayUserAnswerFlag(Integer userId, ModelAndView modelAndView);

    void check(String questionId, Integer userId, JSONObject info);

    void insertUserAnswerRecordInit(String questionId, Integer userId, JSONObject info);

    void updateUserAnswerRecord(String questionId, Integer userId, String userAnswer, JSONObject info);

    void clearRedis();


}
