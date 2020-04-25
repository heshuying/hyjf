package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class LoanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public LoanExample() {
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

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andTelIsNull() {
            addCriterion("tel is null");
            return (Criteria) this;
        }

        public Criteria andTelIsNotNull() {
            addCriterion("tel is not null");
            return (Criteria) this;
        }

        public Criteria andTelEqualTo(String value) {
            addCriterion("tel =", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotEqualTo(String value) {
            addCriterion("tel <>", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelGreaterThan(String value) {
            addCriterion("tel >", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelGreaterThanOrEqualTo(String value) {
            addCriterion("tel >=", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelLessThan(String value) {
            addCriterion("tel <", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelLessThanOrEqualTo(String value) {
            addCriterion("tel <=", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelLike(String value) {
            addCriterion("tel like", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotLike(String value) {
            addCriterion("tel not like", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelIn(List<String> values) {
            addCriterion("tel in", values, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotIn(List<String> values) {
            addCriterion("tel not in", values, "tel");
            return (Criteria) this;
        }

        public Criteria andTelBetween(String value1, String value2) {
            addCriterion("tel between", value1, value2, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotBetween(String value1, String value2) {
            addCriterion("tel not between", value1, value2, "tel");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(Integer value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(Integer value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(Integer value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(Integer value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(Integer value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(Integer value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<Integer> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<Integer> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(Integer value1, Integer value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(Integer value1, Integer value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andAgeIsNull() {
            addCriterion("age is null");
            return (Criteria) this;
        }

        public Criteria andAgeIsNotNull() {
            addCriterion("age is not null");
            return (Criteria) this;
        }

        public Criteria andAgeEqualTo(Integer value) {
            addCriterion("age =", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotEqualTo(Integer value) {
            addCriterion("age <>", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThan(Integer value) {
            addCriterion("age >", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThanOrEqualTo(Integer value) {
            addCriterion("age >=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThan(Integer value) {
            addCriterion("age <", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThanOrEqualTo(Integer value) {
            addCriterion("age <=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeIn(List<Integer> values) {
            addCriterion("age in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotIn(List<Integer> values) {
            addCriterion("age not in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeBetween(Integer value1, Integer value2) {
            addCriterion("age between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotBetween(Integer value1, Integer value2) {
            addCriterion("age not between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(String value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(String value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(String value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(String value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(String value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(String value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLike(String value) {
            addCriterion("money like", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotLike(String value) {
            addCriterion("money not like", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<String> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<String> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(String value1, String value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(String value1, String value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andDayIsNull() {
            addCriterion("`day` is null");
            return (Criteria) this;
        }

        public Criteria andDayIsNotNull() {
            addCriterion("`day` is not null");
            return (Criteria) this;
        }

        public Criteria andDayEqualTo(String value) {
            addCriterion("`day` =", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotEqualTo(String value) {
            addCriterion("`day` <>", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayGreaterThan(String value) {
            addCriterion("`day` >", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayGreaterThanOrEqualTo(String value) {
            addCriterion("`day` >=", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLessThan(String value) {
            addCriterion("`day` <", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLessThanOrEqualTo(String value) {
            addCriterion("`day` <=", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLike(String value) {
            addCriterion("`day` like", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotLike(String value) {
            addCriterion("`day` not like", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayIn(List<String> values) {
            addCriterion("`day` in", values, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotIn(List<String> values) {
            addCriterion("`day` not in", values, "day");
            return (Criteria) this;
        }

        public Criteria andDayBetween(String value1, String value2) {
            addCriterion("`day` between", value1, value2, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotBetween(String value1, String value2) {
            addCriterion("`day` not between", value1, value2, "day");
            return (Criteria) this;
        }

        public Criteria andUseIsNull() {
            addCriterion("`use` is null");
            return (Criteria) this;
        }

        public Criteria andUseIsNotNull() {
            addCriterion("`use` is not null");
            return (Criteria) this;
        }

        public Criteria andUseEqualTo(String value) {
            addCriterion("`use` =", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseNotEqualTo(String value) {
            addCriterion("`use` <>", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseGreaterThan(String value) {
            addCriterion("`use` >", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseGreaterThanOrEqualTo(String value) {
            addCriterion("`use` >=", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseLessThan(String value) {
            addCriterion("`use` <", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseLessThanOrEqualTo(String value) {
            addCriterion("`use` <=", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseLike(String value) {
            addCriterion("`use` like", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseNotLike(String value) {
            addCriterion("`use` not like", value, "use");
            return (Criteria) this;
        }

        public Criteria andUseIn(List<String> values) {
            addCriterion("`use` in", values, "use");
            return (Criteria) this;
        }

        public Criteria andUseNotIn(List<String> values) {
            addCriterion("`use` not in", values, "use");
            return (Criteria) this;
        }

        public Criteria andUseBetween(String value1, String value2) {
            addCriterion("`use` between", value1, value2, "use");
            return (Criteria) this;
        }

        public Criteria andUseNotBetween(String value1, String value2) {
            addCriterion("`use` not between", value1, value2, "use");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("city is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("city is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("city =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("city <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("city >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("city >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("city <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("city <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("city not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("city in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("city not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("city between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("city not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andAreaIsNull() {
            addCriterion("area is null");
            return (Criteria) this;
        }

        public Criteria andAreaIsNotNull() {
            addCriterion("area is not null");
            return (Criteria) this;
        }

        public Criteria andAreaEqualTo(String value) {
            addCriterion("area =", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotEqualTo(String value) {
            addCriterion("area <>", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaGreaterThan(String value) {
            addCriterion("area >", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaGreaterThanOrEqualTo(String value) {
            addCriterion("area >=", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaLessThan(String value) {
            addCriterion("area <", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaLessThanOrEqualTo(String value) {
            addCriterion("area <=", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaLike(String value) {
            addCriterion("area like", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotLike(String value) {
            addCriterion("area not like", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaIn(List<String> values) {
            addCriterion("area in", values, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotIn(List<String> values) {
            addCriterion("area not in", values, "area");
            return (Criteria) this;
        }

        public Criteria andAreaBetween(String value1, String value2) {
            addCriterion("area between", value1, value2, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotBetween(String value1, String value2) {
            addCriterion("area not between", value1, value2, "area");
            return (Criteria) this;
        }

        public Criteria andMortgageIsNull() {
            addCriterion("mortgage is null");
            return (Criteria) this;
        }

        public Criteria andMortgageIsNotNull() {
            addCriterion("mortgage is not null");
            return (Criteria) this;
        }

        public Criteria andMortgageEqualTo(String value) {
            addCriterion("mortgage =", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageNotEqualTo(String value) {
            addCriterion("mortgage <>", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageGreaterThan(String value) {
            addCriterion("mortgage >", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageGreaterThanOrEqualTo(String value) {
            addCriterion("mortgage >=", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageLessThan(String value) {
            addCriterion("mortgage <", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageLessThanOrEqualTo(String value) {
            addCriterion("mortgage <=", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageLike(String value) {
            addCriterion("mortgage like", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageNotLike(String value) {
            addCriterion("mortgage not like", value, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageIn(List<String> values) {
            addCriterion("mortgage in", values, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageNotIn(List<String> values) {
            addCriterion("mortgage not in", values, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageBetween(String value1, String value2) {
            addCriterion("mortgage between", value1, value2, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageNotBetween(String value1, String value2) {
            addCriterion("mortgage not between", value1, value2, "mortgage");
            return (Criteria) this;
        }

        public Criteria andMortgageStateIsNull() {
            addCriterion("mortgage_state is null");
            return (Criteria) this;
        }

        public Criteria andMortgageStateIsNotNull() {
            addCriterion("mortgage_state is not null");
            return (Criteria) this;
        }

        public Criteria andMortgageStateEqualTo(Integer value) {
            addCriterion("mortgage_state =", value, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateNotEqualTo(Integer value) {
            addCriterion("mortgage_state <>", value, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateGreaterThan(Integer value) {
            addCriterion("mortgage_state >", value, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("mortgage_state >=", value, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateLessThan(Integer value) {
            addCriterion("mortgage_state <", value, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateLessThanOrEqualTo(Integer value) {
            addCriterion("mortgage_state <=", value, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateIn(List<Integer> values) {
            addCriterion("mortgage_state in", values, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateNotIn(List<Integer> values) {
            addCriterion("mortgage_state not in", values, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateBetween(Integer value1, Integer value2) {
            addCriterion("mortgage_state between", value1, value2, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andMortgageStateNotBetween(Integer value1, Integer value2) {
            addCriterion("mortgage_state not between", value1, value2, "mortgageState");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("`state` is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("`state` is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Integer value) {
            addCriterion("`state` =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Integer value) {
            addCriterion("`state` <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Integer value) {
            addCriterion("`state` >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("`state` >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Integer value) {
            addCriterion("`state` <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Integer value) {
            addCriterion("`state` <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Integer> values) {
            addCriterion("`state` in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Integer> values) {
            addCriterion("`state` not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Integer value1, Integer value2) {
            addCriterion("`state` between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Integer value1, Integer value2) {
            addCriterion("`state` not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNull() {
            addCriterion("addtime is null");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNotNull() {
            addCriterion("addtime is not null");
            return (Criteria) this;
        }

        public Criteria andAddtimeEqualTo(Integer value) {
            addCriterion("addtime =", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotEqualTo(Integer value) {
            addCriterion("addtime <>", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThan(Integer value) {
            addCriterion("addtime >", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("addtime >=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThan(Integer value) {
            addCriterion("addtime <", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThanOrEqualTo(Integer value) {
            addCriterion("addtime <=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeIn(List<Integer> values) {
            addCriterion("addtime in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotIn(List<Integer> values) {
            addCriterion("addtime not in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeBetween(Integer value1, Integer value2) {
            addCriterion("addtime between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotBetween(Integer value1, Integer value2) {
            addCriterion("addtime not between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddipIsNull() {
            addCriterion("addip is null");
            return (Criteria) this;
        }

        public Criteria andAddipIsNotNull() {
            addCriterion("addip is not null");
            return (Criteria) this;
        }

        public Criteria andAddipEqualTo(String value) {
            addCriterion("addip =", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotEqualTo(String value) {
            addCriterion("addip <>", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipGreaterThan(String value) {
            addCriterion("addip >", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipGreaterThanOrEqualTo(String value) {
            addCriterion("addip >=", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipLessThan(String value) {
            addCriterion("addip <", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipLessThanOrEqualTo(String value) {
            addCriterion("addip <=", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipLike(String value) {
            addCriterion("addip like", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotLike(String value) {
            addCriterion("addip not like", value, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipIn(List<String> values) {
            addCriterion("addip in", values, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotIn(List<String> values) {
            addCriterion("addip not in", values, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipBetween(String value1, String value2) {
            addCriterion("addip between", value1, value2, "addip");
            return (Criteria) this;
        }

        public Criteria andAddipNotBetween(String value1, String value2) {
            addCriterion("addip not between", value1, value2, "addip");
            return (Criteria) this;
        }

        public Criteria andInfoIsNull() {
            addCriterion("info is null");
            return (Criteria) this;
        }

        public Criteria andInfoIsNotNull() {
            addCriterion("info is not null");
            return (Criteria) this;
        }

        public Criteria andInfoEqualTo(String value) {
            addCriterion("info =", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotEqualTo(String value) {
            addCriterion("info <>", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoGreaterThan(String value) {
            addCriterion("info >", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoGreaterThanOrEqualTo(String value) {
            addCriterion("info >=", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLessThan(String value) {
            addCriterion("info <", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLessThanOrEqualTo(String value) {
            addCriterion("info <=", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLike(String value) {
            addCriterion("info like", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotLike(String value) {
            addCriterion("info not like", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoIn(List<String> values) {
            addCriterion("info in", values, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotIn(List<String> values) {
            addCriterion("info not in", values, "info");
            return (Criteria) this;
        }

        public Criteria andInfoBetween(String value1, String value2) {
            addCriterion("info between", value1, value2, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotBetween(String value1, String value2) {
            addCriterion("info not between", value1, value2, "info");
            return (Criteria) this;
        }

        public Criteria andGnameIsNull() {
            addCriterion("gname is null");
            return (Criteria) this;
        }

        public Criteria andGnameIsNotNull() {
            addCriterion("gname is not null");
            return (Criteria) this;
        }

        public Criteria andGnameEqualTo(String value) {
            addCriterion("gname =", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameNotEqualTo(String value) {
            addCriterion("gname <>", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameGreaterThan(String value) {
            addCriterion("gname >", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameGreaterThanOrEqualTo(String value) {
            addCriterion("gname >=", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameLessThan(String value) {
            addCriterion("gname <", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameLessThanOrEqualTo(String value) {
            addCriterion("gname <=", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameLike(String value) {
            addCriterion("gname like", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameNotLike(String value) {
            addCriterion("gname not like", value, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameIn(List<String> values) {
            addCriterion("gname in", values, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameNotIn(List<String> values) {
            addCriterion("gname not in", values, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameBetween(String value1, String value2) {
            addCriterion("gname between", value1, value2, "gname");
            return (Criteria) this;
        }

        public Criteria andGnameNotBetween(String value1, String value2) {
            addCriterion("gname not between", value1, value2, "gname");
            return (Criteria) this;
        }

        public Criteria andGyearIsNull() {
            addCriterion("gyear is null");
            return (Criteria) this;
        }

        public Criteria andGyearIsNotNull() {
            addCriterion("gyear is not null");
            return (Criteria) this;
        }

        public Criteria andGyearEqualTo(String value) {
            addCriterion("gyear =", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearNotEqualTo(String value) {
            addCriterion("gyear <>", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearGreaterThan(String value) {
            addCriterion("gyear >", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearGreaterThanOrEqualTo(String value) {
            addCriterion("gyear >=", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearLessThan(String value) {
            addCriterion("gyear <", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearLessThanOrEqualTo(String value) {
            addCriterion("gyear <=", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearLike(String value) {
            addCriterion("gyear like", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearNotLike(String value) {
            addCriterion("gyear not like", value, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearIn(List<String> values) {
            addCriterion("gyear in", values, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearNotIn(List<String> values) {
            addCriterion("gyear not in", values, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearBetween(String value1, String value2) {
            addCriterion("gyear between", value1, value2, "gyear");
            return (Criteria) this;
        }

        public Criteria andGyearNotBetween(String value1, String value2) {
            addCriterion("gyear not between", value1, value2, "gyear");
            return (Criteria) this;
        }

        public Criteria andGdressIsNull() {
            addCriterion("gdress is null");
            return (Criteria) this;
        }

        public Criteria andGdressIsNotNull() {
            addCriterion("gdress is not null");
            return (Criteria) this;
        }

        public Criteria andGdressEqualTo(String value) {
            addCriterion("gdress =", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressNotEqualTo(String value) {
            addCriterion("gdress <>", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressGreaterThan(String value) {
            addCriterion("gdress >", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressGreaterThanOrEqualTo(String value) {
            addCriterion("gdress >=", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressLessThan(String value) {
            addCriterion("gdress <", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressLessThanOrEqualTo(String value) {
            addCriterion("gdress <=", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressLike(String value) {
            addCriterion("gdress like", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressNotLike(String value) {
            addCriterion("gdress not like", value, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressIn(List<String> values) {
            addCriterion("gdress in", values, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressNotIn(List<String> values) {
            addCriterion("gdress not in", values, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressBetween(String value1, String value2) {
            addCriterion("gdress between", value1, value2, "gdress");
            return (Criteria) this;
        }

        public Criteria andGdressNotBetween(String value1, String value2) {
            addCriterion("gdress not between", value1, value2, "gdress");
            return (Criteria) this;
        }

        public Criteria andGplayIsNull() {
            addCriterion("gplay is null");
            return (Criteria) this;
        }

        public Criteria andGplayIsNotNull() {
            addCriterion("gplay is not null");
            return (Criteria) this;
        }

        public Criteria andGplayEqualTo(String value) {
            addCriterion("gplay =", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayNotEqualTo(String value) {
            addCriterion("gplay <>", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayGreaterThan(String value) {
            addCriterion("gplay >", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayGreaterThanOrEqualTo(String value) {
            addCriterion("gplay >=", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayLessThan(String value) {
            addCriterion("gplay <", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayLessThanOrEqualTo(String value) {
            addCriterion("gplay <=", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayLike(String value) {
            addCriterion("gplay like", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayNotLike(String value) {
            addCriterion("gplay not like", value, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayIn(List<String> values) {
            addCriterion("gplay in", values, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayNotIn(List<String> values) {
            addCriterion("gplay not in", values, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayBetween(String value1, String value2) {
            addCriterion("gplay between", value1, value2, "gplay");
            return (Criteria) this;
        }

        public Criteria andGplayNotBetween(String value1, String value2) {
            addCriterion("gplay not between", value1, value2, "gplay");
            return (Criteria) this;
        }

        public Criteria andGproIsNull() {
            addCriterion("gpro is null");
            return (Criteria) this;
        }

        public Criteria andGproIsNotNull() {
            addCriterion("gpro is not null");
            return (Criteria) this;
        }

        public Criteria andGproEqualTo(String value) {
            addCriterion("gpro =", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproNotEqualTo(String value) {
            addCriterion("gpro <>", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproGreaterThan(String value) {
            addCriterion("gpro >", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproGreaterThanOrEqualTo(String value) {
            addCriterion("gpro >=", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproLessThan(String value) {
            addCriterion("gpro <", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproLessThanOrEqualTo(String value) {
            addCriterion("gpro <=", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproLike(String value) {
            addCriterion("gpro like", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproNotLike(String value) {
            addCriterion("gpro not like", value, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproIn(List<String> values) {
            addCriterion("gpro in", values, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproNotIn(List<String> values) {
            addCriterion("gpro not in", values, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproBetween(String value1, String value2) {
            addCriterion("gpro between", value1, value2, "gpro");
            return (Criteria) this;
        }

        public Criteria andGproNotBetween(String value1, String value2) {
            addCriterion("gpro not between", value1, value2, "gpro");
            return (Criteria) this;
        }

        public Criteria andGmoneyIsNull() {
            addCriterion("gmoney is null");
            return (Criteria) this;
        }

        public Criteria andGmoneyIsNotNull() {
            addCriterion("gmoney is not null");
            return (Criteria) this;
        }

        public Criteria andGmoneyEqualTo(String value) {
            addCriterion("gmoney =", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyNotEqualTo(String value) {
            addCriterion("gmoney <>", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyGreaterThan(String value) {
            addCriterion("gmoney >", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyGreaterThanOrEqualTo(String value) {
            addCriterion("gmoney >=", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyLessThan(String value) {
            addCriterion("gmoney <", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyLessThanOrEqualTo(String value) {
            addCriterion("gmoney <=", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyLike(String value) {
            addCriterion("gmoney like", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyNotLike(String value) {
            addCriterion("gmoney not like", value, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyIn(List<String> values) {
            addCriterion("gmoney in", values, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyNotIn(List<String> values) {
            addCriterion("gmoney not in", values, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyBetween(String value1, String value2) {
            addCriterion("gmoney between", value1, value2, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGmoneyNotBetween(String value1, String value2) {
            addCriterion("gmoney not between", value1, value2, "gmoney");
            return (Criteria) this;
        }

        public Criteria andGgetIsNull() {
            addCriterion("gget is null");
            return (Criteria) this;
        }

        public Criteria andGgetIsNotNull() {
            addCriterion("gget is not null");
            return (Criteria) this;
        }

        public Criteria andGgetEqualTo(String value) {
            addCriterion("gget =", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetNotEqualTo(String value) {
            addCriterion("gget <>", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetGreaterThan(String value) {
            addCriterion("gget >", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetGreaterThanOrEqualTo(String value) {
            addCriterion("gget >=", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetLessThan(String value) {
            addCriterion("gget <", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetLessThanOrEqualTo(String value) {
            addCriterion("gget <=", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetLike(String value) {
            addCriterion("gget like", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetNotLike(String value) {
            addCriterion("gget not like", value, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetIn(List<String> values) {
            addCriterion("gget in", values, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetNotIn(List<String> values) {
            addCriterion("gget not in", values, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetBetween(String value1, String value2) {
            addCriterion("gget between", value1, value2, "gget");
            return (Criteria) this;
        }

        public Criteria andGgetNotBetween(String value1, String value2) {
            addCriterion("gget not between", value1, value2, "gget");
            return (Criteria) this;
        }

        public Criteria andGpayIsNull() {
            addCriterion("gpay is null");
            return (Criteria) this;
        }

        public Criteria andGpayIsNotNull() {
            addCriterion("gpay is not null");
            return (Criteria) this;
        }

        public Criteria andGpayEqualTo(String value) {
            addCriterion("gpay =", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayNotEqualTo(String value) {
            addCriterion("gpay <>", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayGreaterThan(String value) {
            addCriterion("gpay >", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayGreaterThanOrEqualTo(String value) {
            addCriterion("gpay >=", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayLessThan(String value) {
            addCriterion("gpay <", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayLessThanOrEqualTo(String value) {
            addCriterion("gpay <=", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayLike(String value) {
            addCriterion("gpay like", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayNotLike(String value) {
            addCriterion("gpay not like", value, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayIn(List<String> values) {
            addCriterion("gpay in", values, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayNotIn(List<String> values) {
            addCriterion("gpay not in", values, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayBetween(String value1, String value2) {
            addCriterion("gpay between", value1, value2, "gpay");
            return (Criteria) this;
        }

        public Criteria andGpayNotBetween(String value1, String value2) {
            addCriterion("gpay not between", value1, value2, "gpay");
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