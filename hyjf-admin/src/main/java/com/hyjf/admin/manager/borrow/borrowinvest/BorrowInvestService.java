package com.hyjf.admin.manager.borrow.borrowinvest;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

import java.util.List;

public interface BorrowInvestService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public List<BorrowInvestCustomize> selectBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public Long countBorrowInvest(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public String selectBorrowInvestAccount(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 渠道下拉列表
	 * 
	 * @return
	 */
	public List<UtmPlat> getUtmPlatList();

	/**
	 * 导出出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public List<BorrowInvestCustomize> selectExportBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 
	 * 发送计划的居间协议
	 * 
	 * @author pcc
	 * @param userid
	 * @param nid
	 * @param borrownid
	 */
	public String resendMessageAction(String userid, String nid, String borrownid,String sendEmail);
	
	/**
	 * 出借人债权明细
	 * @param 
	 * @return
	 */
	List<BankCallBean> queryInvestorDebtDetails(InvestorDebtBean form);
	
	/**
	 * 
	 * 发送散标的居间协议
	 * 
	 * @author pcc
	 * @param userid
	 * @param nid
	 * @param borrownid
	 */
	/*public String sendMessageAction(String userid, String nid, String borrownid,String sendEmail);*/
	
	/**
	 * 出借金额合计值取得
	 * @author PC-LIUSHOUYI
	 * @param borrowInvestCustomize
	 * @return
	 */
	public String sumBorrowInvest(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 检索BorrowRecover
	 * @param userId
	 * @param borrowNid
	 * @param nid
	 * @return
	 */
    BorrowRecover selectBorrowRecover(Integer userId, String borrowNid, String nid);

}
