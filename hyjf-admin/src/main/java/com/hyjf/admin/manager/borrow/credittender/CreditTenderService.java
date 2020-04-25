package com.hyjf.admin.manager.borrow.credittender;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface CreditTenderService extends BaseService {


	/**
	 * 获取详细列表COUNT
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public Integer countBorrowCreditTenderList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 获取详细列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public List<BorrowCreditCustomize> selectBorrowCreditTenderList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 获取金额合计值
	 * 
	 * @param sumBorrowCreditInfo
	 * @return
	 */
	BorrowCreditCustomize sumBorrowCreditInfo(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 检索承接记录
	 * @param userId
	 * @param borrowNid
	 * @param assignNid
	 * @param creditTenderNid
	 * @param creditNid
	 * @return
	 */
	CreditTender selectCreditTenderRecord(String userId, String borrowNid, String assignNid, String creditTenderNid, String creditNid);

	/**
	 * 调用江西银行查询单笔出借人投标申请
	 * @param userId
	 * @param orderId
	 * @param accountId
	 * @return
	 */
	List<BankCallBean> bidApplyQuery(String userId, String orderId, String accountId);
}
