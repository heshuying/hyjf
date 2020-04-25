package com.hyjf.admin.manager.config.bailconfig;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhBailConfig;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * 提成设置
 *
 * @author liushouyi
 */
public class BailConfigBean extends HjhBailConfig implements Serializable {

    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;

    private List<HjhBailConfigInfoCustomize> recordList;

    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    private Integer monthNCL;
    private Integer monthLCL;
    private Integer monthRCT;
    private Integer monthDEL;
    private Integer endNCL;
    private Integer endLCL;
    private Integer endRCT;
    private Integer endDEL;
    private Integer endmonthNCL;
    private Integer endmonthLCL;
    private Integer endmonthRCT;
    private Integer endmonthDEL;
    private Integer enddayNCL;
    private Integer enddayLCL;
    private Integer enddayRCT;
    private Integer enddayDEL;
    private Integer principalNCL;
    private Integer principalLCL;
    private Integer principalRCT;
    private Integer principalDEL;
    private Integer seasonNCL;
    private Integer seasonLCL;
    private Integer seasonRCT;
    private Integer seasonDEL;
    private Integer endmonthsNCL;
    private Integer endmonthsLCL;
    private Integer endmonthsRCT;
    private Integer endmonthsDEL;

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

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<HjhBailConfigInfoCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<HjhBailConfigInfoCustomize> recordList) {
        this.recordList = recordList;
    }

    public Integer getMonthNCL() {
        return monthNCL;
    }

    public void setMonthNCL(Integer monthNCL) {
        this.monthNCL = monthNCL;
    }

    public Integer getMonthLCL() {
        return monthLCL;
    }

    public void setMonthLCL(Integer monthLCL) {
        this.monthLCL = monthLCL;
    }

    public Integer getMonthRCT() {
        return monthRCT;
    }

    public void setMonthRCT(Integer monthRCT) {
        this.monthRCT = monthRCT;
    }

    public Integer getEndNCL() {
        return endNCL;
    }

    public void setEndNCL(Integer endNCL) {
        this.endNCL = endNCL;
    }

    public Integer getEndLCL() {
        return endLCL;
    }

    public void setEndLCL(Integer endLCL) {
        this.endLCL = endLCL;
    }

    public Integer getEndRCT() {
        return endRCT;
    }

    public void setEndRCT(Integer endRCT) {
        this.endRCT = endRCT;
    }

    public Integer getEndmonthNCL() {
        return endmonthNCL;
    }

    public void setEndmonthNCL(Integer endmonthNCL) {
        this.endmonthNCL = endmonthNCL;
    }

    public Integer getEndmonthLCL() {
        return endmonthLCL;
    }

    public void setEndmonthLCL(Integer endmonthLCL) {
        this.endmonthLCL = endmonthLCL;
    }

    public Integer getEndmonthRCT() {
        return endmonthRCT;
    }

    public void setEndmonthRCT(Integer endmonthRCT) {
        this.endmonthRCT = endmonthRCT;
    }

    public Integer getEnddayNCL() {
        return enddayNCL;
    }

    public void setEnddayNCL(Integer enddayNCL) {
        this.enddayNCL = enddayNCL;
    }

    public Integer getEnddayLCL() {
        return enddayLCL;
    }

    public void setEnddayLCL(Integer enddayLCL) {
        this.enddayLCL = enddayLCL;
    }

    public Integer getEnddayRCT() {
        return enddayRCT;
    }

    public void setEnddayRCT(Integer enddayRCT) {
        this.enddayRCT = enddayRCT;
    }

    public Integer getPrincipalNCL() {
        return principalNCL;
    }

    public void setPrincipalNCL(Integer principalNCL) {
        this.principalNCL = principalNCL;
    }

    public Integer getPrincipalLCL() {
        return principalLCL;
    }

    public void setPrincipalLCL(Integer principalLCL) {
        this.principalLCL = principalLCL;
    }

    public Integer getPrincipalRCT() {
        return principalRCT;
    }

    public void setPrincipalRCT(Integer principalRCT) {
        this.principalRCT = principalRCT;
    }

    public Integer getMonthDEL() {
        return monthDEL;
    }

    public void setMonthDEL(Integer monthDEL) {
        this.monthDEL = monthDEL;
    }

    public Integer getEndDEL() {
        return endDEL;
    }

    public void setEndDEL(Integer endDEL) {
        this.endDEL = endDEL;
    }

    public Integer getEndmonthDEL() {
        return endmonthDEL;
    }

    public void setEndmonthDEL(Integer endmonthDEL) {
        this.endmonthDEL = endmonthDEL;
    }

    public Integer getEnddayDEL() {
        return enddayDEL;
    }

    public void setEnddayDEL(Integer enddayDEL) {
        this.enddayDEL = enddayDEL;
    }

    public Integer getPrincipalDEL() {
        return principalDEL;
    }

    public void setPrincipalDEL(Integer principalDEL) {
        this.principalDEL = principalDEL;
    }

    public Integer getSeasonNCL() {
        return seasonNCL;
    }

    public void setSeasonNCL(Integer seasonNCL) {
        this.seasonNCL = seasonNCL;
    }

    public Integer getSeasonLCL() {
        return seasonLCL;
    }

    public void setSeasonLCL(Integer seasonLCL) {
        this.seasonLCL = seasonLCL;
    }

    public Integer getSeasonRCT() {
        return seasonRCT;
    }

    public void setSeasonRCT(Integer seasonRCT) {
        this.seasonRCT = seasonRCT;
    }

    public Integer getSeasonDEL() {
        return seasonDEL;
    }

    public void setSeasonDEL(Integer seasonDEL) {
        this.seasonDEL = seasonDEL;
    }

    public Integer getEndmonthsNCL() {
        return endmonthsNCL;
    }

    public void setEndmonthsNCL(Integer endmonthsNCL) {
        this.endmonthsNCL = endmonthsNCL;
    }

    public Integer getEndmonthsLCL() {
        return endmonthsLCL;
    }

    public void setEndmonthsLCL(Integer endmonthsLCL) {
        this.endmonthsLCL = endmonthsLCL;
    }

    public Integer getEndmonthsRCT() {
        return endmonthsRCT;
    }

    public void setEndmonthsRCT(Integer endmonthsRCT) {
        this.endmonthsRCT = endmonthsRCT;
    }

    public Integer getEndmonthsDEL() {
        return endmonthsDEL;
    }

    public void setEndmonthsDEL(Integer endmonthsDEL) {
        this.endmonthsDEL = endmonthsDEL;
    }
}
