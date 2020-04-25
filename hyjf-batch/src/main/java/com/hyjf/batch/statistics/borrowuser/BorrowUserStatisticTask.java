package com.hyjf.batch.statistics.borrowuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BorrowUserStatisticTask {
    Logger _log = LoggerFactory.getLogger(BorrowUserStatisticTask.class);
    
    @Autowired
    BorrowUserStatisticService borrowUserStatisticService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                _log.info("--------------运营数据统计（借款人相关）start");
                borrowUserStatisticService.statistic();
                _log.info("--------------运营数据统计（借款人相关）end");
            }finally {
                isOver = true;
            }
        }
    }


}
