package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class AccountChinapnrExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public AccountChinapnrExample() {
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

        public Criteria andChinapnrUsridIsNull() {
            addCriterion("chinapnr_usrid is null");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridIsNotNull() {
            addCriterion("chinapnr_usrid is not null");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridEqualTo(String value) {
            addCriterion("chinapnr_usrid =", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridNotEqualTo(String value) {
            addCriterion("chinapnr_usrid <>", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridGreaterThan(String value) {
            addCriterion("chinapnr_usrid >", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridGreaterThanOrEqualTo(String value) {
            addCriterion("chinapnr_usrid >=", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridLessThan(String value) {
            addCriterion("chinapnr_usrid <", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridLessThanOrEqualTo(String value) {
            addCriterion("chinapnr_usrid <=", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridLike(String value) {
            addCriterion("chinapnr_usrid like", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridNotLike(String value) {
            addCriterion("chinapnr_usrid not like", value, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridIn(List<String> values) {
            addCriterion("chinapnr_usrid in", values, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridNotIn(List<String> values) {
            addCriterion("chinapnr_usrid not in", values, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridBetween(String value1, String value2) {
            addCriterion("chinapnr_usrid between", value1, value2, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsridNotBetween(String value1, String value2) {
            addCriterion("chinapnr_usrid not between", value1, value2, "chinapnrUsrid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidIsNull() {
            addCriterion("chinapnr_usrcustid is null");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidIsNotNull() {
            addCriterion("chinapnr_usrcustid is not null");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidEqualTo(Long value) {
            addCriterion("chinapnr_usrcustid =", value, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidNotEqualTo(Long value) {
            addCriterion("chinapnr_usrcustid <>", value, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidGreaterThan(Long value) {
            addCriterion("chinapnr_usrcustid >", value, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidGreaterThanOrEqualTo(Long value) {
            addCriterion("chinapnr_usrcustid >=", value, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidLessThan(Long value) {
            addCriterion("chinapnr_usrcustid <", value, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidLessThanOrEqualTo(Long value) {
            addCriterion("chinapnr_usrcustid <=", value, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidIn(List<Long> values) {
            addCriterion("chinapnr_usrcustid in", values, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidNotIn(List<Long> values) {
            addCriterion("chinapnr_usrcustid not in", values, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidBetween(Long value1, Long value2) {
            addCriterion("chinapnr_usrcustid between", value1, value2, "chinapnrUsrcustid");
            return (Criteria) this;
        }

        public Criteria andChinapnrUsrcustidNotBetween(Long value1, Long value2) {
            addCriterion("chinapnr_usrcustid not between", value1, value2, "chinapnrUsrcustid");
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

        public Criteria andIsokIsNull() {
            addCriterion("isok is null");
            return (Criteria) this;
        }

        public Criteria andIsokIsNotNull() {
            addCriterion("isok is not null");
            return (Criteria) this;
        }

        public Criteria andIsokEqualTo(Integer value) {
            addCriterion("isok =", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotEqualTo(Integer value) {
            addCriterion("isok <>", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokGreaterThan(Integer value) {
            addCriterion("isok >", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokGreaterThanOrEqualTo(Integer value) {
            addCriterion("isok >=", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokLessThan(Integer value) {
            addCriterion("isok <", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokLessThanOrEqualTo(Integer value) {
            addCriterion("isok <=", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokIn(List<Integer> values) {
            addCriterion("isok in", values, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotIn(List<Integer> values) {
            addCriterion("isok not in", values, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokBetween(Integer value1, Integer value2) {
            addCriterion("isok between", value1, value2, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotBetween(Integer value1, Integer value2) {
            addCriterion("isok not between", value1, value2, "isok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokIsNull() {
            addCriterion("eggs_isok is null");
            return (Criteria) this;
        }

        public Criteria andEggsIsokIsNotNull() {
            addCriterion("eggs_isok is not null");
            return (Criteria) this;
        }

        public Criteria andEggsIsokEqualTo(Integer value) {
            addCriterion("eggs_isok =", value, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokNotEqualTo(Integer value) {
            addCriterion("eggs_isok <>", value, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokGreaterThan(Integer value) {
            addCriterion("eggs_isok >", value, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokGreaterThanOrEqualTo(Integer value) {
            addCriterion("eggs_isok >=", value, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokLessThan(Integer value) {
            addCriterion("eggs_isok <", value, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokLessThanOrEqualTo(Integer value) {
            addCriterion("eggs_isok <=", value, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokIn(List<Integer> values) {
            addCriterion("eggs_isok in", values, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokNotIn(List<Integer> values) {
            addCriterion("eggs_isok not in", values, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokBetween(Integer value1, Integer value2) {
            addCriterion("eggs_isok between", value1, value2, "eggsIsok");
            return (Criteria) this;
        }

        public Criteria andEggsIsokNotBetween(Integer value1, Integer value2) {
            addCriterion("eggs_isok not between", value1, value2, "eggsIsok");
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