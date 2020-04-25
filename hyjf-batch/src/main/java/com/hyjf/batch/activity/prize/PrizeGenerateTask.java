package com.hyjf.batch.activity.prize;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

/**
 * 
 * 生成中奖用户
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 * @see 上午9:54:02
 */
public class PrizeGenerateTask {
    private static final String THIS_CLASS = PrizeGenerateTask.class.getName();


    @Autowired
    PrizeGenerateService prizeGenerateService;
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

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
    	String methodName = "prizeGenerate";
    	LogUtil.startLog(THIS_CLASS, methodName, "生成中奖用户开始");
    	
    	try {
            prizeGenerateService.updatePrizeGenerate();
        } catch (Exception e) {
           LogUtil.errorLog(THIS_CLASS, methodName, "生成中奖用户失败", e);
        }
    	
    	LogUtil.endLog(THIS_CLASS, methodName, "生成中奖用户结束");
    }

    
}
