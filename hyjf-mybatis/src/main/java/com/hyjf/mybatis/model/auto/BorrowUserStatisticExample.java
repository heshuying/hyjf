package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BorrowUserStatisticExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public BorrowUserStatisticExample() {
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

        public Criteria andBorrowuserCountTotalIsNull() {
            addCriterion("borrowuser_count_total is null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalIsNotNull() {
            addCriterion("borrowuser_count_total is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalEqualTo(Integer value) {
            addCriterion("borrowuser_count_total =", value, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalNotEqualTo(Integer value) {
            addCriterion("borrowuser_count_total <>", value, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalGreaterThan(Integer value) {
            addCriterion("borrowuser_count_total >", value, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("borrowuser_count_total >=", value, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalLessThan(Integer value) {
            addCriterion("borrowuser_count_total <", value, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalLessThanOrEqualTo(Integer value) {
            addCriterion("borrowuser_count_total <=", value, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalIn(List<Integer> values) {
            addCriterion("borrowuser_count_total in", values, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalNotIn(List<Integer> values) {
            addCriterion("borrowuser_count_total not in", values, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalBetween(Integer value1, Integer value2) {
            addCriterion("borrowuser_count_total between", value1, value2, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("borrowuser_count_total not between", value1, value2, "borrowuserCountTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentIsNull() {
            addCriterion("borrowuser_count_current is null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentIsNotNull() {
            addCriterion("borrowuser_count_current is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentEqualTo(Integer value) {
            addCriterion("borrowuser_count_current =", value, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentNotEqualTo(Integer value) {
            addCriterion("borrowuser_count_current <>", value, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentGreaterThan(Integer value) {
            addCriterion("borrowuser_count_current >", value, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentGreaterThanOrEqualTo(Integer value) {
            addCriterion("borrowuser_count_current >=", value, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentLessThan(Integer value) {
            addCriterion("borrowuser_count_current <", value, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentLessThanOrEqualTo(Integer value) {
            addCriterion("borrowuser_count_current <=", value, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentIn(List<Integer> values) {
            addCriterion("borrowuser_count_current in", values, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentNotIn(List<Integer> values) {
            addCriterion("borrowuser_count_current not in", values, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentBetween(Integer value1, Integer value2) {
            addCriterion("borrowuser_count_current between", value1, value2, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserCountCurrentNotBetween(Integer value1, Integer value2) {
            addCriterion("borrowuser_count_current not between", value1, value2, "borrowuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenIsNull() {
            addCriterion("borrowuser_money_topten is null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenIsNotNull() {
            addCriterion("borrowuser_money_topten is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topten =", value, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenNotEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topten <>", value, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenGreaterThan(BigDecimal value) {
            addCriterion("borrowuser_money_topten >", value, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topten >=", value, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenLessThan(BigDecimal value) {
            addCriterion("borrowuser_money_topten <", value, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenLessThanOrEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topten <=", value, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenIn(List<BigDecimal> values) {
            addCriterion("borrowuser_money_topten in", values, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenNotIn(List<BigDecimal> values) {
            addCriterion("borrowuser_money_topten not in", values, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrowuser_money_topten between", value1, value2, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToptenNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrowuser_money_topten not between", value1, value2, "borrowuserMoneyTopten");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeIsNull() {
            addCriterion("borrowuser_money_topone is null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeIsNotNull() {
            addCriterion("borrowuser_money_topone is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topone =", value, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeNotEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topone <>", value, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeGreaterThan(BigDecimal value) {
            addCriterion("borrowuser_money_topone >", value, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topone >=", value, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeLessThan(BigDecimal value) {
            addCriterion("borrowuser_money_topone <", value, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_topone <=", value, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeIn(List<BigDecimal> values) {
            addCriterion("borrowuser_money_topone in", values, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeNotIn(List<BigDecimal> values) {
            addCriterion("borrowuser_money_topone not in", values, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrowuser_money_topone between", value1, value2, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyToponeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrowuser_money_topone not between", value1, value2, "borrowuserMoneyTopone");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalIsNull() {
            addCriterion("borrowuser_money_total is null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalIsNotNull() {
            addCriterion("borrowuser_money_total is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_total =", value, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalNotEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_total <>", value, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalGreaterThan(BigDecimal value) {
            addCriterion("borrowuser_money_total >", value, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_total >=", value, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalLessThan(BigDecimal value) {
            addCriterion("borrowuser_money_total <", value, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("borrowuser_money_total <=", value, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalIn(List<BigDecimal> values) {
            addCriterion("borrowuser_money_total in", values, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalNotIn(List<BigDecimal> values) {
            addCriterion("borrowuser_money_total not in", values, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrowuser_money_total between", value1, value2, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andBorrowuserMoneyTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrowuser_money_total not between", value1, value2, "borrowuserMoneyTotal");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentIsNull() {
            addCriterion("tenderuser_count_current is null");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentIsNotNull() {
            addCriterion("tenderuser_count_current is not null");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentEqualTo(Integer value) {
            addCriterion("tenderuser_count_current =", value, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentNotEqualTo(Integer value) {
            addCriterion("tenderuser_count_current <>", value, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentGreaterThan(Integer value) {
            addCriterion("tenderuser_count_current >", value, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentGreaterThanOrEqualTo(Integer value) {
            addCriterion("tenderuser_count_current >=", value, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentLessThan(Integer value) {
            addCriterion("tenderuser_count_current <", value, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentLessThanOrEqualTo(Integer value) {
            addCriterion("tenderuser_count_current <=", value, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentIn(List<Integer> values) {
            addCriterion("tenderuser_count_current in", values, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentNotIn(List<Integer> values) {
            addCriterion("tenderuser_count_current not in", values, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentBetween(Integer value1, Integer value2) {
            addCriterion("tenderuser_count_current between", value1, value2, "tenderuserCountCurrent");
            return (Criteria) this;
        }

        public Criteria andTenderuserCountCurrentNotBetween(Integer value1, Integer value2) {
            addCriterion("tenderuser_count_current not between", value1, value2, "tenderuserCountCurrent");
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