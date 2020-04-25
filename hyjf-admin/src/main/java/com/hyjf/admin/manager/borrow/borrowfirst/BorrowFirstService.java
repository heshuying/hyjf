package com.hyjf.admin.manager.borrow.borrowfirst;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFirstCustomize;

public interface BorrowFirstService extends BaseService {

	/**
	 * 数据合计
	 * 
	 * @return
	 */
	public Integer countBorrowFirst(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<BorrowFirstCustomize> selectBorrowFirstList(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 交保证金
	 * 
	 * @param record
	 * @return 
	 */
	public boolean saveBailRecord(String borrowPreNid);

	/**
	 * 已交保证金
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */
	public boolean getBorrowBail(String borrowNid);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateOntimeRecord(BorrowFirstBean borrowBean,Integer count);

	/**
	 * 获取用户名
	 * 
	 * @param record
	 */
	public String getUserName(Integer userId);

	/**
	 * 统计总额
	 * 
	 * @param corrowCommonCustomize
	 * @return
	 */
	public String sumBorrowFirstAccount(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 根据订单号,查询其是否有预约记录
	 * 
	 * @param corrowCommonCustomize
	 * @return
	 */
	public Boolean hasBookingRecord(String nid);

    /**
     * 发送初审队列消息
     * 
     * @param borrowNid
     * @param routingKey
     */
    void sendToMQ(String borrowNid,String routingKey);
}
