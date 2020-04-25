package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataErrorDebtExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public DataErrorDebtExample() {
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

        public Criteria andBankIdIsNull() {
            addCriterion("bank_id is null");
            return (Criteria) this;
        }

        public Criteria andBankIdIsNotNull() {
            addCriterion("bank_id is not null");
            return (Criteria) this;
        }

        public Criteria andBankIdEqualTo(String value) {
            addCriterion("bank_id =", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotEqualTo(String value) {
            addCriterion("bank_id <>", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdGreaterThan(String value) {
            addCriterion("bank_id >", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdGreaterThanOrEqualTo(String value) {
            addCriterion("bank_id >=", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLessThan(String value) {
            addCriterion("bank_id <", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLessThanOrEqualTo(String value) {
            addCriterion("bank_id <=", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLike(String value) {
            addCriterion("bank_id like", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotLike(String value) {
            addCriterion("bank_id not like", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdIn(List<String> values) {
            addCriterion("bank_id in", values, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotIn(List<String> values) {
            addCriterion("bank_id not in", values, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdBetween(String value1, String value2) {
            addCriterion("bank_id between", value1, value2, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotBetween(String value1, String value2) {
            addCriterion("bank_id not between", value1, value2, "bankId");
            return (Criteria) this;
        }

        public Criteria andBatchIdIsNull() {
            addCriterion("batch_id is null");
            return (Criteria) this;
        }

        public Criteria andBatchIdIsNotNull() {
            addCriterion("batch_id is not null");
            return (Criteria) this;
        }

        public Criteria andBatchIdEqualTo(String value) {
            addCriterion("batch_id =", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdNotEqualTo(String value) {
            addCriterion("batch_id <>", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdGreaterThan(String value) {
            addCriterion("batch_id >", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdGreaterThanOrEqualTo(String value) {
            addCriterion("batch_id >=", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdLessThan(String value) {
            addCriterion("batch_id <", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdLessThanOrEqualTo(String value) {
            addCriterion("batch_id <=", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdLike(String value) {
            addCriterion("batch_id like", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdNotLike(String value) {
            addCriterion("batch_id not like", value, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdIn(List<String> values) {
            addCriterion("batch_id in", values, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdNotIn(List<String> values) {
            addCriterion("batch_id not in", values, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdBetween(String value1, String value2) {
            addCriterion("batch_id between", value1, value2, "batchId");
            return (Criteria) this;
        }

        public Criteria andBatchIdNotBetween(String value1, String value2) {
            addCriterion("batch_id not between", value1, value2, "batchId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdIsNull() {
            addCriterion("borrow_user_id is null");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdIsNotNull() {
            addCriterion("borrow_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdEqualTo(Integer value) {
            addCriterion("borrow_user_id =", value, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdNotEqualTo(Integer value) {
            addCriterion("borrow_user_id <>", value, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdGreaterThan(Integer value) {
            addCriterion("borrow_user_id >", value, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("borrow_user_id >=", value, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdLessThan(Integer value) {
            addCriterion("borrow_user_id <", value, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("borrow_user_id <=", value, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdIn(List<Integer> values) {
            addCriterion("borrow_user_id in", values, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdNotIn(List<Integer> values) {
            addCriterion("borrow_user_id not in", values, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdBetween(Integer value1, Integer value2) {
            addCriterion("borrow_user_id between", value1, value2, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andBorrowUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("borrow_user_id not between", value1, value2, "borrowUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdIsNull() {
            addCriterion("ten_user_id is null");
            return (Criteria) this;
        }

        public Criteria andTenUserIdIsNotNull() {
            addCriterion("ten_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andTenUserIdEqualTo(Integer value) {
            addCriterion("ten_user_id =", value, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdNotEqualTo(Integer value) {
            addCriterion("ten_user_id <>", value, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdGreaterThan(Integer value) {
            addCriterion("ten_user_id >", value, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ten_user_id >=", value, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdLessThan(Integer value) {
            addCriterion("ten_user_id <", value, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("ten_user_id <=", value, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdIn(List<Integer> values) {
            addCriterion("ten_user_id in", values, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdNotIn(List<Integer> values) {
            addCriterion("ten_user_id not in", values, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdBetween(Integer value1, Integer value2) {
            addCriterion("ten_user_id between", value1, value2, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andTenUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ten_user_id not between", value1, value2, "tenUserId");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccIsNull() {
            addCriterion("debt_holder_acc is null");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccIsNotNull() {
            addCriterion("debt_holder_acc is not null");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccEqualTo(String value) {
            addCriterion("debt_holder_acc =", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccNotEqualTo(String value) {
            addCriterion("debt_holder_acc <>", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccGreaterThan(String value) {
            addCriterion("debt_holder_acc >", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccGreaterThanOrEqualTo(String value) {
            addCriterion("debt_holder_acc >=", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccLessThan(String value) {
            addCriterion("debt_holder_acc <", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccLessThanOrEqualTo(String value) {
            addCriterion("debt_holder_acc <=", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccLike(String value) {
            addCriterion("debt_holder_acc like", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccNotLike(String value) {
            addCriterion("debt_holder_acc not like", value, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccIn(List<String> values) {
            addCriterion("debt_holder_acc in", values, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccNotIn(List<String> values) {
            addCriterion("debt_holder_acc not in", values, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccBetween(String value1, String value2) {
            addCriterion("debt_holder_acc between", value1, value2, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andDebtHolderAccNotBetween(String value1, String value2) {
            addCriterion("debt_holder_acc not between", value1, value2, "debtHolderAcc");
            return (Criteria) this;
        }

        public Criteria andProdIssuerIsNull() {
            addCriterion("prod_issuer is null");
            return (Criteria) this;
        }

        public Criteria andProdIssuerIsNotNull() {
            addCriterion("prod_issuer is not null");
            return (Criteria) this;
        }

        public Criteria andProdIssuerEqualTo(String value) {
            addCriterion("prod_issuer =", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerNotEqualTo(String value) {
            addCriterion("prod_issuer <>", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerGreaterThan(String value) {
            addCriterion("prod_issuer >", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerGreaterThanOrEqualTo(String value) {
            addCriterion("prod_issuer >=", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerLessThan(String value) {
            addCriterion("prod_issuer <", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerLessThanOrEqualTo(String value) {
            addCriterion("prod_issuer <=", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerLike(String value) {
            addCriterion("prod_issuer like", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerNotLike(String value) {
            addCriterion("prod_issuer not like", value, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerIn(List<String> values) {
            addCriterion("prod_issuer in", values, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerNotIn(List<String> values) {
            addCriterion("prod_issuer not in", values, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerBetween(String value1, String value2) {
            addCriterion("prod_issuer between", value1, value2, "prodIssuer");
            return (Criteria) this;
        }

        public Criteria andProdIssuerNotBetween(String value1, String value2) {
            addCriterion("prod_issuer not between", value1, value2, "prodIssuer");
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

        public Criteria andSerialNumIsNull() {
            addCriterion("serial_num is null");
            return (Criteria) this;
        }

        public Criteria andSerialNumIsNotNull() {
            addCriterion("serial_num is not null");
            return (Criteria) this;
        }

        public Criteria andSerialNumEqualTo(String value) {
            addCriterion("serial_num =", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumNotEqualTo(String value) {
            addCriterion("serial_num <>", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumGreaterThan(String value) {
            addCriterion("serial_num >", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumGreaterThanOrEqualTo(String value) {
            addCriterion("serial_num >=", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumLessThan(String value) {
            addCriterion("serial_num <", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumLessThanOrEqualTo(String value) {
            addCriterion("serial_num <=", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumLike(String value) {
            addCriterion("serial_num like", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumNotLike(String value) {
            addCriterion("serial_num not like", value, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumIn(List<String> values) {
            addCriterion("serial_num in", values, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumNotIn(List<String> values) {
            addCriterion("serial_num not in", values, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumBetween(String value1, String value2) {
            addCriterion("serial_num between", value1, value2, "serialNum");
            return (Criteria) this;
        }

        public Criteria andSerialNumNotBetween(String value1, String value2) {
            addCriterion("serial_num not between", value1, value2, "serialNum");
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

        public Criteria andInterestWaitIsNull() {
            addCriterion("interest_wait is null");
            return (Criteria) this;
        }

        public Criteria andInterestWaitIsNotNull() {
            addCriterion("interest_wait is not null");
            return (Criteria) this;
        }

        public Criteria andInterestWaitEqualTo(BigDecimal value) {
            addCriterion("interest_wait =", value, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitNotEqualTo(BigDecimal value) {
            addCriterion("interest_wait <>", value, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitGreaterThan(BigDecimal value) {
            addCriterion("interest_wait >", value, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("interest_wait >=", value, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitLessThan(BigDecimal value) {
            addCriterion("interest_wait <", value, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("interest_wait <=", value, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitIn(List<BigDecimal> values) {
            addCriterion("interest_wait in", values, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitNotIn(List<BigDecimal> values) {
            addCriterion("interest_wait not in", values, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("interest_wait between", value1, value2, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestWaitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("interest_wait not between", value1, value2, "interestWait");
            return (Criteria) this;
        }

        public Criteria andInterestPaidIsNull() {
            addCriterion("interest_paid is null");
            return (Criteria) this;
        }

        public Criteria andInterestPaidIsNotNull() {
            addCriterion("interest_paid is not null");
            return (Criteria) this;
        }

        public Criteria andInterestPaidEqualTo(BigDecimal value) {
            addCriterion("interest_paid =", value, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidNotEqualTo(BigDecimal value) {
            addCriterion("interest_paid <>", value, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidGreaterThan(BigDecimal value) {
            addCriterion("interest_paid >", value, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("interest_paid >=", value, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidLessThan(BigDecimal value) {
            addCriterion("interest_paid <", value, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidLessThanOrEqualTo(BigDecimal value) {
            addCriterion("interest_paid <=", value, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidIn(List<BigDecimal> values) {
            addCriterion("interest_paid in", values, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidNotIn(List<BigDecimal> values) {
            addCriterion("interest_paid not in", values, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("interest_paid between", value1, value2, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andInterestPaidNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("interest_paid not between", value1, value2, "interestPaid");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateIsNull() {
            addCriterion("debt_obt_date is null");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateIsNotNull() {
            addCriterion("debt_obt_date is not null");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateEqualTo(String value) {
            addCriterion("debt_obt_date =", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateNotEqualTo(String value) {
            addCriterion("debt_obt_date <>", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateGreaterThan(String value) {
            addCriterion("debt_obt_date >", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateGreaterThanOrEqualTo(String value) {
            addCriterion("debt_obt_date >=", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateLessThan(String value) {
            addCriterion("debt_obt_date <", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateLessThanOrEqualTo(String value) {
            addCriterion("debt_obt_date <=", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateLike(String value) {
            addCriterion("debt_obt_date like", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateNotLike(String value) {
            addCriterion("debt_obt_date not like", value, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateIn(List<String> values) {
            addCriterion("debt_obt_date in", values, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateNotIn(List<String> values) {
            addCriterion("debt_obt_date not in", values, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateBetween(String value1, String value2) {
            addCriterion("debt_obt_date between", value1, value2, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andDebtObtDateNotBetween(String value1, String value2) {
            addCriterion("debt_obt_date not between", value1, value2, "debtObtDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateIsNull() {
            addCriterion("interest_date is null");
            return (Criteria) this;
        }

        public Criteria andInterestDateIsNotNull() {
            addCriterion("interest_date is not null");
            return (Criteria) this;
        }

        public Criteria andInterestDateEqualTo(String value) {
            addCriterion("interest_date =", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateNotEqualTo(String value) {
            addCriterion("interest_date <>", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateGreaterThan(String value) {
            addCriterion("interest_date >", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateGreaterThanOrEqualTo(String value) {
            addCriterion("interest_date >=", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateLessThan(String value) {
            addCriterion("interest_date <", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateLessThanOrEqualTo(String value) {
            addCriterion("interest_date <=", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateLike(String value) {
            addCriterion("interest_date like", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateNotLike(String value) {
            addCriterion("interest_date not like", value, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateIn(List<String> values) {
            addCriterion("interest_date in", values, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateNotIn(List<String> values) {
            addCriterion("interest_date not in", values, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateBetween(String value1, String value2) {
            addCriterion("interest_date between", value1, value2, "interestDate");
            return (Criteria) this;
        }

        public Criteria andInterestDateNotBetween(String value1, String value2) {
            addCriterion("interest_date not between", value1, value2, "interestDate");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleIsNull() {
            addCriterion("int_pay_style is null");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleIsNotNull() {
            addCriterion("int_pay_style is not null");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleEqualTo(Integer value) {
            addCriterion("int_pay_style =", value, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleNotEqualTo(Integer value) {
            addCriterion("int_pay_style <>", value, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleGreaterThan(Integer value) {
            addCriterion("int_pay_style >", value, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleGreaterThanOrEqualTo(Integer value) {
            addCriterion("int_pay_style >=", value, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleLessThan(Integer value) {
            addCriterion("int_pay_style <", value, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleLessThanOrEqualTo(Integer value) {
            addCriterion("int_pay_style <=", value, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleIn(List<Integer> values) {
            addCriterion("int_pay_style in", values, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleNotIn(List<Integer> values) {
            addCriterion("int_pay_style not in", values, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleBetween(Integer value1, Integer value2) {
            addCriterion("int_pay_style between", value1, value2, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayStyleNotBetween(Integer value1, Integer value2) {
            addCriterion("int_pay_style not between", value1, value2, "intPayStyle");
            return (Criteria) this;
        }

        public Criteria andIntPayDateIsNull() {
            addCriterion("int_pay_date is null");
            return (Criteria) this;
        }

        public Criteria andIntPayDateIsNotNull() {
            addCriterion("int_pay_date is not null");
            return (Criteria) this;
        }

        public Criteria andIntPayDateEqualTo(String value) {
            addCriterion("int_pay_date =", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateNotEqualTo(String value) {
            addCriterion("int_pay_date <>", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateGreaterThan(String value) {
            addCriterion("int_pay_date >", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateGreaterThanOrEqualTo(String value) {
            addCriterion("int_pay_date >=", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateLessThan(String value) {
            addCriterion("int_pay_date <", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateLessThanOrEqualTo(String value) {
            addCriterion("int_pay_date <=", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateLike(String value) {
            addCriterion("int_pay_date like", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateNotLike(String value) {
            addCriterion("int_pay_date not like", value, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateIn(List<String> values) {
            addCriterion("int_pay_date in", values, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateNotIn(List<String> values) {
            addCriterion("int_pay_date not in", values, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateBetween(String value1, String value2) {
            addCriterion("int_pay_date between", value1, value2, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andIntPayDateNotBetween(String value1, String value2) {
            addCriterion("int_pay_date not between", value1, value2, "intPayDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNull() {
            addCriterion("end_date is null");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNotNull() {
            addCriterion("end_date is not null");
            return (Criteria) this;
        }

        public Criteria andEndDateEqualTo(String value) {
            addCriterion("end_date =", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotEqualTo(String value) {
            addCriterion("end_date <>", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThan(String value) {
            addCriterion("end_date >", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThanOrEqualTo(String value) {
            addCriterion("end_date >=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThan(String value) {
            addCriterion("end_date <", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThanOrEqualTo(String value) {
            addCriterion("end_date <=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLike(String value) {
            addCriterion("end_date like", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotLike(String value) {
            addCriterion("end_date not like", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIn(List<String> values) {
            addCriterion("end_date in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotIn(List<String> values) {
            addCriterion("end_date not in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateBetween(String value1, String value2) {
            addCriterion("end_date between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotBetween(String value1, String value2) {
            addCriterion("end_date not between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateIsNull() {
            addCriterion("expect_anual_rate is null");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateIsNotNull() {
            addCriterion("expect_anual_rate is not null");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateEqualTo(BigDecimal value) {
            addCriterion("expect_anual_rate =", value, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateNotEqualTo(BigDecimal value) {
            addCriterion("expect_anual_rate <>", value, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateGreaterThan(BigDecimal value) {
            addCriterion("expect_anual_rate >", value, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("expect_anual_rate >=", value, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateLessThan(BigDecimal value) {
            addCriterion("expect_anual_rate <", value, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("expect_anual_rate <=", value, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateIn(List<BigDecimal> values) {
            addCriterion("expect_anual_rate in", values, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateNotIn(List<BigDecimal> values) {
            addCriterion("expect_anual_rate not in", values, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expect_anual_rate between", value1, value2, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andExpectAnualRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expect_anual_rate not between", value1, value2, "expectAnualRate");
            return (Criteria) this;
        }

        public Criteria andCurrTypeIsNull() {
            addCriterion("curr_type is null");
            return (Criteria) this;
        }

        public Criteria andCurrTypeIsNotNull() {
            addCriterion("curr_type is not null");
            return (Criteria) this;
        }

        public Criteria andCurrTypeEqualTo(String value) {
            addCriterion("curr_type =", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeNotEqualTo(String value) {
            addCriterion("curr_type <>", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeGreaterThan(String value) {
            addCriterion("curr_type >", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeGreaterThanOrEqualTo(String value) {
            addCriterion("curr_type >=", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeLessThan(String value) {
            addCriterion("curr_type <", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeLessThanOrEqualTo(String value) {
            addCriterion("curr_type <=", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeLike(String value) {
            addCriterion("curr_type like", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeNotLike(String value) {
            addCriterion("curr_type not like", value, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeIn(List<String> values) {
            addCriterion("curr_type in", values, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeNotIn(List<String> values) {
            addCriterion("curr_type not in", values, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeBetween(String value1, String value2) {
            addCriterion("curr_type between", value1, value2, "currType");
            return (Criteria) this;
        }

        public Criteria andCurrTypeNotBetween(String value1, String value2) {
            addCriterion("curr_type not between", value1, value2, "currType");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andReversIsNull() {
            addCriterion("revers is null");
            return (Criteria) this;
        }

        public Criteria andReversIsNotNull() {
            addCriterion("revers is not null");
            return (Criteria) this;
        }

        public Criteria andReversEqualTo(String value) {
            addCriterion("revers =", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversNotEqualTo(String value) {
            addCriterion("revers <>", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversGreaterThan(String value) {
            addCriterion("revers >", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversGreaterThanOrEqualTo(String value) {
            addCriterion("revers >=", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversLessThan(String value) {
            addCriterion("revers <", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversLessThanOrEqualTo(String value) {
            addCriterion("revers <=", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversLike(String value) {
            addCriterion("revers like", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversNotLike(String value) {
            addCriterion("revers not like", value, "revers");
            return (Criteria) this;
        }

        public Criteria andReversIn(List<String> values) {
            addCriterion("revers in", values, "revers");
            return (Criteria) this;
        }

        public Criteria andReversNotIn(List<String> values) {
            addCriterion("revers not in", values, "revers");
            return (Criteria) this;
        }

        public Criteria andReversBetween(String value1, String value2) {
            addCriterion("revers between", value1, value2, "revers");
            return (Criteria) this;
        }

        public Criteria andReversNotBetween(String value1, String value2) {
            addCriterion("revers not between", value1, value2, "revers");
            return (Criteria) this;
        }

        public Criteria andRetCodeIsNull() {
            addCriterion("ret_code is null");
            return (Criteria) this;
        }

        public Criteria andRetCodeIsNotNull() {
            addCriterion("ret_code is not null");
            return (Criteria) this;
        }

        public Criteria andRetCodeEqualTo(String value) {
            addCriterion("ret_code =", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeNotEqualTo(String value) {
            addCriterion("ret_code <>", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeGreaterThan(String value) {
            addCriterion("ret_code >", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ret_code >=", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeLessThan(String value) {
            addCriterion("ret_code <", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeLessThanOrEqualTo(String value) {
            addCriterion("ret_code <=", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeLike(String value) {
            addCriterion("ret_code like", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeNotLike(String value) {
            addCriterion("ret_code not like", value, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeIn(List<String> values) {
            addCriterion("ret_code in", values, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeNotIn(List<String> values) {
            addCriterion("ret_code not in", values, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeBetween(String value1, String value2) {
            addCriterion("ret_code between", value1, value2, "retCode");
            return (Criteria) this;
        }

        public Criteria andRetCodeNotBetween(String value1, String value2) {
            addCriterion("ret_code not between", value1, value2, "retCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeIsNull() {
            addCriterion("auth_code is null");
            return (Criteria) this;
        }

        public Criteria andAuthCodeIsNotNull() {
            addCriterion("auth_code is not null");
            return (Criteria) this;
        }

        public Criteria andAuthCodeEqualTo(String value) {
            addCriterion("auth_code =", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeNotEqualTo(String value) {
            addCriterion("auth_code <>", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeGreaterThan(String value) {
            addCriterion("auth_code >", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeGreaterThanOrEqualTo(String value) {
            addCriterion("auth_code >=", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeLessThan(String value) {
            addCriterion("auth_code <", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeLessThanOrEqualTo(String value) {
            addCriterion("auth_code <=", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeLike(String value) {
            addCriterion("auth_code like", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeNotLike(String value) {
            addCriterion("auth_code not like", value, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeIn(List<String> values) {
            addCriterion("auth_code in", values, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeNotIn(List<String> values) {
            addCriterion("auth_code not in", values, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeBetween(String value1, String value2) {
            addCriterion("auth_code between", value1, value2, "authCode");
            return (Criteria) this;
        }

        public Criteria andAuthCodeNotBetween(String value1, String value2) {
            addCriterion("auth_code not between", value1, value2, "authCode");
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

        public Criteria andTenderTypeIsNull() {
            addCriterion("tender_type is null");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIsNotNull() {
            addCriterion("tender_type is not null");
            return (Criteria) this;
        }

        public Criteria andTenderTypeEqualTo(Integer value) {
            addCriterion("tender_type =", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotEqualTo(Integer value) {
            addCriterion("tender_type <>", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeGreaterThan(Integer value) {
            addCriterion("tender_type >", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_type >=", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeLessThan(Integer value) {
            addCriterion("tender_type <", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeLessThanOrEqualTo(Integer value) {
            addCriterion("tender_type <=", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIn(List<Integer> values) {
            addCriterion("tender_type in", values, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotIn(List<Integer> values) {
            addCriterion("tender_type not in", values, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeBetween(Integer value1, Integer value2) {
            addCriterion("tender_type between", value1, value2, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_type not between", value1, value2, "tenderType");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("order_id like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("order_id not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
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