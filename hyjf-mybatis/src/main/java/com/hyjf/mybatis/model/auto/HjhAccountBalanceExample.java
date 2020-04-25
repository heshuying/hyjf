package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HjhAccountBalanceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public HjhAccountBalanceExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andDateIsNull() {
            addCriterion("`date` is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("`date` is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(Date value) {
            addCriterionForJDBCDate("`date` =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("`date` <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(Date value) {
            addCriterionForJDBCDate("`date` >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("`date` >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(Date value) {
            addCriterionForJDBCDate("`date` <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("`date` <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<Date> values) {
            addCriterionForJDBCDate("`date` in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("`date` not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("`date` between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("`date` not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andInvestAccountIsNull() {
            addCriterion("invest_account is null");
            return (Criteria) this;
        }

        public Criteria andInvestAccountIsNotNull() {
            addCriterion("invest_account is not null");
            return (Criteria) this;
        }

        public Criteria andInvestAccountEqualTo(BigDecimal value) {
            addCriterion("invest_account =", value, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountNotEqualTo(BigDecimal value) {
            addCriterion("invest_account <>", value, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountGreaterThan(BigDecimal value) {
            addCriterion("invest_account >", value, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("invest_account >=", value, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountLessThan(BigDecimal value) {
            addCriterion("invest_account <", value, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("invest_account <=", value, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountIn(List<BigDecimal> values) {
            addCriterion("invest_account in", values, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountNotIn(List<BigDecimal> values) {
            addCriterion("invest_account not in", values, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("invest_account between", value1, value2, "investAccount");
            return (Criteria) this;
        }

        public Criteria andInvestAccountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("invest_account not between", value1, value2, "investAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountIsNull() {
            addCriterion("credit_account is null");
            return (Criteria) this;
        }

        public Criteria andCreditAccountIsNotNull() {
            addCriterion("credit_account is not null");
            return (Criteria) this;
        }

        public Criteria andCreditAccountEqualTo(BigDecimal value) {
            addCriterion("credit_account =", value, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountNotEqualTo(BigDecimal value) {
            addCriterion("credit_account <>", value, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountGreaterThan(BigDecimal value) {
            addCriterion("credit_account >", value, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("credit_account >=", value, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountLessThan(BigDecimal value) {
            addCriterion("credit_account <", value, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("credit_account <=", value, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountIn(List<BigDecimal> values) {
            addCriterion("credit_account in", values, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountNotIn(List<BigDecimal> values) {
            addCriterion("credit_account not in", values, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("credit_account between", value1, value2, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andCreditAccountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("credit_account not between", value1, value2, "creditAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountIsNull() {
            addCriterion("reinvest_account is null");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountIsNotNull() {
            addCriterion("reinvest_account is not null");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountEqualTo(BigDecimal value) {
            addCriterion("reinvest_account =", value, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountNotEqualTo(BigDecimal value) {
            addCriterion("reinvest_account <>", value, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountGreaterThan(BigDecimal value) {
            addCriterion("reinvest_account >", value, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reinvest_account >=", value, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountLessThan(BigDecimal value) {
            addCriterion("reinvest_account <", value, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reinvest_account <=", value, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountIn(List<BigDecimal> values) {
            addCriterion("reinvest_account in", values, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountNotIn(List<BigDecimal> values) {
            addCriterion("reinvest_account not in", values, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reinvest_account between", value1, value2, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andReinvestAccountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reinvest_account not between", value1, value2, "reinvestAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountIsNull() {
            addCriterion("add_account is null");
            return (Criteria) this;
        }

        public Criteria andAddAccountIsNotNull() {
            addCriterion("add_account is not null");
            return (Criteria) this;
        }

        public Criteria andAddAccountEqualTo(BigDecimal value) {
            addCriterion("add_account =", value, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountNotEqualTo(BigDecimal value) {
            addCriterion("add_account <>", value, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountGreaterThan(BigDecimal value) {
            addCriterion("add_account >", value, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("add_account >=", value, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountLessThan(BigDecimal value) {
            addCriterion("add_account <", value, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("add_account <=", value, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountIn(List<BigDecimal> values) {
            addCriterion("add_account in", values, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountNotIn(List<BigDecimal> values) {
            addCriterion("add_account not in", values, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("add_account between", value1, value2, "addAccount");
            return (Criteria) this;
        }

        public Criteria andAddAccountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("add_account not between", value1, value2, "addAccount");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(Integer value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(Integer value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(Integer value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(Integer value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(Integer value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<Integer> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<Integer> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(Integer value1, Integer value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(Integer value1, Integer value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
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

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(Integer value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(Integer value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(Integer value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(Integer value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(Integer value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<Integer> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<Integer> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(Integer value1, Integer value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(Integer value1, Integer value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
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