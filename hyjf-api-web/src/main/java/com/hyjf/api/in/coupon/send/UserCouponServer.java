package com.hyjf.api.in.coupon.send;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.coupon.BatchUserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponDefine;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;

/**
 * 用户出借记录外部用接口
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = UserCouponDefine.USER_COUPON_REQUEST_MAPPING_CLASS)
public class UserCouponServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(UserCouponServer.class);
    private static final String THIS_CLASS = UserCouponServer.class.getName();

    @Autowired
    UserCouponService userCouponService;

	/**
	 * 自动发放用户优惠券(目前PHP专用)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserCouponDefine.USER_COUPON_SEND_REQUEST_ACTION, method = RequestMethod.POST)
	public JSONObject userCouponSend(HttpServletRequest request,UserCouponBean paramBean) {
		LogUtil.startLog(THIS_CLASS,UserCouponDefine.USER_COUPON_SEND_REQUEST_ACTION);
		LogUtil.infoLog(THIS_CLASS, UserCouponDefine.USER_COUPON_SEND_REQUEST_ACTION, JSONObject.toJSONString(paramBean));
		_log.info("用户："+paramBean.getUserId()+",发放优惠券---开始  " + GetDate.dateToString(new Date()));
		JSONObject result = new JSONObject();
		try {
			if(checkSign(paramBean)){
				result = userCouponService.insertUserCoupon(paramBean);
			}else{
				_log.info("用户："+paramBean.getUserId()+",验签失败！");
			}
		} catch (Exception e) {
			_log.error("用户："+paramBean.getUserId()+",发放优惠券---失败", e);
			result.put("status", 1);
		}
		
		LogUtil.endLog(THIS_CLASS,UserCouponDefine.USER_COUPON_SEND_REQUEST_ACTION);
		_log.info("用户："+paramBean.getUserId()+",发放优惠券---结束  " + GetDate.dateToString(new Date()));
		return result;
	}
	
	/**
	 * 上传批量 自动发放用户优惠券
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserCouponDefine.BATCH_USER_COUPON_SEND_REQUEST_ACTION, method = RequestMethod.POST)
	public JSONObject batchUserCouponSend(HttpServletRequest request,BatchUserCouponBean paramBean) {
		LogUtil.startLog(THIS_CLASS,UserCouponDefine.BATCH_USER_COUPON_SEND_REQUEST_ACTION);
		LogUtil.infoLog(THIS_CLASS, UserCouponDefine.BATCH_USER_COUPON_SEND_REQUEST_ACTION, JSONObject.toJSONString(paramBean));
		_log.info("用户："+paramBean.getUserId()+",发放优惠券---开始  " + GetDate.dateToString(new Date()));
		JSONObject result = new JSONObject();
		try {
			if(checkSign(paramBean)){
				result = userCouponService.batchInsertUserCoupon(paramBean);
			}else{
				_log.info("用户："+paramBean.getUserId()+",验签失败！");
			}
		} catch (Exception e) {
			_log.info("用户："+paramBean.getUserId()+",发放优惠券---失败");
			result.put("status", 1);
		}
		
		LogUtil.endLog(THIS_CLASS,UserCouponDefine.BATCH_USER_COUPON_SEND_REQUEST_ACTION);
		_log.info("用户："+paramBean.getUserId()+",发放优惠券---结束  " + GetDate.dateToString(new Date()));
		return result;
	}
	
	public static void main(String[] args) {
        System.out.println(GetDate.dateToString(new Date()));
    }
	

    /**
     * 验证签名
     * 
     * @param paramBean
     * @return
     */
    private boolean checkSign(UserCouponBean paramBean) {

        String userId = paramBean.getUserId();
        Integer sendFlg = paramBean.getSendFlg();
        String accessKey = PropUtils.getSystem(UserCouponDefine.RELEASE_COUPON_ACCESSKEY);
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + sendFlg + accessKey));
        return StringUtils.equals(sign, paramBean.getSign()) ? true : false;

    }
	

    /**
     * 验证签名
     * 
     * @param paramBean
     * @return
     */
    private boolean checkSign(BatchUserCouponBean paramBean) {

        String userId = paramBean.getUserId();
        String timestamp = paramBean.getTimestamp();
        
        String accessKey = PropUtils.getSystem(UserCouponDefine.RELEASE_COUPON_ACCESSKEY);
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey));
        return StringUtils.equals(sign, paramBean.getSign()) ? true : false;

    }

}
