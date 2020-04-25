package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class AccountChinapnr implements Serializable {
    private Integer id;

    private Integer userId;

    private String chinapnrUsrid;

    private Long chinapnrUsrcustid;

    private String addtime;

    private String addip;

    private Integer isok;

    private Integer eggsIsok;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getChinapnrUsrid() {
        return chinapnrUsrid;
    }

    public void setChinapnrUsrid(String chinapnrUsrid) {
        this.chinapnrUsrid = chinapnrUsrid == null ? null : chinapnrUsrid.trim();
    }

    public Long getChinapnrUsrcustid() {
        return chinapnrUsrcustid;
    }

    public void setChinapnrUsrcustid(Long chinapnrUsrcustid) {
        this.chinapnrUsrcustid = chinapnrUsrcustid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? null : addtime.trim();
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip == null ? null : addip.trim();
    }

    public Integer getIsok() {
        return isok;
    }

    public void setIsok(Integer isok) {
        this.isok = isok;
    }

    public Integer getEggsIsok() {
        return eggsIsok;
    }

    public void setEggsIsok(Integer eggsIsok) {
        this.eggsIsok = eggsIsok;
    }
}