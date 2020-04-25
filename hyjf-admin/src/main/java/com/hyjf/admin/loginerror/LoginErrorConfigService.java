package com.hyjf.admin.loginerror;

import com.hyjf.admin.loginerror.config.LoginErrorConfig;

/**
 * @author cui
 * @version LoginErrorConfigService, v0.1 2018/8/14 14:18
 */
public interface LoginErrorConfigService {

    void saveWebConfig(LoginErrorConfig.Config webConfig);

    void saveAdminConfig(LoginErrorConfig.Config adminConfig);

}
