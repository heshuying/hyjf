package com.hyjf.admin.manager.borrow.borrowcreditrepay;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditRepayCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditTenderCustomize;

public interface BorrowCreditRepayService extends BaseService {

    /**
     * 管理后台   汇转让   还款计划  已承接债转  数目
     * 
     * @return
     */
    Integer countCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);

    /**
     * 管理后台   汇转让   还款计划  已承接债转 列表
     * 
     * @return
     */
    List<AdminBorrowCreditTenderCustomize> selectCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);

    /**
     * 债转还款计划   详细画面
     * 
     * @param request
     * @param form
     * @return
     */
    Integer countBorrowCreditRepayInfoList(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);

    /**
     * 债转还款计划  创建分页机能
     * 
     * @param request
     * @param modeAndView
     * @param form
     */
    List<AdminBorrowCreditRepayCustomize> selectBorrowCreditRepayInfoList(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);
    
	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId);
	/**
     * 获取用户名
     * 
     * @return
     */
	public Users getUsers(String userName);
	/**
     * 金额合计值获取
     * 
     * @return
     */
	public AdminBorrowCreditTenderCustomize sumCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);

	/**
     * 金额合计值获取
     * 
     * @return
     */
	public AdminBorrowCreditRepayCustomize sumCreditRepay(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);

}
