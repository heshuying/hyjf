/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.agreement;

import com.hyjf.mybatis.model.auto.ProtocolTemplate;

import java.util.List;

/**
 * @author yinhui
 * @version AgreementService, v0.1 2018/6/4 16:20
 */
public interface AgreementService {

    public List<String> getImgUrlList(String protocolId) throws Exception;

    /**
     * 往Redis中放入协议模板内容
     * @param protocolType
     * @return
     */
    public boolean setRedisProtocolTemplate(String protocolType);

    /**
     * 协议名称 动态获得
     * @return
     */
    List<ProtocolTemplate> getdisplayNameDynamic();
}
