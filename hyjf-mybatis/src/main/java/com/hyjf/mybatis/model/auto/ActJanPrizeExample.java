package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ActJanPrizeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActJanPrizeExample() {
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

        public Criteria andPrizeNameIsNull() {
            addCriterion("prize_name is null");
            return (Criteria) this;
        }

        public Criteria andPrizeNameIsNotNull() {
            addCriterion("prize_name is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeNameEqualTo(String value) {
            addCriterion("prize_name =", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotEqualTo(String value) {
            addCriterion("prize_name <>", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameGreaterThan(String value) {
            addCriterion("prize_name >", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameGreaterThanOrEqualTo(String value) {
            addCriterion("prize_name >=", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameLessThan(String value) {
            addCriterion("prize_name <", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameLessThanOrEqualTo(String value) {
            addCriterion("prize_name <=", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameLike(String value) {
            addCriterion("prize_name like", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotLike(String value) {
            addCriterion("prize_name not like", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameIn(List<String> values) {
            addCriterion("prize_name in", values, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotIn(List<String> values) {
            addCriterion("prize_name not in", values, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameBetween(String value1, String value2) {
            addCriterion("prize_name between", value1, value2, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotBetween(String value1, String value2) {
            addCriterion("prize_name not between", value1, value2, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeIsNull() {
            addCriterion("prize_code is null");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeIsNotNull() {
            addCriterion("prize_code is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeEqualTo(String value) {
            addCriterion("prize_code =", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotEqualTo(String value) {
            addCriterion("prize_code <>", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeGreaterThan(String value) {
            addCriterion("prize_code >", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("prize_code >=", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeLessThan(String value) {
            addCriterion("prize_code <", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeLessThanOrEqualTo(String value) {
            addCriterion("prize_code <=", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeLike(String value) {
            addCriterion("prize_code like", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotLike(String value) {
            addCriterion("prize_code not like", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeIn(List<String> values) {
            addCriterion("prize_code in", values, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotIn(List<String> values) {
            addCriterion("prize_code not in", values, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeBetween(String value1, String value2) {
            addCriterion("prize_code between", value1, value2, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotBetween(String value1, String value2) {
            addCriterion("prize_code not between", value1, value2, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(BigDecimal value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(BigDecimal value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(BigDecimal value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(BigDecimal value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<BigDecimal> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<BigDecimal> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGoalIsNull() {
            addCriterion("price_goal is null");
            return (Criteria) this;
        }

        public Criteria andPriceGoalIsNotNull() {
            addCriterion("price_goal is not null");
            return (Criteria) this;
        }

        public Criteria andPriceGoalEqualTo(BigDecimal value) {
            addCriterion("price_goal =", value, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalNotEqualTo(BigDecimal value) {
            addCriterion("price_goal <>", value, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalGreaterThan(BigDecimal value) {
            addCriterion("price_goal >", value, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price_goal >=", value, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalLessThan(BigDecimal value) {
            addCriterion("price_goal <", value, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price_goal <=", value, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalIn(List<BigDecimal> values) {
            addCriterion("price_goal in", values, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalNotIn(List<BigDecimal> values) {
            addCriterion("price_goal not in", values, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_goal between", value1, value2, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andPriceGoalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_goal not between", value1, value2, "priceGoal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIsNull() {
            addCriterion("amount_total is null");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIsNotNull() {
            addCriterion("amount_total is not null");
            return (Criteria) this;
        }

        public Criteria andAmountTotalEqualTo(Integer value) {
            addCriterion("amount_total =", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotEqualTo(Integer value) {
            addCriterion("amount_total <>", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalGreaterThan(Integer value) {
            addCriterion("amount_total >", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount_total >=", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLessThan(Integer value) {
            addCriterion("amount_total <", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLessThanOrEqualTo(Integer value) {
            addCriterion("amount_total <=", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIn(List<Integer> values) {
            addCriterion("amount_total in", values, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotIn(List<Integer> values) {
            addCriterion("amount_total not in", values, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalBetween(Integer value1, Integer value2) {
            addCriterion("amount_total between", value1, value2, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("amount_total not between", value1, value2, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andRemainCountIsNull() {
            addCriterion("remain_count is null");
            return (Criteria) this;
        }

        public Criteria andRemainCountIsNotNull() {
            addCriterion("remain_count is not null");
            return (Criteria) this;
        }

        public Criteria andRemainCountEqualTo(Integer value) {
            addCriterion("remain_count =", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountNotEqualTo(Integer value) {
            addCriterion("remain_count <>", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountGreaterThan(Integer value) {
            addCriterion("remain_count >", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("remain_count >=", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountLessThan(Integer value) {
            addCriterion("remain_count <", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountLessThanOrEqualTo(Integer value) {
            addCriterion("remain_count <=", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountIn(List<Integer> values) {
            addCriterion("remain_count in", values, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountNotIn(List<Integer> values) {
            addCriterion("remain_count not in", values, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountBetween(Integer value1, Integer value2) {
            addCriterion("remain_count between", value1, value2, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountNotBetween(Integer value1, Integer value2) {
            addCriterion("remain_count not between", value1, value2, "remainCount");
            return (Criteria) this;
        }

        public Criteria andOrderWeightIsNull() {
            addCriterion("order_weight is null");
            return (Criteria) this;
        }

        public Criteria andOrderWeightIsNotNull() {
            addCriterion("order_weight is not null");
            return (Criteria) this;
        }

        public Criteria andOrderWeightEqualTo(Integer value) {
            addCriterion("order_weight =", value, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightNotEqualTo(Integer value) {
            addCriterion("order_weight <>", value, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightGreaterThan(Integer value) {
            addCriterion("order_weight >", value, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_weight >=", value, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightLessThan(Integer value) {
            addCriterion("order_weight <", value, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightLessThanOrEqualTo(Integer value) {
            addCriterion("order_weight <=", value, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightIn(List<Integer> values) {
            addCriterion("order_weight in", values, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightNotIn(List<Integer> values) {
            addCriterion("order_weight not in", values, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightBetween(Integer value1, Integer value2) {
            addCriterion("order_weight between", value1, value2, "orderWeight");
            return (Criteria) this;
        }

        public Criteria andOrderWeightNotBetween(Integer value1, Integer value2) {
            addCriterion("order_weight not between", value1, value2, "orderWeight");
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

        public Criteria andDelFlgIsNull() {
            addCriterion("del_flg is null");
            return (Criteria) this;
        }

        public Criteria andDelFlgIsNotNull() {
            addCriterion("del_flg is not null");
            return (Criteria) this;
        }

        public Criteria andDelFlgEqualTo(Integer value) {
            addCriterion("del_flg =", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgNotEqualTo(Integer value) {
            addCriterion("del_flg <>", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgGreaterThan(Integer value) {
            addCriterion("del_flg >", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("del_flg >=", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgLessThan(Integer value) {
            addCriterion("del_flg <", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgLessThanOrEqualTo(Integer value) {
            addCriterion("del_flg <=", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgIn(List<Integer> values) {
            addCriterion("del_flg in", values, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgNotIn(List<Integer> values) {
            addCriterion("del_flg not in", values, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgBetween(Integer value1, Integer value2) {
            addCriterion("del_flg between", value1, value2, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("del_flg not between", value1, value2, "delFlg");
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