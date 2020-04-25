package com.hyjf.api.aems.repayment;


import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomizeRe;

import java.util.List;

public interface AemsBorrowRepaymentInfoService extends BaseService {

	/**
	 * 投资明细列表
	 * 
	 * @param borrowRepaymentInfoBean
	 * @return
	 */
	public List<ApiBorrowRepaymentInfoCustomizeRe> selectBorrowRepaymentInfoList(
            ApiBorrowRepaymentInfoCustomize borrowRepaymentInfoBean);

}
