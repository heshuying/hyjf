/**
 * Description:服务器接口控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.server;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.DES;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AppAccessStatistics;

/**
 * @package com.hyjf.app.server
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ServerDefine.REQUEST_MAPPING)
public class ServerController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ServerController.class);
    /** 类名 */
    private static final String THIS_CLASS = ServerController.class.getName();

    @Autowired
    private ServerService serverService;
    
    Logger _log = LoggerFactory.getLogger(ServerController.class);

    /** 从系统配置中获取最新版本号 */
    private static final String NEW_VERSION = PropUtils.getSystem("hyjf.app.version.new");
    /** 从系统配置中获取测试环境地址 */
    private static final String TEST_SERVERIP = PropUtils.getSystem("hyjf.app.serverip.test");

    /**
     * 获取最优服务器
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(ServerDefine.GET_BEST_SERVER_MAPPING)
    public JSONObject getBestServer(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ServerDefine.GET_BEST_SERVER_MAPPING);

        JSONObject ret = new JSONObject();

        // 版本号
        String version = request.getParameter("version"); 
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // 安全码
        String secretKey = request.getParameter("secretKey");
        // 关键key
        String appId = request.getParameter("appId");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform)
                || Validator.isNull(randomString) || Validator.isNull(secretKey) || Validator.isNull(appId)) {
            ret.put("status", "1");
            ret.put("request", ServerDefine.GET_BEST_SERVER_REQUEST);
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        String appKey = "";

        if (CustomConstants.CLIENT_ANDROID.equals(platform)) {
            // 取得appKey
            appKey = "f5lnl3lN";
        } else if (CustomConstants.CLIENT_IOS.equals(platform)) {
            // 取得appKey
            appKey = "tN83YxYY";
        } else {
            ret.put("status", "1");
            ret.put("request", ServerDefine.GET_BEST_SERVER_REQUEST);
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        try {

            // 检验安全码
            boolean isSafe = SecretUtil.checkSecretKey(appId, appKey, randomString, secretKey);

            if (isSafe) {
            	if(version.substring(0,5).equals(NEW_VERSION)&&CustomConstants.CLIENT_IOS.equals(platform)){
            		_log.info("-------------version格式化：" + version.substring(0,5));
                    // 唯一标识
                    String sign = "6bcbd50a-27c4-4aac-b448-ea6b1b9228f43GYE604";

                    // 初始化密钥
                    String initKey = "XtG6KHmz";

                    // 保存到Redis中
                    SignValue signValue = new SignValue(initKey);
                    signValue.setVersion(version);
                    RedisUtils.set(sign, JSON.toJSONString(signValue), RedisUtils.signExpireTime);

                    ret.put("status", "0");
                    ret.put("statusDesc", "成功");
                    ret.put("request", ServerDefine.GET_BEST_SERVER_REQUEST);
                    ret.put("serverIp", DES.encryptDES_ECB(TEST_SERVERIP, initKey));
                    ret.put("initKey", DES.encryptDES_ECB(initKey, appKey));
                    ret.put("sign", sign);
                    // 保存InitKey
                    // SecretUtil.saveInitKey(sign, initKey);
            	} else {
	                // 唯一标识
	                String sign = SecretUtil.createSign();

	                // 初始化密钥
	                String initKey = GetCode.getRandomCode(8);

	                // 保存到Redis中
	                SignValue signValue = new SignValue(initKey);
	                signValue.setVersion(version);
	                RedisUtils.set(sign, JSON.toJSONString(signValue), RedisUtils.signExpireTime);

	                ret.put("status", "0");
	                ret.put("statusDesc", "成功");
	                ret.put("request", ServerDefine.GET_BEST_SERVER_REQUEST);
	                ret.put("serverIp", DES.encryptDES_ECB(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL), initKey));
	                ret.put("initKey", DES.encryptDES_ECB(initKey, appKey));
	                ret.put("sign", sign);
	                // 保存InitKey
	                // SecretUtil.saveInitKey(sign, initKey);
            	}
            } else {
                ret.put("status", "1");
                ret.put("request", ServerDefine.GET_BEST_SERVER_REQUEST);
                ret.put("statusDesc", "安全码错误");
            }
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("request", ServerDefine.GET_BEST_SERVER_REQUEST);
            ret.put("statusDesc", "获取最优服务器发生错误");
        }

        LogUtil.endLog(THIS_CLASS, ServerDefine.GET_BEST_SERVER_MAPPING);

        return ret;
    }

    /**
     * 获取算法密钥
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(ServerDefine.GET_KEY_MAPPING)
    public JSONObject getKey(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ServerDefine.GET_KEY_MAPPING);

        JSONObject ret = new JSONObject();

        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform)
                || Validator.isNull(sign)) {
            ret.put("status", "1");
            ret.put("request", ServerDefine.GET_KEY_REQUEST);
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        try {
        	if (version.substring(0,5).equals(NEW_VERSION) && "6bcbd50a-27c4-4aac-b448-ea6b1b9228f43GYE604".equals(sign)) {
				String key = "iUq3OGYv";
				// 保存Key
				String value = RedisUtils.get(sign);
				logger.info("value is :{}", value);
				SignValue signValue = JSON.parseObject(value, SignValue.class);
				signValue.setKey(key);
				signValue.setVersion(version);
				RedisUtils.set(sign, JSON.toJSONString(signValue));
				ret.put("status", "0");
				ret.put("statusDesc", "成功");
				ret.put("request", ServerDefine.GET_KEY_REQUEST);
				ret.put("key", DES.encryptDES_ECB(key, signValue.getInitKey()));

	            _log.info("---------获取key的值：key:" + key +"加密key:" + DES.encryptDES_ECB(key, signValue.getInitKey()));
        	} else {
	            String key = GetCode.getRandomCode(8);

	            // 保存Key
	            String value = RedisUtils.get(sign);
	            logger.info("value is :{}", value);
	            SignValue signValue = JSON.parseObject(value, SignValue.class);
	            signValue.setKey(key);
	            signValue.setVersion(version);
	            RedisUtils.set(sign, JSON.toJSONString(signValue));

	            ret.put("status", "0");
	            ret.put("statusDesc", "成功");
	            ret.put("request", ServerDefine.GET_KEY_REQUEST);
	            ret.put("key", DES.encryptDES_ECB(key, signValue.getInitKey()));
        	}
            // 获取渠道编号
            String[] temp = version.split("\\.");
            if (temp.length > 3) {
                int sourceId = Integer.parseInt(temp[3]);
                AppAccessStatistics accessStatistics = new AppAccessStatistics();
                accessStatistics.setSourceId(sourceId);
                accessStatistics.setAccessTime(new Date());
                // 插入数据库
                serverService.insertAccessInfo(accessStatistics);
            }

        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("request", ServerDefine.GET_KEY_REQUEST);
            ret.put("statusDesc", "获取算法密钥发生错误");
        }

        LogUtil.endLog(THIS_CLASS, ServerDefine.GET_KEY_MAPPING);
        logger.info("ret is : {}", ret.toJSONString());
        return ret;
    }

    /**
     * 检查参数的正确性
     *
     * @param userId
     * @param transAmtStr
     * @param bankId
     * @return
     */
    @ResponseBody
    @RequestMapping("encrypt")
    public JSONObject encrypt(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();

        String key = request.getParameter("key");
        String value = request.getParameter("value");
        ret.put("result", DES.encryptDES_ECB(value, key));
        return ret;
    }

    /**
     * 检查参数的正确性
     *
     * @param userId
     * @param transAmtStr
     * @param bankId
     * @return
     */
    @ResponseBody
    @RequestMapping("decode")
    public JSONObject decode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();

        try {
            String key = request.getParameter("key");
            String value = request.getParameter("value");
            ret.put("result", DES.decodeValue(key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
    
    /**
     * sign值同步
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(ServerDefine.SIGN_SYNC_MAPPING)
    public JSONObject signSync(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        
        String initKey = request.getParameter("initKey");
        String version = request.getParameter("version");
        String sign = request.getParameter("sign");
        
        System.out.println("开始同步sign： " + sign);
        // 保存到Redis中
        SignValue signValue = new SignValue(initKey);
        signValue.setVersion(version);
        RedisUtils.set(sign, JSON.toJSONString(signValue), RedisUtils.signExpireTime);
        
        ret.put("status", "success");
        return ret;
    }

    /**
     * 取的神策服务器地址,区分测试,线上环境
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(ServerDefine.GET_SENSOR_DATA_URL)
    public JSONObject getSensorDataURL(HttpServletRequest request, HttpServletResponse response){
        JSONObject ret = new JSONObject();
        String sensorsDataUrl = PropUtils.getSystem("sensors.data.url.path");
        if (StringUtils.isBlank(sensorsDataUrl)){
            sensorsDataUrl = "https://sa.hyjf.com:8106/sa?project=default";
        }
        ret.put("sensorsDataUrl",sensorsDataUrl);
        ret.put("status", "000");
        return ret;
    }
}
