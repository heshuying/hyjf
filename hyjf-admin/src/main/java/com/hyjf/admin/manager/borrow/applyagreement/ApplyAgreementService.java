package com.hyjf.admin.manager.borrow.applyagreement;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.mybatis.model.auto.ApplyAgreement;
import com.hyjf.mybatis.model.auto.ApplyAgreementExample;
import com.hyjf.mybatis.model.auto.ApplyAgreementInfo;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepay;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.customize.admin.BorrowRepayAgreementCustomize;

public interface ApplyAgreementService extends BaseService {

    /**
     *  垫付协议申请数目
     * 
     * @return
     */
    Integer countApplyAgreement(ApplyAgreementExample applyAgreementExample);

    /**
     * 还款信息 列表
     * 
     * @return
     */
    List<ApplyAgreement> selectApplyAgreement(ApplyAgreementExample applyAgreementExample);
    
    /**
     * 列表删除
     * 
     * @param record
     */
     void deleteRecord(List<Integer> recordList);
    
    List<TenderAgreement> selectTenderAgreementByTenderNid(String tenderNid);
    
    
    /**
     *  还款信息-分期数目
     * 
     * @return
     */
    Long countBorrowRepay(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize);
    
    /**
     * 还款信息 列表
     * 
     * @return
     */
    List<BorrowRepayAgreementCustomize> selectBorrowRepay(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize);
    
    /**
     *  垫付协议申请数目
     * 
     * @return
     */
    Long countBorrowRepayPlan(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize);
    
    /**
     * 还款信息-分期 列表
     * 
     * @return
     */
    List<BorrowRepayAgreementCustomize> selectBorrowRepayPlan(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize);
    /**
     * 获取计划的债转信息
     * @param nid
     * @return
     */
    List<HjhDebtCreditRepay> selectHjhDebtCreditRepayByExample(String nid,int repay_period);
    
    /**
     * 新增
     * @param record
     * @return
     */
    int insert(ApplyAgreement record);
    
    int updateByPrimaryKey(ApplyAgreement record);
    
    /**
     * 保存垫付协议申请-协议生成详情
     * @param record
     * @return
     */
    int saveApplyAgreementInfo(ApplyAgreementInfo record);
    
    /**
     * 直投债转信息
     * @param nid
     * @return
     */
    List<CreditRepay> selectCreditRepayByExample(String nid,int repay_period);
   // List<CreditRepay> list = this.creditRepayMapper.selectByExample(creditRepayExample creditRepayExample);
    
    /**全部转让债转参数集合*/
    JSONObject getAllcreditParamter(CreditRepay creditRepay,FddGenerateContractBean bean,Borrow borrow);
    /**部分转让债转参数集合*/
    JSONObject getPartcreditParamter(CreditRepay creditRepay,FddGenerateContractBean bean,Borrow borrow,int assignCapital,int assignPay);
    
    
    /**全部转让债转参数集合*/
    JSONObject getAllcreditParamterHjh(HjhDebtCreditRepay hjhDebtCreditRepay,FddGenerateContractBean bean,Borrow borrow);
    /**部分转让债转参数集合*/
    JSONObject getPartcreditParamterHjh(HjhDebtCreditRepay hjhDebtCreditRepay,FddGenerateContractBean bean,Borrow borrow,int assignCapital,int assignPay);
    /**非转让债转参数集合-分期*/
    JSONObject getNocreditParamterPlan(BorrowRecoverPlan borrowRecoverPlan,FddGenerateContractBean bean,Borrow borrow);
    
    /**非转让债转参数集合-不分期*/
    JSONObject getNocreditParamter(BorrowRecover borrowRecover,FddGenerateContractBean bean,Borrow borrow);

}
