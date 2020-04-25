package com.hyjf.admin.manager.debt.debtborrowinvest;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowInvestCustomize;

import java.util.List;

public interface DebtBorrowInvestService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public List<DebtBorrowInvestCustomize> selectBorrowInvestList(DebtBorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public Long countBorrowInvest(DebtBorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public String selectBorrowInvestAccount(DebtBorrowInvestCustomize borrowInvestCustomize);

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
	public List<DebtBorrowInvestCustomize> exportBorrowInvestList(DebtBorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 
	 * 此处为方法说明
	 * 
	 * @author pcc
	 * @param userid
	 * @param nid
	 * @param borrownid
	 */
	public String resendMessageAction(String userid, String nid, String borrownid,String sendEmail);
}
