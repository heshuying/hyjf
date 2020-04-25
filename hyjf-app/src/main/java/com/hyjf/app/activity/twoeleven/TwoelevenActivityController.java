/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.app.activity.twoeleven;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.TwoElevenAwardsNameEnum;
import com.hyjf.common.util.ActivityDateUtil;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 双十一活动
 * @author yinhui
 * @version TwoelevenActivityController, v0.1 2018/10/11 9:57
 */
@Controller
@RequestMapping(value = "/twoeleven")
public class TwoelevenActivityController extends BaseController {

    @Autowired
    @Qualifier("appTwoelevenActivityServiceImpl")
    private TwoelevenActivityService twoelevenActivityService;

    private Logger logger = LoggerFactory.getLogger(TwoelevenActivityController.class);

    /**
     * 初始化数据接口
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/init", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public JSONObject queryActivityList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "999");
        jsonObject.put("statusDesc", "用户未登录，请先登录！");

        long startTime = System.currentTimeMillis();
        long now = 0L;
        long va = 0L;
        // 获取登陆用户userId
        String sign = request.getParameter("sign");
        Integer userId = null;
        if (StringUtils.isNotBlank(sign)) {
            try {
                userId = SecretUtil.getUserId(sign);

                if(userId != null){
                    jsonObject.put("status", "000");
                    jsonObject.put("statusDesc", "成功");
                }

            } catch (Exception e) {
            }
        }

        ActivityList activityList = twoelevenActivityService.checkActivityIfAvailable(ActivityDateUtil.TWOELEVEN_ACTIVITY_ID);

        Map<String,Object> activityOne = new HashMap<String,Object>();

        activityOne.put("activity1",twoelevenActivityService.getActivityJX(activityList));
        activityOne.put("activity2",twoelevenActivityService.getActivityDJ(activityList));
        activityOne.put("activity3",twoelevenActivityService.getActivitySW(userId,activityList));
        activityOne.put("otherData",twoelevenActivityService.getActivityOtherDataVO(userId,activityList));

        jsonObject.put("data",activityOne);

        now = System.currentTimeMillis();
        va = now - startTime;
        logger.info("【双十一秒杀活动】初始化数据接口执行时长="+va);
        return jsonObject;
    }

    /**
     * 奖品秒杀接口
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/seckill", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject seckillAward(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "999");
        jsonObject.put("statusDesc", "用户未登录，请先登录！");

        // 防刷验证
        String activityToken = request.getParameter("activityToken");
        if (StringUtils.isEmpty(activityToken)) {
            jsonObject.put("status", "800");
            jsonObject.put("statusDesc", "Token为空或者不正确！");
            return jsonObject;
        }
        //从Redis中获得防刷验证
        String token = RedisUtils.get(RedisConstants.TWO_ELEVEN_ACTIVITY_TOKEN);
        if(StringUtils.isEmpty(token) || !token.equals(activityToken)){
            jsonObject.put("status", "800");
            jsonObject.put("statusDesc", "Token为空或者不正确！");
            return jsonObject;
        }

        // 秒杀奖品ID
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(TwoElevenAwardsNameEnum.getValue(id))) {
            jsonObject.put("status", "804");
            jsonObject.put("statusDesc", "id为空或者不准确！");
            return jsonObject;
        }

        //当前进行活动
        String activity = request.getParameter("activity");
        if (StringUtils.isEmpty(activity)) {
            jsonObject.put("status", "804");
            jsonObject.put("statusDesc", "当前进行活动为空");
            return jsonObject;
        }

        //奖品数量
        Map<String,Integer> mapCoupon = twoelevenActivityService.getCouponData(id);
        if(mapCoupon == null || mapCoupon.get("rest") <= 0){
            jsonObject.put("status", "801");
            jsonObject.put("statusDesc", "加油再接再厉");
            return jsonObject;
        }

        // 获取登陆用户userId
        String sign = request.getParameter("sign");

        if (Validator.isNull(sign)) {
            return jsonObject;
        }
        Integer userId = null;
        try {
            userId = SecretUtil.getUserId(sign);
        } catch (Exception e) {
            return jsonObject;
        }
        // 获取用户id
        if (userId == null) {
            return jsonObject;
        }

        jsonObject = twoelevenActivityService.seckillAward(id,activity,userId);

        return jsonObject;
    }

}
