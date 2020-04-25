package com.hyjf.api.surong.borrow.status;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.AccountBorrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.customize.BorrowSynCustomize;

public interface BorrowInfoSynService extends BaseService {
    
    /**
     * 取得标的详情
     *
     * @return
     */
    BorrowSynCustomize getBorrow(String borrowNid);
    
    List<BorrowRepayPlan> getBorrowRepayPlan(String borrowNid);
    
    BigDecimal getBorrowRepayFee(String borrowNid,Integer repayStatus);
    
    BorrowRepay getBorrowRepay(String borrowNid);
    
    AccountBorrow getAccountBorrow(String borrowNid);

    BorrowApicron getBorrowApicron(String borrowNid);
}
