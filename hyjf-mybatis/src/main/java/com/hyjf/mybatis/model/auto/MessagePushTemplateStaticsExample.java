package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class MessagePushTemplateStaticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public MessagePushTemplateStaticsExample() {
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

        public Criteria andMsgIdIsNull() {
            addCriterion("msg_id is null");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNotNull() {
            addCriterion("msg_id is not null");
            return (Criteria) this;
        }

        public Criteria andMsgIdEqualTo(Integer value) {
            addCriterion("msg_id =", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotEqualTo(Integer value) {
            addCriterion("msg_id <>", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThan(Integer value) {
            addCriterion("msg_id >", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("msg_id >=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThan(Integer value) {
            addCriterion("msg_id <", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThanOrEqualTo(Integer value) {
            addCriterion("msg_id <=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdIn(List<Integer> values) {
            addCriterion("msg_id in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotIn(List<Integer> values) {
            addCriterion("msg_id not in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdBetween(Integer value1, Integer value2) {
            addCriterion("msg_id between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("msg_id not between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andTagIdIsNull() {
            addCriterion("tag_id is null");
            return (Criteria) this;
        }

        public Criteria andTagIdIsNotNull() {
            addCriterion("tag_id is not null");
            return (Criteria) this;
        }

        public Criteria andTagIdEqualTo(Integer value) {
            addCriterion("tag_id =", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotEqualTo(Integer value) {
            addCriterion("tag_id <>", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdGreaterThan(Integer value) {
            addCriterion("tag_id >", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("tag_id >=", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdLessThan(Integer value) {
            addCriterion("tag_id <", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdLessThanOrEqualTo(Integer value) {
            addCriterion("tag_id <=", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdIn(List<Integer> values) {
            addCriterion("tag_id in", values, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotIn(List<Integer> values) {
            addCriterion("tag_id not in", values, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdBetween(Integer value1, Integer value2) {
            addCriterion("tag_id between", value1, value2, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotBetween(Integer value1, Integer value2) {
            addCriterion("tag_id not between", value1, value2, "tagId");
            return (Criteria) this;
        }

        public Criteria andMsgTitleIsNull() {
            addCriterion("msg_title is null");
            return (Criteria) this;
        }

        public Criteria andMsgTitleIsNotNull() {
            addCriterion("msg_title is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTitleEqualTo(String value) {
            addCriterion("msg_title =", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleNotEqualTo(String value) {
            addCriterion("msg_title <>", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleGreaterThan(String value) {
            addCriterion("msg_title >", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleGreaterThanOrEqualTo(String value) {
            addCriterion("msg_title >=", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleLessThan(String value) {
            addCriterion("msg_title <", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleLessThanOrEqualTo(String value) {
            addCriterion("msg_title <=", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleLike(String value) {
            addCriterion("msg_title like", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleNotLike(String value) {
            addCriterion("msg_title not like", value, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleIn(List<String> values) {
            addCriterion("msg_title in", values, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleNotIn(List<String> values) {
            addCriterion("msg_title not in", values, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleBetween(String value1, String value2) {
            addCriterion("msg_title between", value1, value2, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgTitleNotBetween(String value1, String value2) {
            addCriterion("msg_title not between", value1, value2, "msgTitle");
            return (Criteria) this;
        }

        public Criteria andMsgCodeIsNull() {
            addCriterion("msg_code is null");
            return (Criteria) this;
        }

        public Criteria andMsgCodeIsNotNull() {
            addCriterion("msg_code is not null");
            return (Criteria) this;
        }

        public Criteria andMsgCodeEqualTo(String value) {
            addCriterion("msg_code =", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeNotEqualTo(String value) {
            addCriterion("msg_code <>", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeGreaterThan(String value) {
            addCriterion("msg_code >", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeGreaterThanOrEqualTo(String value) {
            addCriterion("msg_code >=", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeLessThan(String value) {
            addCriterion("msg_code <", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeLessThanOrEqualTo(String value) {
            addCriterion("msg_code <=", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeLike(String value) {
            addCriterion("msg_code like", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeNotLike(String value) {
            addCriterion("msg_code not like", value, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeIn(List<String> values) {
            addCriterion("msg_code in", values, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeNotIn(List<String> values) {
            addCriterion("msg_code not in", values, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeBetween(String value1, String value2) {
            addCriterion("msg_code between", value1, value2, "msgCode");
            return (Criteria) this;
        }

        public Criteria andMsgCodeNotBetween(String value1, String value2) {
            addCriterion("msg_code not between", value1, value2, "msgCode");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNull() {
            addCriterion("send_time is null");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNotNull() {
            addCriterion("send_time is not null");
            return (Criteria) this;
        }

        public Criteria andSendTimeEqualTo(Integer value) {
            addCriterion("send_time =", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotEqualTo(Integer value) {
            addCriterion("send_time <>", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThan(Integer value) {
            addCriterion("send_time >", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_time >=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThan(Integer value) {
            addCriterion("send_time <", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThanOrEqualTo(Integer value) {
            addCriterion("send_time <=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeIn(List<Integer> values) {
            addCriterion("send_time in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotIn(List<Integer> values) {
            addCriterion("send_time not in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeBetween(Integer value1, Integer value2) {
            addCriterion("send_time between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("send_time not between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountIsNull() {
            addCriterion("ios_destination_count is null");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountIsNotNull() {
            addCriterion("ios_destination_count is not null");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountEqualTo(Integer value) {
            addCriterion("ios_destination_count =", value, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountNotEqualTo(Integer value) {
            addCriterion("ios_destination_count <>", value, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountGreaterThan(Integer value) {
            addCriterion("ios_destination_count >", value, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ios_destination_count >=", value, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountLessThan(Integer value) {
            addCriterion("ios_destination_count <", value, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountLessThanOrEqualTo(Integer value) {
            addCriterion("ios_destination_count <=", value, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountIn(List<Integer> values) {
            addCriterion("ios_destination_count in", values, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountNotIn(List<Integer> values) {
            addCriterion("ios_destination_count not in", values, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountBetween(Integer value1, Integer value2) {
            addCriterion("ios_destination_count between", value1, value2, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosDestinationCountNotBetween(Integer value1, Integer value2) {
            addCriterion("ios_destination_count not between", value1, value2, "iosDestinationCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountIsNull() {
            addCriterion("ios_send_count is null");
            return (Criteria) this;
        }

        public Criteria andIosSendCountIsNotNull() {
            addCriterion("ios_send_count is not null");
            return (Criteria) this;
        }

        public Criteria andIosSendCountEqualTo(Integer value) {
            addCriterion("ios_send_count =", value, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountNotEqualTo(Integer value) {
            addCriterion("ios_send_count <>", value, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountGreaterThan(Integer value) {
            addCriterion("ios_send_count >", value, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ios_send_count >=", value, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountLessThan(Integer value) {
            addCriterion("ios_send_count <", value, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountLessThanOrEqualTo(Integer value) {
            addCriterion("ios_send_count <=", value, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountIn(List<Integer> values) {
            addCriterion("ios_send_count in", values, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountNotIn(List<Integer> values) {
            addCriterion("ios_send_count not in", values, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountBetween(Integer value1, Integer value2) {
            addCriterion("ios_send_count between", value1, value2, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosSendCountNotBetween(Integer value1, Integer value2) {
            addCriterion("ios_send_count not between", value1, value2, "iosSendCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountIsNull() {
            addCriterion("ios_read_count is null");
            return (Criteria) this;
        }

        public Criteria andIosReadCountIsNotNull() {
            addCriterion("ios_read_count is not null");
            return (Criteria) this;
        }

        public Criteria andIosReadCountEqualTo(Integer value) {
            addCriterion("ios_read_count =", value, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountNotEqualTo(Integer value) {
            addCriterion("ios_read_count <>", value, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountGreaterThan(Integer value) {
            addCriterion("ios_read_count >", value, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ios_read_count >=", value, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountLessThan(Integer value) {
            addCriterion("ios_read_count <", value, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountLessThanOrEqualTo(Integer value) {
            addCriterion("ios_read_count <=", value, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountIn(List<Integer> values) {
            addCriterion("ios_read_count in", values, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountNotIn(List<Integer> values) {
            addCriterion("ios_read_count not in", values, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountBetween(Integer value1, Integer value2) {
            addCriterion("ios_read_count between", value1, value2, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andIosReadCountNotBetween(Integer value1, Integer value2) {
            addCriterion("ios_read_count not between", value1, value2, "iosReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountIsNull() {
            addCriterion("android_destination_count is null");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountIsNotNull() {
            addCriterion("android_destination_count is not null");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountEqualTo(Integer value) {
            addCriterion("android_destination_count =", value, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountNotEqualTo(Integer value) {
            addCriterion("android_destination_count <>", value, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountGreaterThan(Integer value) {
            addCriterion("android_destination_count >", value, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("android_destination_count >=", value, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountLessThan(Integer value) {
            addCriterion("android_destination_count <", value, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountLessThanOrEqualTo(Integer value) {
            addCriterion("android_destination_count <=", value, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountIn(List<Integer> values) {
            addCriterion("android_destination_count in", values, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountNotIn(List<Integer> values) {
            addCriterion("android_destination_count not in", values, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountBetween(Integer value1, Integer value2) {
            addCriterion("android_destination_count between", value1, value2, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidDestinationCountNotBetween(Integer value1, Integer value2) {
            addCriterion("android_destination_count not between", value1, value2, "androidDestinationCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountIsNull() {
            addCriterion("android_send_count is null");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountIsNotNull() {
            addCriterion("android_send_count is not null");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountEqualTo(Integer value) {
            addCriterion("android_send_count =", value, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountNotEqualTo(Integer value) {
            addCriterion("android_send_count <>", value, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountGreaterThan(Integer value) {
            addCriterion("android_send_count >", value, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("android_send_count >=", value, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountLessThan(Integer value) {
            addCriterion("android_send_count <", value, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountLessThanOrEqualTo(Integer value) {
            addCriterion("android_send_count <=", value, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountIn(List<Integer> values) {
            addCriterion("android_send_count in", values, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountNotIn(List<Integer> values) {
            addCriterion("android_send_count not in", values, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountBetween(Integer value1, Integer value2) {
            addCriterion("android_send_count between", value1, value2, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidSendCountNotBetween(Integer value1, Integer value2) {
            addCriterion("android_send_count not between", value1, value2, "androidSendCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountIsNull() {
            addCriterion("android_read_count is null");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountIsNotNull() {
            addCriterion("android_read_count is not null");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountEqualTo(Integer value) {
            addCriterion("android_read_count =", value, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountNotEqualTo(Integer value) {
            addCriterion("android_read_count <>", value, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountGreaterThan(Integer value) {
            addCriterion("android_read_count >", value, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("android_read_count >=", value, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountLessThan(Integer value) {
            addCriterion("android_read_count <", value, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountLessThanOrEqualTo(Integer value) {
            addCriterion("android_read_count <=", value, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountIn(List<Integer> values) {
            addCriterion("android_read_count in", values, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountNotIn(List<Integer> values) {
            addCriterion("android_read_count not in", values, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountBetween(Integer value1, Integer value2) {
            addCriterion("android_read_count between", value1, value2, "androidReadCount");
            return (Criteria) this;
        }

        public Criteria andAndroidReadCountNotBetween(Integer value1, Integer value2) {
            addCriterion("android_read_count not between", value1, value2, "androidReadCount");
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