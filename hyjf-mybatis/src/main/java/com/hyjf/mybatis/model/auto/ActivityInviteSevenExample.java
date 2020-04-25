package com.hyjf.mybatis.model.auto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ActivityInviteSevenExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ActivityInviteSevenExample() {
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

        public Criteria andUseridIsNull() {
            addCriterion("userid is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userid is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Integer value) {
            addCriterion("userid =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Integer value) {
            addCriterion("userid <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Integer value) {
            addCriterion("userid >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("userid >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Integer value) {
            addCriterion("userid <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Integer value) {
            addCriterion("userid <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Integer> values) {
            addCriterion("userid in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Integer> values) {
            addCriterion("userid not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Integer value1, Integer value2) {
            addCriterion("userid between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("userid not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedIsNull() {
            addCriterion("userid_invited is null");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedIsNotNull() {
            addCriterion("userid_invited is not null");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedEqualTo(Integer value) {
            addCriterion("userid_invited =", value, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedNotEqualTo(Integer value) {
            addCriterion("userid_invited <>", value, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedGreaterThan(Integer value) {
            addCriterion("userid_invited >", value, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedGreaterThanOrEqualTo(Integer value) {
            addCriterion("userid_invited >=", value, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedLessThan(Integer value) {
            addCriterion("userid_invited <", value, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedLessThanOrEqualTo(Integer value) {
            addCriterion("userid_invited <=", value, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedIn(List<Integer> values) {
            addCriterion("userid_invited in", values, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedNotIn(List<Integer> values) {
            addCriterion("userid_invited not in", values, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedBetween(Integer value1, Integer value2) {
            addCriterion("userid_invited between", value1, value2, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUseridInvitedNotBetween(Integer value1, Integer value2) {
            addCriterion("userid_invited not between", value1, value2, "useridInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUserRealnameIsNull() {
            addCriterion("user_realname is null");
            return (Criteria) this;
        }

        public Criteria andUserRealnameIsNotNull() {
            addCriterion("user_realname is not null");
            return (Criteria) this;
        }

        public Criteria andUserRealnameEqualTo(String value) {
            addCriterion("user_realname =", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameNotEqualTo(String value) {
            addCriterion("user_realname <>", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameGreaterThan(String value) {
            addCriterion("user_realname >", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameGreaterThanOrEqualTo(String value) {
            addCriterion("user_realname >=", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameLessThan(String value) {
            addCriterion("user_realname <", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameLessThanOrEqualTo(String value) {
            addCriterion("user_realname <=", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameLike(String value) {
            addCriterion("user_realname like", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameNotLike(String value) {
            addCriterion("user_realname not like", value, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameIn(List<String> values) {
            addCriterion("user_realname in", values, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameNotIn(List<String> values) {
            addCriterion("user_realname not in", values, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameBetween(String value1, String value2) {
            addCriterion("user_realname between", value1, value2, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUserRealnameNotBetween(String value1, String value2) {
            addCriterion("user_realname not between", value1, value2, "userRealname");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedIsNull() {
            addCriterion("username_invited is null");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedIsNotNull() {
            addCriterion("username_invited is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedEqualTo(String value) {
            addCriterion("username_invited =", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedNotEqualTo(String value) {
            addCriterion("username_invited <>", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedGreaterThan(String value) {
            addCriterion("username_invited >", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedGreaterThanOrEqualTo(String value) {
            addCriterion("username_invited >=", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedLessThan(String value) {
            addCriterion("username_invited <", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedLessThanOrEqualTo(String value) {
            addCriterion("username_invited <=", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedLike(String value) {
            addCriterion("username_invited like", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedNotLike(String value) {
            addCriterion("username_invited not like", value, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedIn(List<String> values) {
            addCriterion("username_invited in", values, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedNotIn(List<String> values) {
            addCriterion("username_invited not in", values, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedBetween(String value1, String value2) {
            addCriterion("username_invited between", value1, value2, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andUsernameInvitedNotBetween(String value1, String value2) {
            addCriterion("username_invited not between", value1, value2, "usernameInvited");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedIsNull() {
            addCriterion("mobile_invited is null");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedIsNotNull() {
            addCriterion("mobile_invited is not null");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedEqualTo(String value) {
            addCriterion("mobile_invited =", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedNotEqualTo(String value) {
            addCriterion("mobile_invited <>", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedGreaterThan(String value) {
            addCriterion("mobile_invited >", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedGreaterThanOrEqualTo(String value) {
            addCriterion("mobile_invited >=", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedLessThan(String value) {
            addCriterion("mobile_invited <", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedLessThanOrEqualTo(String value) {
            addCriterion("mobile_invited <=", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedLike(String value) {
            addCriterion("mobile_invited like", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedNotLike(String value) {
            addCriterion("mobile_invited not like", value, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedIn(List<String> values) {
            addCriterion("mobile_invited in", values, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedNotIn(List<String> values) {
            addCriterion("mobile_invited not in", values, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedBetween(String value1, String value2) {
            addCriterion("mobile_invited between", value1, value2, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andMobileInvitedNotBetween(String value1, String value2) {
            addCriterion("mobile_invited not between", value1, value2, "mobileInvited");
            return (Criteria) this;
        }

        public Criteria andRegistTimeIsNull() {
            addCriterion("regist_time is null");
            return (Criteria) this;
        }

        public Criteria andRegistTimeIsNotNull() {
            addCriterion("regist_time is not null");
            return (Criteria) this;
        }

        public Criteria andRegistTimeEqualTo(Integer value) {
            addCriterion("regist_time =", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeNotEqualTo(Integer value) {
            addCriterion("regist_time <>", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeGreaterThan(Integer value) {
            addCriterion("regist_time >", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("regist_time >=", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeLessThan(Integer value) {
            addCriterion("regist_time <", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeLessThanOrEqualTo(Integer value) {
            addCriterion("regist_time <=", value, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeIn(List<Integer> values) {
            addCriterion("regist_time in", values, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeNotIn(List<Integer> values) {
            addCriterion("regist_time not in", values, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeBetween(Integer value1, Integer value2) {
            addCriterion("regist_time between", value1, value2, "registTime");
            return (Criteria) this;
        }

        public Criteria andRegistTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("regist_time not between", value1, value2, "registTime");
            return (Criteria) this;
        }

        public Criteria andInviteCountIsNull() {
            addCriterion("invite_count is null");
            return (Criteria) this;
        }

        public Criteria andInviteCountIsNotNull() {
            addCriterion("invite_count is not null");
            return (Criteria) this;
        }

        public Criteria andInviteCountEqualTo(Integer value) {
            addCriterion("invite_count =", value, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountNotEqualTo(Integer value) {
            addCriterion("invite_count <>", value, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountGreaterThan(Integer value) {
            addCriterion("invite_count >", value, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("invite_count >=", value, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountLessThan(Integer value) {
            addCriterion("invite_count <", value, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountLessThanOrEqualTo(Integer value) {
            addCriterion("invite_count <=", value, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountIn(List<Integer> values) {
            addCriterion("invite_count in", values, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountNotIn(List<Integer> values) {
            addCriterion("invite_count not in", values, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountBetween(Integer value1, Integer value2) {
            addCriterion("invite_count between", value1, value2, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andInviteCountNotBetween(Integer value1, Integer value2) {
            addCriterion("invite_count not between", value1, value2, "inviteCount");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstIsNull() {
            addCriterion("money_first is null");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstIsNotNull() {
            addCriterion("money_first is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstEqualTo(BigDecimal value) {
            addCriterion("money_first =", value, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstNotEqualTo(BigDecimal value) {
            addCriterion("money_first <>", value, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstGreaterThan(BigDecimal value) {
            addCriterion("money_first >", value, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("money_first >=", value, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstLessThan(BigDecimal value) {
            addCriterion("money_first <", value, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstLessThanOrEqualTo(BigDecimal value) {
            addCriterion("money_first <=", value, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstIn(List<BigDecimal> values) {
            addCriterion("money_first in", values, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstNotIn(List<BigDecimal> values) {
            addCriterion("money_first not in", values, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money_first between", value1, value2, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andMoneyFirstNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money_first not between", value1, value2, "moneyFirst");
            return (Criteria) this;
        }

        public Criteria andInvestTimeIsNull() {
            addCriterion("invest_time is null");
            return (Criteria) this;
        }

        public Criteria andInvestTimeIsNotNull() {
            addCriterion("invest_time is not null");
            return (Criteria) this;
        }

        public Criteria andInvestTimeEqualTo(Integer value) {
            addCriterion("invest_time =", value, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeNotEqualTo(Integer value) {
            addCriterion("invest_time <>", value, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeGreaterThan(Integer value) {
            addCriterion("invest_time >", value, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("invest_time >=", value, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeLessThan(Integer value) {
            addCriterion("invest_time <", value, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeLessThanOrEqualTo(Integer value) {
            addCriterion("invest_time <=", value, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeIn(List<Integer> values) {
            addCriterion("invest_time in", values, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeNotIn(List<Integer> values) {
            addCriterion("invest_time not in", values, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeBetween(Integer value1, Integer value2) {
            addCriterion("invest_time between", value1, value2, "investTime");
            return (Criteria) this;
        }

        public Criteria andInvestTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("invest_time not between", value1, value2, "investTime");
            return (Criteria) this;
        }

        public Criteria andCouponNameIsNull() {
            addCriterion("coupon_name is null");
            return (Criteria) this;
        }

        public Criteria andCouponNameIsNotNull() {
            addCriterion("coupon_name is not null");
            return (Criteria) this;
        }

        public Criteria andCouponNameEqualTo(String value) {
            addCriterion("coupon_name =", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotEqualTo(String value) {
            addCriterion("coupon_name <>", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameGreaterThan(String value) {
            addCriterion("coupon_name >", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameGreaterThanOrEqualTo(String value) {
            addCriterion("coupon_name >=", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameLessThan(String value) {
            addCriterion("coupon_name <", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameLessThanOrEqualTo(String value) {
            addCriterion("coupon_name <=", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameLike(String value) {
            addCriterion("coupon_name like", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotLike(String value) {
            addCriterion("coupon_name not like", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameIn(List<String> values) {
            addCriterion("coupon_name in", values, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotIn(List<String> values) {
            addCriterion("coupon_name not in", values, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameBetween(String value1, String value2) {
            addCriterion("coupon_name between", value1, value2, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotBetween(String value1, String value2) {
            addCriterion("coupon_name not between", value1, value2, "couponName");
            return (Criteria) this;
        }

        public Criteria andRewardTypeIsNull() {
            addCriterion("reward_type is null");
            return (Criteria) this;
        }

        public Criteria andRewardTypeIsNotNull() {
            addCriterion("reward_type is not null");
            return (Criteria) this;
        }

        public Criteria andRewardTypeEqualTo(Integer value) {
            addCriterion("reward_type =", value, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeNotEqualTo(Integer value) {
            addCriterion("reward_type <>", value, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeGreaterThan(Integer value) {
            addCriterion("reward_type >", value, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("reward_type >=", value, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeLessThan(Integer value) {
            addCriterion("reward_type <", value, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeLessThanOrEqualTo(Integer value) {
            addCriterion("reward_type <=", value, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeIn(List<Integer> values) {
            addCriterion("reward_type in", values, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeNotIn(List<Integer> values) {
            addCriterion("reward_type not in", values, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeBetween(Integer value1, Integer value2) {
            addCriterion("reward_type between", value1, value2, "rewardType");
            return (Criteria) this;
        }

        public Criteria andRewardTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("reward_type not between", value1, value2, "rewardType");
            return (Criteria) this;
        }

        public Criteria andCouponCodeIsNull() {
            addCriterion("coupon_code is null");
            return (Criteria) this;
        }

        public Criteria andCouponCodeIsNotNull() {
            addCriterion("coupon_code is not null");
            return (Criteria) this;
        }

        public Criteria andCouponCodeEqualTo(String value) {
            addCriterion("coupon_code =", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeNotEqualTo(String value) {
            addCriterion("coupon_code <>", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeGreaterThan(String value) {
            addCriterion("coupon_code >", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeGreaterThanOrEqualTo(String value) {
            addCriterion("coupon_code >=", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeLessThan(String value) {
            addCriterion("coupon_code <", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeLessThanOrEqualTo(String value) {
            addCriterion("coupon_code <=", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeLike(String value) {
            addCriterion("coupon_code like", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeNotLike(String value) {
            addCriterion("coupon_code not like", value, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeIn(List<String> values) {
            addCriterion("coupon_code in", values, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeNotIn(List<String> values) {
            addCriterion("coupon_code not in", values, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeBetween(String value1, String value2) {
            addCriterion("coupon_code between", value1, value2, "couponCode");
            return (Criteria) this;
        }

        public Criteria andCouponCodeNotBetween(String value1, String value2) {
            addCriterion("coupon_code not between", value1, value2, "couponCode");
            return (Criteria) this;
        }

        public Criteria andSendFlgIsNull() {
            addCriterion("send_flg is null");
            return (Criteria) this;
        }

        public Criteria andSendFlgIsNotNull() {
            addCriterion("send_flg is not null");
            return (Criteria) this;
        }

        public Criteria andSendFlgEqualTo(Integer value) {
            addCriterion("send_flg =", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgNotEqualTo(Integer value) {
            addCriterion("send_flg <>", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgGreaterThan(Integer value) {
            addCriterion("send_flg >", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_flg >=", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgLessThan(Integer value) {
            addCriterion("send_flg <", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgLessThanOrEqualTo(Integer value) {
            addCriterion("send_flg <=", value, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgIn(List<Integer> values) {
            addCriterion("send_flg in", values, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgNotIn(List<Integer> values) {
            addCriterion("send_flg not in", values, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgBetween(Integer value1, Integer value2) {
            addCriterion("send_flg between", value1, value2, "sendFlg");
            return (Criteria) this;
        }

        public Criteria andSendFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("send_flg not between", value1, value2, "sendFlg");
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