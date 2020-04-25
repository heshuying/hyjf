package com.hyjf.batch.coupon.repay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;

/**
 * 优惠券单独出借还款
 * @author zhangjp
 *
 */
@Service
public class CouponRepayServiceImpl extends BaseServiceImpl implements CouponRepayService {
	Logger _log = LoggerFactory.getLogger(CouponRepayServiceImpl.class);

	@Override
	public List<String> selectNidForCouponOnly() {
		_log.info("筛选是否有优惠券单独出借需还款");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<String> recoverNidList = this.batchTyjRepayCustomizeMapper.selectNidForTyj(paramMap);
		if(recoverNidList!=null &&recoverNidList.size()>0){
			_log.info("有"+recoverNidList.size()+"条按优惠券单独出借需还款");
			return recoverNidList;
		}
		_log.info("没有优惠券单独出借需还款");
		return null;
	}

	
}
