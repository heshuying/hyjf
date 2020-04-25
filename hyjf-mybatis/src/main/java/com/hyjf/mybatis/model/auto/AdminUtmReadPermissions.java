package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class AdminUtmReadPermissions implements Serializable {
    private Integer id;

    private Integer adminUserId;

    private String adminUserName;

    private String utmIds;

    private String keyCode;

    private Integer createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName == null ? null : adminUserName.trim();
    }

    public String getUtmIds() {
        return utmIds;
    }

    public void setUtmIds(String utmIds) {
        this.utmIds = utmIds == null ? null : utmIds.trim();
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode == null ? null : keyCode.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}