package com.hyjf.bank.service.hjh.borrow.tender;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.customize.HjhAccedeCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 *
 * 汇计划更新表
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月15日
 * @see 下午3:00:47
 */
public interface BankAutoTenderService extends BaseService{

	/**
	 * 查询计划加入明细
	 *
	 * @param accountManageBean
	 * @return
	 */
	List<HjhAccedeCustomize> selectPlanJoinList();

	/**
	 * 查询计划by Plannid
	 * @param planNid
	 * @return
	 */
	HjhPlan selectHjhPlanByPlanNid(String planNid);

	/**
	 * 查询计划订单by 加入计划订单号
	 *
	 * @param accedeOrderId
	 * @return
	 */
	HjhAccede selectHjhAccedeByAccedeOrderId(String accedeOrderId);

//	/**
//	 * 自动投资
//	 * 
//	 * @param hjhPlanAsset
//	 * @return
//	 */
//	boolean autoAssetBorrow(HjhAccede hjhAccede);

	/**
	 * 用户投资一个标的
	 * @param borrowR
	 * @param hjhAccedeR
	 * @param ketouplanAmoust
	 * @param bean
	 * @return
	 */
	boolean updateBorrow(Borrow borrowR,HjhAccede hjhAccedeR, BigDecimal ketouplanAmoust,BankCallBean bean);


	/**
	 *获取投资授权码
	 */
	HjhUserAuth selectUserAuthByUserId(int userId);


	/**
	 * 获取相应的标的详情
	 */
	Borrow selectBorrowByNid(String borrowNid);

	/**
	 * 获取相应的债转详情
	 */
	HjhDebtCredit selectCreditByNid(String creditNid);

	/**
	 * 投资完成更新计划明细
	 */
	int updateHjhAccede(HjhAccede hjhAccede, int orderStaus);

	/**
	 * 投资api
	 * @param borrowNid
	 * @param userId
	 * @param account
	 * @param tenderUsrcustid
	 * @param contOrderId
	 * @return
	 */
	BankCallBean autotenderApi(Borrow borrow, HjhAccede hjhAccede, HjhUserAuth hjhUserAuth,BigDecimal account,String tenderUsrcustid,boolean isLast);

	/**
	 * 自动债转api
	 * @param borrowNid
	 * @param userId
	 * @param account
	 * @param serviceFee
	 * @param assignCapital
	 * @param tenderUsrcustid
	 * @param orderDate
	 * @param orderId
	 * @param contOrderId
	 * @return
	 */
	BankCallBean autoCreditApi(HjhDebtCredit credit, HjhAccede hjhAccede, HjhUserAuth hjhUserAuth,BigDecimal account,BigDecimal assignCapital, BigDecimal serviceFee, String tenderUsrcustid, String sellUsrcustid, String orderId, String orderDate, boolean isLast);


	/**
	 * 插入 自动投资临时表
	 * @param borrow
	 * @param hjhAccede
	 * @param ketouplanAmoust
	 * @param hjhUserAuth
	 * @return
	 */
	Integer insertBorrowTmp(Borrow borrow, HjhDebtCredit credit,HjhAccede hjhAccede, BigDecimal ketouplanAmoust,HjhUserAuth hjhUserAuth,BankCallBean bean,String borrowFlag,boolean isLast);

	/**
	 * 删除 自动投资临时表
	 * @param borrowNid
	 * @param hjhAccede
	 * @return
	 */
	boolean deleteBorrowTmp(String borrowNid, HjhAccede hjhAccede, BankCallBean bean) ;

	boolean deleteBorrowTmp(String borrowNid, String accedeOrderId);

	boolean deleteHjhPlanBorrowTmpByOrderId(String orderId);

	/**
	 *
	 * @param credit
	 * @param debtPlanAccede
	 * @param creditOrderId
	 * @param creditOrderDate
	 * @param account
	 * @param isLast
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> saveCreditTenderLogNoSave(HjhDebtCredit credit, HjhAccede debtPlanAccede, String creditOrderId, String creditOrderDate, BigDecimal account, Boolean isLast) throws Exception;



	/**
	 *
	 * @param credit
	 * @param debtPlanAccede
	 * @param creditOrderId
	 * @param creditOrderDate
	 * @param account
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> saveCreditTenderLog(HjhDebtCredit credit, HjhAccede debtPlanAccede, String creditOrderId, String creditOrderDate, BigDecimal account, Boolean isLast) throws Exception;

	/**
	 *
	 * @param creditR
	 * @param hjhAccedeR
	 * @param hjhPlanR
	 * @param bean
	 * @param tenderUsrcustid
	 * @param sellerUsrcustid
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	boolean updateCredit(HjhDebtCredit creditR, HjhAccede hjhAccedeR, HjhPlan hjhPlanR, BankCallBean bean,
						 String tenderUsrcustid, String sellerUsrcustid, Map<String, Object> resultMap) throws Exception;

	/**
	 * 根据是否原始债权获出让人投标成功的授权号
	 * @param tenderOrderId
	 * @param SourceType
	 * @return
	 */
	String getSellerAuthCode(String tenderOrderId, Integer SourceType);

	/**
	 * 银行结束债权后，更新为完全承接
	 * @param hjhDebtCredit
	 * @return
	 */
	Boolean updateCreditForEnd(HjhDebtCredit hjhDebtCredit);

	/**
	 * 通过nid查找borrowTender
	 * @param tenderNid
	 * @return
	 */
	BorrowTender selectBorrowTenderByNid(String tenderNid);

	/**
	 * 通过AssignOrderId查找HjhDebtCreditTender
	 * @param assignOrderId
	 * @return
	 */
	HjhDebtCreditTender selectHjhDebtCreditTenderByAssignOrderId(String assignOrderId);

	// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 start
	boolean checkAutoPayment(String creditNid);
	// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 end

}
