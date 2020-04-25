package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PreRegistChannelExclusiveActivityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public PreRegistChannelExclusiveActivityExample() {
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

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
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

        public Criteria andReferrerIsNull() {
            addCriterion("referrer is null");
            return (Criteria) this;
        }

        public Criteria andReferrerIsNotNull() {
            addCriterion("referrer is not null");
            return (Criteria) this;
        }

        public Criteria andReferrerEqualTo(Integer value) {
            addCriterion("referrer =", value, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerNotEqualTo(Integer value) {
            addCriterion("referrer <>", value, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerGreaterThan(Integer value) {
            addCriterion("referrer >", value, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerGreaterThanOrEqualTo(Integer value) {
            addCriterion("referrer >=", value, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerLessThan(Integer value) {
            addCriterion("referrer <", value, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerLessThanOrEqualTo(Integer value) {
            addCriterion("referrer <=", value, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerIn(List<Integer> values) {
            addCriterion("referrer in", values, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerNotIn(List<Integer> values) {
            addCriterion("referrer not in", values, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerBetween(Integer value1, Integer value2) {
            addCriterion("referrer between", value1, value2, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerNotBetween(Integer value1, Integer value2) {
            addCriterion("referrer not between", value1, value2, "referrer");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameIsNull() {
            addCriterion("referrer_user_name is null");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameIsNotNull() {
            addCriterion("referrer_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameEqualTo(String value) {
            addCriterion("referrer_user_name =", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameNotEqualTo(String value) {
            addCriterion("referrer_user_name <>", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameGreaterThan(String value) {
            addCriterion("referrer_user_name >", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("referrer_user_name >=", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameLessThan(String value) {
            addCriterion("referrer_user_name <", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameLessThanOrEqualTo(String value) {
            addCriterion("referrer_user_name <=", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameLike(String value) {
            addCriterion("referrer_user_name like", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameNotLike(String value) {
            addCriterion("referrer_user_name not like", value, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameIn(List<String> values) {
            addCriterion("referrer_user_name in", values, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameNotIn(List<String> values) {
            addCriterion("referrer_user_name not in", values, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameBetween(String value1, String value2) {
            addCriterion("referrer_user_name between", value1, value2, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andReferrerUserNameNotBetween(String value1, String value2) {
            addCriterion("referrer_user_name not between", value1, value2, "referrerUserName");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeIsNull() {
            addCriterion("pre_regist_time is null");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeIsNotNull() {
            addCriterion("pre_regist_time is not null");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeEqualTo(Integer value) {
            addCriterion("pre_regist_time =", value, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeNotEqualTo(Integer value) {
            addCriterion("pre_regist_time <>", value, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeGreaterThan(Integer value) {
            addCriterion("pre_regist_time >", value, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pre_regist_time >=", value, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeLessThan(Integer value) {
            addCriterion("pre_regist_time <", value, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeLessThanOrEqualTo(Integer value) {
            addCriterion("pre_regist_time <=", value, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeIn(List<Integer> values) {
            addCriterion("pre_regist_time in", values, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeNotIn(List<Integer> values) {
            addCriterion("pre_regist_time not in", values, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeBetween(Integer value1, Integer value2) {
            addCriterion("pre_regist_time between", value1, value2, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andPreRegistTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("pre_regist_time not between", value1, value2, "preRegistTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeIsNull() {
            addCriterion("regist_time is null");
            return (Criteria) this;
        }

        public Criteria andRegistTimeIsNotNull() {
            addCriterion("regist_time is not null");
            return (Criteria) this;
        }

        public Criteria andRegistTimeEqualTo(Integer value) {
            addCriterion("regist_time =", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeNotEqualTo(Integer value) {
            addCriterion("regist_time <>", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeGreaterThan(Integer value) {
            addCriterion("regist_time >", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("regist_time >=", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeLessThan(Integer value) {
            addCriterion("regist_time <", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeLessThanOrEqualTo(Integer value) {
            addCriterion("regist_time <=", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeIn(List<Integer> values) {
            addCriterion("regist_time in", values, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeNotIn(List<Integer> values) {
            addCriterion("regist_time not in", values, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeBetween(Integer value1, Integer value2) {
            addCriterion("regist_time between", value1, value2, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("regist_time not between", value1, value2, "registTime");
            return (Criteria) this;
        }

        public Criteria andUtmIdIsNull() {
            addCriterion("utm_id is null");
            return (Criteria) this;
        }

        public Criteria andUtmIdIsNotNull() {
            addCriterion("utm_id is not null");
            return (Criteria) this;
        }

        public Criteria andUtmIdEqualTo(Integer value) {
            addCriterion("utm_id =", value, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdNotEqualTo(Integer value) {
            addCriterion("utm_id <>", value, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdGreaterThan(Integer value) {
            addCriterion("utm_id >", value, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("utm_id >=", value, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdLessThan(Integer value) {
            addCriterion("utm_id <", value, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdLessThanOrEqualTo(Integer value) {
            addCriterion("utm_id <=", value, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdIn(List<Integer> values) {
            addCriterion("utm_id in", values, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdNotIn(List<Integer> values) {
            addCriterion("utm_id not in", values, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdBetween(Integer value1, Integer value2) {
            addCriterion("utm_id between", value1, value2, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmIdNotBetween(Integer value1, Integer value2) {
            addCriterion("utm_id not between", value1, value2, "utmId");
            return (Criteria) this;
        }

        public Criteria andUtmTermIsNull() {
            addCriterion("utm_term is null");
            return (Criteria) this;
        }

        public Criteria andUtmTermIsNotNull() {
            addCriterion("utm_term is not null");
            return (Criteria) this;
        }

        public Criteria andUtmTermEqualTo(String value) {
            addCriterion("utm_term =", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermNotEqualTo(String value) {
            addCriterion("utm_term <>", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermGreaterThan(String value) {
            addCriterion("utm_term >", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermGreaterThanOrEqualTo(String value) {
            addCriterion("utm_term >=", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermLessThan(String value) {
            addCriterion("utm_term <", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermLessThanOrEqualTo(String value) {
            addCriterion("utm_term <=", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermLike(String value) {
            addCriterion("utm_term like", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermNotLike(String value) {
            addCriterion("utm_term not like", value, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermIn(List<String> values) {
            addCriterion("utm_term in", values, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermNotIn(List<String> values) {
            addCriterion("utm_term not in", values, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermBetween(String value1, String value2) {
            addCriterion("utm_term between", value1, value2, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andUtmTermNotBetween(String value1, String value2) {
            addCriterion("utm_term not between", value1, value2, "utmTerm");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNull() {
            addCriterion("source_id is null");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNotNull() {
            addCriterion("source_id is not null");
            return (Criteria) this;
        }

        public Criteria andSourceIdEqualTo(Integer value) {
            addCriterion("source_id =", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotEqualTo(Integer value) {
            addCriterion("source_id <>", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThan(Integer value) {
            addCriterion("source_id >", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("source_id >=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThan(Integer value) {
            addCriterion("source_id <", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThanOrEqualTo(Integer value) {
            addCriterion("source_id <=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdIn(List<Integer> values) {
            addCriterion("source_id in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotIn(List<Integer> values) {
            addCriterion("source_id not in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdBetween(Integer value1, Integer value2) {
            addCriterion("source_id between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotBetween(Integer value1, Integer value2) {
            addCriterion("source_id not between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andUtmSourceIsNull() {
            addCriterion("utm_source is null");
            return (Criteria) this;
        }

        public Criteria andUtmSourceIsNotNull() {
            addCriterion("utm_source is not null");
            return (Criteria) this;
        }

        public Criteria andUtmSourceEqualTo(String value) {
            addCriterion("utm_source =", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceNotEqualTo(String value) {
            addCriterion("utm_source <>", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceGreaterThan(String value) {
            addCriterion("utm_source >", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceGreaterThanOrEqualTo(String value) {
            addCriterion("utm_source >=", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceLessThan(String value) {
            addCriterion("utm_source <", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceLessThanOrEqualTo(String value) {
            addCriterion("utm_source <=", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceLike(String value) {
            addCriterion("utm_source like", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceNotLike(String value) {
            addCriterion("utm_source not like", value, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceIn(List<String> values) {
            addCriterion("utm_source in", values, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceNotIn(List<String> values) {
            addCriterion("utm_source not in", values, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceBetween(String value1, String value2) {
            addCriterion("utm_source between", value1, value2, "utmSource");
            return (Criteria) this;
        }

        public Criteria andUtmSourceNotBetween(String value1, String value2) {
            addCriterion("utm_source not between", value1, value2, "utmSource");
            return (Criteria) this;
        }

        public Criteria andTenderTotalIsNull() {
            addCriterion("tender_total is null");
            return (Criteria) this;
        }

        public Criteria andTenderTotalIsNotNull() {
            addCriterion("tender_total is not null");
            return (Criteria) this;
        }

        public Criteria andTenderTotalEqualTo(BigDecimal value) {
            addCriterion("tender_total =", value, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalNotEqualTo(BigDecimal value) {
            addCriterion("tender_total <>", value, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalGreaterThan(BigDecimal value) {
            addCriterion("tender_total >", value, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_total >=", value, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalLessThan(BigDecimal value) {
            addCriterion("tender_total <", value, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_total <=", value, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalIn(List<BigDecimal> values) {
            addCriterion("tender_total in", values, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalNotIn(List<BigDecimal> values) {
            addCriterion("tender_total not in", values, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_total between", value1, value2, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_total not between", value1, value2, "tenderTotal");
            return (Criteria) this;
        }

        public Criteria andTenderSingleIsNull() {
            addCriterion("tender_single is null");
            return (Criteria) this;
        }

        public Criteria andTenderSingleIsNotNull() {
            addCriterion("tender_single is not null");
            return (Criteria) this;
        }

        public Criteria andTenderSingleEqualTo(BigDecimal value) {
            addCriterion("tender_single =", value, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleNotEqualTo(BigDecimal value) {
            addCriterion("tender_single <>", value, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleGreaterThan(BigDecimal value) {
            addCriterion("tender_single >", value, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_single >=", value, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleLessThan(BigDecimal value) {
            addCriterion("tender_single <", value, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_single <=", value, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleIn(List<BigDecimal> values) {
            addCriterion("tender_single in", values, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleNotIn(List<BigDecimal> values) {
            addCriterion("tender_single not in", values, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_single between", value1, value2, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andTenderSingleNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_single not between", value1, value2, "tenderSingle");
            return (Criteria) this;
        }

        public Criteria andRewardIsNull() {
            addCriterion("reward is null");
            return (Criteria) this;
        }

        public Criteria andRewardIsNotNull() {
            addCriterion("reward is not null");
            return (Criteria) this;
        }

        public Criteria andRewardEqualTo(String value) {
            addCriterion("reward =", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotEqualTo(String value) {
            addCriterion("reward <>", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardGreaterThan(String value) {
            addCriterion("reward >", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardGreaterThanOrEqualTo(String value) {
            addCriterion("reward >=", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLessThan(String value) {
            addCriterion("reward <", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLessThanOrEqualTo(String value) {
            addCriterion("reward <=", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLike(String value) {
            addCriterion("reward like", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotLike(String value) {
            addCriterion("reward not like", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardIn(List<String> values) {
            addCriterion("reward in", values, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotIn(List<String> values) {
            addCriterion("reward not in", values, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardBetween(String value1, String value2) {
            addCriterion("reward between", value1, value2, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotBetween(String value1, String value2) {
            addCriterion("reward not between", value1, value2, "reward");
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