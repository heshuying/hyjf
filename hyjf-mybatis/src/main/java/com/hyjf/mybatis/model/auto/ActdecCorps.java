package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActdecCorps implements Serializable {
    private Integer id;

    private String captainOpid;

    private String captainName;

    private String captainHead;

    private String member1Opid;

    private String member1Name;

    private String member1Head;

    private String member2Opid;

    private String member2Name;

    private String member2Head;

    private Integer corpsName;

    private Integer prizeType;

    private Integer teamType;

    private String createUser;

    private Integer createTime;

    private String updateUser;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaptainOpid() {
        return captainOpid;
    }

    public void setCaptainOpid(String captainOpid) {
        this.captainOpid = captainOpid == null ? null : captainOpid.trim();
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName == null ? null : captainName.trim();
    }

    public String getCaptainHead() {
        return captainHead;
    }

    public void setCaptainHead(String captainHead) {
        this.captainHead = captainHead == null ? null : captainHead.trim();
    }

    public String getMember1Opid() {
        return member1Opid;
    }

    public void setMember1Opid(String member1Opid) {
        this.member1Opid = member1Opid == null ? null : member1Opid.trim();
    }

    public String getMember1Name() {
        return member1Name;
    }

    public void setMember1Name(String member1Name) {
        this.member1Name = member1Name == null ? null : member1Name.trim();
    }

    public String getMember1Head() {
        return member1Head;
    }

    public void setMember1Head(String member1Head) {
        this.member1Head = member1Head == null ? null : member1Head.trim();
    }

    public String getMember2Opid() {
        return member2Opid;
    }

    public void setMember2Opid(String member2Opid) {
        this.member2Opid = member2Opid == null ? null : member2Opid.trim();
    }

    public String getMember2Name() {
        return member2Name;
    }

    public void setMember2Name(String member2Name) {
        this.member2Name = member2Name == null ? null : member2Name.trim();
    }

    public String getMember2Head() {
        return member2Head;
    }

    public void setMember2Head(String member2Head) {
        this.member2Head = member2Head == null ? null : member2Head.trim();
    }

    public Integer getCorpsName() {
        return corpsName;
    }

    public void setCorpsName(Integer corpsName) {
        this.corpsName = corpsName;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getTeamType() {
        return teamType;
    }

    public void setTeamType(Integer teamType) {
        this.teamType = teamType;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}