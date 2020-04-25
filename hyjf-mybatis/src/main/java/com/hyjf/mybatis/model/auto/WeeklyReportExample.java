package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WeeklyReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public WeeklyReportExample() {
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

        public Criteria andBeginDateIsNull() {
            addCriterion("begin_date is null");
            return (Criteria) this;
        }

        public Criteria andBeginDateIsNotNull() {
            addCriterion("begin_date is not null");
            return (Criteria) this;
        }

        public Criteria andBeginDateEqualTo(String value) {
            addCriterion("begin_date =", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateNotEqualTo(String value) {
            addCriterion("begin_date <>", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateGreaterThan(String value) {
            addCriterion("begin_date >", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateGreaterThanOrEqualTo(String value) {
            addCriterion("begin_date >=", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateLessThan(String value) {
            addCriterion("begin_date <", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateLessThanOrEqualTo(String value) {
            addCriterion("begin_date <=", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateLike(String value) {
            addCriterion("begin_date like", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateNotLike(String value) {
            addCriterion("begin_date not like", value, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateIn(List<String> values) {
            addCriterion("begin_date in", values, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateNotIn(List<String> values) {
            addCriterion("begin_date not in", values, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateBetween(String value1, String value2) {
            addCriterion("begin_date between", value1, value2, "beginDate");
            return (Criteria) this;
        }

        public Criteria andBeginDateNotBetween(String value1, String value2) {
            addCriterion("begin_date not between", value1, value2, "beginDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNull() {
            addCriterion("end_date is null");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNotNull() {
            addCriterion("end_date is not null");
            return (Criteria) this;
        }

        public Criteria andEndDateEqualTo(String value) {
            addCriterion("end_date =", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotEqualTo(String value) {
            addCriterion("end_date <>", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThan(String value) {
            addCriterion("end_date >", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThanOrEqualTo(String value) {
            addCriterion("end_date >=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThan(String value) {
            addCriterion("end_date <", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThanOrEqualTo(String value) {
            addCriterion("end_date <=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLike(String value) {
            addCriterion("end_date like", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotLike(String value) {
            addCriterion("end_date not like", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIn(List<String> values) {
            addCriterion("end_date in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotIn(List<String> values) {
            addCriterion("end_date not in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateBetween(String value1, String value2) {
            addCriterion("end_date between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotBetween(String value1, String value2) {
            addCriterion("end_date not between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andZongshouyiIsNull() {
            addCriterion("zongshouyi is null");
            return (Criteria) this;
        }

        public Criteria andZongshouyiIsNotNull() {
            addCriterion("zongshouyi is not null");
            return (Criteria) this;
        }

        public Criteria andZongshouyiEqualTo(BigDecimal value) {
            addCriterion("zongshouyi =", value, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiNotEqualTo(BigDecimal value) {
            addCriterion("zongshouyi <>", value, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiGreaterThan(BigDecimal value) {
            addCriterion("zongshouyi >", value, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("zongshouyi >=", value, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiLessThan(BigDecimal value) {
            addCriterion("zongshouyi <", value, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiLessThanOrEqualTo(BigDecimal value) {
            addCriterion("zongshouyi <=", value, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiIn(List<BigDecimal> values) {
            addCriterion("zongshouyi in", values, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiNotIn(List<BigDecimal> values) {
            addCriterion("zongshouyi not in", values, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("zongshouyi between", value1, value2, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andZongshouyiNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("zongshouyi not between", value1, value2, "zongshouyi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiIsNull() {
            addCriterion("baifenbi is null");
            return (Criteria) this;
        }

        public Criteria andBaifenbiIsNotNull() {
            addCriterion("baifenbi is not null");
            return (Criteria) this;
        }

        public Criteria andBaifenbiEqualTo(Integer value) {
            addCriterion("baifenbi =", value, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiNotEqualTo(Integer value) {
            addCriterion("baifenbi <>", value, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiGreaterThan(Integer value) {
            addCriterion("baifenbi >", value, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiGreaterThanOrEqualTo(Integer value) {
            addCriterion("baifenbi >=", value, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiLessThan(Integer value) {
            addCriterion("baifenbi <", value, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiLessThanOrEqualTo(Integer value) {
            addCriterion("baifenbi <=", value, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiIn(List<Integer> values) {
            addCriterion("baifenbi in", values, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiNotIn(List<Integer> values) {
            addCriterion("baifenbi not in", values, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiBetween(Integer value1, Integer value2) {
            addCriterion("baifenbi between", value1, value2, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andBaifenbiNotBetween(Integer value1, Integer value2) {
            addCriterion("baifenbi not between", value1, value2, "baifenbi");
            return (Criteria) this;
        }

        public Criteria andZongtianshuIsNull() {
            addCriterion("zongtianshu is null");
            return (Criteria) this;
        }

        public Criteria andZongtianshuIsNotNull() {
            addCriterion("zongtianshu is not null");
            return (Criteria) this;
        }

        public Criteria andZongtianshuEqualTo(Integer value) {
            addCriterion("zongtianshu =", value, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuNotEqualTo(Integer value) {
            addCriterion("zongtianshu <>", value, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuGreaterThan(Integer value) {
            addCriterion("zongtianshu >", value, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuGreaterThanOrEqualTo(Integer value) {
            addCriterion("zongtianshu >=", value, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuLessThan(Integer value) {
            addCriterion("zongtianshu <", value, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuLessThanOrEqualTo(Integer value) {
            addCriterion("zongtianshu <=", value, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuIn(List<Integer> values) {
            addCriterion("zongtianshu in", values, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuNotIn(List<Integer> values) {
            addCriterion("zongtianshu not in", values, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuBetween(Integer value1, Integer value2) {
            addCriterion("zongtianshu between", value1, value2, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongtianshuNotBetween(Integer value1, Integer value2) {
            addCriterion("zongtianshu not between", value1, value2, "zongtianshu");
            return (Criteria) this;
        }

        public Criteria andZongjineIsNull() {
            addCriterion("zongjine is null");
            return (Criteria) this;
        }

        public Criteria andZongjineIsNotNull() {
            addCriterion("zongjine is not null");
            return (Criteria) this;
        }

        public Criteria andZongjineEqualTo(BigDecimal value) {
            addCriterion("zongjine =", value, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineNotEqualTo(BigDecimal value) {
            addCriterion("zongjine <>", value, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineGreaterThan(BigDecimal value) {
            addCriterion("zongjine >", value, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("zongjine >=", value, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineLessThan(BigDecimal value) {
            addCriterion("zongjine <", value, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineLessThanOrEqualTo(BigDecimal value) {
            addCriterion("zongjine <=", value, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineIn(List<BigDecimal> values) {
            addCriterion("zongjine in", values, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineNotIn(List<BigDecimal> values) {
            addCriterion("zongjine not in", values, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("zongjine between", value1, value2, "zongjine");
            return (Criteria) this;
        }

        public Criteria andZongjineNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("zongjine not between", value1, value2, "zongjine");
            return (Criteria) this;
        }

        public Criteria andTouzieIsNull() {
            addCriterion("touzie is null");
            return (Criteria) this;
        }

        public Criteria andTouzieIsNotNull() {
            addCriterion("touzie is not null");
            return (Criteria) this;
        }

        public Criteria andTouzieEqualTo(BigDecimal value) {
            addCriterion("touzie =", value, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieNotEqualTo(BigDecimal value) {
            addCriterion("touzie <>", value, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieGreaterThan(BigDecimal value) {
            addCriterion("touzie >", value, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("touzie >=", value, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieLessThan(BigDecimal value) {
            addCriterion("touzie <", value, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieLessThanOrEqualTo(BigDecimal value) {
            addCriterion("touzie <=", value, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieIn(List<BigDecimal> values) {
            addCriterion("touzie in", values, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieNotIn(List<BigDecimal> values) {
            addCriterion("touzie not in", values, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("touzie between", value1, value2, "touzie");
            return (Criteria) this;
        }

        public Criteria andTouzieNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("touzie not between", value1, value2, "touzie");
            return (Criteria) this;
        }

        public Criteria andBishuIsNull() {
            addCriterion("bishu is null");
            return (Criteria) this;
        }

        public Criteria andBishuIsNotNull() {
            addCriterion("bishu is not null");
            return (Criteria) this;
        }

        public Criteria andBishuEqualTo(Integer value) {
            addCriterion("bishu =", value, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuNotEqualTo(Integer value) {
            addCriterion("bishu <>", value, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuGreaterThan(Integer value) {
            addCriterion("bishu >", value, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuGreaterThanOrEqualTo(Integer value) {
            addCriterion("bishu >=", value, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuLessThan(Integer value) {
            addCriterion("bishu <", value, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuLessThanOrEqualTo(Integer value) {
            addCriterion("bishu <=", value, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuIn(List<Integer> values) {
            addCriterion("bishu in", values, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuNotIn(List<Integer> values) {
            addCriterion("bishu not in", values, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuBetween(Integer value1, Integer value2) {
            addCriterion("bishu between", value1, value2, "bishu");
            return (Criteria) this;
        }

        public Criteria andBishuNotBetween(Integer value1, Integer value2) {
            addCriterion("bishu not between", value1, value2, "bishu");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeIsNull() {
            addCriterion("huankuanzonge is null");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeIsNotNull() {
            addCriterion("huankuanzonge is not null");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeEqualTo(BigDecimal value) {
            addCriterion("huankuanzonge =", value, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeNotEqualTo(BigDecimal value) {
            addCriterion("huankuanzonge <>", value, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeGreaterThan(BigDecimal value) {
            addCriterion("huankuanzonge >", value, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("huankuanzonge >=", value, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeLessThan(BigDecimal value) {
            addCriterion("huankuanzonge <", value, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("huankuanzonge <=", value, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeIn(List<BigDecimal> values) {
            addCriterion("huankuanzonge in", values, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeNotIn(List<BigDecimal> values) {
            addCriterion("huankuanzonge not in", values, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("huankuanzonge between", value1, value2, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andHuankuanzongeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("huankuanzonge not between", value1, value2, "huankuanzonge");
            return (Criteria) this;
        }

        public Criteria andShouyiIsNull() {
            addCriterion("shouyi is null");
            return (Criteria) this;
        }

        public Criteria andShouyiIsNotNull() {
            addCriterion("shouyi is not null");
            return (Criteria) this;
        }

        public Criteria andShouyiEqualTo(BigDecimal value) {
            addCriterion("shouyi =", value, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiNotEqualTo(BigDecimal value) {
            addCriterion("shouyi <>", value, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiGreaterThan(BigDecimal value) {
            addCriterion("shouyi >", value, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("shouyi >=", value, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiLessThan(BigDecimal value) {
            addCriterion("shouyi <", value, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiLessThanOrEqualTo(BigDecimal value) {
            addCriterion("shouyi <=", value, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiIn(List<BigDecimal> values) {
            addCriterion("shouyi in", values, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiNotIn(List<BigDecimal> values) {
            addCriterion("shouyi not in", values, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("shouyi between", value1, value2, "shouyi");
            return (Criteria) this;
        }

        public Criteria andShouyiNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("shouyi not between", value1, value2, "shouyi");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanIsNull() {
            addCriterion("youhuiquan is null");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanIsNotNull() {
            addCriterion("youhuiquan is not null");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanEqualTo(Integer value) {
            addCriterion("youhuiquan =", value, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanNotEqualTo(Integer value) {
            addCriterion("youhuiquan <>", value, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanGreaterThan(Integer value) {
            addCriterion("youhuiquan >", value, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanGreaterThanOrEqualTo(Integer value) {
            addCriterion("youhuiquan >=", value, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanLessThan(Integer value) {
            addCriterion("youhuiquan <", value, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanLessThanOrEqualTo(Integer value) {
            addCriterion("youhuiquan <=", value, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanIn(List<Integer> values) {
            addCriterion("youhuiquan in", values, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanNotIn(List<Integer> values) {
            addCriterion("youhuiquan not in", values, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanBetween(Integer value1, Integer value2) {
            addCriterion("youhuiquan between", value1, value2, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andYouhuiquanNotBetween(Integer value1, Integer value2) {
            addCriterion("youhuiquan not between", value1, value2, "youhuiquan");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangIsNull() {
            addCriterion("huankuangaikuang is null");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangIsNotNull() {
            addCriterion("huankuangaikuang is not null");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangEqualTo(String value) {
            addCriterion("huankuangaikuang =", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangNotEqualTo(String value) {
            addCriterion("huankuangaikuang <>", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangGreaterThan(String value) {
            addCriterion("huankuangaikuang >", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangGreaterThanOrEqualTo(String value) {
            addCriterion("huankuangaikuang >=", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangLessThan(String value) {
            addCriterion("huankuangaikuang <", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangLessThanOrEqualTo(String value) {
            addCriterion("huankuangaikuang <=", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangLike(String value) {
            addCriterion("huankuangaikuang like", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangNotLike(String value) {
            addCriterion("huankuangaikuang not like", value, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangIn(List<String> values) {
            addCriterion("huankuangaikuang in", values, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangNotIn(List<String> values) {
            addCriterion("huankuangaikuang not in", values, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangBetween(String value1, String value2) {
            addCriterion("huankuangaikuang between", value1, value2, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andHuankuangaikuangNotBetween(String value1, String value2) {
            addCriterion("huankuangaikuang not between", value1, value2, "huankuangaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangIsNull() {
            addCriterion("touzigaikuang is null");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangIsNotNull() {
            addCriterion("touzigaikuang is not null");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangEqualTo(String value) {
            addCriterion("touzigaikuang =", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangNotEqualTo(String value) {
            addCriterion("touzigaikuang <>", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangGreaterThan(String value) {
            addCriterion("touzigaikuang >", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangGreaterThanOrEqualTo(String value) {
            addCriterion("touzigaikuang >=", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangLessThan(String value) {
            addCriterion("touzigaikuang <", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangLessThanOrEqualTo(String value) {
            addCriterion("touzigaikuang <=", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangLike(String value) {
            addCriterion("touzigaikuang like", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangNotLike(String value) {
            addCriterion("touzigaikuang not like", value, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangIn(List<String> values) {
            addCriterion("touzigaikuang in", values, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangNotIn(List<String> values) {
            addCriterion("touzigaikuang not in", values, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangBetween(String value1, String value2) {
            addCriterion("touzigaikuang between", value1, value2, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andTouzigaikuangNotBetween(String value1, String value2) {
            addCriterion("touzigaikuang not between", value1, value2, "touzigaikuang");
            return (Criteria) this;
        }

        public Criteria andDatestringIsNull() {
            addCriterion("dateString is null");
            return (Criteria) this;
        }

        public Criteria andDatestringIsNotNull() {
            addCriterion("dateString is not null");
            return (Criteria) this;
        }

        public Criteria andDatestringEqualTo(String value) {
            addCriterion("dateString =", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringNotEqualTo(String value) {
            addCriterion("dateString <>", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringGreaterThan(String value) {
            addCriterion("dateString >", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringGreaterThanOrEqualTo(String value) {
            addCriterion("dateString >=", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringLessThan(String value) {
            addCriterion("dateString <", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringLessThanOrEqualTo(String value) {
            addCriterion("dateString <=", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringLike(String value) {
            addCriterion("dateString like", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringNotLike(String value) {
            addCriterion("dateString not like", value, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringIn(List<String> values) {
            addCriterion("dateString in", values, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringNotIn(List<String> values) {
            addCriterion("dateString not in", values, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringBetween(String value1, String value2) {
            addCriterion("dateString between", value1, value2, "datestring");
            return (Criteria) this;
        }

        public Criteria andDatestringNotBetween(String value1, String value2) {
            addCriterion("dateString not between", value1, value2, "datestring");
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