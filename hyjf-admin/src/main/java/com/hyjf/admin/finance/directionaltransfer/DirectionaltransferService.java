package com.hyjf.admin.finance.directionaltransfer;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.AccountDirectionalTransfer;

public interface DirectionaltransferService extends BaseService {

    /**
     * 检索定向转账件数
     * 
     * @param form
     * @return
     */
    public int countRecordTotal(DirectionaltransferBean form);

    /**
     * 定向转账list
     * 
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<AccountDirectionalTransfer> getRecordList(DirectionaltransferBean form, int limitStart, int limitEnd);
}
