package com.hyjf.api.web.coupon.myaccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.myaccount.CouponInfoResultBean;
import com.hyjf.coupon.myaccount.MyAccountBean;
import com.hyjf.coupon.myaccount.MyAccountDefine;
import com.hyjf.coupon.myaccount.MyAccountService;
import com.hyjf.coupon.myaccount.VipInfoResultBean;

@Controller
@RequestMapping(value = MyAccountDefine.REQUEST_MAPPING)
public class MyAccountServer extends BaseController {
    @Autowired
    MyAccountService myAccountService;
    
    /**
     * 
     * 获取用户VIP信息
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = MyAccountDefine.GET_VIP_INFO)
    public VipInfoResultBean getUserVIPInfo(@ModelAttribute MyAccountBean form, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), MyAccountDefine.GET_VIP_INFO);
    	VipInfoResultBean result = new VipInfoResultBean();
    	
    	//验证请求参数
        if (Validator.isNull(form.getUserId())) {
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_MYACCOUNT_VIPINFO)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        
    	result = myAccountService.getVipInfo(form.getUserId());
        
        result.setStatus(BaseResultBean.STATUS_SUCCESS);
        result.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), MyAccountDefine.GET_VIP_INFO);
        return result;
    }
    
    
    /**
     * 
     * 获取我的账户用户优惠券描述信息
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = MyAccountDefine.GET_COUPON_INFO)
    public CouponInfoResultBean getCouponInfo(@ModelAttribute MyAccountBean form, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), MyAccountDefine.GET_COUPON_INFO);
        CouponInfoResultBean result = new CouponInfoResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_MYACCOUNT_COUPONINFO)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        
        result = myAccountService.getCouponInfo(form.getUserId());
        
        result.setStatus(BaseResultBean.STATUS_SUCCESS);
        result.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), MyAccountDefine.GET_COUPON_INFO);
        return result;
    }


}
