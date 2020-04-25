package com.hyjf.web.activity.activitylist;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsExample;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.mybatis.model.auto.AdsTypeExample;
import com.hyjf.web.BaseServiceImpl;

/**
 * 
 * 此处为类说明
 * 
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月20日
 * @see 上午9:15:11
 */
@Service
public class ActivityListServiceImpl extends BaseServiceImpl implements ActivityListService {

	/**
	 * 
	 * 获取活动list
	 * 
	 * @param bean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<Ads> getRecordList(Integer typeId, int limitStart, int limitEnd) {
		AdsExample example = new AdsExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause("`is_end` asc,`order` desc,`create_time` desc ");
		com.hyjf.mybatis.model.auto.AdsExample.Criteria criteria = example.createCriteria();
		criteria.andTypeidEqualTo(typeId);
		criteria.andStatusEqualTo((short) 1);// 启用状态
		criteria.andStartTimeLessThanOrEqualTo(GetDate.getDataString(GetDate.datetimeFormat));// 活动开始时间要小于当前时间
		return adsMapper.selectByExample(example);
	}

	/**
	 * 获取活动件数
	 * 
	 * @return
	 */
	@Override
	public int getRecordListCountByTypeid(Integer typeId) {
		AdsExample example = new AdsExample();
		com.hyjf.mybatis.model.auto.AdsExample.Criteria criteria = example.createCriteria();
		criteria.andTypeidEqualTo(typeId); // 类型
		criteria.andStatusEqualTo((short) 1);// 启用状态
		criteria.andStartTimeLessThanOrEqualTo(GetDate.getDataString(GetDate.datetimeFormat));// 活动开始时间要小于当前时间
		return adsMapper.countByExample(example);
	}

	/**
	 * 获取广告类型
	 * 
	 * @param code
	 * @return
	 * @author Michael
	 */

	@Override
	public AdsType getAdsTypeByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		AdsTypeExample example = new AdsTypeExample();
		AdsTypeExample.Criteria cra = example.createCriteria();
		cra.andCodeEqualTo(code);
		List<AdsType> typeList = this.adsTypeMapper.selectByExample(example);
		if (typeList != null && typeList.size() > 0) {
			return typeList.get(0);
		}
		return null;
	}

}
