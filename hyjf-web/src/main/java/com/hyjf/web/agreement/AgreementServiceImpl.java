/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.agreement;

import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.ProtocolTemplateExample;
import com.hyjf.web.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author yinhui
 * @version AgreementServiceImpl, v0.1 2018/6/5 14:11
 */
@Service
public class AgreementServiceImpl extends BaseServiceImpl implements AgreementService {


    /**
     * 获得对应的协议模板pdf路径
     *
     * @param protocolId 协议模版的ID
     * @return
     */
    @Override
    public String getAgreementPdf(String protocolId) throws Exception {

        // 拿出来的信息 /hyjfdata/data/pdf/template/1528268728879.pdf&/hyjfdata/data/pdf/template/1528268728879-0, 1, 2, 3, 4
        String templateUrl = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_URL + protocolId);

        if (StringUtils.isEmpty(templateUrl)) {
            throw new Exception("templateUrl is null");
        }

        if (!templateUrl.contains("&")) {
            throw new Exception("templateUrl is null");
        }

        String[] strUrl = templateUrl.split("&");// &之前的是 pdf路径，&之后的是 图片路径

        String fileDomainUrl = strUrl[0];

        return fileDomainUrl;
    }

    /**
     * 往Redis中放入协议模板内容
     *
     * @param displayName
     * @return
     */
    @Override
    public boolean setRedisProtocolTemplate(String displayName) {
        ProtocolTemplateExample examplev = new ProtocolTemplateExample();
        ProtocolTemplateExample.Criteria criteria = examplev.createCriteria();
        criteria.andProtocolTypeEqualTo(displayName);
        criteria.andStatusEqualTo(1);
        List<ProtocolTemplate> list = protocolTemplateMapper.selectByExample(examplev);

        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        ProtocolTemplate protocolTemplate = list.get(0);

        //将协议模板放入redis中
        RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_URL + protocolTemplate.getProtocolId(), protocolTemplate.getProtocolUrl() + "&" + protocolTemplate.getImgUrl());
        //获取协议模板前端显示名称对应的别名
        String alias = ProtocolEnum.getAlias(protocolTemplate.getProtocolType());
        if (org.apache.commons.lang.StringUtils.isNotBlank(alias)) {
            RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + alias, protocolTemplate.getProtocolId());//协议 ID放入redis
        }
        return true;
    }
    /**
     * 协议名称 动态获得
     *
     * @return
     */
    @Override
    public List<ProtocolTemplate> getdisplayNameDynamic() {
        return protocolTemplateMapper.getdisplayNameDynamic();
    }
}
