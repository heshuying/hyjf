package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class ActivityBillionSecondTimeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActivityBillionSecondTimeExample() {
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

        public Criteria andAccordPointIsNull() {
            addCriterion("accord_point is null");
            return (Criteria) this;
        }

        public Criteria andAccordPointIsNotNull() {
            addCriterion("accord_point is not null");
            return (Criteria) this;
        }

        public Criteria andAccordPointEqualTo(Integer value) {
            addCriterion("accord_point =", value, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointNotEqualTo(Integer value) {
            addCriterion("accord_point <>", value, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointGreaterThan(Integer value) {
            addCriterion("accord_point >", value, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointGreaterThanOrEqualTo(Integer value) {
            addCriterion("accord_point >=", value, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointLessThan(Integer value) {
            addCriterion("accord_point <", value, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointLessThanOrEqualTo(Integer value) {
            addCriterion("accord_point <=", value, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointIn(List<Integer> values) {
            addCriterion("accord_point in", values, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointNotIn(List<Integer> values) {
            addCriterion("accord_point not in", values, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointBetween(Integer value1, Integer value2) {
            addCriterion("accord_point between", value1, value2, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordPointNotBetween(Integer value1, Integer value2) {
            addCriterion("accord_point not between", value1, value2, "accordPoint");
            return (Criteria) this;
        }

        public Criteria andAccordTimeIsNull() {
            addCriterion("accord_time is null");
            return (Criteria) this;
        }

        public Criteria andAccordTimeIsNotNull() {
            addCriterion("accord_time is not null");
            return (Criteria) this;
        }

        public Criteria andAccordTimeEqualTo(Integer value) {
            addCriterion("accord_time =", value, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeNotEqualTo(Integer value) {
            addCriterion("accord_time <>", value, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeGreaterThan(Integer value) {
            addCriterion("accord_time >", value, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("accord_time >=", value, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeLessThan(Integer value) {
            addCriterion("accord_time <", value, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeLessThanOrEqualTo(Integer value) {
            addCriterion("accord_time <=", value, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeIn(List<Integer> values) {
            addCriterion("accord_time in", values, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeNotIn(List<Integer> values) {
            addCriterion("accord_time not in", values, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeBetween(Integer value1, Integer value2) {
            addCriterion("accord_time between", value1, value2, "accordTime");
            return (Criteria) this;
        }

        public Criteria andAccordTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("accord_time not between", value1, value2, "accordTime");
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