package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.mybatis.model.auto.Account;

public class ReturncashCustomize extends Account implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** 用户名 */
    private String username;
    /** 手续费用 */
    private String feeRate;
    /** 可返金额 */
    private String maybackmoney;
    /** 金额 */
    private BigDecimal money;
    /** 备注 */
    private String note;
    /** 添加时间 */
    private String addtime;
    /** 状态 */
    private String status;
    /** 操作者 */
    private String operator;
    /** IP地址 */
    private String ip;
    /** 返现类型 */
    private String type;
    /** 大区 */
    private String regionName;
    /** 分公司 */
    private String branchName;
    /** 部门 */
    private String departmentName;

    // 查询用变量
    /** 用户名 */
    private String usernameSrch;
    /** 可用金额(最小) */
    private String balanceMinSrch;
    /** 可用金额(最大) */
    private String balanceMaxSrch;
    /** 添加时间(开始) */
    private String addtimeStartSrch;
    /** 添加时间(结束) */
    private String addtimeEndSrch;
    /** 操作者 */
    private String operatorSrch;
    /** 部门 */
    private String combotreeSrch;
    /** 部门 */
    private String[] combotreeListSrch;
    /** 翻页开始 */
    protected int limitStart = -1;
    /** 翻页结束 */
    protected int limitEnd = -1;

    // public String getAddtimeFormated() {
    // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // return getAddtime() != null ? formatter.format(getAddtime()) : "";
    // }

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

    public String getUsernameSrch() {
        return usernameSrch;
    }

    public void setUsernameSrch(String usernameSrch) {
        this.usernameSrch = usernameSrch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getMaybackmoney() {
        return maybackmoney;
    }

    public void setMaybackmoney(String maybackmoney) {
        this.maybackmoney = maybackmoney;
    }

    public String getBalanceMinSrch() {
        return balanceMinSrch;
    }

    public void setBalanceMinSrch(String balanceMinSrch) {
        this.balanceMinSrch = balanceMinSrch;
    }

    public String getBalanceMaxSrch() {
        return balanceMaxSrch;
    }

    public void setBalanceMaxSrch(String balanceMaxSrch) {
        this.balanceMaxSrch = balanceMaxSrch;
    }

    public String getAddtimeStartSrch() {
        return addtimeStartSrch;
    }

    public void setAddtimeStartSrch(String addtimeStartSrch) {
        this.addtimeStartSrch = addtimeStartSrch;
    }

    public String getAddtimeEndSrch() {
        return addtimeEndSrch;
    }

    public void setAddtimeEndSrch(String addtimeEndSrch) {
        this.addtimeEndSrch = addtimeEndSrch;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorSrch() {
        return operatorSrch;
    }

    public void setOperatorSrch(String operatorSrch) {
        this.operatorSrch = operatorSrch;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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