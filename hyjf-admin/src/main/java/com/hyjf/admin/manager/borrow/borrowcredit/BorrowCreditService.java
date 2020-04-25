package com.hyjf.admin.manager.borrow.borrowcredit;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;

public interface BorrowCreditService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countBorrowCredit(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowCreditCustomize> selectBorrowCreditList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 导出汇转让列表
	 * 
	 * @return
	 */
	public List<BorrowCreditCustomize> exportBorrowCreditList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 获取详细列表COUNT
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public Integer countBorrowCreditInfoList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 获取详细列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public List<BorrowCreditCustomize> selectBorrowCreditInfoList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 汇转让详细
	 * 
	 * @return
	 */
	public BorrowCredit getBorrowCredit(BorrowCredit borrowCredit);

	/**
	 * 汇转让更新
	 * 
	 * @return
	 */
	public void updateBorrowCredit(BorrowCredit borrowCredit);

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId);

	public	AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(
			Map<String, Object> params);
	
	/**
	 * 获取金额合计
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public BorrowCreditCustomize sumBorrowCredit(BorrowCreditCustomize borrowCreditCustomize);
	
	
	/**
	 * dialog获取金额合计
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public BorrowCreditCustomize sumBorrowCreditInfo(BorrowCreditCustomize borrowCreditCustomize);
}
