package com.hyjf.api.web.coupon.repay;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.repay.CouponRepayBean;
import com.hyjf.coupon.repay.CouponRepayDefine;
import com.hyjf.coupon.repay.CouponRepayService;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

/**
 * 优惠券还款
 * @author zhangjp
 *
 */
@Controller
@RequestMapping(value = CouponRepayDefine.REQUEST_MAPPING)
public class CouponRepayServer extends BaseController {
    /** 类名 */
    //private static final String THIS_CLASS = CouponRepayServer.class.getName();
    Logger _log = LoggerFactory.getLogger(CouponRepayServer.class);
    @Autowired
    private CouponRepayService repayService;
    
    /**
     * 直投类优惠券还款（已废弃，转移到MQ）
     * @param repayBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponRepayDefine.COUPON_REPAY_ACTION)
    public BaseResultBean borrowRepayForCoupon(@ModelAttribute CouponRepayBean repayBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), CouponRepayDefine.COUPON_REPAY_ACTION);
    	_log.info("直投类优惠券还款开始，项目编号："+repayBean.getBorrowNid());
    	BaseResultBean resultBean = new BaseResultBean();

        //验签
        if(!this.checkSign(repayBean, BaseDefine.METHOD_COUPON_REPAY)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //验证请求参数
        if (Validator.isNull(repayBean.getBorrowNid()) || Validator.isNull(repayBean.getPeriodNow())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        
        List<CouponTenderCustomize> couponTenderList = repayService.getCouponTenderList(repayBean.getBorrowNid());
        
        for(CouponTenderCustomize ct:couponTenderList){
            try{
                repayService.updateCouponRecoverMoney(repayBean.getBorrowNid(),repayBean.getPeriodNow(), ct);
                // 避免给即信端造成请求压力导致还款失败
                Thread.sleep(100);
            }catch(Exception e){
            	// 本次优惠券还款失败
                _log.info("直投类优惠券还款失败，优惠券出借编号："+ct.getOrderId());
                /*resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc(e.getMessage());
                return resultBean;*/
            }
        }
        
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        _log.info("直投类优惠券还款结束，项目编号："+repayBean.getBorrowNid());
        return resultBean;
    }
    
    /**
     * 汇添金优惠券还款（已废弃，转移到MQ）
     * @param repayBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponRepayDefine.COUPON_REPAY_HTJ_ACTION)
    public BaseResultBean couponRepayForHjh(@ModelAttribute CouponRepayBean repayBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), CouponRepayDefine.COUPON_REPAY_HTJ_ACTION);
    	_log.info("汇添金优惠券还款开始，计划编号："+repayBean.getPlanNid());
    	BaseResultBean resultBean = new BaseResultBean();

        //验签
        if(!this.checkSign(repayBean, BaseDefine.METHOD_COUPON_REPAY_HTJ)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //验证请求参数
        if (Validator.isNull(repayBean.getOrderId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        
        List<CouponTenderCustomize> couponTenderList = repayService.getCouponTenderList(repayBean.getOrderId());
        
        for(CouponTenderCustomize ct:couponTenderList){
            try{
                repayService.updateCouponRecoverHjh(repayBean.getPlanNid(), ct);
            }catch(Exception e){
            	// 本次优惠券还款失败
                _log.info("汇添金优惠券还款失败，优惠券出借编号："+ct.getOrderId());
                /*resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc(e.getMessage());
                return resultBean;*/
            }
        }
        
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        _log.info("汇添金优惠券还款结束，计划编号："+repayBean.getPlanNid());
        return resultBean;
    }
    
    /**
     * 体验金还款（按照体验金收益期限还款）和计划类优惠券单独出借还款
     * @param repayBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponRepayDefine.COUPON_REPAY_COUPON_ONLY_ACTION)
    public BaseResultBean borrowRepayForCouponOnly(@ModelAttribute CouponRepayBean repayBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), CouponRepayDefine.COUPON_REPAY_COUPON_ONLY_ACTION);
    	_log.info("优惠券单独出借还款开始，项目编号："+repayBean.getBorrowNid());
    	BaseResultBean resultBean = new BaseResultBean();

        //验签
        if(!this.checkSign(repayBean, BaseDefine.METHOD_COUPON_REPAY_TYJ)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        String nidListStr = repayBean.getNidList();
        List<String> nidList = JSONArray.parseArray(nidListStr, String.class);
        for(String nid:nidList){
            try{
                repayService.updateCouponOnlyRecover(nid);
            }catch(Exception e){
            	// 本次优惠券还款失败
                _log.info("优惠券单独出借还款失败，优惠券出借编号："+nid);
            }
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        _log.info("优惠券单独出借还款结束，项目编号："+repayBean.getBorrowNid());
        return resultBean;
    }
}
