package com.hyjf.batch.hjh.borrow.ontimesend;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;

public interface HjhOntimeSendService extends BaseService {

	/**
	 * 查询符合条件的定时投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<Borrow> selectHjhOntimeSendBorrowList();

	/**
	 * 更新标的状态为预约状态
	 * 
	 * @param borrowId
	 * @return
	 */
	public boolean updateHjhOntimeSendBorrow(int borrowId);

	/**
	 * 发送MQ
	 * 
	 * @param borrow
	 * @param routingKey
	 */
	public void sendToMQ(Borrow borrow,String routingKey);
	
	/**
	 * 更新资产表
	 * 
	 * @param borrowNid
	 * @return
	 */
	public boolean updatePlanAsset(String borrowNid);
}
