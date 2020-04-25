package com.hyjf.admin.exception.tenderback;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.TenderBackHistory;

public interface TenderBackService extends BaseService {

	/**
	 * 明细列表
	 * 
	 * @param record
	 * @return
	 */
	public List<TenderBackHistory> selectTenderBackHistoryList(TenderBackBean record);

	/**
	 * 明细记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 */
	public Integer countTenderBackHistory(TenderBackBean record);

}
