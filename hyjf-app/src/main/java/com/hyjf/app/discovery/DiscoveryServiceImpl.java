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

package com.hyjf.app.discovery;

import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;

@Service
public class DiscoveryServiceImpl extends BaseServiceImpl implements
		DiscoveryService {

	/**
	 * 根据用户编号 取得用户评估结果
	 */
	@Override
	public String getEvalationResultByUserId(Integer userId) {
		UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
		example.createCriteria().andUserIdEqualTo(userId);
		List<UserEvalationResultCustomize> resultList = this.userEvalationResultCustomizeMapper
				.selectByExample(example);
		if (resultList != null && resultList.size() == 1) {
			return resultList.get(0).getType();
		}
		return "点击测评";
	}

	/**
	 * 判断测评送券的活动是否进行中
	 */
	@Override
	public String getEvalationMessage() {
		Properties properties = PropUtils.getSystemResourcesProperties();
		int activityId = Integer.valueOf(properties.getProperty(
				"hyjf.activity.ping.id").trim());
		ActivityList activity = this.activityListMapper
				.selectByPrimaryKey(activityId);
		if (activity != null && activity.getTimeEnd() >= GetDate.getNowTime10()
				&& activity.getTimeStart() <= GetDate.getNowTime10()) {
			return "测评领券";
		}
		return "点击测评";
	}

}
