package com.hyjf.admin.loginerror;

import com.hyjf.mybatis.model.auto.LoginErrorLockUser;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @version LoginErrorLockUserService, v0.1 2018/7/13 16:44
 */
public interface LoginErrorLockUserService {

    int countRecordTotal(Map<String, Object> parameterMap);

    List<LoginErrorLockUser> getRecordList(Map<String, Object> parameterMap);

    void updateUnlockBackUser(int id, String loginUserId);

    void updateUnlockFrontUser(int id, String loginUserId);

    Integer getLockedUserId(String username);

    void insertLockUser(LoginErrorLockUser loginErrorLockUser);
}
