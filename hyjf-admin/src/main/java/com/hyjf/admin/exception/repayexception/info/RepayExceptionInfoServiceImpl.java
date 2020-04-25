package com.hyjf.admin.exception.repayexception.info;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

@Service
public class RepayExceptionInfoServiceImpl extends BaseServiceImpl implements RepayExceptionInfoService {

    /**
     * 出借明细列表
     *
     * @param borrowCommonCustomize
     * @return
     * @author Administrator
     */

    @Override
    public List<RepayExceptionCustomize> selectBorrowRepaymentList(RepayExceptionCustomize repayExceptionCustomize) {
        return this.repayExceptionInfoCustomizeMapper.selectBorrowRepaymentList(repayExceptionCustomize);
    }

    /**
     * 出借明细记录 总数COUNT
     *
     * @param borrowCustomize
     * @return
     */
    public Long countBorrowRepayment(RepayExceptionCustomize repayExceptionCustomize) {
        return this.repayExceptionInfoCustomizeMapper.countBorrowRepayment(repayExceptionCustomize);
    }

    /**
     * 出借明细记录 总数SUM
     *
     * @param borrowCustomize
     * @return
     */
    public RepayExceptionCustomize sumBorrowRepaymentInfo(RepayExceptionCustomize repayExceptionCustomize) {
        return this.repayExceptionInfoCustomizeMapper.sumBorrowRepaymentInfo(repayExceptionCustomize);
    }
}
