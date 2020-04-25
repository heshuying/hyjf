package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ActdecTenderBalloonExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActdecTenderBalloonExample() {
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

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
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

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andTenderNidIsNull() {
            addCriterion("tender_nid is null");
            return (Criteria) this;
        }

        public Criteria andTenderNidIsNotNull() {
            addCriterion("tender_nid is not null");
            return (Criteria) this;
        }

        public Criteria andTenderNidEqualTo(String value) {
            addCriterion("tender_nid =", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidNotEqualTo(String value) {
            addCriterion("tender_nid <>", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidGreaterThan(String value) {
            addCriterion("tender_nid >", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidGreaterThanOrEqualTo(String value) {
            addCriterion("tender_nid >=", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidLessThan(String value) {
            addCriterion("tender_nid <", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidLessThanOrEqualTo(String value) {
            addCriterion("tender_nid <=", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidLike(String value) {
            addCriterion("tender_nid like", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidNotLike(String value) {
            addCriterion("tender_nid not like", value, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidIn(List<String> values) {
            addCriterion("tender_nid in", values, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidNotIn(List<String> values) {
            addCriterion("tender_nid not in", values, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidBetween(String value1, String value2) {
            addCriterion("tender_nid between", value1, value2, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderNidNotBetween(String value1, String value2) {
            addCriterion("tender_nid not between", value1, value2, "tenderNid");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyIsNull() {
            addCriterion("tender_money is null");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyIsNotNull() {
            addCriterion("tender_money is not null");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyEqualTo(BigDecimal value) {
            addCriterion("tender_money =", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyNotEqualTo(BigDecimal value) {
            addCriterion("tender_money <>", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyGreaterThan(BigDecimal value) {
            addCriterion("tender_money >", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_money >=", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyLessThan(BigDecimal value) {
            addCriterion("tender_money <", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tender_money <=", value, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyIn(List<BigDecimal> values) {
            addCriterion("tender_money in", values, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyNotIn(List<BigDecimal> values) {
            addCriterion("tender_money not in", values, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_money between", value1, value2, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andTenderMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tender_money not between", value1, value2, "tenderMoney");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderIsNull() {
            addCriterion("is_first_tender is null");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderIsNotNull() {
            addCriterion("is_first_tender is not null");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderEqualTo(Integer value) {
            addCriterion("is_first_tender =", value, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderNotEqualTo(Integer value) {
            addCriterion("is_first_tender <>", value, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderGreaterThan(Integer value) {
            addCriterion("is_first_tender >", value, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_first_tender >=", value, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderLessThan(Integer value) {
            addCriterion("is_first_tender <", value, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderLessThanOrEqualTo(Integer value) {
            addCriterion("is_first_tender <=", value, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderIn(List<Integer> values) {
            addCriterion("is_first_tender in", values, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderNotIn(List<Integer> values) {
            addCriterion("is_first_tender not in", values, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderBetween(Integer value1, Integer value2) {
            addCriterion("is_first_tender between", value1, value2, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andIsFirstTenderNotBetween(Integer value1, Integer value2) {
            addCriterion("is_first_tender not between", value1, value2, "isFirstTender");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIsNull() {
            addCriterion("tender_type is null");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIsNotNull() {
            addCriterion("tender_type is not null");
            return (Criteria) this;
        }

        public Criteria andTenderTypeEqualTo(Integer value) {
            addCriterion("tender_type =", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotEqualTo(Integer value) {
            addCriterion("tender_type <>", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeGreaterThan(Integer value) {
            addCriterion("tender_type >", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_type >=", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeLessThan(Integer value) {
            addCriterion("tender_type <", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeLessThanOrEqualTo(Integer value) {
            addCriterion("tender_type <=", value, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeIn(List<Integer> values) {
            addCriterion("tender_type in", values, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotIn(List<Integer> values) {
            addCriterion("tender_type not in", values, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeBetween(Integer value1, Integer value2) {
            addCriterion("tender_type between", value1, value2, "tenderType");
            return (Criteria) this;
        }

        public Criteria andTenderTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_type not between", value1, value2, "tenderType");
            return (Criteria) this;
        }

        public Criteria andBalloonCountIsNull() {
            addCriterion("balloon_count is null");
            return (Criteria) this;
        }

        public Criteria andBalloonCountIsNotNull() {
            addCriterion("balloon_count is not null");
            return (Criteria) this;
        }

        public Criteria andBalloonCountEqualTo(Integer value) {
            addCriterion("balloon_count =", value, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountNotEqualTo(Integer value) {
            addCriterion("balloon_count <>", value, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountGreaterThan(Integer value) {
            addCriterion("balloon_count >", value, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("balloon_count >=", value, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountLessThan(Integer value) {
            addCriterion("balloon_count <", value, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountLessThanOrEqualTo(Integer value) {
            addCriterion("balloon_count <=", value, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountIn(List<Integer> values) {
            addCriterion("balloon_count in", values, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountNotIn(List<Integer> values) {
            addCriterion("balloon_count not in", values, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountBetween(Integer value1, Integer value2) {
            addCriterion("balloon_count between", value1, value2, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBalloonCountNotBetween(Integer value1, Integer value2) {
            addCriterion("balloon_count not between", value1, value2, "balloonCount");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveIsNull() {
            addCriterion("ballon_can_receive is null");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveIsNotNull() {
            addCriterion("ballon_can_receive is not null");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveEqualTo(Integer value) {
            addCriterion("ballon_can_receive =", value, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveNotEqualTo(Integer value) {
            addCriterion("ballon_can_receive <>", value, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveGreaterThan(Integer value) {
            addCriterion("ballon_can_receive >", value, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveGreaterThanOrEqualTo(Integer value) {
            addCriterion("ballon_can_receive >=", value, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveLessThan(Integer value) {
            addCriterion("ballon_can_receive <", value, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveLessThanOrEqualTo(Integer value) {
            addCriterion("ballon_can_receive <=", value, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveIn(List<Integer> values) {
            addCriterion("ballon_can_receive in", values, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveNotIn(List<Integer> values) {
            addCriterion("ballon_can_receive not in", values, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveBetween(Integer value1, Integer value2) {
            addCriterion("ballon_can_receive between", value1, value2, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonCanReceiveNotBetween(Integer value1, Integer value2) {
            addCriterion("ballon_can_receive not between", value1, value2, "ballonCanReceive");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedIsNull() {
            addCriterion("ballon_received is null");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedIsNotNull() {
            addCriterion("ballon_received is not null");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedEqualTo(Integer value) {
            addCriterion("ballon_received =", value, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedNotEqualTo(Integer value) {
            addCriterion("ballon_received <>", value, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedGreaterThan(Integer value) {
            addCriterion("ballon_received >", value, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedGreaterThanOrEqualTo(Integer value) {
            addCriterion("ballon_received >=", value, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedLessThan(Integer value) {
            addCriterion("ballon_received <", value, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedLessThanOrEqualTo(Integer value) {
            addCriterion("ballon_received <=", value, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedIn(List<Integer> values) {
            addCriterion("ballon_received in", values, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedNotIn(List<Integer> values) {
            addCriterion("ballon_received not in", values, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedBetween(Integer value1, Integer value2) {
            addCriterion("ballon_received between", value1, value2, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andBallonReceivedNotBetween(Integer value1, Integer value2) {
            addCriterion("ballon_received not between", value1, value2, "ballonReceived");
            return (Criteria) this;
        }

        public Criteria andRewardNameIsNull() {
            addCriterion("reward_name is null");
            return (Criteria) this;
        }

        public Criteria andRewardNameIsNotNull() {
            addCriterion("reward_name is not null");
            return (Criteria) this;
        }

        public Criteria andRewardNameEqualTo(String value) {
            addCriterion("reward_name =", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameNotEqualTo(String value) {
            addCriterion("reward_name <>", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameGreaterThan(String value) {
            addCriterion("reward_name >", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameGreaterThanOrEqualTo(String value) {
            addCriterion("reward_name >=", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameLessThan(String value) {
            addCriterion("reward_name <", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameLessThanOrEqualTo(String value) {
            addCriterion("reward_name <=", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameLike(String value) {
            addCriterion("reward_name like", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameNotLike(String value) {
            addCriterion("reward_name not like", value, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameIn(List<String> values) {
            addCriterion("reward_name in", values, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameNotIn(List<String> values) {
            addCriterion("reward_name not in", values, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameBetween(String value1, String value2) {
            addCriterion("reward_name between", value1, value2, "rewardName");
            return (Criteria) this;
        }

        public Criteria andRewardNameNotBetween(String value1, String value2) {
            addCriterion("reward_name not between", value1, value2, "rewardName");
            return (Criteria) this;
        }

        public Criteria andSendStatusIsNull() {
            addCriterion("send_status is null");
            return (Criteria) this;
        }

        public Criteria andSendStatusIsNotNull() {
            addCriterion("send_status is not null");
            return (Criteria) this;
        }

        public Criteria andSendStatusEqualTo(Integer value) {
            addCriterion("send_status =", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotEqualTo(Integer value) {
            addCriterion("send_status <>", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusGreaterThan(Integer value) {
            addCriterion("send_status >", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_status >=", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusLessThan(Integer value) {
            addCriterion("send_status <", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusLessThanOrEqualTo(Integer value) {
            addCriterion("send_status <=", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusIn(List<Integer> values) {
            addCriterion("send_status in", values, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotIn(List<Integer> values) {
            addCriterion("send_status not in", values, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusBetween(Integer value1, Integer value2) {
            addCriterion("send_status between", value1, value2, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("send_status not between", value1, value2, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andTenderTimeIsNull() {
            addCriterion("tender_time is null");
            return (Criteria) this;
        }

        public Criteria andTenderTimeIsNotNull() {
            addCriterion("tender_time is not null");
            return (Criteria) this;
        }

        public Criteria andTenderTimeEqualTo(Integer value) {
            addCriterion("tender_time =", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotEqualTo(Integer value) {
            addCriterion("tender_time <>", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeGreaterThan(Integer value) {
            addCriterion("tender_time >", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_time >=", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeLessThan(Integer value) {
            addCriterion("tender_time <", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeLessThanOrEqualTo(Integer value) {
            addCriterion("tender_time <=", value, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeIn(List<Integer> values) {
            addCriterion("tender_time in", values, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotIn(List<Integer> values) {
            addCriterion("tender_time not in", values, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeBetween(Integer value1, Integer value2) {
            addCriterion("tender_time between", value1, value2, "tenderTime");
            return (Criteria) this;
        }

        public Criteria andTenderTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_time not between", value1, value2, "tenderTime");
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