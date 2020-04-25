/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 *
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by :
 */

package com.hyjf.mqreceiver.hgdatareport;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;
import com.hyjf.mongo.hgdatareport.base.BaseHgDataReportEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.UserInfoSHA256Entity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.nifa.NifaTenderUserInfoCustomize;

import java.util.List;

import java.util.Map;

/**
 * @author liubin
 */

public interface BaseHgDateReportService extends BaseService {

    /**
     * 调用webservice接口并返回数据
     *
     * @param methodName
     * @param encmsg
     * @return
     */
    String webService(String methodName, String encmsg);

    /**
     * 根据借款主体名称获取CA认证编号
     *
     * @param name
     * @return
     */
    CertificateAuthority selectCAInfoByUsername(String name,String cardNo);

    /**
     * 根据平台借款等级变换中互金借款等级
     *
     * @param borrowLevel
     * @return
     */
    Integer getBorrowLevel(String borrowLevel);

    /**
     * 根据借款人获取借款人银行卡信息
     *
     * @param userId
     * @return
     */
    BankCard selectBankCardByUserId(Integer userId);

    /**
     * 判断到期还款还是分期还款
     * @param borrowStyle
     * @return
     */
    boolean isMonth(String borrowStyle);

    /**
     * 根据借款编号获取还款计划 到期还款
     *
     * @param borrowNid
     * @return
     */
    BorrowRepay selectBorrowRepayByBorrowNid(String borrowNid);

    /**
     * 根据借款编号获取还款计划 分期还款
     *
     * @param borrowNid
     * @return
     */
    List<BorrowRepayPlan> selectBorrowRepayPlanByBorrowNid(String borrowNid);

    /**
     * 查询该借款数据是否上报完成
     *
     * @param borrowNid
     * @param message
     * @return
     */
    NifaBorrowInfoEntity selectNifaBorrowInfoByBorrowNid(String borrowNid, String message);

    /**
     * 获取散标债转信息表
     *
     * @param creditNid
     * @return
     */
    BorrowCredit selectBorrowCreditByCreditNid(String creditNid);

    /**
     * 获取散标债转承接人承接信息
     *
     * @param creditNid
     * @return
     */
    List<CreditTender> selectCreditTenderByCreditNid(String creditNid);

    /**
     * 根据债转编号查询承接人承接记录
     *
     * @param creditNid
     * @return
     */
    List<HjhDebtCreditTender> selectHjhDebtCreditTenderByCreditNid(String creditNid);

    /***
     * 根据债转编号获取债转信息
     *
     * @param creditNid
     * @return
     */
    HjhDebtCredit selectHjhDebtCreditByCreditNid(String creditNid);

    /**
     * 获取上一期订单的债转编号
     *
     * @param planOrderId
     * @return
     */
    HjhDebtCreditTender selectHjhDebtCreditTenderByPlanOrderId(String planOrderId);

    /**
     * 上报北互金
     * @param methodName
     * @param data
     * @param <T>
     * @return
     */
    <T extends BaseHgDataReportEntity> T reportData(String methodName, T data);


    /**
     * 获取用户索引信息
     *
     * @param userId
     * @param trueName
     * @param idCard
     * @return
     */
    UserInfoSHA256Entity selectUserIdToSHA256(Integer userId,String trueName,String idCard);

    /**
     * 根据借款编号查询该借款下所有投资人的相关信息
     *
     * @param borrowNid
     * @return
     */
    List<NifaTenderUserInfoCustomize> selectTenderUserInfoByBorrowNid(String borrowNid);

    /**
     * 获取企业注册所属地区编号
     *
     * @param code
     * @return
     */
    String getBorrowUsersArea(String code);
}

	