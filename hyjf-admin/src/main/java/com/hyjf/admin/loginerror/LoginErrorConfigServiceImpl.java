/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.loginerror;

import com.hyjf.admin.loginerror.config.LoginErrorConfig;
import com.hyjf.admin.loginerror.config.LoginErrorConfigManager;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.mybatis.mapper.auto.LoginErrorLockUserMapper;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.mapper.customize.AdminSystemMapper;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.auto.LoginErrorLockUserExample;
import com.hyjf.mybatis.model.auto.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author cui
 * @version LoginErrorConfigServiceImpl, v0.1 2018/8/14 14:21
 */
@Service
public class LoginErrorConfigServiceImpl implements LoginErrorConfigService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private LoginErrorLockUserMapper mapper;

    @Autowired
    private AdminSystemMapper adminSystemMapper;

    @Override
    public void saveWebConfig(LoginErrorConfig.Config webConfig) {
        int oldMaxNum = LoginErrorConfigManager.getInstance().getWebConfig().getMaxLoginErrorNum();

        LoginErrorConfigManager.getInstance().setWebConfig(webConfig);

        if (webConfig.getMaxLoginErrorNum() > oldMaxNum) {
            //设置出错次数比原来的大，清除锁定用户记录
            LoginErrorLockUserExample example = new LoginErrorLockUserExample();
            example.or().andFrontEqualTo(1);
            mapper.deleteByExample(example);
        } else if (webConfig.getMaxLoginErrorNum() < oldMaxNum) {
            //设置出错次数比原来值小，遍历redis中，判断是否需要加入到锁定列表
            Set<String> frontKeys = getLoginErrorRedisKey(RedisConstants.PASSWORD_ERR_COUNT_ALL);
            for (String key : frontKeys) {
                String loginErrorNum = RedisUtils.get(key);
                Integer intNum = Integer.parseInt(loginErrorNum);
                if (intNum >= webConfig.getMaxLoginErrorNum() && intNum < oldMaxNum) {

                    String userId = key.substring(key.lastIndexOf("_") + 1);
                    Users users = usersMapper.selectByPrimaryKey(Integer.parseInt(userId));

                    LoginErrorLockUser lockUser = new LoginErrorLockUser();
                    lockUser.setFront(1);
                    lockUser.setUnlocked(0);
                    lockUser.setUserid(users.getUserId());
                    lockUser.setUsername(users.getUsername());
                    lockUser.setMobile(users.getMobile());
                    lockUser.setLockTime(new Date());
                    Integer loginLockTime = LoginErrorConfigManager.getInstance().getAdminConfig().getLockLong();
                    lockUser.setUnlockTime(DateUtils.nowDateAddDate(loginLockTime));

                    mapper.insert(lockUser);

                    logger.info("因为配置出错次数设置比原来小了，前台锁定用户【{}】", users.getUsername());

                }
            }
        }
    }

    @Override
    public void saveAdminConfig(LoginErrorConfig.Config adminConfig) {
        int oldMaxNum = LoginErrorConfigManager.getInstance().getAdminConfig().getMaxLoginErrorNum();

        LoginErrorConfigManager.getInstance().setAdminConfig(adminConfig);

        if (adminConfig.getMaxLoginErrorNum() > oldMaxNum) {
            LoginErrorLockUserExample example = new LoginErrorLockUserExample();
            example.or().andFrontEqualTo(0);
            mapper.deleteByExample(example);
        } else if (adminConfig.getMaxLoginErrorNum() < oldMaxNum) {
            //设置出错次数比原来值小，遍历redis中，判断是否需要加入到锁定列表
            Set<String> backKey = getLoginErrorRedisKey(RedisConstants.PASSWORD_ERR_COUNT_ADMIN);
            for (String key : backKey) {
                String loginErrorNum = RedisUtils.get(key);
                Integer intNum = Integer.parseInt(loginErrorNum);
                if (intNum >= adminConfig.getMaxLoginErrorNum() && intNum < oldMaxNum) {
                    String userName = key.substring(key.lastIndexOf("_") + 1);
                    List<Admin> lstUsers = adminSystemMapper.selectByUsername(userName);
                    if (lstUsers.size() == 1) {
                        Admin users = lstUsers.get(0);
                        LoginErrorLockUser lockUser = new LoginErrorLockUser();
                        lockUser.setFront(0);
                        lockUser.setUnlocked(0);
                        lockUser.setUserid(users.getId());
                        lockUser.setUsername(users.getUsername());
                        lockUser.setMobile(users.getMobile());
                        lockUser.setLockTime(new Date());
                        Integer loginLockTime = LoginErrorConfigManager.getInstance().getAdminConfig().getLockLong();
                        lockUser.setUnlockTime(DateUtils.nowDateAddDate(loginLockTime));
                        mapper.insert(lockUser);
                        logger.info("因为配置出错次数设置比原来小了，后台锁定用户【{}】", users.getUsername());
                    }
                }
            }
        }
    }

    /**
     * @param redisKeyPrifix
     * @return
     */
    public Set<String> getLoginErrorRedisKey(String redisKeyPrifix) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = RedisUtils.getPool();
            jedis = pool.getResource();
            return jedis.keys(redisKeyPrifix + "*");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            RedisUtils.returnResource(pool, jedis);
        }
        throw new IllegalArgumentException("获取出错次数失败");
    }
}
