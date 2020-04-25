/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.activity.activity2018qixi;

import com.alibaba.fastjson.JSON;
import com.hyjf.bank.service.coupon.BatchSubUserCouponBean;
import com.hyjf.mybatis.mapper.auto.ActivityQixiMapper;
import com.hyjf.mybatis.mapper.customize.ActivityQixiCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.BorrowCustomizeMapper;
import com.hyjf.mybatis.model.auto.ActivityQixi;
import com.hyjf.mybatis.model.auto.ActivityQixiExample;
import com.hyjf.mybatis.model.customize.batch.BatchActivityQixiCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author fq
 * @version Activity2018QixiServiceImpl, v0.1 2018/7/24 11:03
 */
@Service
public class Activity2018QixiServiceImpl implements Activity2018QixiService {
	private static final String startTime = "2018-08-10 00:00:00";
	private static final String endTime = "2018-08-24 23:59:59";
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
	private ActivityQixiCustomizeMapper activityQixiMapper;
	@Autowired
	private BorrowCustomizeMapper customizeMapper;
	@Autowired
	private ActivityQixiMapper mapper;

	@Override
	public void updateSendCoupon() {
		Map<String, Object> map = new HashMap<>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		// 获取符合条件的username集合
		List<BatchActivityQixiCustomize> list = customizeMapper.selectInvestUserName(map);
		List<BatchSubUserCouponBean> subBeans = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			// 更新不是通过活动页面进行出借的用户
			ActivityQixiExample example = new ActivityQixiExample();
			List<ActivityQixi> qixiList = mapper.selectByExample(example);
			List<Integer> userIdList = new ArrayList<>();
			if (!CollectionUtils.isEmpty(qixiList)) {
				for (ActivityQixi qixi : qixiList) {
					userIdList.add(qixi.getUserId());
				}
			}

			for (BatchActivityQixiCustomize customize : list) {
				// 判断每个用户符合的优惠券
				BigDecimal money = customize.getMoney() == null ? BigDecimal.ZERO : customize.getMoney();
				if (money.compareTo(new BigDecimal("5000")) >= 0 && money.compareTo(new BigDecimal("7000")) < 0) {
					setBatchSubUserCouponBean(subBeans, customize.getUsername(), "", Arrays.asList("PJ3916427"));
				} else if (money.compareTo(new BigDecimal("50000")) >= 0
						&& money.compareTo(new BigDecimal("70000")) < 0) {
					setBatchSubUserCouponBean(subBeans, customize.getUsername(), "", Arrays.asList("PJ7694352"));
				} else if (money.compareTo(new BigDecimal("150000")) >= 0
						&& money.compareTo(new BigDecimal("170000")) < 0) {
					setBatchSubUserCouponBean(subBeans, customize.getUsername(), "", Arrays.asList("PJ4286173"));
				} else if (money.compareTo(new BigDecimal("650000")) >= 0
						&& money.compareTo(new BigDecimal("700000")) < 0) {
					setBatchSubUserCouponBean(subBeans, customize.getUsername(), "", Arrays.asList("PJ9183025"));
				}
				// 更新不是通过活动页面进行出借的用户
				int userId = customize.getUserId();
				if (!userIdList.contains(userId)) {
					ActivityQixi activity = new ActivityQixi();
					activity.setUserId(userId);
					activity.setCount(1);
					mapper.insertSelective(activity);
				}
			}
            // 访问API
            Map<String, String> params = new HashMap<>();
            params.put("usercoupons", JSON.toJSONString(subBeans));
            // 请求路径
            try {
                CommonSoaUtils.getBatchCoupons(null, params);
            } catch (Exception e) {
                logger.error("批量发放优惠券失败......", e);
            }
			activityQixiMapper.updateActivityQixi(list);
        }

	}

	/**
	 * 批量发放优惠券基类封装
	 * 
	 * @param subBeans
	 * @param username
	 * @param activityId
	 * @param couponCodeList
	 */
	private void setBatchSubUserCouponBean(List<BatchSubUserCouponBean> subBeans, String username, String activityId,
			List<String> couponCodeList) {
		BatchSubUserCouponBean subBean = new BatchSubUserCouponBean();
		subBean.setUserName(username);
		subBean.setActivityId(activityId);
		subBean.setCouponCode(couponCodeList);
		subBeans.add(subBean);
	}
}
