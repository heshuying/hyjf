package com.hyjf.wechat.controller.user.coupon;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.validator.Validator;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.service.coupon.WxCouponService;
import com.hyjf.wechat.util.ResultEnum;

@Controller
@RequestMapping(value = WxCouponDefine.REQUEST_MAPPING)
public class WxCouponController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private WxCouponService wxCouponService;

    /**
     * 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
     *
     * @param request
     * @param response
     * @return
     * @author cuigq
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxCouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION)
    public BaseResultBean getProjectAvailableUserCoupon(HttpServletRequest request, HttpServletResponse response) {
        SimpleResultBean<Map<String, Object>> result = new SimpleResultBean<>();
        String borrowNid = request.getParameter("borrowNid");
        String money = request.getParameter("money");

        Integer userId = requestUtil.getRequestUserId(request);
        Map<String, Object> vo = wxCouponService.getProjectAvailableUserCoupon(borrowNid, money, userId);

        result.setObject(vo);
        return result;
    }
    
    /**
     * 汇计划可用优惠券列表
     * @param request
     * @param response
     * @return
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxCouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_HJH_ACTION)
    public JSONObject getProjectAvailableUserCouponHJH(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        Integer userId = requestUtil.getRequestUserId(request);
        String borrowNid = request.getParameter("borrowNid");
        String platform = request.getParameter("platform"); // 微官网值是 1
        String money = request.getParameter("money");
        
        // 检查参数正确性
        if ( Validator.isNull(borrowNid) || Validator.isNull(userId)) {
            ret.put("status", ResultEnum.PARAM.getStatus());
            ret.put("statusDesc", ResultEnum.PARAM.getStatusDesc());
            return ret;
        }
        
        if(StringUtils.isBlank(platform)){
        	platform = "1";
        }
        if(money==null||"".equals(money)||money.length()==0){
            money="0";
        }

		// HJH的接口 
        wxCouponService.getHJHProjectAvailableUserCoupon(platform,borrowNid,userId,ret,money);
        
        logger.info("查询优惠券列表结果："+ret.toJSONString());
        ret.put("status", ResultEnum.SUCCESS.getStatus());
        ret.put("statusDesc", ResultEnum.SUCCESS.getStatusDesc());
        return ret;
    }
}
