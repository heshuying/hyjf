/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.bank.wechat.hjh.invest;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.web.BaseServiceImpl;

import redis.clients.jedis.JedisPool;

@Service("wxhjhuserInvestService")
public class InvestServiceImpl extends BaseServiceImpl implements InvestService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	public static JedisPool pool = RedisUtils.getConnection();

	@Override
    public UserCouponConfigCustomize getBestCouponById(String couponId) {
        UserCouponConfigCustomize couponConfig = couponConfigCustomizeMapper.getBestCouponById(couponId);
        return couponConfig;
    }

	/**
	 * 取得用户优惠券信息
	 * 
	 * @param couponGrantId
	 * @return
	 */
	public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId) {
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("couponGrantId", couponGrantId);
	    paramMap.put("userId", userId);
	    CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
	    return ccTemp;
	}
	
}



class ComparatorCouponBean implements Comparator<UserCouponConfigCustomize> {

	@Override
	public int compare(UserCouponConfigCustomize couponBean1, UserCouponConfigCustomize couponBean2) {
		if (1 == couponBean1.getCouponType()) {
			couponBean1.setCouponType(4);
		}
		if (1 == couponBean2.getCouponType()) {
			couponBean2.setCouponType(4);
		}
		int flag = couponBean1.getCouponType() - couponBean2.getCouponType();
		if (4 == couponBean1.getCouponType()) {
			couponBean1.setCouponType(1);
		}
		if (4 == couponBean2.getCouponType()) {
			couponBean2.setCouponType(1);
		}
		return flag;
	}

}