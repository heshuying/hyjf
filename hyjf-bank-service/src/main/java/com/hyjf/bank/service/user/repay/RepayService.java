package com.hyjf.bank.service.user.repay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface RepayService extends BaseService {

	/**
	 * 获取用户还款列表
	 * 
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<WebUserRepayProjectListCustomize> searchUserRepayList(RepayProjectListBean form, int offset, int limit);

	/**
	 * 统计用户还款列表总条数
	 * 
	 * @param form
	 * @return
	 */
	int countUserRepayRecordTotal(RepayProjectListBean form);

	/**
	 * 查询项目的用户出借信息
	 * 
	 * @param borrowNid
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, int limitStart, int limitEnd);

	/**
	 * 统计用户出借列表总条数
	 * 
	 * @param borrowNid
	 * @return
	 */
	int countUserInvestRecordTotal(String borrowNid);

	/**
	 * 查询用户还款详情
	 * 
	 * @param form
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	ProjectBean searchRepayProjectDetail(ProjectBean form,boolean isAllRepay) throws Exception;

	/**
	 * 查询还款用户信息
	 * 
	 * @param userId
	 * @return
	 */
	Users searchRepayUser(int userId);

	/**
	 * 查询还款用户的账户金额
	 * 
	 * @param userId
	 * @return
	 */
	Account searchRepayUserAccount(int userId);

	/**
	 * 查询待还款项目信息
	 * 
	 * @param userId
	 * @param userName
	 * @param roleId
	 * @param borrowNid
	 * @return
	 */
	Borrow searchRepayProject(int userId, String userName, String roleId, String borrowNid);

	/**
	 * 统计用户的相应还款总额，分期
	 * 
	 * @param userId
	 * @param borrow
	 * @param borrowApr
	 * @param borrowStyle
	 * @param periodTotal
	 * @return
	 * @throws ParseException
	 * @throws Exception 
	 */
	BigDecimal searchRepayByTermTotal(int userId, Borrow borrow, BigDecimal borrowApr, String borrowStyle, int periodTotal) throws Exception;

	/**
	 * 统计用户的相应的还款总额 单期
	 * 
	 * @param userId
	 * @param borrow
	 * @return
	 * @throws ParseException
	 */
	BigDecimal searchRepayTotal(int userId, Borrow borrow) throws ParseException;

	/**
	 * 计算相应的分期还款信息
	 * 
	 * @param userId
	 * @param borrow
	 * @return
	 * @throws ParseException
	 * @throws Exception 
	 */
	RepayBean calculateRepayByTerm(int userId, Borrow borrow) throws Exception;

	/**
	 * 计算相应的未分期还款信息
	 * 
	 * @param userId
	 * @param borrow
	 * @return
	 * @throws ParseException
	 */
	RepayBean calculateRepay(int userId, Borrow borrow) throws ParseException;

	/**
	 * 用户还款
	 * 
	 * @param repay
	 * @param callBackBean
	 * @param userName
	 * @param isAllRepay
     * @throws Exception
	 */
	public boolean updateRepayMoney(RepayBean repay, BankCallBean callBackBean, Integer roleId, Integer repayUserId, String userName, boolean isAllRepay) throws Exception;

	/**
	 * 查询用户在平台的账户余额
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getUserBalance(Integer userId);

	/**
	 * 根据用户Id,借款标号查询用户的出借记录
	 * 
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	public BorrowTender getBorrowTenderInfo(Integer userId, String borrowNid);

	List<WebUserInvestListCustomize> selectUserDebtInvestList(String borrowNid, String orderId, int i, int j);

	/**
	 * 查询垫付机构的未还款金额
	 * 
	 * @param userId
	 * @return
	 */
	BigDecimal getRepayOrgRepaywait(Integer userId);

	/**
	 * 根据项目id查询相应的用户的待还款信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	public List<BorrowRecover> searchBorrowRecover(String borrowNid);

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId);

	/**
	 * 根据标的号,还款状态检索还款状态
	 * 
	 * @param borrowNid
	 * @param repayStatus
	 * @return
	 */
	public List<BorrowRecover> searchBorrowRecoverByRepayStatus(String borrowNid, int repayStatus);

	/**
	 * 获取相应的放款任务
	 * 
	 * @param bankSeqNo
	 * @return
	 */
	public BorrowApicron selectBorrowApicron(String bankSeqNo);

	/**
	 * 更新借款API任务表
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean updateBorrowApicron(BorrowApicron apicron, int status) throws Exception;

	BankCallBean batchQuery(BorrowApicron apicron);

	boolean batchDetailsQuery(BorrowApicron apicron);

	/**
	 * 查询垫付机构的待收垫付总额
	 * 
	 * @param userId
	 * @return
	 */
	BigDecimal getUncollectedRepayOrgRepaywait(Integer userId);

	/**
	 * 检索垫付机构已垫付项目列表件数
	 * 
	 * @param form
	 * @return
	 */
	public int countOrgRepayRecordTotal(RepayProjectListBean form);

	/**
	 * 检索垫付机构已垫付项目列表
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<WebUserRepayProjectListCustomize> searchOrgRepayList(RepayProjectListBean form, int offset, int limit);

	/**
	 * 判断标的债转出借列表
	 * @param borrowNid
	 * @return
	 */
	public List<CreditTender> selectCreditTenderList(String borrowNid) ;
	
	
	/**
	 * 用户中心债转被出借的协议
	 * 
	 * @return
	 */
	public Map<String, Object> selectUserCreditContract(Map<String,String> param);

	/**
	 * 插入冻结日志信息
	 * @param userId
	 * @param orderId
	 * @param account
	 * @param borrowNid
	 * @param repayTotal
	 * @param userName
	 */
	public void insertRepayFreezeLof(Integer userId, String orderId, String account, String borrowNid, BigDecimal repayTotal,
			String userName);

	/**
	 * 校验还款是否重复
	 * @param borrowNid
	 * @return
	 */
	public boolean checkRepayInfo(String borrowNid);
	
	/**
	 * 删除还款冻结日志
	 * @param orderId
	 */
	public void deleteFreezeTempLogs(String orderId);

	/**
	 * 获取开户信息
	 * @param bankAccount
	 * @return
	 */
	public BankOpenAccount getBankOpenAccount(String bankAccount);

	/**
	 * 获得用户管理费
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public BigDecimal getRepayMangeFee(String roleId, Integer userId);



	/**
	 * 根据项目编号更新正在出让的债转
	 * @param borrow
	 * @return
	 */
    boolean updateBorrowCreditStautus(Borrow borrow);
    
	/**
	 * 判断标的债转出借列表(计划债转)
	 * @param borrowNid
	 * @return
	 */
	public List<HjhDebtCreditTender> selectHjhCreditTenderList(String borrowNid) ;
	
	/**
     * 承接订单查询的债转出借列表
     * @param assignOrderId
     * @return
     */
    public List<HjhDebtCreditTender> selectHjhCreditTenderListByassignOrderId(String assignOrderId) ;
	
	/**
     * 更新批次债权结束，校验
     *
     * @param bean
     * @return
     */
    public int updateBatchCreditEndCheck(BankCallBean bean);
	
	/**
     * 更新批次债权结束状态
     *
     * @param bean
     * @return
     */
    public int updateBatchCreditEnd(BankCallBean bean);



	RepayBean searchRepayPlanTotal(int userId, Borrow borrow) throws Exception;

	RepayBean searchRepayByTermTotalV2(int userId, Borrow borrow, BigDecimal borrowApr, String borrowStyle,
			int periodTotal) throws Exception;

	RepayBean searchRepayTotalV2(int userId, Borrow borrow) throws Exception;

	/**
	 * 插入垫付机构冻结日志信息
	 * @param userId
	 * @param orderId
	 * @param account
	 * @param borrow
	 * @param repay
	 * @param userName
	 */
	void insertRepayOrgFreezeLof(Integer userId, String orderId, String account, Borrow borrow, RepayBean repay,
								 String userName, boolean isAllRepay);
	/**
	 * 根据条件查询垫付机构冻结日志
	 * @param borrowNid
	 * @param orderId
	 * @return
	 */
	List<BankRepayOrgFreezeLog> getBankRepayOrgFreezeLogList(String borrowNid, String orderId);

	/**
	 * 删除垫付机构还款冻结日志
	 * @param orderId
	 */
	void deleteOrgFreezeTempLogs(String orderId, String borrowNid);


	WebUserTransferBorrowInfoCustomize getBorrowInfo(String borrowNid);
	/**
	 * 用户待还债转详情列表
	 * @return
	 */
	List<WebUserRepayTransferCustomize> selectUserRepayTransferDetailList(String borrowNid, String verificationFlag, int offset, int limit);

	/**
	 * 用户待还债转详情列表总条数
	 * @return
	 */
	int selectUserRepayTransferDetailListTotal(String borrowNid,  String verificationFlag);

	/**
	 * 根据标的ID 获取标的信息
	 * @param borrowNid
	 * @return
	 */
	List<Borrow> getBorrowInfoByBorrowNid(String borrowNid);

	/**
	 * 根据项目id获取项目信息
	 *
	 * @param borrowNid
	 * @return
	 */
	WebProjectDetailCustomize selectProjectDetail(String borrowNid);
}
