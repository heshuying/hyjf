package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class OpenAccountEnquiryBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	// 用戶id
	public String userid;
	// 用户手机号
	public String phone;
	// 用户身份证号
    public String idcard;
	// 开户状态
	public String accountStatus;
	// 开户时间
    public String regTimeEnd;
    //电子账号
    public  String accountString;
    //开户平台
    public int platform;
    //用户姓名
    public String username;
  //用户姓名
    public String mobile;

    public String accounts;

    public String roleId;

    public String name;

    public String idNo;

    public String account;

    
    public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getIdcard() {
        return idcard;
    }
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    public String getRegTimeEnd() {
        return regTimeEnd;
    }
    public void setRegTimeEnd(String regTimeEnd) {
        this.regTimeEnd = regTimeEnd;
    }
    public String getAccountString() {
        return accountString;
    }
    public void setAccountString(String accountString) {
        this.accountString = accountString;
    }
    public int getPlatform() {
        return platform;
    }
    public void setPlatform(int platform) {
        this.platform = platform;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
