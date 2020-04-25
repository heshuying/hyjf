package com.hyjf.batch.finance.poundage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 		
* @description: 手续费分账
 */
public class PoundageTimerTask {

	@Autowired
	private PoundageTimerService poundageTimerService;  
    private static final String THIS_CLASS = PoundageTimerTask.class.getName();
	Logger _log = LoggerFactory.getLogger(PoundageTimerTask.class);
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    


    public void run() {
        if (isOver) {
            try {
                isOver = false;
                //
                poundageTimerTask();
            } finally {
                isOver = true;
            }
        }
    }


	public void poundageTimerTask() {
	    _log.info("START"+THIS_CLASS + "==>" + "poundageTimerTask" + "==>" + "手续费分享明细插入开始");
		// 插入手续费分享明细
	    poundageTimerService.insertPoundage();
        _log.info("END"+THIS_CLASS + "==>" + "poundageTimerTask" + "==>" + "手续费分享明细插入结束");
	}

}
