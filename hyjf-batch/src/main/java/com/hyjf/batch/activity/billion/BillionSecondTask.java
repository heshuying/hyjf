package com.hyjf.batch.activity.billion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;

/**
 * 十一月份活动  助力百亿
 * @author Michael
 */
public class BillionSecondTask {

    @Autowired
    BillionOneService billionOneService;
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    //判断中奖用户是否已经生成过
    private static Boolean isExecute = true;
    
    public void run() {
        // 生成中奖用户
        if (isOver) {
            try{
                isOver = false;
                prizeGenerate();
            }finally {
                isOver = true;
            }
        }
    }
    
    /**
     * 生成中奖用户
     */
    private void prizeGenerate(){
    	
    	if(isExecute){
	    	try {
	    		List<ActivityBillionSecondTime> timeList = billionOneService.getAccordTimes();
	    		if(timeList == null){
	    			return;
	    		}
	    		if(timeList.size() < 6){
	    			return;
	    		}
	    		//没有满105亿  不执行
	    		if(timeList.get(5).getAccordTime() == null){
	    			return;
	    		}
	    		
	    		//定义时间段
	    		int stage104to105 = 0,stage103to104 = 0,stage102to103 = 0,stage101to102 = 0,stage100to101 = 0;
	    		
	    		int loopNum = 0;
	    		int min = 0;
	    		//取时间段,取最短时间
	    		for(int i = timeList.size() - 1;i > 0 ;i--){
	    			loopNum = timeList.get(i).getAccordTime() - timeList.get(i-1).getAccordTime();
	    			if(i == 5){
	    				stage104to105 = loopNum;
	    				min = stage104to105;
	    			}else if(i == 4){
	    				stage103to104 = loopNum;
	    				min = stage103to104 - min > 0 ? min : stage103to104;
	    			}else if(i == 3){
	    				stage102to103 = loopNum;
	    				min = stage102to103 - min > 0 ? min : stage102to103;
	    			}else if(i == 2){
	    				stage101to102 = loopNum;
	    				min = stage101to102 - min > 0 ? min : stage101to102;
	    			}else if(i == 1){
	    				stage100to101 = loopNum;
	    				min = stage100to101 - min > 0 ? min : stage100to101;
	    			}
	    		}
	    		int startTime = 0 , endTime = 0;	
	    		
	    		if(min == stage100to101){
	    			startTime = timeList.get(0).getAccordTime();
	    			endTime = timeList.get(1).getAccordTime();
	    		}else if(min == stage101to102){
	    			startTime = timeList.get(1).getAccordTime();
	    			endTime = timeList.get(2).getAccordTime();
	    		}else if(min == stage102to103){
	    			startTime = timeList.get(2).getAccordTime();
	    			endTime = timeList.get(3).getAccordTime();
	    		}else if(min == stage103to104){
	    			startTime = timeList.get(3).getAccordTime();
	    			endTime = timeList.get(4).getAccordTime();
	    		}else if(min == stage104to105){
	    			startTime = timeList.get(4).getAccordTime();
	    			endTime = timeList.get(5).getAccordTime();
	    		}
	    		//生成记录
	    		this.billionOneService.createBillionSecondPrize(startTime, endTime);
	    		isExecute = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	
    	}
    }
}
