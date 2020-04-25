package com.hyjf.api.web.coupon.loans;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.loans.CouponLoansBean;
import com.hyjf.coupon.loans.CouponLoansDefine;
import com.hyjf.coupon.loans.CouponLoansService;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;

/**
 * 优惠券标的放款
 * @author zhangjinpeng
 *
 */
@Controller
@RequestMapping(CouponLoansDefine.REQUEST_MAPPING)
public class CouponLoansServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(CouponLoansServer.class);
	private static final String THIS_CLASS = CouponLoansServer.class.getName();
	
	@Autowired
	CouponLoansService couponLoansService;
	/**
	 * 优惠券标的放款（已废弃，转移到mq）
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CouponLoansDefine.BORROW_LOANS_FOR_COUPON_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseResultBean borrowLoansForCoupon(HttpServletRequest request,CouponLoansBean paramBean){
		_log.info(THIS_CLASS+"---优惠券放款开始。。。"+"标的编号："+paramBean.getBorrowNid());
		BaseResultBean resultBean = new BaseResultBean();
		//验签
        if(!this.checkSign(paramBean, BaseDefine.METHOD_COUPON_LOANS)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
		List<BorrowTenderCpn> listTenderCpn = couponLoansService.getBorrowTenderCpnList(paramBean.getBorrowNid());
        
        /** 循环优惠券出借详情列表 */
        for (BorrowTenderCpn borrowTenderCpn : listTenderCpn) {
            try {
                if (Validator.isNull(borrowTenderCpn.getLoanOrdid())) {
                    // 设置放款订单号
                    borrowTenderCpn.setLoanOrdid(GetOrderIdUtils.getOrderId2(borrowTenderCpn
                            .getUserId()));
                    // 设置放款时间
                    borrowTenderCpn.setOrderDate(GetOrderIdUtils.getOrderDate());
                    // 更新放款信息
                    couponLoansService.updateBorrowTenderCpn(borrowTenderCpn);
                }
                List<Map<String, String>> msgList =
                		couponLoansService.updateCouponRecover(borrowTenderCpn);
                if (msgList != null && msgList.size() > 0) {
                    // 发送短信
                	couponLoansService.sendSmsCoupon(msgList);
                    // 发送push消息
                    System.out.println("--------------准备调用sendAppMSCoupon方法推送push消息--------");
                    couponLoansService.sendAppMSCoupon(msgList);
                    System.out.println("--------------调用sendAppMSCoupon方法发送push消息结束");
                }
            } catch (Exception e) {
            	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
    			resultBean.setStatusDesc("优惠券放款异常");
    			_log.info(THIS_CLASS+"---优惠券放款异常。。。"+"标的编号："+paramBean.getBorrowNid());
            }
        }
		
		_log.info(THIS_CLASS+"---优惠券放款结束。。。"+"标的编号："+paramBean.getBorrowNid());
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		resultBean.setStatusDesc("优惠券放款成功");
		return resultBean;
	}
	
	/**
	 * VIP出借积分计算
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CouponLoansDefine.TENDER_VIP_VALUE_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseResultBean tenderVipValue(HttpServletRequest request,CouponLoansBean paramBean){
		_log.info(THIS_CLASS+"---vip积分计算开始。。。"+"出借编号："+paramBean.getNid());
		BaseResultBean resultBean = new BaseResultBean();
		//验签
        if(!this.checkSign(paramBean, BaseDefine.METHOD_VIP_VALUE)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
		couponLoansService.updateVipValue(paramBean);
		_log.info(THIS_CLASS+"---vip积分计算结束。。。"+"出借编号："+paramBean.getNid());
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		resultBean.setStatusDesc("vip积分计算成功");
		return resultBean;
	}
}
