package com.hyjf.app.user.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.project.ProjectDefine;
import com.hyjf.app.user.coupon.util.CouponCheckUtil;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.coupon.CouponUserForAppCustomize;

@Controller
@RequestMapping(value = CouponDefine.REQUEST_MAPPING)
public class CouponController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CouponController.class);
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponCheckUtil couponCheckUtil;
    
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

        logger.info("investType is :{}", investType);
        if(investType != null){
        	
        	if(investType==null||!"HJH".equals(investType)){
        		// 如果为空  就执行以前的逻辑
        		couponService.getProjectAvailableUserCoupon(platform,borrowNid,sign,ret,money);
        	}else{
        		// HJH的接口 
        		couponService.getHJHProjectAvailableUserCoupon(platform,borrowNid,sign,ret,money);
        	}
        }else {
			if(borrowNid.contains("HJH")){
				couponService.getHJHProjectAvailableUserCoupon(platform,borrowNid,sign,ret,money);
			}else{
				// 如果为空  就执行以前的逻辑
        		couponService.getProjectAvailableUserCoupon(platform,borrowNid,sign,ret,money);
			}
		}
        
        //logger.info("查询优惠券列表结果："+ret.toJSONString());
        ret.put("status", "0");
        ret.put(CustomConstants.APP_REQUEST, CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING + CouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION);
        ret.put("statusDesc", "成功");
        return ret;
    }
    
    /**
     * 
     * 获取我的优惠券列表
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_USERCOUPON)
    public JSONObject getUserCoupons(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        String sign = request.getParameter("sign");
        String couponStatus = "0";
        Integer page = 1;
        Integer pageSize = 100;
        
        //验证请求参数
        if (Validator.isNull(sign)) {
            ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
            ret.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
            ret.put(CustomConstants.APP_REQUEST, CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING + CouponDefine.GET_USERCOUPON);
            return ret;
        }
        
        String host =  PropUtils.getSystem("hyjf.web.host") + CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING
                + CouponDefine.GET_USERCOUPON_DETAIL + "?sign=" + sign;
        Integer userId = SecretUtil.getUserId(sign);
        
        //验证请求参数
        if (Validator.isNull(userId)) {
            ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
            ret.put(CustomConstants.APP_STATUS_DESC, "获取userid失败");
            ret.put(CustomConstants.APP_REQUEST, CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING + CouponDefine.GET_USERCOUPON);
            return ret;
        }
        
        if(!StringUtils.isEmpty(request.getParameter("page"))){
            page = Integer.parseInt(request.getParameter("page"));
        }
        
        if(!StringUtils.isEmpty(request.getParameter("pageSize"))){
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }
        
        String resultStr = couponService.getUserCouponsData(couponStatus, page, pageSize, userId, host);
        
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        JSONArray data = resultJson.getJSONArray("data");

        ret.put("couponList", this.convertToCouponUser(data.toJSONString()));
        //ret.put("couponList", resultJson.get("data"));
        ret.put("couponTotal", resultJson.get("couponTotal"));
        ret.put("couponStatus", resultJson.get("couponStatus"));
        
        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
        ret.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
        ret.put(CustomConstants.APP_REQUEST, CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING + CouponDefine.GET_USERCOUPON);
        
        LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_LIST_ACTION);
        return ret;
    }

    private List<CouponUserForAppCustomize> convertToCouponUser(String jsonArrayStr) {
        List<CouponUserForAppCustomize> list = new ArrayList<>();
        List<CouponUserForAppCustomize> configs = JSON.parseArray(jsonArrayStr, CouponUserForAppCustomize.class);
        if (!CollectionUtils.isEmpty(configs)) {
            for (CouponUserForAppCustomize couponUserForAppCustomize : configs) {
                if (!"2".equals(couponUserForAppCustomize.getCouponTypeOrigin())) {
                    couponUserForAppCustomize.setCouponQuota(couponUserForAppCustomize.getCouponQuota() + "元");
                }
                list.add(couponUserForAppCustomize);
            }
        }
        return list;
    }

    /**
     * 
     * 优惠券详情页
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CouponDefine.GET_USERCOUPON_DETAIL)
    public ModelAndView getUserCouponDetail(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(CouponDefine.class.toString(), CouponDefine.GET_USERCOUPON_DETAIL);
        ModelAndView modelAndView = new ModelAndView(CouponDefine.COUPON_DETAIL_PATH);
        
        // 优惠券id
        String couponUserId = request.getParameter("id");
        String sign = request.getParameter("sign");
        
        // 请求参数校验
        if(Validator.isNull(couponUserId) || Validator.isNull(sign)){
            modelAndView = new ModelAndView(CouponDefine.ERROR_PTAH);
            return modelAndView;
        }

        // 调用接口获取数据
        String resultStr = couponService.getCouponsDetailData(couponUserId);
        JSONObject resultJson = JSONObject.parseObject(resultStr);

        modelAndView.addObject("detail",resultJson.get("detail"));
        
        modelAndView.addObject("receivedMoney", resultJson.get("receivedMoney"));
        
        modelAndView.addObject("earnings", resultJson.get("earnings"));
        
        modelAndView.addObject("couponRecoverlist", resultJson.get("couponRecoverlist"));
        
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.GET_USERCOUPON_DETAIL);
        
        return modelAndView;

    }
    
    
    /**
     * 
     * 活动弹出页面初始化
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CouponDefine.COUPON_ACTIVE_INIT)
    public ModelAndView couponActiveInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(CouponDefine.class.toString(), CouponDefine.COUPON_ACTIVE_INIT);
        ModelAndView modelAndView = new ModelAndView(CouponDefine.ACTIVE_INIT_PATH);
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        modelAndView.addObject("sign",sign);
        modelAndView.addObject("platform",platform);
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.COUPON_ACTIVE_INIT);
        return modelAndView;
    }
    
    /**
     * 
     * 注册活动送体验金验证
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.REGISTER_ACTIVE_CHECK)
    public JSONObject registerActiveCheck(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
        JSONObject ret = new JSONObject();
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        String activeId = request.getParameter("activeId");
        String msg="";
        //活动有效性验证
        msg=couponCheckUtil.checkActivityIfAvailable(activeId);
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg",msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret; 
        }
        //判断活动适用终端
        msg=couponCheckUtil.checkActivityPlatform(activeId,platform);
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg",msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret; 
        }
        
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            ret.put("error",0);
            ret.put("msg","用户未登录");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret; 
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 4;
        if(userId==null||userId<=0){
            ret.put("error",0);
            ret.put("msg","用户未登录");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret; 
        }
        
        //判断用户是否已经领取
        msg=couponCheckUtil.checkActivityIfInvolvement(activeId,userId+"");
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg","您已经领过体验金券啦，只能领一次哦~您可以参加【测评送加息券】活动，最高加息1.5%呢~~~");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret; 
        }
        
        
        //判断用户是否老用户
        msg=couponCheckUtil.checkOldUser(activeId,userId+"");
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg","做为一个诚实的老用户，体验金就不能领啦 您可以参加【测评送加息券】活动，最高加息1.5%呢~~~");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret; 
        }
        
        ret.put("error",1);
        ret.put("msg","您已参加过此活动");
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
        return ret;
    }
    
    
    /**
     * 
     * 评测送加息券活动验证
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.EVALUATE_ACTIVE_CHECK)
    public JSONObject evaluateActiveCheck(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
        JSONObject ret = new JSONObject();
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        String activeId = request.getParameter("activeId");
        String msg="";
        //活动有效性验证
        msg=couponCheckUtil.checkActivityIfAvailable(activeId);
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg",msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret; 
        }
        //判断活动适用终端
        msg=couponCheckUtil.checkActivityPlatform(activeId,platform);
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg",msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret; 
        }
     // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            ret.put("error",0);
            ret.put("msg","用户未登录");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret; 
        }
        
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 4;
        if(userId==null||userId<=0){
            ret.put("error",0);
            ret.put("msg","用户未登录");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret; 
        }
        
        //判断用户是否已经领取
        msg=couponCheckUtil.checkActivityIfInvolvement(activeId,userId+"");
        if(msg!=null&&msg.length()!=0){
            ret.put("error",1);
            ret.put("msg","您已领过加息券啦，可在优惠券列表中查看");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret; 
        }
        
        
        ret.put("error",3);
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
        return ret;
    }
    
    
    public static void main(String[] args) {
        BigDecimal earnings = DuePrincipalAndInterestUtils
                .getMonthInterest(new BigDecimal(1000), new BigDecimal(14), 6)
                .divide(new BigDecimal("100"));
        
        System.out.println(earnings.toString());
    }

}
