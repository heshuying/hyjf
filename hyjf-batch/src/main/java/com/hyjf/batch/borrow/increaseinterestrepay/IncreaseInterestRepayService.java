package com.hyjf.batch.borrow.increaseinterestrepay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;

/**
 * 融通宝加息还款Service
 * 
 * @ClassName IncreaseInterestRepayService
 * @author 孙小宝
 * @date 2017年1月3日 上午9:21:31
 */
public interface IncreaseInterestRepayService extends BaseService {

	/**
	 * 检索未执行的任务
	 * 
	 * @Title selectBorrowApicronList
	 * @param statusWait
	 * @param status
	 * @return
	 */
	List<BorrowApicron> selectBorrowApicronList(Integer statusWait, int status);

	/**
	 * 更新还款任务
	 * 
	 * @Title updateBorrowApicron
	 * @param id
	 * @param statusError
	 * @param errorMsg
	 */
	public int updateBorrowApicron(Integer id, Integer statusError, String errorMsg);

	/**
	 * 根据借款编号检索借款详情
	 * 
	 * @Title selectBorrowInfo
	 * @param borrowNid
	 * @return
	 */
	public BorrowWithBLOBs selectBorrowInfo(String borrowNid);

	/**
	 * 更新还款任务
	 * 
	 * @Title updateBorrowApicron
	 * @param id
	 * @param statusWait
	 */
	public int updateBorrowApicron(Integer id, Integer statusWait);

	/**
	 * 根据借款编号检索还款信息
	 * 
	 * @Title selectIncreaseInterestLoanList
	 * @param borrowNid
	 * @return
	 */
	public List<IncreaseInterestLoan> selectIncreaseInterestLoanList(String borrowNid);

	/**
	 * 根据借款人userId检索借款人账款信息
	 * 
	 * @Title selectAccountByUserId
	 * @param borrowUserId
	 * @return
	 */
	public Account selectAccountByUserId(Integer borrowUserId);

	/**
	 * 根据借款编号,期数 ,借款方式取得还款金额
	 * 
	 * @Title selectBorrowAccountWithPeriod
	 * @param borrowNid
	 * @param borrowStyle
	 * @param periodNow
	 * @return
	 */
	public BigDecimal selectBorrowAccountWithPeriod(String borrowNid, String borrowStyle, Integer periodNow);

	/**
	 * 检索融通宝加息子账户剩余金额
	 * 
	 * @Title selectCompanyAccount
	 * @return
	 */
	public BigDecimal selectCompanyAccount();

	/**
	 * 自动还款
	 * 
	 * @Title updateBorrowRepay
	 * @param apicron
	 * @param increaseInterestLoan
	 * @param borrowUserCust
	 * @return
	 */
	List<Map<String, String>> updateBorrowRepay(BorrowApicron apicron, IncreaseInterestLoan increaseInterestLoan, BankOpenAccount borrowUserCust);

	/**
	 * 还款成功后,更新标的状态
	 * 
	 * @Title updateBorrowStatus
	 * @param borrowNid
	 * @param periodNow
	 * @param borrowUserId
	 */
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId);

	/**
	 * 还款成功后,发送短信
	 * 
	 * @Title sendSms
	 * @param msgList
	 */
	public void sendSms(List<Map<String, String>> msgList);

	/**
	 * 还款成功后,发送消息推送
	 * 
	 * @Title sendMessage
	 * @param msgList
	 */
	public void sendMessage(List<Map<String, String>> msgList);

	/**
	 * 一次性还款的情况下获取优先处理任务
	 * @param borrowNid
	 * @return
	 */
    BorrowApicron getRepayPeriodSort(String borrowNid);
}
