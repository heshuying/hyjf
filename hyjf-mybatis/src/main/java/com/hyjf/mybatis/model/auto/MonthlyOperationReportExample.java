package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MonthlyOperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public MonthlyOperationReportExample() {
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

        public Criteria andOperationReportIdIsNull() {
            addCriterion("operation_report_id is null");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdIsNotNull() {
            addCriterion("operation_report_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdEqualTo(Integer value) {
            addCriterion("operation_report_id =", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdNotEqualTo(Integer value) {
            addCriterion("operation_report_id <>", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdGreaterThan(Integer value) {
            addCriterion("operation_report_id >", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("operation_report_id >=", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdLessThan(Integer value) {
            addCriterion("operation_report_id <", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdLessThanOrEqualTo(Integer value) {
            addCriterion("operation_report_id <=", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdIn(List<Integer> values) {
            addCriterion("operation_report_id in", values, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdNotIn(List<Integer> values) {
            addCriterion("operation_report_id not in", values, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_id between", value1, value2, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdNotBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_id not between", value1, value2, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andCnNameIsNull() {
            addCriterion("cn_name is null");
            return (Criteria) this;
        }

        public Criteria andCnNameIsNotNull() {
            addCriterion("cn_name is not null");
            return (Criteria) this;
        }

        public Criteria andCnNameEqualTo(String value) {
            addCriterion("cn_name =", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotEqualTo(String value) {
            addCriterion("cn_name <>", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameGreaterThan(String value) {
            addCriterion("cn_name >", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameGreaterThanOrEqualTo(String value) {
            addCriterion("cn_name >=", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameLessThan(String value) {
            addCriterion("cn_name <", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameLessThanOrEqualTo(String value) {
            addCriterion("cn_name <=", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameLike(String value) {
            addCriterion("cn_name like", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotLike(String value) {
            addCriterion("cn_name not like", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameIn(List<String> values) {
            addCriterion("cn_name in", values, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotIn(List<String> values) {
            addCriterion("cn_name not in", values, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameBetween(String value1, String value2) {
            addCriterion("cn_name between", value1, value2, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotBetween(String value1, String value2) {
            addCriterion("cn_name not between", value1, value2, "cnName");
            return (Criteria) this;
        }

        public Criteria andEnNameIsNull() {
            addCriterion("en_name is null");
            return (Criteria) this;
        }

        public Criteria andEnNameIsNotNull() {
            addCriterion("en_name is not null");
            return (Criteria) this;
        }

        public Criteria andEnNameEqualTo(String value) {
            addCriterion("en_name =", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotEqualTo(String value) {
            addCriterion("en_name <>", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameGreaterThan(String value) {
            addCriterion("en_name >", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameGreaterThanOrEqualTo(String value) {
            addCriterion("en_name >=", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameLessThan(String value) {
            addCriterion("en_name <", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameLessThanOrEqualTo(String value) {
            addCriterion("en_name <=", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameLike(String value) {
            addCriterion("en_name like", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotLike(String value) {
            addCriterion("en_name not like", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameIn(List<String> values) {
            addCriterion("en_name in", values, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotIn(List<String> values) {
            addCriterion("en_name not in", values, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameBetween(String value1, String value2) {
            addCriterion("en_name between", value1, value2, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotBetween(String value1, String value2) {
            addCriterion("en_name not between", value1, value2, "enName");
            return (Criteria) this;
        }

        public Criteria andMonthIsNull() {
            addCriterion("`month` is null");
            return (Criteria) this;
        }

        public Criteria andMonthIsNotNull() {
            addCriterion("`month` is not null");
            return (Criteria) this;
        }

        public Criteria andMonthEqualTo(Integer value) {
            addCriterion("`month` =", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthNotEqualTo(Integer value) {
            addCriterion("`month` <>", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthGreaterThan(Integer value) {
            addCriterion("`month` >", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthGreaterThanOrEqualTo(Integer value) {
            addCriterion("`month` >=", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthLessThan(Integer value) {
            addCriterion("`month` <", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthLessThanOrEqualTo(Integer value) {
            addCriterion("`month` <=", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthIn(List<Integer> values) {
            addCriterion("`month` in", values, "month");
            return (Criteria) this;
        }

        public Criteria andMonthNotIn(List<Integer> values) {
            addCriterion("`month` not in", values, "month");
            return (Criteria) this;
        }

        public Criteria andMonthBetween(Integer value1, Integer value2) {
            addCriterion("`month` between", value1, value2, "month");
            return (Criteria) this;
        }

        public Criteria andMonthNotBetween(Integer value1, Integer value2) {
            addCriterion("`month` not between", value1, value2, "month");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountIsNull() {
            addCriterion("last_year_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountIsNotNull() {
            addCriterion("last_year_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountEqualTo(Long value) {
            addCriterion("last_year_month_amount =", value, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountNotEqualTo(Long value) {
            addCriterion("last_year_month_amount <>", value, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountGreaterThan(Long value) {
            addCriterion("last_year_month_amount >", value, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("last_year_month_amount >=", value, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountLessThan(Long value) {
            addCriterion("last_year_month_amount <", value, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountLessThanOrEqualTo(Long value) {
            addCriterion("last_year_month_amount <=", value, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountIn(List<Long> values) {
            addCriterion("last_year_month_amount in", values, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountNotIn(List<Long> values) {
            addCriterion("last_year_month_amount not in", values, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountBetween(Long value1, Long value2) {
            addCriterion("last_year_month_amount between", value1, value2, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthAmountNotBetween(Long value1, Long value2) {
            addCriterion("last_year_month_amount not between", value1, value2, "lastYearMonthAmount");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseIsNull() {
            addCriterion("amount_increase is null");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseIsNotNull() {
            addCriterion("amount_increase is not null");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseEqualTo(BigDecimal value) {
            addCriterion("amount_increase =", value, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseNotEqualTo(BigDecimal value) {
            addCriterion("amount_increase <>", value, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseGreaterThan(BigDecimal value) {
            addCriterion("amount_increase >", value, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_increase >=", value, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseLessThan(BigDecimal value) {
            addCriterion("amount_increase <", value, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_increase <=", value, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseIn(List<BigDecimal> values) {
            addCriterion("amount_increase in", values, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseNotIn(List<BigDecimal> values) {
            addCriterion("amount_increase not in", values, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_increase between", value1, value2, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andAmountIncreaseNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_increase not between", value1, value2, "amountIncrease");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitIsNull() {
            addCriterion("last_year_month_profit is null");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitIsNotNull() {
            addCriterion("last_year_month_profit is not null");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitEqualTo(Long value) {
            addCriterion("last_year_month_profit =", value, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitNotEqualTo(Long value) {
            addCriterion("last_year_month_profit <>", value, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitGreaterThan(Long value) {
            addCriterion("last_year_month_profit >", value, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("last_year_month_profit >=", value, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitLessThan(Long value) {
            addCriterion("last_year_month_profit <", value, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitLessThanOrEqualTo(Long value) {
            addCriterion("last_year_month_profit <=", value, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitIn(List<Long> values) {
            addCriterion("last_year_month_profit in", values, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitNotIn(List<Long> values) {
            addCriterion("last_year_month_profit not in", values, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitBetween(Long value1, Long value2) {
            addCriterion("last_year_month_profit between", value1, value2, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearMonthProfitNotBetween(Long value1, Long value2) {
            addCriterion("last_year_month_profit not between", value1, value2, "lastYearMonthProfit");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseIsNull() {
            addCriterion("profit_increase is null");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseIsNotNull() {
            addCriterion("profit_increase is not null");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseEqualTo(BigDecimal value) {
            addCriterion("profit_increase =", value, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseNotEqualTo(BigDecimal value) {
            addCriterion("profit_increase <>", value, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseGreaterThan(BigDecimal value) {
            addCriterion("profit_increase >", value, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("profit_increase >=", value, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseLessThan(BigDecimal value) {
            addCriterion("profit_increase <", value, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseLessThanOrEqualTo(BigDecimal value) {
            addCriterion("profit_increase <=", value, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseIn(List<BigDecimal> values) {
            addCriterion("profit_increase in", values, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseNotIn(List<BigDecimal> values) {
            addCriterion("profit_increase not in", values, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("profit_increase between", value1, value2, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andProfitIncreaseNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("profit_increase not between", value1, value2, "profitIncrease");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitIsNull() {
            addCriterion("month_avg_profit is null");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitIsNotNull() {
            addCriterion("month_avg_profit is not null");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitEqualTo(BigDecimal value) {
            addCriterion("month_avg_profit =", value, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitNotEqualTo(BigDecimal value) {
            addCriterion("month_avg_profit <>", value, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitGreaterThan(BigDecimal value) {
            addCriterion("month_avg_profit >", value, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("month_avg_profit >=", value, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitLessThan(BigDecimal value) {
            addCriterion("month_avg_profit <", value, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("month_avg_profit <=", value, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitIn(List<BigDecimal> values) {
            addCriterion("month_avg_profit in", values, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitNotIn(List<BigDecimal> values) {
            addCriterion("month_avg_profit not in", values, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_avg_profit between", value1, value2, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAvgProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_avg_profit not between", value1, value2, "monthAvgProfit");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumIsNull() {
            addCriterion("month_app_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumIsNotNull() {
            addCriterion("month_app_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumEqualTo(Integer value) {
            addCriterion("month_app_deal_num =", value, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumNotEqualTo(Integer value) {
            addCriterion("month_app_deal_num <>", value, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumGreaterThan(Integer value) {
            addCriterion("month_app_deal_num >", value, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("month_app_deal_num >=", value, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumLessThan(Integer value) {
            addCriterion("month_app_deal_num <", value, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("month_app_deal_num <=", value, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumIn(List<Integer> values) {
            addCriterion("month_app_deal_num in", values, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumNotIn(List<Integer> values) {
            addCriterion("month_app_deal_num not in", values, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumBetween(Integer value1, Integer value2) {
            addCriterion("month_app_deal_num between", value1, value2, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("month_app_deal_num not between", value1, value2, "monthAppDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionIsNull() {
            addCriterion("month_app_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionIsNotNull() {
            addCriterion("month_app_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionEqualTo(BigDecimal value) {
            addCriterion("month_app_deal_proportion =", value, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("month_app_deal_proportion <>", value, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionGreaterThan(BigDecimal value) {
            addCriterion("month_app_deal_proportion >", value, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("month_app_deal_proportion >=", value, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionLessThan(BigDecimal value) {
            addCriterion("month_app_deal_proportion <", value, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("month_app_deal_proportion <=", value, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionIn(List<BigDecimal> values) {
            addCriterion("month_app_deal_proportion in", values, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("month_app_deal_proportion not in", values, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_app_deal_proportion between", value1, value2, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthAppDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_app_deal_proportion not between", value1, value2, "monthAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumIsNull() {
            addCriterion("month_wechat_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumIsNotNull() {
            addCriterion("month_wechat_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumEqualTo(Integer value) {
            addCriterion("month_wechat_deal_num =", value, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumNotEqualTo(Integer value) {
            addCriterion("month_wechat_deal_num <>", value, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumGreaterThan(Integer value) {
            addCriterion("month_wechat_deal_num >", value, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("month_wechat_deal_num >=", value, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumLessThan(Integer value) {
            addCriterion("month_wechat_deal_num <", value, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("month_wechat_deal_num <=", value, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumIn(List<Integer> values) {
            addCriterion("month_wechat_deal_num in", values, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumNotIn(List<Integer> values) {
            addCriterion("month_wechat_deal_num not in", values, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumBetween(Integer value1, Integer value2) {
            addCriterion("month_wechat_deal_num between", value1, value2, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("month_wechat_deal_num not between", value1, value2, "monthWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionIsNull() {
            addCriterion("month_wechat_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionIsNotNull() {
            addCriterion("month_wechat_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionEqualTo(BigDecimal value) {
            addCriterion("month_wechat_deal_proportion =", value, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("month_wechat_deal_proportion <>", value, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionGreaterThan(BigDecimal value) {
            addCriterion("month_wechat_deal_proportion >", value, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("month_wechat_deal_proportion >=", value, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionLessThan(BigDecimal value) {
            addCriterion("month_wechat_deal_proportion <", value, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("month_wechat_deal_proportion <=", value, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionIn(List<BigDecimal> values) {
            addCriterion("month_wechat_deal_proportion in", values, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("month_wechat_deal_proportion not in", values, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_wechat_deal_proportion between", value1, value2, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthWechatDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_wechat_deal_proportion not between", value1, value2, "monthWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumIsNull() {
            addCriterion("month_pc_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumIsNotNull() {
            addCriterion("month_pc_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumEqualTo(Integer value) {
            addCriterion("month_pc_deal_num =", value, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumNotEqualTo(Integer value) {
            addCriterion("month_pc_deal_num <>", value, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumGreaterThan(Integer value) {
            addCriterion("month_pc_deal_num >", value, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("month_pc_deal_num >=", value, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumLessThan(Integer value) {
            addCriterion("month_pc_deal_num <", value, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("month_pc_deal_num <=", value, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumIn(List<Integer> values) {
            addCriterion("month_pc_deal_num in", values, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumNotIn(List<Integer> values) {
            addCriterion("month_pc_deal_num not in", values, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumBetween(Integer value1, Integer value2) {
            addCriterion("month_pc_deal_num between", value1, value2, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("month_pc_deal_num not between", value1, value2, "monthPcDealNum");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionIsNull() {
            addCriterion("month_pc_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionIsNotNull() {
            addCriterion("month_pc_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionEqualTo(BigDecimal value) {
            addCriterion("month_pc_deal_proportion =", value, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("month_pc_deal_proportion <>", value, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionGreaterThan(BigDecimal value) {
            addCriterion("month_pc_deal_proportion >", value, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("month_pc_deal_proportion >=", value, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionLessThan(BigDecimal value) {
            addCriterion("month_pc_deal_proportion <", value, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("month_pc_deal_proportion <=", value, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionIn(List<BigDecimal> values) {
            addCriterion("month_pc_deal_proportion in", values, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("month_pc_deal_proportion not in", values, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_pc_deal_proportion between", value1, value2, "monthPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andMonthPcDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_pc_deal_proportion not between", value1, value2, "monthPcDealProportion");
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

        public Criteria andUpdateUserIdIsNull() {
            addCriterion("update_user_id is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIsNotNull() {
            addCriterion("update_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdEqualTo(Integer value) {
            addCriterion("update_user_id =", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotEqualTo(Integer value) {
            addCriterion("update_user_id <>", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdGreaterThan(Integer value) {
            addCriterion("update_user_id >", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_user_id >=", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdLessThan(Integer value) {
            addCriterion("update_user_id <", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("update_user_id <=", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIn(List<Integer> values) {
            addCriterion("update_user_id in", values, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotIn(List<Integer> values) {
            addCriterion("update_user_id not in", values, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdBetween(Integer value1, Integer value2) {
            addCriterion("update_user_id between", value1, value2, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("update_user_id not between", value1, value2, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Integer value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Integer value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Integer value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Integer value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Integer> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Integer> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Integer value1, Integer value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNull() {
            addCriterion("create_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNotNull() {
            addCriterion("create_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdEqualTo(Integer value) {
            addCriterion("create_user_id =", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotEqualTo(Integer value) {
            addCriterion("create_user_id <>", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThan(Integer value) {
            addCriterion("create_user_id >", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_user_id >=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThan(Integer value) {
            addCriterion("create_user_id <", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("create_user_id <=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIn(List<Integer> values) {
            addCriterion("create_user_id in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotIn(List<Integer> values) {
            addCriterion("create_user_id not in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdBetween(Integer value1, Integer value2) {
            addCriterion("create_user_id between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("create_user_id not between", value1, value2, "createUserId");
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