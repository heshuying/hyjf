package com.hyjf.app.setmsgmail;

import com.hyjf.app.BaseBean;

/**
 * Created by yaoyong on 2017/12/7.
 */
public class MsgMailRequest extends BaseBean{

    //短信开关
    private String smsOpenStatus;

    //邮件开关
    private String emailOpenStatus;

    public String getSmsOpenStatus() {
        return smsOpenStatus;
    }

    public void setSmsOpenStatus(String smsOpenStatus) {
        this.smsOpenStatus = smsOpenStatus;
    }

    public String getEmailOpenStatus() {
        return emailOpenStatus;
    }

    public void setEmailOpenStatus(String emailOpenStatus) {
        this.emailOpenStatus = emailOpenStatus;
    }
}
