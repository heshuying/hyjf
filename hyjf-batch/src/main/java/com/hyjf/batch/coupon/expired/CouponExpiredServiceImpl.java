package com.hyjf.batch.coupon.expired;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;

/**
 * 优惠券过期未使用
 *
 * @author Administrator
 *
 */
@Service
public class CouponExpiredServiceImpl extends BaseServiceImpl implements CouponExpiredService {

    private static final String THIS_CLASS = CouponExpiredServiceImpl.class.getName();

    /**
     * 优惠券过期更新
     */
	@Override
	public void updateCouponExpired() {
		String methodName = "updateCouponExpired";
		LogUtil.startLog(THIS_CLASS, methodName, "优惠券过期未使用更新 开始。");
		int nowTime = GetDate.getNowTime10();
		//yyyy-MM-dd 的时间戳
		int nowDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
		System.out.println("now time:" + nowDate);
		// 取得体验金出借（无真实出借）的还款列表
		CouponUserExample example = new CouponUserExample();
		CouponUserExample.Criteria criteria = example.createCriteria();
		criteria.andDelFlagEqualTo(0);
		// 未使用
		criteria.andUsedFlagEqualTo(0);
		// 截止日小于当前时间
		criteria.andEndTimeLessThan(nowDate);
		CouponUser couponUser = new CouponUser();
		// 已失效
		couponUser.setUsedFlag(4);
		couponUser.setUpdateTime(nowTime);
		couponUser.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		int count = this.couponUserMapper.updateByExampleSelective(couponUser, example);
		if(count>0){
			LogUtil.infoLog(THIS_CLASS, methodName, count+"张优惠券过期，状态更新为已失效！");
		}
		LogUtil.endLog(THIS_CLASS, methodName, "优惠券过期未使用更新 结束。");
	}

    
}
