/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.app.activity.activity2018qixi;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.ActivityQixiMapper;
import com.hyjf.mybatis.model.auto.ActivityQixi;
import com.hyjf.mybatis.model.auto.ActivityQixiExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author fq
 * @version Activity2018QixiServiceImpl, v0.1 2018/7/23 16:20
 */
@Service
public class Activity2018QixiServiceImpl extends BaseServiceImpl implements Activity2018QixiService {
	@Autowired
	private ActivityQixiMapper activityQixiMapper;

	@Override
	public void updateClickCount(Integer userId) {
		ActivityQixiExample example = new ActivityQixiExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<ActivityQixi> list = activityQixiMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			ActivityQixi activity = list.get(0);
			Integer count = activity.getCount();
			count += count;
			activity.setCount(count);
			activity.setUpdateTime(new Date());
			activityQixiMapper.updateByPrimaryKeySelective(activity);
		} else {
			ActivityQixi activity = new ActivityQixi();
			activity.setUserId(userId);
			activity.setCount(1);
			activityQixiMapper.insertSelective(activity);
		}
	}

	@Override
	public BigDecimal selectInvestSum(Integer userId, int startTime, int endTime) {
		return borrowCustomizeMapper.selectInvestSum(userId, startTime, endTime);
	}

	@Override
	public Integer selectAwartType(Integer userId) {
		ActivityQixiExample example = new ActivityQixiExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<ActivityQixi> list = activityQixiMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			ActivityQixi activityQixi = list.get(0);
			String rewardName = activityQixi.getRewardName();
			// 不同奖品不同类型
			Integer type;
			if (rewardName != null) {
				switch (rewardName) {
					case "携程8000元礼品卡(8张1000元电子卡)":
						type = 1;
						return type;
					case "3290元戴森(Dyson) 吹风机 Dyson Supersonic 电吹风 进口家用 HD01":
						type = 2;
						return type;
					case "698元上海迪士尼双人门票":
						type = 3;
						return type;
					case "300元京东E卡电子卡":
						type = 4;
						return type;
					case "69元美菱家用加湿机":
						type = 5;
						return type;
					case "19.8元爱奇艺会员卡·月卡":
						type = 6;
						return type;
					case "1.8%加息券":
						type = 7;
						return type;
					case "1.7%加息券":
						type = 8;
						return type;
					case "1.6%加息券":
						type = 9;
						return type;
					case "1.5%加息券":
						type = 10;
						return type;
					default:
						type = 0;
						return type;
				}
			}

		}
		return null;
	}
}
