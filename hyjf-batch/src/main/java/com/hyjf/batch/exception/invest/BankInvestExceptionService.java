package com.hyjf.batch.exception.invest;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.batch.BatchBorrowTenderCustomize;

import java.util.List;

/**
 * @author cwyang
 *         江西银行出借调单处理自动任务
 */
public interface BankInvestExceptionService extends BaseService {

    /**
     * @param list
     */
    void insertAuthCode(List<BatchBorrowTenderCustomize> list);

    /**
     * 查询出出借表authcode为空的记录
     *
     * @return
     */
    List<BatchBorrowTenderCustomize> queryAuthCodeBorrowTenderList();
}
