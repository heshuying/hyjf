package com.hyjf.admin.finance.couponrepaymonitor;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminCouponRepayMonitorCustomize;

@Service
public class CouponRepayServiceImpl extends BaseServiceImpl implements CouponRepayService {

	@Override
    public List<AdminCouponRepayMonitorCustomize> selectRecordList(Map<String, Object> paraMap) {
		return this.adminCouponRepayMonitorCustomizeMapper.selectRecordList(paraMap);
	}

	@Override
    public Integer countRecordTotal(Map<String, Object> paraMap) {
		return this.adminCouponRepayMonitorCustomizeMapper.countRecordTotal(paraMap);
	}
	
    @Override
    public List<AdminCouponRepayMonitorCustomize> selectInterestSum(Map<String, Object> paraMap) {
        return this.adminCouponRepayMonitorCustomizeMapper.selectInterestSum(paraMap);
    }

}
