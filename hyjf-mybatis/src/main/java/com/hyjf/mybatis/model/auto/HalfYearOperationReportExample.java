package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HalfYearOperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public HalfYearOperationReportExample() {
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

        public Criteria andFourthMonthAmountIsNull() {
            addCriterion("fourth_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountIsNotNull() {
            addCriterion("fourth_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountEqualTo(BigDecimal value) {
            addCriterion("fourth_month_amount =", value, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("fourth_month_amount <>", value, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("fourth_month_amount >", value, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fourth_month_amount >=", value, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountLessThan(BigDecimal value) {
            addCriterion("fourth_month_amount <", value, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fourth_month_amount <=", value, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountIn(List<BigDecimal> values) {
            addCriterion("fourth_month_amount in", values, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("fourth_month_amount not in", values, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fourth_month_amount between", value1, value2, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFourthMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fourth_month_amount not between", value1, value2, "fourthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountIsNull() {
            addCriterion("fifth_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountIsNotNull() {
            addCriterion("fifth_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountEqualTo(BigDecimal value) {
            addCriterion("fifth_month_amount =", value, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("fifth_month_amount <>", value, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("fifth_month_amount >", value, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fifth_month_amount >=", value, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountLessThan(BigDecimal value) {
            addCriterion("fifth_month_amount <", value, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fifth_month_amount <=", value, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountIn(List<BigDecimal> values) {
            addCriterion("fifth_month_amount in", values, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("fifth_month_amount not in", values, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fifth_month_amount between", value1, value2, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andFifthMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fifth_month_amount not between", value1, value2, "fifthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountIsNull() {
            addCriterion("sixth_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountIsNotNull() {
            addCriterion("sixth_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountEqualTo(BigDecimal value) {
            addCriterion("sixth_month_amount =", value, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("sixth_month_amount <>", value, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("sixth_month_amount >", value, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sixth_month_amount >=", value, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountLessThan(BigDecimal value) {
            addCriterion("sixth_month_amount <", value, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sixth_month_amount <=", value, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountIn(List<BigDecimal> values) {
            addCriterion("sixth_month_amount in", values, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("sixth_month_amount not in", values, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sixth_month_amount between", value1, value2, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andSixthMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sixth_month_amount not between", value1, value2, "sixthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountIsNull() {
            addCriterion("half_year_amount is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountIsNotNull() {
            addCriterion("half_year_amount is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountEqualTo(BigDecimal value) {
            addCriterion("half_year_amount =", value, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountNotEqualTo(BigDecimal value) {
            addCriterion("half_year_amount <>", value, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountGreaterThan(BigDecimal value) {
            addCriterion("half_year_amount >", value, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_amount >=", value, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountLessThan(BigDecimal value) {
            addCriterion("half_year_amount <", value, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_amount <=", value, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountIn(List<BigDecimal> values) {
            addCriterion("half_year_amount in", values, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountNotIn(List<BigDecimal> values) {
            addCriterion("half_year_amount not in", values, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_amount between", value1, value2, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_amount not between", value1, value2, "halfYearAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitIsNull() {
            addCriterion("half_year_profit is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitIsNotNull() {
            addCriterion("half_year_profit is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitEqualTo(BigDecimal value) {
            addCriterion("half_year_profit =", value, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitNotEqualTo(BigDecimal value) {
            addCriterion("half_year_profit <>", value, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitGreaterThan(BigDecimal value) {
            addCriterion("half_year_profit >", value, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_profit >=", value, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitLessThan(BigDecimal value) {
            addCriterion("half_year_profit <", value, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_profit <=", value, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitIn(List<BigDecimal> values) {
            addCriterion("half_year_profit in", values, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitNotIn(List<BigDecimal> values) {
            addCriterion("half_year_profit not in", values, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_profit between", value1, value2, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_profit not between", value1, value2, "halfYearProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealIsNull() {
            addCriterion("half_year_success_deal is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealIsNotNull() {
            addCriterion("half_year_success_deal is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealEqualTo(Integer value) {
            addCriterion("half_year_success_deal =", value, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealNotEqualTo(Integer value) {
            addCriterion("half_year_success_deal <>", value, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealGreaterThan(Integer value) {
            addCriterion("half_year_success_deal >", value, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealGreaterThanOrEqualTo(Integer value) {
            addCriterion("half_year_success_deal >=", value, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealLessThan(Integer value) {
            addCriterion("half_year_success_deal <", value, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealLessThanOrEqualTo(Integer value) {
            addCriterion("half_year_success_deal <=", value, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealIn(List<Integer> values) {
            addCriterion("half_year_success_deal in", values, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealNotIn(List<Integer> values) {
            addCriterion("half_year_success_deal not in", values, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealBetween(Integer value1, Integer value2) {
            addCriterion("half_year_success_deal between", value1, value2, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessDealNotBetween(Integer value1, Integer value2) {
            addCriterion("half_year_success_deal not between", value1, value2, "halfYearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealIsNull() {
            addCriterion("half_year_recharge_deal is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealIsNotNull() {
            addCriterion("half_year_recharge_deal is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealEqualTo(Integer value) {
            addCriterion("half_year_recharge_deal =", value, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealNotEqualTo(Integer value) {
            addCriterion("half_year_recharge_deal <>", value, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealGreaterThan(Integer value) {
            addCriterion("half_year_recharge_deal >", value, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealGreaterThanOrEqualTo(Integer value) {
            addCriterion("half_year_recharge_deal >=", value, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealLessThan(Integer value) {
            addCriterion("half_year_recharge_deal <", value, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealLessThanOrEqualTo(Integer value) {
            addCriterion("half_year_recharge_deal <=", value, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealIn(List<Integer> values) {
            addCriterion("half_year_recharge_deal in", values, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealNotIn(List<Integer> values) {
            addCriterion("half_year_recharge_deal not in", values, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealBetween(Integer value1, Integer value2) {
            addCriterion("half_year_recharge_deal between", value1, value2, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeDealNotBetween(Integer value1, Integer value2) {
            addCriterion("half_year_recharge_deal not between", value1, value2, "halfYearRechargeDeal");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountIsNull() {
            addCriterion("half_year_recharge_amount is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountIsNotNull() {
            addCriterion("half_year_recharge_amount is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountEqualTo(BigDecimal value) {
            addCriterion("half_year_recharge_amount =", value, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountNotEqualTo(BigDecimal value) {
            addCriterion("half_year_recharge_amount <>", value, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountGreaterThan(BigDecimal value) {
            addCriterion("half_year_recharge_amount >", value, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_recharge_amount >=", value, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountLessThan(BigDecimal value) {
            addCriterion("half_year_recharge_amount <", value, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_recharge_amount <=", value, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountIn(List<BigDecimal> values) {
            addCriterion("half_year_recharge_amount in", values, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountNotIn(List<BigDecimal> values) {
            addCriterion("half_year_recharge_amount not in", values, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_recharge_amount between", value1, value2, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearRechargeAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_recharge_amount not between", value1, value2, "halfYearRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthIsNull() {
            addCriterion("half_year_success_month is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthIsNotNull() {
            addCriterion("half_year_success_month is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthEqualTo(Integer value) {
            addCriterion("half_year_success_month =", value, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthNotEqualTo(Integer value) {
            addCriterion("half_year_success_month <>", value, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthGreaterThan(Integer value) {
            addCriterion("half_year_success_month >", value, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthGreaterThanOrEqualTo(Integer value) {
            addCriterion("half_year_success_month >=", value, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthLessThan(Integer value) {
            addCriterion("half_year_success_month <", value, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthLessThanOrEqualTo(Integer value) {
            addCriterion("half_year_success_month <=", value, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthIn(List<Integer> values) {
            addCriterion("half_year_success_month in", values, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthNotIn(List<Integer> values) {
            addCriterion("half_year_success_month not in", values, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthBetween(Integer value1, Integer value2) {
            addCriterion("half_year_success_month between", value1, value2, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthNotBetween(Integer value1, Integer value2) {
            addCriterion("half_year_success_month not between", value1, value2, "halfYearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountIsNull() {
            addCriterion("half_year_success_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountIsNotNull() {
            addCriterion("half_year_success_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountEqualTo(BigDecimal value) {
            addCriterion("half_year_success_month_amount =", value, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("half_year_success_month_amount <>", value, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("half_year_success_month_amount >", value, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_success_month_amount >=", value, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountLessThan(BigDecimal value) {
            addCriterion("half_year_success_month_amount <", value, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_success_month_amount <=", value, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountIn(List<BigDecimal> values) {
            addCriterion("half_year_success_month_amount in", values, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("half_year_success_month_amount not in", values, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_success_month_amount between", value1, value2, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearSuccessMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_success_month_amount not between", value1, value2, "halfYearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitIsNull() {
            addCriterion("half_year_avg_profit is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitIsNotNull() {
            addCriterion("half_year_avg_profit is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitEqualTo(BigDecimal value) {
            addCriterion("half_year_avg_profit =", value, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitNotEqualTo(BigDecimal value) {
            addCriterion("half_year_avg_profit <>", value, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitGreaterThan(BigDecimal value) {
            addCriterion("half_year_avg_profit >", value, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_avg_profit >=", value, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitLessThan(BigDecimal value) {
            addCriterion("half_year_avg_profit <", value, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_avg_profit <=", value, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitIn(List<BigDecimal> values) {
            addCriterion("half_year_avg_profit in", values, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitNotIn(List<BigDecimal> values) {
            addCriterion("half_year_avg_profit not in", values, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_avg_profit between", value1, value2, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAvgProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_avg_profit not between", value1, value2, "halfYearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumIsNull() {
            addCriterion("half_year_app_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumIsNotNull() {
            addCriterion("half_year_app_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumEqualTo(Integer value) {
            addCriterion("half_year_app_deal_num =", value, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumNotEqualTo(Integer value) {
            addCriterion("half_year_app_deal_num <>", value, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumGreaterThan(Integer value) {
            addCriterion("half_year_app_deal_num >", value, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("half_year_app_deal_num >=", value, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumLessThan(Integer value) {
            addCriterion("half_year_app_deal_num <", value, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("half_year_app_deal_num <=", value, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumIn(List<Integer> values) {
            addCriterion("half_year_app_deal_num in", values, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumNotIn(List<Integer> values) {
            addCriterion("half_year_app_deal_num not in", values, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumBetween(Integer value1, Integer value2) {
            addCriterion("half_year_app_deal_num between", value1, value2, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("half_year_app_deal_num not between", value1, value2, "halfYearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionIsNull() {
            addCriterion("half_year_app_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionIsNotNull() {
            addCriterion("half_year_app_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionEqualTo(BigDecimal value) {
            addCriterion("half_year_app_deal_proportion =", value, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("half_year_app_deal_proportion <>", value, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionGreaterThan(BigDecimal value) {
            addCriterion("half_year_app_deal_proportion >", value, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_app_deal_proportion >=", value, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionLessThan(BigDecimal value) {
            addCriterion("half_year_app_deal_proportion <", value, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_app_deal_proportion <=", value, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionIn(List<BigDecimal> values) {
            addCriterion("half_year_app_deal_proportion in", values, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("half_year_app_deal_proportion not in", values, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_app_deal_proportion between", value1, value2, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearAppDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_app_deal_proportion not between", value1, value2, "halfYearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumIsNull() {
            addCriterion("half_year_wechat_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumIsNotNull() {
            addCriterion("half_year_wechat_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumEqualTo(Integer value) {
            addCriterion("half_year_wechat_deal_num =", value, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumNotEqualTo(Integer value) {
            addCriterion("half_year_wechat_deal_num <>", value, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumGreaterThan(Integer value) {
            addCriterion("half_year_wechat_deal_num >", value, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("half_year_wechat_deal_num >=", value, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumLessThan(Integer value) {
            addCriterion("half_year_wechat_deal_num <", value, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("half_year_wechat_deal_num <=", value, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumIn(List<Integer> values) {
            addCriterion("half_year_wechat_deal_num in", values, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumNotIn(List<Integer> values) {
            addCriterion("half_year_wechat_deal_num not in", values, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumBetween(Integer value1, Integer value2) {
            addCriterion("half_year_wechat_deal_num between", value1, value2, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("half_year_wechat_deal_num not between", value1, value2, "halfYearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionIsNull() {
            addCriterion("half_year_wechat_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionIsNotNull() {
            addCriterion("half_year_wechat_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionEqualTo(BigDecimal value) {
            addCriterion("half_year_wechat_deal_proportion =", value, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("half_year_wechat_deal_proportion <>", value, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionGreaterThan(BigDecimal value) {
            addCriterion("half_year_wechat_deal_proportion >", value, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_wechat_deal_proportion >=", value, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionLessThan(BigDecimal value) {
            addCriterion("half_year_wechat_deal_proportion <", value, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_wechat_deal_proportion <=", value, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionIn(List<BigDecimal> values) {
            addCriterion("half_year_wechat_deal_proportion in", values, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("half_year_wechat_deal_proportion not in", values, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_wechat_deal_proportion between", value1, value2, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearWechatDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_wechat_deal_proportion not between", value1, value2, "halfYearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumIsNull() {
            addCriterion("half_year_pc_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumIsNotNull() {
            addCriterion("half_year_pc_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumEqualTo(Integer value) {
            addCriterion("half_year_pc_deal_num =", value, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumNotEqualTo(Integer value) {
            addCriterion("half_year_pc_deal_num <>", value, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumGreaterThan(Integer value) {
            addCriterion("half_year_pc_deal_num >", value, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("half_year_pc_deal_num >=", value, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumLessThan(Integer value) {
            addCriterion("half_year_pc_deal_num <", value, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("half_year_pc_deal_num <=", value, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumIn(List<Integer> values) {
            addCriterion("half_year_pc_deal_num in", values, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumNotIn(List<Integer> values) {
            addCriterion("half_year_pc_deal_num not in", values, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumBetween(Integer value1, Integer value2) {
            addCriterion("half_year_pc_deal_num between", value1, value2, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("half_year_pc_deal_num not between", value1, value2, "halfYearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionIsNull() {
            addCriterion("half_year_pc_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionIsNotNull() {
            addCriterion("half_year_pc_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionEqualTo(BigDecimal value) {
            addCriterion("half_year_pc_deal_proportion =", value, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("half_year_pc_deal_proportion <>", value, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionGreaterThan(BigDecimal value) {
            addCriterion("half_year_pc_deal_proportion >", value, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_pc_deal_proportion >=", value, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionLessThan(BigDecimal value) {
            addCriterion("half_year_pc_deal_proportion <", value, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("half_year_pc_deal_proportion <=", value, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionIn(List<BigDecimal> values) {
            addCriterion("half_year_pc_deal_proportion in", values, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("half_year_pc_deal_proportion not in", values, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_pc_deal_proportion between", value1, value2, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andHalfYearPcDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("half_year_pc_deal_proportion not between", value1, value2, "halfYearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumIsNull() {
            addCriterion("less_thirty_day_num is null");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumIsNotNull() {
            addCriterion("less_thirty_day_num is not null");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumEqualTo(Integer value) {
            addCriterion("less_thirty_day_num =", value, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumNotEqualTo(Integer value) {
            addCriterion("less_thirty_day_num <>", value, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumGreaterThan(Integer value) {
            addCriterion("less_thirty_day_num >", value, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("less_thirty_day_num >=", value, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumLessThan(Integer value) {
            addCriterion("less_thirty_day_num <", value, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumLessThanOrEqualTo(Integer value) {
            addCriterion("less_thirty_day_num <=", value, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumIn(List<Integer> values) {
            addCriterion("less_thirty_day_num in", values, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumNotIn(List<Integer> values) {
            addCriterion("less_thirty_day_num not in", values, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumBetween(Integer value1, Integer value2) {
            addCriterion("less_thirty_day_num between", value1, value2, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayNumNotBetween(Integer value1, Integer value2) {
            addCriterion("less_thirty_day_num not between", value1, value2, "lessThirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionIsNull() {
            addCriterion("less_thirty_day_proportion is null");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionIsNotNull() {
            addCriterion("less_thirty_day_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionEqualTo(BigDecimal value) {
            addCriterion("less_thirty_day_proportion =", value, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionNotEqualTo(BigDecimal value) {
            addCriterion("less_thirty_day_proportion <>", value, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionGreaterThan(BigDecimal value) {
            addCriterion("less_thirty_day_proportion >", value, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("less_thirty_day_proportion >=", value, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionLessThan(BigDecimal value) {
            addCriterion("less_thirty_day_proportion <", value, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("less_thirty_day_proportion <=", value, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionIn(List<BigDecimal> values) {
            addCriterion("less_thirty_day_proportion in", values, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionNotIn(List<BigDecimal> values) {
            addCriterion("less_thirty_day_proportion not in", values, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("less_thirty_day_proportion between", value1, value2, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andLessThirtyDayProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("less_thirty_day_proportion not between", value1, value2, "lessThirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumIsNull() {
            addCriterion("thirty_day_num is null");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumIsNotNull() {
            addCriterion("thirty_day_num is not null");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumEqualTo(Integer value) {
            addCriterion("thirty_day_num =", value, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumNotEqualTo(Integer value) {
            addCriterion("thirty_day_num <>", value, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumGreaterThan(Integer value) {
            addCriterion("thirty_day_num >", value, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("thirty_day_num >=", value, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumLessThan(Integer value) {
            addCriterion("thirty_day_num <", value, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumLessThanOrEqualTo(Integer value) {
            addCriterion("thirty_day_num <=", value, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumIn(List<Integer> values) {
            addCriterion("thirty_day_num in", values, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumNotIn(List<Integer> values) {
            addCriterion("thirty_day_num not in", values, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumBetween(Integer value1, Integer value2) {
            addCriterion("thirty_day_num between", value1, value2, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayNumNotBetween(Integer value1, Integer value2) {
            addCriterion("thirty_day_num not between", value1, value2, "thirtyDayNum");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionIsNull() {
            addCriterion("thirty_day_proportion is null");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionIsNotNull() {
            addCriterion("thirty_day_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionEqualTo(BigDecimal value) {
            addCriterion("thirty_day_proportion =", value, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionNotEqualTo(BigDecimal value) {
            addCriterion("thirty_day_proportion <>", value, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionGreaterThan(BigDecimal value) {
            addCriterion("thirty_day_proportion >", value, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("thirty_day_proportion >=", value, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionLessThan(BigDecimal value) {
            addCriterion("thirty_day_proportion <", value, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("thirty_day_proportion <=", value, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionIn(List<BigDecimal> values) {
            addCriterion("thirty_day_proportion in", values, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionNotIn(List<BigDecimal> values) {
            addCriterion("thirty_day_proportion not in", values, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("thirty_day_proportion between", value1, value2, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andThirtyDayProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("thirty_day_proportion not between", value1, value2, "thirtyDayProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumIsNull() {
            addCriterion("one_month_num is null");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumIsNotNull() {
            addCriterion("one_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumEqualTo(Integer value) {
            addCriterion("one_month_num =", value, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumNotEqualTo(Integer value) {
            addCriterion("one_month_num <>", value, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumGreaterThan(Integer value) {
            addCriterion("one_month_num >", value, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("one_month_num >=", value, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumLessThan(Integer value) {
            addCriterion("one_month_num <", value, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("one_month_num <=", value, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumIn(List<Integer> values) {
            addCriterion("one_month_num in", values, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumNotIn(List<Integer> values) {
            addCriterion("one_month_num not in", values, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("one_month_num between", value1, value2, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("one_month_num not between", value1, value2, "oneMonthNum");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionIsNull() {
            addCriterion("one_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionIsNotNull() {
            addCriterion("one_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionEqualTo(BigDecimal value) {
            addCriterion("one_month_proportion =", value, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("one_month_proportion <>", value, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("one_month_proportion >", value, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("one_month_proportion >=", value, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionLessThan(BigDecimal value) {
            addCriterion("one_month_proportion <", value, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("one_month_proportion <=", value, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionIn(List<BigDecimal> values) {
            addCriterion("one_month_proportion in", values, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("one_month_proportion not in", values, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("one_month_proportion between", value1, value2, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andOneMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("one_month_proportion not between", value1, value2, "oneMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumIsNull() {
            addCriterion("two_month_num is null");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumIsNotNull() {
            addCriterion("two_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumEqualTo(Integer value) {
            addCriterion("two_month_num =", value, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumNotEqualTo(Integer value) {
            addCriterion("two_month_num <>", value, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumGreaterThan(Integer value) {
            addCriterion("two_month_num >", value, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("two_month_num >=", value, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumLessThan(Integer value) {
            addCriterion("two_month_num <", value, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("two_month_num <=", value, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumIn(List<Integer> values) {
            addCriterion("two_month_num in", values, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumNotIn(List<Integer> values) {
            addCriterion("two_month_num not in", values, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("two_month_num between", value1, value2, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("two_month_num not between", value1, value2, "twoMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionIsNull() {
            addCriterion("two_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionIsNotNull() {
            addCriterion("two_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionEqualTo(BigDecimal value) {
            addCriterion("two_month_proportion =", value, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("two_month_proportion <>", value, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("two_month_proportion >", value, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("two_month_proportion >=", value, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionLessThan(BigDecimal value) {
            addCriterion("two_month_proportion <", value, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("two_month_proportion <=", value, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionIn(List<BigDecimal> values) {
            addCriterion("two_month_proportion in", values, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("two_month_proportion not in", values, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("two_month_proportion between", value1, value2, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwoMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("two_month_proportion not between", value1, value2, "twoMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumIsNull() {
            addCriterion("three_month_num is null");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumIsNotNull() {
            addCriterion("three_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumEqualTo(Integer value) {
            addCriterion("three_month_num =", value, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumNotEqualTo(Integer value) {
            addCriterion("three_month_num <>", value, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumGreaterThan(Integer value) {
            addCriterion("three_month_num >", value, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("three_month_num >=", value, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumLessThan(Integer value) {
            addCriterion("three_month_num <", value, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("three_month_num <=", value, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumIn(List<Integer> values) {
            addCriterion("three_month_num in", values, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumNotIn(List<Integer> values) {
            addCriterion("three_month_num not in", values, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("three_month_num between", value1, value2, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("three_month_num not between", value1, value2, "threeMonthNum");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionIsNull() {
            addCriterion("three_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionIsNotNull() {
            addCriterion("three_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionEqualTo(BigDecimal value) {
            addCriterion("three_month_proportion =", value, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("three_month_proportion <>", value, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("three_month_proportion >", value, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("three_month_proportion >=", value, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionLessThan(BigDecimal value) {
            addCriterion("three_month_proportion <", value, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("three_month_proportion <=", value, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionIn(List<BigDecimal> values) {
            addCriterion("three_month_proportion in", values, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("three_month_proportion not in", values, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("three_month_proportion between", value1, value2, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andThreeMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("three_month_proportion not between", value1, value2, "threeMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumIsNull() {
            addCriterion("four_month_num is null");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumIsNotNull() {
            addCriterion("four_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumEqualTo(Integer value) {
            addCriterion("four_month_num =", value, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumNotEqualTo(Integer value) {
            addCriterion("four_month_num <>", value, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumGreaterThan(Integer value) {
            addCriterion("four_month_num >", value, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("four_month_num >=", value, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumLessThan(Integer value) {
            addCriterion("four_month_num <", value, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("four_month_num <=", value, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumIn(List<Integer> values) {
            addCriterion("four_month_num in", values, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumNotIn(List<Integer> values) {
            addCriterion("four_month_num not in", values, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("four_month_num between", value1, value2, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("four_month_num not between", value1, value2, "fourMonthNum");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionIsNull() {
            addCriterion("four_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionIsNotNull() {
            addCriterion("four_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionEqualTo(BigDecimal value) {
            addCriterion("four_month_proportion =", value, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("four_month_proportion <>", value, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("four_month_proportion >", value, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("four_month_proportion >=", value, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionLessThan(BigDecimal value) {
            addCriterion("four_month_proportion <", value, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("four_month_proportion <=", value, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionIn(List<BigDecimal> values) {
            addCriterion("four_month_proportion in", values, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("four_month_proportion not in", values, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("four_month_proportion between", value1, value2, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFourMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("four_month_proportion not between", value1, value2, "fourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumIsNull() {
            addCriterion("five_month_num is null");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumIsNotNull() {
            addCriterion("five_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumEqualTo(Integer value) {
            addCriterion("five_month_num =", value, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumNotEqualTo(Integer value) {
            addCriterion("five_month_num <>", value, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumGreaterThan(Integer value) {
            addCriterion("five_month_num >", value, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("five_month_num >=", value, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumLessThan(Integer value) {
            addCriterion("five_month_num <", value, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("five_month_num <=", value, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumIn(List<Integer> values) {
            addCriterion("five_month_num in", values, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumNotIn(List<Integer> values) {
            addCriterion("five_month_num not in", values, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("five_month_num between", value1, value2, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("five_month_num not between", value1, value2, "fiveMonthNum");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionIsNull() {
            addCriterion("five_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionIsNotNull() {
            addCriterion("five_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionEqualTo(BigDecimal value) {
            addCriterion("five_month_proportion =", value, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("five_month_proportion <>", value, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("five_month_proportion >", value, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("five_month_proportion >=", value, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionLessThan(BigDecimal value) {
            addCriterion("five_month_proportion <", value, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("five_month_proportion <=", value, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionIn(List<BigDecimal> values) {
            addCriterion("five_month_proportion in", values, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("five_month_proportion not in", values, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("five_month_proportion between", value1, value2, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFiveMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("five_month_proportion not between", value1, value2, "fiveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumIsNull() {
            addCriterion("six_month_num is null");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumIsNotNull() {
            addCriterion("six_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumEqualTo(Integer value) {
            addCriterion("six_month_num =", value, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumNotEqualTo(Integer value) {
            addCriterion("six_month_num <>", value, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumGreaterThan(Integer value) {
            addCriterion("six_month_num >", value, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("six_month_num >=", value, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumLessThan(Integer value) {
            addCriterion("six_month_num <", value, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("six_month_num <=", value, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumIn(List<Integer> values) {
            addCriterion("six_month_num in", values, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumNotIn(List<Integer> values) {
            addCriterion("six_month_num not in", values, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("six_month_num between", value1, value2, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("six_month_num not between", value1, value2, "sixMonthNum");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionIsNull() {
            addCriterion("six_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionIsNotNull() {
            addCriterion("six_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionEqualTo(BigDecimal value) {
            addCriterion("six_month_proportion =", value, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("six_month_proportion <>", value, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("six_month_proportion >", value, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("six_month_proportion >=", value, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionLessThan(BigDecimal value) {
            addCriterion("six_month_proportion <", value, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("six_month_proportion <=", value, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionIn(List<BigDecimal> values) {
            addCriterion("six_month_proportion in", values, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("six_month_proportion not in", values, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("six_month_proportion between", value1, value2, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andSixMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("six_month_proportion not between", value1, value2, "sixMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumIsNull() {
            addCriterion("nine_month_num is null");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumIsNotNull() {
            addCriterion("nine_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumEqualTo(Integer value) {
            addCriterion("nine_month_num =", value, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumNotEqualTo(Integer value) {
            addCriterion("nine_month_num <>", value, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumGreaterThan(Integer value) {
            addCriterion("nine_month_num >", value, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("nine_month_num >=", value, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumLessThan(Integer value) {
            addCriterion("nine_month_num <", value, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("nine_month_num <=", value, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumIn(List<Integer> values) {
            addCriterion("nine_month_num in", values, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumNotIn(List<Integer> values) {
            addCriterion("nine_month_num not in", values, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("nine_month_num between", value1, value2, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("nine_month_num not between", value1, value2, "nineMonthNum");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionIsNull() {
            addCriterion("nine_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionIsNotNull() {
            addCriterion("nine_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionEqualTo(BigDecimal value) {
            addCriterion("nine_month_proportion =", value, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("nine_month_proportion <>", value, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("nine_month_proportion >", value, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("nine_month_proportion >=", value, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionLessThan(BigDecimal value) {
            addCriterion("nine_month_proportion <", value, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("nine_month_proportion <=", value, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionIn(List<BigDecimal> values) {
            addCriterion("nine_month_proportion in", values, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("nine_month_proportion not in", values, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("nine_month_proportion between", value1, value2, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andNineMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("nine_month_proportion not between", value1, value2, "nineMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumIsNull() {
            addCriterion("ten_month_num is null");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumIsNotNull() {
            addCriterion("ten_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumEqualTo(Integer value) {
            addCriterion("ten_month_num =", value, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumNotEqualTo(Integer value) {
            addCriterion("ten_month_num <>", value, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumGreaterThan(Integer value) {
            addCriterion("ten_month_num >", value, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("ten_month_num >=", value, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumLessThan(Integer value) {
            addCriterion("ten_month_num <", value, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("ten_month_num <=", value, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumIn(List<Integer> values) {
            addCriterion("ten_month_num in", values, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumNotIn(List<Integer> values) {
            addCriterion("ten_month_num not in", values, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("ten_month_num between", value1, value2, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("ten_month_num not between", value1, value2, "tenMonthNum");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionIsNull() {
            addCriterion("ten_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionIsNotNull() {
            addCriterion("ten_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionEqualTo(BigDecimal value) {
            addCriterion("ten_month_proportion =", value, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("ten_month_proportion <>", value, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("ten_month_proportion >", value, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ten_month_proportion >=", value, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionLessThan(BigDecimal value) {
            addCriterion("ten_month_proportion <", value, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ten_month_proportion <=", value, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionIn(List<BigDecimal> values) {
            addCriterion("ten_month_proportion in", values, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("ten_month_proportion not in", values, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ten_month_proportion between", value1, value2, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTenMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ten_month_proportion not between", value1, value2, "tenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumIsNull() {
            addCriterion("twelve_month_num is null");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumIsNotNull() {
            addCriterion("twelve_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumEqualTo(Integer value) {
            addCriterion("twelve_month_num =", value, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumNotEqualTo(Integer value) {
            addCriterion("twelve_month_num <>", value, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumGreaterThan(Integer value) {
            addCriterion("twelve_month_num >", value, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("twelve_month_num >=", value, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumLessThan(Integer value) {
            addCriterion("twelve_month_num <", value, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("twelve_month_num <=", value, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumIn(List<Integer> values) {
            addCriterion("twelve_month_num in", values, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumNotIn(List<Integer> values) {
            addCriterion("twelve_month_num not in", values, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("twelve_month_num between", value1, value2, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("twelve_month_num not between", value1, value2, "twelveMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionIsNull() {
            addCriterion("twelve_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionIsNotNull() {
            addCriterion("twelve_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionEqualTo(BigDecimal value) {
            addCriterion("twelve_month_proportion =", value, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("twelve_month_proportion <>", value, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("twelve_month_proportion >", value, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("twelve_month_proportion >=", value, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionLessThan(BigDecimal value) {
            addCriterion("twelve_month_proportion <", value, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("twelve_month_proportion <=", value, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionIn(List<BigDecimal> values) {
            addCriterion("twelve_month_proportion in", values, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("twelve_month_proportion not in", values, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("twelve_month_proportion between", value1, value2, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("twelve_month_proportion not between", value1, value2, "twelveMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumIsNull() {
            addCriterion("fifteen_month_num is null");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumIsNotNull() {
            addCriterion("fifteen_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumEqualTo(Integer value) {
            addCriterion("fifteen_month_num =", value, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumNotEqualTo(Integer value) {
            addCriterion("fifteen_month_num <>", value, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumGreaterThan(Integer value) {
            addCriterion("fifteen_month_num >", value, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("fifteen_month_num >=", value, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumLessThan(Integer value) {
            addCriterion("fifteen_month_num <", value, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("fifteen_month_num <=", value, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumIn(List<Integer> values) {
            addCriterion("fifteen_month_num in", values, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumNotIn(List<Integer> values) {
            addCriterion("fifteen_month_num not in", values, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("fifteen_month_num between", value1, value2, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("fifteen_month_num not between", value1, value2, "fifteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionIsNull() {
            addCriterion("fifteen_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionIsNotNull() {
            addCriterion("fifteen_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionEqualTo(BigDecimal value) {
            addCriterion("fifteen_month_proportion =", value, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("fifteen_month_proportion <>", value, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("fifteen_month_proportion >", value, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fifteen_month_proportion >=", value, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionLessThan(BigDecimal value) {
            addCriterion("fifteen_month_proportion <", value, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fifteen_month_proportion <=", value, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionIn(List<BigDecimal> values) {
            addCriterion("fifteen_month_proportion in", values, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("fifteen_month_proportion not in", values, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fifteen_month_proportion between", value1, value2, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andFifteenMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fifteen_month_proportion not between", value1, value2, "fifteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumIsNull() {
            addCriterion("eighteen_month_num is null");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumIsNotNull() {
            addCriterion("eighteen_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumEqualTo(Integer value) {
            addCriterion("eighteen_month_num =", value, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumNotEqualTo(Integer value) {
            addCriterion("eighteen_month_num <>", value, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumGreaterThan(Integer value) {
            addCriterion("eighteen_month_num >", value, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("eighteen_month_num >=", value, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumLessThan(Integer value) {
            addCriterion("eighteen_month_num <", value, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("eighteen_month_num <=", value, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumIn(List<Integer> values) {
            addCriterion("eighteen_month_num in", values, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumNotIn(List<Integer> values) {
            addCriterion("eighteen_month_num not in", values, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("eighteen_month_num between", value1, value2, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("eighteen_month_num not between", value1, value2, "eighteenMonthNum");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionIsNull() {
            addCriterion("eighteen_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionIsNotNull() {
            addCriterion("eighteen_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_proportion =", value, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_proportion <>", value, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("eighteen_month_proportion >", value, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_proportion >=", value, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionLessThan(BigDecimal value) {
            addCriterion("eighteen_month_proportion <", value, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_proportion <=", value, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionIn(List<BigDecimal> values) {
            addCriterion("eighteen_month_proportion in", values, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("eighteen_month_proportion not in", values, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eighteen_month_proportion between", value1, value2, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eighteen_month_proportion not between", value1, value2, "eighteenMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumIsNull() {
            addCriterion("twenty_four_month_num is null");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumIsNotNull() {
            addCriterion("twenty_four_month_num is not null");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumEqualTo(Integer value) {
            addCriterion("twenty_four_month_num =", value, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumNotEqualTo(Integer value) {
            addCriterion("twenty_four_month_num <>", value, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumGreaterThan(Integer value) {
            addCriterion("twenty_four_month_num >", value, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("twenty_four_month_num >=", value, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumLessThan(Integer value) {
            addCriterion("twenty_four_month_num <", value, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumLessThanOrEqualTo(Integer value) {
            addCriterion("twenty_four_month_num <=", value, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumIn(List<Integer> values) {
            addCriterion("twenty_four_month_num in", values, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumNotIn(List<Integer> values) {
            addCriterion("twenty_four_month_num not in", values, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumBetween(Integer value1, Integer value2) {
            addCriterion("twenty_four_month_num between", value1, value2, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthNumNotBetween(Integer value1, Integer value2) {
            addCriterion("twenty_four_month_num not between", value1, value2, "twentyFourMonthNum");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionIsNull() {
            addCriterion("twenty_four_month_proportion is null");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionIsNotNull() {
            addCriterion("twenty_four_month_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionEqualTo(BigDecimal value) {
            addCriterion("twenty_four_month_proportion =", value, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionNotEqualTo(BigDecimal value) {
            addCriterion("twenty_four_month_proportion <>", value, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionGreaterThan(BigDecimal value) {
            addCriterion("twenty_four_month_proportion >", value, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("twenty_four_month_proportion >=", value, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionLessThan(BigDecimal value) {
            addCriterion("twenty_four_month_proportion <", value, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("twenty_four_month_proportion <=", value, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionIn(List<BigDecimal> values) {
            addCriterion("twenty_four_month_proportion in", values, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionNotIn(List<BigDecimal> values) {
            addCriterion("twenty_four_month_proportion not in", values, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("twenty_four_month_proportion between", value1, value2, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andTwentyFourMonthProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("twenty_four_month_proportion not between", value1, value2, "twentyFourMonthProportion");
            return (Criteria) this;
        }

        public Criteria andPhoneNumIsNull() {
            addCriterion("phone_num is null");
            return (Criteria) this;
        }

        public Criteria andPhoneNumIsNotNull() {
            addCriterion("phone_num is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneNumEqualTo(Long value) {
            addCriterion("phone_num =", value, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumNotEqualTo(Long value) {
            addCriterion("phone_num <>", value, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumGreaterThan(Long value) {
            addCriterion("phone_num >", value, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumGreaterThanOrEqualTo(Long value) {
            addCriterion("phone_num >=", value, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumLessThan(Long value) {
            addCriterion("phone_num <", value, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumLessThanOrEqualTo(Long value) {
            addCriterion("phone_num <=", value, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumIn(List<Long> values) {
            addCriterion("phone_num in", values, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumNotIn(List<Long> values) {
            addCriterion("phone_num not in", values, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumBetween(Long value1, Long value2) {
            addCriterion("phone_num between", value1, value2, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andPhoneNumNotBetween(Long value1, Long value2) {
            addCriterion("phone_num not between", value1, value2, "phoneNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumIsNull() {
            addCriterion("qq_customer_service_num is null");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumIsNotNull() {
            addCriterion("qq_customer_service_num is not null");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumEqualTo(Long value) {
            addCriterion("qq_customer_service_num =", value, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumNotEqualTo(Long value) {
            addCriterion("qq_customer_service_num <>", value, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumGreaterThan(Long value) {
            addCriterion("qq_customer_service_num >", value, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumGreaterThanOrEqualTo(Long value) {
            addCriterion("qq_customer_service_num >=", value, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumLessThan(Long value) {
            addCriterion("qq_customer_service_num <", value, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumLessThanOrEqualTo(Long value) {
            addCriterion("qq_customer_service_num <=", value, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumIn(List<Long> values) {
            addCriterion("qq_customer_service_num in", values, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumNotIn(List<Long> values) {
            addCriterion("qq_customer_service_num not in", values, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumBetween(Long value1, Long value2) {
            addCriterion("qq_customer_service_num between", value1, value2, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andQqCustomerServiceNumNotBetween(Long value1, Long value2) {
            addCriterion("qq_customer_service_num not between", value1, value2, "qqCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumIsNull() {
            addCriterion("wechat_customer_service_num is null");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumIsNotNull() {
            addCriterion("wechat_customer_service_num is not null");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumEqualTo(Long value) {
            addCriterion("wechat_customer_service_num =", value, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumNotEqualTo(Long value) {
            addCriterion("wechat_customer_service_num <>", value, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumGreaterThan(Long value) {
            addCriterion("wechat_customer_service_num >", value, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumGreaterThanOrEqualTo(Long value) {
            addCriterion("wechat_customer_service_num >=", value, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumLessThan(Long value) {
            addCriterion("wechat_customer_service_num <", value, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumLessThanOrEqualTo(Long value) {
            addCriterion("wechat_customer_service_num <=", value, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumIn(List<Long> values) {
            addCriterion("wechat_customer_service_num in", values, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumNotIn(List<Long> values) {
            addCriterion("wechat_customer_service_num not in", values, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumBetween(Long value1, Long value2) {
            addCriterion("wechat_customer_service_num between", value1, value2, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andWechatCustomerServiceNumNotBetween(Long value1, Long value2) {
            addCriterion("wechat_customer_service_num not between", value1, value2, "wechatCustomerServiceNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumIsNull() {
            addCriterion("deal_question_num is null");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumIsNotNull() {
            addCriterion("deal_question_num is not null");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumEqualTo(Integer value) {
            addCriterion("deal_question_num =", value, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumNotEqualTo(Integer value) {
            addCriterion("deal_question_num <>", value, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumGreaterThan(Integer value) {
            addCriterion("deal_question_num >", value, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("deal_question_num >=", value, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumLessThan(Integer value) {
            addCriterion("deal_question_num <", value, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumLessThanOrEqualTo(Integer value) {
            addCriterion("deal_question_num <=", value, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumIn(List<Integer> values) {
            addCriterion("deal_question_num in", values, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumNotIn(List<Integer> values) {
            addCriterion("deal_question_num not in", values, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumBetween(Integer value1, Integer value2) {
            addCriterion("deal_question_num between", value1, value2, "dealQuestionNum");
            return (Criteria) this;
        }

        public Criteria andDealQuestionNumNotBetween(Integer value1, Integer value2) {
            addCriterion("deal_question_num not between", value1, value2, "dealQuestionNum");
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