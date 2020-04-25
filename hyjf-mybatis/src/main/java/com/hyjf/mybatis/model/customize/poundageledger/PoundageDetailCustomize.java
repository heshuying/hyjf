/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.hyjf.mybatis.model.customize.poundageledger;

import java.io.Serializable;
import java.math.BigDecimal;

public class PoundageDetailCustomize implements Serializable {

    private static final long serialVersionUID = 2095887656138554832L;
    //alias
    public static final String TABLE_ALIAS = "PoundageDetail";
    public static final String ALIAS_ID = "id";
    public static final String ALIAS_NID = "订单号";
    public static final String ALIAS_BORROW_NID = "项目编号";
    public static final String ALIAS_BORROW_TYPE = "项目类型";
    public static final String ALIAS_ADDTIME = "放款/还款时间";
    public static final String ALIAS_USERNNAME = "出借人用户名";
    public static final String ALIAS_SOURCE = "分账来源";
    public static final String ALIAS_INVITE_REGION_NAME = "推荐人公司";
    public static final String ALIAS_INVITE_USER_ID = "推荐人id";
    public static final String ALIAS_AMOUNT = "分账金额";
    public static final String ALIAS_LEDGER_TIME = "实际分账时间";
    public static final String ALIAS_LEDGER_ID = "手续费分账配置id";


    //查询用变量

    private int limitStart = -1;
    private int limitEnd = -1;

    /**
     * id       db_column: id
     */
    private Integer id;
    /**
     * 订单号       db_column: nid
     */
    private String nid;
    /**
     * 项目编号       db_column: borrow_nid
     */
    private String borrowNid;
    /**
     * 项目类型       db_column: borrow_type
     */
    private String borrowType;
    /**
     * 放款/还款时间       db_column: addtime
     */
    private Integer addtime;
    /**
     * 出借人用户名       db_column: usernname
     */
    private String usernname;
    /**
     * 分账来源       db_column: source
     */
    private String source;
    /**
     * 推荐人公司       db_column: invite_region_name
     */
    private String inviteRegionName;
    /**
     * 推荐人id       db_column: invite_user_id
     */
    private Integer inviteUserId;
    /**
     * 分账金额       db_column: amount
     */
    private java.math.BigDecimal amount;
    /**
     * 实际分账时间       db_column: ledger_time
     */
    private Integer ledgerTime;
    /**
     * 手续费分账配置id       db_column: ledger_id
     */
    private Integer ledgerId;
    /**
     * 手续费分账类型      db_column: poundage_type
     */
    private String poundageType;
    /**
     * 手续费分账id
     */
    private Integer poundageId;
    /**
     * poundageType查询条件
     */
    private String poundageTypeIdSer;
    /**
     * id查询条件
     */
    private Integer idSer;
    /**
     * 订单号查询条件
     */
    private String nidSer;
    /**
     * 项目编号查询条件
     */
    private String borrowNidSer;
    /**
     * 项目类型查询条件
     */
    private String borrowTypeSer;
    /**
     * 放款/还款时间查询条件
     */
    private String addTimeStart;
    private String addTimeEnd;
    /**
     * 出借人用户名查询条件
     */
    private String usernnameSer;
    /**
     * 分账来源查询条件
     */
    private String sourceSer;
    /**
     * 推荐人公司查询条件
     */
    private String inviteRegionNameSer;
    /**
     * 推荐人id查询条件
     */
    private Integer inviteUserIdSer;
    /**
     * 分账金额查询条件
     */
    private BigDecimal amountSer;
    /**
     * 实际分账时间查询条件
     */
    private Integer ledgerTimeSer;
    /**
     * 手续费分账配置id查询条件
     */
    private Integer ledgerIdSer;

    //columns END
    public void setId(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return this.id;
    }

    public void setNid(String value) {
        this.nid = value;
    }

    public String getNid() {
        return this.nid;
    }

    public void setBorrowNid(String value) {
        this.borrowNid = value;
    }

    public String getBorrowNid() {
        return this.borrowNid;
    }

    public void setBorrowType(String value) {
        this.borrowType = value;
    }

    public String getBorrowType() {
        return this.borrowType;
    }

    public void setAddtime(Integer value) {
        this.addtime = value;
    }

    public Integer getAddtime() {
        return this.addtime;
    }

    public void setUsernname(String value) {
        this.usernname = value;
    }

    public String getUsernname() {
        return this.usernname;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getSource() {
        return this.source;
    }

    public void setInviteRegionName(String value) {
        this.inviteRegionName = value;
    }

    public String getInviteRegionName() {
        return this.inviteRegionName;
    }

    public void setInviteUserId(Integer value) {
        this.inviteUserId = value;
    }

    public Integer getInviteUserId() {
        return this.inviteUserId;
    }

    public void setAmount(java.math.BigDecimal value) {
        this.amount = value;
    }

    public java.math.BigDecimal getAmount() {
        return this.amount;
    }

    public void setLedgerTime(Integer value) {
        this.ledgerTime = value;
    }

    public Integer getLedgerTime() {
        return this.ledgerTime;
    }

    public void setLedgerId(Integer value) {
        this.ledgerId = value;
    }

    public Integer getLedgerId() {
        return this.ledgerId;
    }

    public Integer getPoundageId() {
        return poundageId;
    }

    public void setPoundageId(Integer poundageId) {
        this.poundageId = poundageId;
    }

    public void setIdSer(Integer value) {
        this.idSer = value;
    }

    public Integer getIdSer() {
        return this.idSer;
    }

    public void setNidSer(String value) {
        this.nidSer = value;
    }

    public String getNidSer() {
        return this.nidSer;
    }

    public void setBorrowNidSer(String value) {
        this.borrowNidSer = value;
    }

    public String getBorrowNidSer() {
        return this.borrowNidSer;
    }

    public void setBorrowTypeSer(String value) {
        this.borrowTypeSer = value;
    }

    public String getBorrowTypeSer() {
        return this.borrowTypeSer;
    }

    public String getAddTimeStart() {
        return addTimeStart;
    }

    public void setAddTimeStart(String addTimeStart) {
        this.addTimeStart = addTimeStart;
    }

    public String getAddTimeEnd() {
        return addTimeEnd;
    }

    public void setAddTimeEnd(String addTimeEnd) {
        this.addTimeEnd = addTimeEnd;
    }

    public void setUsernnameSer(String value) {
        this.usernnameSer = value;
    }

    public String getUsernnameSer() {
        return this.usernnameSer;
    }

    public void setSourceSer(String value) {
        this.sourceSer = value;
    }

    public String getSourceSer() {
        return this.sourceSer;
    }

    public void setInviteRegionNameSer(String value) {
        this.inviteRegionNameSer = value;
    }

    public String getInviteRegionNameSer() {
        return this.inviteRegionNameSer;
    }

    public void setInviteUserIdSer(Integer value) {
        this.inviteUserIdSer = value;
    }

    public Integer getInviteUserIdSer() {
        return this.inviteUserIdSer;
    }

    public void setAmountSer(java.math.BigDecimal value) {
        this.amountSer = value;
    }

    public java.math.BigDecimal getAmountSer() {
        return this.amountSer;
    }

    public void setLedgerTimeSer(Integer value) {
        this.ledgerTimeSer = value;
    }

    public Integer getLedgerTimeSer() {
        return this.ledgerTimeSer;
    }

    public void setLedgerIdSer(Integer value) {
        this.ledgerIdSer = value;
    }

    public Integer getLedgerIdSer() {
        return this.ledgerIdSer;
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

    public String getPoundageType() {
        return poundageType;
    }

    public void setPoundageType(String poundageType) {
        this.poundageType = poundageType;
    }

    public String getPoundageTypeIdSer() {
        return poundageTypeIdSer;
    }

    public void setPoundageTypeIdSer(String poundageTypeIdSer) {
        this.poundageTypeIdSer = poundageTypeIdSer;
    }

}

