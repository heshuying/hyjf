/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author PC-LIUSHOUYI
 * @version HjhBailConfigCustomizeInfo, v0.1 2018/7/25 20:04
 */
public class HjhBailConfigInfoCustomize extends HjhBailConfigCustomize implements Serializable {

    /**
     * 等额本息 NCL:新增授信  LCL:在贷授信 RCT:回滚方式
     */
    private Integer monthNCL;
    private Integer monthLCL;
    private Integer monthRCT;
    private Integer monthDEL;
    /**
     * 按月计息，到期还本还息
     */
    private Integer endNCL;
    private Integer endLCL;
    private Integer endRCT;
    private Integer endDEL;
    /**
     * 先息后本
     */
    private Integer endmonthNCL;
    private Integer endmonthLCL;
    private Integer endmonthRCT;
    private Integer endmonthDEL;
    /**
     * 按天计息，到期还本息
     */
    private Integer enddayNCL;
    private Integer enddayLCL;
    private Integer enddayRCT;
    private Integer enddayDEL;
    /**
     * 等额本金
     */
    private Integer principalNCL;
    private Integer principalLCL;
    private Integer principalRCT;
    private Integer principalDEL;

    /**
     * 按季还款
     */
    private Integer seasonNCL;
    private Integer seasonLCL;
    private Integer seasonRCT;
    private Integer seasonDEL;

    /**
     * 按月付息到期还本
     */
    private Integer endmonthsNCL;
    private Integer endmonthsLCL;
    private Integer endmonthsRCT;
    private Integer endmonthsDEL;

    private static final long serialVersionUID = 1L;

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
