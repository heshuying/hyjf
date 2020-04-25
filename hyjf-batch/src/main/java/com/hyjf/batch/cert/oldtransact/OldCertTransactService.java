package com.hyjf.batch.cert.oldtransact;

import com.hyjf.batch.BaseService;

/**
 * 应急中心交易流水上报
 * @Author pcc
 * @Date 2018/12/03 14:10
 */
public interface OldCertTransactService extends BaseService {

	void certTransact();

	void certTransactRepaySuccess();
}
