package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PcChannelStatisticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public PcChannelStatisticsExample() {
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

        public Criteria andSourceIdIsNull() {
            addCriterion("source_id is null");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNotNull() {
            addCriterion("source_id is not null");
            return (Criteria) this;
        }

        public Criteria andSourceIdEqualTo(Integer value) {
            addCriterion("source_id =", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotEqualTo(Integer value) {
            addCriterion("source_id <>", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThan(Integer value) {
            addCriterion("source_id >", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("source_id >=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThan(Integer value) {
            addCriterion("source_id <", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThanOrEqualTo(Integer value) {
            addCriterion("source_id <=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdIn(List<Integer> values) {
            addCriterion("source_id in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotIn(List<Integer> values) {
            addCriterion("source_id not in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdBetween(Integer value1, Integer value2) {
            addCriterion("source_id between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotBetween(Integer value1, Integer value2) {
            addCriterion("source_id not between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceNameIsNull() {
            addCriterion("source_name is null");
            return (Criteria) this;
        }

        public Criteria andSourceNameIsNotNull() {
            addCriterion("source_name is not null");
            return (Criteria) this;
        }

        public Criteria andSourceNameEqualTo(String value) {
            addCriterion("source_name =", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotEqualTo(String value) {
            addCriterion("source_name <>", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThan(String value) {
            addCriterion("source_name >", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThanOrEqualTo(String value) {
            addCriterion("source_name >=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThan(String value) {
            addCriterion("source_name <", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThanOrEqualTo(String value) {
            addCriterion("source_name <=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLike(String value) {
            addCriterion("source_name like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotLike(String value) {
            addCriterion("source_name not like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameIn(List<String> values) {
            addCriterion("source_name in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotIn(List<String> values) {
            addCriterion("source_name not in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameBetween(String value1, String value2) {
            addCriterion("source_name between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotBetween(String value1, String value2) {
            addCriterion("source_name not between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andAccessNumberIsNull() {
            addCriterion("access_number is null");
            return (Criteria) this;
        }

        public Criteria andAccessNumberIsNotNull() {
            addCriterion("access_number is not null");
            return (Criteria) this;
        }

        public Criteria andAccessNumberEqualTo(Integer value) {
            addCriterion("access_number =", value, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberNotEqualTo(Integer value) {
            addCriterion("access_number <>", value, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberGreaterThan(Integer value) {
            addCriterion("access_number >", value, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("access_number >=", value, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberLessThan(Integer value) {
            addCriterion("access_number <", value, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberLessThanOrEqualTo(Integer value) {
            addCriterion("access_number <=", value, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberIn(List<Integer> values) {
            addCriterion("access_number in", values, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberNotIn(List<Integer> values) {
            addCriterion("access_number not in", values, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberBetween(Integer value1, Integer value2) {
            addCriterion("access_number between", value1, value2, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andAccessNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("access_number not between", value1, value2, "accessNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberIsNull() {
            addCriterion("regist_number is null");
            return (Criteria) this;
        }

        public Criteria andRegistNumberIsNotNull() {
            addCriterion("regist_number is not null");
            return (Criteria) this;
        }

        public Criteria andRegistNumberEqualTo(Integer value) {
            addCriterion("regist_number =", value, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberNotEqualTo(Integer value) {
            addCriterion("regist_number <>", value, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberGreaterThan(Integer value) {
            addCriterion("regist_number >", value, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("regist_number >=", value, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberLessThan(Integer value) {
            addCriterion("regist_number <", value, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberLessThanOrEqualTo(Integer value) {
            addCriterion("regist_number <=", value, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberIn(List<Integer> values) {
            addCriterion("regist_number in", values, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberNotIn(List<Integer> values) {
            addCriterion("regist_number not in", values, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberBetween(Integer value1, Integer value2) {
            addCriterion("regist_number between", value1, value2, "registNumber");
            return (Criteria) this;
        }

        public Criteria andRegistNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("regist_number not between", value1, value2, "registNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberIsNull() {
            addCriterion("open_account_number is null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberIsNotNull() {
            addCriterion("open_account_number is not null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberEqualTo(Integer value) {
            addCriterion("open_account_number =", value, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberNotEqualTo(Integer value) {
            addCriterion("open_account_number <>", value, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberGreaterThan(Integer value) {
            addCriterion("open_account_number >", value, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("open_account_number >=", value, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberLessThan(Integer value) {
            addCriterion("open_account_number <", value, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberLessThanOrEqualTo(Integer value) {
            addCriterion("open_account_number <=", value, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberIn(List<Integer> values) {
            addCriterion("open_account_number in", values, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberNotIn(List<Integer> values) {
            addCriterion("open_account_number not in", values, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberBetween(Integer value1, Integer value2) {
            addCriterion("open_account_number between", value1, value2, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("open_account_number not between", value1, value2, "openAccountNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIsNull() {
            addCriterion("tender_number is null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIsNotNull() {
            addCriterion("tender_number is not null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberEqualTo(Integer value) {
            addCriterion("tender_number =", value, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberNotEqualTo(Integer value) {
            addCriterion("tender_number <>", value, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberGreaterThan(Integer value) {
            addCriterion("tender_number >", value, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_number >=", value, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberLessThan(Integer value) {
            addCriterion("tender_number <", value, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberLessThanOrEqualTo(Integer value) {
            addCriterion("tender_number <=", value, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIn(List<Integer> values) {
            addCriterion("tender_number in", values, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberNotIn(List<Integer> values) {
            addCriterion("tender_number not in", values, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberBetween(Integer value1, Integer value2) {
            addCriterion("tender_number between", value1, value2, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andTenderNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_number not between", value1, value2, "tenderNumber");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeIsNull() {
            addCriterion("cumulative_recharge is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeIsNotNull() {
            addCriterion("cumulative_recharge is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeEqualTo(BigDecimal value) {
            addCriterion("cumulative_recharge =", value, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_recharge <>", value, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeGreaterThan(BigDecimal value) {
            addCriterion("cumulative_recharge >", value, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_recharge >=", value, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeLessThan(BigDecimal value) {
            addCriterion("cumulative_recharge <", value, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_recharge <=", value, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeIn(List<BigDecimal> values) {
            addCriterion("cumulative_recharge in", values, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_recharge not in", values, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_recharge between", value1, value2, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeRechargeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_recharge not between", value1, value2, "cumulativeRecharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentIsNull() {
            addCriterion("cumulative_investment is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentIsNotNull() {
            addCriterion("cumulative_investment is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentEqualTo(BigDecimal value) {
            addCriterion("cumulative_investment =", value, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_investment <>", value, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentGreaterThan(BigDecimal value) {
            addCriterion("cumulative_investment >", value, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_investment >=", value, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentLessThan(BigDecimal value) {
            addCriterion("cumulative_investment <", value, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_investment <=", value, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentIn(List<BigDecimal> values) {
            addCriterion("cumulative_investment in", values, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_investment not in", values, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_investment between", value1, value2, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestmentNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_investment not between", value1, value2, "cumulativeInvestment");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceIsNull() {
            addCriterion("hzt_tender_price is null");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceIsNotNull() {
            addCriterion("hzt_tender_price is not null");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceEqualTo(BigDecimal value) {
            addCriterion("hzt_tender_price =", value, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceNotEqualTo(BigDecimal value) {
            addCriterion("hzt_tender_price <>", value, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceGreaterThan(BigDecimal value) {
            addCriterion("hzt_tender_price >", value, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hzt_tender_price >=", value, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceLessThan(BigDecimal value) {
            addCriterion("hzt_tender_price <", value, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hzt_tender_price <=", value, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceIn(List<BigDecimal> values) {
            addCriterion("hzt_tender_price in", values, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceNotIn(List<BigDecimal> values) {
            addCriterion("hzt_tender_price not in", values, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzt_tender_price between", value1, value2, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHztTenderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzt_tender_price not between", value1, value2, "hztTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceIsNull() {
            addCriterion("hxf_tender_price is null");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceIsNotNull() {
            addCriterion("hxf_tender_price is not null");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceEqualTo(BigDecimal value) {
            addCriterion("hxf_tender_price =", value, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceNotEqualTo(BigDecimal value) {
            addCriterion("hxf_tender_price <>", value, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceGreaterThan(BigDecimal value) {
            addCriterion("hxf_tender_price >", value, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hxf_tender_price >=", value, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceLessThan(BigDecimal value) {
            addCriterion("hxf_tender_price <", value, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hxf_tender_price <=", value, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceIn(List<BigDecimal> values) {
            addCriterion("hxf_tender_price in", values, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceNotIn(List<BigDecimal> values) {
            addCriterion("hxf_tender_price not in", values, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hxf_tender_price between", value1, value2, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHxfTenderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hxf_tender_price not between", value1, value2, "hxfTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceIsNull() {
            addCriterion("htl_tender_price is null");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceIsNotNull() {
            addCriterion("htl_tender_price is not null");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceEqualTo(BigDecimal value) {
            addCriterion("htl_tender_price =", value, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceNotEqualTo(BigDecimal value) {
            addCriterion("htl_tender_price <>", value, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceGreaterThan(BigDecimal value) {
            addCriterion("htl_tender_price >", value, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("htl_tender_price >=", value, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceLessThan(BigDecimal value) {
            addCriterion("htl_tender_price <", value, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("htl_tender_price <=", value, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceIn(List<BigDecimal> values) {
            addCriterion("htl_tender_price in", values, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceNotIn(List<BigDecimal> values) {
            addCriterion("htl_tender_price not in", values, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htl_tender_price between", value1, value2, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtlTenderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htl_tender_price not between", value1, value2, "htlTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceIsNull() {
            addCriterion("htj_tender_price is null");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceIsNotNull() {
            addCriterion("htj_tender_price is not null");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceEqualTo(BigDecimal value) {
            addCriterion("htj_tender_price =", value, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceNotEqualTo(BigDecimal value) {
            addCriterion("htj_tender_price <>", value, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceGreaterThan(BigDecimal value) {
            addCriterion("htj_tender_price >", value, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("htj_tender_price >=", value, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceLessThan(BigDecimal value) {
            addCriterion("htj_tender_price <", value, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("htj_tender_price <=", value, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceIn(List<BigDecimal> values) {
            addCriterion("htj_tender_price in", values, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceNotIn(List<BigDecimal> values) {
            addCriterion("htj_tender_price not in", values, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htj_tender_price between", value1, value2, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHtjTenderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htj_tender_price not between", value1, value2, "htjTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceIsNull() {
            addCriterion("rtb_tender_price is null");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceIsNotNull() {
            addCriterion("rtb_tender_price is not null");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceEqualTo(BigDecimal value) {
            addCriterion("rtb_tender_price =", value, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceNotEqualTo(BigDecimal value) {
            addCriterion("rtb_tender_price <>", value, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceGreaterThan(BigDecimal value) {
            addCriterion("rtb_tender_price >", value, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rtb_tender_price >=", value, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceLessThan(BigDecimal value) {
            addCriterion("rtb_tender_price <", value, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rtb_tender_price <=", value, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceIn(List<BigDecimal> values) {
            addCriterion("rtb_tender_price in", values, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceNotIn(List<BigDecimal> values) {
            addCriterion("rtb_tender_price not in", values, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rtb_tender_price between", value1, value2, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andRtbTenderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rtb_tender_price not between", value1, value2, "rtbTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceIsNull() {
            addCriterion("hzr_tender_price is null");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceIsNotNull() {
            addCriterion("hzr_tender_price is not null");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceEqualTo(BigDecimal value) {
            addCriterion("hzr_tender_price =", value, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceNotEqualTo(BigDecimal value) {
            addCriterion("hzr_tender_price <>", value, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceGreaterThan(BigDecimal value) {
            addCriterion("hzr_tender_price >", value, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hzr_tender_price >=", value, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceLessThan(BigDecimal value) {
            addCriterion("hzr_tender_price <", value, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hzr_tender_price <=", value, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceIn(List<BigDecimal> values) {
            addCriterion("hzr_tender_price in", values, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceNotIn(List<BigDecimal> values) {
            addCriterion("hzr_tender_price not in", values, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzr_tender_price between", value1, value2, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andHzrTenderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzr_tender_price not between", value1, value2, "hzrTenderPrice");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNull() {
            addCriterion("add_time is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNotNull() {
            addCriterion("add_time is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEqualTo(Date value) {
            addCriterion("add_time =", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotEqualTo(Date value) {
            addCriterion("add_time <>", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThan(Date value) {
            addCriterion("add_time >", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("add_time >=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThan(Date value) {
            addCriterion("add_time <", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThanOrEqualTo(Date value) {
            addCriterion("add_time <=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeIn(List<Date> values) {
            addCriterion("add_time in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotIn(List<Date> values) {
            addCriterion("add_time not in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeBetween(Date value1, Date value2) {
            addCriterion("add_time between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotBetween(Date value1, Date value2) {
            addCriterion("add_time not between", value1, value2, "addTime");
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