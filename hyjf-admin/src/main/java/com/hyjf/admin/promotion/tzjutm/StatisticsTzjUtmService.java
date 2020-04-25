package com.hyjf.admin.promotion.tzjutm;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.StatisticsTzjUtmCustomize;

public interface StatisticsTzjUtmService extends BaseService{

	/**
	 * count列表
	 * 
	 * @return
	 */
	public Integer countSumList(StatisticsTzjUtmCustomize statisticsTzjUtmCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<StatisticsTzjUtmCustomize> getSumRecordList(StatisticsTzjUtmCustomize statisticsTzjUtmCustomize);

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<StatisticsTzjUtmCustomize> exportList(StatisticsTzjUtmCustomize statisticsTzjUtmCustomize);
}
