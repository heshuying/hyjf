package com.hyjf.bank.service.fdd.fddcertificateauthority;

import com.hyjf.bank.service.BaseService;

/**
 * 法大大CA认证Service
 * @author liuyang
 */
public interface FddCertificateAuthorityService extends BaseService {
    /**
     * 调用法大大CA认证接口
     * @param userId
     */
    void updateUserCAInfo(Integer userId) throws Exception;
}
