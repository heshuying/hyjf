/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.nifaconfig;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.NifaContractTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 * 合同模板条款
 */
public class ContractTemplateBean implements Serializable {
    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;

    private List<NifaContractTemplateResponseBean> recordList;

    private String ids;
    //模版编号
    private String templetNid;
    //正常还款定义
    private String normalDefinition;
    //提前还款定义
    private String prepaymentDefinition;
    //借款人承诺与保证
    private String borrowerPromises;
    //出借人承诺与保证
    private String lenderPromises;
    //借款人还款义务
    private String borrowerObligation;
    //保密
    private String confidentiality;
    //违约
    private String breachContract;
    //法律适用
    private String applicableLaw;
    //争议解决
    private String disputeResolution;
    //其他条款
    private String otherConditions;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public List<NifaContractTemplateResponseBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<NifaContractTemplateResponseBean> recordList) {
        this.recordList = recordList;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTempletNid() {
        return templetNid;
    }

    public void setTempletNid(String templetNid) {
        this.templetNid = templetNid;
    }

    public String getNormalDefinition() {
        return normalDefinition;
    }

    public void setNormalDefinition(String normalDefinition) {
        this.normalDefinition = normalDefinition;
    }

    public String getPrepaymentDefinition() {
        return prepaymentDefinition;
    }

    public void setPrepaymentDefinition(String prepaymentDefinition) {
        this.prepaymentDefinition = prepaymentDefinition;
    }

    public String getBorrowerPromises() {
        return borrowerPromises;
    }

    public void setBorrowerPromises(String borrowerPromises) {
        this.borrowerPromises = borrowerPromises;
    }

    public String getLenderPromises() {
        return lenderPromises;
    }

    public void setLenderPromises(String lenderPromises) {
        this.lenderPromises = lenderPromises;
    }

    public String getBorrowerObligation() {
        return borrowerObligation;
    }

    public void setBorrowerObligation(String borrowerObligation) {
        this.borrowerObligation = borrowerObligation;
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getBreachContract() {
        return breachContract;
    }

    public void setBreachContract(String breachContract) {
        this.breachContract = breachContract;
    }

    public String getApplicableLaw() {
        return applicableLaw;
    }

    public void setApplicableLaw(String applicableLaw) {
        this.applicableLaw = applicableLaw;
    }

    public String getDisputeResolution() {
        return disputeResolution;
    }

    public void setDisputeResolution(String disputeResolution) {
        this.disputeResolution = disputeResolution;
    }

    public String getOtherConditions() {
        return otherConditions;
    }

    public void setOtherConditions(String otherConditions) {
        this.otherConditions = otherConditions;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }
}
