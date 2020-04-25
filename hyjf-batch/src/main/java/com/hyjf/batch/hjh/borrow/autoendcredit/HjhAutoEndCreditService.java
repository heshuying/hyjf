package com.hyjf.batch.hjh.borrow.autoendcredit;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;

import java.util.List;

/**
 * 汇计划自动结束前一天未完全承接的债权定时任务Service
 *
 * @author liuyang
 */
public interface HjhAutoEndCreditService extends BaseService{

    /**
     * 检索当天的未完全承接完成的债转
     * @return
     */
    List<HjhDebtCredit> selectHjhDebtCreditList();

    /**
     * 更新债转状态
     * @param hjhDebtCredit
     */
    void updateHjhDebtCreditStatus(HjhDebtCredit hjhDebtCredit) throws Exception;
}
