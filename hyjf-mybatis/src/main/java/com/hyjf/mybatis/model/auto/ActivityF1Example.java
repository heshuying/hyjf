package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ActivityF1Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActivityF1Example() {
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

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNull() {
            addCriterion("real_name is null");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNotNull() {
            addCriterion("real_name is not null");
            return (Criteria) this;
        }

        public Criteria andRealNameEqualTo(String value) {
            addCriterion("real_name =", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotEqualTo(String value) {
            addCriterion("real_name <>", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThan(String value) {
            addCriterion("real_name >", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThanOrEqualTo(String value) {
            addCriterion("real_name >=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThan(String value) {
            addCriterion("real_name <", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThanOrEqualTo(String value) {
            addCriterion("real_name <=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLike(String value) {
            addCriterion("real_name like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotLike(String value) {
            addCriterion("real_name not like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameIn(List<String> values) {
            addCriterion("real_name in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotIn(List<String> values) {
            addCriterion("real_name not in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameBetween(String value1, String value2) {
            addCriterion("real_name between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotBetween(String value1, String value2) {
            addCriterion("real_name not between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andActivityTypeIsNull() {
            addCriterion("activity_type is null");
            return (Criteria) this;
        }

        public Criteria andActivityTypeIsNotNull() {
            addCriterion("activity_type is not null");
            return (Criteria) this;
        }

        public Criteria andActivityTypeEqualTo(String value) {
            addCriterion("activity_type =", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeNotEqualTo(String value) {
            addCriterion("activity_type <>", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeGreaterThan(String value) {
            addCriterion("activity_type >", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeGreaterThanOrEqualTo(String value) {
            addCriterion("activity_type >=", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeLessThan(String value) {
            addCriterion("activity_type <", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeLessThanOrEqualTo(String value) {
            addCriterion("activity_type <=", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeLike(String value) {
            addCriterion("activity_type like", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeNotLike(String value) {
            addCriterion("activity_type not like", value, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeIn(List<String> values) {
            addCriterion("activity_type in", values, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeNotIn(List<String> values) {
            addCriterion("activity_type not in", values, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeBetween(String value1, String value2) {
            addCriterion("activity_type between", value1, value2, "activityType");
            return (Criteria) this;
        }

        public Criteria andActivityTypeNotBetween(String value1, String value2) {
            addCriterion("activity_type not between", value1, value2, "activityType");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNull() {
            addCriterion("speed is null");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNotNull() {
            addCriterion("speed is not null");
            return (Criteria) this;
        }

        public Criteria andSpeedEqualTo(Integer value) {
            addCriterion("speed =", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotEqualTo(Integer value) {
            addCriterion("speed <>", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThan(Integer value) {
            addCriterion("speed >", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThanOrEqualTo(Integer value) {
            addCriterion("speed >=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThan(Integer value) {
            addCriterion("speed <", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThanOrEqualTo(Integer value) {
            addCriterion("speed <=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedIn(List<Integer> values) {
            addCriterion("speed in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotIn(List<Integer> values) {
            addCriterion("speed not in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedBetween(Integer value1, Integer value2) {
            addCriterion("speed between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotBetween(Integer value1, Integer value2) {
            addCriterion("speed not between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllIsNull() {
            addCriterion("tender_account_all is null");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllIsNotNull() {
            addCriterion("tender_account_all is not null");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllEqualTo(BigDecimal value) {
            addCriterion("tender_account_all =", value, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllNotEqualTo(BigDecimal value) {
            addCriterion("tender_account_all <>", value, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllGreaterThan(BigDecimal value) {
            addCriterion("tender_account_all >", value, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_account_all >=", value, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllLessThan(BigDecimal value) {
            addCriterion("tender_account_all <", value, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_account_all <=", value, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllIn(List<BigDecimal> values) {
            addCriterion("tender_account_all in", values, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllNotIn(List<BigDecimal> values) {
            addCriterion("tender_account_all not in", values, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_account_all between", value1, value2, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andTenderAccountAllNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_account_all not between", value1, value2, "tenderAccountAll");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgIsNull() {
            addCriterion("is_app_flg is null");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgIsNotNull() {
            addCriterion("is_app_flg is not null");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgEqualTo(String value) {
            addCriterion("is_app_flg =", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgNotEqualTo(String value) {
            addCriterion("is_app_flg <>", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgGreaterThan(String value) {
            addCriterion("is_app_flg >", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgGreaterThanOrEqualTo(String value) {
            addCriterion("is_app_flg >=", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgLessThan(String value) {
            addCriterion("is_app_flg <", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgLessThanOrEqualTo(String value) {
            addCriterion("is_app_flg <=", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgLike(String value) {
            addCriterion("is_app_flg like", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgNotLike(String value) {
            addCriterion("is_app_flg not like", value, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgIn(List<String> values) {
            addCriterion("is_app_flg in", values, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgNotIn(List<String> values) {
            addCriterion("is_app_flg not in", values, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgBetween(String value1, String value2) {
            addCriterion("is_app_flg between", value1, value2, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsAppFlgNotBetween(String value1, String value2) {
            addCriterion("is_app_flg not between", value1, value2, "isAppFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgIsNull() {
            addCriterion("is_first_flg is null");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgIsNotNull() {
            addCriterion("is_first_flg is not null");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgEqualTo(String value) {
            addCriterion("is_first_flg =", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgNotEqualTo(String value) {
            addCriterion("is_first_flg <>", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgGreaterThan(String value) {
            addCriterion("is_first_flg >", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgGreaterThanOrEqualTo(String value) {
            addCriterion("is_first_flg >=", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgLessThan(String value) {
            addCriterion("is_first_flg <", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgLessThanOrEqualTo(String value) {
            addCriterion("is_first_flg <=", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgLike(String value) {
            addCriterion("is_first_flg like", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgNotLike(String value) {
            addCriterion("is_first_flg not like", value, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgIn(List<String> values) {
            addCriterion("is_first_flg in", values, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgNotIn(List<String> values) {
            addCriterion("is_first_flg not in", values, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgBetween(String value1, String value2) {
            addCriterion("is_first_flg between", value1, value2, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andIsFirstFlgNotBetween(String value1, String value2) {
            addCriterion("is_first_flg not between", value1, value2, "isFirstFlg");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityIsNull() {
            addCriterion("return_amount_activity is null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityIsNotNull() {
            addCriterion("return_amount_activity is not null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityEqualTo(BigDecimal value) {
            addCriterion("return_amount_activity =", value, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityNotEqualTo(BigDecimal value) {
            addCriterion("return_amount_activity <>", value, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityGreaterThan(BigDecimal value) {
            addCriterion("return_amount_activity >", value, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount_activity >=", value, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityLessThan(BigDecimal value) {
            addCriterion("return_amount_activity <", value, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityLessThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount_activity <=", value, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityIn(List<BigDecimal> values) {
            addCriterion("return_amount_activity in", values, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityNotIn(List<BigDecimal> values) {
            addCriterion("return_amount_activity not in", values, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount_activity between", value1, value2, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountActivityNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount_activity not between", value1, value2, "returnAmountActivity");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraIsNull() {
            addCriterion("return_amount_extra is null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraIsNotNull() {
            addCriterion("return_amount_extra is not null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraEqualTo(BigDecimal value) {
            addCriterion("return_amount_extra =", value, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraNotEqualTo(BigDecimal value) {
            addCriterion("return_amount_extra <>", value, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraGreaterThan(BigDecimal value) {
            addCriterion("return_amount_extra >", value, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount_extra >=", value, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraLessThan(BigDecimal value) {
            addCriterion("return_amount_extra <", value, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraLessThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount_extra <=", value, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraIn(List<BigDecimal> values) {
            addCriterion("return_amount_extra in", values, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraNotIn(List<BigDecimal> values) {
            addCriterion("return_amount_extra not in", values, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount_extra between", value1, value2, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountExtraNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount_extra not between", value1, value2, "returnAmountExtra");
            return (Criteria) this;
        }

        public Criteria andReturnAmountIsNull() {
            addCriterion("return_amount is null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountIsNotNull() {
            addCriterion("return_amount is not null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountEqualTo(BigDecimal value) {
            addCriterion("return_amount =", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountNotEqualTo(BigDecimal value) {
            addCriterion("return_amount <>", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountGreaterThan(BigDecimal value) {
            addCriterion("return_amount >", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount >=", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountLessThan(BigDecimal value) {
            addCriterion("return_amount <", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount <=", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountIn(List<BigDecimal> values) {
            addCriterion("return_amount in", values, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountNotIn(List<BigDecimal> values) {
            addCriterion("return_amount not in", values, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount between", value1, value2, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount not between", value1, value2, "returnAmount");
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

        public Criteria andIsReturnFlgIsNull() {
            addCriterion("is_return_flg is null");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgIsNotNull() {
            addCriterion("is_return_flg is not null");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgEqualTo(String value) {
            addCriterion("is_return_flg =", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgNotEqualTo(String value) {
            addCriterion("is_return_flg <>", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgGreaterThan(String value) {
            addCriterion("is_return_flg >", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgGreaterThanOrEqualTo(String value) {
            addCriterion("is_return_flg >=", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgLessThan(String value) {
            addCriterion("is_return_flg <", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgLessThanOrEqualTo(String value) {
            addCriterion("is_return_flg <=", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgLike(String value) {
            addCriterion("is_return_flg like", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgNotLike(String value) {
            addCriterion("is_return_flg not like", value, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgIn(List<String> values) {
            addCriterion("is_return_flg in", values, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgNotIn(List<String> values) {
            addCriterion("is_return_flg not in", values, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgBetween(String value1, String value2) {
            addCriterion("is_return_flg between", value1, value2, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andIsReturnFlgNotBetween(String value1, String value2) {
            addCriterion("is_return_flg not between", value1, value2, "isReturnFlg");
            return (Criteria) this;
        }

        public Criteria andReturnTimeIsNull() {
            addCriterion("return_time is null");
            return (Criteria) this;
        }

        public Criteria andReturnTimeIsNotNull() {
            addCriterion("return_time is not null");
            return (Criteria) this;
        }

        public Criteria andReturnTimeEqualTo(Integer value) {
            addCriterion("return_time =", value, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeNotEqualTo(Integer value) {
            addCriterion("return_time <>", value, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeGreaterThan(Integer value) {
            addCriterion("return_time >", value, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("return_time >=", value, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeLessThan(Integer value) {
            addCriterion("return_time <", value, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeLessThanOrEqualTo(Integer value) {
            addCriterion("return_time <=", value, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeIn(List<Integer> values) {
            addCriterion("return_time in", values, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeNotIn(List<Integer> values) {
            addCriterion("return_time not in", values, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeBetween(Integer value1, Integer value2) {
            addCriterion("return_time between", value1, value2, "returnTime");
            return (Criteria) this;
        }

        public Criteria andReturnTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("return_time not between", value1, value2, "returnTime");
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