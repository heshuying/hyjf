package com.hyjf.batch.bank.borrow.splitsend;

import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 拆分标的的自动发标
 *
 * @author HBZ
 */
public class SplitSendTask {

    Logger _log = LoggerFactory.getLogger(SplitSendTask.class);

    /**
     * 运行状态
     */
    private static int isRun = 0;

    @Autowired
    private SplitSendService splitSendService;

    /**
     * 拆分标的的自动发标
     */
    public void run() {
        try {
            if (isRun == 0) {
                _log.info("拆分标的的自动发标 OntimeSplitTenderTask.run Start... ");
                isRun = 1;
                int nowTime = GetDate.getNowTime10();// 当前时间
                // 获取所有待发布的标的
                List<BorrowWithBLOBs> unSendBorrows = this.splitSendService.queryAllSplitSend();
                //判断待发标列表
                if (unSendBorrows != null && unSendBorrows.size() > 0) {
                    // 延期自动发标时间（单位：分钟）
                    int afterTime = this.splitSendService.getAfterTime(BorrowSendTypeEnum.FABIAO_CD);
                    for (BorrowWithBLOBs unSendBorrow : unSendBorrows) {
                        // 获取批次标的号
                        String borrowPreNid = unSendBorrow.getBorrowPreNid();
                        // 获取新的标的号
                        String borrowPreNidNew = unSendBorrow.getBorrowPreNidNew();
                        // 获取已经复审的标的
                        BorrowWithBLOBs preBorrow = this.splitSendService.getPreBorrow(borrowPreNid, borrowPreNidNew);
                        if (preBorrow != null) {
                            if (preBorrow.getStatus() == 3 || preBorrow.getStatus() == 4 || preBorrow.getStatus() == 5) {
                                // 当前标的满标时间
                                Integer fullTime = Integer.parseInt(preBorrow.getReverifyTime());
                                // 当前标的满标时间 + 等待发标时间
                                Date compareDate = GetDate.getMinutesAfter(fullTime, afterTime);
                                long compareDateLong = compareDate.getTime() / 1000;
                                // 如果当前时间 >= 等待发标时间 + 上期标的满标时间
                                if (nowTime >= compareDateLong) {
                                    // 开始发标吧
                                    this.splitSendService.updateFireBorrow(unSendBorrow, nowTime);
                                }
                            }
                        }
                    }
                }
            } else {
                _log.info("拆分标的的自动发标 OntimeSplitTenderTask.run 正在执行... ");
                return;
            }
            isRun = 0;
            _log.info("拆分标的的自动发标 OntimeSplitTenderTask.run End... ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isRun = 0;
        }
    }
}
