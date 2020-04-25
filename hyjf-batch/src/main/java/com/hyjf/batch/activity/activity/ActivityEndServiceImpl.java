package com.hyjf.batch.activity.activity;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsExample;

/**
 * 进行中活动结束状态变更定时Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class ActivityEndServiceImpl extends BaseServiceImpl implements ActivityEndService {

	/**
	 * 检索进行中活动列表
	 */
	@Override
	public List<Ads> selectActivityList() {
		AdsExample example = new AdsExample();
		AdsExample.Criteria cra = example.createCriteria();
		// 是否结束状态:0:进行中,1:已结束
		cra.andIsEndEqualTo(0);
		// 状态:0:启用
		cra.andStatusEqualTo((short) 1);
		cra.andTypeidEqualTo(9);
		return this.adsMapper.selectByExample(example);
	}

	/**
	 * 更新活动是否结束状态
	 */
	@Override
	public boolean updateActivityEndStatus(Ads ads) {
		boolean isUpdateFlag = this.adsMapper.updateByPrimaryKey(ads) > 0 ? true : false;
		return isUpdateFlag;
	}

}
