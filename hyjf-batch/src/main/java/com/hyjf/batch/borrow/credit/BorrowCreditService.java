package com.hyjf.batch.borrow.credit;

import java.util.List;

import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.mybatis.model.auto.CreditTenderLogExample;

public interface BorrowCreditService {
    List<BorrowCredit> selectBorrowCreditList(BorrowCreditExample borrowCreditExample);
    
    int updateBorrowCredit(BorrowCredit borrowCredit);
    
    List<CreditTenderLog> selectCreditTenderLogList(CreditTenderLogExample borrowCreditExample);
    
    List<CreditTender> selectCreditTenderList(CreditTenderExample borrowCreditExample);
    
    int updateCreditTenderLog(CreditTenderLog borrowCredit);
    
}
