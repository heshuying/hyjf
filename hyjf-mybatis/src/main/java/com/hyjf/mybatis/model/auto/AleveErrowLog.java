package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class AleveErrowLog implements Serializable {
    private Integer id;

    private Integer fileline;

    private Integer saveline;

    private String filestring;

    private String filestats;

    private Integer createUserId;

    private Integer createTime;

    private Integer updateUserId;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileline() {
        return fileline;
    }

    public void setFileline(Integer fileline) {
        this.fileline = fileline;
    }

    public Integer getSaveline() {
        return saveline;
    }

    public void setSaveline(Integer saveline) {
        this.saveline = saveline;
    }

    public String getFilestring() {
        return filestring;
    }

    public void setFilestring(String filestring) {
        this.filestring = filestring == null ? null : filestring.trim();
    }

    public String getFilestats() {
        return filestats;
    }

    public void setFilestats(String filestats) {
        this.filestats = filestats == null ? null : filestats.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
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