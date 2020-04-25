package com.hyjf.batch.datarecover.creditrec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.CreditTender;

/**
 * 债转承接时推荐人信息恢复保存
 * @author hsy
 *
 */
class CreditTenderRecRecoverTask {
    Logger _log = LoggerFactory.getLogger(CreditTenderRecRecoverTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;
	
	private static Integer STEP_CONST = 500;

    @Autowired
    CreditTenderRecRecoverService recoverService;

    public void run() {
    	if (isOver) {
			try {
				isOver = false;
				//
				process();
			} finally {
				isOver = true;
			}
		}
    }
    
    /**
     * 检查优惠券是否过期
     */
    private void process(){
    	_log.info("【债转承接时推荐人信息恢复保存】开始...");
    	Integer idStart = 0;
    	Integer idEnd = STEP_CONST;
    	int idMax = recoverService.getMaxTenderId();
    	if(idMax <=0 ){
    		_log.info("【债转承接时推荐人信息恢复保存】没有承接记录");
    		return;
    	}
    	
    	do{
    		_log.info("【债转承接时推荐人信息恢复保存】 idStart: " +idStart + " idEnd: " + idEnd);
    		List<CreditTender> tenderList = recoverService.selectTenderList(idStart, idEnd);
    		
    		idStart = idEnd;
    		idEnd = idEnd + STEP_CONST;
    		
    		if(tenderList == null || tenderList.isEmpty()){
    			continue;
    		}
    		
    		for(CreditTender record : tenderList){
    			try {
					recoverService.updateCreditTender(record);
				} catch (Exception e) {
					_log.error("更新失败，assign_nid："  + record.getAssignNid(), e);
				}
    		}
    		
    		
    	}while(idEnd < idMax+STEP_CONST);
    	
    	_log.info("【债转承接时推荐人信息恢复保存】结束。。。");
    }

    
}
