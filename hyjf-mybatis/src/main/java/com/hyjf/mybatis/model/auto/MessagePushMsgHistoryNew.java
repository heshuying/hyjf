package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

/**
 * 
 * 取MessagePushMsgHistory的部分属性
 * @author wyz
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月7日
 * @see 下午4:25:40
 */
public class MessagePushMsgHistoryNew implements Serializable {
    private Integer id;

//    private Integer tagId;

    

    private String title;
    private String introduction;

    private String time;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Integer getTagId() {
//        return tagId;
//    }
//
//    public void setTagId(Integer tagId) {
//        this.tagId = tagId;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

   
}