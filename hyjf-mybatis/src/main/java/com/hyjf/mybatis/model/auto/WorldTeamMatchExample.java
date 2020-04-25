package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class WorldTeamMatchExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public WorldTeamMatchExample() {
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

        public Criteria andMatchTeamIsNull() {
            addCriterion("match_team is null");
            return (Criteria) this;
        }

        public Criteria andMatchTeamIsNotNull() {
            addCriterion("match_team is not null");
            return (Criteria) this;
        }

        public Criteria andMatchTeamEqualTo(Integer value) {
            addCriterion("match_team =", value, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamNotEqualTo(Integer value) {
            addCriterion("match_team <>", value, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamGreaterThan(Integer value) {
            addCriterion("match_team >", value, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamGreaterThanOrEqualTo(Integer value) {
            addCriterion("match_team >=", value, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamLessThan(Integer value) {
            addCriterion("match_team <", value, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamLessThanOrEqualTo(Integer value) {
            addCriterion("match_team <=", value, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamIn(List<Integer> values) {
            addCriterion("match_team in", values, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamNotIn(List<Integer> values) {
            addCriterion("match_team not in", values, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamBetween(Integer value1, Integer value2) {
            addCriterion("match_team between", value1, value2, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTeamNotBetween(Integer value1, Integer value2) {
            addCriterion("match_team not between", value1, value2, "matchTeam");
            return (Criteria) this;
        }

        public Criteria andMatchTypeIsNull() {
            addCriterion("match_type is null");
            return (Criteria) this;
        }

        public Criteria andMatchTypeIsNotNull() {
            addCriterion("match_type is not null");
            return (Criteria) this;
        }

        public Criteria andMatchTypeEqualTo(Integer value) {
            addCriterion("match_type =", value, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeNotEqualTo(Integer value) {
            addCriterion("match_type <>", value, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeGreaterThan(Integer value) {
            addCriterion("match_type >", value, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("match_type >=", value, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeLessThan(Integer value) {
            addCriterion("match_type <", value, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeLessThanOrEqualTo(Integer value) {
            addCriterion("match_type <=", value, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeIn(List<Integer> values) {
            addCriterion("match_type in", values, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeNotIn(List<Integer> values) {
            addCriterion("match_type not in", values, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeBetween(Integer value1, Integer value2) {
            addCriterion("match_type between", value1, value2, "matchType");
            return (Criteria) this;
        }

        public Criteria andMatchTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("match_type not between", value1, value2, "matchType");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamIsNull() {
            addCriterion("home_match_team is null");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamIsNotNull() {
            addCriterion("home_match_team is not null");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamEqualTo(Integer value) {
            addCriterion("home_match_team =", value, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamNotEqualTo(Integer value) {
            addCriterion("home_match_team <>", value, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamGreaterThan(Integer value) {
            addCriterion("home_match_team >", value, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamGreaterThanOrEqualTo(Integer value) {
            addCriterion("home_match_team >=", value, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamLessThan(Integer value) {
            addCriterion("home_match_team <", value, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamLessThanOrEqualTo(Integer value) {
            addCriterion("home_match_team <=", value, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamIn(List<Integer> values) {
            addCriterion("home_match_team in", values, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamNotIn(List<Integer> values) {
            addCriterion("home_match_team not in", values, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamBetween(Integer value1, Integer value2) {
            addCriterion("home_match_team between", value1, value2, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andHomeMatchTeamNotBetween(Integer value1, Integer value2) {
            addCriterion("home_match_team not between", value1, value2, "homeMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamIsNull() {
            addCriterion("visiting_match_team is null");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamIsNotNull() {
            addCriterion("visiting_match_team is not null");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamEqualTo(Integer value) {
            addCriterion("visiting_match_team =", value, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamNotEqualTo(Integer value) {
            addCriterion("visiting_match_team <>", value, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamGreaterThan(Integer value) {
            addCriterion("visiting_match_team >", value, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamGreaterThanOrEqualTo(Integer value) {
            addCriterion("visiting_match_team >=", value, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamLessThan(Integer value) {
            addCriterion("visiting_match_team <", value, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamLessThanOrEqualTo(Integer value) {
            addCriterion("visiting_match_team <=", value, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamIn(List<Integer> values) {
            addCriterion("visiting_match_team in", values, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamNotIn(List<Integer> values) {
            addCriterion("visiting_match_team not in", values, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamBetween(Integer value1, Integer value2) {
            addCriterion("visiting_match_team between", value1, value2, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andVisitingMatchTeamNotBetween(Integer value1, Integer value2) {
            addCriterion("visiting_match_team not between", value1, value2, "visitingMatchTeam");
            return (Criteria) this;
        }

        public Criteria andBatchNameIsNull() {
            addCriterion("batch_name is null");
            return (Criteria) this;
        }

        public Criteria andBatchNameIsNotNull() {
            addCriterion("batch_name is not null");
            return (Criteria) this;
        }

        public Criteria andBatchNameEqualTo(String value) {
            addCriterion("batch_name =", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameNotEqualTo(String value) {
            addCriterion("batch_name <>", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameGreaterThan(String value) {
            addCriterion("batch_name >", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameGreaterThanOrEqualTo(String value) {
            addCriterion("batch_name >=", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameLessThan(String value) {
            addCriterion("batch_name <", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameLessThanOrEqualTo(String value) {
            addCriterion("batch_name <=", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameLike(String value) {
            addCriterion("batch_name like", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameNotLike(String value) {
            addCriterion("batch_name not like", value, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameIn(List<String> values) {
            addCriterion("batch_name in", values, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameNotIn(List<String> values) {
            addCriterion("batch_name not in", values, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameBetween(String value1, String value2) {
            addCriterion("batch_name between", value1, value2, "batchName");
            return (Criteria) this;
        }

        public Criteria andBatchNameNotBetween(String value1, String value2) {
            addCriterion("batch_name not between", value1, value2, "batchName");
            return (Criteria) this;
        }

        public Criteria andMatchTimeIsNull() {
            addCriterion("match_time is null");
            return (Criteria) this;
        }

        public Criteria andMatchTimeIsNotNull() {
            addCriterion("match_time is not null");
            return (Criteria) this;
        }

        public Criteria andMatchTimeEqualTo(Integer value) {
            addCriterion("match_time =", value, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeNotEqualTo(Integer value) {
            addCriterion("match_time <>", value, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeGreaterThan(Integer value) {
            addCriterion("match_time >", value, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("match_time >=", value, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeLessThan(Integer value) {
            addCriterion("match_time <", value, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeLessThanOrEqualTo(Integer value) {
            addCriterion("match_time <=", value, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeIn(List<Integer> values) {
            addCriterion("match_time in", values, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeNotIn(List<Integer> values) {
            addCriterion("match_time not in", values, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeBetween(Integer value1, Integer value2) {
            addCriterion("match_time between", value1, value2, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("match_time not between", value1, value2, "matchTime");
            return (Criteria) this;
        }

        public Criteria andMatchResultIsNull() {
            addCriterion("match_result is null");
            return (Criteria) this;
        }

        public Criteria andMatchResultIsNotNull() {
            addCriterion("match_result is not null");
            return (Criteria) this;
        }

        public Criteria andMatchResultEqualTo(String value) {
            addCriterion("match_result =", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultNotEqualTo(String value) {
            addCriterion("match_result <>", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultGreaterThan(String value) {
            addCriterion("match_result >", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultGreaterThanOrEqualTo(String value) {
            addCriterion("match_result >=", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultLessThan(String value) {
            addCriterion("match_result <", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultLessThanOrEqualTo(String value) {
            addCriterion("match_result <=", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultLike(String value) {
            addCriterion("match_result like", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultNotLike(String value) {
            addCriterion("match_result not like", value, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultIn(List<String> values) {
            addCriterion("match_result in", values, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultNotIn(List<String> values) {
            addCriterion("match_result not in", values, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultBetween(String value1, String value2) {
            addCriterion("match_result between", value1, value2, "matchResult");
            return (Criteria) this;
        }

        public Criteria andMatchResultNotBetween(String value1, String value2) {
            addCriterion("match_result not between", value1, value2, "matchResult");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdIsNull() {
            addCriterion("win_team_id is null");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdIsNotNull() {
            addCriterion("win_team_id is not null");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdEqualTo(Integer value) {
            addCriterion("win_team_id =", value, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdNotEqualTo(Integer value) {
            addCriterion("win_team_id <>", value, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdGreaterThan(Integer value) {
            addCriterion("win_team_id >", value, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("win_team_id >=", value, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdLessThan(Integer value) {
            addCriterion("win_team_id <", value, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdLessThanOrEqualTo(Integer value) {
            addCriterion("win_team_id <=", value, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdIn(List<Integer> values) {
            addCriterion("win_team_id in", values, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdNotIn(List<Integer> values) {
            addCriterion("win_team_id not in", values, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdBetween(Integer value1, Integer value2) {
            addCriterion("win_team_id between", value1, value2, "winTeamId");
            return (Criteria) this;
        }

        public Criteria andWinTeamIdNotBetween(Integer value1, Integer value2) {
            addCriterion("win_team_id not between", value1, value2, "winTeamId");
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