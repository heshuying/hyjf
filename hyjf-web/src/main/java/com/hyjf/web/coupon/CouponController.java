package com.hyjf.web.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
import com.hyjf.web.coupon.util.CouponCheckUtil;
import com.hyjf.web.user.mytender.MyTenderDefine;
import com.hyjf.web.util.TreeDESUtils;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = CouponDefine.REQUEST_MAPPING)
public class CouponController {

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
		String borrowNid = request.getParameter("nid");
//		String sign = request.getParameter("sign"); 
//		String platform = request.getParameter("platform");
		String money = request.getParameter("money");
		// 检查参数正确性
		if (Validator.isNull(borrowNid) ) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		if (money == null || "".equals(money) || money.length() == 0) {
			money = "0";
		}
		Integer userId = WebUtils.getUserId(request);
		couponService.getProjectAvailableUserCoupon(CustomConstants.CLIENT_PC, borrowNid, userId, ret, money);

        ret.put("status", "0");
        ret.put(CustomConstants.APP_REQUEST, CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING
                + CouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION);
        ret.put("statusDesc", "成功");
        return ret;
    }

    /**
     * 
     * 初始化優惠券列表頁
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CouponDefine.GET_USERCOUPON)
    public ModelAndView getUserCoupons(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), CouponDefine.GET_USERCOUPON);
        ModelAndView modelAndView = new ModelAndView(CouponDefine.COUPON_LIST_PATH);

        LogUtil.endLog(this.getClass().getName(), CouponDefine.GET_USERCOUPON);
        return modelAndView;
    }

    
    /**
     * 
     * 获取优惠券数据
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_USERCOUPON_LIST)
    public CouponListBean searchUserCouponList(@ModelAttribute CouponListBean form, HttpServletRequest request,
        HttpServletResponse response) {

        LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_LIST_ACTION);

        // 用户ID
        Integer userId = WebUtils.getUserId(request);
        form.setUserId(userId.toString());
        if(form.getUsedFlag()==null){
            form.setUsedFlag(1);
        }
        
        this.createCouponListPage(request, form);

        form.success();
        // userId三重加密
        String encodeData = "";
        String timestamp = System.currentTimeMillis() / 1000 + "";
        try {
            // 对key采用非MD5加密方式进行三重加密算法
            encodeData = TreeDESUtils.getEncrypt(timestamp, String.valueOf(userId));
            // encodeData = URLEncoder.encode(encodeData, "UTF-8");
            form.setUserId(encodeData);
            form.setTimestamp(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_LIST_ACTION);
        return form;
    }

    
   private void createCouponListPage(HttpServletRequest request, CouponListBean form) {

        // 统计相应的用户出借项目总数
        int recordTotal = this.couponService.countUserCouponTotal(form);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            // 查询相应的用户出借项目列表
            List<CouponUserListCustomize> recordList = couponService.selectUserCouponList(form,
                    paginator.getOffset(), paginator.getLimit());
            
            for(CouponUserListCustomize coupon : recordList){
                //操作平台
                List<ParamName> clients=this.couponService.getParamNameList("CLIENT");
                //被选中操作平台
                String clientString = "";
                String clientSed[] = StringUtils.split(coupon.getCouponSystem(), ",");
                for(int i=0 ; i< clientSed.length;i++){
                    if("-1".equals(clientSed[i])){
                        clientString=clientString+"不限";
                        break;
                    }else{
                        for (ParamName paramName : clients) {
                            if(clientSed[i].equals(paramName.getNameCd())){
                                if(i!=0&&clientString.length()!=0){
                                    clientString=clientString+"/";
                                }
                                clientString=clientString+paramName.getName();
                                
                            }
                        }
                    }
                }
                coupon.setCouponSystem(clientString);
                
                
              //被选中项目类型  pcc20160715
              String projectString = "";
              String projectSed[] = StringUtils.split(coupon.getProjectType(), ",");

              if(coupon.getProjectType().indexOf("-1")!=-1){
                   projectString="所有散标/新手/智投项目";
               }else{
                   projectString=projectString+"所有";
                   for (String project : projectSed) {
                        if("1".equals(project)){
                            projectString=projectString+"散标/";
                        }  
                        if("2".equals(project)){
                            projectString=projectString+"";
                        } 
                        if("3".equals(project)){
                            projectString=projectString+"新手/";
                        } 
                        if("4".equals(project)){
                            projectString=projectString+"";
                        } 
                        if("5".equals(project)){
                            projectString=projectString+"";
                        }
                        if("6".equals(project)){
                            projectString=projectString+"智投/";
                        }
                            
                   }
                   projectString = StringUtils.removeEnd(
                           projectString, "/");
                   projectString=projectString+"项目";
                }
                coupon.setProjectType(projectString);
            }
            form.setPaginator(paginator);
            form.setCouponUserList(recordList);
        } else {
            form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
            form.setCouponUserList(new ArrayList<CouponUserListCustomize>());
        }
    }

    /**
     * 
     * 活动弹出页面初始化
     * 
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
        modelAndView.addObject("sign", sign);
        modelAndView.addObject("platform", platform);
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.COUPON_ACTIVE_INIT);
        return modelAndView;
    }

    /**
     * 
     * 注册活动送体验金验证
     * 
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
        String platform = CustomConstants.CLIENT_PC;
        String activeId = request.getParameter("activeId");
        String msg = "";
        // 活动有效性验证
        msg = couponCheckUtil.checkActivityIfAvailable(activeId);
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret;
        }
        // 判断活动适用终端
        msg = couponCheckUtil.checkActivityPlatform(activeId, platform);
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret;
        }

        /*
         * // 获取sign缓存
         * String value = RedisUtils.get(sign);
         * SignValue signValue = JSON.parseObject(value, SignValue.class);
         * String token = signValue.getToken();
         * if (token == null) {
         * ret.put("error", 0);
         * ret.put("msg", "用户未登录");
         * LogUtil.endLog(CouponDefine.class.toString(),
         * CouponDefine.EVALUATE_ACTIVE_CHECK);
         * return ret;
         * }
         */

        // 判断用户是否登录
        Integer userId = WebUtils.getUserId(request);
        // Integer userId = 4;
        if (userId == null || userId <= 0) {
            ret.put("error", 0);
            ret.put("msg", "用户未登录");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret;
        }

        // 判断用户是否已经领取
        msg = couponCheckUtil.checkActivityIfInvolvement(activeId, userId + "");
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", "您已经领过体验金券啦，只能领一次哦~您可以参加【测评送加息券】活动，最高加息1.5%呢~~~");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret;
        }

        // 判断用户是否老用户
        msg = couponCheckUtil.checkOldUser(activeId, userId + "");
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", "做为一个诚实的老用户，体验金就不能领啦 您可以参加【测评送加息券】活动，最高加息1.5%呢~~~");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
            return ret;
        }

        ret.put("error", 1);
        ret.put("msg", "您已参加过此活动");
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.REGISTER_ACTIVE_CHECK);
        return ret;
    }

    /**
     * 
     * 评测送加息券活动验证
     * 
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
        String platform = CustomConstants.CLIENT_PC;
        String activeId = request.getParameter("activeId");
        String msg = "";
        // 活动有效性验证
        msg = couponCheckUtil.checkActivityIfAvailable(activeId);
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret;
        }
        // 判断活动适用终端
        msg = couponCheckUtil.checkActivityPlatform(activeId, platform);
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", msg);
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret;
        }
        /*
         * // 获取sign缓存
         * String value = RedisUtils.get(sign);
         * SignValue signValue = JSON.parseObject(value, SignValue.class);
         * String token = signValue.getToken();
         * if (token == null) {
         * ret.put("error", 0);
         * ret.put("msg", "用户未登录");
         * LogUtil.endLog(CouponDefine.class.toString(),
         * CouponDefine.EVALUATE_ACTIVE_CHECK);
         * return ret;
         * }
         */

        // 判断用户是否登录
        // Integer userId = SecretUtil.getUserId(sign);
        Integer userId = WebUtils.getUserId(request);
        // Integer userId = 4;
        if (userId == null || userId <= 0) {
            ret.put("error", 0);
            ret.put("msg", "用户未登录");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret;
        }

        // 判断用户是否已经领取
        msg = couponCheckUtil.checkActivityIfInvolvement(activeId, userId + "");
        if (msg != null && msg.length() != 0) {
            ret.put("error", 1);
            ret.put("msg", "您已领过加息券啦，可在优惠券列表中查看");
            LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
            return ret;
        }

        ret.put("error", 3);
        LogUtil.endLog(CouponDefine.class.toString(), CouponDefine.EVALUATE_ACTIVE_CHECK);
        return ret;
    }

    public static void main(String[] args) {
        BigDecimal earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(1000), new BigDecimal(14), 6)
                .divide(new BigDecimal("100"));

        System.out.println(earnings.toString());
    }

}
