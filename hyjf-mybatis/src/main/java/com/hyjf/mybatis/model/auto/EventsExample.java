package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class EventsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public EventsExample() {
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

        public Criteria andEventTimeIsNull() {
            addCriterion("event_time is null");
            return (Criteria) this;
        }

        public Criteria andEventTimeIsNotNull() {
            addCriterion("event_time is not null");
            return (Criteria) this;
        }

        public Criteria andEventTimeEqualTo(String value) {
            addCriterion("event_time =", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeNotEqualTo(String value) {
            addCriterion("event_time <>", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeGreaterThan(String value) {
            addCriterion("event_time >", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeGreaterThanOrEqualTo(String value) {
            addCriterion("event_time >=", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeLessThan(String value) {
            addCriterion("event_time <", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeLessThanOrEqualTo(String value) {
            addCriterion("event_time <=", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeLike(String value) {
            addCriterion("event_time like", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeNotLike(String value) {
            addCriterion("event_time not like", value, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeIn(List<String> values) {
            addCriterion("event_time in", values, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeNotIn(List<String> values) {
            addCriterion("event_time not in", values, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeBetween(String value1, String value2) {
            addCriterion("event_time between", value1, value2, "eventTime");
            return (Criteria) this;
        }

        public Criteria andEventTimeNotBetween(String value1, String value2) {
            addCriterion("event_time not between", value1, value2, "eventTime");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andEventYearIsNull() {
            addCriterion("event_year is null");
            return (Criteria) this;
        }

        public Criteria andEventYearIsNotNull() {
            addCriterion("event_year is not null");
            return (Criteria) this;
        }

        public Criteria andEventYearEqualTo(Integer value) {
            addCriterion("event_year =", value, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearNotEqualTo(Integer value) {
            addCriterion("event_year <>", value, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearGreaterThan(Integer value) {
            addCriterion("event_year >", value, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearGreaterThanOrEqualTo(Integer value) {
            addCriterion("event_year >=", value, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearLessThan(Integer value) {
            addCriterion("event_year <", value, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearLessThanOrEqualTo(Integer value) {
            addCriterion("event_year <=", value, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearIn(List<Integer> values) {
            addCriterion("event_year in", values, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearNotIn(List<Integer> values) {
            addCriterion("event_year not in", values, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearBetween(Integer value1, Integer value2) {
            addCriterion("event_year between", value1, value2, "eventYear");
            return (Criteria) this;
        }

        public Criteria andEventYearNotBetween(Integer value1, Integer value2) {
            addCriterion("event_year not between", value1, value2, "eventYear");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
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

        public Criteria andAddAdminIsNull() {
            addCriterion("add_admin is null");
            return (Criteria) this;
        }

        public Criteria andAddAdminIsNotNull() {
            addCriterion("add_admin is not null");
            return (Criteria) this;
        }

        public Criteria andAddAdminEqualTo(String value) {
            addCriterion("add_admin =", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotEqualTo(String value) {
            addCriterion("add_admin <>", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminGreaterThan(String value) {
            addCriterion("add_admin >", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminGreaterThanOrEqualTo(String value) {
            addCriterion("add_admin >=", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminLessThan(String value) {
            addCriterion("add_admin <", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminLessThanOrEqualTo(String value) {
            addCriterion("add_admin <=", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminLike(String value) {
            addCriterion("add_admin like", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotLike(String value) {
            addCriterion("add_admin not like", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminIn(List<String> values) {
            addCriterion("add_admin in", values, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotIn(List<String> values) {
            addCriterion("add_admin not in", values, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminBetween(String value1, String value2) {
            addCriterion("add_admin between", value1, value2, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotBetween(String value1, String value2) {
            addCriterion("add_admin not between", value1, value2, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andActTimeIsNull() {
            addCriterion("act_time is null");
            return (Criteria) this;
        }

        public Criteria andActTimeIsNotNull() {
            addCriterion("act_time is not null");
            return (Criteria) this;
        }

        public Criteria andActTimeEqualTo(Integer value) {
            addCriterion("act_time =", value, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeNotEqualTo(Integer value) {
            addCriterion("act_time <>", value, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeGreaterThan(Integer value) {
            addCriterion("act_time >", value, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("act_time >=", value, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeLessThan(Integer value) {
            addCriterion("act_time <", value, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeLessThanOrEqualTo(Integer value) {
            addCriterion("act_time <=", value, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeIn(List<Integer> values) {
            addCriterion("act_time in", values, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeNotIn(List<Integer> values) {
            addCriterion("act_time not in", values, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeBetween(Integer value1, Integer value2) {
            addCriterion("act_time between", value1, value2, "actTime");
            return (Criteria) this;
        }

        public Criteria andActTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("act_time not between", value1, value2, "actTime");
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