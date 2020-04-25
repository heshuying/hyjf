/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class BorrowLogCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	
	
	/**
     * 排序
     */
    private String sort;
    /**
     * 排序列
     */
    private String col;
    

	/**
	 * 借款编码
	 */
	private String borrowNid;
	
	/**
     * 状态
     */
    private String borrowStatus;
    
	/**
	 * 修改类型-创建标的-新增-修改-删除
	 */
	private String type;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 创建用户
	 */
	private String createUserName;
	
	/**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;
    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;
    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;
    
    private int delFlg;
    
    
    
    
    
    public int getDelFlg() {
        return delFlg;
    }
    public void setDelFlg(int delFlg) {
        this.delFlg = delFlg;
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
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getCol() {
        return col;
    }
    public void setCol(String col) {
        this.col = col;
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
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
    public String getBorrowStatus() {
        return borrowStatus;
    }
    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateUserName() {
        return createUserName;
    }
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

	
}
