package com.hyjf.batch.coupon.timeout;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.CouponRecoverExample;

/**
 * 自动检查体验金收益过期
 *
 * @author Administrator
 *
 */
@Service
public class CouponTimeoutServiceImpl extends BaseServiceImpl implements CouponTimeoutService {

    private static final String THIS_CLASS = CouponTimeoutServiceImpl.class.getName();

    /**
     * 体验金收益过期未领取的  状态更新为已过期
     */
	@Override
	public void updateCouponTimeout() {
		String methodName = "updateCouponTimeout";
		LogUtil.startLog(THIS_CLASS, methodName, "自动检查收益是否过期,如有过期则更新状态为已过期 开始。");
		int nowTime = GetDate.getNowTime10();
		int timeoutDaysPro = Integer.valueOf(PropUtils.getSystem(CustomConstants.GAINS_TIMEOUT_DAYS));
		// 超过30天的 算作收益过期未领取
		int timeoutDate = (int)(GetDate.countDate(5, -timeoutDaysPro).getTime()/1000);
		CouponRecover cr = new CouponRecover();
		// 状态设为 已过期
		cr.setReceivedFlg(6);
		cr.setUpdateTime(nowTime);
		cr.setExpTime(nowTime);
		cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		CouponRecoverExample example = new CouponRecoverExample();
		// 主单还款时间超过15天
		CouponRecoverExample.Criteria criteria = example.createCriteria();
		// 已还款
		criteria.andRecoverStatusEqualTo(1);
		// 未领取
		criteria.andReceivedFlgEqualTo(2);
		// 大于30天
		criteria.andMainRecoverYestimeLessThan(timeoutDate);
		this.couponRecoverMapper.updateByExampleSelective(cr, example);
		LogUtil.endLog(THIS_CLASS, methodName, "自动检查收益是否过期,如有过期则更新状态为已过期 结束。");
	}

    
}
