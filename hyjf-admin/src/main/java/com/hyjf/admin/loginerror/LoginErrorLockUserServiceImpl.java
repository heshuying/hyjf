/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.loginerror;

import com.google.common.base.Preconditions;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.mapper.auto.LoginErrorLockUserMapper;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.mapper.customize.loginerror.LoginErrorLockUserCustomizeMapper;
import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.auto.LoginErrorLockUserExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @version LoginErrorLockUserServiceImpl, v0.1 2018/7/13 16:45
 */
@Service
public class LoginErrorLockUserServiceImpl implements LoginErrorLockUserService {

    @Autowired
    private LoginErrorLockUserMapper loginErrorLockUserMapper;

    @Autowired
    private LoginErrorLockUserCustomizeMapper loginErrorLockUserCustomizeMapper;

    @Autowired
    private UsersMapper usersMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int countRecordTotal(Map<String, Object> parameterMap) {
        return loginErrorLockUserCustomizeMapper.countRecordTotal(parameterMap);
    }

    @Override
    public List<LoginErrorLockUser> getRecordList(Map<String, Object> parameterMap) {
        return loginErrorLockUserCustomizeMapper.getRecordList(parameterMap);
    }

    @Override
    public void updateUnlockBackUser(int id, String loginUserId) {

        updateUnlockUser(id, loginUserId, false);

    }


    @Override
    public void updateUnlockFrontUser(int id, String loginUserId) {

        updateUnlockUser(id, loginUserId, true);

    }

    @Override
    public Integer getLockedUserId(String username) {

        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(username);
        UsersExample.Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
        example1.or(c2);
        List<Users> lstUser = usersMapper.selectByExample(example1);
        Preconditions.checkArgument(lstUser.size() == 1);
        return lstUser.get(0).getUserId();
    }

    private void updateUnlockUser(int id, String loginUserId, boolean isFront) {
        LoginErrorLockUser user = loginErrorLockUserMapper.selectByPrimaryKey(id);

        if (user == null) {
            logger.warn("不存在ID=【{}】的锁定用户", id);
            return;
        }

        String key = user.getUsername();

        if (isFront) {
            key = String.valueOf(getLockedUserId(user.getUsername()));
        }

        String redisKeyPrefix = isFront ? RedisConstants.PASSWORD_ERR_COUNT_ALL : RedisConstants.PASSWORD_ERR_COUNT_ADMIN;

        RedisUtils.del(redisKeyPrefix + key);

        user.setUnlocked(1);
        user.setUnlockTime(new Date());
        user.setOperator(Integer.parseInt(loginUserId));

        LoginErrorLockUserExample example = new LoginErrorLockUserExample();

        example.or().andIdEqualTo(id);

        int result = loginErrorLockUserMapper.updateByExample(user, example);

        String logInfo = isFront ? "解除前台锁定用户【{}】成功！更新记录个数【{}】" : "解除后台锁定用户【{}】成功！更新记录个数【{}】";
        logger.info(logInfo, user.getUsername(), result);
    }

    @Override
    public void insertLockUser(LoginErrorLockUser loginErrorLockUser) {
        loginErrorLockUserMapper.insertSelective(loginErrorLockUser);
    }
}
