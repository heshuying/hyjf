package com.hyjf.batch.fdd;

import com.hyjf.bank.service.fdd.fddcertificateauthority.FddCertificateAuthorityBean;
import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Users;

import java.util.List;

/**
 * 法大大批量做CA认证处理Service
 *
 * @author liuyang
 */
public interface BatchFddCertificateAuthorityService extends BaseService {
    /**
     * 检索已开户未做CA认证的用户
     *
     * @return
     */
    List<Users> selectCAUsersList();

    /**
     * 发送消息队列
     * @param bean
     * @param routingkeyCertificateAuthority
     */
   void sendToMQ(FddCertificateAuthorityBean bean, String routingkeyCertificateAuthority);
}
