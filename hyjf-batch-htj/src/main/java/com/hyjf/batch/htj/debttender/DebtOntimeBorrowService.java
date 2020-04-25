package com.hyjf.batch.htj.debttender;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtBorrow;

public interface DebtOntimeBorrowService extends BaseService {

	/**
	 * 查询符合条件的定时投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<DebtBorrow> queryOntimeTenderList();

	/**
	 * 更新标的状态为预约状态
	 * 
	 * @param borrowId
	 * @return
	 */

	public boolean updateSendBorrow(int borrowId);

}
