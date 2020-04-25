package com.hyjf.batch.activity.billion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.CalculateInvestInterest;

/**
 * 十一月份活动  满心满亿
 * @author Michael
 */
public class BillionTask {

    @Autowired
    BillionOneService billionOneService;
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    
    private static BigDecimal billion100 = new BigDecimal("10000000000");
    private static BigDecimal billion101 = new BigDecimal("10100000000");
    private static BigDecimal billion102 = new BigDecimal("10200000000");
    private static BigDecimal billion103 = new BigDecimal("10300000000");
    private static BigDecimal billion104 = new BigDecimal("10400000000");
    private static BigDecimal billion105 = new BigDecimal("10500000000");
    
    private static Boolean flag100 = true;
    private static Boolean flag101 = true;
    private static Boolean flag102 = true;
    private static Boolean flag103 = true;
    private static Boolean flag104 = true;
    private static Boolean flag105 = true;
    
    
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
    	try {
        	CalculateInvestInterest record = billionOneService.getCalculateRecord();
        	if(record == null){
        		return;
        	}
        	//小于100亿
        	if(record.getTenderSum().compareTo(billion100) < 0 ){
        		return;										 
        	}
        	BigDecimal amount = record.getTenderSum();
        	
        	int num = 0;
        	if(amount.compareTo(billion101) < 0 ){
        		num = 1;							 
        	}else if(amount.compareTo(billion101) >= 0 && amount.compareTo(billion102) < 0){
        		num = 2;
        	}else if(amount.compareTo(billion102) >= 0 && amount.compareTo(billion103) < 0){
        		num = 3;
        	}else if(amount.compareTo(billion103) >= 0 && amount.compareTo(billion104) < 0){
        		num = 4;
        	}else if(amount.compareTo(billion104) >= 0 && amount.compareTo(billion105) < 0){
        		num = 5;
        	}else if(amount.compareTo(billion105) >= 0){
        		num = 6;
        	}
        	//生成数据
        	switch (num) {
    		case 1:
    			if(flag100){
    		    	billionOneService.prizeGenerate(num, record.getCreateTime());
    		    	flag100 = false;
    			}
    			break;
    		case 2:
    			if(flag101){
    		    	billionOneService.prizeGenerate(num, record.getCreateTime());
    		    	flag101 = false;
    			}
    			break;
    		case 3:
    			if(flag102){
    		    	billionOneService.prizeGenerate(num, record.getCreateTime());
    		    	flag102 = false;
    			}
    			break;
    		case 4:
    			if(flag103){
    		    	billionOneService.prizeGenerate(num, record.getCreateTime());
    		    	flag103 = false;
    			}
    			break;
    		case 5:
    			if(flag104){
    		    	billionOneService.prizeGenerate(num, record.getCreateTime());
    		    	flag104 = false;
    			}
    			break;
    		case 6:
    			if(flag105){
    		    	billionOneService.prizeGenerate(num, record.getCreateTime());
    		    	flag105 = false;
    			}
    			break;
    		default:
    			break;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
}
