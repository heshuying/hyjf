package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class AdminMenuPermissions implements Serializable {
    private String menuId;

    private String permissionId;

    private static final long serialVersionUID = 1L;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
    }
}