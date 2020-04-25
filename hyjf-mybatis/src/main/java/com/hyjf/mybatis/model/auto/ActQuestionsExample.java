package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class ActQuestionsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActQuestionsExample() {
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andAnswerAIsNull() {
            addCriterion("answer_a is null");
            return (Criteria) this;
        }

        public Criteria andAnswerAIsNotNull() {
            addCriterion("answer_a is not null");
            return (Criteria) this;
        }

        public Criteria andAnswerAEqualTo(String value) {
            addCriterion("answer_a =", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerANotEqualTo(String value) {
            addCriterion("answer_a <>", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerAGreaterThan(String value) {
            addCriterion("answer_a >", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerAGreaterThanOrEqualTo(String value) {
            addCriterion("answer_a >=", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerALessThan(String value) {
            addCriterion("answer_a <", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerALessThanOrEqualTo(String value) {
            addCriterion("answer_a <=", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerALike(String value) {
            addCriterion("answer_a like", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerANotLike(String value) {
            addCriterion("answer_a not like", value, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerAIn(List<String> values) {
            addCriterion("answer_a in", values, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerANotIn(List<String> values) {
            addCriterion("answer_a not in", values, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerABetween(String value1, String value2) {
            addCriterion("answer_a between", value1, value2, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerANotBetween(String value1, String value2) {
            addCriterion("answer_a not between", value1, value2, "answerA");
            return (Criteria) this;
        }

        public Criteria andAnswerBIsNull() {
            addCriterion("answer_b is null");
            return (Criteria) this;
        }

        public Criteria andAnswerBIsNotNull() {
            addCriterion("answer_b is not null");
            return (Criteria) this;
        }

        public Criteria andAnswerBEqualTo(String value) {
            addCriterion("answer_b =", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBNotEqualTo(String value) {
            addCriterion("answer_b <>", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBGreaterThan(String value) {
            addCriterion("answer_b >", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBGreaterThanOrEqualTo(String value) {
            addCriterion("answer_b >=", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBLessThan(String value) {
            addCriterion("answer_b <", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBLessThanOrEqualTo(String value) {
            addCriterion("answer_b <=", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBLike(String value) {
            addCriterion("answer_b like", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBNotLike(String value) {
            addCriterion("answer_b not like", value, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBIn(List<String> values) {
            addCriterion("answer_b in", values, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBNotIn(List<String> values) {
            addCriterion("answer_b not in", values, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBBetween(String value1, String value2) {
            addCriterion("answer_b between", value1, value2, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerBNotBetween(String value1, String value2) {
            addCriterion("answer_b not between", value1, value2, "answerB");
            return (Criteria) this;
        }

        public Criteria andAnswerCIsNull() {
            addCriterion("answer_c is null");
            return (Criteria) this;
        }

        public Criteria andAnswerCIsNotNull() {
            addCriterion("answer_c is not null");
            return (Criteria) this;
        }

        public Criteria andAnswerCEqualTo(String value) {
            addCriterion("answer_c =", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCNotEqualTo(String value) {
            addCriterion("answer_c <>", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCGreaterThan(String value) {
            addCriterion("answer_c >", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCGreaterThanOrEqualTo(String value) {
            addCriterion("answer_c >=", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCLessThan(String value) {
            addCriterion("answer_c <", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCLessThanOrEqualTo(String value) {
            addCriterion("answer_c <=", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCLike(String value) {
            addCriterion("answer_c like", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCNotLike(String value) {
            addCriterion("answer_c not like", value, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCIn(List<String> values) {
            addCriterion("answer_c in", values, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCNotIn(List<String> values) {
            addCriterion("answer_c not in", values, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCBetween(String value1, String value2) {
            addCriterion("answer_c between", value1, value2, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerCNotBetween(String value1, String value2) {
            addCriterion("answer_c not between", value1, value2, "answerC");
            return (Criteria) this;
        }

        public Criteria andAnswerDIsNull() {
            addCriterion("answer_d is null");
            return (Criteria) this;
        }

        public Criteria andAnswerDIsNotNull() {
            addCriterion("answer_d is not null");
            return (Criteria) this;
        }

        public Criteria andAnswerDEqualTo(String value) {
            addCriterion("answer_d =", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDNotEqualTo(String value) {
            addCriterion("answer_d <>", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDGreaterThan(String value) {
            addCriterion("answer_d >", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDGreaterThanOrEqualTo(String value) {
            addCriterion("answer_d >=", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDLessThan(String value) {
            addCriterion("answer_d <", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDLessThanOrEqualTo(String value) {
            addCriterion("answer_d <=", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDLike(String value) {
            addCriterion("answer_d like", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDNotLike(String value) {
            addCriterion("answer_d not like", value, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDIn(List<String> values) {
            addCriterion("answer_d in", values, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDNotIn(List<String> values) {
            addCriterion("answer_d not in", values, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDBetween(String value1, String value2) {
            addCriterion("answer_d between", value1, value2, "answerD");
            return (Criteria) this;
        }

        public Criteria andAnswerDNotBetween(String value1, String value2) {
            addCriterion("answer_d not between", value1, value2, "answerD");
            return (Criteria) this;
        }

        public Criteria andTimeStartIsNull() {
            addCriterion("time_start is null");
            return (Criteria) this;
        }

        public Criteria andTimeStartIsNotNull() {
            addCriterion("time_start is not null");
            return (Criteria) this;
        }

        public Criteria andTimeStartEqualTo(Integer value) {
            addCriterion("time_start =", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotEqualTo(Integer value) {
            addCriterion("time_start <>", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartGreaterThan(Integer value) {
            addCriterion("time_start >", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_start >=", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartLessThan(Integer value) {
            addCriterion("time_start <", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartLessThanOrEqualTo(Integer value) {
            addCriterion("time_start <=", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartIn(List<Integer> values) {
            addCriterion("time_start in", values, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotIn(List<Integer> values) {
            addCriterion("time_start not in", values, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartBetween(Integer value1, Integer value2) {
            addCriterion("time_start between", value1, value2, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotBetween(Integer value1, Integer value2) {
            addCriterion("time_start not between", value1, value2, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeEndIsNull() {
            addCriterion("time_end is null");
            return (Criteria) this;
        }

        public Criteria andTimeEndIsNotNull() {
            addCriterion("time_end is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEndEqualTo(Integer value) {
            addCriterion("time_end =", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotEqualTo(Integer value) {
            addCriterion("time_end <>", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndGreaterThan(Integer value) {
            addCriterion("time_end >", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_end >=", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndLessThan(Integer value) {
            addCriterion("time_end <", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndLessThanOrEqualTo(Integer value) {
            addCriterion("time_end <=", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndIn(List<Integer> values) {
            addCriterion("time_end in", values, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotIn(List<Integer> values) {
            addCriterion("time_end not in", values, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndBetween(Integer value1, Integer value2) {
            addCriterion("time_end between", value1, value2, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotBetween(Integer value1, Integer value2) {
            addCriterion("time_end not between", value1, value2, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andAnswerRightIsNull() {
            addCriterion("answer_right is null");
            return (Criteria) this;
        }

        public Criteria andAnswerRightIsNotNull() {
            addCriterion("answer_right is not null");
            return (Criteria) this;
        }

        public Criteria andAnswerRightEqualTo(String value) {
            addCriterion("answer_right =", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightNotEqualTo(String value) {
            addCriterion("answer_right <>", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightGreaterThan(String value) {
            addCriterion("answer_right >", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightGreaterThanOrEqualTo(String value) {
            addCriterion("answer_right >=", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightLessThan(String value) {
            addCriterion("answer_right <", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightLessThanOrEqualTo(String value) {
            addCriterion("answer_right <=", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightLike(String value) {
            addCriterion("answer_right like", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightNotLike(String value) {
            addCriterion("answer_right not like", value, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightIn(List<String> values) {
            addCriterion("answer_right in", values, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightNotIn(List<String> values) {
            addCriterion("answer_right not in", values, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightBetween(String value1, String value2) {
            addCriterion("answer_right between", value1, value2, "answerRight");
            return (Criteria) this;
        }

        public Criteria andAnswerRightNotBetween(String value1, String value2) {
            addCriterion("answer_right not between", value1, value2, "answerRight");
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