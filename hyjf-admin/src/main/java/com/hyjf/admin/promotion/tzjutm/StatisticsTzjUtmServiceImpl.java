package com.hyjf.admin.promotion.tzjutm;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.StatisticsTzjUtmCustomize;

@Service
public class StatisticsTzjUtmServiceImpl extends BaseServiceImpl implements StatisticsTzjUtmService {


	/**
	 * 获取列表数
	 * @param StatisticsTzjUtmCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countSumList(StatisticsTzjUtmCustomize statisticsTzjUtmCustomize) {
		return statisticsTzjUtmCustomizeMapper.countSumList(statisticsTzjUtmCustomize);
			
	}

	/**
	 * 获取列表
	 * @param StatisticsTzjUtmCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<StatisticsTzjUtmCustomize> getSumRecordList(
			StatisticsTzjUtmCustomize statisticsTzjUtmCustomize) {
		return statisticsTzjUtmCustomizeMapper.selectSumList(statisticsTzjUtmCustomize);
			
	}

	/**
	 * 导出报表
	 * @param StatisticsTzjUtmCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<StatisticsTzjUtmCustomize> exportList(StatisticsTzjUtmCustomize statisticsTzjUtmCustomize) {
		return statisticsTzjUtmCustomizeMapper.exportList(statisticsTzjUtmCustomize);
			
	}
}
