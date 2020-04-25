/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.agreement;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.ProtocolTemplateExample;
import com.hyjf.wechat.base.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinhui
 * @version AgreementServiceImpl, v0.1 2018/6/4 16:20
 */
@Service
public class AgreementServiceImpl extends BaseServiceImpl implements AgreementService {

    /**
     * 查询协议图片
     *
     * @param protocolId 协议模版的ID
     * @return
     */
    @Override
    public List<String> getImgUrlList(String protocolId) throws Exception{
        List<String> listImg = new ArrayList<>();

        // 拿出来的信息 /hyjfdata/data/pdf/template/1528268728879.pdf&/hyjfdata/data/pdf/template/1528268728879-0, 1, 2, 3, 4
        String templateUrl = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_URL + protocolId);

        if (StringUtils.isEmpty(templateUrl)) {
            throw new Exception("templateUrl is null");
        }

        if (!templateUrl.contains("&")) {
            throw new Exception("templateUrl is null");
        }

        String[] strUrl = templateUrl.split("&");// &之前的是 pdf路径，&之后的是 图片路径

        //图片地址存储的路径是： /hyjfdata/data/pdf/template/1528087341328-0,1,2
        String imgUrl = strUrl[1];
        if (!imgUrl.contains("-")) {
            throw new Exception("templateUrl is null");
        }

        String[] url = imgUrl.split("-");
        String imgPath = url[0];// /hyjfdata/data/pdf/template/1528087341328
        String[] imgSize = url[1].split(",");// 0,1,2
        for (String str : imgSize) {

            listImg.add(new StringBuilder().append(imgPath).append("/").append(str).append(".jpg").toString());
        }

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("listImg", listImg);

        return listImg;
    }

    /**
     * 往Redis中放入协议模板内容
     * @param protocolType
     * @return
     */
    @Override
    public boolean setRedisProtocolTemplate(String protocolType){
        ProtocolTemplateExample examplev = new ProtocolTemplateExample();
        ProtocolTemplateExample.Criteria criteria = examplev.createCriteria();
        criteria.andProtocolTypeEqualTo(protocolType);
        criteria.andStatusEqualTo(1);
        List<ProtocolTemplate> list = protocolTemplateMapper.selectByExample(examplev);

        if(CollectionUtils.isEmpty(list)){
            return false;
        }

        ProtocolTemplate protocolTemplate = list.get(0);

        //将协议模板放入redis中
        RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_URL+protocolTemplate.getProtocolId(),protocolTemplate.getProtocolUrl()+"&"+protocolTemplate.getImgUrl());
        //获取协议模板前端显示名称对应的别名
        String alias = ProtocolEnum.getAlias(protocolTemplate.getProtocolType());
        if(StringUtils.isNotBlank(alias)){
            RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_ALIAS+alias,protocolTemplate.getProtocolId());//协议 ID放入redis
        }
        return true;
    }

    /**
     * 协议名称 动态获得
     * @return
     */
    @Override
    public List<ProtocolTemplate> getdisplayNameDynamic() {
        return protocolTemplateMapper.getdisplayNameDynamic();
    }
}
