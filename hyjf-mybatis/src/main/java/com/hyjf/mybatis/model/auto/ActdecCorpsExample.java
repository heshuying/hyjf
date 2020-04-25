package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class ActdecCorpsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActdecCorpsExample() {
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

        public Criteria andCaptainOpidIsNull() {
            addCriterion("captain_opid is null");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidIsNotNull() {
            addCriterion("captain_opid is not null");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidEqualTo(String value) {
            addCriterion("captain_opid =", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidNotEqualTo(String value) {
            addCriterion("captain_opid <>", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidGreaterThan(String value) {
            addCriterion("captain_opid >", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidGreaterThanOrEqualTo(String value) {
            addCriterion("captain_opid >=", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidLessThan(String value) {
            addCriterion("captain_opid <", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidLessThanOrEqualTo(String value) {
            addCriterion("captain_opid <=", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidLike(String value) {
            addCriterion("captain_opid like", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidNotLike(String value) {
            addCriterion("captain_opid not like", value, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidIn(List<String> values) {
            addCriterion("captain_opid in", values, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidNotIn(List<String> values) {
            addCriterion("captain_opid not in", values, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidBetween(String value1, String value2) {
            addCriterion("captain_opid between", value1, value2, "captainOpid");
            return (Criteria) this;
        }

        public Criteria andCaptainOpidNotBetween(String value1, String value2) {
            addCriterion("captain_opid not between", value1, value2, "captainOpid");
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

        public Criteria andCaptainHeadIsNull() {
            addCriterion("captain_head is null");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadIsNotNull() {
            addCriterion("captain_head is not null");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadEqualTo(String value) {
            addCriterion("captain_head =", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadNotEqualTo(String value) {
            addCriterion("captain_head <>", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadGreaterThan(String value) {
            addCriterion("captain_head >", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadGreaterThanOrEqualTo(String value) {
            addCriterion("captain_head >=", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadLessThan(String value) {
            addCriterion("captain_head <", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadLessThanOrEqualTo(String value) {
            addCriterion("captain_head <=", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadLike(String value) {
            addCriterion("captain_head like", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadNotLike(String value) {
            addCriterion("captain_head not like", value, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadIn(List<String> values) {
            addCriterion("captain_head in", values, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadNotIn(List<String> values) {
            addCriterion("captain_head not in", values, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadBetween(String value1, String value2) {
            addCriterion("captain_head between", value1, value2, "captainHead");
            return (Criteria) this;
        }

        public Criteria andCaptainHeadNotBetween(String value1, String value2) {
            addCriterion("captain_head not between", value1, value2, "captainHead");
            return (Criteria) this;
        }

        public Criteria andMember1OpidIsNull() {
            addCriterion("member1_opid is null");
            return (Criteria) this;
        }

        public Criteria andMember1OpidIsNotNull() {
            addCriterion("member1_opid is not null");
            return (Criteria) this;
        }

        public Criteria andMember1OpidEqualTo(String value) {
            addCriterion("member1_opid =", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidNotEqualTo(String value) {
            addCriterion("member1_opid <>", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidGreaterThan(String value) {
            addCriterion("member1_opid >", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidGreaterThanOrEqualTo(String value) {
            addCriterion("member1_opid >=", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidLessThan(String value) {
            addCriterion("member1_opid <", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidLessThanOrEqualTo(String value) {
            addCriterion("member1_opid <=", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidLike(String value) {
            addCriterion("member1_opid like", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidNotLike(String value) {
            addCriterion("member1_opid not like", value, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidIn(List<String> values) {
            addCriterion("member1_opid in", values, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidNotIn(List<String> values) {
            addCriterion("member1_opid not in", values, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidBetween(String value1, String value2) {
            addCriterion("member1_opid between", value1, value2, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1OpidNotBetween(String value1, String value2) {
            addCriterion("member1_opid not between", value1, value2, "member1Opid");
            return (Criteria) this;
        }

        public Criteria andMember1NameIsNull() {
            addCriterion("member1_name is null");
            return (Criteria) this;
        }

        public Criteria andMember1NameIsNotNull() {
            addCriterion("member1_name is not null");
            return (Criteria) this;
        }

        public Criteria andMember1NameEqualTo(String value) {
            addCriterion("member1_name =", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameNotEqualTo(String value) {
            addCriterion("member1_name <>", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameGreaterThan(String value) {
            addCriterion("member1_name >", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameGreaterThanOrEqualTo(String value) {
            addCriterion("member1_name >=", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameLessThan(String value) {
            addCriterion("member1_name <", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameLessThanOrEqualTo(String value) {
            addCriterion("member1_name <=", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameLike(String value) {
            addCriterion("member1_name like", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameNotLike(String value) {
            addCriterion("member1_name not like", value, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameIn(List<String> values) {
            addCriterion("member1_name in", values, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameNotIn(List<String> values) {
            addCriterion("member1_name not in", values, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameBetween(String value1, String value2) {
            addCriterion("member1_name between", value1, value2, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1NameNotBetween(String value1, String value2) {
            addCriterion("member1_name not between", value1, value2, "member1Name");
            return (Criteria) this;
        }

        public Criteria andMember1HeadIsNull() {
            addCriterion("member1_head is null");
            return (Criteria) this;
        }

        public Criteria andMember1HeadIsNotNull() {
            addCriterion("member1_head is not null");
            return (Criteria) this;
        }

        public Criteria andMember1HeadEqualTo(String value) {
            addCriterion("member1_head =", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadNotEqualTo(String value) {
            addCriterion("member1_head <>", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadGreaterThan(String value) {
            addCriterion("member1_head >", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadGreaterThanOrEqualTo(String value) {
            addCriterion("member1_head >=", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadLessThan(String value) {
            addCriterion("member1_head <", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadLessThanOrEqualTo(String value) {
            addCriterion("member1_head <=", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadLike(String value) {
            addCriterion("member1_head like", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadNotLike(String value) {
            addCriterion("member1_head not like", value, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadIn(List<String> values) {
            addCriterion("member1_head in", values, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadNotIn(List<String> values) {
            addCriterion("member1_head not in", values, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadBetween(String value1, String value2) {
            addCriterion("member1_head between", value1, value2, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember1HeadNotBetween(String value1, String value2) {
            addCriterion("member1_head not between", value1, value2, "member1Head");
            return (Criteria) this;
        }

        public Criteria andMember2OpidIsNull() {
            addCriterion("member2_opid is null");
            return (Criteria) this;
        }

        public Criteria andMember2OpidIsNotNull() {
            addCriterion("member2_opid is not null");
            return (Criteria) this;
        }

        public Criteria andMember2OpidEqualTo(String value) {
            addCriterion("member2_opid =", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidNotEqualTo(String value) {
            addCriterion("member2_opid <>", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidGreaterThan(String value) {
            addCriterion("member2_opid >", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidGreaterThanOrEqualTo(String value) {
            addCriterion("member2_opid >=", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidLessThan(String value) {
            addCriterion("member2_opid <", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidLessThanOrEqualTo(String value) {
            addCriterion("member2_opid <=", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidLike(String value) {
            addCriterion("member2_opid like", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidNotLike(String value) {
            addCriterion("member2_opid not like", value, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidIn(List<String> values) {
            addCriterion("member2_opid in", values, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidNotIn(List<String> values) {
            addCriterion("member2_opid not in", values, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidBetween(String value1, String value2) {
            addCriterion("member2_opid between", value1, value2, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2OpidNotBetween(String value1, String value2) {
            addCriterion("member2_opid not between", value1, value2, "member2Opid");
            return (Criteria) this;
        }

        public Criteria andMember2NameIsNull() {
            addCriterion("member2_name is null");
            return (Criteria) this;
        }

        public Criteria andMember2NameIsNotNull() {
            addCriterion("member2_name is not null");
            return (Criteria) this;
        }

        public Criteria andMember2NameEqualTo(String value) {
            addCriterion("member2_name =", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameNotEqualTo(String value) {
            addCriterion("member2_name <>", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameGreaterThan(String value) {
            addCriterion("member2_name >", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameGreaterThanOrEqualTo(String value) {
            addCriterion("member2_name >=", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameLessThan(String value) {
            addCriterion("member2_name <", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameLessThanOrEqualTo(String value) {
            addCriterion("member2_name <=", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameLike(String value) {
            addCriterion("member2_name like", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameNotLike(String value) {
            addCriterion("member2_name not like", value, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameIn(List<String> values) {
            addCriterion("member2_name in", values, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameNotIn(List<String> values) {
            addCriterion("member2_name not in", values, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameBetween(String value1, String value2) {
            addCriterion("member2_name between", value1, value2, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2NameNotBetween(String value1, String value2) {
            addCriterion("member2_name not between", value1, value2, "member2Name");
            return (Criteria) this;
        }

        public Criteria andMember2HeadIsNull() {
            addCriterion("member2_head is null");
            return (Criteria) this;
        }

        public Criteria andMember2HeadIsNotNull() {
            addCriterion("member2_head is not null");
            return (Criteria) this;
        }

        public Criteria andMember2HeadEqualTo(String value) {
            addCriterion("member2_head =", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadNotEqualTo(String value) {
            addCriterion("member2_head <>", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadGreaterThan(String value) {
            addCriterion("member2_head >", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadGreaterThanOrEqualTo(String value) {
            addCriterion("member2_head >=", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadLessThan(String value) {
            addCriterion("member2_head <", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadLessThanOrEqualTo(String value) {
            addCriterion("member2_head <=", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadLike(String value) {
            addCriterion("member2_head like", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadNotLike(String value) {
            addCriterion("member2_head not like", value, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadIn(List<String> values) {
            addCriterion("member2_head in", values, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadNotIn(List<String> values) {
            addCriterion("member2_head not in", values, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadBetween(String value1, String value2) {
            addCriterion("member2_head between", value1, value2, "member2Head");
            return (Criteria) this;
        }

        public Criteria andMember2HeadNotBetween(String value1, String value2) {
            addCriterion("member2_head not between", value1, value2, "member2Head");
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

        public Criteria andCorpsNameEqualTo(Byte value) {
            addCriterion("corps_name =", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotEqualTo(Byte value) {
            addCriterion("corps_name <>", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameGreaterThan(Byte value) {
            addCriterion("corps_name >", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameGreaterThanOrEqualTo(Byte value) {
            addCriterion("corps_name >=", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameLessThan(Byte value) {
            addCriterion("corps_name <", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameLessThanOrEqualTo(Byte value) {
            addCriterion("corps_name <=", value, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameIn(List<Byte> values) {
            addCriterion("corps_name in", values, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotIn(List<Byte> values) {
            addCriterion("corps_name not in", values, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameBetween(Byte value1, Byte value2) {
            addCriterion("corps_name between", value1, value2, "corpsName");
            return (Criteria) this;
        }

        public Criteria andCorpsNameNotBetween(Byte value1, Byte value2) {
            addCriterion("corps_name not between", value1, value2, "corpsName");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeIsNull() {
            addCriterion("prize_type is null");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeIsNotNull() {
            addCriterion("prize_type is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeEqualTo(Integer value) {
            addCriterion("prize_type =", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeNotEqualTo(Integer value) {
            addCriterion("prize_type <>", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeGreaterThan(Integer value) {
            addCriterion("prize_type >", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_type >=", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeLessThan(Integer value) {
            addCriterion("prize_type <", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeLessThanOrEqualTo(Integer value) {
            addCriterion("prize_type <=", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeIn(List<Integer> values) {
            addCriterion("prize_type in", values, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeNotIn(List<Integer> values) {
            addCriterion("prize_type not in", values, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeBetween(Integer value1, Integer value2) {
            addCriterion("prize_type between", value1, value2, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_type not between", value1, value2, "prizeType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeIsNull() {
            addCriterion("team_type is null");
            return (Criteria) this;
        }

        public Criteria andTeamTypeIsNotNull() {
            addCriterion("team_type is not null");
            return (Criteria) this;
        }

        public Criteria andTeamTypeEqualTo(Integer value) {
            addCriterion("team_type =", value, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeNotEqualTo(Integer value) {
            addCriterion("team_type <>", value, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeGreaterThan(Integer value) {
            addCriterion("team_type >", value, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("team_type >=", value, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeLessThan(Integer value) {
            addCriterion("team_type <", value, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeLessThanOrEqualTo(Integer value) {
            addCriterion("team_type <=", value, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeIn(List<Integer> values) {
            addCriterion("team_type in", values, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeNotIn(List<Integer> values) {
            addCriterion("team_type not in", values, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeBetween(Integer value1, Integer value2) {
            addCriterion("team_type between", value1, value2, "teamType");
            return (Criteria) this;
        }

        public Criteria andTeamTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("team_type not between", value1, value2, "teamType");
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