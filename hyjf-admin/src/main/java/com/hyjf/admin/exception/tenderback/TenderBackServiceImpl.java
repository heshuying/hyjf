package com.hyjf.admin.exception.tenderback;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.TenderBackHistory;
import com.hyjf.mybatis.model.auto.TenderBackHistoryExample;

@Service
public class TenderBackServiceImpl extends BaseServiceImpl implements TenderBackService {
	/**
	 * 明细列表
	 * 
	 * @param record
	 * @return
	 */
	public List<TenderBackHistory> selectTenderBackHistoryList(TenderBackBean record) {
		TenderBackHistoryExample example = this.createExample(record);
		return this.tenderBackHistoryMapper.selectByExample(example);

	}

	/**
	 * 明细记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 */
	public Integer countTenderBackHistory(TenderBackBean record) {
		TenderBackHistoryExample example = this.createExample(record);
		example.setLimitStart(record.getLimitStart());
		example.setLimitEnd(record.getLimitEnd());
		return this.tenderBackHistoryMapper.countByExample(example);

	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	private TenderBackHistoryExample createExample(TenderBackBean record) {
		TenderBackHistoryExample example = new TenderBackHistoryExample();
		TenderBackHistoryExample.Criteria cra = example.createCriteria();

		if (StringUtils.isNotEmpty(record.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(record.getBorrowNidSrch());
		}

		if (StringUtils.isNotEmpty(record.getBorrowNameSrch())) {
			cra.andBorrowNameLike("%" + record.getBorrowNameSrch() + "%");
		}

		if (StringUtils.isNotEmpty(record.getTimeStartSrch())) {
			Date date = GetDate.stringToDate(GetDate.getDayStart(record.getTimeStartSrch()));
			Integer time = Integer.valueOf(String.valueOf(date.getTime() / 1000));
			cra.andCreateTimeGreaterThanOrEqualTo(time);
		}

		if (StringUtils.isNotEmpty(record.getTimeEndSrch())) {
			Date date = GetDate.stringToDate(GetDate.getDayEnd(record.getTimeEndSrch()));
			Integer time = Integer.valueOf(String.valueOf(date.getTime() / 1000));
			cra.andCreateTimeLessThanOrEqualTo(time);
		}

		example.setLimitStart(record.getLimitStart());
		example.setLimitEnd(record.getLimitEnd());

		example.setOrderByClause(" create_time DESC ");

		return example;
	}
}
