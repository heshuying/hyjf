package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class NewyearQuestionUserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public NewyearQuestionUserExample() {
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

        public Criteria andQuestionIdIsNull() {
            addCriterion("question_id is null");
            return (Criteria) this;
        }

        public Criteria andQuestionIdIsNotNull() {
            addCriterion("question_id is not null");
            return (Criteria) this;
        }

        public Criteria andQuestionIdEqualTo(Integer value) {
            addCriterion("question_id =", value, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdNotEqualTo(Integer value) {
            addCriterion("question_id <>", value, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdGreaterThan(Integer value) {
            addCriterion("question_id >", value, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("question_id >=", value, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdLessThan(Integer value) {
            addCriterion("question_id <", value, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdLessThanOrEqualTo(Integer value) {
            addCriterion("question_id <=", value, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdIn(List<Integer> values) {
            addCriterion("question_id in", values, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdNotIn(List<Integer> values) {
            addCriterion("question_id not in", values, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdBetween(Integer value1, Integer value2) {
            addCriterion("question_id between", value1, value2, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionIdNotBetween(Integer value1, Integer value2) {
            addCriterion("question_id not between", value1, value2, "questionId");
            return (Criteria) this;
        }

        public Criteria andQuestionNumIsNull() {
            addCriterion("question_num is null");
            return (Criteria) this;
        }

        public Criteria andQuestionNumIsNotNull() {
            addCriterion("question_num is not null");
            return (Criteria) this;
        }

        public Criteria andQuestionNumEqualTo(Integer value) {
            addCriterion("question_num =", value, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumNotEqualTo(Integer value) {
            addCriterion("question_num <>", value, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumGreaterThan(Integer value) {
            addCriterion("question_num >", value, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("question_num >=", value, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumLessThan(Integer value) {
            addCriterion("question_num <", value, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumLessThanOrEqualTo(Integer value) {
            addCriterion("question_num <=", value, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumIn(List<Integer> values) {
            addCriterion("question_num in", values, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumNotIn(List<Integer> values) {
            addCriterion("question_num not in", values, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumBetween(Integer value1, Integer value2) {
            addCriterion("question_num between", value1, value2, "questionNum");
            return (Criteria) this;
        }

        public Criteria andQuestionNumNotBetween(Integer value1, Integer value2) {
            addCriterion("question_num not between", value1, value2, "questionNum");
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

        public Criteria andCurrentExchangeIsNull() {
            addCriterion("current_exchange is null");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeIsNotNull() {
            addCriterion("current_exchange is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeEqualTo(Integer value) {
            addCriterion("current_exchange =", value, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeNotEqualTo(Integer value) {
            addCriterion("current_exchange <>", value, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeGreaterThan(Integer value) {
            addCriterion("current_exchange >", value, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeGreaterThanOrEqualTo(Integer value) {
            addCriterion("current_exchange >=", value, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeLessThan(Integer value) {
            addCriterion("current_exchange <", value, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeLessThanOrEqualTo(Integer value) {
            addCriterion("current_exchange <=", value, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeIn(List<Integer> values) {
            addCriterion("current_exchange in", values, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeNotIn(List<Integer> values) {
            addCriterion("current_exchange not in", values, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeBetween(Integer value1, Integer value2) {
            addCriterion("current_exchange between", value1, value2, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andCurrentExchangeNotBetween(Integer value1, Integer value2) {
            addCriterion("current_exchange not between", value1, value2, "currentExchange");
            return (Criteria) this;
        }

        public Criteria andViewNameIsNull() {
            addCriterion("view_name is null");
            return (Criteria) this;
        }

        public Criteria andViewNameIsNotNull() {
            addCriterion("view_name is not null");
            return (Criteria) this;
        }

        public Criteria andViewNameEqualTo(String value) {
            addCriterion("view_name =", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameNotEqualTo(String value) {
            addCriterion("view_name <>", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameGreaterThan(String value) {
            addCriterion("view_name >", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameGreaterThanOrEqualTo(String value) {
            addCriterion("view_name >=", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameLessThan(String value) {
            addCriterion("view_name <", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameLessThanOrEqualTo(String value) {
            addCriterion("view_name <=", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameLike(String value) {
            addCriterion("view_name like", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameNotLike(String value) {
            addCriterion("view_name not like", value, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameIn(List<String> values) {
            addCriterion("view_name in", values, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameNotIn(List<String> values) {
            addCriterion("view_name not in", values, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameBetween(String value1, String value2) {
            addCriterion("view_name between", value1, value2, "viewName");
            return (Criteria) this;
        }

        public Criteria andViewNameNotBetween(String value1, String value2) {
            addCriterion("view_name not between", value1, value2, "viewName");
            return (Criteria) this;
        }

        public Criteria andPrizeJineIsNull() {
            addCriterion("prize_jine is null");
            return (Criteria) this;
        }

        public Criteria andPrizeJineIsNotNull() {
            addCriterion("prize_jine is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeJineEqualTo(Integer value) {
            addCriterion("prize_jine =", value, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineNotEqualTo(Integer value) {
            addCriterion("prize_jine <>", value, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineGreaterThan(Integer value) {
            addCriterion("prize_jine >", value, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_jine >=", value, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineLessThan(Integer value) {
            addCriterion("prize_jine <", value, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineLessThanOrEqualTo(Integer value) {
            addCriterion("prize_jine <=", value, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineIn(List<Integer> values) {
            addCriterion("prize_jine in", values, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineNotIn(List<Integer> values) {
            addCriterion("prize_jine not in", values, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineBetween(Integer value1, Integer value2) {
            addCriterion("prize_jine between", value1, value2, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andPrizeJineNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_jine not between", value1, value2, "prizeJine");
            return (Criteria) this;
        }

        public Criteria andUserAnswerIsNull() {
            addCriterion("user_answer is null");
            return (Criteria) this;
        }

        public Criteria andUserAnswerIsNotNull() {
            addCriterion("user_answer is not null");
            return (Criteria) this;
        }

        public Criteria andUserAnswerEqualTo(String value) {
            addCriterion("user_answer =", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerNotEqualTo(String value) {
            addCriterion("user_answer <>", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerGreaterThan(String value) {
            addCriterion("user_answer >", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerGreaterThanOrEqualTo(String value) {
            addCriterion("user_answer >=", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerLessThan(String value) {
            addCriterion("user_answer <", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerLessThanOrEqualTo(String value) {
            addCriterion("user_answer <=", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerLike(String value) {
            addCriterion("user_answer like", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerNotLike(String value) {
            addCriterion("user_answer not like", value, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerIn(List<String> values) {
            addCriterion("user_answer in", values, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerNotIn(List<String> values) {
            addCriterion("user_answer not in", values, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerBetween(String value1, String value2) {
            addCriterion("user_answer between", value1, value2, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerNotBetween(String value1, String value2) {
            addCriterion("user_answer not between", value1, value2, "userAnswer");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultIsNull() {
            addCriterion("user_answer_result is null");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultIsNotNull() {
            addCriterion("user_answer_result is not null");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultEqualTo(Integer value) {
            addCriterion("user_answer_result =", value, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultNotEqualTo(Integer value) {
            addCriterion("user_answer_result <>", value, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultGreaterThan(Integer value) {
            addCriterion("user_answer_result >", value, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_answer_result >=", value, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultLessThan(Integer value) {
            addCriterion("user_answer_result <", value, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultLessThanOrEqualTo(Integer value) {
            addCriterion("user_answer_result <=", value, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultIn(List<Integer> values) {
            addCriterion("user_answer_result in", values, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultNotIn(List<Integer> values) {
            addCriterion("user_answer_result not in", values, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultBetween(Integer value1, Integer value2) {
            addCriterion("user_answer_result between", value1, value2, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andUserAnswerResultNotBetween(Integer value1, Integer value2) {
            addCriterion("user_answer_result not between", value1, value2, "userAnswerResult");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgIsNull() {
            addCriterion("double_flg is null");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgIsNotNull() {
            addCriterion("double_flg is not null");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgEqualTo(Integer value) {
            addCriterion("double_flg =", value, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgNotEqualTo(Integer value) {
            addCriterion("double_flg <>", value, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgGreaterThan(Integer value) {
            addCriterion("double_flg >", value, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("double_flg >=", value, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgLessThan(Integer value) {
            addCriterion("double_flg <", value, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgLessThanOrEqualTo(Integer value) {
            addCriterion("double_flg <=", value, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgIn(List<Integer> values) {
            addCriterion("double_flg in", values, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgNotIn(List<Integer> values) {
            addCriterion("double_flg not in", values, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgBetween(Integer value1, Integer value2) {
            addCriterion("double_flg between", value1, value2, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andDoubleFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("double_flg not between", value1, value2, "doubleFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgIsNull() {
            addCriterion("send_flg is null");
            return (Criteria) this;
        }

        public Criteria andSendFlgIsNotNull() {
            addCriterion("send_flg is not null");
            return (Criteria) this;
        }

        public Criteria andSendFlgEqualTo(Integer value) {
            addCriterion("send_flg =", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgNotEqualTo(Integer value) {
            addCriterion("send_flg <>", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgGreaterThan(Integer value) {
            addCriterion("send_flg >", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_flg >=", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgLessThan(Integer value) {
            addCriterion("send_flg <", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgLessThanOrEqualTo(Integer value) {
            addCriterion("send_flg <=", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgIn(List<Integer> values) {
            addCriterion("send_flg in", values, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgNotIn(List<Integer> values) {
            addCriterion("send_flg not in", values, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgBetween(Integer value1, Integer value2) {
            addCriterion("send_flg between", value1, value2, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("send_flg not between", value1, value2, "sendFlg");
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