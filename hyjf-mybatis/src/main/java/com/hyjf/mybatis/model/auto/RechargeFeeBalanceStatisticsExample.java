package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RechargeFeeBalanceStatisticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public RechargeFeeBalanceStatisticsExample() {
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

        public Criteria andCurDateIsNull() {
            addCriterion("cur_date is null");
            return (Criteria) this;
        }

        public Criteria andCurDateIsNotNull() {
            addCriterion("cur_date is not null");
            return (Criteria) this;
        }

        public Criteria andCurDateEqualTo(String value) {
            addCriterion("cur_date =", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateNotEqualTo(String value) {
            addCriterion("cur_date <>", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateGreaterThan(String value) {
            addCriterion("cur_date >", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateGreaterThanOrEqualTo(String value) {
            addCriterion("cur_date >=", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateLessThan(String value) {
            addCriterion("cur_date <", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateLessThanOrEqualTo(String value) {
            addCriterion("cur_date <=", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateLike(String value) {
            addCriterion("cur_date like", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateNotLike(String value) {
            addCriterion("cur_date not like", value, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateIn(List<String> values) {
            addCriterion("cur_date in", values, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateNotIn(List<String> values) {
            addCriterion("cur_date not in", values, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateBetween(String value1, String value2) {
            addCriterion("cur_date between", value1, value2, "curDate");
            return (Criteria) this;
        }

        public Criteria andCurDateNotBetween(String value1, String value2) {
            addCriterion("cur_date not between", value1, value2, "curDate");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeIsNull() {
            addCriterion("sub_account_code is null");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeIsNotNull() {
            addCriterion("sub_account_code is not null");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeEqualTo(String value) {
            addCriterion("sub_account_code =", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeNotEqualTo(String value) {
            addCriterion("sub_account_code <>", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeGreaterThan(String value) {
            addCriterion("sub_account_code >", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeGreaterThanOrEqualTo(String value) {
            addCriterion("sub_account_code >=", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeLessThan(String value) {
            addCriterion("sub_account_code <", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeLessThanOrEqualTo(String value) {
            addCriterion("sub_account_code <=", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeLike(String value) {
            addCriterion("sub_account_code like", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeNotLike(String value) {
            addCriterion("sub_account_code not like", value, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeIn(List<String> values) {
            addCriterion("sub_account_code in", values, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeNotIn(List<String> values) {
            addCriterion("sub_account_code not in", values, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeBetween(String value1, String value2) {
            addCriterion("sub_account_code between", value1, value2, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andSubAccountCodeNotBetween(String value1, String value2) {
            addCriterion("sub_account_code not between", value1, value2, "subAccountCode");
            return (Criteria) this;
        }

        public Criteria andTimePointIsNull() {
            addCriterion("time_point is null");
            return (Criteria) this;
        }

        public Criteria andTimePointIsNotNull() {
            addCriterion("time_point is not null");
            return (Criteria) this;
        }

        public Criteria andTimePointEqualTo(String value) {
            addCriterion("time_point =", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointNotEqualTo(String value) {
            addCriterion("time_point <>", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointGreaterThan(String value) {
            addCriterion("time_point >", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointGreaterThanOrEqualTo(String value) {
            addCriterion("time_point >=", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointLessThan(String value) {
            addCriterion("time_point <", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointLessThanOrEqualTo(String value) {
            addCriterion("time_point <=", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointLike(String value) {
            addCriterion("time_point like", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointNotLike(String value) {
            addCriterion("time_point not like", value, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointIn(List<String> values) {
            addCriterion("time_point in", values, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointNotIn(List<String> values) {
            addCriterion("time_point not in", values, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointBetween(String value1, String value2) {
            addCriterion("time_point between", value1, value2, "timePoint");
            return (Criteria) this;
        }

        public Criteria andTimePointNotBetween(String value1, String value2) {
            addCriterion("time_point not between", value1, value2, "timePoint");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNull() {
            addCriterion("balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(BigDecimal value) {
            addCriterion("balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(BigDecimal value) {
            addCriterion("balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(BigDecimal value) {
            addCriterion("balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(BigDecimal value) {
            addCriterion("balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<BigDecimal> values) {
            addCriterion("balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<BigDecimal> values) {
            addCriterion("balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance not between", value1, value2, "balance");
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