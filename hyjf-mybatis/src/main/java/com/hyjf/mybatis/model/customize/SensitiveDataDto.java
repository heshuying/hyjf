/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author fp
 * @version SensitiveDataDto, v0.1 2018/3/29 10:24
 * 敏感数据dto
 */
public class SensitiveDataDto implements Serializable {

    private static final long serialVersionUID = -3150777219978899072L;

    private Integer userId;
    //手机号
    private String mobile;
    //用户名
    private String userName;
    //证件号
    private String idCard;
    //银行卡号
    private String bankNo;
    //注册时间
    private String registerDate;
    //开户时间
    private String openAccountDate;
    //充值总额
    private String rechargeAmount;
    //提现总额
    private String withDrawAmount;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getOpenAccountDate() {
        return openAccountDate;
    }

    public void setOpenAccountDate(String openAccountDate) {
        this.openAccountDate = openAccountDate;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getWithDrawAmount() {
        return withDrawAmount;
    }

    public void setWithDrawAmount(String withDrawAmount) {
        this.withDrawAmount = withDrawAmount;
    }
}
