package com.hyjf.batch.datarecover.couponrepay;

import java.util.List;

import com.hyjf.batch.BaseService;

public interface RepayDataRecoverService extends BaseService {
	List<String> selectNidForTyj();
}
