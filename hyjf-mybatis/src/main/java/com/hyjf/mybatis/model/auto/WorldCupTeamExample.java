package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class WorldCupTeamExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public WorldCupTeamExample() {
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

        public Criteria andTeamYearIsNull() {
            addCriterion("team_year is null");
            return (Criteria) this;
        }

        public Criteria andTeamYearIsNotNull() {
            addCriterion("team_year is not null");
            return (Criteria) this;
        }

        public Criteria andTeamYearEqualTo(String value) {
            addCriterion("team_year =", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearNotEqualTo(String value) {
            addCriterion("team_year <>", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearGreaterThan(String value) {
            addCriterion("team_year >", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearGreaterThanOrEqualTo(String value) {
            addCriterion("team_year >=", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearLessThan(String value) {
            addCriterion("team_year <", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearLessThanOrEqualTo(String value) {
            addCriterion("team_year <=", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearLike(String value) {
            addCriterion("team_year like", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearNotLike(String value) {
            addCriterion("team_year not like", value, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearIn(List<String> values) {
            addCriterion("team_year in", values, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearNotIn(List<String> values) {
            addCriterion("team_year not in", values, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearBetween(String value1, String value2) {
            addCriterion("team_year between", value1, value2, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamYearNotBetween(String value1, String value2) {
            addCriterion("team_year not between", value1, value2, "teamYear");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsIsNull() {
            addCriterion("team_groupings is null");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsIsNotNull() {
            addCriterion("team_groupings is not null");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsEqualTo(Integer value) {
            addCriterion("team_groupings =", value, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsNotEqualTo(Integer value) {
            addCriterion("team_groupings <>", value, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsGreaterThan(Integer value) {
            addCriterion("team_groupings >", value, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsGreaterThanOrEqualTo(Integer value) {
            addCriterion("team_groupings >=", value, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsLessThan(Integer value) {
            addCriterion("team_groupings <", value, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsLessThanOrEqualTo(Integer value) {
            addCriterion("team_groupings <=", value, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsIn(List<Integer> values) {
            addCriterion("team_groupings in", values, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsNotIn(List<Integer> values) {
            addCriterion("team_groupings not in", values, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsBetween(Integer value1, Integer value2) {
            addCriterion("team_groupings between", value1, value2, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamGroupingsNotBetween(Integer value1, Integer value2) {
            addCriterion("team_groupings not between", value1, value2, "teamGroupings");
            return (Criteria) this;
        }

        public Criteria andTeamNameIsNull() {
            addCriterion("team_name is null");
            return (Criteria) this;
        }

        public Criteria andTeamNameIsNotNull() {
            addCriterion("team_name is not null");
            return (Criteria) this;
        }

        public Criteria andTeamNameEqualTo(String value) {
            addCriterion("team_name =", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameNotEqualTo(String value) {
            addCriterion("team_name <>", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameGreaterThan(String value) {
            addCriterion("team_name >", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameGreaterThanOrEqualTo(String value) {
            addCriterion("team_name >=", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameLessThan(String value) {
            addCriterion("team_name <", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameLessThanOrEqualTo(String value) {
            addCriterion("team_name <=", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameLike(String value) {
            addCriterion("team_name like", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameNotLike(String value) {
            addCriterion("team_name not like", value, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameIn(List<String> values) {
            addCriterion("team_name in", values, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameNotIn(List<String> values) {
            addCriterion("team_name not in", values, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameBetween(String value1, String value2) {
            addCriterion("team_name between", value1, value2, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamNameNotBetween(String value1, String value2) {
            addCriterion("team_name not between", value1, value2, "teamName");
            return (Criteria) this;
        }

        public Criteria andTeamLogoIsNull() {
            addCriterion("team_logo is null");
            return (Criteria) this;
        }

        public Criteria andTeamLogoIsNotNull() {
            addCriterion("team_logo is not null");
            return (Criteria) this;
        }

        public Criteria andTeamLogoEqualTo(String value) {
            addCriterion("team_logo =", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoNotEqualTo(String value) {
            addCriterion("team_logo <>", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoGreaterThan(String value) {
            addCriterion("team_logo >", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoGreaterThanOrEqualTo(String value) {
            addCriterion("team_logo >=", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoLessThan(String value) {
            addCriterion("team_logo <", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoLessThanOrEqualTo(String value) {
            addCriterion("team_logo <=", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoLike(String value) {
            addCriterion("team_logo like", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoNotLike(String value) {
            addCriterion("team_logo not like", value, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoIn(List<String> values) {
            addCriterion("team_logo in", values, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoNotIn(List<String> values) {
            addCriterion("team_logo not in", values, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoBetween(String value1, String value2) {
            addCriterion("team_logo between", value1, value2, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamLogoNotBetween(String value1, String value2) {
            addCriterion("team_logo not between", value1, value2, "teamLogo");
            return (Criteria) this;
        }

        public Criteria andTeamNumberIsNull() {
            addCriterion("team_number is null");
            return (Criteria) this;
        }

        public Criteria andTeamNumberIsNotNull() {
            addCriterion("team_number is not null");
            return (Criteria) this;
        }

        public Criteria andTeamNumberEqualTo(String value) {
            addCriterion("team_number =", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberNotEqualTo(String value) {
            addCriterion("team_number <>", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberGreaterThan(String value) {
            addCriterion("team_number >", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberGreaterThanOrEqualTo(String value) {
            addCriterion("team_number >=", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberLessThan(String value) {
            addCriterion("team_number <", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberLessThanOrEqualTo(String value) {
            addCriterion("team_number <=", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberLike(String value) {
            addCriterion("team_number like", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberNotLike(String value) {
            addCriterion("team_number not like", value, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberIn(List<String> values) {
            addCriterion("team_number in", values, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberNotIn(List<String> values) {
            addCriterion("team_number not in", values, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberBetween(String value1, String value2) {
            addCriterion("team_number between", value1, value2, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andTeamNumberNotBetween(String value1, String value2) {
            addCriterion("team_number not between", value1, value2, "teamNumber");
            return (Criteria) this;
        }

        public Criteria andChampionNumIsNull() {
            addCriterion("champion_num is null");
            return (Criteria) this;
        }

        public Criteria andChampionNumIsNotNull() {
            addCriterion("champion_num is not null");
            return (Criteria) this;
        }

        public Criteria andChampionNumEqualTo(Integer value) {
            addCriterion("champion_num =", value, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumNotEqualTo(Integer value) {
            addCriterion("champion_num <>", value, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumGreaterThan(Integer value) {
            addCriterion("champion_num >", value, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("champion_num >=", value, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumLessThan(Integer value) {
            addCriterion("champion_num <", value, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumLessThanOrEqualTo(Integer value) {
            addCriterion("champion_num <=", value, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumIn(List<Integer> values) {
            addCriterion("champion_num in", values, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumNotIn(List<Integer> values) {
            addCriterion("champion_num not in", values, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumBetween(Integer value1, Integer value2) {
            addCriterion("champion_num between", value1, value2, "championNum");
            return (Criteria) this;
        }

        public Criteria andChampionNumNotBetween(Integer value1, Integer value2) {
            addCriterion("champion_num not between", value1, value2, "championNum");
            return (Criteria) this;
        }

        public Criteria andIsEliminateIsNull() {
            addCriterion("is_eliminate is null");
            return (Criteria) this;
        }

        public Criteria andIsEliminateIsNotNull() {
            addCriterion("is_eliminate is not null");
            return (Criteria) this;
        }

        public Criteria andIsEliminateEqualTo(Integer value) {
            addCriterion("is_eliminate =", value, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateNotEqualTo(Integer value) {
            addCriterion("is_eliminate <>", value, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateGreaterThan(Integer value) {
            addCriterion("is_eliminate >", value, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_eliminate >=", value, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateLessThan(Integer value) {
            addCriterion("is_eliminate <", value, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateLessThanOrEqualTo(Integer value) {
            addCriterion("is_eliminate <=", value, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateIn(List<Integer> values) {
            addCriterion("is_eliminate in", values, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateNotIn(List<Integer> values) {
            addCriterion("is_eliminate not in", values, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateBetween(Integer value1, Integer value2) {
            addCriterion("is_eliminate between", value1, value2, "isEliminate");
            return (Criteria) this;
        }

        public Criteria andIsEliminateNotBetween(Integer value1, Integer value2) {
            addCriterion("is_eliminate not between", value1, value2, "isEliminate");
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