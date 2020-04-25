package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class ActdecWinningExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActdecWinningExample() {
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

        public Criteria andCorpsIdIsNull() {
            addCriterion("corps_id is null");
            return (Criteria) this;
        }

        public Criteria andCorpsIdIsNotNull() {
            addCriterion("corps_id is not null");
            return (Criteria) this;
        }

        public Criteria andCorpsIdEqualTo(Integer value) {
            addCriterion("corps_id =", value, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdNotEqualTo(Integer value) {
            addCriterion("corps_id <>", value, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdGreaterThan(Integer value) {
            addCriterion("corps_id >", value, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("corps_id >=", value, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdLessThan(Integer value) {
            addCriterion("corps_id <", value, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdLessThanOrEqualTo(Integer value) {
            addCriterion("corps_id <=", value, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdIn(List<Integer> values) {
            addCriterion("corps_id in", values, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdNotIn(List<Integer> values) {
            addCriterion("corps_id not in", values, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdBetween(Integer value1, Integer value2) {
            addCriterion("corps_id between", value1, value2, "corpsId");
            return (Criteria) this;
        }

        public Criteria andCorpsIdNotBetween(Integer value1, Integer value2) {
            addCriterion("corps_id not between", value1, value2, "corpsId");
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

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
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

        public Criteria andWinningOpidIsNull() {
            addCriterion("winning_opid is null");
            return (Criteria) this;
        }

        public Criteria andWinningOpidIsNotNull() {
            addCriterion("winning_opid is not null");
            return (Criteria) this;
        }

        public Criteria andWinningOpidEqualTo(String value) {
            addCriterion("winning_opid =", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidNotEqualTo(String value) {
            addCriterion("winning_opid <>", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidGreaterThan(String value) {
            addCriterion("winning_opid >", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidGreaterThanOrEqualTo(String value) {
            addCriterion("winning_opid >=", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidLessThan(String value) {
            addCriterion("winning_opid <", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidLessThanOrEqualTo(String value) {
            addCriterion("winning_opid <=", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidLike(String value) {
            addCriterion("winning_opid like", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidNotLike(String value) {
            addCriterion("winning_opid not like", value, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidIn(List<String> values) {
            addCriterion("winning_opid in", values, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidNotIn(List<String> values) {
            addCriterion("winning_opid not in", values, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidBetween(String value1, String value2) {
            addCriterion("winning_opid between", value1, value2, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningOpidNotBetween(String value1, String value2) {
            addCriterion("winning_opid not between", value1, value2, "winningOpid");
            return (Criteria) this;
        }

        public Criteria andWinningHeadIsNull() {
            addCriterion("winning_head is null");
            return (Criteria) this;
        }

        public Criteria andWinningHeadIsNotNull() {
            addCriterion("winning_head is not null");
            return (Criteria) this;
        }

        public Criteria andWinningHeadEqualTo(String value) {
            addCriterion("winning_head =", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadNotEqualTo(String value) {
            addCriterion("winning_head <>", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadGreaterThan(String value) {
            addCriterion("winning_head >", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadGreaterThanOrEqualTo(String value) {
            addCriterion("winning_head >=", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadLessThan(String value) {
            addCriterion("winning_head <", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadLessThanOrEqualTo(String value) {
            addCriterion("winning_head <=", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadLike(String value) {
            addCriterion("winning_head like", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadNotLike(String value) {
            addCriterion("winning_head not like", value, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadIn(List<String> values) {
            addCriterion("winning_head in", values, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadNotIn(List<String> values) {
            addCriterion("winning_head not in", values, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadBetween(String value1, String value2) {
            addCriterion("winning_head between", value1, value2, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningHeadNotBetween(String value1, String value2) {
            addCriterion("winning_head not between", value1, value2, "winningHead");
            return (Criteria) this;
        }

        public Criteria andWinningNameIsNull() {
            addCriterion("winning_name is null");
            return (Criteria) this;
        }

        public Criteria andWinningNameIsNotNull() {
            addCriterion("winning_name is not null");
            return (Criteria) this;
        }

        public Criteria andWinningNameEqualTo(String value) {
            addCriterion("winning_name =", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameNotEqualTo(String value) {
            addCriterion("winning_name <>", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameGreaterThan(String value) {
            addCriterion("winning_name >", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameGreaterThanOrEqualTo(String value) {
            addCriterion("winning_name >=", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameLessThan(String value) {
            addCriterion("winning_name <", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameLessThanOrEqualTo(String value) {
            addCriterion("winning_name <=", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameLike(String value) {
            addCriterion("winning_name like", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameNotLike(String value) {
            addCriterion("winning_name not like", value, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameIn(List<String> values) {
            addCriterion("winning_name in", values, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameNotIn(List<String> values) {
            addCriterion("winning_name not in", values, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameBetween(String value1, String value2) {
            addCriterion("winning_name between", value1, value2, "winningName");
            return (Criteria) this;
        }

        public Criteria andWinningNameNotBetween(String value1, String value2) {
            addCriterion("winning_name not between", value1, value2, "winningName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameIsNull() {
            addCriterion("corps_name is null");
            return (Criteria) this;
        }

        public Criteria andCorpsNameIsNotNull() {
            addCriterion("corps_name is not null");
            return (Criteria) this;
        }

        public Criteria andCorpsNameEqualTo(String value) {
            addCriterion("corps_name =", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotEqualTo(String value) {
            addCriterion("corps_name <>", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameGreaterThan(String value) {
            addCriterion("corps_name >", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameGreaterThanOrEqualTo(String value) {
            addCriterion("corps_name >=", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameLessThan(String value) {
            addCriterion("corps_name <", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameLessThanOrEqualTo(String value) {
            addCriterion("corps_name <=", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameLike(String value) {
            addCriterion("corps_name like", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotLike(String value) {
            addCriterion("corps_name not like", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameIn(List<String> values) {
            addCriterion("corps_name in", values, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotIn(List<String> values) {
            addCriterion("corps_name not in", values, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameBetween(String value1, String value2) {
            addCriterion("corps_name between", value1, value2, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotBetween(String value1, String value2) {
            addCriterion("corps_name not between", value1, value2, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameIsNull() {
            addCriterion("captain_name is null");
            return (Criteria) this;
        }

        public Criteria andCaptainNameIsNotNull() {
            addCriterion("captain_name is not null");
            return (Criteria) this;
        }

        public Criteria andCaptainNameEqualTo(String value) {
            addCriterion("captain_name =", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameNotEqualTo(String value) {
            addCriterion("captain_name <>", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameGreaterThan(String value) {
            addCriterion("captain_name >", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameGreaterThanOrEqualTo(String value) {
            addCriterion("captain_name >=", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameLessThan(String value) {
            addCriterion("captain_name <", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameLessThanOrEqualTo(String value) {
            addCriterion("captain_name <=", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameLike(String value) {
            addCriterion("captain_name like", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameNotLike(String value) {
            addCriterion("captain_name not like", value, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameIn(List<String> values) {
            addCriterion("captain_name in", values, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameNotIn(List<String> values) {
            addCriterion("captain_name not in", values, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameBetween(String value1, String value2) {
            addCriterion("captain_name between", value1, value2, "captainName");
            return (Criteria) this;
        }

        public Criteria andCaptainNameNotBetween(String value1, String value2) {
            addCriterion("captain_name not between", value1, value2, "captainName");
            return (Criteria) this;
        }

        public Criteria andWinningTypeIsNull() {
            addCriterion("winning_type is null");
            return (Criteria) this;
        }

        public Criteria andWinningTypeIsNotNull() {
            addCriterion("winning_type is not null");
            return (Criteria) this;
        }

        public Criteria andWinningTypeEqualTo(Integer value) {
            addCriterion("winning_type =", value, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeNotEqualTo(Integer value) {
            addCriterion("winning_type <>", value, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeGreaterThan(Integer value) {
            addCriterion("winning_type >", value, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("winning_type >=", value, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeLessThan(Integer value) {
            addCriterion("winning_type <", value, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeLessThanOrEqualTo(Integer value) {
            addCriterion("winning_type <=", value, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeIn(List<Integer> values) {
            addCriterion("winning_type in", values, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeNotIn(List<Integer> values) {
            addCriterion("winning_type not in", values, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeBetween(Integer value1, Integer value2) {
            addCriterion("winning_type between", value1, value2, "winningType");
            return (Criteria) this;
        }

        public Criteria andWinningTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("winning_type not between", value1, value2, "winningType");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Integer value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Integer value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Integer value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Integer value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Integer value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Integer> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Integer> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Integer value1, Integer value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("`type` is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("`type` is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("`type` not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("create_user like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("create_user not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
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

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(String value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(String value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(String value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(String value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLike(String value) {
            addCriterion("update_user like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotLike(String value) {
            addCriterion("update_user not like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<String> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<String> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(String value1, String value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(String value1, String value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
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

        public Criteria andDelFlgEqualTo(Boolean value) {
            addCriterion("del_flg =", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgNotEqualTo(Boolean value) {
            addCriterion("del_flg <>", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgGreaterThan(Boolean value) {
            addCriterion("del_flg >", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgGreaterThanOrEqualTo(Boolean value) {
            addCriterion("del_flg >=", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgLessThan(Boolean value) {
            addCriterion("del_flg <", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgLessThanOrEqualTo(Boolean value) {
            addCriterion("del_flg <=", value, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgIn(List<Boolean> values) {
            addCriterion("del_flg in", values, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgNotIn(List<Boolean> values) {
            addCriterion("del_flg not in", values, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgBetween(Boolean value1, Boolean value2) {
            addCriterion("del_flg between", value1, value2, "delFlg");
            return (Criteria) this;
        }

        public Criteria andDelFlgNotBetween(Boolean value1, Boolean value2) {
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