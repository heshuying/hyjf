package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * @author B2
 * @version AppFindSecurityCustomize, v0.1 2018/5/4 10:27
 */

public class AppFindSecurityCustomize implements Serializable {
    private String  StartYear;                      //成立年份
    private String  OperateYear;                  //运营多少年
    private String  CompanyGrade;              //企业评级
    private String  TotalTradeVolume;           //平台累计交易额
    private String  TotalUserIncome;           //平台累计用户收入
    private String  TotalInvester;              //平台累计出借者

    public String getStartYear() {
        return StartYear;
    }

    public void setStartYear(String startYear) {
        StartYear = startYear;
    }

    public String getOperateYear() {
        return OperateYear;
    }

    public void setOperateYear(String operateYear) {
        OperateYear = operateYear;
    }

    public String getCompanyGrade() {
        return CompanyGrade;
    }

    public void setCompanyGrade(String companyGrade) {
        CompanyGrade = companyGrade;
    }

    public String getTotalTradeVolume() {
        return TotalTradeVolume;
    }

    public void setTotalTradeVolume(String totalTradeVolume) {
        TotalTradeVolume = totalTradeVolume;
    }

    public String getTotalUserIncome() {
        return TotalUserIncome;
    }

    public void setTotalUserIncome(String totalUserIncome) {
        TotalUserIncome = totalUserIncome;
    }

    public String getTotalInvester() {
        return TotalInvester;
    }

    public void setTotalInvester(String totalInvester) {
        TotalInvester = totalInvester;
    }
}
