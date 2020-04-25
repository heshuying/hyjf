package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewyearPrizeConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public NewyearPrizeConfigExample() {
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

        public Criteria andPrizeQuantityIsNull() {
            addCriterion("prize_quantity is null");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityIsNotNull() {
            addCriterion("prize_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityEqualTo(Integer value) {
            addCriterion("prize_quantity =", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityNotEqualTo(Integer value) {
            addCriterion("prize_quantity <>", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityGreaterThan(Integer value) {
            addCriterion("prize_quantity >", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_quantity >=", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityLessThan(Integer value) {
            addCriterion("prize_quantity <", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("prize_quantity <=", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityIn(List<Integer> values) {
            addCriterion("prize_quantity in", values, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityNotIn(List<Integer> values) {
            addCriterion("prize_quantity not in", values, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityBetween(Integer value1, Integer value2) {
            addCriterion("prize_quantity between", value1, value2, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_quantity not between", value1, value2, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityIsNull() {
            addCriterion("prize_probability is null");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityIsNotNull() {
            addCriterion("prize_probability is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityEqualTo(BigDecimal value) {
            addCriterion("prize_probability =", value, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityNotEqualTo(BigDecimal value) {
            addCriterion("prize_probability <>", value, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityGreaterThan(BigDecimal value) {
            addCriterion("prize_probability >", value, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("prize_probability >=", value, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityLessThan(BigDecimal value) {
            addCriterion("prize_probability <", value, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityLessThanOrEqualTo(BigDecimal value) {
            addCriterion("prize_probability <=", value, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityIn(List<BigDecimal> values) {
            addCriterion("prize_probability in", values, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityNotIn(List<BigDecimal> values) {
            addCriterion("prize_probability not in", values, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("prize_probability between", value1, value2, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeProbabilityNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("prize_probability not between", value1, value2, "prizeProbability");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountIsNull() {
            addCriterion("prize_drawed_count is null");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountIsNotNull() {
            addCriterion("prize_drawed_count is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountEqualTo(Integer value) {
            addCriterion("prize_drawed_count =", value, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountNotEqualTo(Integer value) {
            addCriterion("prize_drawed_count <>", value, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountGreaterThan(Integer value) {
            addCriterion("prize_drawed_count >", value, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_drawed_count >=", value, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountLessThan(Integer value) {
            addCriterion("prize_drawed_count <", value, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountLessThanOrEqualTo(Integer value) {
            addCriterion("prize_drawed_count <=", value, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountIn(List<Integer> values) {
            addCriterion("prize_drawed_count in", values, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountNotIn(List<Integer> values) {
            addCriterion("prize_drawed_count not in", values, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountBetween(Integer value1, Integer value2) {
            addCriterion("prize_drawed_count between", value1, value2, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeDrawedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_drawed_count not between", value1, value2, "prizeDrawedCount");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineIsNull() {
            addCriterion("prize_online is null");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineIsNotNull() {
            addCriterion("prize_online is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineEqualTo(Integer value) {
            addCriterion("prize_online =", value, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineNotEqualTo(Integer value) {
            addCriterion("prize_online <>", value, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineGreaterThan(Integer value) {
            addCriterion("prize_online >", value, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_online >=", value, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineLessThan(Integer value) {
            addCriterion("prize_online <", value, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineLessThanOrEqualTo(Integer value) {
            addCriterion("prize_online <=", value, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineIn(List<Integer> values) {
            addCriterion("prize_online in", values, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineNotIn(List<Integer> values) {
            addCriterion("prize_online not in", values, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineBetween(Integer value1, Integer value2) {
            addCriterion("prize_online between", value1, value2, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeOnlineNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_online not between", value1, value2, "prizeOnline");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeIsNull() {
            addCriterion("prize_coupon_code is null");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeIsNotNull() {
            addCriterion("prize_coupon_code is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeEqualTo(String value) {
            addCriterion("prize_coupon_code =", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeNotEqualTo(String value) {
            addCriterion("prize_coupon_code <>", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeGreaterThan(String value) {
            addCriterion("prize_coupon_code >", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeGreaterThanOrEqualTo(String value) {
            addCriterion("prize_coupon_code >=", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeLessThan(String value) {
            addCriterion("prize_coupon_code <", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeLessThanOrEqualTo(String value) {
            addCriterion("prize_coupon_code <=", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeLike(String value) {
            addCriterion("prize_coupon_code like", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeNotLike(String value) {
            addCriterion("prize_coupon_code not like", value, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeIn(List<String> values) {
            addCriterion("prize_coupon_code in", values, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeNotIn(List<String> values) {
            addCriterion("prize_coupon_code not in", values, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeBetween(String value1, String value2) {
            addCriterion("prize_coupon_code between", value1, value2, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCouponCodeNotBetween(String value1, String value2) {
            addCriterion("prize_coupon_code not between", value1, value2, "prizeCouponCode");
            return (Criteria) this;
        }

        public Criteria andActivityFlgIsNull() {
            addCriterion("activity_flg is null");
            return (Criteria) this;
        }

        public Criteria andActivityFlgIsNotNull() {
            addCriterion("activity_flg is not null");
            return (Criteria) this;
        }

        public Criteria andActivityFlgEqualTo(Integer value) {
            addCriterion("activity_flg =", value, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgNotEqualTo(Integer value) {
            addCriterion("activity_flg <>", value, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgGreaterThan(Integer value) {
            addCriterion("activity_flg >", value, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("activity_flg >=", value, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgLessThan(Integer value) {
            addCriterion("activity_flg <", value, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgLessThanOrEqualTo(Integer value) {
            addCriterion("activity_flg <=", value, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgIn(List<Integer> values) {
            addCriterion("activity_flg in", values, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgNotIn(List<Integer> values) {
            addCriterion("activity_flg not in", values, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgBetween(Integer value1, Integer value2) {
            addCriterion("activity_flg between", value1, value2, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andActivityFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("activity_flg not between", value1, value2, "activityFlg");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
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