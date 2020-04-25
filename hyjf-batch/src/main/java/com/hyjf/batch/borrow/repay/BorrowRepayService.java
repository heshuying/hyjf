package com.hyjf.batch.borrow.repay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

public interface BorrowRepayService extends BaseService {

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
     * 自动放款(本金)
     *
     * @param apicron
     * @return
     */
    public List<Map<String, String>> updateBorrowRepay(BorrowApicron apicron, BorrowRecover borrowRecover, AccountChinapnr borrowUserCust) throws Exception;
    
    /**
     * 取得还款API任务表
     *
     * @return
     */
    public List<BorrowApicron> getBorrowApicronList(Integer status, Integer apiType);

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

	/**
	 * @param msgList
	 * @param borrowNid 
	 */
		
	public void sendMessage(List<Map<String, String>> msgList);


	public int updateBorrowRecoverPlan(BorrowRecoverPlan borrowRecoverPlan);


	public BorrowRecoverPlan getBorrowRecoverPlan(String borrowNid, Integer periodNow, Integer tenderUserId, Integer tenderId);

	/**
	 * 获取实际还款用户id
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	public Integer getRepayUserId(Integer userId, String borrowNid);}





