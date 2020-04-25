/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */

public class InviteSevenCustomize implements Serializable {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /** 被邀请人用户id*/
    private int useridInvited;
    /** 被邀请人用户名*/
    private String usernameInvited;
    /** 邀请人用户id*/
    private int userid;
    /** 邀请人用户名*/
    private String username;
    /** 邀请人真实姓名*/
    private String truename;
    /** 邀请人手机号*/
    private String mobile;
    /** 被邀请人手机号*/
    private String mobileInvited;
    /** 被邀请人首投金额*/
    private BigDecimal investSum;
    /** 被邀请人首投日期*/
    private String investTime;
    /** 债转首次出借时间*/
    private String creditInvestTime;
    /** 注册时间*/
    private String regTime;
    
    public int getUseridInvited() {
        return useridInvited;
    }
    public void setUseridInvited(int useridInvited) {
        this.useridInvited = useridInvited;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public BigDecimal getInvestSum() {
        return investSum;
    }
    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }
    public String getInvestTime() {
        return investTime;
    }
    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }
    public String getUsernameInvited() {
        return usernameInvited;
    }
    public void setUsernameInvited(String usernameInvited) {
        this.usernameInvited = usernameInvited;
    }
    public String getRegTime() {
        return regTime;
    }
    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
    public String getMobileInvited() {
        return mobileInvited;
    }
    public void setMobileInvited(String mobileInvited) {
        this.mobileInvited = mobileInvited;
    }
    public String getCreditInvestTime() {
        return creditInvestTime;
    }
    public void setCreditInvestTime(String creditInvestTime) {
        this.creditInvestTime = creditInvestTime;
    }
    public String getTruename() {
        return truename;
    }
    public void setTruename(String truename) {
        this.truename = truename;
    }
    
    
}
