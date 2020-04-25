package com.hyjf.mqreceiver.hgdatareport.cert.olddata.userinfo;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoMessageHadnle;
import com.hyjf.mybatis.model.auto.CertMobileHash;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.CertSendUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 合规数据上报 CERT 国家互联网应急中心 投资人老数据上报
 * @author sss
 */

@Service
public class CertOldUserServiceImpl extends BaseHgCertReportServiceImpl implements CertOldUserService {
    Logger logger = LoggerFactory.getLogger(CertOldUserServiceImpl.class);

    private String thisMessName = "投资人老数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";


    /**
     * 查询未上报的数据
     *
     * @return
     */
    @Override
    public List<CertSendUser> insertCertUserNotSend() {
        List<CertSendUser> users = usersCustomizeMapper.selectCertUserNotSend();
        return users;
    }

    /**
     * 查询未进行哈希的用户
     *
     * @return
     */
    @Override
    public List<Users> getNotHashUsers() {
        List<Users> users = usersCustomizeMapper.getNotHashUsers();
        return users;
    }

    /**
     * 批量插入手机号哈希值
     *
     * @param mobileHashes
     */
    @Override
    public void insertMobileHashBatch(List<CertMobileHash> mobileHashes) {
        usersCustomizeMapper.insertMobileHashBatch(mobileHashes);
    }

}
