package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;
/**
 * 渠道账号管理
 * @author Michael
 */
public class AdminUtmReadPermissionsCustomize implements Serializable {
	
    /**
	 * serialVersionUID:序列化id
	 */
		
	private static final long serialVersionUID = 4264420447504028078L;
	
	/*
	 * 渠道账号id
	 */
	private String id;
	/*
	 * 系统用户id
	 */
	private int adminUserId;
	/*
	 * 系统用户名
	 */
	private String adminUserName;
	/*
	 * 系统用户姓名
	 */
	private String adminTrueName;
	/*
	 * 角色名称
	 */
	private String roleName;
	/*
	 * 渠道ids
	 */
	private String utmIds;
	/*
	 * 关键字
	 */
	private String keyCode;
	/*
	 * 添加时间
	 */
	private int createTime;

    /*
	 * 页码
	 */
	protected int limitStart = -1;
	/*
	 * 页码
	 */
    protected int limitEnd = -1;
    
    /*
	 * 查询开始时间
	 */
	private String timeStartSrch;
    /*
	 * 查询结束时间
	 */
    private String timeEndSrch;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAdminUserId() {
		return adminUserId;
	}
	public void setAdminUserId(int adminUserId) {
		this.adminUserId = adminUserId;
	}
	public String getAdminUserName() {
		return adminUserName;
	}
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	public String getAdminTrueName() {
		return adminTrueName;
	}
	public void setAdminTrueName(String adminTrueName) {
		this.adminTrueName = adminTrueName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUtmIds() {
		return utmIds;
	}
	public void setUtmIds(String utmIds) {
		this.utmIds = utmIds;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public int getLimitStart() {
		return limitStart;
	}
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}
	public int getLimitEnd() {
		return limitEnd;
	}
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}
	public String getTimeStartSrch() {
		return timeStartSrch;
	}
	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}
	public String getTimeEndSrch() {
		return timeEndSrch;
	}
	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}
    
}