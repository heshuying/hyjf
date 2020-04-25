package com.hyjf.batch.prize.reset;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityList;

/**
 * 奖品重置
 * 
 * @author zhangjinpeng
 * 
 */
@Service
public class PrizeResetServiceImpl extends BaseServiceImpl implements PrizeResetService {

	private static final String THIS_CLASS = PrizeResetServiceImpl.class.getName();
	Logger _log = LoggerFactory.getLogger(PrizeResetServiceImpl.class);

	/**
	 * 奖品重置
	 * 
	 */
	@Override
	public void updatePrizeReset() {
		String methodName = "updatePrizeReset";
		try {
			Properties properties = PropUtils.getSystemResourcesProperties();
			String activityId = properties.getProperty(CustomConstants.MGM10_ACTIVITY_ID).trim();
			ActivityList activity = this.activityListMapper.selectByPrimaryKey(Integer.valueOf(activityId));
			Integer timeStart = activity.getTimeStart();
			Integer timeEnd = activity.getTimeEnd();
			Integer now = GetDate.getNowTime10();
			// 判断当前活动有效期
			if (now >= timeStart && now <= timeEnd) {
				// 在有效期内，则重置奖品数量
				this.prizeResetCustomizeMapper.updatePrizeReset();
			}
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "奖品数量重置成功！");
		} catch (Exception e) {
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "奖品数量重置失败！");
		}
	}
}
