package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StatisticsTzjUtmExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public StatisticsTzjUtmExample() {
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

        public Criteria andDayIsNull() {
            addCriterion("`day` is null");
            return (Criteria) this;
        }

        public Criteria andDayIsNotNull() {
            addCriterion("`day` is not null");
            return (Criteria) this;
        }

        public Criteria andDayEqualTo(String value) {
            addCriterion("`day` =", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotEqualTo(String value) {
            addCriterion("`day` <>", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayGreaterThan(String value) {
            addCriterion("`day` >", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayGreaterThanOrEqualTo(String value) {
            addCriterion("`day` >=", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLessThan(String value) {
            addCriterion("`day` <", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLessThanOrEqualTo(String value) {
            addCriterion("`day` <=", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLike(String value) {
            addCriterion("`day` like", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotLike(String value) {
            addCriterion("`day` not like", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayIn(List<String> values) {
            addCriterion("`day` in", values, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotIn(List<String> values) {
            addCriterion("`day` not in", values, "day");
            return (Criteria) this;
        }

        public Criteria andDayBetween(String value1, String value2) {
            addCriterion("`day` between", value1, value2, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotBetween(String value1, String value2) {
            addCriterion("`day` not between", value1, value2, "day");
            return (Criteria) this;
        }

        public Criteria andRegistCountIsNull() {
            addCriterion("regist_count is null");
            return (Criteria) this;
        }

        public Criteria andRegistCountIsNotNull() {
            addCriterion("regist_count is not null");
            return (Criteria) this;
        }

        public Criteria andRegistCountEqualTo(Integer value) {
            addCriterion("regist_count =", value, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountNotEqualTo(Integer value) {
            addCriterion("regist_count <>", value, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountGreaterThan(Integer value) {
            addCriterion("regist_count >", value, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("regist_count >=", value, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountLessThan(Integer value) {
            addCriterion("regist_count <", value, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountLessThanOrEqualTo(Integer value) {
            addCriterion("regist_count <=", value, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountIn(List<Integer> values) {
            addCriterion("regist_count in", values, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountNotIn(List<Integer> values) {
            addCriterion("regist_count not in", values, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountBetween(Integer value1, Integer value2) {
            addCriterion("regist_count between", value1, value2, "registCount");
            return (Criteria) this;
        }

        public Criteria andRegistCountNotBetween(Integer value1, Integer value2) {
            addCriterion("regist_count not between", value1, value2, "registCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountIsNull() {
            addCriterion("open_count is null");
            return (Criteria) this;
        }

        public Criteria andOpenCountIsNotNull() {
            addCriterion("open_count is not null");
            return (Criteria) this;
        }

        public Criteria andOpenCountEqualTo(Integer value) {
            addCriterion("open_count =", value, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountNotEqualTo(Integer value) {
            addCriterion("open_count <>", value, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountGreaterThan(Integer value) {
            addCriterion("open_count >", value, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("open_count >=", value, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountLessThan(Integer value) {
            addCriterion("open_count <", value, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountLessThanOrEqualTo(Integer value) {
            addCriterion("open_count <=", value, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountIn(List<Integer> values) {
            addCriterion("open_count in", values, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountNotIn(List<Integer> values) {
            addCriterion("open_count not in", values, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountBetween(Integer value1, Integer value2) {
            addCriterion("open_count between", value1, value2, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenCountNotBetween(Integer value1, Integer value2) {
            addCriterion("open_count not between", value1, value2, "openCount");
            return (Criteria) this;
        }

        public Criteria andOpenRateIsNull() {
            addCriterion("open_rate is null");
            return (Criteria) this;
        }

        public Criteria andOpenRateIsNotNull() {
            addCriterion("open_rate is not null");
            return (Criteria) this;
        }

        public Criteria andOpenRateEqualTo(BigDecimal value) {
            addCriterion("open_rate =", value, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateNotEqualTo(BigDecimal value) {
            addCriterion("open_rate <>", value, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateGreaterThan(BigDecimal value) {
            addCriterion("open_rate >", value, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("open_rate >=", value, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateLessThan(BigDecimal value) {
            addCriterion("open_rate <", value, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("open_rate <=", value, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateIn(List<BigDecimal> values) {
            addCriterion("open_rate in", values, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateNotIn(List<BigDecimal> values) {
            addCriterion("open_rate not in", values, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open_rate between", value1, value2, "openRate");
            return (Criteria) this;
        }

        public Criteria andOpenRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open_rate not between", value1, value2, "openRate");
            return (Criteria) this;
        }

        public Criteria andCardbindCountIsNull() {
            addCriterion("cardbind_count is null");
            return (Criteria) this;
        }

        public Criteria andCardbindCountIsNotNull() {
            addCriterion("cardbind_count is not null");
            return (Criteria) this;
        }

        public Criteria andCardbindCountEqualTo(Integer value) {
            addCriterion("cardbind_count =", value, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountNotEqualTo(Integer value) {
            addCriterion("cardbind_count <>", value, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountGreaterThan(Integer value) {
            addCriterion("cardbind_count >", value, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("cardbind_count >=", value, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountLessThan(Integer value) {
            addCriterion("cardbind_count <", value, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountLessThanOrEqualTo(Integer value) {
            addCriterion("cardbind_count <=", value, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountIn(List<Integer> values) {
            addCriterion("cardbind_count in", values, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountNotIn(List<Integer> values) {
            addCriterion("cardbind_count not in", values, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountBetween(Integer value1, Integer value2) {
            addCriterion("cardbind_count between", value1, value2, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindCountNotBetween(Integer value1, Integer value2) {
            addCriterion("cardbind_count not between", value1, value2, "cardbindCount");
            return (Criteria) this;
        }

        public Criteria andCardbindRateIsNull() {
            addCriterion("cardbind_rate is null");
            return (Criteria) this;
        }

        public Criteria andCardbindRateIsNotNull() {
            addCriterion("cardbind_rate is not null");
            return (Criteria) this;
        }

        public Criteria andCardbindRateEqualTo(BigDecimal value) {
            addCriterion("cardbind_rate =", value, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateNotEqualTo(BigDecimal value) {
            addCriterion("cardbind_rate <>", value, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateGreaterThan(BigDecimal value) {
            addCriterion("cardbind_rate >", value, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cardbind_rate >=", value, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateLessThan(BigDecimal value) {
            addCriterion("cardbind_rate <", value, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cardbind_rate <=", value, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateIn(List<BigDecimal> values) {
            addCriterion("cardbind_rate in", values, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateNotIn(List<BigDecimal> values) {
            addCriterion("cardbind_rate not in", values, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cardbind_rate between", value1, value2, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andCardbindRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cardbind_rate not between", value1, value2, "cardbindRate");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountIsNull() {
            addCriterion("rechargenew_count is null");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountIsNotNull() {
            addCriterion("rechargenew_count is not null");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountEqualTo(Integer value) {
            addCriterion("rechargenew_count =", value, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountNotEqualTo(Integer value) {
            addCriterion("rechargenew_count <>", value, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountGreaterThan(Integer value) {
            addCriterion("rechargenew_count >", value, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("rechargenew_count >=", value, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountLessThan(Integer value) {
            addCriterion("rechargenew_count <", value, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountLessThanOrEqualTo(Integer value) {
            addCriterion("rechargenew_count <=", value, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountIn(List<Integer> values) {
            addCriterion("rechargenew_count in", values, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountNotIn(List<Integer> values) {
            addCriterion("rechargenew_count not in", values, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountBetween(Integer value1, Integer value2) {
            addCriterion("rechargenew_count between", value1, value2, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargenewCountNotBetween(Integer value1, Integer value2) {
            addCriterion("rechargenew_count not between", value1, value2, "rechargenewCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountIsNull() {
            addCriterion("recharge_count is null");
            return (Criteria) this;
        }

        public Criteria andRechargeCountIsNotNull() {
            addCriterion("recharge_count is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeCountEqualTo(Integer value) {
            addCriterion("recharge_count =", value, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountNotEqualTo(Integer value) {
            addCriterion("recharge_count <>", value, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountGreaterThan(Integer value) {
            addCriterion("recharge_count >", value, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("recharge_count >=", value, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountLessThan(Integer value) {
            addCriterion("recharge_count <", value, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountLessThanOrEqualTo(Integer value) {
            addCriterion("recharge_count <=", value, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountIn(List<Integer> values) {
            addCriterion("recharge_count in", values, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountNotIn(List<Integer> values) {
            addCriterion("recharge_count not in", values, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountBetween(Integer value1, Integer value2) {
            addCriterion("recharge_count between", value1, value2, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andRechargeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("recharge_count not between", value1, value2, "rechargeCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountIsNull() {
            addCriterion("tendernew_count is null");
            return (Criteria) this;
        }

        public Criteria andTendernewCountIsNotNull() {
            addCriterion("tendernew_count is not null");
            return (Criteria) this;
        }

        public Criteria andTendernewCountEqualTo(Integer value) {
            addCriterion("tendernew_count =", value, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountNotEqualTo(Integer value) {
            addCriterion("tendernew_count <>", value, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountGreaterThan(Integer value) {
            addCriterion("tendernew_count >", value, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("tendernew_count >=", value, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountLessThan(Integer value) {
            addCriterion("tendernew_count <", value, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountLessThanOrEqualTo(Integer value) {
            addCriterion("tendernew_count <=", value, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountIn(List<Integer> values) {
            addCriterion("tendernew_count in", values, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountNotIn(List<Integer> values) {
            addCriterion("tendernew_count not in", values, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountBetween(Integer value1, Integer value2) {
            addCriterion("tendernew_count between", value1, value2, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTendernewCountNotBetween(Integer value1, Integer value2) {
            addCriterion("tendernew_count not between", value1, value2, "tendernewCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountIsNull() {
            addCriterion("tenderfirst_count is null");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountIsNotNull() {
            addCriterion("tenderfirst_count is not null");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountEqualTo(Integer value) {
            addCriterion("tenderfirst_count =", value, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountNotEqualTo(Integer value) {
            addCriterion("tenderfirst_count <>", value, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountGreaterThan(Integer value) {
            addCriterion("tenderfirst_count >", value, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("tenderfirst_count >=", value, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountLessThan(Integer value) {
            addCriterion("tenderfirst_count <", value, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountLessThanOrEqualTo(Integer value) {
            addCriterion("tenderfirst_count <=", value, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountIn(List<Integer> values) {
            addCriterion("tenderfirst_count in", values, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountNotIn(List<Integer> values) {
            addCriterion("tenderfirst_count not in", values, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountBetween(Integer value1, Integer value2) {
            addCriterion("tenderfirst_count between", value1, value2, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstCountNotBetween(Integer value1, Integer value2) {
            addCriterion("tenderfirst_count not between", value1, value2, "tenderfirstCount");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyIsNull() {
            addCriterion("tenderfirst_money is null");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyIsNotNull() {
            addCriterion("tenderfirst_money is not null");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyEqualTo(BigDecimal value) {
            addCriterion("tenderfirst_money =", value, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyNotEqualTo(BigDecimal value) {
            addCriterion("tenderfirst_money <>", value, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyGreaterThan(BigDecimal value) {
            addCriterion("tenderfirst_money >", value, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tenderfirst_money >=", value, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyLessThan(BigDecimal value) {
            addCriterion("tenderfirst_money <", value, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tenderfirst_money <=", value, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyIn(List<BigDecimal> values) {
            addCriterion("tenderfirst_money in", values, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyNotIn(List<BigDecimal> values) {
            addCriterion("tenderfirst_money not in", values, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tenderfirst_money between", value1, value2, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTenderfirstMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tenderfirst_money not between", value1, value2, "tenderfirstMoney");
            return (Criteria) this;
        }

        public Criteria andTendernewRateIsNull() {
            addCriterion("tendernew_rate is null");
            return (Criteria) this;
        }

        public Criteria andTendernewRateIsNotNull() {
            addCriterion("tendernew_rate is not null");
            return (Criteria) this;
        }

        public Criteria andTendernewRateEqualTo(BigDecimal value) {
            addCriterion("tendernew_rate =", value, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateNotEqualTo(BigDecimal value) {
            addCriterion("tendernew_rate <>", value, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateGreaterThan(BigDecimal value) {
            addCriterion("tendernew_rate >", value, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tendernew_rate >=", value, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateLessThan(BigDecimal value) {
            addCriterion("tendernew_rate <", value, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tendernew_rate <=", value, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateIn(List<BigDecimal> values) {
            addCriterion("tendernew_rate in", values, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateNotIn(List<BigDecimal> values) {
            addCriterion("tendernew_rate not in", values, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tendernew_rate between", value1, value2, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTendernewRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tendernew_rate not between", value1, value2, "tendernewRate");
            return (Criteria) this;
        }

        public Criteria andTenderCountIsNull() {
            addCriterion("tender_count is null");
            return (Criteria) this;
        }

        public Criteria andTenderCountIsNotNull() {
            addCriterion("tender_count is not null");
            return (Criteria) this;
        }

        public Criteria andTenderCountEqualTo(Integer value) {
            addCriterion("tender_count =", value, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountNotEqualTo(Integer value) {
            addCriterion("tender_count <>", value, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountGreaterThan(Integer value) {
            addCriterion("tender_count >", value, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_count >=", value, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountLessThan(Integer value) {
            addCriterion("tender_count <", value, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountLessThanOrEqualTo(Integer value) {
            addCriterion("tender_count <=", value, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountIn(List<Integer> values) {
            addCriterion("tender_count in", values, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountNotIn(List<Integer> values) {
            addCriterion("tender_count not in", values, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountBetween(Integer value1, Integer value2) {
            addCriterion("tender_count between", value1, value2, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderCountNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_count not between", value1, value2, "tenderCount");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyIsNull() {
            addCriterion("tender_money is null");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyIsNotNull() {
            addCriterion("tender_money is not null");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyEqualTo(BigDecimal value) {
            addCriterion("tender_money =", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyNotEqualTo(BigDecimal value) {
            addCriterion("tender_money <>", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyGreaterThan(BigDecimal value) {
            addCriterion("tender_money >", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_money >=", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyLessThan(BigDecimal value) {
            addCriterion("tender_money <", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_money <=", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyIn(List<BigDecimal> values) {
            addCriterion("tender_money in", values, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyNotIn(List<BigDecimal> values) {
            addCriterion("tender_money not in", values, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_money between", value1, value2, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_money not between", value1, value2, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountIsNull() {
            addCriterion("tender_again_count is null");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountIsNotNull() {
            addCriterion("tender_again_count is not null");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountEqualTo(Integer value) {
            addCriterion("tender_again_count =", value, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountNotEqualTo(Integer value) {
            addCriterion("tender_again_count <>", value, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountGreaterThan(Integer value) {
            addCriterion("tender_again_count >", value, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_again_count >=", value, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountLessThan(Integer value) {
            addCriterion("tender_again_count <", value, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountLessThanOrEqualTo(Integer value) {
            addCriterion("tender_again_count <=", value, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountIn(List<Integer> values) {
            addCriterion("tender_again_count in", values, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountNotIn(List<Integer> values) {
            addCriterion("tender_again_count not in", values, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountBetween(Integer value1, Integer value2) {
            addCriterion("tender_again_count between", value1, value2, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainCountNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_again_count not between", value1, value2, "tenderAgainCount");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateIsNull() {
            addCriterion("tender_again_rate is null");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateIsNotNull() {
            addCriterion("tender_again_rate is not null");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateEqualTo(BigDecimal value) {
            addCriterion("tender_again_rate =", value, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateNotEqualTo(BigDecimal value) {
            addCriterion("tender_again_rate <>", value, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateGreaterThan(BigDecimal value) {
            addCriterion("tender_again_rate >", value, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_again_rate >=", value, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateLessThan(BigDecimal value) {
            addCriterion("tender_again_rate <", value, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_again_rate <=", value, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateIn(List<BigDecimal> values) {
            addCriterion("tender_again_rate in", values, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateNotIn(List<BigDecimal> values) {
            addCriterion("tender_again_rate not in", values, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_again_rate between", value1, value2, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andTenderAgainRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_again_rate not between", value1, value2, "tenderAgainRate");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNull() {
            addCriterion("channel_name is null");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNotNull() {
            addCriterion("channel_name is not null");
            return (Criteria) this;
        }

        public Criteria andChannelNameEqualTo(String value) {
            addCriterion("channel_name =", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotEqualTo(String value) {
            addCriterion("channel_name <>", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThan(String value) {
            addCriterion("channel_name >", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThanOrEqualTo(String value) {
            addCriterion("channel_name >=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThan(String value) {
            addCriterion("channel_name <", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThanOrEqualTo(String value) {
            addCriterion("channel_name <=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLike(String value) {
            addCriterion("channel_name like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotLike(String value) {
            addCriterion("channel_name not like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameIn(List<String> values) {
            addCriterion("channel_name in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotIn(List<String> values) {
            addCriterion("channel_name not in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameBetween(String value1, String value2) {
            addCriterion("channel_name between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotBetween(String value1, String value2) {
            addCriterion("channel_name not between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelFlgIsNull() {
            addCriterion("channel_flg is null");
            return (Criteria) this;
        }

        public Criteria andChannelFlgIsNotNull() {
            addCriterion("channel_flg is not null");
            return (Criteria) this;
        }

        public Criteria andChannelFlgEqualTo(Integer value) {
            addCriterion("channel_flg =", value, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgNotEqualTo(Integer value) {
            addCriterion("channel_flg <>", value, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgGreaterThan(Integer value) {
            addCriterion("channel_flg >", value, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("channel_flg >=", value, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgLessThan(Integer value) {
            addCriterion("channel_flg <", value, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgLessThanOrEqualTo(Integer value) {
            addCriterion("channel_flg <=", value, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgIn(List<Integer> values) {
            addCriterion("channel_flg in", values, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgNotIn(List<Integer> values) {
            addCriterion("channel_flg not in", values, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgBetween(Integer value1, Integer value2) {
            addCriterion("channel_flg between", value1, value2, "channelFlg");
            return (Criteria) this;
        }

        public Criteria andChannelFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("channel_flg not between", value1, value2, "channelFlg");
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

        public Criteria andAddTimeEqualTo(Integer value) {
            addCriterion("add_time =", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotEqualTo(Integer value) {
            addCriterion("add_time <>", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThan(Integer value) {
            addCriterion("add_time >", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("add_time >=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThan(Integer value) {
            addCriterion("add_time <", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThanOrEqualTo(Integer value) {
            addCriterion("add_time <=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeIn(List<Integer> values) {
            addCriterion("add_time in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotIn(List<Integer> values) {
            addCriterion("add_time not in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeBetween(Integer value1, Integer value2) {
            addCriterion("add_time between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("add_time not between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Integer value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Integer value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Integer value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Integer value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Integer> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Integer> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Integer value1, Integer value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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