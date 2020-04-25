package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class GuessingWinningExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public GuessingWinningExample() {
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

        public Criteria andTrueNameIsNull() {
            addCriterion("true_name is null");
            return (Criteria) this;
        }

        public Criteria andTrueNameIsNotNull() {
            addCriterion("true_name is not null");
            return (Criteria) this;
        }

        public Criteria andTrueNameEqualTo(String value) {
            addCriterion("true_name =", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameNotEqualTo(String value) {
            addCriterion("true_name <>", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameGreaterThan(String value) {
            addCriterion("true_name >", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameGreaterThanOrEqualTo(String value) {
            addCriterion("true_name >=", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameLessThan(String value) {
            addCriterion("true_name <", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameLessThanOrEqualTo(String value) {
            addCriterion("true_name <=", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameLike(String value) {
            addCriterion("true_name like", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameNotLike(String value) {
            addCriterion("true_name not like", value, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameIn(List<String> values) {
            addCriterion("true_name in", values, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameNotIn(List<String> values) {
            addCriterion("true_name not in", values, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameBetween(String value1, String value2) {
            addCriterion("true_name between", value1, value2, "trueName");
            return (Criteria) this;
        }

        public Criteria andTrueNameNotBetween(String value1, String value2) {
            addCriterion("true_name not between", value1, value2, "trueName");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdIsNull() {
            addCriterion("guessing_match_id is null");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdIsNotNull() {
            addCriterion("guessing_match_id is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdEqualTo(Integer value) {
            addCriterion("guessing_match_id =", value, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdNotEqualTo(Integer value) {
            addCriterion("guessing_match_id <>", value, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdGreaterThan(Integer value) {
            addCriterion("guessing_match_id >", value, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_match_id >=", value, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdLessThan(Integer value) {
            addCriterion("guessing_match_id <", value, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_match_id <=", value, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdIn(List<Integer> values) {
            addCriterion("guessing_match_id in", values, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdNotIn(List<Integer> values) {
            addCriterion("guessing_match_id not in", values, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdBetween(Integer value1, Integer value2) {
            addCriterion("guessing_match_id between", value1, value2, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchIdNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_match_id not between", value1, value2, "guessingMatchId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdIsNull() {
            addCriterion("user_match_team_id is null");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdIsNotNull() {
            addCriterion("user_match_team_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdEqualTo(Integer value) {
            addCriterion("user_match_team_id =", value, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdNotEqualTo(Integer value) {
            addCriterion("user_match_team_id <>", value, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdGreaterThan(Integer value) {
            addCriterion("user_match_team_id >", value, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_match_team_id >=", value, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdLessThan(Integer value) {
            addCriterion("user_match_team_id <", value, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_match_team_id <=", value, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdIn(List<Integer> values) {
            addCriterion("user_match_team_id in", values, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdNotIn(List<Integer> values) {
            addCriterion("user_match_team_id not in", values, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdBetween(Integer value1, Integer value2) {
            addCriterion("user_match_team_id between", value1, value2, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andUserMatchTeamIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_match_team_id not between", value1, value2, "userMatchTeamId");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultIsNull() {
            addCriterion("guessing_match_result is null");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultIsNotNull() {
            addCriterion("guessing_match_result is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultEqualTo(Integer value) {
            addCriterion("guessing_match_result =", value, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultNotEqualTo(Integer value) {
            addCriterion("guessing_match_result <>", value, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultGreaterThan(Integer value) {
            addCriterion("guessing_match_result >", value, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_match_result >=", value, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultLessThan(Integer value) {
            addCriterion("guessing_match_result <", value, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_match_result <=", value, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultIn(List<Integer> values) {
            addCriterion("guessing_match_result in", values, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultNotIn(List<Integer> values) {
            addCriterion("guessing_match_result not in", values, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultBetween(Integer value1, Integer value2) {
            addCriterion("guessing_match_result between", value1, value2, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchResultNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_match_result not between", value1, value2, "guessingMatchResult");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumIsNull() {
            addCriterion("guessing_match_num is null");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumIsNotNull() {
            addCriterion("guessing_match_num is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumEqualTo(Integer value) {
            addCriterion("guessing_match_num =", value, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumNotEqualTo(Integer value) {
            addCriterion("guessing_match_num <>", value, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumGreaterThan(Integer value) {
            addCriterion("guessing_match_num >", value, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_match_num >=", value, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumLessThan(Integer value) {
            addCriterion("guessing_match_num <", value, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_match_num <=", value, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumIn(List<Integer> values) {
            addCriterion("guessing_match_num in", values, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumNotIn(List<Integer> values) {
            addCriterion("guessing_match_num not in", values, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumBetween(Integer value1, Integer value2) {
            addCriterion("guessing_match_num between", value1, value2, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingMatchNumNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_match_num not between", value1, value2, "guessingMatchNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumIsNull() {
            addCriterion("guessing_field_num is null");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumIsNotNull() {
            addCriterion("guessing_field_num is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumEqualTo(Integer value) {
            addCriterion("guessing_field_num =", value, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumNotEqualTo(Integer value) {
            addCriterion("guessing_field_num <>", value, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumGreaterThan(Integer value) {
            addCriterion("guessing_field_num >", value, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_field_num >=", value, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumLessThan(Integer value) {
            addCriterion("guessing_field_num <", value, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_field_num <=", value, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumIn(List<Integer> values) {
            addCriterion("guessing_field_num in", values, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumNotIn(List<Integer> values) {
            addCriterion("guessing_field_num not in", values, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumBetween(Integer value1, Integer value2) {
            addCriterion("guessing_field_num between", value1, value2, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingFieldNumNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_field_num not between", value1, value2, "guessingFieldNum");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsIsNull() {
            addCriterion("guessing_rankings is null");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsIsNotNull() {
            addCriterion("guessing_rankings is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsEqualTo(Integer value) {
            addCriterion("guessing_rankings =", value, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsNotEqualTo(Integer value) {
            addCriterion("guessing_rankings <>", value, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsGreaterThan(Integer value) {
            addCriterion("guessing_rankings >", value, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_rankings >=", value, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsLessThan(Integer value) {
            addCriterion("guessing_rankings <", value, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_rankings <=", value, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsIn(List<Integer> values) {
            addCriterion("guessing_rankings in", values, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsNotIn(List<Integer> values) {
            addCriterion("guessing_rankings not in", values, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsBetween(Integer value1, Integer value2) {
            addCriterion("guessing_rankings between", value1, value2, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingRankingsNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_rankings not between", value1, value2, "guessingRankings");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeIsNull() {
            addCriterion("guessing_time is null");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeIsNotNull() {
            addCriterion("guessing_time is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeEqualTo(Integer value) {
            addCriterion("guessing_time =", value, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeNotEqualTo(Integer value) {
            addCriterion("guessing_time <>", value, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeGreaterThan(Integer value) {
            addCriterion("guessing_time >", value, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_time >=", value, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeLessThan(Integer value) {
            addCriterion("guessing_time <", value, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_time <=", value, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeIn(List<Integer> values) {
            addCriterion("guessing_time in", values, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeNotIn(List<Integer> values) {
            addCriterion("guessing_time not in", values, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeBetween(Integer value1, Integer value2) {
            addCriterion("guessing_time between", value1, value2, "guessingTime");
            return (Criteria) this;
        }

        public Criteria andGuessingTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_time not between", value1, value2, "guessingTime");
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