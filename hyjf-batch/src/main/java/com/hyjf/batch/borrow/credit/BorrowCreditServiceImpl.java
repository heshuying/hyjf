package com.hyjf.batch.borrow.credit;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.mybatis.model.auto.CreditTenderLogExample;

@Service
public class BorrowCreditServiceImpl extends BaseServiceImpl implements BorrowCreditService {

    /**
     * 查询债转的数据列表
     * @param channelStatisticsCustomize
     * @return
     * @author Michael
     */
    @Override
    public List<BorrowCredit> selectBorrowCreditList(BorrowCreditExample borrowCreditExample) {
        return this.borrowCreditMapper.selectByExample(borrowCreditExample);
    }

    /**
     * 更新债转数据
     * @param channelStatisticsCustomize
     * @return
     * @author Michael
     */
    @Override
    public int updateBorrowCredit(BorrowCredit borrowCredit) {
        return this.borrowCreditMapper.updateByPrimaryKey(borrowCredit);
    }

    /**
     * 查询提交承接的临时日志数据列表
     * @param channelStatisticsCustomize
     * @return
     * @author Michael
     */
    @Override
    public List<CreditTenderLog> selectCreditTenderLogList(CreditTenderLogExample creditTenderLogExample) {
        return this.creditTenderLogMapper.selectByExample(creditTenderLogExample);
    }
    
    /**
     * 查询完成承接交易的数据列表
     * @param channelStatisticsCustomize
     * @return
     * @author Michael
     */
    @Override
    public List<CreditTender> selectCreditTenderList(CreditTenderExample creditTenderExample) {
        return this.creditTenderMapper.selectByExample(creditTenderExample);
    }

    /**
     * 更新承接的临时日志数据列表
     * @param channelStatisticsCustomize
     * @return
     * @author Michael
     */
    @Override
    public int updateCreditTenderLog(CreditTenderLog borrowCredit) {
        return this.creditTenderLogMapper.updateByPrimaryKey(borrowCredit);
    }
    
    
}
