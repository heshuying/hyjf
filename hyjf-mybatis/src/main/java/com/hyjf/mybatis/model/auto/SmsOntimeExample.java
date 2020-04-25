package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SmsOntimeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public SmsOntimeExample() {
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

        public Criteria andChannelTypeIsNull() {
            addCriterion("channel_type is null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNotNull() {
            addCriterion("channel_type is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeEqualTo(String value) {
            addCriterion("channel_type =", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotEqualTo(String value) {
            addCriterion("channel_type <>", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThan(String value) {
            addCriterion("channel_type >", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThanOrEqualTo(String value) {
            addCriterion("channel_type >=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThan(String value) {
            addCriterion("channel_type <", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThanOrEqualTo(String value) {
            addCriterion("channel_type <=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLike(String value) {
            addCriterion("channel_type like", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotLike(String value) {
            addCriterion("channel_type not like", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIn(List<String> values) {
            addCriterion("channel_type in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotIn(List<String> values) {
            addCriterion("channel_type not in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeBetween(String value1, String value2) {
            addCriterion("channel_type between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotBetween(String value1, String value2) {
            addCriterion("channel_type not between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andStarttimeIsNull() {
            addCriterion("starttime is null");
            return (Criteria) this;
        }

        public Criteria andStarttimeIsNotNull() {
            addCriterion("starttime is not null");
            return (Criteria) this;
        }

        public Criteria andStarttimeEqualTo(Integer value) {
            addCriterion("starttime =", value, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeNotEqualTo(Integer value) {
            addCriterion("starttime <>", value, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeGreaterThan(Integer value) {
            addCriterion("starttime >", value, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("starttime >=", value, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeLessThan(Integer value) {
            addCriterion("starttime <", value, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeLessThanOrEqualTo(Integer value) {
            addCriterion("starttime <=", value, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeIn(List<Integer> values) {
            addCriterion("starttime in", values, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeNotIn(List<Integer> values) {
            addCriterion("starttime not in", values, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeBetween(Integer value1, Integer value2) {
            addCriterion("starttime between", value1, value2, "starttime");
            return (Criteria) this;
        }

        public Criteria andStarttimeNotBetween(Integer value1, Integer value2) {
            addCriterion("starttime not between", value1, value2, "starttime");
            return (Criteria) this;
        }

        public Criteria andEndtimeIsNull() {
            addCriterion("endtime is null");
            return (Criteria) this;
        }

        public Criteria andEndtimeIsNotNull() {
            addCriterion("endtime is not null");
            return (Criteria) this;
        }

        public Criteria andEndtimeEqualTo(Integer value) {
            addCriterion("endtime =", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotEqualTo(Integer value) {
            addCriterion("endtime <>", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeGreaterThan(Integer value) {
            addCriterion("endtime >", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("endtime >=", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeLessThan(Integer value) {
            addCriterion("endtime <", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeLessThanOrEqualTo(Integer value) {
            addCriterion("endtime <=", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeIn(List<Integer> values) {
            addCriterion("endtime in", values, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotIn(List<Integer> values) {
            addCriterion("endtime not in", values, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeBetween(Integer value1, Integer value2) {
            addCriterion("endtime between", value1, value2, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotBetween(Integer value1, Integer value2) {
            addCriterion("endtime not between", value1, value2, "endtime");
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

        public Criteria andOpenAccountIsNull() {
            addCriterion("open_account is null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountIsNotNull() {
            addCriterion("open_account is not null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountEqualTo(Integer value) {
            addCriterion("open_account =", value, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNotEqualTo(Integer value) {
            addCriterion("open_account <>", value, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountGreaterThan(Integer value) {
            addCriterion("open_account >", value, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountGreaterThanOrEqualTo(Integer value) {
            addCriterion("open_account >=", value, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountLessThan(Integer value) {
            addCriterion("open_account <", value, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountLessThanOrEqualTo(Integer value) {
            addCriterion("open_account <=", value, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountIn(List<Integer> values) {
            addCriterion("open_account in", values, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNotIn(List<Integer> values) {
            addCriterion("open_account not in", values, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBetween(Integer value1, Integer value2) {
            addCriterion("open_account between", value1, value2, "openAccount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountNotBetween(Integer value1, Integer value2) {
            addCriterion("open_account not between", value1, value2, "openAccount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountIsNull() {
            addCriterion("add_money_count is null");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountIsNotNull() {
            addCriterion("add_money_count is not null");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountEqualTo(BigDecimal value) {
            addCriterion("add_money_count =", value, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountNotEqualTo(BigDecimal value) {
            addCriterion("add_money_count <>", value, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountGreaterThan(BigDecimal value) {
            addCriterion("add_money_count >", value, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("add_money_count >=", value, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountLessThan(BigDecimal value) {
            addCriterion("add_money_count <", value, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("add_money_count <=", value, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountIn(List<BigDecimal> values) {
            addCriterion("add_money_count in", values, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountNotIn(List<BigDecimal> values) {
            addCriterion("add_money_count not in", values, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("add_money_count between", value1, value2, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddMoneyCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("add_money_count not between", value1, value2, "addMoneyCount");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginIsNull() {
            addCriterion("add_time_begin is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginIsNotNull() {
            addCriterion("add_time_begin is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginEqualTo(String value) {
            addCriterion("add_time_begin =", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginNotEqualTo(String value) {
            addCriterion("add_time_begin <>", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginGreaterThan(String value) {
            addCriterion("add_time_begin >", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginGreaterThanOrEqualTo(String value) {
            addCriterion("add_time_begin >=", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginLessThan(String value) {
            addCriterion("add_time_begin <", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginLessThanOrEqualTo(String value) {
            addCriterion("add_time_begin <=", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginLike(String value) {
            addCriterion("add_time_begin like", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginNotLike(String value) {
            addCriterion("add_time_begin not like", value, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginIn(List<String> values) {
            addCriterion("add_time_begin in", values, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginNotIn(List<String> values) {
            addCriterion("add_time_begin not in", values, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginBetween(String value1, String value2) {
            addCriterion("add_time_begin between", value1, value2, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeBeginNotBetween(String value1, String value2) {
            addCriterion("add_time_begin not between", value1, value2, "addTimeBegin");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndIsNull() {
            addCriterion("add_time_end is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndIsNotNull() {
            addCriterion("add_time_end is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndEqualTo(String value) {
            addCriterion("add_time_end =", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndNotEqualTo(String value) {
            addCriterion("add_time_end <>", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndGreaterThan(String value) {
            addCriterion("add_time_end >", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndGreaterThanOrEqualTo(String value) {
            addCriterion("add_time_end >=", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndLessThan(String value) {
            addCriterion("add_time_end <", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndLessThanOrEqualTo(String value) {
            addCriterion("add_time_end <=", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndLike(String value) {
            addCriterion("add_time_end like", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndNotLike(String value) {
            addCriterion("add_time_end not like", value, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndIn(List<String> values) {
            addCriterion("add_time_end in", values, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndNotIn(List<String> values) {
            addCriterion("add_time_end not in", values, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndBetween(String value1, String value2) {
            addCriterion("add_time_end between", value1, value2, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andAddTimeEndNotBetween(String value1, String value2) {
            addCriterion("add_time_end not between", value1, value2, "addTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginIsNull() {
            addCriterion("re_time_begin is null");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginIsNotNull() {
            addCriterion("re_time_begin is not null");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginEqualTo(String value) {
            addCriterion("re_time_begin =", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginNotEqualTo(String value) {
            addCriterion("re_time_begin <>", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginGreaterThan(String value) {
            addCriterion("re_time_begin >", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginGreaterThanOrEqualTo(String value) {
            addCriterion("re_time_begin >=", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginLessThan(String value) {
            addCriterion("re_time_begin <", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginLessThanOrEqualTo(String value) {
            addCriterion("re_time_begin <=", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginLike(String value) {
            addCriterion("re_time_begin like", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginNotLike(String value) {
            addCriterion("re_time_begin not like", value, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginIn(List<String> values) {
            addCriterion("re_time_begin in", values, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginNotIn(List<String> values) {
            addCriterion("re_time_begin not in", values, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginBetween(String value1, String value2) {
            addCriterion("re_time_begin between", value1, value2, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeBeginNotBetween(String value1, String value2) {
            addCriterion("re_time_begin not between", value1, value2, "reTimeBegin");
            return (Criteria) this;
        }

        public Criteria andReTimeEndIsNull() {
            addCriterion("re_time_end is null");
            return (Criteria) this;
        }

        public Criteria andReTimeEndIsNotNull() {
            addCriterion("re_time_end is not null");
            return (Criteria) this;
        }

        public Criteria andReTimeEndEqualTo(String value) {
            addCriterion("re_time_end =", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndNotEqualTo(String value) {
            addCriterion("re_time_end <>", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndGreaterThan(String value) {
            addCriterion("re_time_end >", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndGreaterThanOrEqualTo(String value) {
            addCriterion("re_time_end >=", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndLessThan(String value) {
            addCriterion("re_time_end <", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndLessThanOrEqualTo(String value) {
            addCriterion("re_time_end <=", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndLike(String value) {
            addCriterion("re_time_end like", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndNotLike(String value) {
            addCriterion("re_time_end not like", value, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndIn(List<String> values) {
            addCriterion("re_time_end in", values, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndNotIn(List<String> values) {
            addCriterion("re_time_end not in", values, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndBetween(String value1, String value2) {
            addCriterion("re_time_end between", value1, value2, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andReTimeEndNotBetween(String value1, String value2) {
            addCriterion("re_time_end not between", value1, value2, "reTimeEnd");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
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

        public Criteria andCreateUserIdIsNull() {
            addCriterion("create_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNotNull() {
            addCriterion("create_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdEqualTo(Integer value) {
            addCriterion("create_user_id =", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotEqualTo(Integer value) {
            addCriterion("create_user_id <>", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThan(Integer value) {
            addCriterion("create_user_id >", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_user_id >=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThan(Integer value) {
            addCriterion("create_user_id <", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("create_user_id <=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIn(List<Integer> values) {
            addCriterion("create_user_id in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotIn(List<Integer> values) {
            addCriterion("create_user_id not in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdBetween(Integer value1, Integer value2) {
            addCriterion("create_user_id between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("create_user_id not between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameIsNull() {
            addCriterion("create_user_name is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameIsNotNull() {
            addCriterion("create_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameEqualTo(String value) {
            addCriterion("create_user_name =", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotEqualTo(String value) {
            addCriterion("create_user_name <>", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameGreaterThan(String value) {
            addCriterion("create_user_name >", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("create_user_name >=", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameLessThan(String value) {
            addCriterion("create_user_name <", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameLessThanOrEqualTo(String value) {
            addCriterion("create_user_name <=", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameLike(String value) {
            addCriterion("create_user_name like", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotLike(String value) {
            addCriterion("create_user_name not like", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameIn(List<String> values) {
            addCriterion("create_user_name in", values, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotIn(List<String> values) {
            addCriterion("create_user_name not in", values, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameBetween(String value1, String value2) {
            addCriterion("create_user_name between", value1, value2, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotBetween(String value1, String value2) {
            addCriterion("create_user_name not between", value1, value2, "createUserName");
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