package com.hyjf.bank.service.fdd.fdduserinfochange;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.CertificateAuthority;

/**
 * 法大大客户信息修改Service
 *
 * @author liuyang
 */
public interface FddUserInfoChangeService extends BaseService {

    /**
     * 请求法大大客户信息修改接口,更新用户CA认证相关信息
     * @param userId
     */
    void updateUserCAInfo(Integer userId) throws Exception;
}
