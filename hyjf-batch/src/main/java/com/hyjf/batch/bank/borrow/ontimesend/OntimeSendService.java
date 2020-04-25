package com.hyjf.batch.bank.borrow.ontimesend;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;

public interface OntimeSendService extends BaseService {

	/**
	 * 查询符合条件的定时投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<Borrow> selectOntimeSendBorrowList();

	/**
	 * 更新标的状态为预约状态
	 * 
	 * @param borrowId
	 * @return
	 */
	public boolean updateOntimeSendBorrow(int borrowId);

}
