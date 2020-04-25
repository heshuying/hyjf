package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

public interface RepayExceptionCustomizeMapper {

    /**
     * 出借明细列表
     *
     * @param alllBorrowCustomize
     * @return
     */
    List<RepayExceptionCustomize> selectBorrowRepaymentList(RepayExceptionCustomize repayExceptionCustomize);

    /**
     * 出借明细记录 总数COUNT
     *
     * @param borrowCustomize
     * @return
     */
    Long countBorrowRepayment(RepayExceptionCustomize repayExceptionCustomize);

}