package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RechargeFeeStatisticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public RechargeFeeStatisticsExample() {
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

        public Criteria andStatDateIsNull() {
            addCriterion("stat_date is null");
            return (Criteria) this;
        }

        public Criteria andStatDateIsNotNull() {
            addCriterion("stat_date is not null");
            return (Criteria) this;
        }

        public Criteria andStatDateEqualTo(String value) {
            addCriterion("stat_date =", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotEqualTo(String value) {
            addCriterion("stat_date <>", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThan(String value) {
            addCriterion("stat_date >", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThanOrEqualTo(String value) {
            addCriterion("stat_date >=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThan(String value) {
            addCriterion("stat_date <", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThanOrEqualTo(String value) {
            addCriterion("stat_date <=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLike(String value) {
            addCriterion("stat_date like", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotLike(String value) {
            addCriterion("stat_date not like", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateIn(List<String> values) {
            addCriterion("stat_date in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotIn(List<String> values) {
            addCriterion("stat_date not in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateBetween(String value1, String value2) {
            addCriterion("stat_date between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotBetween(String value1, String value2) {
            addCriterion("stat_date not between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountIsNull() {
            addCriterion("recharge_amount is null");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountIsNotNull() {
            addCriterion("recharge_amount is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountEqualTo(BigDecimal value) {
            addCriterion("recharge_amount =", value, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountNotEqualTo(BigDecimal value) {
            addCriterion("recharge_amount <>", value, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountGreaterThan(BigDecimal value) {
            addCriterion("recharge_amount >", value, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("recharge_amount >=", value, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountLessThan(BigDecimal value) {
            addCriterion("recharge_amount <", value, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("recharge_amount <=", value, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountIn(List<BigDecimal> values) {
            addCriterion("recharge_amount in", values, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountNotIn(List<BigDecimal> values) {
            addCriterion("recharge_amount not in", values, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recharge_amount between", value1, value2, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recharge_amount not between", value1, value2, "rechargeAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountIsNull() {
            addCriterion("quick_amount is null");
            return (Criteria) this;
        }

        public Criteria andQuickAmountIsNotNull() {
            addCriterion("quick_amount is not null");
            return (Criteria) this;
        }

        public Criteria andQuickAmountEqualTo(BigDecimal value) {
            addCriterion("quick_amount =", value, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountNotEqualTo(BigDecimal value) {
            addCriterion("quick_amount <>", value, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountGreaterThan(BigDecimal value) {
            addCriterion("quick_amount >", value, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("quick_amount >=", value, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountLessThan(BigDecimal value) {
            addCriterion("quick_amount <", value, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("quick_amount <=", value, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountIn(List<BigDecimal> values) {
            addCriterion("quick_amount in", values, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountNotIn(List<BigDecimal> values) {
            addCriterion("quick_amount not in", values, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quick_amount between", value1, value2, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andQuickAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("quick_amount not between", value1, value2, "quickAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountIsNull() {
            addCriterion("personal_amount is null");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountIsNotNull() {
            addCriterion("personal_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountEqualTo(BigDecimal value) {
            addCriterion("personal_amount =", value, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountNotEqualTo(BigDecimal value) {
            addCriterion("personal_amount <>", value, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountGreaterThan(BigDecimal value) {
            addCriterion("personal_amount >", value, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("personal_amount >=", value, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountLessThan(BigDecimal value) {
            addCriterion("personal_amount <", value, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("personal_amount <=", value, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountIn(List<BigDecimal> values) {
            addCriterion("personal_amount in", values, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountNotIn(List<BigDecimal> values) {
            addCriterion("personal_amount not in", values, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("personal_amount between", value1, value2, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andPersonalAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("personal_amount not between", value1, value2, "personalAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountIsNull() {
            addCriterion("com_amount is null");
            return (Criteria) this;
        }

        public Criteria andComAmountIsNotNull() {
            addCriterion("com_amount is not null");
            return (Criteria) this;
        }

        public Criteria andComAmountEqualTo(BigDecimal value) {
            addCriterion("com_amount =", value, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountNotEqualTo(BigDecimal value) {
            addCriterion("com_amount <>", value, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountGreaterThan(BigDecimal value) {
            addCriterion("com_amount >", value, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("com_amount >=", value, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountLessThan(BigDecimal value) {
            addCriterion("com_amount <", value, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("com_amount <=", value, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountIn(List<BigDecimal> values) {
            addCriterion("com_amount in", values, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountNotIn(List<BigDecimal> values) {
            addCriterion("com_amount not in", values, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("com_amount between", value1, value2, "comAmount");
            return (Criteria) this;
        }

        public Criteria andComAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("com_amount not between", value1, value2, "comAmount");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(BigDecimal value) {
            addCriterion("fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(BigDecimal value) {
            addCriterion("fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(BigDecimal value) {
            addCriterion("fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(BigDecimal value) {
            addCriterion("fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<BigDecimal> values) {
            addCriterion("fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<BigDecimal> values) {
            addCriterion("fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee not between", value1, value2, "fee");
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