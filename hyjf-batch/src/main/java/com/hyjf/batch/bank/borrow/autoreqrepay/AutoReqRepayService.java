package com.hyjf.batch.bank.borrow.autoreqrepay;

import java.text.ParseException;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.AutoReqRepayBorrowCustomize;

public interface AutoReqRepayService extends BaseService {
	/**
	 * 取得本日应还款标的列表
	 * @return
	 */
	public List<AutoReqRepayBorrowCustomize> getAutoReqRepayBorrow();
	
	/**
	 * 取得本日应还款标的列表
	 * @return
	 * @throws Exception 
	 */
	public boolean repayUserBorrowProject(AutoReqRepayBorrowCustomize autoReqRepayBorrow)  throws Exception;
}
