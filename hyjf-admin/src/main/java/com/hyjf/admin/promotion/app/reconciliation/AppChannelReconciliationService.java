package com.hyjf.admin.promotion.app.reconciliation;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AppChannelReconciliationCustomize;

import java.util.List;

public interface AppChannelReconciliationService extends BaseService{

	/**
	 * count列表
	 * 
	 * @return
	 */
	public Integer countAppChannelReconciliationRecord(AppChannelReconciliationCustomize appChannelReconciliationCustomize);


	/**
	 * count列表_汇计划
	 *
	 * @return
	 */
	public Integer countAppChannelReconciliationRecordHjh(AppChannelReconciliationCustomize appChannelReconciliationCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AppChannelReconciliationCustomize> selectAppChannelReconciliationRecord(AppChannelReconciliationCustomize appChannelReconciliationCustomize);


	/**
	 * 获取列表_汇计划
	 *
	 * @return
	 */
	public List<AppChannelReconciliationCustomize> selectAppChannelReconciliationRecordHjh(AppChannelReconciliationCustomize appChannelReconciliationCustomize);

	/**
	 * 获取app渠道
	 */
	public List<UtmPlat> utmPlatListAppGet(UtmPlat utmPlat);

}
