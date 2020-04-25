/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.activity.activity0601;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.wechat.annotation.SignValidate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 六一活动
 *
 * @author fuqiang
 * @version Act0601Controller, v0.1 2018/5/25 11:19
 */
@RequestMapping(Act0601Define.REQUEST_MAPPING)
@RestController
public class Act0601Controller {

    Logger logger = LoggerFactory.getLogger(Act0601Controller.class);

    private static String ACTIVITYID = PropUtils.getSystem("hyjf.201806activity.id");
    private static String COUPONCODE = PropUtils.getSystem("hyjf.201806activity.couponcode");


 /*   private static String activityId = "123456";
    private static String couponCode = "PJ2679518";*/

    @Autowired
    private Act0601Service act0601Service;

    @SignValidate
    @RequestMapping(value = Act0601Define.GET_ACTIVITY, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public JSONObject exchange(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) {

        JSONObject jsonObject = new JSONObject();
        String command = (String) params.get("command");

        Integer userId = (Integer) request.getAttribute("userId");
        // 校验兑奖口令不能为空
        if (StringUtils.isBlank(command)) {
            jsonObject.put("status", "11");
            jsonObject.put("statusDesc", "兑奖口令错误!");
            return jsonObject;
        }

        try {
            // 校验兑奖口令是否正确
			jsonObject = act0601Service.selectCommand(command, userId, COUPONCODE, ACTIVITYID);
			if (jsonObject.get("status") != null) {
				return jsonObject;
			}
            jsonObject = act0601Service.sendCoupon(userId, COUPONCODE, ACTIVITYID);
            String status = jsonObject.getString("status");
            if ("11".equals(status)) {
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("61活动兑奖出错...");
            RedisUtils.del(Act0601Define.COMMOND_USER_KEY + userId);
            jsonObject.put("status", "11");
            jsonObject.put("statusDesc", "兑奖口令错误!");
            return jsonObject;
        }

        jsonObject.put("status", "000");
        jsonObject.put("statusDesc", "恭喜, 1%的加息券已经飞入<br>您的账户, 请注意查看!");
        return jsonObject;
    }

}
