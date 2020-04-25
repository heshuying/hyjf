package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuarterOperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public QuarterOperationReportExample() {
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

        public Criteria andQuarterTypeIsNull() {
            addCriterion("quarter_type is null");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeIsNotNull() {
            addCriterion("quarter_type is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeEqualTo(Integer value) {
            addCriterion("quarter_type =", value, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeNotEqualTo(Integer value) {
            addCriterion("quarter_type <>", value, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeGreaterThan(Integer value) {
            addCriterion("quarter_type >", value, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("quarter_type >=", value, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeLessThan(Integer value) {
            addCriterion("quarter_type <", value, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeLessThanOrEqualTo(Integer value) {
            addCriterion("quarter_type <=", value, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeIn(List<Integer> values) {
            addCriterion("quarter_type in", values, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeNotIn(List<Integer> values) {
            addCriterion("quarter_type not in", values, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeBetween(Integer value1, Integer value2) {
            addCriterion("quarter_type between", value1, value2, "quarterType");
            return (Criteria) this;
        }

        public Criteria andQuarterTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("quarter_type not between", value1, value2, "quarterType");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountIsNull() {
            addCriterion("first_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountIsNotNull() {
            addCriterion("first_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountEqualTo(BigDecimal value) {
            addCriterion("first_month_amount =", value, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("first_month_amount <>", value, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("first_month_amount >", value, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("first_month_amount >=", value, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountLessThan(BigDecimal value) {
            addCriterion("first_month_amount <", value, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("first_month_amount <=", value, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountIn(List<BigDecimal> values) {
            addCriterion("first_month_amount in", values, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("first_month_amount not in", values, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("first_month_amount between", value1, value2, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFirstMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("first_month_amount not between", value1, value2, "firstMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountIsNull() {
            addCriterion("second_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountIsNotNull() {
            addCriterion("second_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountEqualTo(BigDecimal value) {
            addCriterion("second_month_amount =", value, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("second_month_amount <>", value, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("second_month_amount >", value, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("second_month_amount >=", value, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountLessThan(BigDecimal value) {
            addCriterion("second_month_amount <", value, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("second_month_amount <=", value, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountIn(List<BigDecimal> values) {
            addCriterion("second_month_amount in", values, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("second_month_amount not in", values, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("second_month_amount between", value1, value2, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSecondMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("second_month_amount not between", value1, value2, "secondMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountIsNull() {
            addCriterion("third_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountIsNotNull() {
            addCriterion("third_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountEqualTo(BigDecimal value) {
            addCriterion("third_month_amount =", value, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("third_month_amount <>", value, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("third_month_amount >", value, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("third_month_amount >=", value, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountLessThan(BigDecimal value) {
            addCriterion("third_month_amount <", value, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("third_month_amount <=", value, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountIn(List<BigDecimal> values) {
            addCriterion("third_month_amount in", values, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("third_month_amount not in", values, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_month_amount between", value1, value2, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andThirdMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_month_amount not between", value1, value2, "thirdMonthAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountIsNull() {
            addCriterion("last_year_quarter_amount is null");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountIsNotNull() {
            addCriterion("last_year_quarter_amount is not null");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_amount =", value, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountNotEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_amount <>", value, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountGreaterThan(BigDecimal value) {
            addCriterion("last_year_quarter_amount >", value, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_amount >=", value, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountLessThan(BigDecimal value) {
            addCriterion("last_year_quarter_amount <", value, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_amount <=", value, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountIn(List<BigDecimal> values) {
            addCriterion("last_year_quarter_amount in", values, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountNotIn(List<BigDecimal> values) {
            addCriterion("last_year_quarter_amount not in", values, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("last_year_quarter_amount between", value1, value2, "lastYearQuarterAmount");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("last_year_quarter_amount not between", value1, value2, "lastYearQuarterAmount");
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

        public Criteria andLastYearQuarterProfitIsNull() {
            addCriterion("last_year_quarter_profit is null");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitIsNotNull() {
            addCriterion("last_year_quarter_profit is not null");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_profit =", value, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitNotEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_profit <>", value, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitGreaterThan(BigDecimal value) {
            addCriterion("last_year_quarter_profit >", value, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_profit >=", value, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitLessThan(BigDecimal value) {
            addCriterion("last_year_quarter_profit <", value, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("last_year_quarter_profit <=", value, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitIn(List<BigDecimal> values) {
            addCriterion("last_year_quarter_profit in", values, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitNotIn(List<BigDecimal> values) {
            addCriterion("last_year_quarter_profit not in", values, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("last_year_quarter_profit between", value1, value2, "lastYearQuarterProfit");
            return (Criteria) this;
        }

        public Criteria andLastYearQuarterProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("last_year_quarter_profit not between", value1, value2, "lastYearQuarterProfit");
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

        public Criteria andQuarterAvgProfitIsNull() {
            addCriterion("quarter_avg_profit is null");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitIsNotNull() {
            addCriterion("quarter_avg_profit is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitEqualTo(BigDecimal value) {
            addCriterion("quarter_avg_profit =", value, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitNotEqualTo(BigDecimal value) {
            addCriterion("quarter_avg_profit <>", value, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitGreaterThan(BigDecimal value) {
            addCriterion("quarter_avg_profit >", value, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_avg_profit >=", value, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitLessThan(BigDecimal value) {
            addCriterion("quarter_avg_profit <", value, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_avg_profit <=", value, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitIn(List<BigDecimal> values) {
            addCriterion("quarter_avg_profit in", values, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitNotIn(List<BigDecimal> values) {
            addCriterion("quarter_avg_profit not in", values, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_avg_profit between", value1, value2, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAvgProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_avg_profit not between", value1, value2, "quarterAvgProfit");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumIsNull() {
            addCriterion("quarter_app_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumIsNotNull() {
            addCriterion("quarter_app_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumEqualTo(Integer value) {
            addCriterion("quarter_app_deal_num =", value, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumNotEqualTo(Integer value) {
            addCriterion("quarter_app_deal_num <>", value, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumGreaterThan(Integer value) {
            addCriterion("quarter_app_deal_num >", value, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("quarter_app_deal_num >=", value, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumLessThan(Integer value) {
            addCriterion("quarter_app_deal_num <", value, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("quarter_app_deal_num <=", value, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumIn(List<Integer> values) {
            addCriterion("quarter_app_deal_num in", values, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumNotIn(List<Integer> values) {
            addCriterion("quarter_app_deal_num not in", values, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumBetween(Integer value1, Integer value2) {
            addCriterion("quarter_app_deal_num between", value1, value2, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("quarter_app_deal_num not between", value1, value2, "quarterAppDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionIsNull() {
            addCriterion("quarter_app_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionIsNotNull() {
            addCriterion("quarter_app_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionEqualTo(BigDecimal value) {
            addCriterion("quarter_app_deal_proportion =", value, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("quarter_app_deal_proportion <>", value, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionGreaterThan(BigDecimal value) {
            addCriterion("quarter_app_deal_proportion >", value, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_app_deal_proportion >=", value, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionLessThan(BigDecimal value) {
            addCriterion("quarter_app_deal_proportion <", value, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_app_deal_proportion <=", value, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionIn(List<BigDecimal> values) {
            addCriterion("quarter_app_deal_proportion in", values, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("quarter_app_deal_proportion not in", values, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_app_deal_proportion between", value1, value2, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterAppDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_app_deal_proportion not between", value1, value2, "quarterAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumIsNull() {
            addCriterion("quarter_wechat_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumIsNotNull() {
            addCriterion("quarter_wechat_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumEqualTo(Integer value) {
            addCriterion("quarter_wechat_deal_num =", value, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumNotEqualTo(Integer value) {
            addCriterion("quarter_wechat_deal_num <>", value, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumGreaterThan(Integer value) {
            addCriterion("quarter_wechat_deal_num >", value, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("quarter_wechat_deal_num >=", value, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumLessThan(Integer value) {
            addCriterion("quarter_wechat_deal_num <", value, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("quarter_wechat_deal_num <=", value, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumIn(List<Integer> values) {
            addCriterion("quarter_wechat_deal_num in", values, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumNotIn(List<Integer> values) {
            addCriterion("quarter_wechat_deal_num not in", values, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumBetween(Integer value1, Integer value2) {
            addCriterion("quarter_wechat_deal_num between", value1, value2, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("quarter_wechat_deal_num not between", value1, value2, "quarterWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionIsNull() {
            addCriterion("quarter_wechat_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionIsNotNull() {
            addCriterion("quarter_wechat_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionEqualTo(BigDecimal value) {
            addCriterion("quarter_wechat_deal_proportion =", value, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("quarter_wechat_deal_proportion <>", value, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionGreaterThan(BigDecimal value) {
            addCriterion("quarter_wechat_deal_proportion >", value, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_wechat_deal_proportion >=", value, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionLessThan(BigDecimal value) {
            addCriterion("quarter_wechat_deal_proportion <", value, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_wechat_deal_proportion <=", value, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionIn(List<BigDecimal> values) {
            addCriterion("quarter_wechat_deal_proportion in", values, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("quarter_wechat_deal_proportion not in", values, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_wechat_deal_proportion between", value1, value2, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterWechatDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_wechat_deal_proportion not between", value1, value2, "quarterWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumIsNull() {
            addCriterion("quarter_pc_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumIsNotNull() {
            addCriterion("quarter_pc_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumEqualTo(Integer value) {
            addCriterion("quarter_pc_deal_num =", value, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumNotEqualTo(Integer value) {
            addCriterion("quarter_pc_deal_num <>", value, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumGreaterThan(Integer value) {
            addCriterion("quarter_pc_deal_num >", value, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("quarter_pc_deal_num >=", value, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumLessThan(Integer value) {
            addCriterion("quarter_pc_deal_num <", value, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("quarter_pc_deal_num <=", value, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumIn(List<Integer> values) {
            addCriterion("quarter_pc_deal_num in", values, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumNotIn(List<Integer> values) {
            addCriterion("quarter_pc_deal_num not in", values, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumBetween(Integer value1, Integer value2) {
            addCriterion("quarter_pc_deal_num between", value1, value2, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("quarter_pc_deal_num not between", value1, value2, "quarterPcDealNum");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionIsNull() {
            addCriterion("quarter_pc_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionIsNotNull() {
            addCriterion("quarter_pc_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionEqualTo(BigDecimal value) {
            addCriterion("quarter_pc_deal_proportion =", value, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("quarter_pc_deal_proportion <>", value, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionGreaterThan(BigDecimal value) {
            addCriterion("quarter_pc_deal_proportion >", value, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_pc_deal_proportion >=", value, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionLessThan(BigDecimal value) {
            addCriterion("quarter_pc_deal_proportion <", value, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("quarter_pc_deal_proportion <=", value, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionIn(List<BigDecimal> values) {
            addCriterion("quarter_pc_deal_proportion in", values, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("quarter_pc_deal_proportion not in", values, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_pc_deal_proportion between", value1, value2, "quarterPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andQuarterPcDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quarter_pc_deal_proportion not between", value1, value2, "quarterPcDealProportion");
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