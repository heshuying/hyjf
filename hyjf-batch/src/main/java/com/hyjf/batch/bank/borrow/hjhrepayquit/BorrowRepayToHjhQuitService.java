package com.hyjf.batch.bank.borrow.hjhrepayquit;


import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.HjhAccede;

public interface BorrowRepayToHjhQuitService extends BaseService {

	/**
	 * 查询待退出的计划
	 * @return
	 */
	public List<HjhAccede> selectWaitQuitHjhList();

	
}
