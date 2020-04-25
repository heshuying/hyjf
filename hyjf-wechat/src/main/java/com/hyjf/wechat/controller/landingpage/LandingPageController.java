package com.hyjf.wechat.controller.landingpage;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.service.landingpage.LandingPageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * 着陆页 Controller
 * @Author : huanghui
 * @Version : hyjf 1.0
 * @Date : 2018年05月24日
 */
@Controller
@RequestMapping(value = LandingPageDefine.REQUEST_MAPPING)
public class LandingPageController  extends BaseController {

    private Logger logger = LoggerFactory.getLogger(LandingPageController.class);

    @Autowired
    private LandingPageService landingPageService;

    /**
     * 微信着陆页 用户出借数据
     * @param request
     * @param response
     * @return interestSum:为用户赚取收益,亿元; investorSum:累计出借者,万人.
     */
    @ResponseBody
    @RequestMapping(value = LandingPageDefine.LANDINGPAGE_USER_DATA, method = RequestMethod.GET)
    public BaseResultBean landingPageUserData(HttpServletRequest request, HttpServletResponse response){
        LogUtil.startLog(LandingPageDefine.THIS_CLASS, LandingPageDefine.LANDINGPAGE_USER_DATA);

        LandingPageResulltVo landingPageResulltVo = new LandingPageResulltVo();

        //累计收益(亿元)
        BigDecimal profitSum = BigDecimal.ZERO;
        //累计出借者(万人),等同于累计交易笔数
        Integer serveUserSum = null;

        profitSum = landingPageService.selectInterestSum().divide(new BigDecimal("100000000")).setScale(0, BigDecimal.ROUND_DOWN);
        serveUserSum = landingPageService.selectAllUserRegisterCounts();

        if (serveUserSum == null){
            serveUserSum = 0;
        }else {
            serveUserSum = serveUserSum / 10000;
        }

        landingPageResulltVo.setProfitSum(profitSum);
        landingPageResulltVo.setServeUserSum(serveUserSum);
        return landingPageResulltVo;
    }

    /**
     * 通过推荐人ID查询推荐人的渠道
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LandingPageDefine.SELECT_USER_UTM_INFO, method = RequestMethod.POST)
    public JSONObject selectUserUtmInfo(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();

        String refferUserId = request.getParameter("refferUserId");

        if (StringUtils.isNotBlank(refferUserId)){
            AdminUserDetailCustomize userUtmInfo = landingPageService.selectUserUtmInfo(Integer.valueOf(request.getParameter("refferUserId")));
            System.out.println(userUtmInfo);
            if (userUtmInfo != null){
                jsonObject.put("status", "000");
                jsonObject.put("userUtmId", userUtmInfo.getSourceId().toString());
                jsonObject.put("statusDsc", "请求成功!");
            }else {
                jsonObject.put("status", "000");
                jsonObject.put("userUtmId", "");
                jsonObject.put("statusDsc", "推荐人渠道为空");
            }
        }else {
            jsonObject.put("status", "999");
            jsonObject.put("statusDsc", "推荐人ID不能为空");
        }
        return jsonObject;
    }


}
