package com.hyjf.batch.borrow.tender;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowAppoint;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface OntimeTenderService extends BaseService {

	/**
	 * 查询符合条件的定时投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<Borrow> queryOntimeTenderList();

	/**
	 * 修改投标信息
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(BorrowWithBLOBs record);

	/**
	 * 修改投标信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(BorrowWithBLOBs record);

	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(String tplname);

	/**
	 * 查询到达预约开始时间标的信息
	 * 
	 * @return
	 */

	public List<Borrow> selectBorrowAppointStart();

	/**
	 * 更新标的预约投标状态
	 * 
	 * @param borrowId
	 * @param status
	 */
	public void updateBorrowAppointStatus(Integer borrowId, int status);

	/**
	 * 查询到达预约结束时间的标的信息
	 * 
	 * @return
	 */

	public List<Borrow> selectBorrowAppointEnd();

	/**
	 * 查询标的预约记录
	 * 
	 * @param borrowNid
	 *            标的号
	 * 
	 * @return
	 */

	public List<BorrowAppoint> selectBorrowAppoint(String borrowNid);

	/**
	 * 预约标的进行出借
	 * 
	 * @param borrowAppoint
	 * @param freezeId 
	 * @return
	 * @throws Exception 
	 */

	public boolean updateBorrowTender(BorrowAppoint borrowAppoint,String orderDate, String freezeId) throws Exception;

	/**
	 * 转让状态更新
	 * 
	 * @param borrowAppoint
	 * @param status
	 * @param message
	 */

	public void updateAppointStatus(BorrowAppoint borrowAppoint, int status, String message);

	/**
	 * 更新标的状态为预约状态
	 * 
	 * @param borrowId
	 * @return
	 */

	public boolean updateSendBorrow(int borrowId);

	/**
	 * 校验是否可以预约
	 * 
	 * @param borrowAppoint
	 * @param info
	 */

	public void checkAppoint(BorrowAppoint borrowAppoint, JSONObject info);

	/**
	 * 调用汇付天下接口前操作
	 * 
	 * @param borrowId
	 *            借款id
	 * @param userId
	 *            用户id
	 * @param account
	 *            出借金额
	 * @return 操作是否成功
	 * @throws Exception 
	 */
	public boolean updateBeforeApoint(BorrowAppoint borrowAppoint) throws Exception;

	/**
	 * 调用预约出借接口
	 * 
	 * @param borrowAppoint
	 * @param orderId 
	 * @return 
	 * @throws UnsupportedEncodingException 
	 */

	public JSONObject appointTender(BorrowAppoint borrowAppoint,String orderDate) throws UnsupportedEncodingException;

	/**
	 * 
	 * 解冻订单
	 * @param freezeId
	 * @return
	 * @throws Exception 
	 */
		
	public ChinapnrBean usrUnFreeze(String freezeId) throws Exception;

	/**
	 * 回滚用户的余额
	 * @param borrowAppoint
	 */
		
	public int updateUserAccount(BorrowAppoint borrowAppoint);

	/**
	 * 更新用户积分信息
	 * @param borrowAppoint
	 * @param record 
	 * @param message 
	 * @return 
	 * @throws Exception 
	 */
	public boolean updateUserRecord(BorrowAppoint borrowAppoint) throws Exception;

	/**
	 * @param borrowUserId
	 * @param investUserId
	 * @param orderId
	 * @param trxId
	 * @param ordDate
	 * @return
	 * @throws Exception
	 */
		
	boolean unFreezeOrder(int investUserId, String orderId, String trxId, String ordDate)
			throws Exception;

	/**
	 * 获取用户的冻结记录表
	 * @param userId
	 * @param borrowNid
	 * @param appointOrderId
	 * @return
	 */
		
	public FreezeList getUserFreeze(int userId, String borrowNid, String appointOrderId);


	/**
	 * 删除用户预约冻结相应的冻结记录
	 * @param borrowAppoint
	 * @param trxId
	 * @return
	 */
		
	public boolean deleteFreezeList(BorrowAppoint borrowAppoint);

}
