package com.hyjf.batch.coupon.repay;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 优惠券单独出借还款
 * @author zhangjp
 *
 */
public class CouponRepayTask {
    /** 运行状态 */
    private static int isRun = 0;
    Logger _log = LoggerFactory.getLogger(CouponRepayTask.class);
    @Autowired
    CouponRepayService couponRepayService;
    
    public void run() {
        // 调用还款接口
    	couponRepay();
    }

    /**
     * 调用还款接口
     *
     * @return
     */
    private void couponRepay() {
        _log.info("筛选优惠券单独出借还款开始");
        if (isRun == 0) {
            isRun = 1;
            try {
            	List<String> recoverNidList = couponRepayService.selectNidForCouponOnly();
            	if(recoverNidList != null){
            		_log.info("需按优惠券单独出借还款包括："+JSONArray.toJSONString(recoverNidList));
            		CommonSoaUtils.couponOnlyRepay(recoverNidList);
            	}
            	
            }catch(Exception e){
            	e.printStackTrace();
            }finally {
                isRun = 0;
            }
        }
        _log.info("筛选优惠券单独出借还款结束");
    }
}
