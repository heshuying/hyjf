package com.hyjf.admin.promotion.reconciliation;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.PcChannelReconciliationCustomize;

public interface ChannelReconciliationService extends BaseService{

	/**
	 * count列表
	 * 
	 * @return
	 */
	public Integer countPcChannelReconciliationRecord(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);

	/**
	 * count汇计划列表数量
	 * @param pcChannelReconciliationCustomize
	 * @return
	 */
	public Integer countPcChannelReconciliationRecordHjh(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<PcChannelReconciliationCustomize> selectPcChannelReconciliationRecord(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);

	/**
	 * 获取汇计划相关列表
	 * @param pcChannelReconciliationCustomize
	 * @return
	 */
	public List<PcChannelReconciliationCustomize> selectPcChannelReconciliationRecordHjh(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);
	/**
	 * 获取pc渠道
	 */
	public List<UtmPlat> utmPlatListPcGet(UtmPlat utmPlat);
}
