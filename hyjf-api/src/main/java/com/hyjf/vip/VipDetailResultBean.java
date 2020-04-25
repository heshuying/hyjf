package com.hyjf.vip;

import java.io.Serializable;

import com.hyjf.base.bean.BaseResultBean;

public class VipDetailResultBean extends BaseResultBean implements Serializable  {


	private static final long serialVersionUID = 2569482809922162226L;
	//用户名
	private String username;
	//用户头像
	private String iconUrl;
	//vip等级名称	
	private String vipName;
	//当前等级vip升级经验上限
	private String vipValueUp;
	//是否是最高等级
	private String ifVipLevelUp;
	//升级备注
	private String vipUpgradeRemarks;
	//vip到期时间
	private String vipExpDate;
	//当前用户v值
	private String vipValue;
	//当前用户vip等级
    private String vipLevel;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public String getVipName() {
        return vipName;
    }
    public void setVipName(String vipName) {
        this.vipName = vipName;
    }
    public String getVipValueUp() {
        return vipValueUp;
    }
    public void setVipValueUp(String vipValueUp) {
        this.vipValueUp = vipValueUp;
    }
    public String getIfVipLevelUp() {
        return ifVipLevelUp;
    }
    public void setIfVipLevelUp(String ifVipLevelUp) {
        this.ifVipLevelUp = ifVipLevelUp;
    }
    public String getVipUpgradeRemarks() {
        return vipUpgradeRemarks;
    }
    public void setVipUpgradeRemarks(String vipUpgradeRemarks) {
        this.vipUpgradeRemarks = vipUpgradeRemarks;
    }
    public String getVipExpDate() {
        return vipExpDate;
    }
    public void setVipExpDate(String vipExpDate) {
        this.vipExpDate = vipExpDate;
    }
    public String getVipValue() {
        return vipValue;
    }
    public void setVipValue(String vipValue) {
        this.vipValue = vipValue;
    }
    public String getVipLevel() {
        return vipLevel;
    }
    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }
    
    
}
