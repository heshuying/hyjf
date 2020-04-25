package com.hyjf.batch.hjh.notice.sms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.Borrow;

/**
 * 
 * 标的还款逾期短信提醒
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 * @see 上午9:54:02
 */
public class HjhSmsNoticeTask {
    
    Logger _log = LoggerFactory.getLogger(HjhSmsNoticeServiceImpl.class);

    @Autowired
    HjhSmsNoticeService hjhSmsNoticeService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                overdueStatistic();
            }finally {
                isOver = true;
            }
        }
    }
    
    /**
     * 逾期标的发短信
     */
    private void overdueStatistic(){
        _log.info("-------------------逾期未还款标的统计发短信任务开始------------------");
    	try {
    	    List<Borrow> borrowList = hjhSmsNoticeService.selectOverdueBorrowList();
    	    if(borrowList ==null || borrowList.isEmpty()){
    	        _log.info("当前没有逾期的标的信息");
    	        return;
    	    }
    	    
    	    for(Borrow record : borrowList){
    	        _log.info("逾期未还款标的：" + record.getBorrowNid());
    	        hjhSmsNoticeService.sendSmsForManager(record.getBorrowNid(), record.getUserId());
    	    }
        } catch (Exception e) {
           _log.error("逾期未还款标的统计发短信任务执行失败", e);
        }
    	
    	_log.info("-------------------逾期未还款标的统计发短信任务结束-----------------");
    }

    
}
