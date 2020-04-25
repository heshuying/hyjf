/**
 * Description:php,java session共享
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 *           Created at: 2015年12月18日 上午9:25:23
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.session;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.ThreeDESUtils;
import com.hyjf.common.util.DigitalUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.shiro.ShiroUtil;

@Controller
@RequestMapping(value = "/connection")
public class SessionController {
    private static String KEY = PropUtils.getSystem("hyjf.3des.key").trim();

    private static Integer EXPIRE_TIME = 30 * 60;

    @Autowired
    private UserSessionService userSessionService;

    /**
     * 创建session，有更新，有效期60分
     *
     * @return
     */
    @RequestMapping(value = "/create", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String createSession(HttpServletRequest request, HttpServletResponse response) {
        // connection参数格式如{"sessionId" :
        // "xxx","userId":"16235"}；
        String connection = request.getParameter("connection");
        LogUtil.startLog(this.getClass().getName(), "create======decode before", "connection:" + connection);
        Map<String, String> map = new HashMap<String, String>();
        try {
            connection = URLDecoder.decode(connection, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            LogUtil.errorLog(this.getClass().getName(), "create-decode", e1);
            e1.printStackTrace();
            map.put("error", "1");// 失败
            return JSON.toJSONString(map);
        }
        String timestamp = request.getParameter("timestamp");
        if (checkTimeOut(timestamp)) {
            map.put("error", "1");// 失败
            return JSON.toJSONString(map);
        }
        String kkey = KEY + timestamp;

        // 三重解密
        try {
            String result = ThreeDESUtils.Decrypt3DES(kkey, connection);
            JSONObject objcet = JSONObject.parseObject(result);
            String sessionId = objcet.getString("sessionId");
            String userId = objcet.getString("userId");
            if (!DigitalUtils.isInteger(userId)) {
                map.put("error", "1");// 失败
                return JSON.toJSONString(map);
            }
            // 查询用户表，验证用户是否存在
            Users user = userSessionService.getUsers(Integer.valueOf(userId));
            if (null != user) {
                JSONObject object = new JSONObject();
                object.put("userId", user.getUserId());
                object.put("username", user.getUsername());
                // 放入redis
                if (1 == RedisUtils.set(sessionId, object.toJSONString(), EXPIRE_TIME)) {
                    map.put("error", "0");// 成功
                } else {
                    map.put("error", "1");// 失败
                }
            } else {
                map.put("error", "1");// 失败
                return JSON.toJSONString(map);
            }
        } catch (Exception e) {
            map.put("error", "1");
            e.printStackTrace();
            LogUtil.errorLog(this.getClass().getName(), "createSession", e);
        }
        String result = JSON.toJSONString(map);
        LogUtil.endLog(this.getClass().getName(), "create", result);

        return result;
    }

    /**
     * 销毁redis session和java端session
     *
     * @return
     */
    @RequestMapping(value = "/destroy", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String destroySession(HttpServletRequest request, HttpServletResponse response) {
        String connection = request.getParameter("connection");
        String timestamp = request.getParameter("timestamp");
        LogUtil.startLog(this.getClass().getName(), "destroy======", "connection:" + connection + "-timestamp:"
                + timestamp);
        Map<String, String> map = new HashMap<String, String>();
        try {
            connection = URLDecoder.decode(connection, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            LogUtil.errorLog(this.getClass().getName(), "create-decode", e1);
            map.put("error", "1");
            return JSON.toJSONString(map);
        }
        String kkey = KEY + timestamp;
        if (checkTimeOut(timestamp)) {
            map.put("error", "1");// 失败
            return JSON.toJSONString(map);
        }
        // 三重解密
        try {
            String result = ThreeDESUtils.Decrypt3DES(kkey, connection);
            JSONObject objcet = JSONObject.parseObject(result);
            String sessionId = objcet.getString("sessionId");
            RedisUtils.del(sessionId);
            map.put("error", "0");// 成功
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "1");
            LogUtil.errorLog(this.getClass().getName(), "destroySession", e);
            return JSON.toJSONString(map);
        }
        String result = JSON.toJSONString(map);
        LogUtil.endLog(this.getClass().getName(), "destroy", result);
        return result;
    }

    /**
     * java端登陆
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String connection = request.getParameter("connection");
        String timestamp = request.getParameter("timestamp");
        LogUtil.startLog(this.getClass().getName(), "login======", "connection:" + connection + "-timestamp:"
                + timestamp);
        System.out.println("login======" + "connection:" + connection + "-timestamp:" + timestamp);

        String kkey = KEY + timestamp;
        Map<String, String> map = new HashMap<String, String>();
        if (checkTimeOut(timestamp)) {
            map.put("error", "1");// 失败
            return JSON.toJSONString(map);
        }
        // 三重解密
        try {
            String result = ThreeDESUtils.Decrypt3DES(kkey, connection);
            JSONObject objcet = JSONObject.parseObject(result);
            String sessionId = objcet.getString("sessionId");

            // // 根据sessionid查找userId
            String userId = RedisUtils.get(sessionId);
            // if (StringUtils.isBlank(userId)) {
            // map.put("error", "1");// 登陆失败重新登陆
            // return JSON.toJSONString(map);
            // }
            //
            // // 查询用户表，验证用户是否存在
            // boolean flag =
            // userSessionService.checkUserExists(Integer.valueOf(userId));
            // if (!flag) {
            // map.put("error", "1");// 失败
            // return JSON.toJSONString(map);
            // }

            // 放入redis
            Boolean exist = RedisUtils.exists(sessionId);// 存在1，不存在0
            if (exist == null) {
                map.put("error", "1");// 失败
                return JSON.toJSONString(map);
            } else {
                map.put("error", "0");// 成功
                RedisUtils.set(sessionId, userId, EXPIRE_TIME);
            }

            Users user = userSessionService.getUsers(Integer.valueOf(userId));
            JSONObject object = new JSONObject();
            object.put("userId", user.getUserId());
            object.put("username", user.getUsername());
            // System.out.println("login_jseessionid RedisStart:" +
            // object.toJSONString());
            RedisUtils.set("user:" + sessionId, object.toJSONString(), EXPIRE_TIME);
            // System.out.println("login_jseessionid RedisEnd:" +
            // RedisUtils.get(sessionId));

            // ShiroUtil.getLoginUserId() + "-----sessionId----" + sessionId);
            map.put("error", "0");// 成功
        } catch (Exception e) {
            map.put("error", "1");
            e.printStackTrace();
            LogUtil.errorLog(this.getClass().getName(), "login", e);
        }
        String result = JSON.toJSONString(map);
        LogUtil.endLog(this.getClass().getName(), "login", result);
        return result;
    }

    /**
     * 判断用户是否登录
     *
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "islogin", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String checkPhpLogin(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), "islogin");

        // 需要验证的变量
        String connection = request.getParameter("connection");
        try {
            connection = URLDecoder.decode(connection, "UTF-8");
        } catch (Exception e1) {
            LogUtil.errorLog(this.getClass().getName(), "islogin", e1);
        }
        String timestamp = request.getParameter("timestamp");
        Map<String, String> map = new HashMap<String, String>();
        if (checkTimeOut(timestamp)) {
            map.put("error", "1");// 失败
            return JSON.toJSONString(map);
        }
        String kkey = KEY + timestamp;

        // 三重解密
        try {
            String result = ThreeDESUtils.Decrypt3DES(kkey, connection);
            JSONObject objcet = JSONObject.parseObject(result);
            String sessionId = objcet.getString("sessionId");

            // 用户ID
            Integer userId = ShiroUtil.getLoginUserId(request);

            if (userId != null && String.valueOf(userId).equals(RedisUtils.get(sessionId))) {
                map.put("error", "0");// 成功
                return JSON.toJSONString(map);
            }

        } catch (Exception e) {
            LogUtil.errorLog(this.getClass().getName(), "islogin", e);
        }
        map.put("error", "1");// 失败
        LogUtil.endLog(this.getClass().getName(), "islogin");
        return JSON.toJSONString(map);
    }

    /**
     * 判断请求时间戳与目前时间时间差是否超过5分钟，如果超过返回true
     *
     * @param timestamp
     * @return
     */
    public boolean checkTimeOut(String timestamp) {
        if (StringUtils.isBlank(timestamp)) {
            return true;
        }
        if (!DigitalUtils.isInteger(timestamp)) {
            return true;
        }
        long time = Long.valueOf(timestamp);
        long now = System.currentTimeMillis() / 1000;
        long subtract = now - time;
        if (subtract > (30000)) {
            return true;
        }
        return false;
    }

}
