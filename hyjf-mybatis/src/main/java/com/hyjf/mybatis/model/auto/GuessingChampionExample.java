package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class GuessingChampionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public GuessingChampionExample() {
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

        public Criteria andGuessingChampionIdIsNull() {
            addCriterion("guessing_champion_id is null");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdIsNotNull() {
            addCriterion("guessing_champion_id is not null");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdEqualTo(Integer value) {
            addCriterion("guessing_champion_id =", value, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdNotEqualTo(Integer value) {
            addCriterion("guessing_champion_id <>", value, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdGreaterThan(Integer value) {
            addCriterion("guessing_champion_id >", value, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("guessing_champion_id >=", value, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdLessThan(Integer value) {
            addCriterion("guessing_champion_id <", value, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdLessThanOrEqualTo(Integer value) {
            addCriterion("guessing_champion_id <=", value, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdIn(List<Integer> values) {
            addCriterion("guessing_champion_id in", values, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdNotIn(List<Integer> values) {
            addCriterion("guessing_champion_id not in", values, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdBetween(Integer value1, Integer value2) {
            addCriterion("guessing_champion_id between", value1, value2, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andGuessingChampionIdNotBetween(Integer value1, Integer value2) {
            addCriterion("guessing_champion_id not between", value1, value2, "guessingChampionId");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumIsNull() {
            addCriterion("team_win_num is null");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumIsNotNull() {
            addCriterion("team_win_num is not null");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumEqualTo(Integer value) {
            addCriterion("team_win_num =", value, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumNotEqualTo(Integer value) {
            addCriterion("team_win_num <>", value, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumGreaterThan(Integer value) {
            addCriterion("team_win_num >", value, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("team_win_num >=", value, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumLessThan(Integer value) {
            addCriterion("team_win_num <", value, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumLessThanOrEqualTo(Integer value) {
            addCriterion("team_win_num <=", value, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumIn(List<Integer> values) {
            addCriterion("team_win_num in", values, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumNotIn(List<Integer> values) {
            addCriterion("team_win_num not in", values, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumBetween(Integer value1, Integer value2) {
            addCriterion("team_win_num between", value1, value2, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andTeamWinNumNotBetween(Integer value1, Integer value2) {
            addCriterion("team_win_num not between", value1, value2, "teamWinNum");
            return (Criteria) this;
        }

        public Criteria andVoteTimeIsNull() {
            addCriterion("vote_time is null");
            return (Criteria) this;
        }

        public Criteria andVoteTimeIsNotNull() {
            addCriterion("vote_time is not null");
            return (Criteria) this;
        }

        public Criteria andVoteTimeEqualTo(Integer value) {
            addCriterion("vote_time =", value, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeNotEqualTo(Integer value) {
            addCriterion("vote_time <>", value, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeGreaterThan(Integer value) {
            addCriterion("vote_time >", value, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("vote_time >=", value, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeLessThan(Integer value) {
            addCriterion("vote_time <", value, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeLessThanOrEqualTo(Integer value) {
            addCriterion("vote_time <=", value, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeIn(List<Integer> values) {
            addCriterion("vote_time in", values, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeNotIn(List<Integer> values) {
            addCriterion("vote_time not in", values, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeBetween(Integer value1, Integer value2) {
            addCriterion("vote_time between", value1, value2, "voteTime");
            return (Criteria) this;
        }

        public Criteria andVoteTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("vote_time not between", value1, value2, "voteTime");
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