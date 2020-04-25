package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class OperationReportActivityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public OperationReportActivityExample() {
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

        public Criteria andActivtyTypeIsNull() {
            addCriterion("activty_type is null");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeIsNotNull() {
            addCriterion("activty_type is not null");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeEqualTo(Integer value) {
            addCriterion("activty_type =", value, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeNotEqualTo(Integer value) {
            addCriterion("activty_type <>", value, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeGreaterThan(Integer value) {
            addCriterion("activty_type >", value, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("activty_type >=", value, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeLessThan(Integer value) {
            addCriterion("activty_type <", value, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeLessThanOrEqualTo(Integer value) {
            addCriterion("activty_type <=", value, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeIn(List<Integer> values) {
            addCriterion("activty_type in", values, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeNotIn(List<Integer> values) {
            addCriterion("activty_type not in", values, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeBetween(Integer value1, Integer value2) {
            addCriterion("activty_type between", value1, value2, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("activty_type not between", value1, value2, "activtyType");
            return (Criteria) this;
        }

        public Criteria andActivtyNameIsNull() {
            addCriterion("activty_name is null");
            return (Criteria) this;
        }

        public Criteria andActivtyNameIsNotNull() {
            addCriterion("activty_name is not null");
            return (Criteria) this;
        }

        public Criteria andActivtyNameEqualTo(String value) {
            addCriterion("activty_name =", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameNotEqualTo(String value) {
            addCriterion("activty_name <>", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameGreaterThan(String value) {
            addCriterion("activty_name >", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameGreaterThanOrEqualTo(String value) {
            addCriterion("activty_name >=", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameLessThan(String value) {
            addCriterion("activty_name <", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameLessThanOrEqualTo(String value) {
            addCriterion("activty_name <=", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameLike(String value) {
            addCriterion("activty_name like", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameNotLike(String value) {
            addCriterion("activty_name not like", value, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameIn(List<String> values) {
            addCriterion("activty_name in", values, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameNotIn(List<String> values) {
            addCriterion("activty_name not in", values, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameBetween(String value1, String value2) {
            addCriterion("activty_name between", value1, value2, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyNameNotBetween(String value1, String value2) {
            addCriterion("activty_name not between", value1, value2, "activtyName");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeIsNull() {
            addCriterion("activty_time is null");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeIsNotNull() {
            addCriterion("activty_time is not null");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeEqualTo(Integer value) {
            addCriterion("activty_time =", value, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeNotEqualTo(Integer value) {
            addCriterion("activty_time <>", value, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeGreaterThan(Integer value) {
            addCriterion("activty_time >", value, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("activty_time >=", value, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeLessThan(Integer value) {
            addCriterion("activty_time <", value, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeLessThanOrEqualTo(Integer value) {
            addCriterion("activty_time <=", value, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeIn(List<Integer> values) {
            addCriterion("activty_time in", values, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeNotIn(List<Integer> values) {
            addCriterion("activty_time not in", values, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeBetween(Integer value1, Integer value2) {
            addCriterion("activty_time between", value1, value2, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("activty_time not between", value1, value2, "activtyTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeIsNull() {
            addCriterion("activty_start_time is null");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeIsNotNull() {
            addCriterion("activty_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeEqualTo(Integer value) {
            addCriterion("activty_start_time =", value, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeNotEqualTo(Integer value) {
            addCriterion("activty_start_time <>", value, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeGreaterThan(Integer value) {
            addCriterion("activty_start_time >", value, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("activty_start_time >=", value, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeLessThan(Integer value) {
            addCriterion("activty_start_time <", value, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeLessThanOrEqualTo(Integer value) {
            addCriterion("activty_start_time <=", value, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeIn(List<Integer> values) {
            addCriterion("activty_start_time in", values, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeNotIn(List<Integer> values) {
            addCriterion("activty_start_time not in", values, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeBetween(Integer value1, Integer value2) {
            addCriterion("activty_start_time between", value1, value2, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyStartTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("activty_start_time not between", value1, value2, "activtyStartTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeIsNull() {
            addCriterion("activty_end_time is null");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeIsNotNull() {
            addCriterion("activty_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeEqualTo(Integer value) {
            addCriterion("activty_end_time =", value, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeNotEqualTo(Integer value) {
            addCriterion("activty_end_time <>", value, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeGreaterThan(Integer value) {
            addCriterion("activty_end_time >", value, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("activty_end_time >=", value, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeLessThan(Integer value) {
            addCriterion("activty_end_time <", value, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeLessThanOrEqualTo(Integer value) {
            addCriterion("activty_end_time <=", value, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeIn(List<Integer> values) {
            addCriterion("activty_end_time in", values, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeNotIn(List<Integer> values) {
            addCriterion("activty_end_time not in", values, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeBetween(Integer value1, Integer value2) {
            addCriterion("activty_end_time between", value1, value2, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyEndTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("activty_end_time not between", value1, value2, "activtyEndTime");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlIsNull() {
            addCriterion("activty_picture_url is null");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlIsNotNull() {
            addCriterion("activty_picture_url is not null");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlEqualTo(String value) {
            addCriterion("activty_picture_url =", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlNotEqualTo(String value) {
            addCriterion("activty_picture_url <>", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlGreaterThan(String value) {
            addCriterion("activty_picture_url >", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlGreaterThanOrEqualTo(String value) {
            addCriterion("activty_picture_url >=", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlLessThan(String value) {
            addCriterion("activty_picture_url <", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlLessThanOrEqualTo(String value) {
            addCriterion("activty_picture_url <=", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlLike(String value) {
            addCriterion("activty_picture_url like", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlNotLike(String value) {
            addCriterion("activty_picture_url not like", value, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlIn(List<String> values) {
            addCriterion("activty_picture_url in", values, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlNotIn(List<String> values) {
            addCriterion("activty_picture_url not in", values, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlBetween(String value1, String value2) {
            addCriterion("activty_picture_url between", value1, value2, "activtyPictureUrl");
            return (Criteria) this;
        }

        public Criteria andActivtyPictureUrlNotBetween(String value1, String value2) {
            addCriterion("activty_picture_url not between", value1, value2, "activtyPictureUrl");
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