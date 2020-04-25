package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppChannelStatisticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public AppChannelStatisticsExample() {
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

        public Criteria andSourceIdIsNull() {
            addCriterion("source_id is null");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNotNull() {
            addCriterion("source_id is not null");
            return (Criteria) this;
        }

        public Criteria andSourceIdEqualTo(Integer value) {
            addCriterion("source_id =", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotEqualTo(Integer value) {
            addCriterion("source_id <>", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThan(Integer value) {
            addCriterion("source_id >", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("source_id >=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThan(Integer value) {
            addCriterion("source_id <", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThanOrEqualTo(Integer value) {
            addCriterion("source_id <=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdIn(List<Integer> values) {
            addCriterion("source_id in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotIn(List<Integer> values) {
            addCriterion("source_id not in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdBetween(Integer value1, Integer value2) {
            addCriterion("source_id between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotBetween(Integer value1, Integer value2) {
            addCriterion("source_id not between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNull() {
            addCriterion("channel_name is null");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNotNull() {
            addCriterion("channel_name is not null");
            return (Criteria) this;
        }

        public Criteria andChannelNameEqualTo(String value) {
            addCriterion("channel_name =", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotEqualTo(String value) {
            addCriterion("channel_name <>", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThan(String value) {
            addCriterion("channel_name >", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThanOrEqualTo(String value) {
            addCriterion("channel_name >=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThan(String value) {
            addCriterion("channel_name <", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThanOrEqualTo(String value) {
            addCriterion("channel_name <=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLike(String value) {
            addCriterion("channel_name like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotLike(String value) {
            addCriterion("channel_name not like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameIn(List<String> values) {
            addCriterion("channel_name in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotIn(List<String> values) {
            addCriterion("channel_name not in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameBetween(String value1, String value2) {
            addCriterion("channel_name between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotBetween(String value1, String value2) {
            addCriterion("channel_name not between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andVisitCountIsNull() {
            addCriterion("visit_count is null");
            return (Criteria) this;
        }

        public Criteria andVisitCountIsNotNull() {
            addCriterion("visit_count is not null");
            return (Criteria) this;
        }

        public Criteria andVisitCountEqualTo(BigDecimal value) {
            addCriterion("visit_count =", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountNotEqualTo(BigDecimal value) {
            addCriterion("visit_count <>", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountGreaterThan(BigDecimal value) {
            addCriterion("visit_count >", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("visit_count >=", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountLessThan(BigDecimal value) {
            addCriterion("visit_count <", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("visit_count <=", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountIn(List<BigDecimal> values) {
            addCriterion("visit_count in", values, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountNotIn(List<BigDecimal> values) {
            addCriterion("visit_count not in", values, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("visit_count between", value1, value2, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("visit_count not between", value1, value2, "visitCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountIsNull() {
            addCriterion("register_count is null");
            return (Criteria) this;
        }

        public Criteria andRegisterCountIsNotNull() {
            addCriterion("register_count is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterCountEqualTo(BigDecimal value) {
            addCriterion("register_count =", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountNotEqualTo(BigDecimal value) {
            addCriterion("register_count <>", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountGreaterThan(BigDecimal value) {
            addCriterion("register_count >", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("register_count >=", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountLessThan(BigDecimal value) {
            addCriterion("register_count <", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("register_count <=", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountIn(List<BigDecimal> values) {
            addCriterion("register_count in", values, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountNotIn(List<BigDecimal> values) {
            addCriterion("register_count not in", values, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("register_count between", value1, value2, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("register_count not between", value1, value2, "registerCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountIsNull() {
            addCriterion("open_account_count is null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountIsNotNull() {
            addCriterion("open_account_count is not null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountEqualTo(BigDecimal value) {
            addCriterion("open_account_count =", value, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountNotEqualTo(BigDecimal value) {
            addCriterion("open_account_count <>", value, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountGreaterThan(BigDecimal value) {
            addCriterion("open_account_count >", value, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("open_account_count >=", value, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountLessThan(BigDecimal value) {
            addCriterion("open_account_count <", value, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("open_account_count <=", value, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountIn(List<BigDecimal> values) {
            addCriterion("open_account_count in", values, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountNotIn(List<BigDecimal> values) {
            addCriterion("open_account_count not in", values, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open_account_count between", value1, value2, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open_account_count not between", value1, value2, "openAccountCount");
            return (Criteria) this;
        }

        public Criteria andInvestNumberIsNull() {
            addCriterion("invest_number is null");
            return (Criteria) this;
        }

        public Criteria andInvestNumberIsNotNull() {
            addCriterion("invest_number is not null");
            return (Criteria) this;
        }

        public Criteria andInvestNumberEqualTo(BigDecimal value) {
            addCriterion("invest_number =", value, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberNotEqualTo(BigDecimal value) {
            addCriterion("invest_number <>", value, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberGreaterThan(BigDecimal value) {
            addCriterion("invest_number >", value, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("invest_number >=", value, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberLessThan(BigDecimal value) {
            addCriterion("invest_number <", value, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberLessThanOrEqualTo(BigDecimal value) {
            addCriterion("invest_number <=", value, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberIn(List<BigDecimal> values) {
            addCriterion("invest_number in", values, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberNotIn(List<BigDecimal> values) {
            addCriterion("invest_number not in", values, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("invest_number between", value1, value2, "investNumber");
            return (Criteria) this;
        }

        public Criteria andInvestNumberNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("invest_number not between", value1, value2, "investNumber");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeIsNull() {
            addCriterion("cumulative_charge is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeIsNotNull() {
            addCriterion("cumulative_charge is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeEqualTo(BigDecimal value) {
            addCriterion("cumulative_charge =", value, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_charge <>", value, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeGreaterThan(BigDecimal value) {
            addCriterion("cumulative_charge >", value, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_charge >=", value, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeLessThan(BigDecimal value) {
            addCriterion("cumulative_charge <", value, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_charge <=", value, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeIn(List<BigDecimal> values) {
            addCriterion("cumulative_charge in", values, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_charge not in", values, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_charge between", value1, value2, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeChargeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_charge not between", value1, value2, "cumulativeCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestIsNull() {
            addCriterion("cumulative_invest is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestIsNotNull() {
            addCriterion("cumulative_invest is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestEqualTo(BigDecimal value) {
            addCriterion("cumulative_invest =", value, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_invest <>", value, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestGreaterThan(BigDecimal value) {
            addCriterion("cumulative_invest >", value, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_invest >=", value, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestLessThan(BigDecimal value) {
            addCriterion("cumulative_invest <", value, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_invest <=", value, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestIn(List<BigDecimal> values) {
            addCriterion("cumulative_invest in", values, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_invest not in", values, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_invest between", value1, value2, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeInvestNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_invest not between", value1, value2, "cumulativeInvest");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumIsNull() {
            addCriterion("hzt_invest_sum is null");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumIsNotNull() {
            addCriterion("hzt_invest_sum is not null");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumEqualTo(BigDecimal value) {
            addCriterion("hzt_invest_sum =", value, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumNotEqualTo(BigDecimal value) {
            addCriterion("hzt_invest_sum <>", value, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumGreaterThan(BigDecimal value) {
            addCriterion("hzt_invest_sum >", value, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hzt_invest_sum >=", value, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumLessThan(BigDecimal value) {
            addCriterion("hzt_invest_sum <", value, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hzt_invest_sum <=", value, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumIn(List<BigDecimal> values) {
            addCriterion("hzt_invest_sum in", values, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumNotIn(List<BigDecimal> values) {
            addCriterion("hzt_invest_sum not in", values, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzt_invest_sum between", value1, value2, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHztInvestSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzt_invest_sum not between", value1, value2, "hztInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumIsNull() {
            addCriterion("hxf_invest_sum is null");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumIsNotNull() {
            addCriterion("hxf_invest_sum is not null");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumEqualTo(BigDecimal value) {
            addCriterion("hxf_invest_sum =", value, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumNotEqualTo(BigDecimal value) {
            addCriterion("hxf_invest_sum <>", value, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumGreaterThan(BigDecimal value) {
            addCriterion("hxf_invest_sum >", value, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hxf_invest_sum >=", value, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumLessThan(BigDecimal value) {
            addCriterion("hxf_invest_sum <", value, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hxf_invest_sum <=", value, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumIn(List<BigDecimal> values) {
            addCriterion("hxf_invest_sum in", values, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumNotIn(List<BigDecimal> values) {
            addCriterion("hxf_invest_sum not in", values, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hxf_invest_sum between", value1, value2, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHxfInvestSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hxf_invest_sum not between", value1, value2, "hxfInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumIsNull() {
            addCriterion("htl_invest_sum is null");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumIsNotNull() {
            addCriterion("htl_invest_sum is not null");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumEqualTo(BigDecimal value) {
            addCriterion("htl_invest_sum =", value, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumNotEqualTo(BigDecimal value) {
            addCriterion("htl_invest_sum <>", value, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumGreaterThan(BigDecimal value) {
            addCriterion("htl_invest_sum >", value, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("htl_invest_sum >=", value, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumLessThan(BigDecimal value) {
            addCriterion("htl_invest_sum <", value, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("htl_invest_sum <=", value, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumIn(List<BigDecimal> values) {
            addCriterion("htl_invest_sum in", values, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumNotIn(List<BigDecimal> values) {
            addCriterion("htl_invest_sum not in", values, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htl_invest_sum between", value1, value2, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtlInvestSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htl_invest_sum not between", value1, value2, "htlInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumIsNull() {
            addCriterion("htj_invest_sum is null");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumIsNotNull() {
            addCriterion("htj_invest_sum is not null");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumEqualTo(BigDecimal value) {
            addCriterion("htj_invest_sum =", value, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumNotEqualTo(BigDecimal value) {
            addCriterion("htj_invest_sum <>", value, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumGreaterThan(BigDecimal value) {
            addCriterion("htj_invest_sum >", value, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("htj_invest_sum >=", value, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumLessThan(BigDecimal value) {
            addCriterion("htj_invest_sum <", value, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("htj_invest_sum <=", value, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumIn(List<BigDecimal> values) {
            addCriterion("htj_invest_sum in", values, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumNotIn(List<BigDecimal> values) {
            addCriterion("htj_invest_sum not in", values, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htj_invest_sum between", value1, value2, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andHtjInvestSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("htj_invest_sum not between", value1, value2, "htjInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumIsNull() {
            addCriterion("rtb_invest_sum is null");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumIsNotNull() {
            addCriterion("rtb_invest_sum is not null");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumEqualTo(BigDecimal value) {
            addCriterion("rtb_invest_sum =", value, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumNotEqualTo(BigDecimal value) {
            addCriterion("rtb_invest_sum <>", value, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumGreaterThan(BigDecimal value) {
            addCriterion("rtb_invest_sum >", value, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rtb_invest_sum >=", value, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumLessThan(BigDecimal value) {
            addCriterion("rtb_invest_sum <", value, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rtb_invest_sum <=", value, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumIn(List<BigDecimal> values) {
            addCriterion("rtb_invest_sum in", values, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumNotIn(List<BigDecimal> values) {
            addCriterion("rtb_invest_sum not in", values, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rtb_invest_sum between", value1, value2, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andRtbInvestSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rtb_invest_sum not between", value1, value2, "rtbInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumIsNull() {
            addCriterion("hzr_invest_sum is null");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumIsNotNull() {
            addCriterion("hzr_invest_sum is not null");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumEqualTo(BigDecimal value) {
            addCriterion("hzr_invest_sum =", value, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumNotEqualTo(BigDecimal value) {
            addCriterion("hzr_invest_sum <>", value, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumGreaterThan(BigDecimal value) {
            addCriterion("hzr_invest_sum >", value, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hzr_invest_sum >=", value, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumLessThan(BigDecimal value) {
            addCriterion("hzr_invest_sum <", value, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hzr_invest_sum <=", value, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumIn(List<BigDecimal> values) {
            addCriterion("hzr_invest_sum in", values, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumNotIn(List<BigDecimal> values) {
            addCriterion("hzr_invest_sum not in", values, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzr_invest_sum between", value1, value2, "hzrInvestSum");
            return (Criteria) this;
        }

        public Criteria andHzrInvestSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hzr_invest_sum not between", value1, value2, "hzrInvestSum");
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

        public Criteria andRegisterAttrCountIsNull() {
            addCriterion("register_attr_count is null");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountIsNotNull() {
            addCriterion("register_attr_count is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountEqualTo(BigDecimal value) {
            addCriterion("register_attr_count =", value, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountNotEqualTo(BigDecimal value) {
            addCriterion("register_attr_count <>", value, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountGreaterThan(BigDecimal value) {
            addCriterion("register_attr_count >", value, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("register_attr_count >=", value, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountLessThan(BigDecimal value) {
            addCriterion("register_attr_count <", value, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("register_attr_count <=", value, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountIn(List<BigDecimal> values) {
            addCriterion("register_attr_count in", values, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountNotIn(List<BigDecimal> values) {
            addCriterion("register_attr_count not in", values, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("register_attr_count between", value1, value2, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andRegisterAttrCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("register_attr_count not between", value1, value2, "registerAttrCount");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosIsNull() {
            addCriterion("account_number_ios is null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosIsNotNull() {
            addCriterion("account_number_ios is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosEqualTo(Integer value) {
            addCriterion("account_number_ios =", value, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosNotEqualTo(Integer value) {
            addCriterion("account_number_ios <>", value, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosGreaterThan(Integer value) {
            addCriterion("account_number_ios >", value, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_number_ios >=", value, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosLessThan(Integer value) {
            addCriterion("account_number_ios <", value, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosLessThanOrEqualTo(Integer value) {
            addCriterion("account_number_ios <=", value, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosIn(List<Integer> values) {
            addCriterion("account_number_ios in", values, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosNotIn(List<Integer> values) {
            addCriterion("account_number_ios not in", values, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosBetween(Integer value1, Integer value2) {
            addCriterion("account_number_ios between", value1, value2, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberIosNotBetween(Integer value1, Integer value2) {
            addCriterion("account_number_ios not between", value1, value2, "accountNumberIos");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcIsNull() {
            addCriterion("account_number_pc is null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcIsNotNull() {
            addCriterion("account_number_pc is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcEqualTo(Integer value) {
            addCriterion("account_number_pc =", value, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcNotEqualTo(Integer value) {
            addCriterion("account_number_pc <>", value, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcGreaterThan(Integer value) {
            addCriterion("account_number_pc >", value, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_number_pc >=", value, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcLessThan(Integer value) {
            addCriterion("account_number_pc <", value, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcLessThanOrEqualTo(Integer value) {
            addCriterion("account_number_pc <=", value, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcIn(List<Integer> values) {
            addCriterion("account_number_pc in", values, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcNotIn(List<Integer> values) {
            addCriterion("account_number_pc not in", values, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcBetween(Integer value1, Integer value2) {
            addCriterion("account_number_pc between", value1, value2, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberPcNotBetween(Integer value1, Integer value2) {
            addCriterion("account_number_pc not between", value1, value2, "accountNumberPc");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidIsNull() {
            addCriterion("account_number_android is null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidIsNotNull() {
            addCriterion("account_number_android is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidEqualTo(Integer value) {
            addCriterion("account_number_android =", value, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidNotEqualTo(Integer value) {
            addCriterion("account_number_android <>", value, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidGreaterThan(Integer value) {
            addCriterion("account_number_android >", value, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_number_android >=", value, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidLessThan(Integer value) {
            addCriterion("account_number_android <", value, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidLessThanOrEqualTo(Integer value) {
            addCriterion("account_number_android <=", value, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidIn(List<Integer> values) {
            addCriterion("account_number_android in", values, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidNotIn(List<Integer> values) {
            addCriterion("account_number_android not in", values, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidBetween(Integer value1, Integer value2) {
            addCriterion("account_number_android between", value1, value2, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberAndroidNotBetween(Integer value1, Integer value2) {
            addCriterion("account_number_android not between", value1, value2, "accountNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatIsNull() {
            addCriterion("account_number_wechat is null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatIsNotNull() {
            addCriterion("account_number_wechat is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatEqualTo(Integer value) {
            addCriterion("account_number_wechat =", value, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatNotEqualTo(Integer value) {
            addCriterion("account_number_wechat <>", value, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatGreaterThan(Integer value) {
            addCriterion("account_number_wechat >", value, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_number_wechat >=", value, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatLessThan(Integer value) {
            addCriterion("account_number_wechat <", value, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatLessThanOrEqualTo(Integer value) {
            addCriterion("account_number_wechat <=", value, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatIn(List<Integer> values) {
            addCriterion("account_number_wechat in", values, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatNotIn(List<Integer> values) {
            addCriterion("account_number_wechat not in", values, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatBetween(Integer value1, Integer value2) {
            addCriterion("account_number_wechat between", value1, value2, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andAccountNumberWechatNotBetween(Integer value1, Integer value2) {
            addCriterion("account_number_wechat not between", value1, value2, "accountNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidIsNull() {
            addCriterion("tender_number_android is null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidIsNotNull() {
            addCriterion("tender_number_android is not null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidEqualTo(Integer value) {
            addCriterion("tender_number_android =", value, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidNotEqualTo(Integer value) {
            addCriterion("tender_number_android <>", value, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidGreaterThan(Integer value) {
            addCriterion("tender_number_android >", value, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_number_android >=", value, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidLessThan(Integer value) {
            addCriterion("tender_number_android <", value, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidLessThanOrEqualTo(Integer value) {
            addCriterion("tender_number_android <=", value, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidIn(List<Integer> values) {
            addCriterion("tender_number_android in", values, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidNotIn(List<Integer> values) {
            addCriterion("tender_number_android not in", values, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_android between", value1, value2, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberAndroidNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_android not between", value1, value2, "tenderNumberAndroid");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosIsNull() {
            addCriterion("tender_number_ios is null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosIsNotNull() {
            addCriterion("tender_number_ios is not null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosEqualTo(Integer value) {
            addCriterion("tender_number_ios =", value, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosNotEqualTo(Integer value) {
            addCriterion("tender_number_ios <>", value, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosGreaterThan(Integer value) {
            addCriterion("tender_number_ios >", value, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_number_ios >=", value, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosLessThan(Integer value) {
            addCriterion("tender_number_ios <", value, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosLessThanOrEqualTo(Integer value) {
            addCriterion("tender_number_ios <=", value, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosIn(List<Integer> values) {
            addCriterion("tender_number_ios in", values, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosNotIn(List<Integer> values) {
            addCriterion("tender_number_ios not in", values, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_ios between", value1, value2, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberIosNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_ios not between", value1, value2, "tenderNumberIos");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcIsNull() {
            addCriterion("tender_number_pc is null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcIsNotNull() {
            addCriterion("tender_number_pc is not null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcEqualTo(Integer value) {
            addCriterion("tender_number_pc =", value, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcNotEqualTo(Integer value) {
            addCriterion("tender_number_pc <>", value, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcGreaterThan(Integer value) {
            addCriterion("tender_number_pc >", value, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_number_pc >=", value, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcLessThan(Integer value) {
            addCriterion("tender_number_pc <", value, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcLessThanOrEqualTo(Integer value) {
            addCriterion("tender_number_pc <=", value, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcIn(List<Integer> values) {
            addCriterion("tender_number_pc in", values, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcNotIn(List<Integer> values) {
            addCriterion("tender_number_pc not in", values, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_pc between", value1, value2, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberPcNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_pc not between", value1, value2, "tenderNumberPc");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatIsNull() {
            addCriterion("tender_number_wechat is null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatIsNotNull() {
            addCriterion("tender_number_wechat is not null");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatEqualTo(Integer value) {
            addCriterion("tender_number_wechat =", value, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatNotEqualTo(Integer value) {
            addCriterion("tender_number_wechat <>", value, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatGreaterThan(Integer value) {
            addCriterion("tender_number_wechat >", value, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_number_wechat >=", value, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatLessThan(Integer value) {
            addCriterion("tender_number_wechat <", value, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatLessThanOrEqualTo(Integer value) {
            addCriterion("tender_number_wechat <=", value, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatIn(List<Integer> values) {
            addCriterion("tender_number_wechat in", values, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatNotIn(List<Integer> values) {
            addCriterion("tender_number_wechat not in", values, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_wechat between", value1, value2, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andTenderNumberWechatNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_number_wechat not between", value1, value2, "tenderNumberWechat");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeIsNull() {
            addCriterion("cumulative_attr_charge is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeIsNotNull() {
            addCriterion("cumulative_attr_charge is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_charge =", value, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_charge <>", value, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeGreaterThan(BigDecimal value) {
            addCriterion("cumulative_attr_charge >", value, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_charge >=", value, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeLessThan(BigDecimal value) {
            addCriterion("cumulative_attr_charge <", value, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_charge <=", value, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeIn(List<BigDecimal> values) {
            addCriterion("cumulative_attr_charge in", values, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_attr_charge not in", values, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_attr_charge between", value1, value2, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrChargeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_attr_charge not between", value1, value2, "cumulativeAttrCharge");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountIsNull() {
            addCriterion("open_account_attr_count is null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountIsNotNull() {
            addCriterion("open_account_attr_count is not null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountEqualTo(Integer value) {
            addCriterion("open_account_attr_count =", value, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountNotEqualTo(Integer value) {
            addCriterion("open_account_attr_count <>", value, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountGreaterThan(Integer value) {
            addCriterion("open_account_attr_count >", value, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("open_account_attr_count >=", value, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountLessThan(Integer value) {
            addCriterion("open_account_attr_count <", value, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountLessThanOrEqualTo(Integer value) {
            addCriterion("open_account_attr_count <=", value, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountIn(List<Integer> values) {
            addCriterion("open_account_attr_count in", values, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountNotIn(List<Integer> values) {
            addCriterion("open_account_attr_count not in", values, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountBetween(Integer value1, Integer value2) {
            addCriterion("open_account_attr_count between", value1, value2, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andOpenAccountAttrCountNotBetween(Integer value1, Integer value2) {
            addCriterion("open_account_attr_count not between", value1, value2, "openAccountAttrCount");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberIsNull() {
            addCriterion("invest_attr_number is null");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberIsNotNull() {
            addCriterion("invest_attr_number is not null");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberEqualTo(Integer value) {
            addCriterion("invest_attr_number =", value, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberNotEqualTo(Integer value) {
            addCriterion("invest_attr_number <>", value, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberGreaterThan(Integer value) {
            addCriterion("invest_attr_number >", value, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("invest_attr_number >=", value, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberLessThan(Integer value) {
            addCriterion("invest_attr_number <", value, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberLessThanOrEqualTo(Integer value) {
            addCriterion("invest_attr_number <=", value, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberIn(List<Integer> values) {
            addCriterion("invest_attr_number in", values, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberNotIn(List<Integer> values) {
            addCriterion("invest_attr_number not in", values, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberBetween(Integer value1, Integer value2) {
            addCriterion("invest_attr_number between", value1, value2, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andInvestAttrNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("invest_attr_number not between", value1, value2, "investAttrNumber");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestIsNull() {
            addCriterion("cumulative_attr_invest is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestIsNotNull() {
            addCriterion("cumulative_attr_invest is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_invest =", value, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_invest <>", value, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestGreaterThan(BigDecimal value) {
            addCriterion("cumulative_attr_invest >", value, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_invest >=", value, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestLessThan(BigDecimal value) {
            addCriterion("cumulative_attr_invest <", value, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_attr_invest <=", value, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestIn(List<BigDecimal> values) {
            addCriterion("cumulative_attr_invest in", values, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_attr_invest not in", values, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_attr_invest between", value1, value2, "cumulativeAttrInvest");
            return (Criteria) this;
        }

        public Criteria andCumulativeAttrInvestNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_attr_invest not between", value1, value2, "cumulativeAttrInvest");
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