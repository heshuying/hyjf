package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class StatisticsTzjHourExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public StatisticsTzjHourExample() {
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

        public Criteria andHourIsNull() {
            addCriterion("`hour` is null");
            return (Criteria) this;
        }

        public Criteria andHourIsNotNull() {
            addCriterion("`hour` is not null");
            return (Criteria) this;
        }

        public Criteria andHourEqualTo(String value) {
            addCriterion("`hour` =", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourNotEqualTo(String value) {
            addCriterion("`hour` <>", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourGreaterThan(String value) {
            addCriterion("`hour` >", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourGreaterThanOrEqualTo(String value) {
            addCriterion("`hour` >=", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourLessThan(String value) {
            addCriterion("`hour` <", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourLessThanOrEqualTo(String value) {
            addCriterion("`hour` <=", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourLike(String value) {
            addCriterion("`hour` like", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourNotLike(String value) {
            addCriterion("`hour` not like", value, "hour");
            return (Criteria) this;
        }

        public Criteria andHourIn(List<String> values) {
            addCriterion("`hour` in", values, "hour");
            return (Criteria) this;
        }

        public Criteria andHourNotIn(List<String> values) {
            addCriterion("`hour` not in", values, "hour");
            return (Criteria) this;
        }

        public Criteria andHourBetween(String value1, String value2) {
            addCriterion("`hour` between", value1, value2, "hour");
            return (Criteria) this;
        }

        public Criteria andHourNotBetween(String value1, String value2) {
            addCriterion("`hour` not between", value1, value2, "hour");
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