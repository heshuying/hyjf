package com.hyjf.admin.manager.hjhplan.plancapital;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 文章管理实体类
 * 
 * @author
 *
 */
public class PlanCapitalListBean extends HjhPlanCapital implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4285199353931419072L;
    /**
     * 排序
     */
    private String sort;
    /**
     * 排序列
     */
    private String col;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;
    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;

    /** *****以下画面检索条件所需字段****** */
    /**
     * 检索条件 计划编号
     */
    private String planNidSrch;
    /**
     * 检索条件 计划名称
     */
    private String planNameSrch;
    /**
     * 检索条件锁定期
     */
    private String lockPeriodSrch;
    /**
     * 检索条件 日期开始
     */
    private String dateFromSrch;
    /**
     * 检索条件 日期结束
     */
    private String dateToSrch;

    /** 列表list */
    private List<HjhPlanCapital> recordList;

    /** 总计：复投总额 */
    private BigDecimal sumReinvestAccount;

    /** 总计：债转总额 */
    private BigDecimal sumCreditAccount;

    /**
     *  日期KEY1
     */
    private String dateKey;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
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

    /** *****以下画面用字段****** */
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

    public String getPlanNidSrch() {
        return planNidSrch;
    }

    public void setPlanNidSrch(String planNidSrch) {
        this.planNidSrch = planNidSrch;
    }

    public String getPlanNameSrch() {
        return planNameSrch;
    }

    public void setPlanNameSrch(String planNameSrch) {
        this.planNameSrch = planNameSrch;
    }

    public String getLockPeriodSrch() {
        return lockPeriodSrch;
    }

    public void setLockPeriodSrch(String lockPeriodSrch) {
        this.lockPeriodSrch = lockPeriodSrch;
    }

    public String getDateFromSrch() {
        return dateFromSrch;
    }

    public void setDateFromSrch(String dateFromSrch) {
        this.dateFromSrch = dateFromSrch;
    }

    public String getDateToSrch() {
        return dateToSrch;
    }

    public void setDateToSrch(String dateToSrch) {
        this.dateToSrch = dateToSrch;
    }

    public List<HjhPlanCapital> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<HjhPlanCapital> recordList) {
        this.recordList = recordList;
    }

    public String getDateKey() {
        return dateKey;
    }

    public void setDateKey(String dateKey) {
        this.dateKey = dateKey;
    }

    public BigDecimal getSumReinvestAccount() {
        return sumReinvestAccount;
    }

    public void setSumReinvestAccount(BigDecimal sumReinvestAccount) {
        this.sumReinvestAccount = sumReinvestAccount;
    }

    public BigDecimal getSumCreditAccount() {
        return sumCreditAccount;
    }

    public void setSumCreditAccount(BigDecimal sumCreditAccount) {
        this.sumCreditAccount = sumCreditAccount;
    }
}
