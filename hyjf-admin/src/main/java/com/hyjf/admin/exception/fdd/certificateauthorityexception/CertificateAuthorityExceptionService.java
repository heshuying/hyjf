package com.hyjf.admin.exception.fdd.certificateauthorityexception;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.CertificateAuthority;

import java.util.List;

/**
 * CA认证异常处理Service
 * @author liuyang
 */
public interface CertificateAuthorityExceptionService extends BaseService {
    /**
     * 检索CA异常列表数量
     * @param form
     * @return
     */
    Integer countCAExceptionList(CertificateAuthorityExceptionBean form);

    /**
     * 检索CA异常列表
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    List<CertificateAuthority> getCAExceptionList(CertificateAuthorityExceptionBean form,int limitStart,int limitEnd);

    /**
     * 发送CA认证MQ
     * @param userId
     */
    void updateUserCAMQ(String userId);
}
