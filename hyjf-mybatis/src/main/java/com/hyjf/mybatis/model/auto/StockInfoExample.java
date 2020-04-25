package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public StockInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNowPriceIsNull() {
            addCriterion("now_price is null");
            return (Criteria) this;
        }

        public Criteria andNowPriceIsNotNull() {
            addCriterion("now_price is not null");
            return (Criteria) this;
        }

        public Criteria andNowPriceEqualTo(BigDecimal value) {
            addCriterion("now_price =", value, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceNotEqualTo(BigDecimal value) {
            addCriterion("now_price <>", value, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceGreaterThan(BigDecimal value) {
            addCriterion("now_price >", value, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("now_price >=", value, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceLessThan(BigDecimal value) {
            addCriterion("now_price <", value, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("now_price <=", value, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceIn(List<BigDecimal> values) {
            addCriterion("now_price in", values, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceNotIn(List<BigDecimal> values) {
            addCriterion("now_price not in", values, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("now_price between", value1, value2, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andNowPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("now_price not between", value1, value2, "nowPrice");
            return (Criteria) this;
        }

        public Criteria andIncreaseIsNull() {
            addCriterion("increase is null");
            return (Criteria) this;
        }

        public Criteria andIncreaseIsNotNull() {
            addCriterion("increase is not null");
            return (Criteria) this;
        }

        public Criteria andIncreaseEqualTo(BigDecimal value) {
            addCriterion("increase =", value, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseNotEqualTo(BigDecimal value) {
            addCriterion("increase <>", value, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseGreaterThan(BigDecimal value) {
            addCriterion("increase >", value, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("increase >=", value, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseLessThan(BigDecimal value) {
            addCriterion("increase <", value, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseLessThanOrEqualTo(BigDecimal value) {
            addCriterion("increase <=", value, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseIn(List<BigDecimal> values) {
            addCriterion("increase in", values, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseNotIn(List<BigDecimal> values) {
            addCriterion("increase not in", values, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("increase between", value1, value2, "increase");
            return (Criteria) this;
        }

        public Criteria andIncreaseNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("increase not between", value1, value2, "increase");
            return (Criteria) this;
        }

        public Criteria andDeclineIsNull() {
            addCriterion("decline is null");
            return (Criteria) this;
        }

        public Criteria andDeclineIsNotNull() {
            addCriterion("decline is not null");
            return (Criteria) this;
        }

        public Criteria andDeclineEqualTo(BigDecimal value) {
            addCriterion("decline =", value, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineNotEqualTo(BigDecimal value) {
            addCriterion("decline <>", value, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineGreaterThan(BigDecimal value) {
            addCriterion("decline >", value, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("decline >=", value, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineLessThan(BigDecimal value) {
            addCriterion("decline <", value, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineLessThanOrEqualTo(BigDecimal value) {
            addCriterion("decline <=", value, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineIn(List<BigDecimal> values) {
            addCriterion("decline in", values, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineNotIn(List<BigDecimal> values) {
            addCriterion("decline not in", values, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("decline between", value1, value2, "decline");
            return (Criteria) this;
        }

        public Criteria andDeclineNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("decline not between", value1, value2, "decline");
            return (Criteria) this;
        }

        public Criteria andVolumeIsNull() {
            addCriterion("volume is null");
            return (Criteria) this;
        }

        public Criteria andVolumeIsNotNull() {
            addCriterion("volume is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeEqualTo(BigDecimal value) {
            addCriterion("volume =", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeNotEqualTo(BigDecimal value) {
            addCriterion("volume <>", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeGreaterThan(BigDecimal value) {
            addCriterion("volume >", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("volume >=", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeLessThan(BigDecimal value) {
            addCriterion("volume <", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("volume <=", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeIn(List<BigDecimal> values) {
            addCriterion("volume in", values, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeNotIn(List<BigDecimal> values) {
            addCriterion("volume not in", values, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("volume between", value1, value2, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("volume not between", value1, value2, "volume");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("`date` is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("`date` is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(Integer value) {
            addCriterion("`date` =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(Integer value) {
            addCriterion("`date` <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(Integer value) {
            addCriterion("`date` >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(Integer value) {
            addCriterion("`date` >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(Integer value) {
            addCriterion("`date` <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(Integer value) {
            addCriterion("`date` <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<Integer> values) {
            addCriterion("`date` in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<Integer> values) {
            addCriterion("`date` not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(Integer value1, Integer value2) {
            addCriterion("`date` between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(Integer value1, Integer value2) {
            addCriterion("`date` not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceIsNull() {
            addCriterion("previous_close_price is null");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceIsNotNull() {
            addCriterion("previous_close_price is not null");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceEqualTo(BigDecimal value) {
            addCriterion("previous_close_price =", value, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceNotEqualTo(BigDecimal value) {
            addCriterion("previous_close_price <>", value, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceGreaterThan(BigDecimal value) {
            addCriterion("previous_close_price >", value, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("previous_close_price >=", value, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceLessThan(BigDecimal value) {
            addCriterion("previous_close_price <", value, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("previous_close_price <=", value, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceIn(List<BigDecimal> values) {
            addCriterion("previous_close_price in", values, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceNotIn(List<BigDecimal> values) {
            addCriterion("previous_close_price not in", values, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("previous_close_price between", value1, value2, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andPreviousClosePriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("previous_close_price not between", value1, value2, "previousClosePrice");
            return (Criteria) this;
        }

        public Criteria andDayLowIsNull() {
            addCriterion("day_low is null");
            return (Criteria) this;
        }

        public Criteria andDayLowIsNotNull() {
            addCriterion("day_low is not null");
            return (Criteria) this;
        }

        public Criteria andDayLowEqualTo(BigDecimal value) {
            addCriterion("day_low =", value, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowNotEqualTo(BigDecimal value) {
            addCriterion("day_low <>", value, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowGreaterThan(BigDecimal value) {
            addCriterion("day_low >", value, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("day_low >=", value, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowLessThan(BigDecimal value) {
            addCriterion("day_low <", value, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowLessThanOrEqualTo(BigDecimal value) {
            addCriterion("day_low <=", value, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowIn(List<BigDecimal> values) {
            addCriterion("day_low in", values, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowNotIn(List<BigDecimal> values) {
            addCriterion("day_low not in", values, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("day_low between", value1, value2, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayLowNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("day_low not between", value1, value2, "dayLow");
            return (Criteria) this;
        }

        public Criteria andDayHighIsNull() {
            addCriterion("day_high is null");
            return (Criteria) this;
        }

        public Criteria andDayHighIsNotNull() {
            addCriterion("day_high is not null");
            return (Criteria) this;
        }

        public Criteria andDayHighEqualTo(BigDecimal value) {
            addCriterion("day_high =", value, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighNotEqualTo(BigDecimal value) {
            addCriterion("day_high <>", value, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighGreaterThan(BigDecimal value) {
            addCriterion("day_high >", value, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("day_high >=", value, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighLessThan(BigDecimal value) {
            addCriterion("day_high <", value, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighLessThanOrEqualTo(BigDecimal value) {
            addCriterion("day_high <=", value, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighIn(List<BigDecimal> values) {
            addCriterion("day_high in", values, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighNotIn(List<BigDecimal> values) {
            addCriterion("day_high not in", values, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("day_high between", value1, value2, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andDayHighNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("day_high not between", value1, value2, "dayHigh");
            return (Criteria) this;
        }

        public Criteria andOpenPriceIsNull() {
            addCriterion("open_price is null");
            return (Criteria) this;
        }

        public Criteria andOpenPriceIsNotNull() {
            addCriterion("open_price is not null");
            return (Criteria) this;
        }

        public Criteria andOpenPriceEqualTo(BigDecimal value) {
            addCriterion("open_price =", value, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceNotEqualTo(BigDecimal value) {
            addCriterion("open_price <>", value, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceGreaterThan(BigDecimal value) {
            addCriterion("open_price >", value, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("open_price >=", value, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceLessThan(BigDecimal value) {
            addCriterion("open_price <", value, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("open_price <=", value, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceIn(List<BigDecimal> values) {
            addCriterion("open_price in", values, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceNotIn(List<BigDecimal> values) {
            addCriterion("open_price not in", values, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open_price between", value1, value2, "openPrice");
            return (Criteria) this;
        }

        public Criteria andOpenPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open_price not between", value1, value2, "openPrice");
            return (Criteria) this;
        }

        public Criteria andMarketCapIsNull() {
            addCriterion("market_cap is null");
            return (Criteria) this;
        }

        public Criteria andMarketCapIsNotNull() {
            addCriterion("market_cap is not null");
            return (Criteria) this;
        }

        public Criteria andMarketCapEqualTo(BigDecimal value) {
            addCriterion("market_cap =", value, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapNotEqualTo(BigDecimal value) {
            addCriterion("market_cap <>", value, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapGreaterThan(BigDecimal value) {
            addCriterion("market_cap >", value, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("market_cap >=", value, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapLessThan(BigDecimal value) {
            addCriterion("market_cap <", value, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapLessThanOrEqualTo(BigDecimal value) {
            addCriterion("market_cap <=", value, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapIn(List<BigDecimal> values) {
            addCriterion("market_cap in", values, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapNotIn(List<BigDecimal> values) {
            addCriterion("market_cap not in", values, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("market_cap between", value1, value2, "marketCap");
            return (Criteria) this;
        }

        public Criteria andMarketCapNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("market_cap not between", value1, value2, "marketCap");
            return (Criteria) this;
        }

        public Criteria andPeRatioIsNull() {
            addCriterion("pe_ratio is null");
            return (Criteria) this;
        }

        public Criteria andPeRatioIsNotNull() {
            addCriterion("pe_ratio is not null");
            return (Criteria) this;
        }

        public Criteria andPeRatioEqualTo(BigDecimal value) {
            addCriterion("pe_ratio =", value, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioNotEqualTo(BigDecimal value) {
            addCriterion("pe_ratio <>", value, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioGreaterThan(BigDecimal value) {
            addCriterion("pe_ratio >", value, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pe_ratio >=", value, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioLessThan(BigDecimal value) {
            addCriterion("pe_ratio <", value, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pe_ratio <=", value, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioIn(List<BigDecimal> values) {
            addCriterion("pe_ratio in", values, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioNotIn(List<BigDecimal> values) {
            addCriterion("pe_ratio not in", values, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pe_ratio between", value1, value2, "peRatio");
            return (Criteria) this;
        }

        public Criteria andPeRatioNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pe_ratio not between", value1, value2, "peRatio");
            return (Criteria) this;
        }

        public Criteria andEpsIsNull() {
            addCriterion("eps is null");
            return (Criteria) this;
        }

        public Criteria andEpsIsNotNull() {
            addCriterion("eps is not null");
            return (Criteria) this;
        }

        public Criteria andEpsEqualTo(BigDecimal value) {
            addCriterion("eps =", value, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsNotEqualTo(BigDecimal value) {
            addCriterion("eps <>", value, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsGreaterThan(BigDecimal value) {
            addCriterion("eps >", value, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("eps >=", value, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsLessThan(BigDecimal value) {
            addCriterion("eps <", value, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsLessThanOrEqualTo(BigDecimal value) {
            addCriterion("eps <=", value, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsIn(List<BigDecimal> values) {
            addCriterion("eps in", values, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsNotIn(List<BigDecimal> values) {
            addCriterion("eps not in", values, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eps between", value1, value2, "eps");
            return (Criteria) this;
        }

        public Criteria andEpsNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eps not between", value1, value2, "eps");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}