package com.hyjf.app.activity.newyear;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.model.customize.apiweb.UserLanternIllumineCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class NewYearActivityServiceImpl extends BaseServiceImpl implements NewYearActivityService {
    public static JedisPool pool = RedisUtils.getConnection();
    @Override
    public void getPresentRiddles(ModelAndView modelAndView) {
        JSONObject jsonObject=CommonSoaUtils.getPresentRiddles();
        
        String questionId=(String) jsonObject.get("questionId");
        
        modelAndView.addObject("ifReturnQuestion", jsonObject.get("ifReturnQuestion"));
        modelAndView.addObject("questionId", questionId);
        modelAndView.addObject("questionContent", jsonObject.get("questionContent"));
        modelAndView.addObject("questionAnswer", jsonObject.get("questionAnswer"));
        modelAndView.addObject("questionImageName", jsonObject.get("questionImageName"));
        modelAndView.addObject("questionHint", jsonObject.get("questionHint"));
        modelAndView.addObject("lanternFestivalFlag", jsonObject.get("lanternFestivalFlag"));
        //页签标示
        if(StringUtils.isNotEmpty(questionId)){
            modelAndView.addObject("tabFlag", "2");
        }else{
            modelAndView.addObject("tabFlag", "1");
        }
        
        
    }

    @Override
    public void getUserPresentCumulativeCoupon(Integer userId,ModelAndView modelAndView) {
        JSONObject jsonObject=CommonSoaUtils.getUserPresentCumulativeCoupon(userId);
        modelAndView.addObject("canReceiveFlag", jsonObject.get("canReceiveFlag"));
        modelAndView.addObject("userPresentCumulativeCoupon", jsonObject.get("userPresentCumulativeCoupon"));
        modelAndView.addObject("userPresentCumulativeCouponCount", jsonObject.get("userPresentCumulativeCouponCount"));
        modelAndView.addObject("showAnswerFlag", jsonObject.get("showAnswerFlag"));
        modelAndView.addObject("lastQuestion", jsonObject.get("lastQuestion"));
    }

    @Override
    public void getUserLanternIllumineList(Integer userId, ModelAndView modelAndView) {
        JSONObject jsonObject=CommonSoaUtils.getUserLanternIllumineList(userId);
        List<UserLanternIllumineCustomize> userLanternIllumineList = JSONArray.parseArray(JSONObject.toJSONString(jsonObject.get("userLanternIllumineList")), UserLanternIllumineCustomize.class);
        modelAndView.addObject("userLanternIllumineList", userLanternIllumineList);
    }

    @Override
    public void getTodayUserAnswerFlag(Integer userId, ModelAndView modelAndView) {
        JSONObject jsonObject=CommonSoaUtils.getTodayUserAnswerFlag(userId);
        modelAndView.addObject("userAnswerFlag", jsonObject.get("userAnswerFlag"));
    }


    @Override
    public void check(String questionId, Integer userId, JSONObject info) {
        JSONObject jsonObject=CommonSoaUtils.check(userId, questionId);
        info.put("checkStatus", jsonObject.get("checkStatus"));
        info.put("message1", jsonObject.get("message1"));
        info.put("message2", jsonObject.get("message2"));
    }

    @Override
    public void insertUserAnswerRecordInit(String questionId, Integer userId, JSONObject info) {
        JSONObject jsonObject=CommonSoaUtils.insertUserAnswerRecordInit(userId,new Integer(questionId));
        info.put("status", jsonObject.get("status"));

        
    }

    @Override
    public void updateUserAnswerRecord(String questionId, Integer userId,String userAnswer, JSONObject info) {
        JSONObject jsonObject=CommonSoaUtils.updateUserAnswerRecord(userId, questionId, userAnswer);
        info.put("isCorrect", jsonObject.get("isCorrect"));
        info.put("prompt", jsonObject.get("prompt"));
        info.put("couponCount", jsonObject.get("couponCount"));
        
    }
    @Override
    public void clearRedis() {
        Jedis jedis=pool.getResource();
        Transaction tx = jedis.multi();
        tx.set("userAnswerMap", JSONArray.toJSONString(new HashMap<String, String>()));
        tx.exec();
        
    }
    
}
