package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountAccurateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public AccountAccurateExample() {
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

        public Criteria andFrostCashIsNull() {
            addCriterion("frost_cash is null");
            return (Criteria) this;
        }

        public Criteria andFrostCashIsNotNull() {
            addCriterion("frost_cash is not null");
            return (Criteria) this;
        }

        public Criteria andFrostCashEqualTo(BigDecimal value) {
            addCriterion("frost_cash =", value, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashNotEqualTo(BigDecimal value) {
            addCriterion("frost_cash <>", value, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashGreaterThan(BigDecimal value) {
            addCriterion("frost_cash >", value, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("frost_cash >=", value, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashLessThan(BigDecimal value) {
            addCriterion("frost_cash <", value, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashLessThanOrEqualTo(BigDecimal value) {
            addCriterion("frost_cash <=", value, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashIn(List<BigDecimal> values) {
            addCriterion("frost_cash in", values, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashNotIn(List<BigDecimal> values) {
            addCriterion("frost_cash not in", values, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost_cash between", value1, value2, "frostCash");
            return (Criteria) this;
        }

        public Criteria andFrostCashNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("frost_cash not between", value1, value2, "frostCash");
            return (Criteria) this;
        }

        public Criteria andIsUpdateIsNull() {
            addCriterion("is_update is null");
            return (Criteria) this;
        }

        public Criteria andIsUpdateIsNotNull() {
            addCriterion("is_update is not null");
            return (Criteria) this;
        }

        public Criteria andIsUpdateEqualTo(Integer value) {
            addCriterion("is_update =", value, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateNotEqualTo(Integer value) {
            addCriterion("is_update <>", value, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateGreaterThan(Integer value) {
            addCriterion("is_update >", value, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_update >=", value, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateLessThan(Integer value) {
            addCriterion("is_update <", value, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateLessThanOrEqualTo(Integer value) {
            addCriterion("is_update <=", value, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateIn(List<Integer> values) {
            addCriterion("is_update in", values, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateNotIn(List<Integer> values) {
            addCriterion("is_update not in", values, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateBetween(Integer value1, Integer value2) {
            addCriterion("is_update between", value1, value2, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsUpdateNotBetween(Integer value1, Integer value2) {
            addCriterion("is_update not between", value1, value2, "isUpdate");
            return (Criteria) this;
        }

        public Criteria andIsokIsNull() {
            addCriterion("isok is null");
            return (Criteria) this;
        }

        public Criteria andIsokIsNotNull() {
            addCriterion("isok is not null");
            return (Criteria) this;
        }

        public Criteria andIsokEqualTo(Boolean value) {
            addCriterion("isok =", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotEqualTo(Boolean value) {
            addCriterion("isok <>", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokGreaterThan(Boolean value) {
            addCriterion("isok >", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isok >=", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokLessThan(Boolean value) {
            addCriterion("isok <", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokLessThanOrEqualTo(Boolean value) {
            addCriterion("isok <=", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokIn(List<Boolean> values) {
            addCriterion("isok in", values, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotIn(List<Boolean> values) {
            addCriterion("isok not in", values, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokBetween(Boolean value1, Boolean value2) {
            addCriterion("isok between", value1, value2, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isok not between", value1, value2, "isok");
            return (Criteria) this;
        }

        public Criteria andRecMoneyIsNull() {
            addCriterion("rec_money is null");
            return (Criteria) this;
        }

        public Criteria andRecMoneyIsNotNull() {
            addCriterion("rec_money is not null");
            return (Criteria) this;
        }

        public Criteria andRecMoneyEqualTo(BigDecimal value) {
            addCriterion("rec_money =", value, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyNotEqualTo(BigDecimal value) {
            addCriterion("rec_money <>", value, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyGreaterThan(BigDecimal value) {
            addCriterion("rec_money >", value, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rec_money >=", value, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyLessThan(BigDecimal value) {
            addCriterion("rec_money <", value, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rec_money <=", value, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyIn(List<BigDecimal> values) {
            addCriterion("rec_money in", values, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyNotIn(List<BigDecimal> values) {
            addCriterion("rec_money not in", values, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rec_money between", value1, value2, "recMoney");
            return (Criteria) this;
        }

        public Criteria andRecMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rec_money not between", value1, value2, "recMoney");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(BigDecimal value) {
            addCriterion("fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(BigDecimal value) {
            addCriterion("fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(BigDecimal value) {
            addCriterion("fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(BigDecimal value) {
            addCriterion("fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<BigDecimal> values) {
            addCriterion("fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<BigDecimal> values) {
            addCriterion("fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee not between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andInMoneyIsNull() {
            addCriterion("in_money is null");
            return (Criteria) this;
        }

        public Criteria andInMoneyIsNotNull() {
            addCriterion("in_money is not null");
            return (Criteria) this;
        }

        public Criteria andInMoneyEqualTo(BigDecimal value) {
            addCriterion("in_money =", value, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyNotEqualTo(BigDecimal value) {
            addCriterion("in_money <>", value, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyGreaterThan(BigDecimal value) {
            addCriterion("in_money >", value, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("in_money >=", value, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyLessThan(BigDecimal value) {
            addCriterion("in_money <", value, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("in_money <=", value, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyIn(List<BigDecimal> values) {
            addCriterion("in_money in", values, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyNotIn(List<BigDecimal> values) {
            addCriterion("in_money not in", values, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("in_money between", value1, value2, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("in_money not between", value1, value2, "inMoney");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagIsNull() {
            addCriterion("in_money_flag is null");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagIsNotNull() {
            addCriterion("in_money_flag is not null");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagEqualTo(Integer value) {
            addCriterion("in_money_flag =", value, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagNotEqualTo(Integer value) {
            addCriterion("in_money_flag <>", value, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagGreaterThan(Integer value) {
            addCriterion("in_money_flag >", value, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("in_money_flag >=", value, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagLessThan(Integer value) {
            addCriterion("in_money_flag <", value, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagLessThanOrEqualTo(Integer value) {
            addCriterion("in_money_flag <=", value, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagIn(List<Integer> values) {
            addCriterion("in_money_flag in", values, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagNotIn(List<Integer> values) {
            addCriterion("in_money_flag not in", values, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagBetween(Integer value1, Integer value2) {
            addCriterion("in_money_flag between", value1, value2, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andInMoneyFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("in_money_flag not between", value1, value2, "inMoneyFlag");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestIsNull() {
            addCriterion("recover_interest is null");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestIsNotNull() {
            addCriterion("recover_interest is not null");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestEqualTo(BigDecimal value) {
            addCriterion("recover_interest =", value, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestNotEqualTo(BigDecimal value) {
            addCriterion("recover_interest <>", value, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestGreaterThan(BigDecimal value) {
            addCriterion("recover_interest >", value, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("recover_interest >=", value, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestLessThan(BigDecimal value) {
            addCriterion("recover_interest <", value, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestLessThanOrEqualTo(BigDecimal value) {
            addCriterion("recover_interest <=", value, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestIn(List<BigDecimal> values) {
            addCriterion("recover_interest in", values, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestNotIn(List<BigDecimal> values) {
            addCriterion("recover_interest not in", values, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recover_interest between", value1, value2, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andRecoverInterestNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recover_interest not between", value1, value2, "recoverInterest");
            return (Criteria) this;
        }

        public Criteria andInvestTotalIsNull() {
            addCriterion("invest_total is null");
            return (Criteria) this;
        }

        public Criteria andInvestTotalIsNotNull() {
            addCriterion("invest_total is not null");
            return (Criteria) this;
        }

        public Criteria andInvestTotalEqualTo(BigDecimal value) {
            addCriterion("invest_total =", value, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalNotEqualTo(BigDecimal value) {
            addCriterion("invest_total <>", value, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalGreaterThan(BigDecimal value) {
            addCriterion("invest_total >", value, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("invest_total >=", value, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalLessThan(BigDecimal value) {
            addCriterion("invest_total <", value, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("invest_total <=", value, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalIn(List<BigDecimal> values) {
            addCriterion("invest_total in", values, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalNotIn(List<BigDecimal> values) {
            addCriterion("invest_total not in", values, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("invest_total between", value1, value2, "investTotal");
            return (Criteria) this;
        }

        public Criteria andInvestTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("invest_total not between", value1, value2, "investTotal");
            return (Criteria) this;
        }

        public Criteria andWaitInterestIsNull() {
            addCriterion("wait_interest is null");
            return (Criteria) this;
        }

        public Criteria andWaitInterestIsNotNull() {
            addCriterion("wait_interest is not null");
            return (Criteria) this;
        }

        public Criteria andWaitInterestEqualTo(BigDecimal value) {
            addCriterion("wait_interest =", value, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestNotEqualTo(BigDecimal value) {
            addCriterion("wait_interest <>", value, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestGreaterThan(BigDecimal value) {
            addCriterion("wait_interest >", value, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("wait_interest >=", value, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestLessThan(BigDecimal value) {
            addCriterion("wait_interest <", value, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestLessThanOrEqualTo(BigDecimal value) {
            addCriterion("wait_interest <=", value, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestIn(List<BigDecimal> values) {
            addCriterion("wait_interest in", values, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestNotIn(List<BigDecimal> values) {
            addCriterion("wait_interest not in", values, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("wait_interest between", value1, value2, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitInterestNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("wait_interest not between", value1, value2, "waitInterest");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalIsNull() {
            addCriterion("wait_capital is null");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalIsNotNull() {
            addCriterion("wait_capital is not null");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalEqualTo(BigDecimal value) {
            addCriterion("wait_capital =", value, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalNotEqualTo(BigDecimal value) {
            addCriterion("wait_capital <>", value, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalGreaterThan(BigDecimal value) {
            addCriterion("wait_capital >", value, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("wait_capital >=", value, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalLessThan(BigDecimal value) {
            addCriterion("wait_capital <", value, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("wait_capital <=", value, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalIn(List<BigDecimal> values) {
            addCriterion("wait_capital in", values, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalNotIn(List<BigDecimal> values) {
            addCriterion("wait_capital not in", values, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("wait_capital between", value1, value2, "waitCapital");
            return (Criteria) this;
        }

        public Criteria andWaitCapitalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("wait_capital not between", value1, value2, "waitCapital");
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