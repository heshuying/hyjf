package com.hyjf.wechat.controller.user.credit.entity;

import java.io.Serializable;

/**
 * 
 * 债转协议Bean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月5日
 * @see 下午4:22:39
 */
public class WxTenderCreditAssignedBean implements Serializable {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 4231518200185061869L;

    /**
     * 原标nid
     */
    private String bidNid;

    /**
     * 债转标号
     */
    private String creditNid;

    /**
     * 债转投标单号
     */
    private String creditTenderNid;

    /**
     * 认购单号
     */
    private String assignNid;

    public String getBidNid() {
        return bidNid;
    }

    public void setBidNid(String bidNid) {
        this.bidNid = bidNid;
    }

    public String getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }

    public String getCreditTenderNid() {
        return creditTenderNid;
    }

    public void setCreditTenderNid(String creditTenderNid) {
        this.creditTenderNid = creditTenderNid;
    }

    public String getAssignNid() {
        return assignNid;
    }

    public void setAssignNid(String assignNid) {
        this.assignNid = assignNid;
    }

}
