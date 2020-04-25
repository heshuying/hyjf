package com.hyjf.batch.exception.debtexception;

import com.hyjf.mybatis.model.customize.admin.AdminBankDebtEndCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 结束债权异常定时任务
 * @author liuyang
 */
public class BatchDebtEndTask {

    @Autowired
    private BatchDebtEndService batchDebtEndService;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static boolean isOver = true;

    Logger _log = LoggerFactory.getLogger(BatchDebtEndTask.class);

    public void run() {
        if (isOver) {
            isOver = false;
            _log.info("----批次请求结束债权开始----");
            try {
                // 查询需要解释的债权
                List<AdminBankDebtEndCustomize> recordList = this.batchDebtEndService.selectDebtEndList();
                if (recordList != null && recordList.size() > 0) {
                    // 循环调用银行结束债权接口
                    for (AdminBankDebtEndCustomize debtEndCustomize : recordList) {
                        this.batchDebtEndService.requestDebtEnd(debtEndCustomize.getBorrowNid(), debtEndCustomize.getTenderNid(), Integer.parseInt(debtEndCustomize.getUserId()));
                    }
                }
                _log.info("----批次请求结束债权结束----");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isOver = true;
            }
        }
    }
}
