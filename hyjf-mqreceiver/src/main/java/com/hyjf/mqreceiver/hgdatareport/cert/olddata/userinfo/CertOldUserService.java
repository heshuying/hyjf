package com.hyjf.mqreceiver.hgdatareport.cert.olddata.userinfo;

import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertMobileHash;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.CertSendUser;

import java.util.List;


/**
 * 合规数据上报 CERT 国家互联网应急中心 投资人老数据上报
 * @author sss
 */

public interface CertOldUserService extends BaseHgCertReportService {

    /**
     * 查询未上报的数据
     * @return
     */
    List<CertSendUser> insertCertUserNotSend();

    /**
     * 查询未进行哈希的用户
     * @return
     */
    List<Users> getNotHashUsers();

    /**
     * 批量插入手机号哈希值
     * @param mobileHashes
     */
    void insertMobileHashBatch(List<CertMobileHash> mobileHashes);
}