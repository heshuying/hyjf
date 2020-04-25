package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class OperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public OperationReportExample() {
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
        this.limitStart = limitStart;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
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

        public Criteria andCnNameIsNull() {
            addCriterion("cn_name is null");
            return (Criteria) this;
        }

        public Criteria andCnNameIsNotNull() {
            addCriterion("cn_name is not null");
            return (Criteria) this;
        }

        public Criteria andCnNameEqualTo(String value) {
            addCriterion("cn_name =", value, "cnName");
            return (Criteria) this;
        }
        public Criteria andYearEqualTo(String value) {
            addCriterion("year =", value, "year");
            return (Criteria) this;
        }
        public Criteria andCnNameNotEqualTo(String value) {
            addCriterion("cn_name <>", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameGreaterThan(String value) {
            addCriterion("cn_name >", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameGreaterThanOrEqualTo(String value) {
            addCriterion("cn_name >=", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameLessThan(String value) {
            addCriterion("cn_name <", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameLessThanOrEqualTo(String value) {
            addCriterion("cn_name <=", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameLike(String value) {
            addCriterion("cn_name like", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotLike(String value) {
            addCriterion("cn_name not like", value, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameIn(List<String> values) {
            addCriterion("cn_name in", values, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotIn(List<String> values) {
            addCriterion("cn_name not in", values, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameBetween(String value1, String value2) {
            addCriterion("cn_name between", value1, value2, "cnName");
            return (Criteria) this;
        }

        public Criteria andCnNameNotBetween(String value1, String value2) {
            addCriterion("cn_name not between", value1, value2, "cnName");
            return (Criteria) this;
        }

        public Criteria andEnNameIsNull() {
            addCriterion("en_name is null");
            return (Criteria) this;
        }

        public Criteria andEnNameIsNotNull() {
            addCriterion("en_name is not null");
            return (Criteria) this;
        }

        public Criteria andEnNameEqualTo(String value) {
            addCriterion("en_name =", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotEqualTo(String value) {
            addCriterion("en_name <>", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameGreaterThan(String value) {
            addCriterion("en_name >", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameGreaterThanOrEqualTo(String value) {
            addCriterion("en_name >=", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameLessThan(String value) {
            addCriterion("en_name <", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameLessThanOrEqualTo(String value) {
            addCriterion("en_name <=", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameLike(String value) {
            addCriterion("en_name like", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotLike(String value) {
            addCriterion("en_name not like", value, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameIn(List<String> values) {
            addCriterion("en_name in", values, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotIn(List<String> values) {
            addCriterion("en_name not in", values, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameBetween(String value1, String value2) {
            addCriterion("en_name between", value1, value2, "enName");
            return (Criteria) this;
        }

        public Criteria andEnNameNotBetween(String value1, String value2) {
            addCriterion("en_name not between", value1, value2, "enName");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeIsNull() {
            addCriterion("operation_report_type is null");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeIsNotNull() {
            addCriterion("operation_report_type is not null");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeEqualTo(Integer value) {
            addCriterion("operation_report_type =", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeNotEqualTo(Integer value) {
            addCriterion("operation_report_type <>", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeGreaterThan(Integer value) {
            addCriterion("operation_report_type >", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("operation_report_type >=", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeLessThan(Integer value) {
            addCriterion("operation_report_type <", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeLessThanOrEqualTo(Integer value) {
            addCriterion("operation_report_type <=", value, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeIn(List<Integer> values) {
            addCriterion("operation_report_type in", values, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeNotIn(List<Integer> values) {
            addCriterion("operation_report_type not in", values, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_type between", value1, value2, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andOperationReportTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_type not between", value1, value2, "operationReportType");
            return (Criteria) this;
        }

        public Criteria andAllAmountIsNull() {
            addCriterion("all_amount is null");
            return (Criteria) this;
        }

        public Criteria andAllAmountIsNotNull() {
            addCriterion("all_amount is not null");
            return (Criteria) this;
        }

        public Criteria andAllAmountEqualTo(Long value) {
            addCriterion("all_amount =", value, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountNotEqualTo(Long value) {
            addCriterion("all_amount <>", value, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountGreaterThan(Long value) {
            addCriterion("all_amount >", value, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("all_amount >=", value, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountLessThan(Long value) {
            addCriterion("all_amount <", value, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountLessThanOrEqualTo(Long value) {
            addCriterion("all_amount <=", value, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountIn(List<Long> values) {
            addCriterion("all_amount in", values, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountNotIn(List<Long> values) {
            addCriterion("all_amount not in", values, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountBetween(Long value1, Long value2) {
            addCriterion("all_amount between", value1, value2, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllAmountNotBetween(Long value1, Long value2) {
            addCriterion("all_amount not between", value1, value2, "allAmount");
            return (Criteria) this;
        }

        public Criteria andAllProfitIsNull() {
            addCriterion("all_profit is null");
            return (Criteria) this;
        }

        public Criteria andAllProfitIsNotNull() {
            addCriterion("all_profit is not null");
            return (Criteria) this;
        }

        public Criteria andAllProfitEqualTo(Long value) {
            addCriterion("all_profit =", value, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitNotEqualTo(Long value) {
            addCriterion("all_profit <>", value, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitGreaterThan(Long value) {
            addCriterion("all_profit >", value, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("all_profit >=", value, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitLessThan(Long value) {
            addCriterion("all_profit <", value, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitLessThanOrEqualTo(Long value) {
            addCriterion("all_profit <=", value, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitIn(List<Long> values) {
            addCriterion("all_profit in", values, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitNotIn(List<Long> values) {
            addCriterion("all_profit not in", values, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitBetween(Long value1, Long value2) {
            addCriterion("all_profit between", value1, value2, "allProfit");
            return (Criteria) this;
        }

        public Criteria andAllProfitNotBetween(Long value1, Long value2) {
            addCriterion("all_profit not between", value1, value2, "allProfit");
            return (Criteria) this;
        }

        public Criteria andRegistNumIsNull() {
            addCriterion("regist_num is null");
            return (Criteria) this;
        }

        public Criteria andRegistNumIsNotNull() {
            addCriterion("regist_num is not null");
            return (Criteria) this;
        }

        public Criteria andRegistNumEqualTo(Long value) {
            addCriterion("regist_num =", value, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumNotEqualTo(Long value) {
            addCriterion("regist_num <>", value, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumGreaterThan(Long value) {
            addCriterion("regist_num >", value, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumGreaterThanOrEqualTo(Long value) {
            addCriterion("regist_num >=", value, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumLessThan(Long value) {
            addCriterion("regist_num <", value, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumLessThanOrEqualTo(Long value) {
            addCriterion("regist_num <=", value, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumIn(List<Long> values) {
            addCriterion("regist_num in", values, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumNotIn(List<Long> values) {
            addCriterion("regist_num not in", values, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumBetween(Long value1, Long value2) {
            addCriterion("regist_num between", value1, value2, "registNum");
            return (Criteria) this;
        }

        public Criteria andRegistNumNotBetween(Long value1, Long value2) {
            addCriterion("regist_num not between", value1, value2, "registNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumIsNull() {
            addCriterion("success_deal_num is null");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumIsNotNull() {
            addCriterion("success_deal_num is not null");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumEqualTo(Integer value) {
            addCriterion("success_deal_num =", value, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumNotEqualTo(Integer value) {
            addCriterion("success_deal_num <>", value, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumGreaterThan(Integer value) {
            addCriterion("success_deal_num >", value, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("success_deal_num >=", value, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumLessThan(Integer value) {
            addCriterion("success_deal_num <", value, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumLessThanOrEqualTo(Integer value) {
            addCriterion("success_deal_num <=", value, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumIn(List<Integer> values) {
            addCriterion("success_deal_num in", values, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumNotIn(List<Integer> values) {
            addCriterion("success_deal_num not in", values, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumBetween(Integer value1, Integer value2) {
            addCriterion("success_deal_num between", value1, value2, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andSuccessDealNumNotBetween(Integer value1, Integer value2) {
            addCriterion("success_deal_num not between", value1, value2, "successDealNum");
            return (Criteria) this;
        }

        public Criteria andOperationAmountIsNull() {
            addCriterion("operation_amount is null");
            return (Criteria) this;
        }

        public Criteria andOperationAmountIsNotNull() {
            addCriterion("operation_amount is not null");
            return (Criteria) this;
        }

        public Criteria andOperationAmountEqualTo(Long value) {
            addCriterion("operation_amount =", value, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountNotEqualTo(Long value) {
            addCriterion("operation_amount <>", value, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountGreaterThan(Long value) {
            addCriterion("operation_amount >", value, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("operation_amount >=", value, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountLessThan(Long value) {
            addCriterion("operation_amount <", value, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountLessThanOrEqualTo(Long value) {
            addCriterion("operation_amount <=", value, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountIn(List<Long> values) {
            addCriterion("operation_amount in", values, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountNotIn(List<Long> values) {
            addCriterion("operation_amount not in", values, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountBetween(Long value1, Long value2) {
            addCriterion("operation_amount between", value1, value2, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationAmountNotBetween(Long value1, Long value2) {
            addCriterion("operation_amount not between", value1, value2, "operationAmount");
            return (Criteria) this;
        }

        public Criteria andOperationProfitIsNull() {
            addCriterion("operation_profit is null");
            return (Criteria) this;
        }

        public Criteria andOperationProfitIsNotNull() {
            addCriterion("operation_profit is not null");
            return (Criteria) this;
        }

        public Criteria andOperationProfitEqualTo(Long value) {
            addCriterion("operation_profit =", value, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitNotEqualTo(Long value) {
            addCriterion("operation_profit <>", value, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitGreaterThan(Long value) {
            addCriterion("operation_profit >", value, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("operation_profit >=", value, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitLessThan(Long value) {
            addCriterion("operation_profit <", value, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitLessThanOrEqualTo(Long value) {
            addCriterion("operation_profit <=", value, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitIn(List<Long> values) {
            addCriterion("operation_profit in", values, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitNotIn(List<Long> values) {
            addCriterion("operation_profit not in", values, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitBetween(Long value1, Long value2) {
            addCriterion("operation_profit between", value1, value2, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andOperationProfitNotBetween(Long value1, Long value2) {
            addCriterion("operation_profit not between", value1, value2, "operationProfit");
            return (Criteria) this;
        }

        public Criteria andIsReleaseIsNull() {
            addCriterion("is_release is null");
            return (Criteria) this;
        }

        public Criteria andIsReleaseIsNotNull() {
            addCriterion("is_release is not null");
            return (Criteria) this;
        }

        public Criteria andIsReleaseEqualTo(Integer value) {
            addCriterion("is_release =", value, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseNotEqualTo(Integer value) {
            addCriterion("is_release <>", value, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseGreaterThan(Integer value) {
            addCriterion("is_release >", value, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_release >=", value, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseLessThan(Integer value) {
            addCriterion("is_release <", value, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseLessThanOrEqualTo(Integer value) {
            addCriterion("is_release <=", value, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseIn(List<Integer> values) {
            addCriterion("is_release in", values, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseNotIn(List<Integer> values) {
            addCriterion("is_release not in", values, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseBetween(Integer value1, Integer value2) {
            addCriterion("is_release between", value1, value2, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsReleaseNotBetween(Integer value1, Integer value2) {
            addCriterion("is_release not between", value1, value2, "isRelease");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Integer value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Integer value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Integer value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Integer value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Integer> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Integer> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Integer value1, Integer value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Integer value1, Integer value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIsNull() {
            addCriterion("release_time is null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIsNotNull() {
            addCriterion("release_time is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeEqualTo(Integer value) {
            addCriterion("release_time =", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotEqualTo(Integer value) {
            addCriterion("release_time <>", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeGreaterThan(Integer value) {
            addCriterion("release_time >", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("release_time >=", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeLessThan(Integer value) {
            addCriterion("release_time <", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeLessThanOrEqualTo(Integer value) {
            addCriterion("release_time <=", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIn(List<Integer> values) {
            addCriterion("release_time in", values, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotIn(List<Integer> values) {
            addCriterion("release_time not in", values, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeBetween(Integer value1, Integer value2) {
            addCriterion("release_time between", value1, value2, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("release_time not between", value1, value2, "releaseTime");
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

        public Criteria andUpdateUserIdIsNull() {
            addCriterion("update_user_id is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIsNotNull() {
            addCriterion("update_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdEqualTo(Integer value) {
            addCriterion("update_user_id =", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotEqualTo(Integer value) {
            addCriterion("update_user_id <>", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdGreaterThan(Integer value) {
            addCriterion("update_user_id >", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_user_id >=", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdLessThan(Integer value) {
            addCriterion("update_user_id <", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("update_user_id <=", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIn(List<Integer> values) {
            addCriterion("update_user_id in", values, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotIn(List<Integer> values) {
            addCriterion("update_user_id not in", values, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdBetween(Integer value1, Integer value2) {
            addCriterion("update_user_id between", value1, value2, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("update_user_id not between", value1, value2, "updateUserId");
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

        public Criteria andCreateUserIdIsNull() {
            addCriterion("create_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNotNull() {
            addCriterion("create_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdEqualTo(Integer value) {
            addCriterion("create_user_id =", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotEqualTo(Integer value) {
            addCriterion("create_user_id <>", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThan(Integer value) {
            addCriterion("create_user_id >", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_user_id >=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThan(Integer value) {
            addCriterion("create_user_id <", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("create_user_id <=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIn(List<Integer> values) {
            addCriterion("create_user_id in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotIn(List<Integer> values) {
            addCriterion("create_user_id not in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdBetween(Integer value1, Integer value2) {
            addCriterion("create_user_id between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("create_user_id not between", value1, value2, "createUserId");
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