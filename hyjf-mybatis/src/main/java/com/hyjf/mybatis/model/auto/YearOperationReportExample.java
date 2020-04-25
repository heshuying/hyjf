package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class YearOperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public YearOperationReportExample() {
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

        public Criteria andSeventhMonthAmounIsNull() {
            addCriterion("seventh_month_amoun is null");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounIsNotNull() {
            addCriterion("seventh_month_amoun is not null");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounEqualTo(BigDecimal value) {
            addCriterion("seventh_month_amoun =", value, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounNotEqualTo(BigDecimal value) {
            addCriterion("seventh_month_amoun <>", value, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounGreaterThan(BigDecimal value) {
            addCriterion("seventh_month_amoun >", value, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("seventh_month_amoun >=", value, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounLessThan(BigDecimal value) {
            addCriterion("seventh_month_amoun <", value, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounLessThanOrEqualTo(BigDecimal value) {
            addCriterion("seventh_month_amoun <=", value, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounIn(List<BigDecimal> values) {
            addCriterion("seventh_month_amoun in", values, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounNotIn(List<BigDecimal> values) {
            addCriterion("seventh_month_amoun not in", values, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("seventh_month_amoun between", value1, value2, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andSeventhMonthAmounNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("seventh_month_amoun not between", value1, value2, "seventhMonthAmoun");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountIsNull() {
            addCriterion("eighteen_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountIsNotNull() {
            addCriterion("eighteen_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_amount =", value, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_amount <>", value, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("eighteen_month_amount >", value, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_amount >=", value, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountLessThan(BigDecimal value) {
            addCriterion("eighteen_month_amount <", value, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("eighteen_month_amount <=", value, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountIn(List<BigDecimal> values) {
            addCriterion("eighteen_month_amount in", values, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("eighteen_month_amount not in", values, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eighteen_month_amount between", value1, value2, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEighteenMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eighteen_month_amount not between", value1, value2, "eighteenMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountIsNull() {
            addCriterion("ninth_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountIsNotNull() {
            addCriterion("ninth_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountEqualTo(BigDecimal value) {
            addCriterion("ninth_month_amount =", value, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("ninth_month_amount <>", value, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("ninth_month_amount >", value, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ninth_month_amount >=", value, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountLessThan(BigDecimal value) {
            addCriterion("ninth_month_amount <", value, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ninth_month_amount <=", value, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountIn(List<BigDecimal> values) {
            addCriterion("ninth_month_amount in", values, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("ninth_month_amount not in", values, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ninth_month_amount between", value1, value2, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andNinthMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ninth_month_amount not between", value1, value2, "ninthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountIsNull() {
            addCriterion("tenth_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountIsNotNull() {
            addCriterion("tenth_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountEqualTo(BigDecimal value) {
            addCriterion("tenth_month_amount =", value, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("tenth_month_amount <>", value, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("tenth_month_amount >", value, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tenth_month_amount >=", value, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountLessThan(BigDecimal value) {
            addCriterion("tenth_month_amount <", value, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tenth_month_amount <=", value, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountIn(List<BigDecimal> values) {
            addCriterion("tenth_month_amount in", values, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("tenth_month_amount not in", values, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tenth_month_amount between", value1, value2, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTenthMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tenth_month_amount not between", value1, value2, "tenthMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountIsNull() {
            addCriterion("eleventh_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountIsNotNull() {
            addCriterion("eleventh_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountEqualTo(BigDecimal value) {
            addCriterion("eleventh_month_amount =", value, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("eleventh_month_amount <>", value, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("eleventh_month_amount >", value, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("eleventh_month_amount >=", value, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountLessThan(BigDecimal value) {
            addCriterion("eleventh_month_amount <", value, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("eleventh_month_amount <=", value, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountIn(List<BigDecimal> values) {
            addCriterion("eleventh_month_amount in", values, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("eleventh_month_amount not in", values, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eleventh_month_amount between", value1, value2, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andEleventhMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eleventh_month_amount not between", value1, value2, "eleventhMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountIsNull() {
            addCriterion("twelve_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountIsNotNull() {
            addCriterion("twelve_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountEqualTo(BigDecimal value) {
            addCriterion("twelve_month_amount =", value, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("twelve_month_amount <>", value, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("twelve_month_amount >", value, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("twelve_month_amount >=", value, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountLessThan(BigDecimal value) {
            addCriterion("twelve_month_amount <", value, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("twelve_month_amount <=", value, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountIn(List<BigDecimal> values) {
            addCriterion("twelve_month_amount in", values, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("twelve_month_amount not in", values, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("twelve_month_amount between", value1, value2, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andTwelveMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("twelve_month_amount not between", value1, value2, "twelveMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountIsNull() {
            addCriterion("year_amount is null");
            return (Criteria) this;
        }

        public Criteria andYearAmountIsNotNull() {
            addCriterion("year_amount is not null");
            return (Criteria) this;
        }

        public Criteria andYearAmountEqualTo(BigDecimal value) {
            addCriterion("year_amount =", value, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountNotEqualTo(BigDecimal value) {
            addCriterion("year_amount <>", value, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountGreaterThan(BigDecimal value) {
            addCriterion("year_amount >", value, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_amount >=", value, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountLessThan(BigDecimal value) {
            addCriterion("year_amount <", value, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_amount <=", value, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountIn(List<BigDecimal> values) {
            addCriterion("year_amount in", values, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountNotIn(List<BigDecimal> values) {
            addCriterion("year_amount not in", values, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_amount between", value1, value2, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_amount not between", value1, value2, "yearAmount");
            return (Criteria) this;
        }

        public Criteria andYearProfitIsNull() {
            addCriterion("year_profit is null");
            return (Criteria) this;
        }

        public Criteria andYearProfitIsNotNull() {
            addCriterion("year_profit is not null");
            return (Criteria) this;
        }

        public Criteria andYearProfitEqualTo(BigDecimal value) {
            addCriterion("year_profit =", value, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitNotEqualTo(BigDecimal value) {
            addCriterion("year_profit <>", value, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitGreaterThan(BigDecimal value) {
            addCriterion("year_profit >", value, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_profit >=", value, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitLessThan(BigDecimal value) {
            addCriterion("year_profit <", value, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_profit <=", value, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitIn(List<BigDecimal> values) {
            addCriterion("year_profit in", values, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitNotIn(List<BigDecimal> values) {
            addCriterion("year_profit not in", values, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_profit between", value1, value2, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_profit not between", value1, value2, "yearProfit");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealIsNull() {
            addCriterion("year_success_deal is null");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealIsNotNull() {
            addCriterion("year_success_deal is not null");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealEqualTo(Integer value) {
            addCriterion("year_success_deal =", value, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealNotEqualTo(Integer value) {
            addCriterion("year_success_deal <>", value, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealGreaterThan(Integer value) {
            addCriterion("year_success_deal >", value, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealGreaterThanOrEqualTo(Integer value) {
            addCriterion("year_success_deal >=", value, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealLessThan(Integer value) {
            addCriterion("year_success_deal <", value, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealLessThanOrEqualTo(Integer value) {
            addCriterion("year_success_deal <=", value, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealIn(List<Integer> values) {
            addCriterion("year_success_deal in", values, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealNotIn(List<Integer> values) {
            addCriterion("year_success_deal not in", values, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealBetween(Integer value1, Integer value2) {
            addCriterion("year_success_deal between", value1, value2, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessDealNotBetween(Integer value1, Integer value2) {
            addCriterion("year_success_deal not between", value1, value2, "yearSuccessDeal");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthIsNull() {
            addCriterion("year_success_month is null");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthIsNotNull() {
            addCriterion("year_success_month is not null");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthEqualTo(Integer value) {
            addCriterion("year_success_month =", value, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthNotEqualTo(Integer value) {
            addCriterion("year_success_month <>", value, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthGreaterThan(Integer value) {
            addCriterion("year_success_month >", value, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthGreaterThanOrEqualTo(Integer value) {
            addCriterion("year_success_month >=", value, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthLessThan(Integer value) {
            addCriterion("year_success_month <", value, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthLessThanOrEqualTo(Integer value) {
            addCriterion("year_success_month <=", value, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthIn(List<Integer> values) {
            addCriterion("year_success_month in", values, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthNotIn(List<Integer> values) {
            addCriterion("year_success_month not in", values, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthBetween(Integer value1, Integer value2) {
            addCriterion("year_success_month between", value1, value2, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthNotBetween(Integer value1, Integer value2) {
            addCriterion("year_success_month not between", value1, value2, "yearSuccessMonth");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountIsNull() {
            addCriterion("year_success_month_amount is null");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountIsNotNull() {
            addCriterion("year_success_month_amount is not null");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountEqualTo(BigDecimal value) {
            addCriterion("year_success_month_amount =", value, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountNotEqualTo(BigDecimal value) {
            addCriterion("year_success_month_amount <>", value, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountGreaterThan(BigDecimal value) {
            addCriterion("year_success_month_amount >", value, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_success_month_amount >=", value, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountLessThan(BigDecimal value) {
            addCriterion("year_success_month_amount <", value, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_success_month_amount <=", value, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountIn(List<BigDecimal> values) {
            addCriterion("year_success_month_amount in", values, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountNotIn(List<BigDecimal> values) {
            addCriterion("year_success_month_amount not in", values, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_success_month_amount between", value1, value2, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearSuccessMonthAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_success_month_amount not between", value1, value2, "yearSuccessMonthAmount");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitIsNull() {
            addCriterion("year_avg_profit is null");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitIsNotNull() {
            addCriterion("year_avg_profit is not null");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitEqualTo(BigDecimal value) {
            addCriterion("year_avg_profit =", value, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitNotEqualTo(BigDecimal value) {
            addCriterion("year_avg_profit <>", value, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitGreaterThan(BigDecimal value) {
            addCriterion("year_avg_profit >", value, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_avg_profit >=", value, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitLessThan(BigDecimal value) {
            addCriterion("year_avg_profit <", value, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_avg_profit <=", value, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitIn(List<BigDecimal> values) {
            addCriterion("year_avg_profit in", values, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitNotIn(List<BigDecimal> values) {
            addCriterion("year_avg_profit not in", values, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_avg_profit between", value1, value2, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAvgProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_avg_profit not between", value1, value2, "yearAvgProfit");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumIsNull() {
            addCriterion("year_app_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumIsNotNull() {
            addCriterion("year_app_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumEqualTo(Integer value) {
            addCriterion("year_app_deal_num =", value, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumNotEqualTo(Integer value) {
            addCriterion("year_app_deal_num <>", value, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumGreaterThan(Integer value) {
            addCriterion("year_app_deal_num >", value, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("year_app_deal_num >=", value, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumLessThan(Integer value) {
            addCriterion("year_app_deal_num <", value, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("year_app_deal_num <=", value, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumIn(List<Integer> values) {
            addCriterion("year_app_deal_num in", values, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumNotIn(List<Integer> values) {
            addCriterion("year_app_deal_num not in", values, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumBetween(Integer value1, Integer value2) {
            addCriterion("year_app_deal_num between", value1, value2, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("year_app_deal_num not between", value1, value2, "yearAppDealNum");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionIsNull() {
            addCriterion("year_app_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionIsNotNull() {
            addCriterion("year_app_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_proportion =", value, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_proportion <>", value, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionGreaterThan(BigDecimal value) {
            addCriterion("year_app_deal_proportion >", value, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_proportion >=", value, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionLessThan(BigDecimal value) {
            addCriterion("year_app_deal_proportion <", value, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_proportion <=", value, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionIn(List<BigDecimal> values) {
            addCriterion("year_app_deal_proportion in", values, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("year_app_deal_proportion not in", values, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_app_deal_proportion between", value1, value2, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_app_deal_proportion not between", value1, value2, "yearAppDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumIsNull() {
            addCriterion("year_wechat_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumIsNotNull() {
            addCriterion("year_wechat_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumEqualTo(Integer value) {
            addCriterion("year_wechat_deal_num =", value, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumNotEqualTo(Integer value) {
            addCriterion("year_wechat_deal_num <>", value, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumGreaterThan(Integer value) {
            addCriterion("year_wechat_deal_num >", value, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("year_wechat_deal_num >=", value, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumLessThan(Integer value) {
            addCriterion("year_wechat_deal_num <", value, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("year_wechat_deal_num <=", value, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumIn(List<Integer> values) {
            addCriterion("year_wechat_deal_num in", values, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumNotIn(List<Integer> values) {
            addCriterion("year_wechat_deal_num not in", values, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumBetween(Integer value1, Integer value2) {
            addCriterion("year_wechat_deal_num between", value1, value2, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("year_wechat_deal_num not between", value1, value2, "yearWechatDealNum");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionIsNull() {
            addCriterion("year_wechat_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionIsNotNull() {
            addCriterion("year_wechat_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_proportion =", value, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_proportion <>", value, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionGreaterThan(BigDecimal value) {
            addCriterion("year_wechat_deal_proportion >", value, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_proportion >=", value, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionLessThan(BigDecimal value) {
            addCriterion("year_wechat_deal_proportion <", value, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_proportion <=", value, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionIn(List<BigDecimal> values) {
            addCriterion("year_wechat_deal_proportion in", values, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("year_wechat_deal_proportion not in", values, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_wechat_deal_proportion between", value1, value2, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_wechat_deal_proportion not between", value1, value2, "yearWechatDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumIsNull() {
            addCriterion("year_pc_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumIsNotNull() {
            addCriterion("year_pc_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumEqualTo(Integer value) {
            addCriterion("year_pc_deal_num =", value, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumNotEqualTo(Integer value) {
            addCriterion("year_pc_deal_num <>", value, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumGreaterThan(Integer value) {
            addCriterion("year_pc_deal_num >", value, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("year_pc_deal_num >=", value, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumLessThan(Integer value) {
            addCriterion("year_pc_deal_num <", value, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("year_pc_deal_num <=", value, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumIn(List<Integer> values) {
            addCriterion("year_pc_deal_num in", values, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumNotIn(List<Integer> values) {
            addCriterion("year_pc_deal_num not in", values, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumBetween(Integer value1, Integer value2) {
            addCriterion("year_pc_deal_num between", value1, value2, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("year_pc_deal_num not between", value1, value2, "yearPcDealNum");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionIsNull() {
            addCriterion("year_pc_deal_proportion is null");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionIsNotNull() {
            addCriterion("year_pc_deal_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_proportion =", value, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionNotEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_proportion <>", value, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionGreaterThan(BigDecimal value) {
            addCriterion("year_pc_deal_proportion >", value, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_proportion >=", value, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionLessThan(BigDecimal value) {
            addCriterion("year_pc_deal_proportion <", value, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_proportion <=", value, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionIn(List<BigDecimal> values) {
            addCriterion("year_pc_deal_proportion in", values, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionNotIn(List<BigDecimal> values) {
            addCriterion("year_pc_deal_proportion not in", values, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_pc_deal_proportion between", value1, value2, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_pc_deal_proportion not between", value1, value2, "yearPcDealProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountIsNull() {
            addCriterion("year_app_deal_amount is null");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountIsNotNull() {
            addCriterion("year_app_deal_amount is not null");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_amount =", value, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountNotEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_amount <>", value, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountGreaterThan(BigDecimal value) {
            addCriterion("year_app_deal_amount >", value, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_amount >=", value, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountLessThan(BigDecimal value) {
            addCriterion("year_app_deal_amount <", value, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_app_deal_amount <=", value, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountIn(List<BigDecimal> values) {
            addCriterion("year_app_deal_amount in", values, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountNotIn(List<BigDecimal> values) {
            addCriterion("year_app_deal_amount not in", values, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_app_deal_amount between", value1, value2, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppDealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_app_deal_amount not between", value1, value2, "yearAppDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionIsNull() {
            addCriterion("year_app_amount_proportion is null");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionIsNotNull() {
            addCriterion("year_app_amount_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionEqualTo(BigDecimal value) {
            addCriterion("year_app_amount_proportion =", value, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionNotEqualTo(BigDecimal value) {
            addCriterion("year_app_amount_proportion <>", value, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionGreaterThan(BigDecimal value) {
            addCriterion("year_app_amount_proportion >", value, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_app_amount_proportion >=", value, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionLessThan(BigDecimal value) {
            addCriterion("year_app_amount_proportion <", value, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_app_amount_proportion <=", value, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionIn(List<BigDecimal> values) {
            addCriterion("year_app_amount_proportion in", values, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionNotIn(List<BigDecimal> values) {
            addCriterion("year_app_amount_proportion not in", values, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_app_amount_proportion between", value1, value2, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearAppAmountProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_app_amount_proportion not between", value1, value2, "yearAppAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountIsNull() {
            addCriterion("year_wechat_deal_amount is null");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountIsNotNull() {
            addCriterion("year_wechat_deal_amount is not null");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_amount =", value, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountNotEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_amount <>", value, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountGreaterThan(BigDecimal value) {
            addCriterion("year_wechat_deal_amount >", value, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_amount >=", value, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountLessThan(BigDecimal value) {
            addCriterion("year_wechat_deal_amount <", value, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_wechat_deal_amount <=", value, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountIn(List<BigDecimal> values) {
            addCriterion("year_wechat_deal_amount in", values, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountNotIn(List<BigDecimal> values) {
            addCriterion("year_wechat_deal_amount not in", values, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_wechat_deal_amount between", value1, value2, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatDealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_wechat_deal_amount not between", value1, value2, "yearWechatDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionIsNull() {
            addCriterion("year_wechat_amount_proportion is null");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionIsNotNull() {
            addCriterion("year_wechat_amount_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionEqualTo(BigDecimal value) {
            addCriterion("year_wechat_amount_proportion =", value, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionNotEqualTo(BigDecimal value) {
            addCriterion("year_wechat_amount_proportion <>", value, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionGreaterThan(BigDecimal value) {
            addCriterion("year_wechat_amount_proportion >", value, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_wechat_amount_proportion >=", value, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionLessThan(BigDecimal value) {
            addCriterion("year_wechat_amount_proportion <", value, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_wechat_amount_proportion <=", value, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionIn(List<BigDecimal> values) {
            addCriterion("year_wechat_amount_proportion in", values, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionNotIn(List<BigDecimal> values) {
            addCriterion("year_wechat_amount_proportion not in", values, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_wechat_amount_proportion between", value1, value2, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearWechatAmountProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_wechat_amount_proportion not between", value1, value2, "yearWechatAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountIsNull() {
            addCriterion("year_pc_deal_amount is null");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountIsNotNull() {
            addCriterion("year_pc_deal_amount is not null");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_amount =", value, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountNotEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_amount <>", value, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountGreaterThan(BigDecimal value) {
            addCriterion("year_pc_deal_amount >", value, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_amount >=", value, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountLessThan(BigDecimal value) {
            addCriterion("year_pc_deal_amount <", value, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_pc_deal_amount <=", value, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountIn(List<BigDecimal> values) {
            addCriterion("year_pc_deal_amount in", values, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountNotIn(List<BigDecimal> values) {
            addCriterion("year_pc_deal_amount not in", values, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_pc_deal_amount between", value1, value2, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcDealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_pc_deal_amount not between", value1, value2, "yearPcDealAmount");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionIsNull() {
            addCriterion("year_pc_amount_proportion is null");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionIsNotNull() {
            addCriterion("year_pc_amount_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionEqualTo(BigDecimal value) {
            addCriterion("year_pc_amount_proportion =", value, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionNotEqualTo(BigDecimal value) {
            addCriterion("year_pc_amount_proportion <>", value, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionGreaterThan(BigDecimal value) {
            addCriterion("year_pc_amount_proportion >", value, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("year_pc_amount_proportion >=", value, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionLessThan(BigDecimal value) {
            addCriterion("year_pc_amount_proportion <", value, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("year_pc_amount_proportion <=", value, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionIn(List<BigDecimal> values) {
            addCriterion("year_pc_amount_proportion in", values, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionNotIn(List<BigDecimal> values) {
            addCriterion("year_pc_amount_proportion not in", values, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_pc_amount_proportion between", value1, value2, "yearPcAmountProportion");
            return (Criteria) this;
        }

        public Criteria andYearPcAmountProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("year_pc_amount_proportion not between", value1, value2, "yearPcAmountProportion");
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