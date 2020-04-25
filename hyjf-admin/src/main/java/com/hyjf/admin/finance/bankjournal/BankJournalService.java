package com.hyjf.admin.finance.bankjournal;

/**
 * Created by cui on 2018/1/19.
 */
public interface BankJournalService {

    /**
     * 查询银行交易明细列表
     * @param form
     */
    void queryList(BankJournalBean form);

}
