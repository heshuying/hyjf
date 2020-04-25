/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.user.loginerror;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * 前后台登录失败配置管理，单例模式
 *
 * @author cui
 * @version LoginErrorConfigManager, v0.1 2018/7/13 9:30
 */
public class LoginErrorConfigManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static class SingletonHolder {
        private static final LoginErrorConfigManager instance = new LoginErrorConfigManager();
    }

    public static LoginErrorConfigManager getInstance() {
        return SingletonHolder.instance;
    }

    private LoginErrorConfigManager() {

        boolean isExist = RedisUtils.exists(RedisConstants.LOGIN_LOCK_CONFIG);
        if (!isExist) {

            LoginErrorConfig config = buildDefaultConfig();

            String configString = JSON.toJSONString(config);

            RedisUtils.set(RedisConstants.LOGIN_LOCK_CONFIG, configString);

        }

    }

    public LoginErrorConfig.Config getWebConfig() {

        String configString = RedisUtils.get(RedisConstants.LOGIN_LOCK_CONFIG);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(configString), "未配置登录失败");

        LoginErrorConfig loginErrorConfig = JSON.parseObject(configString, LoginErrorConfig.class);

        return loginErrorConfig.getWebConfig();

    }

    /**
     * 生成默认配置信息
     *
     * @return
     */
    private LoginErrorConfig buildDefaultConfig() {

        LoginErrorConfig config = new LoginErrorConfig();

        LoginErrorConfig.Config defaultConfig = new LoginErrorConfig.Config();

        defaultConfig.setLockLong(24);

        defaultConfig.setMaxLoginErrorNum(5);

        config.setWebConfig(defaultConfig);

        LoginErrorConfig.Config defaultConfig1 = new LoginErrorConfig.Config();
        BeanUtils.copyProperties(defaultConfig, defaultConfig1);
        config.setAdminConfig(defaultConfig1);
        return config;
    }

}
