package com.hyjf.mqreceiver.hgdatareport.cert.userinfo;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.CertSendUser;

import java.util.List;
import java.util.Map;


/**
 * @author sss
 */

public interface CertUserInfoService extends BaseHgCertReportService {


    /**
     * 组装调用应急中心日志
     * @param user
     * @param borrowNid
     * @return
     */
    JSONArray getSendData(CertSendUser user, String borrowNid , List<CertUser> certUser) throws Exception;

    /**
     * 插入国家互联网应急中心已上送用户表
     * @param certUser
     */
    void insertCertUser(CertUser certUser);

    /**
     * 组装单个用户数据
     * @param item
     * @return
     */
    Map<String,Object> getUserData(CertSendUser item ,String borrowNid,String userStatus) throws Exception;

    /**
     * 根据userId查询需要上报的用户信息
     * @param userId
     * @return
     */
    CertSendUser getCertSendUserByUserId(int userId);

    /**
     * 修改国家互联网应急中心已上送用户表
     * @param certUser
     */
    void updateCertUser(CertUser certUser);

    /**
     * 批量插入上报记录
     * @param certUsers
     */
    void insertCertUserByList(List<CertUser> certUsers);

    /**
     * 根据borrowNid userId查询
     * @param userId
     * @param borrowNid
     * @return
     */
    CertUser getCertUserByUserIdBorrowNid(int userId, String borrowNid);


}