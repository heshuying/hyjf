package com.hyjf.admin.manager.config.nifaconfig;

import java.io.Serializable;

public class NifaContractTemplateResponseBean implements Serializable {
    private Integer id;

    private String templetNid;

    private String normalDefinition;

    private String prepaymentDefinition;

    private String borrowerPromises;

    private String lenderPromises;

    private String borrowerObligation;

    private String confidentiality;

    private String breachContract;

    private String applicableLaw;

    private String disputeResolution;

    private String otherConditions;

    private String updateUserName;


    private String updateDate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTempletNid() {
        return templetNid;
    }

    public void setTempletNid(String templetNid) {
        this.templetNid = templetNid == null ? null : templetNid.trim();
    }

    public String getNormalDefinition() {
        return normalDefinition;
    }

    public void setNormalDefinition(String normalDefinition) {
        this.normalDefinition = normalDefinition == null ? null : normalDefinition.trim();
    }

    public String getPrepaymentDefinition() {
        return prepaymentDefinition;
    }

    public void setPrepaymentDefinition(String prepaymentDefinition) {
        this.prepaymentDefinition = prepaymentDefinition == null ? null : prepaymentDefinition.trim();
    }

    public String getBorrowerPromises() {
        return borrowerPromises;
    }

    public void setBorrowerPromises(String borrowerPromises) {
        this.borrowerPromises = borrowerPromises == null ? null : borrowerPromises.trim();
    }

    public String getLenderPromises() {
        return lenderPromises;
    }

    public void setLenderPromises(String lenderPromises) {
        this.lenderPromises = lenderPromises == null ? null : lenderPromises.trim();
    }

    public String getBorrowerObligation() {
        return borrowerObligation;
    }

    public void setBorrowerObligation(String borrowerObligation) {
        this.borrowerObligation = borrowerObligation == null ? null : borrowerObligation.trim();
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality == null ? null : confidentiality.trim();
    }

    public String getBreachContract() {
        return breachContract;
    }

    public void setBreachContract(String breachContract) {
        this.breachContract = breachContract == null ? null : breachContract.trim();
    }

    public String getApplicableLaw() {
        return applicableLaw;
    }

    public void setApplicableLaw(String applicableLaw) {
        this.applicableLaw = applicableLaw == null ? null : applicableLaw.trim();
    }

    public String getDisputeResolution() {
        return disputeResolution;
    }

    public void setDisputeResolution(String disputeResolution) {
        this.disputeResolution = disputeResolution == null ? null : disputeResolution.trim();
    }

    public String getOtherConditions() {
        return otherConditions;
    }

    public void setOtherConditions(String otherConditions) {
        this.otherConditions = otherConditions == null ? null : otherConditions.trim();
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}