package com.hyjf.bank.service.user.tender;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 
 * 产品加息
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年7月28日
 * @see 上午9:57:51
 */
public interface IncreaseInterestInvestService extends BaseService {

    /**
     * 产品加息 
     * @author sunss
     * @param borrow
     * @param bean
     */
    public Integer insertIncreaseInterest(Borrow borrow, BankCallBean bean , BorrowTender tender);
}
