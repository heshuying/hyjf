package com.hyjf.web.bank.wechat.hjh.coupon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.validator.Validator;
import com.hyjf.web.BaseController;
import com.hyjf.web.coupon.CouponService;

@Controller("hjhCouponController")
@RequestMapping(value = CouponDefine.REQUEST_MAPPING)
public class CouponController extends BaseController {

    @Autowired
    private CouponService couponService;
    
    /**
     * 
     * 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION)
    public JSONObject getProjectAvailableUserCoupon(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        String borrowNid = request.getParameter("borrowNid");
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        String money = request.getParameter("money");
        String investType = request.getParameter("borrowType"); // 项目类型  HJH传HJH
        String userId = request.getParameter("userId"); // 
     // 检查参数正确性
        if ( Validator.isNull(borrowNid) || Validator.isNull(sign)|| 
                Validator.isNull(platform)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        if(money==null||"".equals(money)||money.length()==0){
            money="0";
        }
        if("HJH".equals(investType)){
            // HJH的接口 
            couponService.getHJHProjectAvailableUserCoupon(platform,borrowNid,ret,money,userId);
        }
        
        ret.put("status", "0");
        ret.put("statusDesc", "成功");
        return ret;
    }
}
