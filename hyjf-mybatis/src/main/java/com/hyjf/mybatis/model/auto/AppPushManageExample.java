package com.hyjf.mybatis.model.auto;

import java.util.ArrayList;
import java.util.List;

public class AppPushManageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public AppPushManageExample() {
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andJumpTypeIsNull() {
            addCriterion("jump_type is null");
            return (Criteria) this;
        }

        public Criteria andJumpTypeIsNotNull() {
            addCriterion("jump_type is not null");
            return (Criteria) this;
        }

        public Criteria andJumpTypeEqualTo(Integer value) {
            addCriterion("jump_type =", value, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeNotEqualTo(Integer value) {
            addCriterion("jump_type <>", value, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeGreaterThan(Integer value) {
            addCriterion("jump_type >", value, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("jump_type >=", value, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeLessThan(Integer value) {
            addCriterion("jump_type <", value, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeLessThanOrEqualTo(Integer value) {
            addCriterion("jump_type <=", value, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeIn(List<Integer> values) {
            addCriterion("jump_type in", values, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeNotIn(List<Integer> values) {
            addCriterion("jump_type not in", values, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeBetween(Integer value1, Integer value2) {
            addCriterion("jump_type between", value1, value2, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("jump_type not between", value1, value2, "jumpType");
            return (Criteria) this;
        }

        public Criteria andJumpContentIsNull() {
            addCriterion("jump_content is null");
            return (Criteria) this;
        }

        public Criteria andJumpContentIsNotNull() {
            addCriterion("jump_content is not null");
            return (Criteria) this;
        }

        public Criteria andJumpContentEqualTo(Integer value) {
            addCriterion("jump_content =", value, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentNotEqualTo(Integer value) {
            addCriterion("jump_content <>", value, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentGreaterThan(Integer value) {
            addCriterion("jump_content >", value, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentGreaterThanOrEqualTo(Integer value) {
            addCriterion("jump_content >=", value, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentLessThan(Integer value) {
            addCriterion("jump_content <", value, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentLessThanOrEqualTo(Integer value) {
            addCriterion("jump_content <=", value, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentIn(List<Integer> values) {
            addCriterion("jump_content in", values, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentNotIn(List<Integer> values) {
            addCriterion("jump_content not in", values, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentBetween(Integer value1, Integer value2) {
            addCriterion("jump_content between", value1, value2, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpContentNotBetween(Integer value1, Integer value2) {
            addCriterion("jump_content not between", value1, value2, "jumpContent");
            return (Criteria) this;
        }

        public Criteria andJumpUrlIsNull() {
            addCriterion("jump_url is null");
            return (Criteria) this;
        }

        public Criteria andJumpUrlIsNotNull() {
            addCriterion("jump_url is not null");
            return (Criteria) this;
        }

        public Criteria andJumpUrlEqualTo(String value) {
            addCriterion("jump_url =", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlNotEqualTo(String value) {
            addCriterion("jump_url <>", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlGreaterThan(String value) {
            addCriterion("jump_url >", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlGreaterThanOrEqualTo(String value) {
            addCriterion("jump_url >=", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlLessThan(String value) {
            addCriterion("jump_url <", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlLessThanOrEqualTo(String value) {
            addCriterion("jump_url <=", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlLike(String value) {
            addCriterion("jump_url like", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlNotLike(String value) {
            addCriterion("jump_url not like", value, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlIn(List<String> values) {
            addCriterion("jump_url in", values, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlNotIn(List<String> values) {
            addCriterion("jump_url not in", values, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlBetween(String value1, String value2) {
            addCriterion("jump_url between", value1, value2, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andJumpUrlNotBetween(String value1, String value2) {
            addCriterion("jump_url not between", value1, value2, "jumpUrl");
            return (Criteria) this;
        }

        public Criteria andOrderIsNull() {
            addCriterion("`order` is null");
            return (Criteria) this;
        }

        public Criteria andOrderIsNotNull() {
            addCriterion("`order` is not null");
            return (Criteria) this;
        }

        public Criteria andOrderEqualTo(Integer value) {
            addCriterion("`order` =", value, "order");
            return (Criteria) this;
        }

        public Criteria andOrderNotEqualTo(Integer value) {
            addCriterion("`order` <>", value, "order");
            return (Criteria) this;
        }

        public Criteria andOrderGreaterThan(Integer value) {
            addCriterion("`order` >", value, "order");
            return (Criteria) this;
        }

        public Criteria andOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("`order` >=", value, "order");
            return (Criteria) this;
        }

        public Criteria andOrderLessThan(Integer value) {
            addCriterion("`order` <", value, "order");
            return (Criteria) this;
        }

        public Criteria andOrderLessThanOrEqualTo(Integer value) {
            addCriterion("`order` <=", value, "order");
            return (Criteria) this;
        }

        public Criteria andOrderIn(List<Integer> values) {
            addCriterion("`order` in", values, "order");
            return (Criteria) this;
        }

        public Criteria andOrderNotIn(List<Integer> values) {
            addCriterion("`order` not in", values, "order");
            return (Criteria) this;
        }

        public Criteria andOrderBetween(Integer value1, Integer value2) {
            addCriterion("`order` between", value1, value2, "order");
            return (Criteria) this;
        }

        public Criteria andOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("`order` not between", value1, value2, "order");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTimeStartIsNull() {
            addCriterion("time_start is null");
            return (Criteria) this;
        }

        public Criteria andTimeStartIsNotNull() {
            addCriterion("time_start is not null");
            return (Criteria) this;
        }

        public Criteria andTimeStartEqualTo(Integer value) {
            addCriterion("time_start =", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotEqualTo(Integer value) {
            addCriterion("time_start <>", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartGreaterThan(Integer value) {
            addCriterion("time_start >", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_start >=", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartLessThan(Integer value) {
            addCriterion("time_start <", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartLessThanOrEqualTo(Integer value) {
            addCriterion("time_start <=", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartIn(List<Integer> values) {
            addCriterion("time_start in", values, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotIn(List<Integer> values) {
            addCriterion("time_start not in", values, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartBetween(Integer value1, Integer value2) {
            addCriterion("time_start between", value1, value2, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotBetween(Integer value1, Integer value2) {
            addCriterion("time_start not between", value1, value2, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeEndIsNull() {
            addCriterion("time_end is null");
            return (Criteria) this;
        }

        public Criteria andTimeEndIsNotNull() {
            addCriterion("time_end is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEndEqualTo(Integer value) {
            addCriterion("time_end =", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotEqualTo(Integer value) {
            addCriterion("time_end <>", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndGreaterThan(Integer value) {
            addCriterion("time_end >", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_end >=", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndLessThan(Integer value) {
            addCriterion("time_end <", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndLessThanOrEqualTo(Integer value) {
            addCriterion("time_end <=", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndIn(List<Integer> values) {
            addCriterion("time_end in", values, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotIn(List<Integer> values) {
            addCriterion("time_end not in", values, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndBetween(Integer value1, Integer value2) {
            addCriterion("time_end between", value1, value2, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotBetween(Integer value1, Integer value2) {
            addCriterion("time_end not between", value1, value2, "timeEnd");
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

        public Criteria andAddAdminIsNull() {
            addCriterion("add_admin is null");
            return (Criteria) this;
        }

        public Criteria andAddAdminIsNotNull() {
            addCriterion("add_admin is not null");
            return (Criteria) this;
        }

        public Criteria andAddAdminEqualTo(Integer value) {
            addCriterion("add_admin =", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotEqualTo(Integer value) {
            addCriterion("add_admin <>", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminGreaterThan(Integer value) {
            addCriterion("add_admin >", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminGreaterThanOrEqualTo(Integer value) {
            addCriterion("add_admin >=", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminLessThan(Integer value) {
            addCriterion("add_admin <", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminLessThanOrEqualTo(Integer value) {
            addCriterion("add_admin <=", value, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminIn(List<Integer> values) {
            addCriterion("add_admin in", values, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotIn(List<Integer> values) {
            addCriterion("add_admin not in", values, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminBetween(Integer value1, Integer value2) {
            addCriterion("add_admin between", value1, value2, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andAddAdminNotBetween(Integer value1, Integer value2) {
            addCriterion("add_admin not between", value1, value2, "addAdmin");
            return (Criteria) this;
        }

        public Criteria andThumbIsNull() {
            addCriterion("thumb is null");
            return (Criteria) this;
        }

        public Criteria andThumbIsNotNull() {
            addCriterion("thumb is not null");
            return (Criteria) this;
        }

        public Criteria andThumbEqualTo(String value) {
            addCriterion("thumb =", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotEqualTo(String value) {
            addCriterion("thumb <>", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbGreaterThan(String value) {
            addCriterion("thumb >", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbGreaterThanOrEqualTo(String value) {
            addCriterion("thumb >=", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbLessThan(String value) {
            addCriterion("thumb <", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbLessThanOrEqualTo(String value) {
            addCriterion("thumb <=", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbLike(String value) {
            addCriterion("thumb like", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotLike(String value) {
            addCriterion("thumb not like", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbIn(List<String> values) {
            addCriterion("thumb in", values, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotIn(List<String> values) {
            addCriterion("thumb not in", values, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbBetween(String value1, String value2) {
            addCriterion("thumb between", value1, value2, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotBetween(String value1, String value2) {
            addCriterion("thumb not between", value1, value2, "thumb");
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