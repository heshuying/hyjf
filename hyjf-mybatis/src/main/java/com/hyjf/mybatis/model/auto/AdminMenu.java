package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class AdminMenu implements Serializable {
    private String menuUuid;

    private String menuPuuid;

    private String menuCtrl;

    private String menuIcon;

    private String menuName;

    private Integer menuSort;

    private String menuUrl;

    private Integer menuHide;

    private String menuTip;

    private String delFlag;

    private String createtime;

    private String updatetime;

    private String createuser;

    private String updateuser;

    private static final long serialVersionUID = 1L;

    public String getMenuUuid() {
        return menuUuid;
    }

    public void setMenuUuid(String menuUuid) {
        this.menuUuid = menuUuid == null ? null : menuUuid.trim();
    }

    public String getMenuPuuid() {
        return menuPuuid;
    }

    public void setMenuPuuid(String menuPuuid) {
        this.menuPuuid = menuPuuid == null ? null : menuPuuid.trim();
    }

    public String getMenuCtrl() {
        return menuCtrl;
    }

    public void setMenuCtrl(String menuCtrl) {
        this.menuCtrl = menuCtrl == null ? null : menuCtrl.trim();
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon == null ? null : menuIcon.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public Integer getMenuHide() {
        return menuHide;
    }

    public void setMenuHide(Integer menuHide) {
        this.menuHide = menuHide;
    }

    public String getMenuTip() {
        return menuTip;
    }

    public void setMenuTip(String menuTip) {
        this.menuTip = menuTip == null ? null : menuTip.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser == null ? null : createuser.trim();
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser == null ? null : updateuser.trim();
    }
}