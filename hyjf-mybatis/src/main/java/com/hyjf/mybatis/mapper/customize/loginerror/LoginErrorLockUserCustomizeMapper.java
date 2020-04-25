package com.hyjf.mybatis.mapper.customize.loginerror;

import com.hyjf.mybatis.model.auto.LoginErrorLockUser;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @version LoginErrorLockUserCustomizeMapper, v0.1 2018/7/16 10:52
 */
public interface LoginErrorLockUserCustomizeMapper {

    int countRecordTotal(Map<String, Object> parameterMap);

    List<LoginErrorLockUser> getRecordList(Map<String, Object> parameterMap);
}
