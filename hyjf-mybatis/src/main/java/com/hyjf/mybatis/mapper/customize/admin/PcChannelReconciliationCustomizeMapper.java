/**
 * PC渠道对账
 */
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.PcChannelReconciliationCustomize;

public interface PcChannelReconciliationCustomizeMapper {

	/**
	 * 获取PC渠道对账列表
	 * @param pcChannelReconciliationCustomize
	 * @return
	 */
	List<PcChannelReconciliationCustomize> selectPcChannelReconciliationRecord(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);

	List<PcChannelReconciliationCustomize> selectPcChannelReconciliationRecordHjh(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);

	/**
	 * 获得列表数
	 * 
	 * @param pcChannelReconciliationCustomize
	 * @return
	 */
	Integer countPcChannelReconciliationRecord(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);
	
	Integer countPcChannelReconciliationRecordHjh(PcChannelReconciliationCustomize pcChannelReconciliationCustomize);

}
