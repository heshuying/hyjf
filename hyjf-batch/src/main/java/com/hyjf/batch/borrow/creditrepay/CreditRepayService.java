package com.hyjf.batch.borrow.creditrepay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;

public interface CreditRepayService extends BaseService {

	/**
	 * 取得还款明细列表
	 *
	 * @return
	 */
	public List<BorrowRecover> getBorrowRecoverList(String borrowNid);
	
	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId);

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	public BorrowTender getBorrowTender(Integer tenderId);

	/**
	 * 自动放款
	 *
	 * @param apicron
	 * @param borrowCredit 
	 * @param creditTender 
	 * @param creditRepay 
	 * @return
	 */
	public List<Map<String, String>> updateBorrowCreditRepay(BorrowApicron apicron, BorrowRecover borrowRecover,
			AccountChinapnr borrowUserCust, BorrowCredit borrowCredit, CreditTender creditTender, CreditRepay creditRepay) throws Exception;
	
	/**
	 * 取得还款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronList(Integer status, Integer apiType, Integer creditStatus);

	/**
	 * 取得本期应还金额
	 *
	 * @return
	 */
	public BigDecimal getBorrowAccountWithPeriod(String borrowNid, String borrowStyle, Integer period);

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status);

	/**
	 * 更新还款API任务表
	 *
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status, String data);

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecover(BorrowRecover recoder);

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param borrow
	 * @param periodNow
	 */
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId);

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	public BorrowWithBLOBs getBorrow(String borrowNid);

	/**
	 * 发送短信(还款成功)
	 *
	 * @param userId
	 */
	public void sendSms(List<Map<String, String>> msgList);

	public List<BorrowCredit> getBorrowCreditList(String borrowNid, int i, int j);

	public List<BorrowCredit> selectBorrowCreditList(String borrowNid, String tenderOrdId, int i, int j);

	/**
	 * @param borrowNid
	 * @param creditNid
	 * @return
	 */
		
	public List<CreditTender> selectCreditTenderList(String borrowNid, int creditNid);

	/**
	 * @param borrowNid
	 * @param periodNow
	 * @param tenderUserId
	 * @param nid
	 * @return
	 */
		
	public BorrowRecoverPlan getBorrowRecoverPlan(String borrowNid, int periodNow, int tenderUserId, int tenderId);

	/**
	 * @param creditNid
	 * @param i
	 * @param j
	 * @param nowTime
	 * @param k
	 */
		
	public void updateBorrowCredit(int creditNid, int i, int j, int nowTime, int k);

	/**
	 * @param borrowCredit
	 * @return
	 */
		
	public int updateBorrowCredit(BorrowCredit borrowCredit);

	/**
	 * 还款未债转部分
	 * @param apicron
	 * @param borrowRecover
	 * @param borrowUserCust
	 * @return
	 * @throws Exception 
	 */
		
	public List<Map<String, String>> updateBorrowRepay(BorrowApicron apicron, BorrowRecover borrowRecover,
			AccountChinapnr borrowUserCust) throws Exception;

	/**
	 * @param msgList
	 */
		
	public void sendCreditMessage(List<Map<String, String>> msgList);

	/**
	 * @param msgList
	 */
		
	public void sendMessage(List<Map<String, String>> msgList);

	/**
	 * @param borrowCredit
	 */
		
	public void sendCreditEndMessage(BorrowCredit borrowCredit);

	public int updateCreditRepay(CreditRepay creditRepay);

	public CreditRepay selectCreditRepay(String borrowNid, int assignUserId, String tenderOrdId, String assignNid, int periodNow, int i);

	public int updateBorrowRecoverPlan(BorrowRecoverPlan borrowRecoverPlan);
}
