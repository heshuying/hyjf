package com.hyjf.batch.user.appoint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;

/**
 * 平台数据
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class RecordTruncateServiceImpl extends BaseServiceImpl implements RecordTruncateService {

	@Override
	public void updateRecordTruncate() {
		this.webCalculateInvestInterestCustomizeMapper.updateRecordTruncate();
	}

}
