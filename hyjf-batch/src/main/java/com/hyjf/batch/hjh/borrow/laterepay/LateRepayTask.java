package com.hyjf.batch.hjh.borrow.laterepay;

import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 逾期未还款标的更新互金上传数据
 *
 * @author lsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 */
public class LateRepayTask {

    Logger _log = LoggerFactory.getLogger(LateRepayTask.class);

    @Autowired
    LateRepayService lateRepayService;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        _log.info("-------------------【拉取逾期未还款标的】更新互金上传数据任务开始------------------");
        if (isOver) {
            try {
                isOver = false;
                overdueStatistic();
            } finally {
                isOver = true;
            }
        } else {
            _log.info("-------------------【拉取逾期未还款标的】上一轮批次未结束------------------");
        }
        _log.info("-------------------【拉取逾期未还款标的】更新互金上传数据任务结束------------------");
    }

    /**
     * 逾期标的
     */
    private void overdueStatistic() {
        try {
            List<BorrowRepay> borrowRepayList = lateRepayService.selectOverdueBorrowList();
            if (borrowRepayList == null || borrowRepayList.isEmpty()) {
                _log.info("【拉取逾期未还款标的】当前没有逾期的标的信息");
                return;
            }

            for (BorrowRepay record : borrowRepayList) {
                _log.info("【拉取逾期未还款标的】逾期未还款标的：" + record.getBorrowNid() + "，发送到MQ消息队列。");
                // 发送MQ到更新合同状态NIFA
                lateRepayService.sendToMQ(record);
                // add by liushouyi nifa2 20181204 start
                // 互金审计二期逾期标的处理
                NifaBorrowInfoEntity nifaBorrowInfoEntity = this.lateRepayService.dualNifaBorrowInfo(record);
                if (null != nifaBorrowInfoEntity){
                    // 逾期上送数据保存
                    this.lateRepayService.insertNifaBorrowInfo(nifaBorrowInfoEntity);
                }
                // add by liushouyi nifa2 20181204 end
            }
            // 拉取完全债转的数据跟新互金合同状态
            List<BorrowRecover> borrowRecoverList = lateRepayService.selectBorrowRecoverCredit();
            if (null == borrowRecoverList && borrowRecoverList.size()<=0) {
                _log.info("【拉取逾期未还款标的】当前没有完全债转的标的信息");
                return;
            }

            for (BorrowRecover record : borrowRecoverList) {
                _log.info("【拉取逾期未还款标的】完全债转的标的：" + record.getNid() + "，发送到MQ消息队列。");
                // 发送MQ到更新合同状态NIFA
                lateRepayService.sendToMQCredit(record.getNid());
            }

        } catch (Exception e) {
            _log.error("【拉取逾期未还款标的】逾期未还款标的统计任务执行失败", e);
        }
    }


}
