package com.hyjf.batch.hjh.commision.compute;

import java.util.List;

import com.hyjf.bank.service.hjh.commision.compute.CommisionComputeService;
import com.hyjf.bank.service.hjh.commision.compute.CommisionComputeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.HjhAccede;

/**
 * 
 * 汇计划提成计算
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 */
public class CommisionComputeTask {
    
    Logger _log = LoggerFactory.getLogger(CommisionComputeServiceImpl.class);

    @Autowired
    CommisionComputeService commisionComputeService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                runCompute();
            }finally {
                isOver = true;
            }
        }
    }
    
    /**
     * 计算提成
     */
    private void runCompute(){
        _log.info("-------------------汇计划提成计算任务开始------------------");
        HjhAccede currentRecord = null;
    	try {
    	    List<HjhAccede> accedeList = commisionComputeService.selectHasCommisionAccedeList();
    	    if(accedeList ==null || accedeList.isEmpty()){
    	        _log.info("当前没有待计算的计入记录");
    	        return;
    	    }
    	    
    	    for(HjhAccede record : accedeList){
    	        currentRecord = record;
    	        _log.info("计算提成，订单号：" + record.getAccedeOrderId() + " 用户名：" + record.getUserName());
    	        commisionComputeService.commisionCompute(record);
    	    }
        } catch (Exception e) {
           _log.error("汇计划提成计算任务执行失败", e);
           commisionComputeService.statusUpdate(currentRecord, 2);
        }
    	
    	_log.info("-------------------汇计划提成计算任务结束-----------------");
    }

    
}
