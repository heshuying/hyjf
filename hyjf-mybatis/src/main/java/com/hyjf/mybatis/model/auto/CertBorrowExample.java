package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class CertBorrowExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public CertBorrowExample() {
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

        public Criteria andIsUserInfoIsNull() {
            addCriterion("is_user_info is null");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoIsNotNull() {
            addCriterion("is_user_info is not null");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoEqualTo(Integer value) {
            addCriterion("is_user_info =", value, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoNotEqualTo(Integer value) {
            addCriterion("is_user_info <>", value, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoGreaterThan(Integer value) {
            addCriterion("is_user_info >", value, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_user_info >=", value, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoLessThan(Integer value) {
            addCriterion("is_user_info <", value, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoLessThanOrEqualTo(Integer value) {
            addCriterion("is_user_info <=", value, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoIn(List<Integer> values) {
            addCriterion("is_user_info in", values, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoNotIn(List<Integer> values) {
            addCriterion("is_user_info not in", values, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoBetween(Integer value1, Integer value2) {
            addCriterion("is_user_info between", value1, value2, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsUserInfoNotBetween(Integer value1, Integer value2) {
            addCriterion("is_user_info not between", value1, value2, "isUserInfo");
            return (Criteria) this;
        }

        public Criteria andIsScatterIsNull() {
            addCriterion("is_scatter is null");
            return (Criteria) this;
        }

        public Criteria andIsScatterIsNotNull() {
            addCriterion("is_scatter is not null");
            return (Criteria) this;
        }

        public Criteria andIsScatterEqualTo(Integer value) {
            addCriterion("is_scatter =", value, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterNotEqualTo(Integer value) {
            addCriterion("is_scatter <>", value, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterGreaterThan(Integer value) {
            addCriterion("is_scatter >", value, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_scatter >=", value, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterLessThan(Integer value) {
            addCriterion("is_scatter <", value, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterLessThanOrEqualTo(Integer value) {
            addCriterion("is_scatter <=", value, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterIn(List<Integer> values) {
            addCriterion("is_scatter in", values, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterNotIn(List<Integer> values) {
            addCriterion("is_scatter not in", values, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterBetween(Integer value1, Integer value2) {
            addCriterion("is_scatter between", value1, value2, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsScatterNotBetween(Integer value1, Integer value2) {
            addCriterion("is_scatter not between", value1, value2, "isScatter");
            return (Criteria) this;
        }

        public Criteria andIsStatusIsNull() {
            addCriterion("is_status is null");
            return (Criteria) this;
        }

        public Criteria andIsStatusIsNotNull() {
            addCriterion("is_status is not null");
            return (Criteria) this;
        }

        public Criteria andIsStatusEqualTo(Integer value) {
            addCriterion("is_status =", value, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusNotEqualTo(Integer value) {
            addCriterion("is_status <>", value, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusGreaterThan(Integer value) {
            addCriterion("is_status >", value, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_status >=", value, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusLessThan(Integer value) {
            addCriterion("is_status <", value, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusLessThanOrEqualTo(Integer value) {
            addCriterion("is_status <=", value, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusIn(List<Integer> values) {
            addCriterion("is_status in", values, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusNotIn(List<Integer> values) {
            addCriterion("is_status not in", values, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusBetween(Integer value1, Integer value2) {
            addCriterion("is_status between", value1, value2, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("is_status not between", value1, value2, "isStatus");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanIsNull() {
            addCriterion("is_repay_plan is null");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanIsNotNull() {
            addCriterion("is_repay_plan is not null");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanEqualTo(Integer value) {
            addCriterion("is_repay_plan =", value, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanNotEqualTo(Integer value) {
            addCriterion("is_repay_plan <>", value, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanGreaterThan(Integer value) {
            addCriterion("is_repay_plan >", value, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_repay_plan >=", value, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanLessThan(Integer value) {
            addCriterion("is_repay_plan <", value, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanLessThanOrEqualTo(Integer value) {
            addCriterion("is_repay_plan <=", value, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanIn(List<Integer> values) {
            addCriterion("is_repay_plan in", values, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanNotIn(List<Integer> values) {
            addCriterion("is_repay_plan not in", values, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanBetween(Integer value1, Integer value2) {
            addCriterion("is_repay_plan between", value1, value2, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsRepayPlanNotBetween(Integer value1, Integer value2) {
            addCriterion("is_repay_plan not between", value1, value2, "isRepayPlan");
            return (Criteria) this;
        }

        public Criteria andIsCreditIsNull() {
            addCriterion("is_credit is null");
            return (Criteria) this;
        }

        public Criteria andIsCreditIsNotNull() {
            addCriterion("is_credit is not null");
            return (Criteria) this;
        }

        public Criteria andIsCreditEqualTo(Integer value) {
            addCriterion("is_credit =", value, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditNotEqualTo(Integer value) {
            addCriterion("is_credit <>", value, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditGreaterThan(Integer value) {
            addCriterion("is_credit >", value, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_credit >=", value, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditLessThan(Integer value) {
            addCriterion("is_credit <", value, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditLessThanOrEqualTo(Integer value) {
            addCriterion("is_credit <=", value, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditIn(List<Integer> values) {
            addCriterion("is_credit in", values, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditNotIn(List<Integer> values) {
            addCriterion("is_credit not in", values, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditBetween(Integer value1, Integer value2) {
            addCriterion("is_credit between", value1, value2, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsCreditNotBetween(Integer value1, Integer value2) {
            addCriterion("is_credit not between", value1, value2, "isCredit");
            return (Criteria) this;
        }

        public Criteria andIsTransferIsNull() {
            addCriterion("is_transfer is null");
            return (Criteria) this;
        }

        public Criteria andIsTransferIsNotNull() {
            addCriterion("is_transfer is not null");
            return (Criteria) this;
        }

        public Criteria andIsTransferEqualTo(Integer value) {
            addCriterion("is_transfer =", value, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferNotEqualTo(Integer value) {
            addCriterion("is_transfer <>", value, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferGreaterThan(Integer value) {
            addCriterion("is_transfer >", value, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_transfer >=", value, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferLessThan(Integer value) {
            addCriterion("is_transfer <", value, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferLessThanOrEqualTo(Integer value) {
            addCriterion("is_transfer <=", value, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferIn(List<Integer> values) {
            addCriterion("is_transfer in", values, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferNotIn(List<Integer> values) {
            addCriterion("is_transfer not in", values, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferBetween(Integer value1, Integer value2) {
            addCriterion("is_transfer between", value1, value2, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferNotBetween(Integer value1, Integer value2) {
            addCriterion("is_transfer not between", value1, value2, "isTransfer");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusIsNull() {
            addCriterion("is_transfer_status is null");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusIsNotNull() {
            addCriterion("is_transfer_status is not null");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusEqualTo(Integer value) {
            addCriterion("is_transfer_status =", value, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusNotEqualTo(Integer value) {
            addCriterion("is_transfer_status <>", value, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusGreaterThan(Integer value) {
            addCriterion("is_transfer_status >", value, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_transfer_status >=", value, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusLessThan(Integer value) {
            addCriterion("is_transfer_status <", value, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusLessThanOrEqualTo(Integer value) {
            addCriterion("is_transfer_status <=", value, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusIn(List<Integer> values) {
            addCriterion("is_transfer_status in", values, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusNotIn(List<Integer> values) {
            addCriterion("is_transfer_status not in", values, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusBetween(Integer value1, Integer value2) {
            addCriterion("is_transfer_status between", value1, value2, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsTransferStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("is_transfer_status not between", value1, value2, "isTransferStatus");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeIsNull() {
            addCriterion("is_underTake is null");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeIsNotNull() {
            addCriterion("is_underTake is not null");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeEqualTo(Integer value) {
            addCriterion("is_underTake =", value, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeNotEqualTo(Integer value) {
            addCriterion("is_underTake <>", value, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeGreaterThan(Integer value) {
            addCriterion("is_underTake >", value, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_underTake >=", value, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeLessThan(Integer value) {
            addCriterion("is_underTake <", value, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeLessThanOrEqualTo(Integer value) {
            addCriterion("is_underTake <=", value, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeIn(List<Integer> values) {
            addCriterion("is_underTake in", values, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeNotIn(List<Integer> values) {
            addCriterion("is_underTake not in", values, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeBetween(Integer value1, Integer value2) {
            addCriterion("is_underTake between", value1, value2, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsUndertakeNotBetween(Integer value1, Integer value2) {
            addCriterion("is_underTake not between", value1, value2, "isUndertake");
            return (Criteria) this;
        }

        public Criteria andIsTransactIsNull() {
            addCriterion("is_transact is null");
            return (Criteria) this;
        }

        public Criteria andIsTransactIsNotNull() {
            addCriterion("is_transact is not null");
            return (Criteria) this;
        }

        public Criteria andIsTransactEqualTo(Integer value) {
            addCriterion("is_transact =", value, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactNotEqualTo(Integer value) {
            addCriterion("is_transact <>", value, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactGreaterThan(Integer value) {
            addCriterion("is_transact >", value, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_transact >=", value, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactLessThan(Integer value) {
            addCriterion("is_transact <", value, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactLessThanOrEqualTo(Integer value) {
            addCriterion("is_transact <=", value, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactIn(List<Integer> values) {
            addCriterion("is_transact in", values, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactNotIn(List<Integer> values) {
            addCriterion("is_transact not in", values, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactBetween(Integer value1, Integer value2) {
            addCriterion("is_transact between", value1, value2, "isTransact");
            return (Criteria) this;
        }

        public Criteria andIsTransactNotBetween(Integer value1, Integer value2) {
            addCriterion("is_transact not between", value1, value2, "isTransact");
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