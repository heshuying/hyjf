package com.hyjf.bank.service.fdd.fddgeneratecontract;

import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;

/**
 * 法大大合同生成接口类
 */
public interface FddGenerateContractService extends BaseService {

    /**
     * 生成居间服务协议
     * @param bean
     * 合同参数类
     */
     void tenderGenerateContract(FddGenerateContractBean bean) throws Exception;

    /**
     * 计划加入协议
     * @param bean
     * 合同参数类
     */
     void planJoinGenerateContract(FddGenerateContractBean bean);

    /**
     * 债权转让协议
     * @param bean
     * 合同参数类
     */
     void creditGenerateContract(FddGenerateContractBean bean) throws  Exception;

    /**
     * 自动签署异步返回结果处理
     * @param bean
     */
     void updateAutoSignData(DzqzCallBean bean) throws Exception;

    /**
     * 计划债转转让协议
     * @param bean
     */
     void planCreditGenerateContract(FddGenerateContractBean bean);
    
    /**
     * 计划垫付债转转让协议
     * @param bean
     */
    public void planCreditGenerateContractApply(FddGenerateContractBean bean);
    
    /**
     * 根据出借订单号或承接订单号查询协议
     * @param tenderNid
     * 合同参数类
     */
     List<TenderAgreement> selectByExample(String tenderNid);
    public List<TenderAgreement> selectLikeByExample(String tenderNid, String nid);

    /**
     * 更新脱敏后的图片地址
     * @param tenderAgreementID
     * @param iamgeurl
     * @param tmpdfPath
     */
     void updateTenderAgreementImageURL(String tenderAgreementID, String iamgeurl, String tmpdfPath);

    /**
     * 获得合同签署信息
     * @param tenderAgreementID
     * @return
     */
      TenderAgreement getTenderAgrementInfo(String tenderAgreementID);

    /**
     * 根据加入订单号查询计划加入订单
     * @param tenderNid
     * @return
     */
    HjhAccede selectHjhAccede(String tenderNid);

    UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params);

    /**
     * 变更borrowRecover数据
     * @param borrowRecover
     */
     void updateBorrowRecover(BorrowRecover borrowRecover);

    /**
     * 获取出借回款信息
     * @param tenderAgreementID
     * @return
     */
      BorrowRecover selectBorrowRecoverByTenderNid(String tenderAgreementID);

    /**
     * 获取企业借款主体
     * @param borrowNid
     * @return
     */
     BorrowUsers getBorrowUsers(String borrowNid);

    /**
     * 获取个人借款主体
     * @param borrowNid
     * @return
     */
     BorrowManinfo getBorrowManInfo(String borrowNid);
    
    /**
     * 
     * @param bean
     */
    public void creditGenerateContractApply(FddGenerateContractBean bean) throws  Exception;

    /**
     * 成功签署后发送MQ消息
     * @param contract_id
     */
    void sendMQToNifa(String contract_id);
}
