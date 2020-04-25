package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class PrizeListExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public PrizeListExample() {
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

        public Criteria andPrizeSelfCodeIsNull() {
            addCriterion("prize_self_code is null");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeIsNotNull() {
            addCriterion("prize_self_code is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeEqualTo(String value) {
            addCriterion("prize_self_code =", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeNotEqualTo(String value) {
            addCriterion("prize_self_code <>", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeGreaterThan(String value) {
            addCriterion("prize_self_code >", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeGreaterThanOrEqualTo(String value) {
            addCriterion("prize_self_code >=", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeLessThan(String value) {
            addCriterion("prize_self_code <", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeLessThanOrEqualTo(String value) {
            addCriterion("prize_self_code <=", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeLike(String value) {
            addCriterion("prize_self_code like", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeNotLike(String value) {
            addCriterion("prize_self_code not like", value, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeIn(List<String> values) {
            addCriterion("prize_self_code in", values, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeNotIn(List<String> values) {
            addCriterion("prize_self_code not in", values, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeBetween(String value1, String value2) {
            addCriterion("prize_self_code between", value1, value2, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeSelfCodeNotBetween(String value1, String value2) {
            addCriterion("prize_self_code not between", value1, value2, "prizeSelfCode");
            return (Criteria) this;
        }

        public Criteria andPrizeNameIsNull() {
            addCriterion("prize_name is null");
            return (Criteria) this;
        }

        public Criteria andPrizeNameIsNotNull() {
            addCriterion("prize_name is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeNameEqualTo(String value) {
            addCriterion("prize_name =", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotEqualTo(String value) {
            addCriterion("prize_name <>", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameGreaterThan(String value) {
            addCriterion("prize_name >", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameGreaterThanOrEqualTo(String value) {
            addCriterion("prize_name >=", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameLessThan(String value) {
            addCriterion("prize_name <", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameLessThanOrEqualTo(String value) {
            addCriterion("prize_name <=", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameLike(String value) {
            addCriterion("prize_name like", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotLike(String value) {
            addCriterion("prize_name not like", value, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameIn(List<String> values) {
            addCriterion("prize_name in", values, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotIn(List<String> values) {
            addCriterion("prize_name not in", values, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameBetween(String value1, String value2) {
            addCriterion("prize_name between", value1, value2, "prizeName");
            return (Criteria) this;
        }

        public Criteria andPrizeNameNotBetween(String value1, String value2) {
            addCriterion("prize_name not between", value1, value2, "prizeName");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountIsNull() {
            addCriterion("all_person_count is null");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountIsNotNull() {
            addCriterion("all_person_count is not null");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountEqualTo(Integer value) {
            addCriterion("all_person_count =", value, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountNotEqualTo(Integer value) {
            addCriterion("all_person_count <>", value, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountGreaterThan(Integer value) {
            addCriterion("all_person_count >", value, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("all_person_count >=", value, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountLessThan(Integer value) {
            addCriterion("all_person_count <", value, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountLessThanOrEqualTo(Integer value) {
            addCriterion("all_person_count <=", value, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountIn(List<Integer> values) {
            addCriterion("all_person_count in", values, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountNotIn(List<Integer> values) {
            addCriterion("all_person_count not in", values, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountBetween(Integer value1, Integer value2) {
            addCriterion("all_person_count between", value1, value2, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andAllPersonCountNotBetween(Integer value1, Integer value2) {
            addCriterion("all_person_count not between", value1, value2, "allPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountIsNull() {
            addCriterion("joined_person_count is null");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountIsNotNull() {
            addCriterion("joined_person_count is not null");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountEqualTo(Integer value) {
            addCriterion("joined_person_count =", value, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountNotEqualTo(Integer value) {
            addCriterion("joined_person_count <>", value, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountGreaterThan(Integer value) {
            addCriterion("joined_person_count >", value, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("joined_person_count >=", value, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountLessThan(Integer value) {
            addCriterion("joined_person_count <", value, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountLessThanOrEqualTo(Integer value) {
            addCriterion("joined_person_count <=", value, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountIn(List<Integer> values) {
            addCriterion("joined_person_count in", values, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountNotIn(List<Integer> values) {
            addCriterion("joined_person_count not in", values, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountBetween(Integer value1, Integer value2) {
            addCriterion("joined_person_count between", value1, value2, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andJoinedPersonCountNotBetween(Integer value1, Integer value2) {
            addCriterion("joined_person_count not between", value1, value2, "joinedPersonCount");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusIsNull() {
            addCriterion("prize_status is null");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusIsNotNull() {
            addCriterion("prize_status is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusEqualTo(Integer value) {
            addCriterion("prize_status =", value, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusNotEqualTo(Integer value) {
            addCriterion("prize_status <>", value, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusGreaterThan(Integer value) {
            addCriterion("prize_status >", value, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_status >=", value, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusLessThan(Integer value) {
            addCriterion("prize_status <", value, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("prize_status <=", value, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusIn(List<Integer> values) {
            addCriterion("prize_status in", values, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusNotIn(List<Integer> values) {
            addCriterion("prize_status not in", values, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusBetween(Integer value1, Integer value2) {
            addCriterion("prize_status between", value1, value2, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_status not between", value1, value2, "prizeStatus");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityIsNull() {
            addCriterion("prize_quantity is null");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityIsNotNull() {
            addCriterion("prize_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityEqualTo(Integer value) {
            addCriterion("prize_quantity =", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityNotEqualTo(Integer value) {
            addCriterion("prize_quantity <>", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityGreaterThan(Integer value) {
            addCriterion("prize_quantity >", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_quantity >=", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityLessThan(Integer value) {
            addCriterion("prize_quantity <", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("prize_quantity <=", value, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityIn(List<Integer> values) {
            addCriterion("prize_quantity in", values, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityNotIn(List<Integer> values) {
            addCriterion("prize_quantity not in", values, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityBetween(Integer value1, Integer value2) {
            addCriterion("prize_quantity between", value1, value2, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_quantity not between", value1, value2, "prizeQuantity");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeIsNull() {
            addCriterion("prize_code is null");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeIsNotNull() {
            addCriterion("prize_code is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeEqualTo(String value) {
            addCriterion("prize_code =", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotEqualTo(String value) {
            addCriterion("prize_code <>", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeGreaterThan(String value) {
            addCriterion("prize_code >", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("prize_code >=", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeLessThan(String value) {
            addCriterion("prize_code <", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeLessThanOrEqualTo(String value) {
            addCriterion("prize_code <=", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeLike(String value) {
            addCriterion("prize_code like", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotLike(String value) {
            addCriterion("prize_code not like", value, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeIn(List<String> values) {
            addCriterion("prize_code in", values, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotIn(List<String> values) {
            addCriterion("prize_code not in", values, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeBetween(String value1, String value2) {
            addCriterion("prize_code between", value1, value2, "prizeCode");
            return (Criteria) this;
        }

        public Criteria andPrizeCodeNotBetween(String value1, String value2) {
            addCriterion("prize_code not between", value1, value2, "prizeCode");
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

        public Criteria andAddUserIsNull() {
            addCriterion("add_user is null");
            return (Criteria) this;
        }

        public Criteria andAddUserIsNotNull() {
            addCriterion("add_user is not null");
            return (Criteria) this;
        }

        public Criteria andAddUserEqualTo(String value) {
            addCriterion("add_user =", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserNotEqualTo(String value) {
            addCriterion("add_user <>", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserGreaterThan(String value) {
            addCriterion("add_user >", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserGreaterThanOrEqualTo(String value) {
            addCriterion("add_user >=", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserLessThan(String value) {
            addCriterion("add_user <", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserLessThanOrEqualTo(String value) {
            addCriterion("add_user <=", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserLike(String value) {
            addCriterion("add_user like", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserNotLike(String value) {
            addCriterion("add_user not like", value, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserIn(List<String> values) {
            addCriterion("add_user in", values, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserNotIn(List<String> values) {
            addCriterion("add_user not in", values, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserBetween(String value1, String value2) {
            addCriterion("add_user between", value1, value2, "addUser");
            return (Criteria) this;
        }

        public Criteria andAddUserNotBetween(String value1, String value2) {
            addCriterion("add_user not between", value1, value2, "addUser");
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