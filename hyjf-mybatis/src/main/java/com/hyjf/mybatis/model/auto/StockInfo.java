package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockInfo implements Serializable {
    private Integer id;

    private BigDecimal nowPrice;

    private BigDecimal increase;

    private BigDecimal decline;

    private BigDecimal volume;

    private Integer date;

    private BigDecimal previousClosePrice;

    private BigDecimal dayLow;

    private BigDecimal dayHigh;

    private BigDecimal openPrice;

    private BigDecimal marketCap;

    private BigDecimal peRatio;

    private BigDecimal eps;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    public BigDecimal getIncrease() {
        return increase;
    }

    public void setIncrease(BigDecimal increase) {
        this.increase = increase;
    }

    public BigDecimal getDecline() {
        return decline;
    }

    public void setDecline(BigDecimal decline) {
        this.decline = decline;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public BigDecimal getPreviousClosePrice() {
        return previousClosePrice;
    }

    public void setPreviousClosePrice(BigDecimal previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }

    public BigDecimal getDayLow() {
        return dayLow;
    }

    public void setDayLow(BigDecimal dayLow) {
        this.dayLow = dayLow;
    }

    public BigDecimal getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(BigDecimal dayHigh) {
        this.dayHigh = dayHigh;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(BigDecimal peRatio) {
        this.peRatio = peRatio;
    }

    public BigDecimal getEps() {
        return eps;
    }

    public void setEps(BigDecimal eps) {
        this.eps = eps;
    }
}