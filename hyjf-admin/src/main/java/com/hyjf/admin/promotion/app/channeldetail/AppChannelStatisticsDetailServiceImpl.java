package com.hyjf.admin.promotion.app.channeldetail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsDetailCustomize;

@Service
public class AppChannelStatisticsDetailServiceImpl extends BaseServiceImpl implements AppChannelStatisticsDetailService {


	/**
	 * 获取列表数
	 * @param appChannelStatisticsDetailCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize) {
		return appChannelStatisticsDetailCustomizeMapper.countList(appChannelStatisticsDetailCustomize);
			
	}

	/**
	 * 获取列表
	 * @param appChannelStatisticsDetailCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AppChannelStatisticsDetailCustomize> getRecordList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize) {
		return appChannelStatisticsDetailCustomizeMapper.selectList(appChannelStatisticsDetailCustomize);
			
	}

	/**
	 * 导出报表
	 * @param appChannelStatisticsDetailCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AppChannelStatisticsDetailCustomize> exportList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize) {
		return appChannelStatisticsDetailCustomizeMapper.exportList(appChannelStatisticsDetailCustomize);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<UtmPlat> getAppUtm() {
		UtmPlatExample utmPlat = new UtmPlatExample();
		UtmPlatExample.Criteria cra = utmPlat.createCriteria();
		cra.andSourceTypeEqualTo(1);//app渠道类别
		List<UtmPlat> list = utmPlatMapper.selectByExample(utmPlat);
		if(list == null){
			list = new ArrayList<UtmPlat>();
		}
		return list;
	}
}
