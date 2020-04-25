package com.hyjf.admin.exception.repayexception.info;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

public interface RepayExceptionInfoService extends BaseService {

	/**
	 * 出借明细列表
	 *
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<RepayExceptionCustomize> selectBorrowRepaymentList(RepayExceptionCustomize repayExceptionCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 *
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepayment(RepayExceptionCustomize repayExceptionCustomize);

    /**
     * 出借明细记录 总数SUM
     *
     * @param borrowCustomize
     * @return
     */
    public RepayExceptionCustomize sumBorrowRepaymentInfo(RepayExceptionCustomize repayExceptionCustomize);
}
