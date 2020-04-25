/**
 * PC渠道对账
 */
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AppChannelReconciliationCustomize;

public interface AppChannelReconciliationCustomizeMapper {

	/**
	 * 获取app渠道对账列表
	 * @param appChannelReconciliationCustomize
	 * @return
	 */
	List<AppChannelReconciliationCustomize> selectAppChannelReconciliationRecord(AppChannelReconciliationCustomize appChannelReconciliationCustomize);


	/**
	 * 获取app渠道对账列表_汇计划
	 * @param appChannelReconciliationCustomize
	 * @return
	 */
	List<AppChannelReconciliationCustomize> selectAppChannelReconciliationRecordHjh(AppChannelReconciliationCustomize appChannelReconciliationCustomize);


	/**
	 * 获得列表数
	 * 
	 * @param appChannelReconciliationCustomize
	 * @return
	 */
	Integer countAppChannelReconciliationRecord(AppChannelReconciliationCustomize appChannelReconciliationCustomize);


	/**
	 * 获得列表数_汇计划
	 *
	 * @param appChannelReconciliationCustomize
	 * @return
	 */
	Integer countAppChannelReconciliationRecordHjh(AppChannelReconciliationCustomize appChannelReconciliationCustomize);

}
