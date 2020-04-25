package com.hyjf.app.agreement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.app.AppUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;

public interface AgreementService extends BaseService {
	
	/**
	 * 用户中心债转被出借的协议
	 * 
	 * @return
	 */
	public Map<String, Object> selectUserCreditContract(CreditAssignedBean tenderCreditAssignedBean);
	
	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);
	
    /**
     * 
     * 查询用户汇计划出借明细
     * @author pcc
     * @param params
     * @return
     */
    UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params);

    List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	Integer selectBorrowerByBorrowNid(String borrowNid);
	BigDecimal getAccedeAccount(String accedeOrderId);

	/**
	 * 获取债转承接信息
	 * @param nid
	 * @return
	 */
    HjhDebtCreditTender getHjhDebtCreditTender(Integer nid);

	/**
	 * 获取债转信息
	 * @param creditNid
	 * @return
	 */
	HjhDebtCredit getHjhDebtCreditByCreditNid(String creditNid);

}
