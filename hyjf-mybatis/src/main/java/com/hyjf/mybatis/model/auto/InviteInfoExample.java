package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class InviteInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public InviteInfoExample() {
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

        public Criteria andInviteUserIsNull() {
            addCriterion("invite_user is null");
            return (Criteria) this;
        }

        public Criteria andInviteUserIsNotNull() {
            addCriterion("invite_user is not null");
            return (Criteria) this;
        }

        public Criteria andInviteUserEqualTo(Integer value) {
            addCriterion("invite_user =", value, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserNotEqualTo(Integer value) {
            addCriterion("invite_user <>", value, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserGreaterThan(Integer value) {
            addCriterion("invite_user >", value, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("invite_user >=", value, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserLessThan(Integer value) {
            addCriterion("invite_user <", value, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserLessThanOrEqualTo(Integer value) {
            addCriterion("invite_user <=", value, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserIn(List<Integer> values) {
            addCriterion("invite_user in", values, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserNotIn(List<Integer> values) {
            addCriterion("invite_user not in", values, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserBetween(Integer value1, Integer value2) {
            addCriterion("invite_user between", value1, value2, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteUserNotBetween(Integer value1, Integer value2) {
            addCriterion("invite_user not between", value1, value2, "inviteUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserIsNull() {
            addCriterion("invite_by_user is null");
            return (Criteria) this;
        }

        public Criteria andInviteByUserIsNotNull() {
            addCriterion("invite_by_user is not null");
            return (Criteria) this;
        }

        public Criteria andInviteByUserEqualTo(Integer value) {
            addCriterion("invite_by_user =", value, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserNotEqualTo(Integer value) {
            addCriterion("invite_by_user <>", value, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserGreaterThan(Integer value) {
            addCriterion("invite_by_user >", value, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("invite_by_user >=", value, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserLessThan(Integer value) {
            addCriterion("invite_by_user <", value, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserLessThanOrEqualTo(Integer value) {
            addCriterion("invite_by_user <=", value, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserIn(List<Integer> values) {
            addCriterion("invite_by_user in", values, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserNotIn(List<Integer> values) {
            addCriterion("invite_by_user not in", values, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserBetween(Integer value1, Integer value2) {
            addCriterion("invite_by_user between", value1, value2, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andInviteByUserNotBetween(Integer value1, Integer value2) {
            addCriterion("invite_by_user not between", value1, value2, "inviteByUser");
            return (Criteria) this;
        }

        public Criteria andGroupCodeIsNull() {
            addCriterion("group_code is null");
            return (Criteria) this;
        }

        public Criteria andGroupCodeIsNotNull() {
            addCriterion("group_code is not null");
            return (Criteria) this;
        }

        public Criteria andGroupCodeEqualTo(String value) {
            addCriterion("group_code =", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeNotEqualTo(String value) {
            addCriterion("group_code <>", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeGreaterThan(String value) {
            addCriterion("group_code >", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeGreaterThanOrEqualTo(String value) {
            addCriterion("group_code >=", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeLessThan(String value) {
            addCriterion("group_code <", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeLessThanOrEqualTo(String value) {
            addCriterion("group_code <=", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeLike(String value) {
            addCriterion("group_code like", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeNotLike(String value) {
            addCriterion("group_code not like", value, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeIn(List<String> values) {
            addCriterion("group_code in", values, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeNotIn(List<String> values) {
            addCriterion("group_code not in", values, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeBetween(String value1, String value2) {
            addCriterion("group_code between", value1, value2, "groupCode");
            return (Criteria) this;
        }

        public Criteria andGroupCodeNotBetween(String value1, String value2) {
            addCriterion("group_code not between", value1, value2, "groupCode");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceIsNull() {
            addCriterion("recommend_source is null");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceIsNotNull() {
            addCriterion("recommend_source is not null");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceEqualTo(Integer value) {
            addCriterion("recommend_source =", value, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceNotEqualTo(Integer value) {
            addCriterion("recommend_source <>", value, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceGreaterThan(Integer value) {
            addCriterion("recommend_source >", value, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceGreaterThanOrEqualTo(Integer value) {
            addCriterion("recommend_source >=", value, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceLessThan(Integer value) {
            addCriterion("recommend_source <", value, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceLessThanOrEqualTo(Integer value) {
            addCriterion("recommend_source <=", value, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceIn(List<Integer> values) {
            addCriterion("recommend_source in", values, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceNotIn(List<Integer> values) {
            addCriterion("recommend_source not in", values, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceBetween(Integer value1, Integer value2) {
            addCriterion("recommend_source between", value1, value2, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendSourceNotBetween(Integer value1, Integer value2) {
            addCriterion("recommend_source not between", value1, value2, "recommendSource");
            return (Criteria) this;
        }

        public Criteria andRecommendCountIsNull() {
            addCriterion("recommend_count is null");
            return (Criteria) this;
        }

        public Criteria andRecommendCountIsNotNull() {
            addCriterion("recommend_count is not null");
            return (Criteria) this;
        }

        public Criteria andRecommendCountEqualTo(Integer value) {
            addCriterion("recommend_count =", value, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountNotEqualTo(Integer value) {
            addCriterion("recommend_count <>", value, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountGreaterThan(Integer value) {
            addCriterion("recommend_count >", value, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("recommend_count >=", value, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountLessThan(Integer value) {
            addCriterion("recommend_count <", value, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountLessThanOrEqualTo(Integer value) {
            addCriterion("recommend_count <=", value, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountIn(List<Integer> values) {
            addCriterion("recommend_count in", values, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountNotIn(List<Integer> values) {
            addCriterion("recommend_count not in", values, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountBetween(Integer value1, Integer value2) {
            addCriterion("recommend_count between", value1, value2, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andRecommendCountNotBetween(Integer value1, Integer value2) {
            addCriterion("recommend_count not between", value1, value2, "recommendCount");
            return (Criteria) this;
        }

        public Criteria andSendFlagIsNull() {
            addCriterion("send_flag is null");
            return (Criteria) this;
        }

        public Criteria andSendFlagIsNotNull() {
            addCriterion("send_flag is not null");
            return (Criteria) this;
        }

        public Criteria andSendFlagEqualTo(Integer value) {
            addCriterion("send_flag =", value, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagNotEqualTo(Integer value) {
            addCriterion("send_flag <>", value, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagGreaterThan(Integer value) {
            addCriterion("send_flag >", value, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_flag >=", value, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagLessThan(Integer value) {
            addCriterion("send_flag <", value, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagLessThanOrEqualTo(Integer value) {
            addCriterion("send_flag <=", value, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagIn(List<Integer> values) {
            addCriterion("send_flag in", values, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagNotIn(List<Integer> values) {
            addCriterion("send_flag not in", values, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagBetween(Integer value1, Integer value2) {
            addCriterion("send_flag between", value1, value2, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andSendFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("send_flag not between", value1, value2, "sendFlag");
            return (Criteria) this;
        }

        public Criteria andTenderStatusIsNull() {
            addCriterion("tender_status is null");
            return (Criteria) this;
        }

        public Criteria andTenderStatusIsNotNull() {
            addCriterion("tender_status is not null");
            return (Criteria) this;
        }

        public Criteria andTenderStatusEqualTo(Integer value) {
            addCriterion("tender_status =", value, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusNotEqualTo(Integer value) {
            addCriterion("tender_status <>", value, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusGreaterThan(Integer value) {
            addCriterion("tender_status >", value, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("tender_status >=", value, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusLessThan(Integer value) {
            addCriterion("tender_status <", value, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusLessThanOrEqualTo(Integer value) {
            addCriterion("tender_status <=", value, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusIn(List<Integer> values) {
            addCriterion("tender_status in", values, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusNotIn(List<Integer> values) {
            addCriterion("tender_status not in", values, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusBetween(Integer value1, Integer value2) {
            addCriterion("tender_status between", value1, value2, "tenderStatus");
            return (Criteria) this;
        }

        public Criteria andTenderStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("tender_status not between", value1, value2, "tenderStatus");
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