package com.hyjf.wechat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetOrderIdUtils;

/**
 * 
 * 安全工具类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月18日
 * @see 上午9:21:27
 */
public class SecretUtil {

    // TODO to：星辰，年后改为正式的redis，临时用全局变量替代
    private static Map<String, String> redis = new HashMap<String, String>();

    private static String letter = "abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";

    /**
     * 生成唯一标识符
     *
     * 生成规则： uuid + 三位随机字母 + 三位随机数
     *
     * @return
     */
    public static String createSign() {
        String sign = "WX"+UUID.randomUUID().toString().replaceAll("-", "") + RandomUtils.nextInt(0, 9);
        Random random = new Random();
        // 生成随机三位字母
        for (int i = 0; i < 3; i++) {
            sign = sign + letter.charAt(random.nextInt(52));
        }
        // 生成一个随机的三位数
        sign = sign + GetOrderIdUtils.getRandomNum(1000);

        return sign;
    }

    /**
     *
     * 生成规则：
     *
     * @return
     */
    public static String createToken( Integer userId, String username , String accountId) {
        // 参数校验
        if (userId <= 0 || StringUtils.isEmpty(username.trim())) {
            throw new RuntimeException("参数异常");
        }
        AppUserToken token = new AppUserToken(userId, username, accountId);
        String encryptString = JSON.toJSONString(token);
        String sign = createSign();
        RedisUtils.set(sign, encryptString, RedisUtils.signExpireTime);
        return sign;
    }

    /**
     * 保存Redis
     *
     * @return
     */
    public static void saveRedis(String key, String value) {
        redis.put(key, value); // TODO
    }

    /**
     * 取得Redis
     *
     * @return
     */
    public static String getRedis(String key) {
        return redis.get(key); // TODO
    }

    /**
     * 删除Redis
     *
     * @return
     */
    public static String removeRedis(String key) {
        return redis.remove(key); // TODO
    }

    /**
     * 保存InitKey
     *
     * @return
     */
    public static void saveInitKey(String sign, String value) {
        saveRedis(sign + "_initKey", value);
    }

    /**
     * 取得InitKey
     *
     * @return
     */
    public static String getInitKey(String sign) {
        return getRedis(sign + "_initKey");
    }

    /**
     * 清除InitKey
     *
     * @return
     */
    public static String removeInitKey(String sign) {
        return removeRedis(sign + "_initKey");
    }

    /**
     * 保存InitKey
     *
     * @return
     */
    public static void saveKey(String sign, String value) {
        saveRedis(sign + "_Key", value);
    }

    /**
     * 取得InitKey
     *
     * @return
     */
    public static String getKey(String sign) {
        String value = RedisUtils.get(sign);
        if (value != null) {
            SignValue signValue = JSON.parseObject(value, SignValue.class);
            return signValue.getKey();
        } else {
            return null;
        }
    }

    /**
     * 清除InitKey
     *
     * @return
     */
    public static String removeKey(String sign) {
        return removeRedis(sign + "_Key");
    }

    /**
     * 保存Sign
     *
     * @return
     */
    public static void saveSignVal(String sign, String value) {
        saveRedis(sign, value);
    }

    /**
     * 取得Sign
     *
     * @return
     */
    public static String getSignVal(String sign) {
        return getRedis(sign);
    }

    /**
     * 清除Sign
     *
     * @return
     */
    public static void clearToken(String sign) {
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        if(StringUtils.isNotBlank(value)){
            removeSign(sign);
        }
    }

    /**
     * 删除Sign
     *
     * @return
     */
    public static String removeSign(String sign) {
        return removeRedis(sign);
    }

    /**
     * 判断是否存在
     *
     * @return
     */
    public static boolean isExists(String key) {
        return redis.containsKey(key);
    }

    /**
     * 从token中取得用户ID
     *
     * @param sign
     * @return
     */
    public static AppUserToken getUserId(String sign) {
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        if(StringUtils.isBlank(value)){
            return null;
        }
        AppUserToken signValue = JSON.parseObject(value, AppUserToken.class);
        if (null == signValue) {
            throw new RuntimeException("用户未登陆");
        }
        return signValue;
    }
    
    /**
     * 
     * 刷新sign
     * @author sunss
     * @param sign
     */
    public static void refreshSign(String sign) {
        if(StringUtils.isEmpty(sign)){
            return ;
        }
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        if(StringUtils.isEmpty(value)){
            return ;
        }
        RedisUtils.expire(sign,RedisUtils.signExpireTime);
    }

    /**
     * 
     * 刷新account
     * @author sunss
     * @param account
     */
    public static void refreshAccount(String sign , String accountId) {
        if(StringUtils.isEmpty(sign)){
            return ;
        }
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        if(StringUtils.isEmpty(value)){
            return ;
        }
        AppUserToken signValue = JSON.parseObject(value, AppUserToken.class);
        signValue.setAccountId(accountId);
        String encryptString = JSON.toJSONString(signValue);
        RedisUtils.set(sign, encryptString, RedisUtils.signExpireTime);
    }
}
