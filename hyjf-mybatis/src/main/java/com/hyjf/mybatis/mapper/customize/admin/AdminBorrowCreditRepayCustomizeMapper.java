package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditTenderCustomize;

public interface AdminBorrowCreditRepayCustomizeMapper {

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
     * 金额合计值取得
     * 
     * @return
     */
    AdminBorrowCreditTenderCustomize sumCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize);
}