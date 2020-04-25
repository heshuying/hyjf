package com.hyjf.admin.manager.config.subconfig;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;

public class SubConfigBean extends SubCommissionListConfigCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 8853785949572253967L;

    /** 用户名 */
    private String userNameSrch;
    
    /** 姓名 */
	String trueNameSrch;
	
	/** 角色 */
	String roleNameSrch;
	
    /** 用户类型 */
    private String userTypeSrch;

    /** 江西银行电子账号 */
    private String accountSrch;

    /** 用户状态 */
    private String statusSrch;
    
    /** 添加时间 */
    private String recieveTimeStartSrch;
    
    
    /** 添加时间 */
    private String recieveTimeEndSrch;
    
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
    
    /**
	 * 列表
	 */
	private List<SubCommissionListConfigCustomize> recordList;

    public int getPaginatorPage() {
        return paginatorPage == 0 ? 1 : paginatorPage;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getTrueNameSrch() {
		return trueNameSrch;
	}

	public void setTrueNameSrch(String trueNameSrch) {
		this.trueNameSrch = trueNameSrch;
	}

	public String getRoleNameSrch() {
		return roleNameSrch;
	}

	public void setRoleNameSrch(String roleNameSrch) {
		this.roleNameSrch = roleNameSrch;
	}

	public String getUserTypeSrch() {
		return userTypeSrch;
	}

	public void setUserTypeSrch(String userTypeSrch) {
		this.userTypeSrch = userTypeSrch;
	}

	public String getAccountSrch() {
		return accountSrch;
	}

	public void setAccountSrch(String accountSrch) {
		this.accountSrch = accountSrch;
	}

	public String getStatusSrch() {
		return statusSrch;
	}

	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SubCommissionListConfigCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<SubCommissionListConfigCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getRecieveTimeStartSrch() {
		return recieveTimeStartSrch;
	}

	public void setRecieveTimeStartSrch(String recieveTimeStartSrch) {
		this.recieveTimeStartSrch = recieveTimeStartSrch;
	}

	public String getRecieveTimeEndSrch() {
		return recieveTimeEndSrch;
	}

	public void setRecieveTimeEndSrch(String recieveTimeEndSrch) {
		this.recieveTimeEndSrch = recieveTimeEndSrch;
	}
	

}
