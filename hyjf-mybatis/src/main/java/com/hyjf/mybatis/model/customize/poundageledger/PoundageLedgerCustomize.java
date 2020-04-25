/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.hyjf.mybatis.model.customize.poundageledger;

import java.math.BigDecimal;

public class PoundageLedgerCustomize implements java.io.Serializable {

    private static final long serialVersionUID = 3315062355307079181L;
    //alias
    public static final String TABLE_ALIAS = "PoundageLedger";
    public static final String ALIAS_ID = "id";
    public static final String ALIAS_USERNAME = "用户名";
    public static final String ALIAS_TRUENAME = "真实姓名";
    public static final String ALIAS_ACCOUNT = "电子账号";
    public static final String ALIAS_TYPE = "分账类型   1:按出借人分账； 2:按借款人分账";
    public static final String ALIAS_SOURCE = "分账来源  0:全部； 1:服务费； 2:债转服务费； 3:管理费";
    public static final String ALIAS_SERVICE_RATIO = "服务费分账比例";
    public static final String ALIAS_CREDIT_RATIO = "债转服务费分账比例";
    public static final String ALIAS_MANAGE_RATIO = "管理费分账比例";
    public static final String ALIAS_INVESTOR_COMPANY_ID = "出借人分公司id";
    public static final String ALIAS_INVESTOR_COMPANY = "出借人分公司名称";
    public static final String ALIAS_PROJECT_TYPE = "项目类型，可保存所有";
    public static final String ALIAS_STATUS = "启用状态  0：禁用    1：启用";
    public static final String ALIAS_EXPLAN = "说明";
    public static final String ALIAS_CREATE_TIME = "创建时间";
    public static final String ALIAS_CREATERE = "创建人id";
    public static final String ALIAS_UPDATE_TIME = "更新时间";
    public static final String ALIAS_UPDATER = "修改人id";

    public static final int TYPE_INVEST = 1;//"分账类型 1:按出借人分账";
    public static final int TYPE_LOAN = 2;//"分账类型 2:按借款人分账";
    public static final String TYPE_INVEST_STR = "按出借人分账";
    public static final String TYPE_LOAN_STR = "按借款人分账";

    public static final String SOURCE_ALL = "0";//"分账来源  0:全部";
    public static final String SOURCE_SERVICE = "1";//"分账来源 1:服务费";
    public static final String SOURCE_CREDIT = "2"; //"分账来源 2:债转服务费";
    public static final String SOURCE_MANAGE = "3";//"分账来源 3:管理费";
    public static final String SOURCE_ALL_STR = "全部";
    public static final String SOURCE_SERVICE_STR = "服务费";
    public static final String SOURCE_CREDIT_STR = "债转服务费";
    public static final String SOURCE_MANAGE_STR = "管理费";

    /**
     * 获取分账类型信息
     *
     * @param type
     * @return
     * @author wgx
     */
    public static String getTypeStr(int type) {
        switch (type) {
            case TYPE_INVEST:
                return TYPE_INVEST_STR;
            case TYPE_LOAN:
                return TYPE_LOAN_STR;
        }
        return "";
    }

    /**
     * 获取分账来源信息
     *
     * @param source
     * @return
     * @author wgx
     */
    public static String getSourceStr(String source) {
        switch (source) {
            case SOURCE_ALL:
                return SOURCE_ALL_STR;
            case SOURCE_SERVICE:
                return SOURCE_SERVICE_STR;
            case SOURCE_CREDIT:
                return SOURCE_CREDIT_STR;
            case SOURCE_MANAGE:
                return SOURCE_MANAGE_STR;
        }
        return "";
    }
    //查询用变量

    private int limitStart = -1;
    private int limitEnd = -1;

    /**
     * id       db_column: id
     */
    private Integer id;
    /**
     * 用户名       db_column: username
     */
    private String username;
    /**
     * 真实姓名       db_column: truename
     */
    private String truename;
    /**
     * 电子账号       db_column: account
     */
    private String account;
    /**
     * 分账类型   1:按出借人分账； 2:按借款人分账       db_column: type
     */
    private Integer type;
    /**
     * 分账来源  0:全部； 1:服务费； 2:债转服务费； 3:管理费       db_column: source
     */
    private String source;
    /**
     * 服务费分账比例       db_column: service_ratio
     */
    private BigDecimal serviceRatio;
    /**
     * 债转服务费分账比例       db_column: credit_ratio
     */
    private BigDecimal creditRatio;
    /**
     * 管理费分账比例       db_column: manage_ratio
     */
    private BigDecimal manageRatio;
    /**
     * 出借人分公司id       db_column: investor_company_id
     */
    private Integer investorCompanyId;
    /**
     * 出借人分公司名称       db_column: investor_company
     */
    private String investorCompany;
    /**
     * 项目类型，可保存所有       db_column: project_type
     */
    private String projectType;
    /**
     * 启用状态  0：禁用    1：启用       db_column: status
     */
    private Integer status;
    /**
     * 说明       db_column: explan
     */
    private String explan;
    /**
     * 创建时间       db_column: create_time
     */
    private Integer createTime;
    /**
     * 创建人id       db_column: createre
     */
    private Integer createre;
    /**
     * 更新时间       db_column: update_time
     */
    private Integer updateTime;
    /**
     * 修改人id       db_column: updater
     */
    private Integer updater;

    /**
     * id查询条件
     */
    private Integer idSer;
    /**
     * 用户名查询条件
     */
    private String usernameSer;
    /**
     * 真实姓名查询条件
     */
    private String truenameSer;
    /**
     * 电子账号查询条件
     */
    private String accountSer;
    /**
     * 分账类型   1:按出借人分账； 2:按借款人分账查询条件
     */
    private Integer typeSer;
    /**
     * 分账来源  0:全部； 1:服务费； 2:债转服务费； 3:管理费查询条件
     */
    private String sourceSer;
    /**
     * 服务费分账比例查询条件
     */
    private BigDecimal serviceRatioSer;
    /**
     * 债转服务费分账比例查询条件
     */
    private BigDecimal creditRatioSer;
    /**
     * 管理费分账比例查询条件
     */
    private BigDecimal manageRatioSer;
    /**
     * 出借人分公司id查询条件
     */
    private Integer investorCompanyIdSer;
    /**
     * 出借人分公司名称查询条件
     */
    private String investorCompanySer;
    /**
     * 项目类型，可保存所有查询条件
     */
    private String projectTypeSer;
    /**
     * 启用状态  0：禁用    1：启用查询条件
     */
    private Integer statusSer;
    /**
     * 说明查询条件
     */
    private String explanSer;
    /**
     * 创建时间查询条件
     */
    private Integer createTimeSer;
    /**
     * 创建人id查询条件
     */
    private Integer createreSer;
    /**
     * 更新时间查询条件
     */
    private Integer updateTimeSer;
    /**
     * 修改人id查询条件
     */
    private Integer updaterSer;

    //columns END
    public void setId(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return this.id;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public String getUsername() {
        return this.username;
    }

    public void setTruename(String value) {
        this.truename = value;
    }

    public String getTruename() {
        return this.truename;
    }

    public void setAccount(String value) {
        this.account = value;
    }

    public String getAccount() {
        return this.account;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getSource() {
        return this.source;
    }

    public void setServiceRatio(BigDecimal value) {
        this.serviceRatio = value;
    }

    public BigDecimal getServiceRatio() {
        return this.serviceRatio;
    }

    public void setCreditRatio(BigDecimal value) {
        this.creditRatio = value;
    }

    public BigDecimal getCreditRatio() {
        return this.creditRatio;
    }

    public void setManageRatio(BigDecimal value) {
        this.manageRatio = value;
    }

    public BigDecimal getManageRatio() {
        return this.manageRatio;
    }

    public void setInvestorCompanyId(Integer value) {
        this.investorCompanyId = value;
    }

    public Integer getInvestorCompanyId() {
        return this.investorCompanyId;
    }

    public void setInvestorCompany(String value) {
        this.investorCompany = value;
    }

    public String getInvestorCompany() {
        return this.investorCompany;
    }

    public void setProjectType(String value) {
        this.projectType = value;
    }

    public String getProjectType() {
        return this.projectType;
    }

    public void setStatus(Integer value) {
        this.status = value;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setExplan(String value) {
        this.explan = value;
    }

    public String getExplan() {
        return this.explan;
    }

    public void setCreateTime(Integer value) {
        this.createTime = value;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public void setCreatere(Integer value) {
        this.createre = value;
    }

    public Integer getCreatere() {
        return this.createre;
    }

    public void setUpdateTime(Integer value) {
        this.updateTime = value;
    }

    public Integer getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdater(Integer value) {
        this.updater = value;
    }

    public Integer getUpdater() {
        return this.updater;
    }

    public void setIdSer(Integer value) {
        this.idSer = value;
    }

    public Integer getIdSer() {
        return this.idSer;
    }

    public void setUsernameSer(String value) {
        this.usernameSer = value;
    }

    public String getUsernameSer() {
        return this.usernameSer;
    }

    public void setTruenameSer(String value) {
        this.truenameSer = value;
    }

    public String getTruenameSer() {
        return this.truenameSer;
    }

    public void setAccountSer(String value) {
        this.accountSer = value;
    }

    public String getAccountSer() {
        return this.accountSer;
    }

    public void setTypeSer(Integer value) {
        this.typeSer = value;
    }

    public Integer getTypeSer() {
        return this.typeSer;
    }

    public void setSourceSer(String value) {
        this.sourceSer = value;
    }

    public String getSourceSer() {
        return this.sourceSer;
    }

    public void setServiceRatioSer(BigDecimal value) {
        this.serviceRatioSer = value;
    }

    public BigDecimal getServiceRatioSer() {
        return this.serviceRatioSer;
    }

    public void setCreditRatioSer(BigDecimal value) {
        this.creditRatioSer = value;
    }

    public BigDecimal getCreditRatioSer() {
        return this.creditRatioSer;
    }

    public void setManageRatioSer(BigDecimal value) {
        this.manageRatioSer = value;
    }

    public BigDecimal getManageRatioSer() {
        return this.manageRatioSer;
    }

    public void setInvestorCompanyIdSer(Integer value) {
        this.investorCompanyIdSer = value;
    }

    public Integer getInvestorCompanyIdSer() {
        return this.investorCompanyIdSer;
    }

    public void setInvestorCompanySer(String value) {
        this.investorCompanySer = value;
    }

    public String getInvestorCompanySer() {
        return this.investorCompanySer;
    }

    public void setProjectTypeSer(String value) {
        this.projectTypeSer = value;
    }

    public String getProjectTypeSer() {
        return this.projectTypeSer;
    }

    public void setStatusSer(Integer value) {
        this.statusSer = value;
    }

    public Integer getStatusSer() {
        return this.statusSer;
    }

    public void setExplanSer(String value) {
        this.explanSer = value;
    }

    public String getExplanSer() {
        return this.explanSer;
    }

    public void setCreateTimeSer(Integer value) {
        this.createTimeSer = value;
    }

    public Integer getCreateTimeSer() {
        return this.createTimeSer;
    }

    public void setCreatereSer(Integer value) {
        this.createreSer = value;
    }

    public Integer getCreatereSer() {
        return this.createreSer;
    }

    public void setUpdateTimeSer(Integer value) {
        this.updateTimeSer = value;
    }

    public Integer getUpdateTimeSer() {
        return this.updateTimeSer;
    }

    public void setUpdaterSer(Integer value) {
        this.updaterSer = value;
    }

    public Integer getUpdaterSer() {
        return this.updaterSer;
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

}

