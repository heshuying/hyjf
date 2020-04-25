package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class NewyearCaishenCardQuantityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public NewyearCaishenCardQuantityExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityIsNull() {
            addCriterion("card_jin_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityIsNotNull() {
            addCriterion("card_jin_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityEqualTo(Integer value) {
            addCriterion("card_jin_quantity =", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityNotEqualTo(Integer value) {
            addCriterion("card_jin_quantity <>", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityGreaterThan(Integer value) {
            addCriterion("card_jin_quantity >", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_jin_quantity >=", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityLessThan(Integer value) {
            addCriterion("card_jin_quantity <", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_jin_quantity <=", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityIn(List<Integer> values) {
            addCriterion("card_jin_quantity in", values, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityNotIn(List<Integer> values) {
            addCriterion("card_jin_quantity not in", values, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_jin_quantity between", value1, value2, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_jin_quantity not between", value1, value2, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityIsNull() {
            addCriterion("card_ji_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityIsNotNull() {
            addCriterion("card_ji_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityEqualTo(Integer value) {
            addCriterion("card_ji_quantity =", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityNotEqualTo(Integer value) {
            addCriterion("card_ji_quantity <>", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityGreaterThan(Integer value) {
            addCriterion("card_ji_quantity >", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_ji_quantity >=", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityLessThan(Integer value) {
            addCriterion("card_ji_quantity <", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_ji_quantity <=", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityIn(List<Integer> values) {
            addCriterion("card_ji_quantity in", values, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityNotIn(List<Integer> values) {
            addCriterion("card_ji_quantity not in", values, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_ji_quantity between", value1, value2, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_ji_quantity not between", value1, value2, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityIsNull() {
            addCriterion("card_na_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityIsNotNull() {
            addCriterion("card_na_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityEqualTo(Integer value) {
            addCriterion("card_na_quantity =", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityNotEqualTo(Integer value) {
            addCriterion("card_na_quantity <>", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityGreaterThan(Integer value) {
            addCriterion("card_na_quantity >", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_na_quantity >=", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityLessThan(Integer value) {
            addCriterion("card_na_quantity <", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_na_quantity <=", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityIn(List<Integer> values) {
            addCriterion("card_na_quantity in", values, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityNotIn(List<Integer> values) {
            addCriterion("card_na_quantity not in", values, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_na_quantity between", value1, value2, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_na_quantity not between", value1, value2, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityIsNull() {
            addCriterion("card_fu_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityIsNotNull() {
            addCriterion("card_fu_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityEqualTo(Integer value) {
            addCriterion("card_fu_quantity =", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityNotEqualTo(Integer value) {
            addCriterion("card_fu_quantity <>", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityGreaterThan(Integer value) {
            addCriterion("card_fu_quantity >", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_fu_quantity >=", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityLessThan(Integer value) {
            addCriterion("card_fu_quantity <", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_fu_quantity <=", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityIn(List<Integer> values) {
            addCriterion("card_fu_quantity in", values, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityNotIn(List<Integer> values) {
            addCriterion("card_fu_quantity not in", values, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_fu_quantity between", value1, value2, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_fu_quantity not between", value1, value2, "cardFuQuantity");
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