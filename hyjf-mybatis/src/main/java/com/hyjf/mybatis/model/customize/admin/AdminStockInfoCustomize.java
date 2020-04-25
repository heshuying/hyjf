/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.hyjf.mybatis.model.customize.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminStockInfoCustomize implements java.io.Serializable {
    public static final long serialVersionUID = 5454155825314635342L;

    // alias
    public static final String TABLE_ALIAS = "AdminStockInfo";

    public static final String ALIAS_ID = "id";

    public static final String ALIAS_NOW_PRICE = "当前价格";

    public static final String ALIAS_INCREASE = "涨幅 百分数";

    public static final String ALIAS_DECLINE = "跌幅 百分数";

    public static final String ALIAS_VOLUME = "成交量";

    public static final String ALIAS_DATE = "date";

    public static final String ALIAS_PREVIOUS_CLOSE_PRICE = "昨日收盘";

    public static final String ALIAS_DAY_LOW = "最低";

    public static final String ALIAS_DAY_HIGH = "最高";

    public static final String ALIAS_OPEN_PRICE = "今日开盘";

    public static final String ALIAS_MARKET_CAP = "总市值";

    public static final String ALIAS_PE_RATIO = "市盈率";

    public static final String ALIAS_EPS = "每股收益";

    public int limitStart = -1;

    public int limitEnd = -1;

    /**
     * id db_column: id
     */
    public java.lang.Integer id;

    /**
     * 当前价格 db_column: now_price
     */
    public java.math.BigDecimal nowPrice;

    /**
     * 涨幅 百分数 db_column: increase
     */
    public java.math.BigDecimal increase;

    /**
     * 跌幅 百分数 db_column: decline
     */
    public java.math.BigDecimal decline;

    /**
     * 成交量 db_column: volume
     */
    public java.math.BigDecimal volume;

    /**
     * date db_column: date
     */
    public String date;

    /**
     * 昨日收盘 db_column: previous_close_price
     */
    public java.math.BigDecimal previousClosePrice;

    /**
     * 最低 db_column: day_low
     */
    public java.math.BigDecimal dayLow;

    /**
     * 最高 db_column: day_high
     */
    public java.math.BigDecimal dayHigh;

    /**
     * 今日开盘 db_column: open_price
     */
    public java.math.BigDecimal openPrice;

    /**
     * 总市值 db_column: market_cap
     */
    public java.math.BigDecimal marketCap;

    /**
     * 市盈率 db_column: pe_ratio
     */
    public java.math.BigDecimal peRatio;

    /**
     * 每股收益 db_column: eps
     */
    public java.math.BigDecimal eps;

    /**
     * id查询条件
     */
    public java.lang.Integer idSer;

    /**
     * 当前价格查询条件
     */
    public java.math.BigDecimal nowPriceSer;

    /**
     * 涨幅 百分数查询条件
     */
    public java.math.BigDecimal increaseSer;

    /**
     * 跌幅 百分数查询条件
     */
    public java.math.BigDecimal declineSer;

    /**
     * 成交量查询条件
     */
    public java.math.BigDecimal volumeSer;

    /**
     * date查询条件
     */
    public java.lang.Integer dateSer;

    /**
     * 昨日收盘查询条件
     */
    public java.math.BigDecimal previousClosePriceSer;

    /**
     * 最低查询条件
     */
    public java.math.BigDecimal dayLowSer;

    /**
     * 最高查询条件
     */
    public java.math.BigDecimal dayHighSer;

    /**
     * 今日开盘查询条件
     */
    public java.math.BigDecimal openPriceSer;

    /**
     * 总市值查询条件
     */
    public java.math.BigDecimal marketCapSer;

    /**
     * 市盈率查询条件
     */
    public java.math.BigDecimal peRatioSer;

    /**
     * 每股收益查询条件
     */
    public java.math.BigDecimal epsSer;

    /**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;

    // columns END
    public void setId(java.lang.Integer value) {
        this.id = value;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setNowPrice(java.math.BigDecimal value) {
        this.nowPrice = value;
    }

    public java.math.BigDecimal getNowPrice() {
        return this.nowPrice;
    }

    public void setIncrease(java.math.BigDecimal value) {
        this.increase = value;
    }

    public java.math.BigDecimal getIncrease() {
        return this.increase;
    }

    public void setDecline(java.math.BigDecimal value) {
        this.decline = value;
    }

    public java.math.BigDecimal getDecline() {
        return this.decline;
    }

    public void setVolume(java.math.BigDecimal value) {
        this.volume = value;
    }

    public java.math.BigDecimal getVolume() {
        return this.volume;
    }

    public void setDate(String value) {
        this.date = value;
    }

    public String getDate() {
        if(this.date!=null && this.date!=""){
            return stampToDate(this.date);  
        }else{
            return ""; 
        }
    }

    public void setPreviousClosePrice(java.math.BigDecimal value) {
        this.previousClosePrice = value;
    }

    public java.math.BigDecimal getPreviousClosePrice() {
        return this.previousClosePrice;
    }

    public void setDayLow(java.math.BigDecimal value) {
        this.dayLow = value;
    }

    public java.math.BigDecimal getDayLow() {
        return this.dayLow;
    }

    public void setDayHigh(java.math.BigDecimal value) {
        this.dayHigh = value;
    }

    public java.math.BigDecimal getDayHigh() {
        return this.dayHigh;
    }

    public void setOpenPrice(java.math.BigDecimal value) {
        this.openPrice = value;
    }

    public java.math.BigDecimal getOpenPrice() {
        return this.openPrice;
    }

    public void setMarketCap(java.math.BigDecimal value) {
        this.marketCap = value;
    }

    public java.math.BigDecimal getMarketCap() {
        return this.marketCap;
    }

    public void setPeRatio(java.math.BigDecimal value) {
        this.peRatio = value;
    }

    public java.math.BigDecimal getPeRatio() {
        return this.peRatio;
    }

    public void setEps(java.math.BigDecimal value) {
        this.eps = value;
    }

    public java.math.BigDecimal getEps() {
        return this.eps;
    }

    public void setIdSer(java.lang.Integer value) {
        this.idSer = value;
    }

    public java.lang.Integer getIdSer() {
        return this.idSer;
    }

    public void setNowPriceSer(java.math.BigDecimal value) {
        this.nowPriceSer = value;
    }

    public java.math.BigDecimal getNowPriceSer() {
        return this.nowPriceSer;
    }

    public void setIncreaseSer(java.math.BigDecimal value) {
        this.increaseSer = value;
    }

    public java.math.BigDecimal getIncreaseSer() {
        return this.increaseSer;
    }

    public void setDeclineSer(java.math.BigDecimal value) {
        this.declineSer = value;
    }

    public java.math.BigDecimal getDeclineSer() {
        return this.declineSer;
    }

    public void setVolumeSer(java.math.BigDecimal value) {
        this.volumeSer = value;
    }

    public java.math.BigDecimal getVolumeSer() {
        return this.volumeSer;
    }

    public void setDateSer(java.lang.Integer value) {
        this.dateSer = value;
    }

    public java.lang.Integer getDateSer() {
        return this.dateSer;
    }

    public void setPreviousClosePriceSer(java.math.BigDecimal value) {
        this.previousClosePriceSer = value;
    }

    public java.math.BigDecimal getPreviousClosePriceSer() {
        return this.previousClosePriceSer;
    }

    public void setDayLowSer(java.math.BigDecimal value) {
        this.dayLowSer = value;
    }

    public java.math.BigDecimal getDayLowSer() {
        return this.dayLowSer;
    }

    public void setDayHighSer(java.math.BigDecimal value) {
        this.dayHighSer = value;
    }

    public java.math.BigDecimal getDayHighSer() {
        return this.dayHighSer;
    }

    public void setOpenPriceSer(java.math.BigDecimal value) {
        this.openPriceSer = value;
    }

    public java.math.BigDecimal getOpenPriceSer() {
        return this.openPriceSer;
    }

    public void setMarketCapSer(java.math.BigDecimal value) {
        this.marketCapSer = value;
    }

    public java.math.BigDecimal getMarketCapSer() {
        return this.marketCapSer;
    }

    public void setPeRatioSer(java.math.BigDecimal value) {
        this.peRatioSer = value;
    }

    public java.math.BigDecimal getPeRatioSer() {
        return this.peRatioSer;
    }

    public void setEpsSer(java.math.BigDecimal value) {
        this.epsSer = value;
    }

    public java.math.BigDecimal getEpsSer() {
        return this.epsSer;
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
    
    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s)*1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
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

}
