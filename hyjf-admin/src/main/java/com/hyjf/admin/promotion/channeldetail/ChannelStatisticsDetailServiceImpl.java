package com.hyjf.admin.promotion.channeldetail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.ChannelStatisticsDetailCustomize;

@Service
public class ChannelStatisticsDetailServiceImpl extends BaseServiceImpl implements ChannelStatisticsDetailService {

	/**
	 * 获取列表数
	 * 
	 * @param channelStatisticsDetailCustomize
	 * @return
	 * @author Michael
	 */

	@Override
	public Integer countList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize) {
		return channelStatisticsDetailCustomizeMapper.countList(channelStatisticsDetailCustomize);

	}

	/**
	 * 获取列表
	 * 
	 * @param channelStatisticsDetailCustomize
	 * @return
	 * @author Michael
	 */

	@Override
	public List<ChannelStatisticsDetailCustomize> getRecordList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize) {
		return channelStatisticsDetailCustomizeMapper.selectList(channelStatisticsDetailCustomize);

	}

	/**
	 * 导出报表
	 * 
	 * @param channelStatisticsDetailCustomize
	 * @return
	 * @author Michael
	 */

	@Override
	public List<ChannelStatisticsDetailCustomize> exportList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize) {
		return channelStatisticsDetailCustomizeMapper.exportList(channelStatisticsDetailCustomize);

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @return
	 * @author Michael
	 */

	@Override
	public List<UtmPlat> getPCUtm() {
		UtmPlatExample utmPlat = new UtmPlatExample();
		UtmPlatExample.Criteria cra = utmPlat.createCriteria();
		cra.andSourceTypeEqualTo(0);// app渠道类别
		List<UtmPlat> list = utmPlatMapper.selectByExample(utmPlat);
		if (list == null) {
			list = new ArrayList<UtmPlat>();
		}
		return list;
	}

}
