package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class InviteRecommendPrizeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public InviteRecommendPrizeExample() {
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

        public Criteria andPrizeGroupIsNull() {
            addCriterion("prize_group is null");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupIsNotNull() {
            addCriterion("prize_group is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupEqualTo(String value) {
            addCriterion("prize_group =", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupNotEqualTo(String value) {
            addCriterion("prize_group <>", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupGreaterThan(String value) {
            addCriterion("prize_group >", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupGreaterThanOrEqualTo(String value) {
            addCriterion("prize_group >=", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupLessThan(String value) {
            addCriterion("prize_group <", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupLessThanOrEqualTo(String value) {
            addCriterion("prize_group <=", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupLike(String value) {
            addCriterion("prize_group like", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupNotLike(String value) {
            addCriterion("prize_group not like", value, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupIn(List<String> values) {
            addCriterion("prize_group in", values, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupNotIn(List<String> values) {
            addCriterion("prize_group not in", values, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupBetween(String value1, String value2) {
            addCriterion("prize_group between", value1, value2, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeGroupNotBetween(String value1, String value2) {
            addCriterion("prize_group not between", value1, value2, "prizeGroup");
            return (Criteria) this;
        }

        public Criteria andPrizeCountIsNull() {
            addCriterion("prize_count is null");
            return (Criteria) this;
        }

        public Criteria andPrizeCountIsNotNull() {
            addCriterion("prize_count is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeCountEqualTo(Integer value) {
            addCriterion("prize_count =", value, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountNotEqualTo(Integer value) {
            addCriterion("prize_count <>", value, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountGreaterThan(Integer value) {
            addCriterion("prize_count >", value, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_count >=", value, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountLessThan(Integer value) {
            addCriterion("prize_count <", value, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountLessThanOrEqualTo(Integer value) {
            addCriterion("prize_count <=", value, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountIn(List<Integer> values) {
            addCriterion("prize_count in", values, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountNotIn(List<Integer> values) {
            addCriterion("prize_count not in", values, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountBetween(Integer value1, Integer value2) {
            addCriterion("prize_count between", value1, value2, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andPrizeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_count not between", value1, value2, "prizeCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountIsNull() {
            addCriterion("used_recommend_count is null");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountIsNotNull() {
            addCriterion("used_recommend_count is not null");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountEqualTo(Integer value) {
            addCriterion("used_recommend_count =", value, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountNotEqualTo(Integer value) {
            addCriterion("used_recommend_count <>", value, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountGreaterThan(Integer value) {
            addCriterion("used_recommend_count >", value, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("used_recommend_count >=", value, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountLessThan(Integer value) {
            addCriterion("used_recommend_count <", value, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountLessThanOrEqualTo(Integer value) {
            addCriterion("used_recommend_count <=", value, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountIn(List<Integer> values) {
            addCriterion("used_recommend_count in", values, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountNotIn(List<Integer> values) {
            addCriterion("used_recommend_count not in", values, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountBetween(Integer value1, Integer value2) {
            addCriterion("used_recommend_count between", value1, value2, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andUsedRecommendCountNotBetween(Integer value1, Integer value2) {
            addCriterion("used_recommend_count not between", value1, value2, "usedRecommendCount");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeIsNull() {
            addCriterion("prize_type is null");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeIsNotNull() {
            addCriterion("prize_type is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeEqualTo(Integer value) {
            addCriterion("prize_type =", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeNotEqualTo(Integer value) {
            addCriterion("prize_type <>", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeGreaterThan(Integer value) {
            addCriterion("prize_type >", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_type >=", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeLessThan(Integer value) {
            addCriterion("prize_type <", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeLessThanOrEqualTo(Integer value) {
            addCriterion("prize_type <=", value, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeIn(List<Integer> values) {
            addCriterion("prize_type in", values, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeNotIn(List<Integer> values) {
            addCriterion("prize_type not in", values, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeBetween(Integer value1, Integer value2) {
            addCriterion("prize_type between", value1, value2, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_type not between", value1, value2, "prizeType");
            return (Criteria) this;
        }

        public Criteria andPrizeKindIsNull() {
            addCriterion("prize_kind is null");
            return (Criteria) this;
        }

        public Criteria andPrizeKindIsNotNull() {
            addCriterion("prize_kind is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeKindEqualTo(Integer value) {
            addCriterion("prize_kind =", value, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindNotEqualTo(Integer value) {
            addCriterion("prize_kind <>", value, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindGreaterThan(Integer value) {
            addCriterion("prize_kind >", value, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_kind >=", value, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindLessThan(Integer value) {
            addCriterion("prize_kind <", value, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindLessThanOrEqualTo(Integer value) {
            addCriterion("prize_kind <=", value, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindIn(List<Integer> values) {
            addCriterion("prize_kind in", values, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindNotIn(List<Integer> values) {
            addCriterion("prize_kind not in", values, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindBetween(Integer value1, Integer value2) {
            addCriterion("prize_kind between", value1, value2, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeKindNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_kind not between", value1, value2, "prizeKind");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagIsNull() {
            addCriterion("prize_send_flag is null");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagIsNotNull() {
            addCriterion("prize_send_flag is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagEqualTo(Integer value) {
            addCriterion("prize_send_flag =", value, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagNotEqualTo(Integer value) {
            addCriterion("prize_send_flag <>", value, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagGreaterThan(Integer value) {
            addCriterion("prize_send_flag >", value, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("prize_send_flag >=", value, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagLessThan(Integer value) {
            addCriterion("prize_send_flag <", value, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagLessThanOrEqualTo(Integer value) {
            addCriterion("prize_send_flag <=", value, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagIn(List<Integer> values) {
            addCriterion("prize_send_flag in", values, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagNotIn(List<Integer> values) {
            addCriterion("prize_send_flag not in", values, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagBetween(Integer value1, Integer value2) {
            addCriterion("prize_send_flag between", value1, value2, "prizeSendFlag");
            return (Criteria) this;
        }

        public Criteria andPrizeSendFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("prize_send_flag not between", value1, value2, "prizeSendFlag");
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