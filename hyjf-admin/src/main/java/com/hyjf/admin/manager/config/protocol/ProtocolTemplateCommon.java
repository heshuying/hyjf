package com.hyjf.admin.manager.config.protocol;

import com.hyjf.mybatis.model.auto.ProtocolLog;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.ProtocolVersion;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2018/5/25.
 */
public class ProtocolTemplateCommon implements Serializable {

    //    //协议模板
    private ProtocolTemplate protocolTemplate;
    //    //协议日志
    private List<ProtocolLog> protocolLog;
    //    //协议版本
    private List<ProtocolVersion> protocolVersion;

    //修改时间
    private String updateTime;

    public ProtocolTemplate getProtocolTemplate() {
        return protocolTemplate;
    }

    public void setProtocolTemplate(ProtocolTemplate protocolTemplate) {
        this.protocolTemplate = protocolTemplate;
    }

    public List<ProtocolLog> getProtocolLog() {
        return protocolLog;
    }

    public void setProtocolLog(List<ProtocolLog> protocolLog) {
        this.protocolLog = protocolLog;
    }
    public List<ProtocolVersion> getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(List<ProtocolVersion> protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
