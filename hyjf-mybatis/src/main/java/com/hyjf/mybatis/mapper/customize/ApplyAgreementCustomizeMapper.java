package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.BorrowRepayAgreementCustomize;

public interface ApplyAgreementCustomizeMapper {
    Long countBorrowRepay(BorrowRepayAgreementCustomize example);

    Long countBorrowRepayPlan(BorrowRepayAgreementCustomize example);

    List<BorrowRepayAgreementCustomize> selectBorrowRepay(BorrowRepayAgreementCustomize example);
    
    List<BorrowRepayAgreementCustomize> selectBorrowRepayPlan(BorrowRepayAgreementCustomize example);
    
}