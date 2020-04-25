package com.hyjf.api.server.user.openaccountplus;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.BindUsers;

/**
 * Created by yaoyong on 2017/11/30.
 */
public interface BindUserService extends BaseService {

	Boolean bindUser(Integer userId, String bindUniqueId, String platForm);


    /**
     * 根据第三方用户id查询绑定关系
     * @param bindUniqueId
     * @param bind_platform_id
     * @return
     */
    BindUsers getUsersByUniqueId(Long bindUniqueId, Integer bind_platform_id);

    /**
     * 获取用户电子账号
     * @param userId
     * @return
     */
    String getAccountId(Integer userId);

    /**
     * 获取银联行号
     * @param userId
     * @return
     */
    String getPayAllianceCode(Integer userId);

    /**
     * 获取自动投标授权状态
     * @param userId
     * @return
     */
    String getAutoInvesStatus(Integer userId);

    String getTrueName(String idNo);
}
