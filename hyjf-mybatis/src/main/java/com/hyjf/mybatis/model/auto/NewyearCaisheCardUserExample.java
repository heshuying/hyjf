package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class NewyearCaisheCardUserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public NewyearCaisheCardUserExample() {
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

        public Criteria andCardTypeIsNull() {
            addCriterion("card_type is null");
            return (Criteria) this;
        }

        public Criteria andCardTypeIsNotNull() {
            addCriterion("card_type is not null");
            return (Criteria) this;
        }

        public Criteria andCardTypeEqualTo(Integer value) {
            addCriterion("card_type =", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotEqualTo(Integer value) {
            addCriterion("card_type <>", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeGreaterThan(Integer value) {
            addCriterion("card_type >", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_type >=", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLessThan(Integer value) {
            addCriterion("card_type <", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLessThanOrEqualTo(Integer value) {
            addCriterion("card_type <=", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeIn(List<Integer> values) {
            addCriterion("card_type in", values, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotIn(List<Integer> values) {
            addCriterion("card_type not in", values, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeBetween(Integer value1, Integer value2) {
            addCriterion("card_type between", value1, value2, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("card_type not between", value1, value2, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityIsNull() {
            addCriterion("card_jin_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityIsNotNull() {
            addCriterion("card_jin_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityEqualTo(Integer value) {
            addCriterion("card_jin_quantity =", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityNotEqualTo(Integer value) {
            addCriterion("card_jin_quantity <>", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityGreaterThan(Integer value) {
            addCriterion("card_jin_quantity >", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_jin_quantity >=", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityLessThan(Integer value) {
            addCriterion("card_jin_quantity <", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_jin_quantity <=", value, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityIn(List<Integer> values) {
            addCriterion("card_jin_quantity in", values, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityNotIn(List<Integer> values) {
            addCriterion("card_jin_quantity not in", values, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_jin_quantity between", value1, value2, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJinQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_jin_quantity not between", value1, value2, "cardJinQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityIsNull() {
            addCriterion("card_ji_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityIsNotNull() {
            addCriterion("card_ji_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityEqualTo(Integer value) {
            addCriterion("card_ji_quantity =", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityNotEqualTo(Integer value) {
            addCriterion("card_ji_quantity <>", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityGreaterThan(Integer value) {
            addCriterion("card_ji_quantity >", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_ji_quantity >=", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityLessThan(Integer value) {
            addCriterion("card_ji_quantity <", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_ji_quantity <=", value, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityIn(List<Integer> values) {
            addCriterion("card_ji_quantity in", values, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityNotIn(List<Integer> values) {
            addCriterion("card_ji_quantity not in", values, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_ji_quantity between", value1, value2, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardJiQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_ji_quantity not between", value1, value2, "cardJiQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityIsNull() {
            addCriterion("card_na_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityIsNotNull() {
            addCriterion("card_na_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityEqualTo(Integer value) {
            addCriterion("card_na_quantity =", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityNotEqualTo(Integer value) {
            addCriterion("card_na_quantity <>", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityGreaterThan(Integer value) {
            addCriterion("card_na_quantity >", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_na_quantity >=", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityLessThan(Integer value) {
            addCriterion("card_na_quantity <", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_na_quantity <=", value, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityIn(List<Integer> values) {
            addCriterion("card_na_quantity in", values, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityNotIn(List<Integer> values) {
            addCriterion("card_na_quantity not in", values, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_na_quantity between", value1, value2, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardNaQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_na_quantity not between", value1, value2, "cardNaQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityIsNull() {
            addCriterion("card_fu_quantity is null");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityIsNotNull() {
            addCriterion("card_fu_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityEqualTo(Integer value) {
            addCriterion("card_fu_quantity =", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityNotEqualTo(Integer value) {
            addCriterion("card_fu_quantity <>", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityGreaterThan(Integer value) {
            addCriterion("card_fu_quantity >", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_fu_quantity >=", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityLessThan(Integer value) {
            addCriterion("card_fu_quantity <", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("card_fu_quantity <=", value, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityIn(List<Integer> values) {
            addCriterion("card_fu_quantity in", values, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityNotIn(List<Integer> values) {
            addCriterion("card_fu_quantity not in", values, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityBetween(Integer value1, Integer value2) {
            addCriterion("card_fu_quantity between", value1, value2, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andCardFuQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("card_fu_quantity not between", value1, value2, "cardFuQuantity");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIsNull() {
            addCriterion("operate_type is null");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIsNotNull() {
            addCriterion("operate_type is not null");
            return (Criteria) this;
        }

        public Criteria andOperateTypeEqualTo(Integer value) {
            addCriterion("operate_type =", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotEqualTo(Integer value) {
            addCriterion("operate_type <>", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeGreaterThan(Integer value) {
            addCriterion("operate_type >", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("operate_type >=", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLessThan(Integer value) {
            addCriterion("operate_type <", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeLessThanOrEqualTo(Integer value) {
            addCriterion("operate_type <=", value, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeIn(List<Integer> values) {
            addCriterion("operate_type in", values, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotIn(List<Integer> values) {
            addCriterion("operate_type not in", values, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeBetween(Integer value1, Integer value2) {
            addCriterion("operate_type between", value1, value2, "operateType");
            return (Criteria) this;
        }

        public Criteria andOperateTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("operate_type not between", value1, value2, "operateType");
            return (Criteria) this;
        }

        public Criteria andCardSourceIsNull() {
            addCriterion("card_source is null");
            return (Criteria) this;
        }

        public Criteria andCardSourceIsNotNull() {
            addCriterion("card_source is not null");
            return (Criteria) this;
        }

        public Criteria andCardSourceEqualTo(Integer value) {
            addCriterion("card_source =", value, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceNotEqualTo(Integer value) {
            addCriterion("card_source <>", value, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceGreaterThan(Integer value) {
            addCriterion("card_source >", value, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_source >=", value, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceLessThan(Integer value) {
            addCriterion("card_source <", value, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceLessThanOrEqualTo(Integer value) {
            addCriterion("card_source <=", value, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceIn(List<Integer> values) {
            addCriterion("card_source in", values, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceNotIn(List<Integer> values) {
            addCriterion("card_source not in", values, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceBetween(Integer value1, Integer value2) {
            addCriterion("card_source between", value1, value2, "cardSource");
            return (Criteria) this;
        }

        public Criteria andCardSourceNotBetween(Integer value1, Integer value2) {
            addCriterion("card_source not between", value1, value2, "cardSource");
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