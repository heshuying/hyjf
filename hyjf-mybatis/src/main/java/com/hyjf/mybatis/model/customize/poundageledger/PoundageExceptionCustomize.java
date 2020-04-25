package com.hyjf.mybatis.model.customize.poundageledger;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * com.hyjf.mybatis.model.customize.poundage
 *
 * @author wgx
 * @date 2017/12/15
 */
public class PoundageExceptionCustomize implements Serializable {

    private static final long serialVersionUID = 3390080676233108815L;

    //alias
    public static final String TABLE_ALIAS = "PoundageException";
    public static final String ALIAS_ID = "id";
    public static final String ALIAS_NID = "交易凭证号";
    public static final String ALIAS_BANK_SEQ_NO = "流水号";
    public static final String ALIAS_PROJECT_NO = "项目编号";
    public static final String ALIAS_PAY_DATE = "放款/还款时间";
    public static final String ALIAS_LEDGER_AMOUNT = "分账金额";
    public static final String ALIAS_LEDGER_ID = "手续费分账配置id";
    public static final String ALIAS_PAYEE_NAME = "收款人用户名";
    public static final String ALIAS_LEDGER_STATUS = "分账状态:0.未分账;1.已分账";
    public static final String ALIAS_CREATE_TIME = "创建时间";
    public static final String ALIAS_UPDATE_TIME = "更新时间";
    public static final String ALIAS_UPDATER = "修改人id";

    public static final int STATUS_FAIL = 0; //"分账状态：未分账";
    public static final int STATUS_SUCCESS = 1;//"分账状态：已分账";


    private int limitStart = -1;

    private int limitEnd = -1;

    public void setId(Integer id) {
        this.id = id;
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

    /**
     * id       db_column: id
     */
    private Integer id;
    /**
     * 分账金额       db_column: ledger_amount
     */
    private java.math.BigDecimal ledgerAmount;
    /**
     * 手续费配置id       db_column: ledger_id
     */
    private Integer ledgerId;
    /**
     * 手续费分账id       db_column: poundage_id
     */
    private Integer poundageId;
    /**
     * 收款人用户名       db_column: payee_name
     */
    private String payeeName;
    /**
     * 分账状态:0.未分账;1.已分账       db_column: ledger_status
     */
    private Integer ledgerStatus;
    /**
     * 创建时间       db_column: create_time
     */
    private Integer createTime;
    /**
     * 更新时间       db_column: update_time
     */
    private Integer updateTime;
    /**
     * 修改人id       db_column: updater
     */
    private Integer updater;
    /** huiyingdai_poundage_ledger start*/
    /**
     * 收款方用户id
     */
    private Integer userId;
    /**
     * 收款方用户名
     */
    private String userName;
    /**
     * 收款方姓名
     */
    private String realName;
    /**
     * 分账类型
     */
    private Integer ledgerType;
    /**
     * 分账来源
     */
    private Integer ledgerSource;
    /**
     * 江西银行电子账号
     */
    private String account;
    /**
     * 出借人分公司
     */
    private String investorCompany;
    /** huiyingdai_poundage_ledger end*/
    /** huiyingdai_poundage start*/
    /**
     * 交易凭证号
     */
    private String nid;
    /**
     * 请求流水号
     */
    private String seqNo;
    /**
     * 银行返回流水号
     */
    private String bankSeqNo;
    /**
     * 银行订单日期
     */
    private Integer txDate;
    /**
     * 银行订单时间
     */
    private Integer txTime;
    /**
     * 分账状态:
     */
    private Integer poundageStatus;
    /**
     * 分账时间段
     */
    private String poundageTime;
    /**
     * huiyingdai_poundage end
     */
    /*
     * 转出方用户电子账户号
     */
    private String accountId;
    /*
     * 余额
     */
    private String balance;
    /**
     * 密码
     */
    private String password;
    /**
     * id查询条件
     */
    private Integer idSer;
    /**
     * 交易凭证号查询条件
     */
    private String nidSer;
    /**
     * 请求流水号查询条件
     */
    private String seqNoSer;
    /**
     * 银行返回流水号查询条件
     */
    private String bankSeqNoSer;
    /**
     * 分账时间段查询条件
     */
    private String poundageTimeStart;
    private String poundageTimeEnd;
    /**
     * 分账金额查询条件
     */
    private BigDecimal ledgerAmountSer;
    /**
     * 手续费配置id查询条件
     */
    private Integer ledgerIdSer;
    /**
     * 收款人用户名查询条件
     */
    private String payeeNameSer;
    /**
     * 分账状态:0.未分账;1.已分账查询条件
     */
    private Integer ledgerStatusSer;
    /**
     * 创建时间查询条件
     */
    private Integer createTimeSer;
    /**
     * 更新时间查询条件
     */
    private Integer updateTimeSer;
    /**
     * 修改人id查询条件
     */
    private Integer updaterSer;
    /**
     * 收款方用户名查询条件
     */
    private String userNameSer;
    /**
     * 分账类型查询条件
     */
    private Integer ledgerTypeSer;
    /**
     * 分账来源查询条件
     */
    private Integer ledgerSourceSer;
    /**
     * 分账状态查询条件
     */
    private Integer poundageStatusSer;
    /**
     * 出借人分公司查询条件
     */
    private String investorCompanySer;

    public Integer getId() {
        return id;
    }

    public BigDecimal getLedgerAmount() {
        return ledgerAmount;
    }

    public void setLedgerAmount(BigDecimal ledgerAmount) {
        this.ledgerAmount = ledgerAmount;
    }

    public Integer getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Integer ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Integer getPoundageId() {
        return poundageId;
    }

    public void setPoundageId(Integer poundageId) {
        this.poundageId = poundageId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public Integer getLedgerStatus() {
        return ledgerStatus;
    }

    public void setLedgerStatus(Integer ledgerStatus) {
        this.ledgerStatus = ledgerStatus;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getLedgerType() {
        return ledgerType;
    }

    public void setLedgerType(Integer ledgerType) {
        this.ledgerType = ledgerType;
    }

    public Integer getLedgerSource() {
        return ledgerSource;
    }

    public void setLedgerSource(Integer ledgerSource) {
        this.ledgerSource = ledgerSource;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInvestorCompany() {
        return investorCompany;
    }

    public void setInvestorCompany(String investorCompany) {
        this.investorCompany = investorCompany;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }
    public String getBankSeqNo() {
        if(txDate == null || txTime == null){
            return "";
        }
        return txDate + String.valueOf(1000000 + txTime).substring(1) + seqNo;
    }
    public Integer getTxDate() {
        return txDate;
    }

    public void setTxDate(Integer txDate) {
        this.txDate = txDate;
    }

    public Integer getTxTime() {
        return txTime;
    }

    public void setTxTime(Integer txTime) {
        this.txTime = txTime;
    }

    public Integer getPoundageStatus() {
        return poundageStatus;
    }

    public void setPoundageStatus(Integer poundageStatus) {
        this.poundageStatus = poundageStatus;
    }

    public String getPoundageTime() {
        return poundageTime;
    }

    public void setPoundageTime(String poundageTime) {
        this.poundageTime = poundageTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdSer() {
        return idSer;
    }

    public void setIdSer(Integer idSer) {
        this.idSer = idSer;
    }

    public String getPoundageTimeStart() {
        return poundageTimeStart;
    }

    public void setPoundageTimeStart(String poundageTimeStart) {
        this.poundageTimeStart = poundageTimeStart;
    }

    public String getPoundageTimeEnd() {
        return poundageTimeEnd;
    }

    public void setPoundageTimeEnd(String poundageTimeEnd) {
        this.poundageTimeEnd = poundageTimeEnd;
    }

    public BigDecimal getLedgerAmountSer() {
        return ledgerAmountSer;
    }

    public void setLedgerAmountSer(BigDecimal ledgerAmountSer) {
        this.ledgerAmountSer = ledgerAmountSer;
    }

    public Integer getLedgerIdSer() {
        return ledgerIdSer;
    }

    public void setLedgerIdSer(Integer ledgerIdSer) {
        this.ledgerIdSer = ledgerIdSer;
    }

    public String getPayeeNameSer() {
        return payeeNameSer;
    }

    public void setPayeeNameSer(String payeeNameSer) {
        this.payeeNameSer = payeeNameSer;
    }

    public Integer getLedgerStatusSer() {
        return ledgerStatusSer;
    }

    public void setLedgerStatusSer(Integer ledgerStatusSer) {
        this.ledgerStatusSer = ledgerStatusSer;
    }

    public Integer getCreateTimeSer() {
        return createTimeSer;
    }

    public void setCreateTimeSer(Integer createTimeSer) {
        this.createTimeSer = createTimeSer;
    }

    public Integer getUpdateTimeSer() {
        return updateTimeSer;
    }

    public void setUpdateTimeSer(Integer updateTimeSer) {
        this.updateTimeSer = updateTimeSer;
    }

    public Integer getUpdaterSer() {
        return updaterSer;
    }

    public void setUpdaterSer(Integer updaterSer) {
        this.updaterSer = updaterSer;
    }

    public String getUserNameSer() {
        return userNameSer;
    }

    public void setUserNameSer(String userNameSer) {
        this.userNameSer = userNameSer;
    }

    public Integer getLedgerTypeSer() {
        return ledgerTypeSer;
    }

    public void setLedgerTypeSer(Integer ledgerTypeSer) {
        this.ledgerTypeSer = ledgerTypeSer;
    }

    public Integer getLedgerSourceSer() {
        return ledgerSourceSer;
    }

    public void setLedgerSourceSer(Integer ledgerSourceSer) {
        this.ledgerSourceSer = ledgerSourceSer;
    }

    public Integer getPoundageStatusSer() {
        return poundageStatusSer;
    }

    public void setPoundageStatusSer(Integer poundageStatusSer) {
        this.poundageStatusSer = poundageStatusSer;
    }

    public String getInvestorCompanySer() {
        return investorCompanySer;
    }

    public void setInvestorCompanySer(String investorCompanySer) {
        this.investorCompanySer = investorCompanySer;
    }

    public String getNidSer() {
        return nidSer;
    }

    public void setNidSer(String nidSer) {
        this.nidSer = nidSer;
    }

    public String getSeqNoSer() {
        return seqNoSer;
    }

    public void setSeqNoSer(String seqNoSer) {
        this.seqNoSer = seqNoSer;
    }

    public String getBankSeqNoSer() {
        return bankSeqNoSer;
    }

    public void setBankSeqNoSer(String bankSeqNoSer) {
        this.bankSeqNoSer = bankSeqNoSer;
    }
}
