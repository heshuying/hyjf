package com.hyjf.admin.manager.user.certificateauthority;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.CertificateAuthority;

import java.util.List;

/**
 * CA认证认证记录
 *
 * @author liuyang
 */
public interface FddCertificateAuthorityService extends BaseService {
    /**
     * CA认证记录件数
     *
     * @param form
     * @return
     */
    Integer countCertificateAuthorityList(FddCertificateAuthorityBean form);

    /**
     * CA认证记录列表
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    List<CertificateAuthority> getCertificateAuthorityList(FddCertificateAuthorityBean form, int limitStart, int limitEnd);
}
