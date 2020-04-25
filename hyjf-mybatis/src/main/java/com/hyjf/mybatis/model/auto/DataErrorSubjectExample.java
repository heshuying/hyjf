package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataErrorSubjectExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public DataErrorSubjectExample() {
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

        public Criteria andBorrowIdIsNull() {
            addCriterion("borrow_id is null");
            return (Criteria) this;
        }

        public Criteria andBorrowIdIsNotNull() {
            addCriterion("borrow_id is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowIdEqualTo(Integer value) {
            addCriterion("borrow_id =", value, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdNotEqualTo(Integer value) {
            addCriterion("borrow_id <>", value, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdGreaterThan(Integer value) {
            addCriterion("borrow_id >", value, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("borrow_id >=", value, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdLessThan(Integer value) {
            addCriterion("borrow_id <", value, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdLessThanOrEqualTo(Integer value) {
            addCriterion("borrow_id <=", value, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdIn(List<Integer> values) {
            addCriterion("borrow_id in", values, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdNotIn(List<Integer> values) {
            addCriterion("borrow_id not in", values, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdBetween(Integer value1, Integer value2) {
            addCriterion("borrow_id between", value1, value2, "borrowId");
            return (Criteria) this;
        }

        public Criteria andBorrowIdNotBetween(Integer value1, Integer value2) {
            addCriterion("borrow_id not between", value1, value2, "borrowId");
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

        public Criteria andBorrowDescIsNull() {
            addCriterion("borrow_desc is null");
            return (Criteria) this;
        }

        public Criteria andBorrowDescIsNotNull() {
            addCriterion("borrow_desc is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowDescEqualTo(String value) {
            addCriterion("borrow_desc =", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescNotEqualTo(String value) {
            addCriterion("borrow_desc <>", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescGreaterThan(String value) {
            addCriterion("borrow_desc >", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescGreaterThanOrEqualTo(String value) {
            addCriterion("borrow_desc >=", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescLessThan(String value) {
            addCriterion("borrow_desc <", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescLessThanOrEqualTo(String value) {
            addCriterion("borrow_desc <=", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescLike(String value) {
            addCriterion("borrow_desc like", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescNotLike(String value) {
            addCriterion("borrow_desc not like", value, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescIn(List<String> values) {
            addCriterion("borrow_desc in", values, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescNotIn(List<String> values) {
            addCriterion("borrow_desc not in", values, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescBetween(String value1, String value2) {
            addCriterion("borrow_desc between", value1, value2, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowDescNotBetween(String value1, String value2) {
            addCriterion("borrow_desc not between", value1, value2, "borrowDesc");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdIsNull() {
            addCriterion("borrow_account_id is null");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdIsNotNull() {
            addCriterion("borrow_account_id is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdEqualTo(String value) {
            addCriterion("borrow_account_id =", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdNotEqualTo(String value) {
            addCriterion("borrow_account_id <>", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdGreaterThan(String value) {
            addCriterion("borrow_account_id >", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdGreaterThanOrEqualTo(String value) {
            addCriterion("borrow_account_id >=", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdLessThan(String value) {
            addCriterion("borrow_account_id <", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdLessThanOrEqualTo(String value) {
            addCriterion("borrow_account_id <=", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdLike(String value) {
            addCriterion("borrow_account_id like", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdNotLike(String value) {
            addCriterion("borrow_account_id not like", value, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdIn(List<String> values) {
            addCriterion("borrow_account_id in", values, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdNotIn(List<String> values) {
            addCriterion("borrow_account_id not in", values, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdBetween(String value1, String value2) {
            addCriterion("borrow_account_id between", value1, value2, "borrowAccountId");
            return (Criteria) this;
        }

        public Criteria andBorrowAccountIdNotBetween(String value1, String value2) {
            addCriterion("borrow_account_id not between", value1, value2, "borrowAccountId");
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

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeIsNull() {
            addCriterion("payment_type is null");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeIsNotNull() {
            addCriterion("payment_type is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeEqualTo(Integer value) {
            addCriterion("payment_type =", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeNotEqualTo(Integer value) {
            addCriterion("payment_type <>", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeGreaterThan(Integer value) {
            addCriterion("payment_type >", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("payment_type >=", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeLessThan(Integer value) {
            addCriterion("payment_type <", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeLessThanOrEqualTo(Integer value) {
            addCriterion("payment_type <=", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeIn(List<Integer> values) {
            addCriterion("payment_type in", values, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeNotIn(List<Integer> values) {
            addCriterion("payment_type not in", values, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeBetween(Integer value1, Integer value2) {
            addCriterion("payment_type between", value1, value2, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("payment_type not between", value1, value2, "paymentType");
            return (Criteria) this;
        }

        public Criteria andLoanTermIsNull() {
            addCriterion("loan_term is null");
            return (Criteria) this;
        }

        public Criteria andLoanTermIsNotNull() {
            addCriterion("loan_term is not null");
            return (Criteria) this;
        }

        public Criteria andLoanTermEqualTo(Integer value) {
            addCriterion("loan_term =", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermNotEqualTo(Integer value) {
            addCriterion("loan_term <>", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermGreaterThan(Integer value) {
            addCriterion("loan_term >", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermGreaterThanOrEqualTo(Integer value) {
            addCriterion("loan_term >=", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermLessThan(Integer value) {
            addCriterion("loan_term <", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermLessThanOrEqualTo(Integer value) {
            addCriterion("loan_term <=", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermIn(List<Integer> values) {
            addCriterion("loan_term in", values, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermNotIn(List<Integer> values) {
            addCriterion("loan_term not in", values, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermBetween(Integer value1, Integer value2) {
            addCriterion("loan_term between", value1, value2, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermNotBetween(Integer value1, Integer value2) {
            addCriterion("loan_term not between", value1, value2, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andBorrowAprIsNull() {
            addCriterion("borrow_apr is null");
            return (Criteria) this;
        }

        public Criteria andBorrowAprIsNotNull() {
            addCriterion("borrow_apr is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowAprEqualTo(BigDecimal value) {
            addCriterion("borrow_apr =", value, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprNotEqualTo(BigDecimal value) {
            addCriterion("borrow_apr <>", value, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprGreaterThan(BigDecimal value) {
            addCriterion("borrow_apr >", value, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("borrow_apr >=", value, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprLessThan(BigDecimal value) {
            addCriterion("borrow_apr <", value, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprLessThanOrEqualTo(BigDecimal value) {
            addCriterion("borrow_apr <=", value, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprIn(List<BigDecimal> values) {
            addCriterion("borrow_apr in", values, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprNotIn(List<BigDecimal> values) {
            addCriterion("borrow_apr not in", values, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrow_apr between", value1, value2, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andBorrowAprNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("borrow_apr not between", value1, value2, "borrowApr");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdIsNull() {
            addCriterion("guarantee_account_id is null");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdIsNotNull() {
            addCriterion("guarantee_account_id is not null");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdEqualTo(String value) {
            addCriterion("guarantee_account_id =", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdNotEqualTo(String value) {
            addCriterion("guarantee_account_id <>", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdGreaterThan(String value) {
            addCriterion("guarantee_account_id >", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdGreaterThanOrEqualTo(String value) {
            addCriterion("guarantee_account_id >=", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdLessThan(String value) {
            addCriterion("guarantee_account_id <", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdLessThanOrEqualTo(String value) {
            addCriterion("guarantee_account_id <=", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdLike(String value) {
            addCriterion("guarantee_account_id like", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdNotLike(String value) {
            addCriterion("guarantee_account_id not like", value, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdIn(List<String> values) {
            addCriterion("guarantee_account_id in", values, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdNotIn(List<String> values) {
            addCriterion("guarantee_account_id not in", values, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdBetween(String value1, String value2) {
            addCriterion("guarantee_account_id between", value1, value2, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andGuaranteeAccountIdNotBetween(String value1, String value2) {
            addCriterion("guarantee_account_id not between", value1, value2, "guaranteeAccountId");
            return (Criteria) this;
        }

        public Criteria andRaiseDateIsNull() {
            addCriterion("raise_date is null");
            return (Criteria) this;
        }

        public Criteria andRaiseDateIsNotNull() {
            addCriterion("raise_date is not null");
            return (Criteria) this;
        }

        public Criteria andRaiseDateEqualTo(String value) {
            addCriterion("raise_date =", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateNotEqualTo(String value) {
            addCriterion("raise_date <>", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateGreaterThan(String value) {
            addCriterion("raise_date >", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateGreaterThanOrEqualTo(String value) {
            addCriterion("raise_date >=", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateLessThan(String value) {
            addCriterion("raise_date <", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateLessThanOrEqualTo(String value) {
            addCriterion("raise_date <=", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateLike(String value) {
            addCriterion("raise_date like", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateNotLike(String value) {
            addCriterion("raise_date not like", value, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateIn(List<String> values) {
            addCriterion("raise_date in", values, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateNotIn(List<String> values) {
            addCriterion("raise_date not in", values, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateBetween(String value1, String value2) {
            addCriterion("raise_date between", value1, value2, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseDateNotBetween(String value1, String value2) {
            addCriterion("raise_date not between", value1, value2, "raiseDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateIsNull() {
            addCriterion("raise_end_date is null");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateIsNotNull() {
            addCriterion("raise_end_date is not null");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateEqualTo(String value) {
            addCriterion("raise_end_date =", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateNotEqualTo(String value) {
            addCriterion("raise_end_date <>", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateGreaterThan(String value) {
            addCriterion("raise_end_date >", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateGreaterThanOrEqualTo(String value) {
            addCriterion("raise_end_date >=", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateLessThan(String value) {
            addCriterion("raise_end_date <", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateLessThanOrEqualTo(String value) {
            addCriterion("raise_end_date <=", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateLike(String value) {
            addCriterion("raise_end_date like", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateNotLike(String value) {
            addCriterion("raise_end_date not like", value, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateIn(List<String> values) {
            addCriterion("raise_end_date in", values, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateNotIn(List<String> values) {
            addCriterion("raise_end_date not in", values, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateBetween(String value1, String value2) {
            addCriterion("raise_end_date between", value1, value2, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRaiseEndDateNotBetween(String value1, String value2) {
            addCriterion("raise_end_date not between", value1, value2, "raiseEndDate");
            return (Criteria) this;
        }

        public Criteria andRespCodeIsNull() {
            addCriterion("resp_code is null");
            return (Criteria) this;
        }

        public Criteria andRespCodeIsNotNull() {
            addCriterion("resp_code is not null");
            return (Criteria) this;
        }

        public Criteria andRespCodeEqualTo(String value) {
            addCriterion("resp_code =", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeNotEqualTo(String value) {
            addCriterion("resp_code <>", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeGreaterThan(String value) {
            addCriterion("resp_code >", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeGreaterThanOrEqualTo(String value) {
            addCriterion("resp_code >=", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeLessThan(String value) {
            addCriterion("resp_code <", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeLessThanOrEqualTo(String value) {
            addCriterion("resp_code <=", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeLike(String value) {
            addCriterion("resp_code like", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeNotLike(String value) {
            addCriterion("resp_code not like", value, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeIn(List<String> values) {
            addCriterion("resp_code in", values, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeNotIn(List<String> values) {
            addCriterion("resp_code not in", values, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeBetween(String value1, String value2) {
            addCriterion("resp_code between", value1, value2, "respCode");
            return (Criteria) this;
        }

        public Criteria andRespCodeNotBetween(String value1, String value2) {
            addCriterion("resp_code not between", value1, value2, "respCode");
            return (Criteria) this;
        }

        public Criteria andErrorDescIsNull() {
            addCriterion("error_desc is null");
            return (Criteria) this;
        }

        public Criteria andErrorDescIsNotNull() {
            addCriterion("error_desc is not null");
            return (Criteria) this;
        }

        public Criteria andErrorDescEqualTo(String value) {
            addCriterion("error_desc =", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescNotEqualTo(String value) {
            addCriterion("error_desc <>", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescGreaterThan(String value) {
            addCriterion("error_desc >", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescGreaterThanOrEqualTo(String value) {
            addCriterion("error_desc >=", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescLessThan(String value) {
            addCriterion("error_desc <", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescLessThanOrEqualTo(String value) {
            addCriterion("error_desc <=", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescLike(String value) {
            addCriterion("error_desc like", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescNotLike(String value) {
            addCriterion("error_desc not like", value, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescIn(List<String> values) {
            addCriterion("error_desc in", values, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescNotIn(List<String> values) {
            addCriterion("error_desc not in", values, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescBetween(String value1, String value2) {
            addCriterion("error_desc between", value1, value2, "errorDesc");
            return (Criteria) this;
        }

        public Criteria andErrorDescNotBetween(String value1, String value2) {
            addCriterion("error_desc not between", value1, value2, "errorDesc");
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

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
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

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
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