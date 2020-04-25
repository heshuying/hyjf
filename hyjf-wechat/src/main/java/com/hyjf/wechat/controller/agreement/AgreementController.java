package com.hyjf.wechat.controller.agreement;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.wechat.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.wechat.base.BaseController;

import java.util.HashMap;
import java.util.List;

/**
 * 协议相关  /wx/agreement
 *
 * @author sss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年11月13日
 */
@Controller("hjhAgreementController")
@RequestMapping(value = AgreementDefine.REQUEST_MAPPING)
public class AgreementController extends BaseController {

    @Autowired
    private AgreementService agreementService;

    /**
     * 跳转协议
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AgreementDefine.GODETAIL_MAPPING)
    public ModelAndView goDetail(String pageType) {
        LogUtil.startLog(AgreementController.class.getName(), AgreementDefine.GODETAIL_MAPPING);
        ModelAndView modeAndView = null;
        if (pageType.equals("wx_hjh_contract")) {
            // 微信 散标风险揭示书H5
            modeAndView = new ModelAndView("invest/app_contract");
        } else if (pageType.equals("wx_hjh_serve")) {
            // 智投服务协议
            modeAndView = new ModelAndView("invest/type-new-hjhxieyi");
        } else if (pageType.equals("wx_hjh_invest")) {
            // 居间服务协议
            modeAndView = new ModelAndView("invest/type-invest");
        }
        LogUtil.endLog(AgreementController.class.getName(), AgreementDefine.GODETAIL_MAPPING);
        return modeAndView;
    }

    /**
     * 通过 模板名字或者协议模板HTML
     *
     * @param aliasName 协议模版的别名
     * @return
     */
    @RequestMapping(value = AgreementDefine.GOAGREEMENT_IMG, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JSONObject getUrl(@RequestParam String aliasName) {
        JSONObject jsonObject = new JSONObject();

        LogUtil.startLog(AgreementController.class.getName(), AgreementDefine.GOAGREEMENT_IMG);
        if (StringUtils.isEmpty(aliasName)) {
            jsonObject.put("status","99");
            jsonObject.put("statusDesc","请求参数非法");
            return jsonObject;
        }

        //是否在枚举中有定义
        String protocolType = ProtocolEnum.getDisplayName(aliasName);
        if (StringUtils.isEmpty(protocolType)) {
            jsonObject.put("status","99");
            jsonObject.put("statusDesc","请求参数非法");
            return jsonObject;
        }

        String protocolId = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + aliasName);
        if (StringUtils.isEmpty(protocolId)) {

            boolean flag = agreementService.setRedisProtocolTemplate(protocolType);
            if (!flag) {
                jsonObject.put("status","99");
                jsonObject.put("statusDesc","数据非法");
                return jsonObject;
            }
            protocolId = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + aliasName);

        }

        try {
            List<String> imgJSON = agreementService.getImgUrlList(protocolId);
            jsonObject.put("request", imgJSON);
            jsonObject.put("status","000");
            jsonObject.put("statusDesc","成功");
        } catch (Exception e) {
            jsonObject.put("status","99");
            jsonObject.put("statusDesc","失败");
        }

        LogUtil.endLog(AgreementController.class.getName(), AgreementDefine.GOAGREEMENT_IMG);
        return jsonObject;
    }
    /**
     * 协议名称 动态获得
     *
     * @return
     */
    @RequestMapping(value = AgreementDefine.GET_DISPLAY_NAME_DYNAMIC, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JSONObject getdisplayNameDynamic(){
        LogUtil.startLog(AgreementController.class.getName(), AgreementDefine.GET_DISPLAY_NAME_DYNAMIC);
        JSONObject jsonObject = null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        jsonObject = JSONObject.parseObject(RedisUtils.get(RedisConstants.PROTOCOL_PARAMS));
        if (jsonObject == null) {
            jsonObject = new JSONObject();
            try {
                List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
                //是否在枚举中有定义
                for (ProtocolTemplate p : list) {
                    String protocolType = p.getProtocolType();
                    String alia = ProtocolEnum.getAlias(protocolType);
                    if (alia != null){
                        map.put(alia, p.getDisplayName());
                    }
                }
                jsonObject.put("status","000");
                jsonObject.put("statusDesc","成功");
                jsonObject.put("displayName",map);
            } catch (Exception e) {
                jsonObject.put("status","99");
                jsonObject.put("statusDesc","失败");
            }
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        return jsonObject;
    }

}
