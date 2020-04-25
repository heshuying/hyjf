package com.hyjf.api.server.borrow.repayment;


import java.util.List;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomizeRe;

public interface BorrowRepaymentInfoService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<ApiBorrowRepaymentInfoCustomizeRe> selectBorrowRepaymentInfoList(
	        ApiBorrowRepaymentInfoCustomize borrowRepaymentInfoBean);

}
