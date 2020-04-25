package com.hyjf.admin.finance.couponrepaymonitor;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminCouponRepayMonitorCustomize;

public interface CouponRepayService extends BaseService {

    Integer countRecordTotal(Map<String, Object> paraMap);

    List<AdminCouponRepayMonitorCustomize> selectRecordList(Map<String, Object> paraMap);

    List<AdminCouponRepayMonitorCustomize> selectInterestSum(Map<String, Object> paraMap);
    
    
}
