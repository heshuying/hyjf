package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TenderMonthInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public TenderMonthInfoExample() {
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

        public Criteria andTenderTypeIsNull() {
            addCriterion("tender_type is null");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIsNotNull() {
            addCriterion("tender_type is not null");
            return (Criteria) this;
        }

        public Criteria andTenderTypeEqualTo(Integer value) {
            addCriterion("tender_type =", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotEqualTo(Integer value) {
            addCriterion("tender_type <>", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeGreaterThan(Integer value) {
            addCriterion("tender_type >", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_type >=", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeLessThan(Integer value) {
            addCriterion("tender_type <", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeLessThanOrEqualTo(Integer value) {
            addCriterion("tender_type <=", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIn(List<Integer> values) {
            addCriterion("tender_type in", values, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotIn(List<Integer> values) {
            addCriterion("tender_type not in", values, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeBetween(Integer value1, Integer value2) {
            addCriterion("tender_type between", value1, value2, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_type not between", value1, value2, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTimeIsNull() {
            addCriterion("tender_time is null");
            return (Criteria) this;
        }

        public Criteria andTenderTimeIsNotNull() {
            addCriterion("tender_time is not null");
            return (Criteria) this;
        }

        public Criteria andTenderTimeEqualTo(String value) {
            addCriterion("tender_time =", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotEqualTo(String value) {
            addCriterion("tender_time <>", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeGreaterThan(String value) {
            addCriterion("tender_time >", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeGreaterThanOrEqualTo(String value) {
            addCriterion("tender_time >=", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeLessThan(String value) {
            addCriterion("tender_time <", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeLessThanOrEqualTo(String value) {
            addCriterion("tender_time <=", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeLike(String value) {
            addCriterion("tender_time like", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotLike(String value) {
            addCriterion("tender_time not like", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeIn(List<String> values) {
            addCriterion("tender_time in", values, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotIn(List<String> values) {
            addCriterion("tender_time not in", values, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeBetween(String value1, String value2) {
            addCriterion("tender_time between", value1, value2, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotBetween(String value1, String value2) {
            addCriterion("tender_time not between", value1, value2, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNull() {
            addCriterion("addtime is null");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNotNull() {
            addCriterion("addtime is not null");
            return (Criteria) this;
        }

        public Criteria andAddtimeEqualTo(Date value) {
            addCriterion("addtime =", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotEqualTo(Date value) {
            addCriterion("addtime <>", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThan(Date value) {
            addCriterion("addtime >", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("addtime >=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThan(Date value) {
            addCriterion("addtime <", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThanOrEqualTo(Date value) {
            addCriterion("addtime <=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeIn(List<Date> values) {
            addCriterion("addtime in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotIn(List<Date> values) {
            addCriterion("addtime not in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeBetween(Date value1, Date value2) {
            addCriterion("addtime between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotBetween(Date value1, Date value2) {
            addCriterion("addtime not between", value1, value2, "addtime");
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