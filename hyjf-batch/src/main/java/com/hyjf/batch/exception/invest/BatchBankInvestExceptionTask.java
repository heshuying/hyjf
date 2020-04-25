package com.hyjf.batch.exception.invest;

import com.hyjf.mybatis.model.customize.batch.BatchBorrowTenderCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yangchangwei 出借调单异常处理
 */
public class BatchBankInvestExceptionTask {

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    @Autowired
    private BankInvestExceptionService bankInvestExceptionService;

    Logger _log = LoggerFactory.getLogger(BatchBankInvestExceptionTask.class);

    public void run() {
        if (isOver) {
            _log.info("出借异常跑批任务开始------");
            isOver = false;
            try {
                List<BatchBorrowTenderCustomize> list = this.bankInvestExceptionService.queryAuthCodeBorrowTenderList();
                if (list != null && list.size() > 0) {
                    bankInvestExceptionService.insertAuthCode(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isOver = true;
            }
            _log.info("出借异常跑批任务结束------");
        }
    }

}
