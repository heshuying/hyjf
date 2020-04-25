package com.hyjf.batch.fdd.push;


import com.hyjf.mybatis.model.auto.BorrowApicron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 法大大电子签章放款推送
 *
 * @author yangchangwei
 */
public class FddAgreementPushTask {


    @Autowired
    private FddAgreementPushService fddAgreementPushService;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static boolean isOver = true;

    Logger _log = LoggerFactory.getLogger(FddAgreementPushTask.class);

    public void run() {
        if (isOver) {
            _log.info("------法大大放款推送定时任务开始------");
            isOver = false;
            try {
                List<BorrowApicron> list =  fddAgreementPushService.getFddPushBorrowList();
                if(list != null && list.size() > 0){
                    for (int i = 0; i < list.size(); i++) {
                        BorrowApicron borrowApicron = list.get(i);
                        fddAgreementPushService.updatePushFdd(borrowApicron);
                    }
                }else {
                    _log.info("--------法大大放款没有需要推送的标的---------");
                }
                _log.info("------法大大放款推送定时任务结束------");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isOver = true;
            }
        }
    }
}
