package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public AccountLogExample() {
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

        public Criteria andNidIsNull() {
            addCriterion("nid is null");
            return (Criteria) this;
        }

        public Criteria andNidIsNotNull() {
            addCriterion("nid is not null");
            return (Criteria) this;
        }

        public Criteria andNidEqualTo(String value) {
            addCriterion("nid =", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidNotEqualTo(String value) {
            addCriterion("nid <>", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidGreaterThan(String value) {
            addCriterion("nid >", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidGreaterThanOrEqualTo(String value) {
            addCriterion("nid >=", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidLessThan(String value) {
            addCriterion("nid <", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidLessThanOrEqualTo(String value) {
            addCriterion("nid <=", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidLike(String value) {
            addCriterion("nid like", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidNotLike(String value) {
            addCriterion("nid not like", value, "nid");
            return (Criteria) this;
        }

        public Criteria andNidIn(List<String> values) {
            addCriterion("nid in", values, "nid");
            return (Criteria) this;
        }

        public Criteria andNidNotIn(List<String> values) {
            addCriterion("nid not in", values, "nid");
            return (Criteria) this;
        }

        public Criteria andNidBetween(String value1, String value2) {
            addCriterion("nid between", value1, value2, "nid");
            return (Criteria) this;
        }

        public Criteria andNidNotBetween(String value1, String value2) {
            addCriterion("nid not between", value1, value2, "nid");
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

        public Criteria andTypeIsNull() {
            addCriterion("`type` is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("`type` is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("`type` like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("`type` not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("`type` not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(BigDecimal value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(BigDecimal value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(BigDecimal value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(BigDecimal value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<BigDecimal> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<BigDecimal> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalOldIsNull() {
            addCriterion("total_old is null");
            return (Criteria) this;
        }

        public Criteria andTotalOldIsNotNull() {
            addCriterion("total_old is not null");
            return (Criteria) this;
        }

        public Criteria andTotalOldEqualTo(BigDecimal value) {
            addCriterion("total_old =", value, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldNotEqualTo(BigDecimal value) {
            addCriterion("total_old <>", value, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldGreaterThan(BigDecimal value) {
            addCriterion("total_old >", value, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_old >=", value, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldLessThan(BigDecimal value) {
            addCriterion("total_old <", value, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_old <=", value, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldIn(List<BigDecimal> values) {
            addCriterion("total_old in", values, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldNotIn(List<BigDecimal> values) {
            addCriterion("total_old not in", values, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_old between", value1, value2, "totalOld");
            return (Criteria) this;
        }

        public Criteria andTotalOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_old not between", value1, value2, "totalOld");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeTypeIsNull() {
            addCriterion("code_type is null");
            return (Criteria) this;
        }

        public Criteria andCodeTypeIsNotNull() {
            addCriterion("code_type is not null");
            return (Criteria) this;
        }

        public Criteria andCodeTypeEqualTo(String value) {
            addCriterion("code_type =", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeNotEqualTo(String value) {
            addCriterion("code_type <>", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeGreaterThan(String value) {
            addCriterion("code_type >", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeGreaterThanOrEqualTo(String value) {
            addCriterion("code_type >=", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeLessThan(String value) {
            addCriterion("code_type <", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeLessThanOrEqualTo(String value) {
            addCriterion("code_type <=", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeLike(String value) {
            addCriterion("code_type like", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeNotLike(String value) {
            addCriterion("code_type not like", value, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeIn(List<String> values) {
            addCriterion("code_type in", values, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeNotIn(List<String> values) {
            addCriterion("code_type not in", values, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeBetween(String value1, String value2) {
            addCriterion("code_type between", value1, value2, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeTypeNotBetween(String value1, String value2) {
            addCriterion("code_type not between", value1, value2, "codeType");
            return (Criteria) this;
        }

        public Criteria andCodeNidIsNull() {
            addCriterion("code_nid is null");
            return (Criteria) this;
        }

        public Criteria andCodeNidIsNotNull() {
            addCriterion("code_nid is not null");
            return (Criteria) this;
        }

        public Criteria andCodeNidEqualTo(String value) {
            addCriterion("code_nid =", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidNotEqualTo(String value) {
            addCriterion("code_nid <>", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidGreaterThan(String value) {
            addCriterion("code_nid >", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidGreaterThanOrEqualTo(String value) {
            addCriterion("code_nid >=", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidLessThan(String value) {
            addCriterion("code_nid <", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidLessThanOrEqualTo(String value) {
            addCriterion("code_nid <=", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidLike(String value) {
            addCriterion("code_nid like", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidNotLike(String value) {
            addCriterion("code_nid not like", value, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidIn(List<String> values) {
            addCriterion("code_nid in", values, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidNotIn(List<String> values) {
            addCriterion("code_nid not in", values, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidBetween(String value1, String value2) {
            addCriterion("code_nid between", value1, value2, "codeNid");
            return (Criteria) this;
        }

        public Criteria andCodeNidNotBetween(String value1, String value2) {
            addCriterion("code_nid not between", value1, value2, "codeNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidIsNull() {
            addCriterion("borrow_nid is null");
            return (Criteria) this;
        }

        public Criteria andBorrowNidIsNotNull() {
            addCriterion("borrow_nid is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowNidEqualTo(String value) {
            addCriterion("borrow_nid =", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidNotEqualTo(String value) {
            addCriterion("borrow_nid <>", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidGreaterThan(String value) {
            addCriterion("borrow_nid >", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidGreaterThanOrEqualTo(String value) {
            addCriterion("borrow_nid >=", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidLessThan(String value) {
            addCriterion("borrow_nid <", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidLessThanOrEqualTo(String value) {
            addCriterion("borrow_nid <=", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidLike(String value) {
            addCriterion("borrow_nid like", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidNotLike(String value) {
            addCriterion("borrow_nid not like", value, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidIn(List<String> values) {
            addCriterion("borrow_nid in", values, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidNotIn(List<String> values) {
            addCriterion("borrow_nid not in", values, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidBetween(String value1, String value2) {
            addCriterion("borrow_nid between", value1, value2, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andBorrowNidNotBetween(String value1, String value2) {
            addCriterion("borrow_nid not between", value1, value2, "borrowNid");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(BigDecimal value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(BigDecimal value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(BigDecimal value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(BigDecimal value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<BigDecimal> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<BigDecimal> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNull() {
            addCriterion("income is null");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNotNull() {
            addCriterion("income is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeEqualTo(BigDecimal value) {
            addCriterion("income =", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotEqualTo(BigDecimal value) {
            addCriterion("income <>", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThan(BigDecimal value) {
            addCriterion("income >", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("income >=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThan(BigDecimal value) {
            addCriterion("income <", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("income <=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeIn(List<BigDecimal> values) {
            addCriterion("income in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotIn(List<BigDecimal> values) {
            addCriterion("income not in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income not between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeOldIsNull() {
            addCriterion("income_old is null");
            return (Criteria) this;
        }

        public Criteria andIncomeOldIsNotNull() {
            addCriterion("income_old is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeOldEqualTo(BigDecimal value) {
            addCriterion("income_old =", value, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldNotEqualTo(BigDecimal value) {
            addCriterion("income_old <>", value, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldGreaterThan(BigDecimal value) {
            addCriterion("income_old >", value, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("income_old >=", value, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldLessThan(BigDecimal value) {
            addCriterion("income_old <", value, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("income_old <=", value, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldIn(List<BigDecimal> values) {
            addCriterion("income_old in", values, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldNotIn(List<BigDecimal> values) {
            addCriterion("income_old not in", values, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income_old between", value1, value2, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income_old not between", value1, value2, "incomeOld");
            return (Criteria) this;
        }

        public Criteria andIncomeNewIsNull() {
            addCriterion("income_new is null");
            return (Criteria) this;
        }

        public Criteria andIncomeNewIsNotNull() {
            addCriterion("income_new is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeNewEqualTo(BigDecimal value) {
            addCriterion("income_new =", value, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewNotEqualTo(BigDecimal value) {
            addCriterion("income_new <>", value, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewGreaterThan(BigDecimal value) {
            addCriterion("income_new >", value, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("income_new >=", value, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewLessThan(BigDecimal value) {
            addCriterion("income_new <", value, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("income_new <=", value, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewIn(List<BigDecimal> values) {
            addCriterion("income_new in", values, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewNotIn(List<BigDecimal> values) {
            addCriterion("income_new not in", values, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income_new between", value1, value2, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andIncomeNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income_new not between", value1, value2, "incomeNew");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusIsNull() {
            addCriterion("account_web_status is null");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusIsNotNull() {
            addCriterion("account_web_status is not null");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusEqualTo(Integer value) {
            addCriterion("account_web_status =", value, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusNotEqualTo(Integer value) {
            addCriterion("account_web_status <>", value, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusGreaterThan(Integer value) {
            addCriterion("account_web_status >", value, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_web_status >=", value, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusLessThan(Integer value) {
            addCriterion("account_web_status <", value, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusLessThanOrEqualTo(Integer value) {
            addCriterion("account_web_status <=", value, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusIn(List<Integer> values) {
            addCriterion("account_web_status in", values, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusNotIn(List<Integer> values) {
            addCriterion("account_web_status not in", values, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusBetween(Integer value1, Integer value2) {
            addCriterion("account_web_status between", value1, value2, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountWebStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("account_web_status not between", value1, value2, "accountWebStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusIsNull() {
            addCriterion("account_user_status is null");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusIsNotNull() {
            addCriterion("account_user_status is not null");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusEqualTo(Integer value) {
            addCriterion("account_user_status =", value, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusNotEqualTo(Integer value) {
            addCriterion("account_user_status <>", value, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusGreaterThan(Integer value) {
            addCriterion("account_user_status >", value, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_user_status >=", value, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusLessThan(Integer value) {
            addCriterion("account_user_status <", value, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusLessThanOrEqualTo(Integer value) {
            addCriterion("account_user_status <=", value, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusIn(List<Integer> values) {
            addCriterion("account_user_status in", values, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusNotIn(List<Integer> values) {
            addCriterion("account_user_status not in", values, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusBetween(Integer value1, Integer value2) {
            addCriterion("account_user_status between", value1, value2, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountUserStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("account_user_status not between", value1, value2, "accountUserStatus");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNull() {
            addCriterion("account_type is null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNotNull() {
            addCriterion("account_type is not null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeEqualTo(String value) {
            addCriterion("account_type =", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotEqualTo(String value) {
            addCriterion("account_type <>", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThan(String value) {
            addCriterion("account_type >", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThanOrEqualTo(String value) {
            addCriterion("account_type >=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThan(String value) {
            addCriterion("account_type <", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThanOrEqualTo(String value) {
            addCriterion("account_type <=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLike(String value) {
            addCriterion("account_type like", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotLike(String value) {
            addCriterion("account_type not like", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIn(List<String> values) {
            addCriterion("account_type in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotIn(List<String> values) {
            addCriterion("account_type not in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeBetween(String value1, String value2) {
            addCriterion("account_type between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotBetween(String value1, String value2) {
            addCriterion("account_type not between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andExpendIsNull() {
            addCriterion("expend is null");
            return (Criteria) this;
        }

        public Criteria andExpendIsNotNull() {
            addCriterion("expend is not null");
            return (Criteria) this;
        }

        public Criteria andExpendEqualTo(BigDecimal value) {
            addCriterion("expend =", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendNotEqualTo(BigDecimal value) {
            addCriterion("expend <>", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendGreaterThan(BigDecimal value) {
            addCriterion("expend >", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("expend >=", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendLessThan(BigDecimal value) {
            addCriterion("expend <", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendLessThanOrEqualTo(BigDecimal value) {
            addCriterion("expend <=", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendIn(List<BigDecimal> values) {
            addCriterion("expend in", values, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendNotIn(List<BigDecimal> values) {
            addCriterion("expend not in", values, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend between", value1, value2, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend not between", value1, value2, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendOldIsNull() {
            addCriterion("expend_old is null");
            return (Criteria) this;
        }

        public Criteria andExpendOldIsNotNull() {
            addCriterion("expend_old is not null");
            return (Criteria) this;
        }

        public Criteria andExpendOldEqualTo(BigDecimal value) {
            addCriterion("expend_old =", value, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldNotEqualTo(BigDecimal value) {
            addCriterion("expend_old <>", value, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldGreaterThan(BigDecimal value) {
            addCriterion("expend_old >", value, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("expend_old >=", value, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldLessThan(BigDecimal value) {
            addCriterion("expend_old <", value, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("expend_old <=", value, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldIn(List<BigDecimal> values) {
            addCriterion("expend_old in", values, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldNotIn(List<BigDecimal> values) {
            addCriterion("expend_old not in", values, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend_old between", value1, value2, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend_old not between", value1, value2, "expendOld");
            return (Criteria) this;
        }

        public Criteria andExpendNewIsNull() {
            addCriterion("expend_new is null");
            return (Criteria) this;
        }

        public Criteria andExpendNewIsNotNull() {
            addCriterion("expend_new is not null");
            return (Criteria) this;
        }

        public Criteria andExpendNewEqualTo(BigDecimal value) {
            addCriterion("expend_new =", value, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewNotEqualTo(BigDecimal value) {
            addCriterion("expend_new <>", value, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewGreaterThan(BigDecimal value) {
            addCriterion("expend_new >", value, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("expend_new >=", value, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewLessThan(BigDecimal value) {
            addCriterion("expend_new <", value, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("expend_new <=", value, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewIn(List<BigDecimal> values) {
            addCriterion("expend_new in", values, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewNotIn(List<BigDecimal> values) {
            addCriterion("expend_new not in", values, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend_new between", value1, value2, "expendNew");
            return (Criteria) this;
        }

        public Criteria andExpendNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend_new not between", value1, value2, "expendNew");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNull() {
            addCriterion("balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(BigDecimal value) {
            addCriterion("balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(BigDecimal value) {
            addCriterion("balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(BigDecimal value) {
            addCriterion("balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(BigDecimal value) {
            addCriterion("balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<BigDecimal> values) {
            addCriterion("balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<BigDecimal> values) {
            addCriterion("balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance not between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceOldIsNull() {
            addCriterion("balance_old is null");
            return (Criteria) this;
        }

        public Criteria andBalanceOldIsNotNull() {
            addCriterion("balance_old is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceOldEqualTo(BigDecimal value) {
            addCriterion("balance_old =", value, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldNotEqualTo(BigDecimal value) {
            addCriterion("balance_old <>", value, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldGreaterThan(BigDecimal value) {
            addCriterion("balance_old >", value, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_old >=", value, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldLessThan(BigDecimal value) {
            addCriterion("balance_old <", value, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_old <=", value, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldIn(List<BigDecimal> values) {
            addCriterion("balance_old in", values, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldNotIn(List<BigDecimal> values) {
            addCriterion("balance_old not in", values, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_old between", value1, value2, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_old not between", value1, value2, "balanceOld");
            return (Criteria) this;
        }

        public Criteria andBalanceNewIsNull() {
            addCriterion("balance_new is null");
            return (Criteria) this;
        }

        public Criteria andBalanceNewIsNotNull() {
            addCriterion("balance_new is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceNewEqualTo(BigDecimal value) {
            addCriterion("balance_new =", value, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewNotEqualTo(BigDecimal value) {
            addCriterion("balance_new <>", value, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewGreaterThan(BigDecimal value) {
            addCriterion("balance_new >", value, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_new >=", value, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewLessThan(BigDecimal value) {
            addCriterion("balance_new <", value, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_new <=", value, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewIn(List<BigDecimal> values) {
            addCriterion("balance_new in", values, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewNotIn(List<BigDecimal> values) {
            addCriterion("balance_new not in", values, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_new between", value1, value2, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_new not between", value1, value2, "balanceNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashIsNull() {
            addCriterion("balance_cash is null");
            return (Criteria) this;
        }

        public Criteria andBalanceCashIsNotNull() {
            addCriterion("balance_cash is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceCashEqualTo(BigDecimal value) {
            addCriterion("balance_cash =", value, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNotEqualTo(BigDecimal value) {
            addCriterion("balance_cash <>", value, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashGreaterThan(BigDecimal value) {
            addCriterion("balance_cash >", value, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_cash >=", value, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashLessThan(BigDecimal value) {
            addCriterion("balance_cash <", value, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_cash <=", value, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashIn(List<BigDecimal> values) {
            addCriterion("balance_cash in", values, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNotIn(List<BigDecimal> values) {
            addCriterion("balance_cash not in", values, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_cash between", value1, value2, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_cash not between", value1, value2, "balanceCash");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldIsNull() {
            addCriterion("balance_cash_old is null");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldIsNotNull() {
            addCriterion("balance_cash_old is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldEqualTo(BigDecimal value) {
            addCriterion("balance_cash_old =", value, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldNotEqualTo(BigDecimal value) {
            addCriterion("balance_cash_old <>", value, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldGreaterThan(BigDecimal value) {
            addCriterion("balance_cash_old >", value, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_cash_old >=", value, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldLessThan(BigDecimal value) {
            addCriterion("balance_cash_old <", value, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_cash_old <=", value, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldIn(List<BigDecimal> values) {
            addCriterion("balance_cash_old in", values, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldNotIn(List<BigDecimal> values) {
            addCriterion("balance_cash_old not in", values, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_cash_old between", value1, value2, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_cash_old not between", value1, value2, "balanceCashOld");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewIsNull() {
            addCriterion("balance_cash_new is null");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewIsNotNull() {
            addCriterion("balance_cash_new is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewEqualTo(BigDecimal value) {
            addCriterion("balance_cash_new =", value, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewNotEqualTo(BigDecimal value) {
            addCriterion("balance_cash_new <>", value, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewGreaterThan(BigDecimal value) {
            addCriterion("balance_cash_new >", value, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_cash_new >=", value, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewLessThan(BigDecimal value) {
            addCriterion("balance_cash_new <", value, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_cash_new <=", value, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewIn(List<BigDecimal> values) {
            addCriterion("balance_cash_new in", values, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewNotIn(List<BigDecimal> values) {
            addCriterion("balance_cash_new not in", values, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_cash_new between", value1, value2, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceCashNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_cash_new not between", value1, value2, "balanceCashNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostIsNull() {
            addCriterion("balance_frost is null");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostIsNotNull() {
            addCriterion("balance_frost is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostEqualTo(BigDecimal value) {
            addCriterion("balance_frost =", value, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNotEqualTo(BigDecimal value) {
            addCriterion("balance_frost <>", value, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostGreaterThan(BigDecimal value) {
            addCriterion("balance_frost >", value, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_frost >=", value, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostLessThan(BigDecimal value) {
            addCriterion("balance_frost <", value, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_frost <=", value, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostIn(List<BigDecimal> values) {
            addCriterion("balance_frost in", values, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNotIn(List<BigDecimal> values) {
            addCriterion("balance_frost not in", values, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_frost between", value1, value2, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_frost not between", value1, value2, "balanceFrost");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldIsNull() {
            addCriterion("balance_frost_old is null");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldIsNotNull() {
            addCriterion("balance_frost_old is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldEqualTo(BigDecimal value) {
            addCriterion("balance_frost_old =", value, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldNotEqualTo(BigDecimal value) {
            addCriterion("balance_frost_old <>", value, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldGreaterThan(BigDecimal value) {
            addCriterion("balance_frost_old >", value, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_frost_old >=", value, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldLessThan(BigDecimal value) {
            addCriterion("balance_frost_old <", value, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_frost_old <=", value, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldIn(List<BigDecimal> values) {
            addCriterion("balance_frost_old in", values, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldNotIn(List<BigDecimal> values) {
            addCriterion("balance_frost_old not in", values, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_frost_old between", value1, value2, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_frost_old not between", value1, value2, "balanceFrostOld");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewIsNull() {
            addCriterion("balance_frost_new is null");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewIsNotNull() {
            addCriterion("balance_frost_new is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewEqualTo(BigDecimal value) {
            addCriterion("balance_frost_new =", value, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewNotEqualTo(BigDecimal value) {
            addCriterion("balance_frost_new <>", value, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewGreaterThan(BigDecimal value) {
            addCriterion("balance_frost_new >", value, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_frost_new >=", value, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewLessThan(BigDecimal value) {
            addCriterion("balance_frost_new <", value, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance_frost_new <=", value, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewIn(List<BigDecimal> values) {
            addCriterion("balance_frost_new in", values, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewNotIn(List<BigDecimal> values) {
            addCriterion("balance_frost_new not in", values, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_frost_new between", value1, value2, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andBalanceFrostNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance_frost_new not between", value1, value2, "balanceFrostNew");
            return (Criteria) this;
        }

        public Criteria andFrostIsNull() {
            addCriterion("frost is null");
            return (Criteria) this;
        }

        public Criteria andFrostIsNotNull() {
            addCriterion("frost is not null");
            return (Criteria) this;
        }

        public Criteria andFrostEqualTo(BigDecimal value) {
            addCriterion("frost =", value, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostNotEqualTo(BigDecimal value) {
            addCriterion("frost <>", value, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostGreaterThan(BigDecimal value) {
            addCriterion("frost >", value, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("frost >=", value, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostLessThan(BigDecimal value) {
            addCriterion("frost <", value, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostLessThanOrEqualTo(BigDecimal value) {
            addCriterion("frost <=", value, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostIn(List<BigDecimal> values) {
            addCriterion("frost in", values, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostNotIn(List<BigDecimal> values) {
            addCriterion("frost not in", values, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost between", value1, value2, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost not between", value1, value2, "frost");
            return (Criteria) this;
        }

        public Criteria andFrostOldIsNull() {
            addCriterion("frost_old is null");
            return (Criteria) this;
        }

        public Criteria andFrostOldIsNotNull() {
            addCriterion("frost_old is not null");
            return (Criteria) this;
        }

        public Criteria andFrostOldEqualTo(BigDecimal value) {
            addCriterion("frost_old =", value, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldNotEqualTo(BigDecimal value) {
            addCriterion("frost_old <>", value, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldGreaterThan(BigDecimal value) {
            addCriterion("frost_old >", value, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("frost_old >=", value, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldLessThan(BigDecimal value) {
            addCriterion("frost_old <", value, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("frost_old <=", value, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldIn(List<BigDecimal> values) {
            addCriterion("frost_old in", values, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldNotIn(List<BigDecimal> values) {
            addCriterion("frost_old not in", values, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost_old between", value1, value2, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost_old not between", value1, value2, "frostOld");
            return (Criteria) this;
        }

        public Criteria andFrostNewIsNull() {
            addCriterion("frost_new is null");
            return (Criteria) this;
        }

        public Criteria andFrostNewIsNotNull() {
            addCriterion("frost_new is not null");
            return (Criteria) this;
        }

        public Criteria andFrostNewEqualTo(BigDecimal value) {
            addCriterion("frost_new =", value, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewNotEqualTo(BigDecimal value) {
            addCriterion("frost_new <>", value, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewGreaterThan(BigDecimal value) {
            addCriterion("frost_new >", value, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("frost_new >=", value, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewLessThan(BigDecimal value) {
            addCriterion("frost_new <", value, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("frost_new <=", value, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewIn(List<BigDecimal> values) {
            addCriterion("frost_new in", values, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewNotIn(List<BigDecimal> values) {
            addCriterion("frost_new not in", values, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost_new between", value1, value2, "frostNew");
            return (Criteria) this;
        }

        public Criteria andFrostNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost_new not between", value1, value2, "frostNew");
            return (Criteria) this;
        }

        public Criteria andRepayIsNull() {
            addCriterion("repay is null");
            return (Criteria) this;
        }

        public Criteria andRepayIsNotNull() {
            addCriterion("repay is not null");
            return (Criteria) this;
        }

        public Criteria andRepayEqualTo(BigDecimal value) {
            addCriterion("repay =", value, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayNotEqualTo(BigDecimal value) {
            addCriterion("repay <>", value, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayGreaterThan(BigDecimal value) {
            addCriterion("repay >", value, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("repay >=", value, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayLessThan(BigDecimal value) {
            addCriterion("repay <", value, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayLessThanOrEqualTo(BigDecimal value) {
            addCriterion("repay <=", value, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayIn(List<BigDecimal> values) {
            addCriterion("repay in", values, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayNotIn(List<BigDecimal> values) {
            addCriterion("repay not in", values, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("repay between", value1, value2, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("repay not between", value1, value2, "repay");
            return (Criteria) this;
        }

        public Criteria andRepayNewIsNull() {
            addCriterion("repay_new is null");
            return (Criteria) this;
        }

        public Criteria andRepayNewIsNotNull() {
            addCriterion("repay_new is not null");
            return (Criteria) this;
        }

        public Criteria andRepayNewEqualTo(BigDecimal value) {
            addCriterion("repay_new =", value, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewNotEqualTo(BigDecimal value) {
            addCriterion("repay_new <>", value, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewGreaterThan(BigDecimal value) {
            addCriterion("repay_new >", value, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("repay_new >=", value, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewLessThan(BigDecimal value) {
            addCriterion("repay_new <", value, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("repay_new <=", value, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewIn(List<BigDecimal> values) {
            addCriterion("repay_new in", values, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewNotIn(List<BigDecimal> values) {
            addCriterion("repay_new not in", values, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("repay_new between", value1, value2, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("repay_new not between", value1, value2, "repayNew");
            return (Criteria) this;
        }

        public Criteria andRepayOldIsNull() {
            addCriterion("repay_old is null");
            return (Criteria) this;
        }

        public Criteria andRepayOldIsNotNull() {
            addCriterion("repay_old is not null");
            return (Criteria) this;
        }

        public Criteria andRepayOldEqualTo(BigDecimal value) {
            addCriterion("repay_old =", value, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldNotEqualTo(BigDecimal value) {
            addCriterion("repay_old <>", value, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldGreaterThan(BigDecimal value) {
            addCriterion("repay_old >", value, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("repay_old >=", value, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldLessThan(BigDecimal value) {
            addCriterion("repay_old <", value, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("repay_old <=", value, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldIn(List<BigDecimal> values) {
            addCriterion("repay_old in", values, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldNotIn(List<BigDecimal> values) {
            addCriterion("repay_old not in", values, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("repay_old between", value1, value2, "repayOld");
            return (Criteria) this;
        }

        public Criteria andRepayOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("repay_old not between", value1, value2, "repayOld");
            return (Criteria) this;
        }

        public Criteria andAwaitIsNull() {
            addCriterion("await is null");
            return (Criteria) this;
        }

        public Criteria andAwaitIsNotNull() {
            addCriterion("await is not null");
            return (Criteria) this;
        }

        public Criteria andAwaitEqualTo(BigDecimal value) {
            addCriterion("await =", value, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitNotEqualTo(BigDecimal value) {
            addCriterion("await <>", value, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitGreaterThan(BigDecimal value) {
            addCriterion("await >", value, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("await >=", value, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitLessThan(BigDecimal value) {
            addCriterion("await <", value, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("await <=", value, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitIn(List<BigDecimal> values) {
            addCriterion("await in", values, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitNotIn(List<BigDecimal> values) {
            addCriterion("await not in", values, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("await between", value1, value2, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("await not between", value1, value2, "await");
            return (Criteria) this;
        }

        public Criteria andAwaitOldIsNull() {
            addCriterion("await_old is null");
            return (Criteria) this;
        }

        public Criteria andAwaitOldIsNotNull() {
            addCriterion("await_old is not null");
            return (Criteria) this;
        }

        public Criteria andAwaitOldEqualTo(BigDecimal value) {
            addCriterion("await_old =", value, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldNotEqualTo(BigDecimal value) {
            addCriterion("await_old <>", value, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldGreaterThan(BigDecimal value) {
            addCriterion("await_old >", value, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("await_old >=", value, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldLessThan(BigDecimal value) {
            addCriterion("await_old <", value, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("await_old <=", value, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldIn(List<BigDecimal> values) {
            addCriterion("await_old in", values, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldNotIn(List<BigDecimal> values) {
            addCriterion("await_old not in", values, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("await_old between", value1, value2, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitOldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("await_old not between", value1, value2, "awaitOld");
            return (Criteria) this;
        }

        public Criteria andAwaitNewIsNull() {
            addCriterion("await_new is null");
            return (Criteria) this;
        }

        public Criteria andAwaitNewIsNotNull() {
            addCriterion("await_new is not null");
            return (Criteria) this;
        }

        public Criteria andAwaitNewEqualTo(BigDecimal value) {
            addCriterion("await_new =", value, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewNotEqualTo(BigDecimal value) {
            addCriterion("await_new <>", value, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewGreaterThan(BigDecimal value) {
            addCriterion("await_new >", value, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("await_new >=", value, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewLessThan(BigDecimal value) {
            addCriterion("await_new <", value, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("await_new <=", value, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewIn(List<BigDecimal> values) {
            addCriterion("await_new in", values, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewNotIn(List<BigDecimal> values) {
            addCriterion("await_new not in", values, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("await_new between", value1, value2, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andAwaitNewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("await_new not between", value1, value2, "awaitNew");
            return (Criteria) this;
        }

        public Criteria andToUseridIsNull() {
            addCriterion("to_userid is null");
            return (Criteria) this;
        }

        public Criteria andToUseridIsNotNull() {
            addCriterion("to_userid is not null");
            return (Criteria) this;
        }

        public Criteria andToUseridEqualTo(Integer value) {
            addCriterion("to_userid =", value, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridNotEqualTo(Integer value) {
            addCriterion("to_userid <>", value, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridGreaterThan(Integer value) {
            addCriterion("to_userid >", value, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("to_userid >=", value, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridLessThan(Integer value) {
            addCriterion("to_userid <", value, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridLessThanOrEqualTo(Integer value) {
            addCriterion("to_userid <=", value, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridIn(List<Integer> values) {
            addCriterion("to_userid in", values, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridNotIn(List<Integer> values) {
            addCriterion("to_userid not in", values, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridBetween(Integer value1, Integer value2) {
            addCriterion("to_userid between", value1, value2, "toUserid");
            return (Criteria) this;
        }

        public Criteria andToUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("to_userid not between", value1, value2, "toUserid");
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

        public Criteria andAddtimeIsNull() {
            addCriterion("addtime is null");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNotNull() {
            addCriterion("addtime is not null");
            return (Criteria) this;
        }

        public Criteria andAddtimeEqualTo(String value) {
            addCriterion("addtime =", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotEqualTo(String value) {
            addCriterion("addtime <>", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThan(String value) {
            addCriterion("addtime >", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThanOrEqualTo(String value) {
            addCriterion("addtime >=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThan(String value) {
            addCriterion("addtime <", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThanOrEqualTo(String value) {
            addCriterion("addtime <=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLike(String value) {
            addCriterion("addtime like", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotLike(String value) {
            addCriterion("addtime not like", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeIn(List<String> values) {
            addCriterion("addtime in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotIn(List<String> values) {
            addCriterion("addtime not in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeBetween(String value1, String value2) {
            addCriterion("addtime between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotBetween(String value1, String value2) {
            addCriterion("addtime not between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddipIsNull() {
            addCriterion("addip is null");
            return (Criteria) this;
        }

        public Criteria andAddipIsNotNull() {
            addCriterion("addip is not null");
            return (Criteria) this;
        }

        public Criteria andAddipEqualTo(String value) {
            addCriterion("addip =", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotEqualTo(String value) {
            addCriterion("addip <>", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipGreaterThan(String value) {
            addCriterion("addip >", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipGreaterThanOrEqualTo(String value) {
            addCriterion("addip >=", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipLessThan(String value) {
            addCriterion("addip <", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipLessThanOrEqualTo(String value) {
            addCriterion("addip <=", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipLike(String value) {
            addCriterion("addip like", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotLike(String value) {
            addCriterion("addip not like", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipIn(List<String> values) {
            addCriterion("addip in", values, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotIn(List<String> values) {
            addCriterion("addip not in", values, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipBetween(String value1, String value2) {
            addCriterion("addip between", value1, value2, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotBetween(String value1, String value2) {
            addCriterion("addip not between", value1, value2, "addip");
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