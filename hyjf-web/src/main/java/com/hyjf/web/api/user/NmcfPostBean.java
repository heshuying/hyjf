package com.hyjf.web.api.user;

public class NmcfPostBean {
    /** 当前时间戳 */
    private String timestamp;
    /** 机构编号 */
    private String instCode;
    /** 第三方用户ID */
    private String userId;
    /** 跳转URL */
    private String retUrl;
    /** 标的编号 */
    private String borrowNid;
    /** 签名 */
    private String chkValue;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRetUrl() {
        return retUrl;
    }

    public void setRetUrl(String retUrl) {
        this.retUrl = retUrl;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getChkValue() {
        return chkValue;
    }

    public void setChkValue(String chkValue) {
        this.chkValue = chkValue;
    }

}
