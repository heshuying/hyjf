package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TenthOperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public TenthOperationReportExample() {
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

        public Criteria andOperationReportTypeIsNull() {
            addCriterion("operation_report_type is null");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeIsNotNull() {
            addCriterion("operation_report_type is not null");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeEqualTo(Integer value) {
            addCriterion("operation_report_type =", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeNotEqualTo(Integer value) {
            addCriterion("operation_report_type <>", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeGreaterThan(Integer value) {
            addCriterion("operation_report_type >", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("operation_report_type >=", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeLessThan(Integer value) {
            addCriterion("operation_report_type <", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeLessThanOrEqualTo(Integer value) {
            addCriterion("operation_report_type <=", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeIn(List<Integer> values) {
            addCriterion("operation_report_type in", values, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeNotIn(List<Integer> values) {
            addCriterion("operation_report_type not in", values, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_type between", value1, value2, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_type not between", value1, value2, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameIsNull() {
            addCriterion("first_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameIsNotNull() {
            addCriterion("first_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameEqualTo(String value) {
            addCriterion("first_tender_username =", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameNotEqualTo(String value) {
            addCriterion("first_tender_username <>", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameGreaterThan(String value) {
            addCriterion("first_tender_username >", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("first_tender_username >=", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameLessThan(String value) {
            addCriterion("first_tender_username <", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("first_tender_username <=", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameLike(String value) {
            addCriterion("first_tender_username like", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameNotLike(String value) {
            addCriterion("first_tender_username not like", value, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameIn(List<String> values) {
            addCriterion("first_tender_username in", values, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameNotIn(List<String> values) {
            addCriterion("first_tender_username not in", values, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameBetween(String value1, String value2) {
            addCriterion("first_tender_username between", value1, value2, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("first_tender_username not between", value1, value2, "firstTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountIsNull() {
            addCriterion("first_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountIsNotNull() {
            addCriterion("first_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountEqualTo(BigDecimal value) {
            addCriterion("first_tender_amount =", value, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("first_tender_amount <>", value, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("first_tender_amount >", value, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("first_tender_amount >=", value, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountLessThan(BigDecimal value) {
            addCriterion("first_tender_amount <", value, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("first_tender_amount <=", value, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountIn(List<BigDecimal> values) {
            addCriterion("first_tender_amount in", values, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("first_tender_amount not in", values, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("first_tender_amount between", value1, value2, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFirstTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("first_tender_amount not between", value1, value2, "firstTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameIsNull() {
            addCriterion("second_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameIsNotNull() {
            addCriterion("second_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameEqualTo(String value) {
            addCriterion("second_tender_username =", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameNotEqualTo(String value) {
            addCriterion("second_tender_username <>", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameGreaterThan(String value) {
            addCriterion("second_tender_username >", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("second_tender_username >=", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameLessThan(String value) {
            addCriterion("second_tender_username <", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("second_tender_username <=", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameLike(String value) {
            addCriterion("second_tender_username like", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameNotLike(String value) {
            addCriterion("second_tender_username not like", value, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameIn(List<String> values) {
            addCriterion("second_tender_username in", values, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameNotIn(List<String> values) {
            addCriterion("second_tender_username not in", values, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameBetween(String value1, String value2) {
            addCriterion("second_tender_username between", value1, value2, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("second_tender_username not between", value1, value2, "secondTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountIsNull() {
            addCriterion("second_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountIsNotNull() {
            addCriterion("second_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountEqualTo(BigDecimal value) {
            addCriterion("second_tender_amount =", value, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("second_tender_amount <>", value, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("second_tender_amount >", value, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("second_tender_amount >=", value, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountLessThan(BigDecimal value) {
            addCriterion("second_tender_amount <", value, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("second_tender_amount <=", value, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountIn(List<BigDecimal> values) {
            addCriterion("second_tender_amount in", values, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("second_tender_amount not in", values, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("second_tender_amount between", value1, value2, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSecondTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("second_tender_amount not between", value1, value2, "secondTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameIsNull() {
            addCriterion("third_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameIsNotNull() {
            addCriterion("third_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameEqualTo(String value) {
            addCriterion("third_tender_username =", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameNotEqualTo(String value) {
            addCriterion("third_tender_username <>", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameGreaterThan(String value) {
            addCriterion("third_tender_username >", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("third_tender_username >=", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameLessThan(String value) {
            addCriterion("third_tender_username <", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("third_tender_username <=", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameLike(String value) {
            addCriterion("third_tender_username like", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameNotLike(String value) {
            addCriterion("third_tender_username not like", value, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameIn(List<String> values) {
            addCriterion("third_tender_username in", values, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameNotIn(List<String> values) {
            addCriterion("third_tender_username not in", values, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameBetween(String value1, String value2) {
            addCriterion("third_tender_username between", value1, value2, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("third_tender_username not between", value1, value2, "thirdTenderUsername");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountIsNull() {
            addCriterion("third_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountIsNotNull() {
            addCriterion("third_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountEqualTo(BigDecimal value) {
            addCriterion("third_tender_amount =", value, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("third_tender_amount <>", value, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("third_tender_amount >", value, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("third_tender_amount >=", value, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountLessThan(BigDecimal value) {
            addCriterion("third_tender_amount <", value, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("third_tender_amount <=", value, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountIn(List<BigDecimal> values) {
            addCriterion("third_tender_amount in", values, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("third_tender_amount not in", values, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_tender_amount between", value1, value2, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andThirdTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_tender_amount not between", value1, value2, "thirdTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameIsNull() {
            addCriterion("fourth_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameIsNotNull() {
            addCriterion("fourth_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameEqualTo(String value) {
            addCriterion("fourth_tender_username =", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameNotEqualTo(String value) {
            addCriterion("fourth_tender_username <>", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameGreaterThan(String value) {
            addCriterion("fourth_tender_username >", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("fourth_tender_username >=", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameLessThan(String value) {
            addCriterion("fourth_tender_username <", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("fourth_tender_username <=", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameLike(String value) {
            addCriterion("fourth_tender_username like", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameNotLike(String value) {
            addCriterion("fourth_tender_username not like", value, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameIn(List<String> values) {
            addCriterion("fourth_tender_username in", values, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameNotIn(List<String> values) {
            addCriterion("fourth_tender_username not in", values, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameBetween(String value1, String value2) {
            addCriterion("fourth_tender_username between", value1, value2, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("fourth_tender_username not between", value1, value2, "fourthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountIsNull() {
            addCriterion("fourth_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountIsNotNull() {
            addCriterion("fourth_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountEqualTo(BigDecimal value) {
            addCriterion("fourth_tender_amount =", value, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("fourth_tender_amount <>", value, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("fourth_tender_amount >", value, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fourth_tender_amount >=", value, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountLessThan(BigDecimal value) {
            addCriterion("fourth_tender_amount <", value, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fourth_tender_amount <=", value, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountIn(List<BigDecimal> values) {
            addCriterion("fourth_tender_amount in", values, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("fourth_tender_amount not in", values, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fourth_tender_amount between", value1, value2, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFourthTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fourth_tender_amount not between", value1, value2, "fourthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameIsNull() {
            addCriterion("fifth_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameIsNotNull() {
            addCriterion("fifth_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameEqualTo(String value) {
            addCriterion("fifth_tender_username =", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameNotEqualTo(String value) {
            addCriterion("fifth_tender_username <>", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameGreaterThan(String value) {
            addCriterion("fifth_tender_username >", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("fifth_tender_username >=", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameLessThan(String value) {
            addCriterion("fifth_tender_username <", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("fifth_tender_username <=", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameLike(String value) {
            addCriterion("fifth_tender_username like", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameNotLike(String value) {
            addCriterion("fifth_tender_username not like", value, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameIn(List<String> values) {
            addCriterion("fifth_tender_username in", values, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameNotIn(List<String> values) {
            addCriterion("fifth_tender_username not in", values, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameBetween(String value1, String value2) {
            addCriterion("fifth_tender_username between", value1, value2, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("fifth_tender_username not between", value1, value2, "fifthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountIsNull() {
            addCriterion("fifth_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountIsNotNull() {
            addCriterion("fifth_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountEqualTo(BigDecimal value) {
            addCriterion("fifth_tender_amount =", value, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("fifth_tender_amount <>", value, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("fifth_tender_amount >", value, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fifth_tender_amount >=", value, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountLessThan(BigDecimal value) {
            addCriterion("fifth_tender_amount <", value, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fifth_tender_amount <=", value, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountIn(List<BigDecimal> values) {
            addCriterion("fifth_tender_amount in", values, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("fifth_tender_amount not in", values, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fifth_tender_amount between", value1, value2, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andFifthTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fifth_tender_amount not between", value1, value2, "fifthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameIsNull() {
            addCriterion("sixth_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameIsNotNull() {
            addCriterion("sixth_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameEqualTo(String value) {
            addCriterion("sixth_tender_username =", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameNotEqualTo(String value) {
            addCriterion("sixth_tender_username <>", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameGreaterThan(String value) {
            addCriterion("sixth_tender_username >", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("sixth_tender_username >=", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameLessThan(String value) {
            addCriterion("sixth_tender_username <", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("sixth_tender_username <=", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameLike(String value) {
            addCriterion("sixth_tender_username like", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameNotLike(String value) {
            addCriterion("sixth_tender_username not like", value, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameIn(List<String> values) {
            addCriterion("sixth_tender_username in", values, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameNotIn(List<String> values) {
            addCriterion("sixth_tender_username not in", values, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameBetween(String value1, String value2) {
            addCriterion("sixth_tender_username between", value1, value2, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("sixth_tender_username not between", value1, value2, "sixthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountIsNull() {
            addCriterion("sixth_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountIsNotNull() {
            addCriterion("sixth_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountEqualTo(BigDecimal value) {
            addCriterion("sixth_tender_amount =", value, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("sixth_tender_amount <>", value, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("sixth_tender_amount >", value, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sixth_tender_amount >=", value, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountLessThan(BigDecimal value) {
            addCriterion("sixth_tender_amount <", value, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sixth_tender_amount <=", value, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountIn(List<BigDecimal> values) {
            addCriterion("sixth_tender_amount in", values, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("sixth_tender_amount not in", values, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sixth_tender_amount between", value1, value2, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSixthTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sixth_tender_amount not between", value1, value2, "sixthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameIsNull() {
            addCriterion("seventh_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameIsNotNull() {
            addCriterion("seventh_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameEqualTo(String value) {
            addCriterion("seventh_tender_username =", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameNotEqualTo(String value) {
            addCriterion("seventh_tender_username <>", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameGreaterThan(String value) {
            addCriterion("seventh_tender_username >", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("seventh_tender_username >=", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameLessThan(String value) {
            addCriterion("seventh_tender_username <", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("seventh_tender_username <=", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameLike(String value) {
            addCriterion("seventh_tender_username like", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameNotLike(String value) {
            addCriterion("seventh_tender_username not like", value, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameIn(List<String> values) {
            addCriterion("seventh_tender_username in", values, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameNotIn(List<String> values) {
            addCriterion("seventh_tender_username not in", values, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameBetween(String value1, String value2) {
            addCriterion("seventh_tender_username between", value1, value2, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("seventh_tender_username not between", value1, value2, "seventhTenderUsername");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountIsNull() {
            addCriterion("seventh_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountIsNotNull() {
            addCriterion("seventh_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountEqualTo(BigDecimal value) {
            addCriterion("seventh_tender_amount =", value, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("seventh_tender_amount <>", value, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("seventh_tender_amount >", value, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("seventh_tender_amount >=", value, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountLessThan(BigDecimal value) {
            addCriterion("seventh_tender_amount <", value, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("seventh_tender_amount <=", value, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountIn(List<BigDecimal> values) {
            addCriterion("seventh_tender_amount in", values, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("seventh_tender_amount not in", values, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("seventh_tender_amount between", value1, value2, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andSeventhTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("seventh_tender_amount not between", value1, value2, "seventhTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameIsNull() {
            addCriterion("eighth_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameIsNotNull() {
            addCriterion("eighth_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameEqualTo(String value) {
            addCriterion("eighth_tender_username =", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameNotEqualTo(String value) {
            addCriterion("eighth_tender_username <>", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameGreaterThan(String value) {
            addCriterion("eighth_tender_username >", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("eighth_tender_username >=", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameLessThan(String value) {
            addCriterion("eighth_tender_username <", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("eighth_tender_username <=", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameLike(String value) {
            addCriterion("eighth_tender_username like", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameNotLike(String value) {
            addCriterion("eighth_tender_username not like", value, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameIn(List<String> values) {
            addCriterion("eighth_tender_username in", values, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameNotIn(List<String> values) {
            addCriterion("eighth_tender_username not in", values, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameBetween(String value1, String value2) {
            addCriterion("eighth_tender_username between", value1, value2, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("eighth_tender_username not between", value1, value2, "eighthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountIsNull() {
            addCriterion("eighth_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountIsNotNull() {
            addCriterion("eighth_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountEqualTo(BigDecimal value) {
            addCriterion("eighth_tender_amount =", value, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("eighth_tender_amount <>", value, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("eighth_tender_amount >", value, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("eighth_tender_amount >=", value, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountLessThan(BigDecimal value) {
            addCriterion("eighth_tender_amount <", value, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("eighth_tender_amount <=", value, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountIn(List<BigDecimal> values) {
            addCriterion("eighth_tender_amount in", values, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("eighth_tender_amount not in", values, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eighth_tender_amount between", value1, value2, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andEighthTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eighth_tender_amount not between", value1, value2, "eighthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameIsNull() {
            addCriterion("ninth_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameIsNotNull() {
            addCriterion("ninth_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameEqualTo(String value) {
            addCriterion("ninth_tender_username =", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameNotEqualTo(String value) {
            addCriterion("ninth_tender_username <>", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameGreaterThan(String value) {
            addCriterion("ninth_tender_username >", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("ninth_tender_username >=", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameLessThan(String value) {
            addCriterion("ninth_tender_username <", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("ninth_tender_username <=", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameLike(String value) {
            addCriterion("ninth_tender_username like", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameNotLike(String value) {
            addCriterion("ninth_tender_username not like", value, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameIn(List<String> values) {
            addCriterion("ninth_tender_username in", values, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameNotIn(List<String> values) {
            addCriterion("ninth_tender_username not in", values, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameBetween(String value1, String value2) {
            addCriterion("ninth_tender_username between", value1, value2, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("ninth_tender_username not between", value1, value2, "ninthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountIsNull() {
            addCriterion("ninth_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountIsNotNull() {
            addCriterion("ninth_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountEqualTo(BigDecimal value) {
            addCriterion("ninth_tender_amount =", value, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("ninth_tender_amount <>", value, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("ninth_tender_amount >", value, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ninth_tender_amount >=", value, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountLessThan(BigDecimal value) {
            addCriterion("ninth_tender_amount <", value, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ninth_tender_amount <=", value, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountIn(List<BigDecimal> values) {
            addCriterion("ninth_tender_amount in", values, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("ninth_tender_amount not in", values, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ninth_tender_amount between", value1, value2, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andNinthTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ninth_tender_amount not between", value1, value2, "ninthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameIsNull() {
            addCriterion("tenth_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameIsNotNull() {
            addCriterion("tenth_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameEqualTo(String value) {
            addCriterion("tenth_tender_username =", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameNotEqualTo(String value) {
            addCriterion("tenth_tender_username <>", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameGreaterThan(String value) {
            addCriterion("tenth_tender_username >", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("tenth_tender_username >=", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameLessThan(String value) {
            addCriterion("tenth_tender_username <", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("tenth_tender_username <=", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameLike(String value) {
            addCriterion("tenth_tender_username like", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameNotLike(String value) {
            addCriterion("tenth_tender_username not like", value, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameIn(List<String> values) {
            addCriterion("tenth_tender_username in", values, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameNotIn(List<String> values) {
            addCriterion("tenth_tender_username not in", values, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameBetween(String value1, String value2) {
            addCriterion("tenth_tender_username between", value1, value2, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("tenth_tender_username not between", value1, value2, "tenthTenderUsername");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountIsNull() {
            addCriterion("tenth_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountIsNotNull() {
            addCriterion("tenth_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountEqualTo(BigDecimal value) {
            addCriterion("tenth_tender_amount =", value, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("tenth_tender_amount <>", value, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("tenth_tender_amount >", value, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tenth_tender_amount >=", value, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountLessThan(BigDecimal value) {
            addCriterion("tenth_tender_amount <", value, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tenth_tender_amount <=", value, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountIn(List<BigDecimal> values) {
            addCriterion("tenth_tender_amount in", values, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("tenth_tender_amount not in", values, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tenth_tender_amount between", value1, value2, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenthTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tenth_tender_amount not between", value1, value2, "tenthTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountIsNull() {
            addCriterion("ten_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountIsNotNull() {
            addCriterion("ten_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountEqualTo(BigDecimal value) {
            addCriterion("ten_tender_amount =", value, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountNotEqualTo(BigDecimal value) {
            addCriterion("ten_tender_amount <>", value, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountGreaterThan(BigDecimal value) {
            addCriterion("ten_tender_amount >", value, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ten_tender_amount >=", value, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountLessThan(BigDecimal value) {
            addCriterion("ten_tender_amount <", value, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ten_tender_amount <=", value, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountIn(List<BigDecimal> values) {
            addCriterion("ten_tender_amount in", values, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountNotIn(List<BigDecimal> values) {
            addCriterion("ten_tender_amount not in", values, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ten_tender_amount between", value1, value2, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ten_tender_amount not between", value1, value2, "tenTenderAmount");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionIsNull() {
            addCriterion("ten_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionIsNotNull() {
            addCriterion("ten_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionEqualTo(BigDecimal value) {
            addCriterion("ten_tender_proportion =", value, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("ten_tender_proportion <>", value, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("ten_tender_proportion >", value, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ten_tender_proportion >=", value, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionLessThan(BigDecimal value) {
            addCriterion("ten_tender_proportion <", value, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ten_tender_proportion <=", value, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionIn(List<BigDecimal> values) {
            addCriterion("ten_tender_proportion in", values, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("ten_tender_proportion not in", values, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ten_tender_proportion between", value1, value2, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andTenTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ten_tender_proportion not between", value1, value2, "tenTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountIsNull() {
            addCriterion("other_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountIsNotNull() {
            addCriterion("other_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountEqualTo(Long value) {
            addCriterion("other_tender_amount =", value, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountNotEqualTo(Long value) {
            addCriterion("other_tender_amount <>", value, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountGreaterThan(Long value) {
            addCriterion("other_tender_amount >", value, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("other_tender_amount >=", value, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountLessThan(Long value) {
            addCriterion("other_tender_amount <", value, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountLessThanOrEqualTo(Long value) {
            addCriterion("other_tender_amount <=", value, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountIn(List<Long> values) {
            addCriterion("other_tender_amount in", values, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountNotIn(List<Long> values) {
            addCriterion("other_tender_amount not in", values, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountBetween(Long value1, Long value2) {
            addCriterion("other_tender_amount between", value1, value2, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderAmountNotBetween(Long value1, Long value2) {
            addCriterion("other_tender_amount not between", value1, value2, "otherTenderAmount");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionIsNull() {
            addCriterion("other_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionIsNotNull() {
            addCriterion("other_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionEqualTo(BigDecimal value) {
            addCriterion("other_tender_proportion =", value, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("other_tender_proportion <>", value, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("other_tender_proportion >", value, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("other_tender_proportion >=", value, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionLessThan(BigDecimal value) {
            addCriterion("other_tender_proportion <", value, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("other_tender_proportion <=", value, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionIn(List<BigDecimal> values) {
            addCriterion("other_tender_proportion in", values, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("other_tender_proportion not in", values, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("other_tender_proportion between", value1, value2, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andOtherTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("other_tender_proportion not between", value1, value2, "otherTenderProportion");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameIsNull() {
            addCriterion("most_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameIsNotNull() {
            addCriterion("most_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameEqualTo(String value) {
            addCriterion("most_tender_username =", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameNotEqualTo(String value) {
            addCriterion("most_tender_username <>", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameGreaterThan(String value) {
            addCriterion("most_tender_username >", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("most_tender_username >=", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameLessThan(String value) {
            addCriterion("most_tender_username <", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("most_tender_username <=", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameLike(String value) {
            addCriterion("most_tender_username like", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameNotLike(String value) {
            addCriterion("most_tender_username not like", value, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameIn(List<String> values) {
            addCriterion("most_tender_username in", values, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameNotIn(List<String> values) {
            addCriterion("most_tender_username not in", values, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameBetween(String value1, String value2) {
            addCriterion("most_tender_username between", value1, value2, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("most_tender_username not between", value1, value2, "mostTenderUsername");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountIsNull() {
            addCriterion("most_tender_amount is null");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountIsNotNull() {
            addCriterion("most_tender_amount is not null");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountEqualTo(Long value) {
            addCriterion("most_tender_amount =", value, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountNotEqualTo(Long value) {
            addCriterion("most_tender_amount <>", value, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountGreaterThan(Long value) {
            addCriterion("most_tender_amount >", value, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("most_tender_amount >=", value, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountLessThan(Long value) {
            addCriterion("most_tender_amount <", value, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountLessThanOrEqualTo(Long value) {
            addCriterion("most_tender_amount <=", value, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountIn(List<Long> values) {
            addCriterion("most_tender_amount in", values, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountNotIn(List<Long> values) {
            addCriterion("most_tender_amount not in", values, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountBetween(Long value1, Long value2) {
            addCriterion("most_tender_amount between", value1, value2, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderAmountNotBetween(Long value1, Long value2) {
            addCriterion("most_tender_amount not between", value1, value2, "mostTenderAmount");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeIsNull() {
            addCriterion("most_tender_user_age is null");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeIsNotNull() {
            addCriterion("most_tender_user_age is not null");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeEqualTo(Integer value) {
            addCriterion("most_tender_user_age =", value, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeNotEqualTo(Integer value) {
            addCriterion("most_tender_user_age <>", value, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeGreaterThan(Integer value) {
            addCriterion("most_tender_user_age >", value, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeGreaterThanOrEqualTo(Integer value) {
            addCriterion("most_tender_user_age >=", value, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeLessThan(Integer value) {
            addCriterion("most_tender_user_age <", value, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeLessThanOrEqualTo(Integer value) {
            addCriterion("most_tender_user_age <=", value, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeIn(List<Integer> values) {
            addCriterion("most_tender_user_age in", values, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeNotIn(List<Integer> values) {
            addCriterion("most_tender_user_age not in", values, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeBetween(Integer value1, Integer value2) {
            addCriterion("most_tender_user_age between", value1, value2, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAgeNotBetween(Integer value1, Integer value2) {
            addCriterion("most_tender_user_age not between", value1, value2, "mostTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaIsNull() {
            addCriterion("most_tender_user_area is null");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaIsNotNull() {
            addCriterion("most_tender_user_area is not null");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaEqualTo(String value) {
            addCriterion("most_tender_user_area =", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaNotEqualTo(String value) {
            addCriterion("most_tender_user_area <>", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaGreaterThan(String value) {
            addCriterion("most_tender_user_area >", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaGreaterThanOrEqualTo(String value) {
            addCriterion("most_tender_user_area >=", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaLessThan(String value) {
            addCriterion("most_tender_user_area <", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaLessThanOrEqualTo(String value) {
            addCriterion("most_tender_user_area <=", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaLike(String value) {
            addCriterion("most_tender_user_area like", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaNotLike(String value) {
            addCriterion("most_tender_user_area not like", value, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaIn(List<String> values) {
            addCriterion("most_tender_user_area in", values, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaNotIn(List<String> values) {
            addCriterion("most_tender_user_area not in", values, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaBetween(String value1, String value2) {
            addCriterion("most_tender_user_area between", value1, value2, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andMostTenderUserAreaNotBetween(String value1, String value2) {
            addCriterion("most_tender_user_area not between", value1, value2, "mostTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameIsNull() {
            addCriterion("big_minner_username is null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameIsNotNull() {
            addCriterion("big_minner_username is not null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameEqualTo(String value) {
            addCriterion("big_minner_username =", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameNotEqualTo(String value) {
            addCriterion("big_minner_username <>", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameGreaterThan(String value) {
            addCriterion("big_minner_username >", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("big_minner_username >=", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameLessThan(String value) {
            addCriterion("big_minner_username <", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameLessThanOrEqualTo(String value) {
            addCriterion("big_minner_username <=", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameLike(String value) {
            addCriterion("big_minner_username like", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameNotLike(String value) {
            addCriterion("big_minner_username not like", value, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameIn(List<String> values) {
            addCriterion("big_minner_username in", values, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameNotIn(List<String> values) {
            addCriterion("big_minner_username not in", values, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameBetween(String value1, String value2) {
            addCriterion("big_minner_username between", value1, value2, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUsernameNotBetween(String value1, String value2) {
            addCriterion("big_minner_username not between", value1, value2, "bigMinnerUsername");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitIsNull() {
            addCriterion("big_minner_profit is null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitIsNotNull() {
            addCriterion("big_minner_profit is not null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitEqualTo(Long value) {
            addCriterion("big_minner_profit =", value, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitNotEqualTo(Long value) {
            addCriterion("big_minner_profit <>", value, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitGreaterThan(Long value) {
            addCriterion("big_minner_profit >", value, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("big_minner_profit >=", value, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitLessThan(Long value) {
            addCriterion("big_minner_profit <", value, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitLessThanOrEqualTo(Long value) {
            addCriterion("big_minner_profit <=", value, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitIn(List<Long> values) {
            addCriterion("big_minner_profit in", values, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitNotIn(List<Long> values) {
            addCriterion("big_minner_profit not in", values, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitBetween(Long value1, Long value2) {
            addCriterion("big_minner_profit between", value1, value2, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerProfitNotBetween(Long value1, Long value2) {
            addCriterion("big_minner_profit not between", value1, value2, "bigMinnerProfit");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeIsNull() {
            addCriterion("big_minner_user_age is null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeIsNotNull() {
            addCriterion("big_minner_user_age is not null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeEqualTo(Integer value) {
            addCriterion("big_minner_user_age =", value, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeNotEqualTo(Integer value) {
            addCriterion("big_minner_user_age <>", value, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeGreaterThan(Integer value) {
            addCriterion("big_minner_user_age >", value, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeGreaterThanOrEqualTo(Integer value) {
            addCriterion("big_minner_user_age >=", value, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeLessThan(Integer value) {
            addCriterion("big_minner_user_age <", value, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeLessThanOrEqualTo(Integer value) {
            addCriterion("big_minner_user_age <=", value, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeIn(List<Integer> values) {
            addCriterion("big_minner_user_age in", values, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeNotIn(List<Integer> values) {
            addCriterion("big_minner_user_age not in", values, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeBetween(Integer value1, Integer value2) {
            addCriterion("big_minner_user_age between", value1, value2, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAgeNotBetween(Integer value1, Integer value2) {
            addCriterion("big_minner_user_age not between", value1, value2, "bigMinnerUserAge");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaIsNull() {
            addCriterion("big_minner_user_area is null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaIsNotNull() {
            addCriterion("big_minner_user_area is not null");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaEqualTo(String value) {
            addCriterion("big_minner_user_area =", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaNotEqualTo(String value) {
            addCriterion("big_minner_user_area <>", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaGreaterThan(String value) {
            addCriterion("big_minner_user_area >", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaGreaterThanOrEqualTo(String value) {
            addCriterion("big_minner_user_area >=", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaLessThan(String value) {
            addCriterion("big_minner_user_area <", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaLessThanOrEqualTo(String value) {
            addCriterion("big_minner_user_area <=", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaLike(String value) {
            addCriterion("big_minner_user_area like", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaNotLike(String value) {
            addCriterion("big_minner_user_area not like", value, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaIn(List<String> values) {
            addCriterion("big_minner_user_area in", values, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaNotIn(List<String> values) {
            addCriterion("big_minner_user_area not in", values, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaBetween(String value1, String value2) {
            addCriterion("big_minner_user_area between", value1, value2, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andBigMinnerUserAreaNotBetween(String value1, String value2) {
            addCriterion("big_minner_user_area not between", value1, value2, "bigMinnerUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameIsNull() {
            addCriterion("active_tender_username is null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameIsNotNull() {
            addCriterion("active_tender_username is not null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameEqualTo(String value) {
            addCriterion("active_tender_username =", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameNotEqualTo(String value) {
            addCriterion("active_tender_username <>", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameGreaterThan(String value) {
            addCriterion("active_tender_username >", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("active_tender_username >=", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameLessThan(String value) {
            addCriterion("active_tender_username <", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameLessThanOrEqualTo(String value) {
            addCriterion("active_tender_username <=", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameLike(String value) {
            addCriterion("active_tender_username like", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameNotLike(String value) {
            addCriterion("active_tender_username not like", value, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameIn(List<String> values) {
            addCriterion("active_tender_username in", values, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameNotIn(List<String> values) {
            addCriterion("active_tender_username not in", values, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameBetween(String value1, String value2) {
            addCriterion("active_tender_username between", value1, value2, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUsernameNotBetween(String value1, String value2) {
            addCriterion("active_tender_username not between", value1, value2, "activeTenderUsername");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumIsNull() {
            addCriterion("active_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumIsNotNull() {
            addCriterion("active_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumEqualTo(Long value) {
            addCriterion("active_tender_num =", value, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumNotEqualTo(Long value) {
            addCriterion("active_tender_num <>", value, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumGreaterThan(Long value) {
            addCriterion("active_tender_num >", value, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumGreaterThanOrEqualTo(Long value) {
            addCriterion("active_tender_num >=", value, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumLessThan(Long value) {
            addCriterion("active_tender_num <", value, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumLessThanOrEqualTo(Long value) {
            addCriterion("active_tender_num <=", value, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumIn(List<Long> values) {
            addCriterion("active_tender_num in", values, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumNotIn(List<Long> values) {
            addCriterion("active_tender_num not in", values, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumBetween(Long value1, Long value2) {
            addCriterion("active_tender_num between", value1, value2, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderNumNotBetween(Long value1, Long value2) {
            addCriterion("active_tender_num not between", value1, value2, "activeTenderNum");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeIsNull() {
            addCriterion("active_tender_user_age is null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeIsNotNull() {
            addCriterion("active_tender_user_age is not null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeEqualTo(Integer value) {
            addCriterion("active_tender_user_age =", value, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeNotEqualTo(Integer value) {
            addCriterion("active_tender_user_age <>", value, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeGreaterThan(Integer value) {
            addCriterion("active_tender_user_age >", value, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeGreaterThanOrEqualTo(Integer value) {
            addCriterion("active_tender_user_age >=", value, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeLessThan(Integer value) {
            addCriterion("active_tender_user_age <", value, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeLessThanOrEqualTo(Integer value) {
            addCriterion("active_tender_user_age <=", value, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeIn(List<Integer> values) {
            addCriterion("active_tender_user_age in", values, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeNotIn(List<Integer> values) {
            addCriterion("active_tender_user_age not in", values, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeBetween(Integer value1, Integer value2) {
            addCriterion("active_tender_user_age between", value1, value2, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAgeNotBetween(Integer value1, Integer value2) {
            addCriterion("active_tender_user_age not between", value1, value2, "activeTenderUserAge");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaIsNull() {
            addCriterion("active_tender_user_area is null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaIsNotNull() {
            addCriterion("active_tender_user_area is not null");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaEqualTo(String value) {
            addCriterion("active_tender_user_area =", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaNotEqualTo(String value) {
            addCriterion("active_tender_user_area <>", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaGreaterThan(String value) {
            addCriterion("active_tender_user_area >", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaGreaterThanOrEqualTo(String value) {
            addCriterion("active_tender_user_area >=", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaLessThan(String value) {
            addCriterion("active_tender_user_area <", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaLessThanOrEqualTo(String value) {
            addCriterion("active_tender_user_area <=", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaLike(String value) {
            addCriterion("active_tender_user_area like", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaNotLike(String value) {
            addCriterion("active_tender_user_area not like", value, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaIn(List<String> values) {
            addCriterion("active_tender_user_area in", values, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaNotIn(List<String> values) {
            addCriterion("active_tender_user_area not in", values, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaBetween(String value1, String value2) {
            addCriterion("active_tender_user_area between", value1, value2, "activeTenderUserArea");
            return (Criteria) this;
        }

        public Criteria andActiveTenderUserAreaNotBetween(String value1, String value2) {
            addCriterion("active_tender_user_area not between", value1, value2, "activeTenderUserArea");
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