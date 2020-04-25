package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WithdrawalsTimeConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public WithdrawalsTimeConfigExample() {
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

        public Criteria andIfWorkingdayIsNull() {
            addCriterion("if_workingday is null");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayIsNotNull() {
            addCriterion("if_workingday is not null");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayEqualTo(String value) {
            addCriterion("if_workingday =", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayNotEqualTo(String value) {
            addCriterion("if_workingday <>", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayGreaterThan(String value) {
            addCriterion("if_workingday >", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayGreaterThanOrEqualTo(String value) {
            addCriterion("if_workingday >=", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayLessThan(String value) {
            addCriterion("if_workingday <", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayLessThanOrEqualTo(String value) {
            addCriterion("if_workingday <=", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayLike(String value) {
            addCriterion("if_workingday like", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayNotLike(String value) {
            addCriterion("if_workingday not like", value, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayIn(List<String> values) {
            addCriterion("if_workingday in", values, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayNotIn(List<String> values) {
            addCriterion("if_workingday not in", values, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayBetween(String value1, String value2) {
            addCriterion("if_workingday between", value1, value2, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andIfWorkingdayNotBetween(String value1, String value2) {
            addCriterion("if_workingday not between", value1, value2, "ifWorkingday");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartIsNull() {
            addCriterion("withdrawals_start is null");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartIsNotNull() {
            addCriterion("withdrawals_start is not null");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartEqualTo(String value) {
            addCriterion("withdrawals_start =", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartNotEqualTo(String value) {
            addCriterion("withdrawals_start <>", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartGreaterThan(String value) {
            addCriterion("withdrawals_start >", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartGreaterThanOrEqualTo(String value) {
            addCriterion("withdrawals_start >=", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartLessThan(String value) {
            addCriterion("withdrawals_start <", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartLessThanOrEqualTo(String value) {
            addCriterion("withdrawals_start <=", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartLike(String value) {
            addCriterion("withdrawals_start like", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartNotLike(String value) {
            addCriterion("withdrawals_start not like", value, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartIn(List<String> values) {
            addCriterion("withdrawals_start in", values, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartNotIn(List<String> values) {
            addCriterion("withdrawals_start not in", values, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartBetween(String value1, String value2) {
            addCriterion("withdrawals_start between", value1, value2, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsStartNotBetween(String value1, String value2) {
            addCriterion("withdrawals_start not between", value1, value2, "withdrawalsStart");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndIsNull() {
            addCriterion("withdrawals_end is null");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndIsNotNull() {
            addCriterion("withdrawals_end is not null");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndEqualTo(String value) {
            addCriterion("withdrawals_end =", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndNotEqualTo(String value) {
            addCriterion("withdrawals_end <>", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndGreaterThan(String value) {
            addCriterion("withdrawals_end >", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndGreaterThanOrEqualTo(String value) {
            addCriterion("withdrawals_end >=", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndLessThan(String value) {
            addCriterion("withdrawals_end <", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndLessThanOrEqualTo(String value) {
            addCriterion("withdrawals_end <=", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndLike(String value) {
            addCriterion("withdrawals_end like", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndNotLike(String value) {
            addCriterion("withdrawals_end not like", value, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndIn(List<String> values) {
            addCriterion("withdrawals_end in", values, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndNotIn(List<String> values) {
            addCriterion("withdrawals_end not in", values, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndBetween(String value1, String value2) {
            addCriterion("withdrawals_end between", value1, value2, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andWithdrawalsEndNotBetween(String value1, String value2) {
            addCriterion("withdrawals_end not between", value1, value2, "withdrawalsEnd");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawIsNull() {
            addCriterion("immediately_withdraw is null");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawIsNotNull() {
            addCriterion("immediately_withdraw is not null");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawEqualTo(String value) {
            addCriterion("immediately_withdraw =", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawNotEqualTo(String value) {
            addCriterion("immediately_withdraw <>", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawGreaterThan(String value) {
            addCriterion("immediately_withdraw >", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawGreaterThanOrEqualTo(String value) {
            addCriterion("immediately_withdraw >=", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawLessThan(String value) {
            addCriterion("immediately_withdraw <", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawLessThanOrEqualTo(String value) {
            addCriterion("immediately_withdraw <=", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawLike(String value) {
            addCriterion("immediately_withdraw like", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawNotLike(String value) {
            addCriterion("immediately_withdraw not like", value, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawIn(List<String> values) {
            addCriterion("immediately_withdraw in", values, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawNotIn(List<String> values) {
            addCriterion("immediately_withdraw not in", values, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawBetween(String value1, String value2) {
            addCriterion("immediately_withdraw between", value1, value2, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andImmediatelyWithdrawNotBetween(String value1, String value2) {
            addCriterion("immediately_withdraw not between", value1, value2, "immediatelyWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawIsNull() {
            addCriterion("quick_withdraw is null");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawIsNotNull() {
            addCriterion("quick_withdraw is not null");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawEqualTo(String value) {
            addCriterion("quick_withdraw =", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawNotEqualTo(String value) {
            addCriterion("quick_withdraw <>", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawGreaterThan(String value) {
            addCriterion("quick_withdraw >", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawGreaterThanOrEqualTo(String value) {
            addCriterion("quick_withdraw >=", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawLessThan(String value) {
            addCriterion("quick_withdraw <", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawLessThanOrEqualTo(String value) {
            addCriterion("quick_withdraw <=", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawLike(String value) {
            addCriterion("quick_withdraw like", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawNotLike(String value) {
            addCriterion("quick_withdraw not like", value, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawIn(List<String> values) {
            addCriterion("quick_withdraw in", values, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawNotIn(List<String> values) {
            addCriterion("quick_withdraw not in", values, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawBetween(String value1, String value2) {
            addCriterion("quick_withdraw between", value1, value2, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andQuickWithdrawNotBetween(String value1, String value2) {
            addCriterion("quick_withdraw not between", value1, value2, "quickWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawIsNull() {
            addCriterion("normal_withdraw is null");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawIsNotNull() {
            addCriterion("normal_withdraw is not null");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawEqualTo(String value) {
            addCriterion("normal_withdraw =", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawNotEqualTo(String value) {
            addCriterion("normal_withdraw <>", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawGreaterThan(String value) {
            addCriterion("normal_withdraw >", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawGreaterThanOrEqualTo(String value) {
            addCriterion("normal_withdraw >=", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawLessThan(String value) {
            addCriterion("normal_withdraw <", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawLessThanOrEqualTo(String value) {
            addCriterion("normal_withdraw <=", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawLike(String value) {
            addCriterion("normal_withdraw like", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawNotLike(String value) {
            addCriterion("normal_withdraw not like", value, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawIn(List<String> values) {
            addCriterion("normal_withdraw in", values, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawNotIn(List<String> values) {
            addCriterion("normal_withdraw not in", values, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawBetween(String value1, String value2) {
            addCriterion("normal_withdraw between", value1, value2, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andNormalWithdrawNotBetween(String value1, String value2) {
            addCriterion("normal_withdraw not between", value1, value2, "normalWithdraw");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreateuserIsNull() {
            addCriterion("`createuser` is null");
            return (Criteria) this;
        }

        public Criteria andCreateuserIsNotNull() {
            addCriterion("`createuser` is not null");
            return (Criteria) this;
        }

        public Criteria andCreateuserEqualTo(String value) {
            addCriterion("`createuser` =", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserNotEqualTo(String value) {
            addCriterion("`createuser` <>", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserGreaterThan(String value) {
            addCriterion("`createuser` >", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserGreaterThanOrEqualTo(String value) {
            addCriterion("`createuser` >=", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserLessThan(String value) {
            addCriterion("`createuser` <", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserLessThanOrEqualTo(String value) {
            addCriterion("`createuser` <=", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserLike(String value) {
            addCriterion("`createuser` like", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserNotLike(String value) {
            addCriterion("`createuser` not like", value, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserIn(List<String> values) {
            addCriterion("`createuser` in", values, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserNotIn(List<String> values) {
            addCriterion("`createuser` not in", values, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserBetween(String value1, String value2) {
            addCriterion("`createuser` between", value1, value2, "createuser");
            return (Criteria) this;
        }

        public Criteria andCreateuserNotBetween(String value1, String value2) {
            addCriterion("`createuser` not between", value1, value2, "createuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserIsNull() {
            addCriterion("updateuser is null");
            return (Criteria) this;
        }

        public Criteria andUpdateuserIsNotNull() {
            addCriterion("updateuser is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateuserEqualTo(String value) {
            addCriterion("updateuser =", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserNotEqualTo(String value) {
            addCriterion("updateuser <>", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserGreaterThan(String value) {
            addCriterion("updateuser >", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserGreaterThanOrEqualTo(String value) {
            addCriterion("updateuser >=", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserLessThan(String value) {
            addCriterion("updateuser <", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserLessThanOrEqualTo(String value) {
            addCriterion("updateuser <=", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserLike(String value) {
            addCriterion("updateuser like", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserNotLike(String value) {
            addCriterion("updateuser not like", value, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserIn(List<String> values) {
            addCriterion("updateuser in", values, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserNotIn(List<String> values) {
            addCriterion("updateuser not in", values, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserBetween(String value1, String value2) {
            addCriterion("updateuser between", value1, value2, "updateuser");
            return (Criteria) this;
        }

        public Criteria andUpdateuserNotBetween(String value1, String value2) {
            addCriterion("updateuser not between", value1, value2, "updateuser");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updatetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updatetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("updatetime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("updatetime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("updatetime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updatetime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("updatetime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("updatetime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("updatetime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("updatetime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("updatetime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("updatetime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNull() {
            addCriterion("remarks is null");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNotNull() {
            addCriterion("remarks is not null");
            return (Criteria) this;
        }

        public Criteria andRemarksEqualTo(String value) {
            addCriterion("remarks =", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotEqualTo(String value) {
            addCriterion("remarks <>", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThan(String value) {
            addCriterion("remarks >", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThanOrEqualTo(String value) {
            addCriterion("remarks >=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThan(String value) {
            addCriterion("remarks <", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThanOrEqualTo(String value) {
            addCriterion("remarks <=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLike(String value) {
            addCriterion("remarks like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotLike(String value) {
            addCriterion("remarks not like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksIn(List<String> values) {
            addCriterion("remarks in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotIn(List<String> values) {
            addCriterion("remarks not in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksBetween(String value1, String value2) {
            addCriterion("remarks between", value1, value2, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotBetween(String value1, String value2) {
            addCriterion("remarks not between", value1, value2, "remarks");
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