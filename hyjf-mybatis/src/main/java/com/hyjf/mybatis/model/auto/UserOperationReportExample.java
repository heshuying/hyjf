package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserOperationReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public UserOperationReportExample() {
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

        public Criteria andOperationReportIdIsNull() {
            addCriterion("operation_report_id is null");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdIsNotNull() {
            addCriterion("operation_report_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdEqualTo(Integer value) {
            addCriterion("operation_report_id =", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdNotEqualTo(Integer value) {
            addCriterion("operation_report_id <>", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdGreaterThan(Integer value) {
            addCriterion("operation_report_id >", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("operation_report_id >=", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdLessThan(Integer value) {
            addCriterion("operation_report_id <", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdLessThanOrEqualTo(Integer value) {
            addCriterion("operation_report_id <=", value, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdIn(List<Integer> values) {
            addCriterion("operation_report_id in", values, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdNotIn(List<Integer> values) {
            addCriterion("operation_report_id not in", values, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_id between", value1, value2, "operationReportId");
            return (Criteria) this;
        }

        public Criteria andOperationReportIdNotBetween(Integer value1, Integer value2) {
            addCriterion("operation_report_id not between", value1, value2, "operationReportId");
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

        public Criteria andManTenderNumIsNull() {
            addCriterion("man_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andManTenderNumIsNotNull() {
            addCriterion("man_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andManTenderNumEqualTo(Integer value) {
            addCriterion("man_tender_num =", value, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumNotEqualTo(Integer value) {
            addCriterion("man_tender_num <>", value, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumGreaterThan(Integer value) {
            addCriterion("man_tender_num >", value, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("man_tender_num >=", value, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumLessThan(Integer value) {
            addCriterion("man_tender_num <", value, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("man_tender_num <=", value, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumIn(List<Integer> values) {
            addCriterion("man_tender_num in", values, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumNotIn(List<Integer> values) {
            addCriterion("man_tender_num not in", values, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("man_tender_num between", value1, value2, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("man_tender_num not between", value1, value2, "manTenderNum");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionIsNull() {
            addCriterion("man_tender_num_proportion is null");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionIsNotNull() {
            addCriterion("man_tender_num_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionEqualTo(BigDecimal value) {
            addCriterion("man_tender_num_proportion =", value, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionNotEqualTo(BigDecimal value) {
            addCriterion("man_tender_num_proportion <>", value, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionGreaterThan(BigDecimal value) {
            addCriterion("man_tender_num_proportion >", value, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("man_tender_num_proportion >=", value, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionLessThan(BigDecimal value) {
            addCriterion("man_tender_num_proportion <", value, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("man_tender_num_proportion <=", value, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionIn(List<BigDecimal> values) {
            addCriterion("man_tender_num_proportion in", values, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionNotIn(List<BigDecimal> values) {
            addCriterion("man_tender_num_proportion not in", values, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("man_tender_num_proportion between", value1, value2, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andManTenderNumProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("man_tender_num_proportion not between", value1, value2, "manTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumIsNull() {
            addCriterion("woman_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumIsNotNull() {
            addCriterion("woman_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumEqualTo(Integer value) {
            addCriterion("woman_tender_num =", value, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumNotEqualTo(Integer value) {
            addCriterion("woman_tender_num <>", value, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumGreaterThan(Integer value) {
            addCriterion("woman_tender_num >", value, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("woman_tender_num >=", value, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumLessThan(Integer value) {
            addCriterion("woman_tender_num <", value, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("woman_tender_num <=", value, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumIn(List<Integer> values) {
            addCriterion("woman_tender_num in", values, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumNotIn(List<Integer> values) {
            addCriterion("woman_tender_num not in", values, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("woman_tender_num between", value1, value2, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("woman_tender_num not between", value1, value2, "womanTenderNum");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionIsNull() {
            addCriterion("woman_tender_num_proportion is null");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionIsNotNull() {
            addCriterion("woman_tender_num_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionEqualTo(BigDecimal value) {
            addCriterion("woman_tender_num_proportion =", value, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionNotEqualTo(BigDecimal value) {
            addCriterion("woman_tender_num_proportion <>", value, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionGreaterThan(BigDecimal value) {
            addCriterion("woman_tender_num_proportion >", value, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("woman_tender_num_proportion >=", value, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionLessThan(BigDecimal value) {
            addCriterion("woman_tender_num_proportion <", value, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("woman_tender_num_proportion <=", value, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionIn(List<BigDecimal> values) {
            addCriterion("woman_tender_num_proportion in", values, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionNotIn(List<BigDecimal> values) {
            addCriterion("woman_tender_num_proportion not in", values, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("woman_tender_num_proportion between", value1, value2, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andWomanTenderNumProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("woman_tender_num_proportion not between", value1, value2, "womanTenderNumProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumIsNull() {
            addCriterion("age_first_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumIsNotNull() {
            addCriterion("age_first_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumEqualTo(Integer value) {
            addCriterion("age_first_stage_tender_num =", value, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumNotEqualTo(Integer value) {
            addCriterion("age_first_stage_tender_num <>", value, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumGreaterThan(Integer value) {
            addCriterion("age_first_stage_tender_num >", value, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("age_first_stage_tender_num >=", value, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumLessThan(Integer value) {
            addCriterion("age_first_stage_tender_num <", value, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("age_first_stage_tender_num <=", value, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumIn(List<Integer> values) {
            addCriterion("age_first_stage_tender_num in", values, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumNotIn(List<Integer> values) {
            addCriterion("age_first_stage_tender_num not in", values, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("age_first_stage_tender_num between", value1, value2, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("age_first_stage_tender_num not between", value1, value2, "ageFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionIsNull() {
            addCriterion("age_first_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionIsNotNull() {
            addCriterion("age_first_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("age_first_stage_tender_proportion =", value, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("age_first_stage_tender_proportion <>", value, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("age_first_stage_tender_proportion >", value, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("age_first_stage_tender_proportion >=", value, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("age_first_stage_tender_proportion <", value, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("age_first_stage_tender_proportion <=", value, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("age_first_stage_tender_proportion in", values, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("age_first_stage_tender_proportion not in", values, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_first_stage_tender_proportion between", value1, value2, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirstStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_first_stage_tender_proportion not between", value1, value2, "ageFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumIsNull() {
            addCriterion("age_second_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumIsNotNull() {
            addCriterion("age_second_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumEqualTo(Integer value) {
            addCriterion("age_second_stage_tender_num =", value, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumNotEqualTo(Integer value) {
            addCriterion("age_second_stage_tender_num <>", value, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumGreaterThan(Integer value) {
            addCriterion("age_second_stage_tender_num >", value, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("age_second_stage_tender_num >=", value, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumLessThan(Integer value) {
            addCriterion("age_second_stage_tender_num <", value, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("age_second_stage_tender_num <=", value, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumIn(List<Integer> values) {
            addCriterion("age_second_stage_tender_num in", values, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumNotIn(List<Integer> values) {
            addCriterion("age_second_stage_tender_num not in", values, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("age_second_stage_tender_num between", value1, value2, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("age_second_stage_tender_num not between", value1, value2, "ageSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionIsNull() {
            addCriterion("age_second_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionIsNotNull() {
            addCriterion("age_second_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("age_second_stage_tender_proportion =", value, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("age_second_stage_tender_proportion <>", value, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("age_second_stage_tender_proportion >", value, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("age_second_stage_tender_proportion >=", value, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("age_second_stage_tender_proportion <", value, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("age_second_stage_tender_proportion <=", value, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("age_second_stage_tender_proportion in", values, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("age_second_stage_tender_proportion not in", values, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_second_stage_tender_proportion between", value1, value2, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeSecondStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_second_stage_tender_proportion not between", value1, value2, "ageSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumIsNull() {
            addCriterion("age_third_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumIsNotNull() {
            addCriterion("age_third_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumEqualTo(Integer value) {
            addCriterion("age_third_stage_tender_num =", value, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumNotEqualTo(Integer value) {
            addCriterion("age_third_stage_tender_num <>", value, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumGreaterThan(Integer value) {
            addCriterion("age_third_stage_tender_num >", value, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("age_third_stage_tender_num >=", value, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumLessThan(Integer value) {
            addCriterion("age_third_stage_tender_num <", value, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("age_third_stage_tender_num <=", value, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumIn(List<Integer> values) {
            addCriterion("age_third_stage_tender_num in", values, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumNotIn(List<Integer> values) {
            addCriterion("age_third_stage_tender_num not in", values, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("age_third_stage_tender_num between", value1, value2, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("age_third_stage_tender_num not between", value1, value2, "ageThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionIsNull() {
            addCriterion("age_third_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionIsNotNull() {
            addCriterion("age_third_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("age_third_stage_tender_proportion =", value, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("age_third_stage_tender_proportion <>", value, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("age_third_stage_tender_proportion >", value, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("age_third_stage_tender_proportion >=", value, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("age_third_stage_tender_proportion <", value, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("age_third_stage_tender_proportion <=", value, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("age_third_stage_tender_proportion in", values, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("age_third_stage_tender_proportion not in", values, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_third_stage_tender_proportion between", value1, value2, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeThirdStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_third_stage_tender_proportion not between", value1, value2, "ageThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumIsNull() {
            addCriterion("age_fourth_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumIsNotNull() {
            addCriterion("age_fourth_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumEqualTo(Integer value) {
            addCriterion("age_fourth_stage_tender_num =", value, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumNotEqualTo(Integer value) {
            addCriterion("age_fourth_stage_tender_num <>", value, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumGreaterThan(Integer value) {
            addCriterion("age_fourth_stage_tender_num >", value, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("age_fourth_stage_tender_num >=", value, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumLessThan(Integer value) {
            addCriterion("age_fourth_stage_tender_num <", value, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("age_fourth_stage_tender_num <=", value, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumIn(List<Integer> values) {
            addCriterion("age_fourth_stage_tender_num in", values, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumNotIn(List<Integer> values) {
            addCriterion("age_fourth_stage_tender_num not in", values, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("age_fourth_stage_tender_num between", value1, value2, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("age_fourth_stage_tender_num not between", value1, value2, "ageFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionIsNull() {
            addCriterion("age_fourth_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionIsNotNull() {
            addCriterion("age_fourth_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("age_fourth_stage_tender_proportion =", value, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("age_fourth_stage_tender_proportion <>", value, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("age_fourth_stage_tender_proportion >", value, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("age_fourth_stage_tender_proportion >=", value, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("age_fourth_stage_tender_proportion <", value, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("age_fourth_stage_tender_proportion <=", value, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("age_fourth_stage_tender_proportion in", values, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("age_fourth_stage_tender_proportion not in", values, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_fourth_stage_tender_proportion between", value1, value2, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFourthStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_fourth_stage_tender_proportion not between", value1, value2, "ageFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumIsNull() {
            addCriterion("age_firve_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumIsNotNull() {
            addCriterion("age_firve_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumEqualTo(Integer value) {
            addCriterion("age_firve_stage_tender_num =", value, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumNotEqualTo(Integer value) {
            addCriterion("age_firve_stage_tender_num <>", value, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumGreaterThan(Integer value) {
            addCriterion("age_firve_stage_tender_num >", value, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("age_firve_stage_tender_num >=", value, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumLessThan(Integer value) {
            addCriterion("age_firve_stage_tender_num <", value, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("age_firve_stage_tender_num <=", value, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumIn(List<Integer> values) {
            addCriterion("age_firve_stage_tender_num in", values, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumNotIn(List<Integer> values) {
            addCriterion("age_firve_stage_tender_num not in", values, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("age_firve_stage_tender_num between", value1, value2, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("age_firve_stage_tender_num not between", value1, value2, "ageFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionIsNull() {
            addCriterion("age_firve_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionIsNotNull() {
            addCriterion("age_firve_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("age_firve_stage_tender_proportion =", value, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("age_firve_stage_tender_proportion <>", value, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("age_firve_stage_tender_proportion >", value, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("age_firve_stage_tender_proportion >=", value, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("age_firve_stage_tender_proportion <", value, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("age_firve_stage_tender_proportion <=", value, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("age_firve_stage_tender_proportion in", values, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("age_firve_stage_tender_proportion not in", values, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_firve_stage_tender_proportion between", value1, value2, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAgeFirveStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("age_firve_stage_tender_proportion not between", value1, value2, "ageFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumIsNull() {
            addCriterion("amount_first_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumIsNotNull() {
            addCriterion("amount_first_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumEqualTo(Integer value) {
            addCriterion("amount_first_stage_tender_num =", value, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumNotEqualTo(Integer value) {
            addCriterion("amount_first_stage_tender_num <>", value, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumGreaterThan(Integer value) {
            addCriterion("amount_first_stage_tender_num >", value, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount_first_stage_tender_num >=", value, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumLessThan(Integer value) {
            addCriterion("amount_first_stage_tender_num <", value, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("amount_first_stage_tender_num <=", value, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumIn(List<Integer> values) {
            addCriterion("amount_first_stage_tender_num in", values, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumNotIn(List<Integer> values) {
            addCriterion("amount_first_stage_tender_num not in", values, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("amount_first_stage_tender_num between", value1, value2, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("amount_first_stage_tender_num not between", value1, value2, "amountFirstStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionIsNull() {
            addCriterion("amount_first_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionIsNotNull() {
            addCriterion("amount_first_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("amount_first_stage_tender_proportion =", value, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("amount_first_stage_tender_proportion <>", value, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("amount_first_stage_tender_proportion >", value, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_first_stage_tender_proportion >=", value, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("amount_first_stage_tender_proportion <", value, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_first_stage_tender_proportion <=", value, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("amount_first_stage_tender_proportion in", values, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("amount_first_stage_tender_proportion not in", values, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_first_stage_tender_proportion between", value1, value2, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirstStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_first_stage_tender_proportion not between", value1, value2, "amountFirstStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumIsNull() {
            addCriterion("amount_second_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumIsNotNull() {
            addCriterion("amount_second_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumEqualTo(Integer value) {
            addCriterion("amount_second_stage_tender_num =", value, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumNotEqualTo(Integer value) {
            addCriterion("amount_second_stage_tender_num <>", value, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumGreaterThan(Integer value) {
            addCriterion("amount_second_stage_tender_num >", value, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount_second_stage_tender_num >=", value, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumLessThan(Integer value) {
            addCriterion("amount_second_stage_tender_num <", value, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("amount_second_stage_tender_num <=", value, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumIn(List<Integer> values) {
            addCriterion("amount_second_stage_tender_num in", values, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumNotIn(List<Integer> values) {
            addCriterion("amount_second_stage_tender_num not in", values, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("amount_second_stage_tender_num between", value1, value2, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("amount_second_stage_tender_num not between", value1, value2, "amountSecondStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionIsNull() {
            addCriterion("amount_second_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionIsNotNull() {
            addCriterion("amount_second_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("amount_second_stage_tender_proportion =", value, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("amount_second_stage_tender_proportion <>", value, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("amount_second_stage_tender_proportion >", value, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_second_stage_tender_proportion >=", value, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("amount_second_stage_tender_proportion <", value, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_second_stage_tender_proportion <=", value, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("amount_second_stage_tender_proportion in", values, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("amount_second_stage_tender_proportion not in", values, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_second_stage_tender_proportion between", value1, value2, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountSecondStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_second_stage_tender_proportion not between", value1, value2, "amountSecondStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumIsNull() {
            addCriterion("amount_third_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumIsNotNull() {
            addCriterion("amount_third_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumEqualTo(Integer value) {
            addCriterion("amount_third_stage_tender_num =", value, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumNotEqualTo(Integer value) {
            addCriterion("amount_third_stage_tender_num <>", value, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumGreaterThan(Integer value) {
            addCriterion("amount_third_stage_tender_num >", value, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount_third_stage_tender_num >=", value, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumLessThan(Integer value) {
            addCriterion("amount_third_stage_tender_num <", value, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("amount_third_stage_tender_num <=", value, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumIn(List<Integer> values) {
            addCriterion("amount_third_stage_tender_num in", values, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumNotIn(List<Integer> values) {
            addCriterion("amount_third_stage_tender_num not in", values, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("amount_third_stage_tender_num between", value1, value2, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("amount_third_stage_tender_num not between", value1, value2, "amountThirdStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionIsNull() {
            addCriterion("amount_third_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionIsNotNull() {
            addCriterion("amount_third_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("amount_third_stage_tender_proportion =", value, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("amount_third_stage_tender_proportion <>", value, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("amount_third_stage_tender_proportion >", value, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_third_stage_tender_proportion >=", value, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("amount_third_stage_tender_proportion <", value, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_third_stage_tender_proportion <=", value, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("amount_third_stage_tender_proportion in", values, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("amount_third_stage_tender_proportion not in", values, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_third_stage_tender_proportion between", value1, value2, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountThirdStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_third_stage_tender_proportion not between", value1, value2, "amountThirdStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumIsNull() {
            addCriterion("amount_fourth_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumIsNotNull() {
            addCriterion("amount_fourth_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumEqualTo(Integer value) {
            addCriterion("amount_fourth_stage_tender_num =", value, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumNotEqualTo(Integer value) {
            addCriterion("amount_fourth_stage_tender_num <>", value, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumGreaterThan(Integer value) {
            addCriterion("amount_fourth_stage_tender_num >", value, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount_fourth_stage_tender_num >=", value, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumLessThan(Integer value) {
            addCriterion("amount_fourth_stage_tender_num <", value, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("amount_fourth_stage_tender_num <=", value, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumIn(List<Integer> values) {
            addCriterion("amount_fourth_stage_tender_num in", values, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumNotIn(List<Integer> values) {
            addCriterion("amount_fourth_stage_tender_num not in", values, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("amount_fourth_stage_tender_num between", value1, value2, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("amount_fourth_stage_tender_num not between", value1, value2, "amountFourthStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionIsNull() {
            addCriterion("amount_fourth_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionIsNotNull() {
            addCriterion("amount_fourth_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("amount_fourth_stage_tender_proportion =", value, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("amount_fourth_stage_tender_proportion <>", value, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("amount_fourth_stage_tender_proportion >", value, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_fourth_stage_tender_proportion >=", value, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("amount_fourth_stage_tender_proportion <", value, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_fourth_stage_tender_proportion <=", value, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("amount_fourth_stage_tender_proportion in", values, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("amount_fourth_stage_tender_proportion not in", values, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_fourth_stage_tender_proportion between", value1, value2, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFourthStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_fourth_stage_tender_proportion not between", value1, value2, "amountFourthStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumIsNull() {
            addCriterion("amount_firve_stage_tender_num is null");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumIsNotNull() {
            addCriterion("amount_firve_stage_tender_num is not null");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumEqualTo(Integer value) {
            addCriterion("amount_firve_stage_tender_num =", value, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumNotEqualTo(Integer value) {
            addCriterion("amount_firve_stage_tender_num <>", value, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumGreaterThan(Integer value) {
            addCriterion("amount_firve_stage_tender_num >", value, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount_firve_stage_tender_num >=", value, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumLessThan(Integer value) {
            addCriterion("amount_firve_stage_tender_num <", value, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumLessThanOrEqualTo(Integer value) {
            addCriterion("amount_firve_stage_tender_num <=", value, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumIn(List<Integer> values) {
            addCriterion("amount_firve_stage_tender_num in", values, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumNotIn(List<Integer> values) {
            addCriterion("amount_firve_stage_tender_num not in", values, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumBetween(Integer value1, Integer value2) {
            addCriterion("amount_firve_stage_tender_num between", value1, value2, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("amount_firve_stage_tender_num not between", value1, value2, "amountFirveStageTenderNum");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionIsNull() {
            addCriterion("amount_firve_stage_tender_proportion is null");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionIsNotNull() {
            addCriterion("amount_firve_stage_tender_proportion is not null");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionEqualTo(BigDecimal value) {
            addCriterion("amount_firve_stage_tender_proportion =", value, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionNotEqualTo(BigDecimal value) {
            addCriterion("amount_firve_stage_tender_proportion <>", value, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionGreaterThan(BigDecimal value) {
            addCriterion("amount_firve_stage_tender_proportion >", value, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_firve_stage_tender_proportion >=", value, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionLessThan(BigDecimal value) {
            addCriterion("amount_firve_stage_tender_proportion <", value, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount_firve_stage_tender_proportion <=", value, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionIn(List<BigDecimal> values) {
            addCriterion("amount_firve_stage_tender_proportion in", values, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionNotIn(List<BigDecimal> values) {
            addCriterion("amount_firve_stage_tender_proportion not in", values, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_firve_stage_tender_proportion between", value1, value2, "amountFirveStageTenderProportion");
            return (Criteria) this;
        }

        public Criteria andAmountFirveStageTenderProportionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount_firve_stage_tender_proportion not between", value1, value2, "amountFirveStageTenderProportion");
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