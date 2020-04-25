/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.agreement;

import com.hyjf.mybatis.model.auto.ProtocolTemplate;

import java.util.List;

/**
 * @author yinhui
 * @version AgreementService, v0.1 2018/6/5 14:11
 */
public interface AgreementService {

    /**
     * 获得对应的协议模板pdf路径
     * @param protocolId 协议模版的ID
     * @return
     */
    public String getAgreementPdf(String protocolId) throws Exception;

    /**
     * 往Redis中放入协议模板内容
     *
     * @param displayName
     * @return
     */
    public boolean setRedisProtocolTemplate(String displayName);

    /**
     * 协议名称 动态获得
     *
     * @return
     */
    List<ProtocolTemplate> getdisplayNameDynamic();
}
