/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportrait;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.UserPortraitCustomize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ${yaoy}
 * @version UsersPortraitBean, v0.1 2018/5/11 15:19
 */
public class UsersPortraitBean extends UsersPortrait implements Serializable {

//    检索条件
    /**
     * 年龄开始
     */
    private Integer ageStart;
    /**
     * 年龄开始
     */
    private Integer ageEnd;
    /**
     * 账户总资产开始
     */
    private BigDecimal bankTotalStart;
    /**
     * 账户总资产结束
     */
    private BigDecimal bankTotalEnd;
    /**
     * 累计收益开始
     */
    private BigDecimal interestSumStart;
    /**
     * 累计收益结束
     */
    private BigDecimal interestSumEnd;
    /**
     * 交易笔数开始
     */
    private Integer tradeNumberStart;
    /**
     * 交易笔数结束
     */
    private Integer tradeNumberEnd;
    /**
     * 当前拥有人
     */
    private String currentOwner;
    /**
     * 是否有主单
     */
    private Integer attribute;
    /**
     * 出借进程
     */
    private String investProcess;
    /**
     * 注册时间开始
     */
    private String regTimeStart;
    /**
     * 注册时间结束
     */
    private String regTimeEnd;
    private List<UserPortraitCustomize> recordlist;
    private List<UserPortraitCustomizeBean> recordList;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public List<UserPortraitCustomize> getRecordlist() {
        return recordlist;
    }

    public void setRecordlist(List<UserPortraitCustomize> recordlist) {
        this.recordlist = recordlist;
    }

    public List<UserPortraitCustomizeBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<UserPortraitCustomizeBean> recordList) {
        this.recordList = recordList;
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

    public Integer getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(Integer ageStart) {
        this.ageStart = ageStart;
    }

    public Integer getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(Integer ageEnd) {
        this.ageEnd = ageEnd;
    }

    public BigDecimal getBankTotalStart() {
        return bankTotalStart;
    }

    public void setBankTotalStart(BigDecimal bankTotalStart) {
        this.bankTotalStart = bankTotalStart;
    }

    public BigDecimal getBankTotalEnd() {
        return bankTotalEnd;
    }

    public void setBankTotalEnd(BigDecimal bankTotalEnd) {
        this.bankTotalEnd = bankTotalEnd;
    }

    public BigDecimal getInterestSumStart() {
        return interestSumStart;
    }

    public void setInterestSumStart(BigDecimal interestSumStart) {
        this.interestSumStart = interestSumStart;
    }

    public BigDecimal getInterestSumEnd() {
        return interestSumEnd;
    }

    public void setInterestSumEnd(BigDecimal interestSumEnd) {
        this.interestSumEnd = interestSumEnd;
    }

    public Integer getTradeNumberStart() {
        return tradeNumberStart;
    }

    public void setTradeNumberStart(Integer tradeNumberStart) {
        this.tradeNumberStart = tradeNumberStart;
    }

    public Integer getTradeNumberEnd() {
        return tradeNumberEnd;
    }

    public void setTradeNumberEnd(Integer tradeNumberEnd) {
        this.tradeNumberEnd = tradeNumberEnd;
    }

    @Override
    public String getCurrentOwner() {
        return currentOwner;
    }

    @Override
    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    @Override
    public Integer getAttribute() {
        return attribute;
    }

    @Override
    public void setAttribute(Integer attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getInvestProcess() {
        return investProcess;
    }

    @Override
    public void setInvestProcess(String investProcess) {
        this.investProcess = investProcess;
    }

    public String getRegTimeStart() {
        return regTimeStart;
    }

    public void setRegTimeStart(String regTimeStart) {
        this.regTimeStart = regTimeStart;
    }

    public String getRegTimeEnd() {
        return regTimeEnd;
    }

    public void setRegTimeEnd(String regTimeEnd) {
        this.regTimeEnd = regTimeEnd;
    }
}
