/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.midautumn;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.ActivityDateUtil;
import com.hyjf.mybatis.model.customize.ActivityMidauInfoCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 2018中秋活动
 * @author yinhui
 * @version MidauActivityController, v0.1 2018/9/10 11:04
 */
@Controller
@RequestMapping("/midautumn")
public class MidauActivityController extends BaseController {

    @Autowired
    @Qualifier("webMidauActivityServiceImpl")
    private MidauActivityService midauActivityService;

    /**
     * 登记参与活动的用户
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONObject queryActivityList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "999");
        jsonObject.put("statusDesc", "用户未登录，请先登录！");

        // 活动类型 1=加息券,2=代金券,3=实物
        String activityType = request.getParameter("activityType");
        // 获取活动类型
        if (StringUtils.isEmpty(activityType)) {
            jsonObject.put("status", "103");
            jsonObject.put("statusDesc", "活动类型不能为空！");
            return jsonObject;
        }

        String code = midauActivityService.checkActivityIfAvailable(ActivityDateUtil.MIDAU_ACTIVITY_ID);
        if (!"000".equals(code)) {
            jsonObject.put("status", "101");
            jsonObject.put("statusDesc", "不在活动期内！");
            return jsonObject;
        }
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request);
        } catch (Exception e) {
            return jsonObject;
        }
        // 获取用户id
        if (userId == null) {
            return jsonObject;
        }

        try {

            midauActivityService.registerUser(userId,activityType);

            jsonObject.put("status", "000");
            jsonObject.put("statusDesc", "成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("登记中秋活动失败......", e);
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc", "失败");
            return jsonObject;
        }

    }

    /**
     * 查询我的双节奖励
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAwardList", method = RequestMethod.POST)
    public JSONObject getAwardList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "999");
        jsonObject.put("statusDesc", "用户未登录，请先登录！");
        // 获取登陆用户userId
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request);
        } catch (Exception e) {
            return jsonObject;
        }
        // 获取用户id
        if (userId == null) {
            return jsonObject;
        }

        try {

            List<ActivityMidauInfoCustomize> list = midauActivityService.getActivityMidauInfoList(userId);

            jsonObject.put("list",list);
            jsonObject.put("status", "000");
            jsonObject.put("statusDesc", "成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("登记中秋活动失败......", e);
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc", "失败");
            return jsonObject;
        }

    }

    /**
     * 获得活动状态
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getStatus", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public JSONObject getStatus(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "999");
        jsonObject.put("statusDesc", "用户未登录，请先登录！");

        String code = midauActivityService.checkActivityIfAvailable(ActivityDateUtil.MIDAU_ACTIVITY_ID);
        if("103".equals(code)){
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc", "失败！");
            return jsonObject;
        }

        if("104".equals(code)){
            jsonObject.put("status", "104");
            jsonObject.put("statusDesc", "活动不存在！");
            return jsonObject;
        }

        if("101".equals(code)){
            jsonObject.put("status", "101");
            jsonObject.put("statusDesc", "活动未开始！");
            return jsonObject;
        }

        if("102".equals(code)){
            jsonObject.put("status", "102");
            jsonObject.put("statusDesc", "活动已结束！");
            return jsonObject;
        }

        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request);
        } catch (Exception e) {
            return jsonObject;
        }
        // 获取用户id
        if (userId == null) {
            return jsonObject;
        }

        jsonObject.put("status", "000");
        jsonObject.put("statusDesc", "成功");
        return jsonObject;

    }
}
