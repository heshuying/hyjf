
/**
 * Description:用户列表前端显示所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
    
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class NaMiActivityUserListCustomize implements Serializable{
    
    /**
	 * 序列化id
	 */
		
	private static final long serialVersionUID = 7627555508742735666L;
	//用戶id
	private String userId;
    //用戶名
    private String userName;
    //用户手机号
    private String mobile;
    private String foldRatio;
    //推荐人名称
    private String recommendName;

    /** 大区 */
    private String regionName;
    /** 分公司 */
    private String branchName;
    /** 部门 */
    private String departmentName;
 
    /** 部门 */
    private String combotreeSrch;
    /** 部门 */
    private String[] combotreeListSrch;
    /**
     * 构造方法不带参数
     */
        
    public NaMiActivityUserListCustomize() {
        super();
    }
    
    /**
     * 获取用户id
     * userId
     * @return the userId
     */
    
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     * @param userId the userId to set
     */
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名
     * userName
     * @return the userName
     */
    
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     * @param userName the userName to set
     */
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    /**
     * 获取推荐人
     * recommendName
     * @return the recommendName
     */
    
    public String getRecommendName() {
        return recommendName;
    }

    /**
     * 设置推荐人
     * @param recommendName the recommendName to set
     */
    
    public void setRecommendName(String recommendName) {
        this.recommendName = recommendName;
    }

    /**
     * accountStatus
     * @return the accountStatus
     */
    
   
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * regionName
     * @return the regionName
     */
    
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName the regionName to set
     */
    
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * branchName
     * @return the branchName
     */
    
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName the branchName to set
     */
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * departmentName
     * @return the departmentName
     */
    
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFoldRatio() {
        return foldRatio;
    }

    public void setFoldRatio(String foldRatio) {
        this.foldRatio = foldRatio;
    }

    public String getCombotreeSrch() {
        return combotreeSrch;
    }

    public void setCombotreeSrch(String combotreeSrch) {
        this.combotreeSrch = combotreeSrch;
    }

    public String[] getCombotreeListSrch() {
        return combotreeListSrch;
    }

    public void setCombotreeListSrch(String[] combotreeListSrch) {
        this.combotreeListSrch = combotreeListSrch;
    }

    
}

    