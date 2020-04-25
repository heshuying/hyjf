/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge.fee;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.customize.RechargeFeeCustomize;
import com.hyjf.mybatis.util.mail.MailUtil;

/**
 * 充值手续费账单生成
 * @author 李深强
 */
public class RechargeFeeTask {
   
	
   @Autowired
   private RechargeFeeService rechargeFeeService;
   
   private String emails[] = {"sunjianhua@hyjf.com","duxuezhen@hyjf.com","gaohonggang@hyjf.com"};
   
    /**
     * 
     * 每周生成借款人充值手续费垫付账单
     * @author 李深强
     */
    public void insertRechargeFeeReconciliation() {
        LogUtil.startLog(RechargeFeeTask.class.toString(), "insertRechargeFeeReconciliation");
        System.out.println("--------------充值手续费生成账单start-------------------");
        //查出借款人充值列表
        RechargeFeeCustomize rechargeFeeCustomize = new RechargeFeeCustomize();
        int endTime = GetDate.getNowTime10();
        int startTime = endTime - 7*24*3600 + 1;
        rechargeFeeCustomize.setEndTime(endTime);
        rechargeFeeCustomize.setStartTime(startTime);//一个星期前数据
        List<RechargeFeeCustomize> rechargeFeeList = rechargeFeeService.selectRechargeFeeReconciliationList(rechargeFeeCustomize);
        RechargeFeeCustomize rechargeFee = null;
        if(rechargeFeeList != null && rechargeFeeList.size() > 0){
        	for(int i = 0;i < rechargeFeeList.size();i++){
        		rechargeFee = rechargeFeeList.get(i);
        		//垫付手续费大于0  入库
        		if(rechargeFee.getRechargeFee().compareTo(BigDecimal.ZERO) > 0){
            		rechargeFee.setStartTime(startTime);
            		rechargeFee.setEndTime(endTime);
            		//插入数据库
            		rechargeFeeService.insertRechargeFeeReconciliation(rechargeFee);
        		}
        	}
        }
        System.out.println("--------------充值手续费生成账单end-------------------");
        LogUtil.endLog(RechargeFeeTask.class.toString(), "insertRechargeFeeReconciliation");
    }
   
    /**
     * 
     * 每周生成借款人充值手续费垫付账单
     * @author 李深强
     */
    public void insertRechargeFeeReconciliationHistory() {
        LogUtil.startLog(RechargeFeeTask.class.toString(), "insertRechargeFeeReconciliationHistory");
        System.out.println("--------------充值手续费生成账单start-------------------");
        //开始时间
        int firstBeginTime = Integer.valueOf(GetDate.get10Time("2016-03-15 00:00:00"));
        int firstEndTime = firstBeginTime + 6*24*3600 - 1;
        //间隔时间
        int betweenDays =  GetDate.countDate(GetDate.stringToDate("2016-03-15 00:00:00"), GetDate.stringToDate("2016-07-03 23:59:59"));
        int weeks = betweenDays % 7 == 0? betweenDays / 7 : (betweenDays / 7) + 1;
        //查出借款人充值列表
        RechargeFeeCustomize rechargeFeeCustomize = new RechargeFeeCustomize();
        
        for(int j = 0;j < weeks ;j++){
            int startTime = 0;
            int endTime = 0;
            if(j == 0){
                startTime = firstBeginTime;
                endTime = firstEndTime;
            }else{
            	startTime = firstEndTime + (j-1)*(7*24*3600) + 1;
            	endTime = firstEndTime + j*(7*24*3600);
            }
            rechargeFeeCustomize.setEndTime(endTime);
            rechargeFeeCustomize.setStartTime(startTime);//一个星期前数据
            List<RechargeFeeCustomize> rechargeFeeList = rechargeFeeService.selectRechargeFeeReconciliationList(rechargeFeeCustomize);
            RechargeFeeCustomize rechargeFee = null;
            if(rechargeFeeList != null && rechargeFeeList.size() > 0){
            	for(int i = 0;i < rechargeFeeList.size();i++){
            		rechargeFee = rechargeFeeList.get(i);
            		//垫付手续费大于0  入库
            		if(rechargeFee.getRechargeFee().compareTo(BigDecimal.ZERO) > 0){
                		rechargeFee.setStartTime(startTime);
                		rechargeFee.setEndTime(endTime);
                		//插入数据库
                		rechargeFeeService.insertRechargeFeeReconciliation(rechargeFee);
            		}
            	}
            }
        }

        System.out.println("--------------充值手续费生成账单end-------------------");
        LogUtil.endLog(RechargeFeeTask.class.toString(), "insertRechargeFeeReconciliation");
    }


    
    
    
    /**
     * 账单生成后，若14天内未成功支付，则邮件提醒公司相关人员
	 * 账单生成后，若30天内未成功支付，则邮件提醒公司相关人员
     * 检查、发送邮件
     * @author 李深强
     */
    public void checkSendEmail() {
        LogUtil.startLog(RechargeFeeTask.class.toString(), "checkSendEmail");
        
        System.out.println("--------------发送邮件start-------------------");
        try {
	        List<RechargeFeeReconciliation> delayList = rechargeFeeService.selectFeeListDelay();
	        RechargeFeeReconciliation rechargeFeeReconciliation = null;
	        //当前日期
	        int nowTime = GetDate.getNowTime10();
	        if(delayList != null && delayList.size() > 0){
	        	for(int i=0;i<delayList.size();i++){
	        		rechargeFeeReconciliation = delayList.get(i);
	        		if(rechargeFeeReconciliation.getIsMail() == 0){ //未发送判断14天
	        			int days = GetDate.daysBetween(rechargeFeeReconciliation.getAddTime(), nowTime);
	        			if(days >= 14 ){
	        				//发送邮件 
	    					Map<String, String> replaceMap = new HashMap<String, String>();
	    					replaceMap.put("val_name", rechargeFeeReconciliation.getUserName());
	    					replaceMap.put("val_order", rechargeFeeReconciliation.getRechargeNid());
	    					replaceMap.put("val_time", GetDate.timestamptoStrYYYYMMDD(rechargeFeeReconciliation.getStartTime())+"-"+ GetDate.timestamptoStrYYYYMMDD(rechargeFeeReconciliation.getEndTime()));
	    					replaceMap.put("val_fee", String.valueOf(rechargeFeeReconciliation.getRechargeFee()));
	    					MailUtil.sendMail(emails, "充值手续费对账", CustomConstants.PARAM_RECHARGE_FEE_RECONCILIATION_DELAY14, replaceMap, null);
	        				//更新数据库 发送邮件字段
	        				rechargeFeeReconciliation.setIsMail(1);
	        				this.rechargeFeeService.updateRechargeFeeReconciliation(rechargeFeeReconciliation);
	        			}
	        			
	        		}else if(rechargeFeeReconciliation.getIsMail() == 1){//发送过判断30天
	        			int days = GetDate.daysBetween(rechargeFeeReconciliation.getAddTime(), nowTime);
	        			if(days >= 30 ){
	        				//发送邮件 
	    					Map<String, String> replaceMap = new HashMap<String, String>();
	    					replaceMap.put("val_name", rechargeFeeReconciliation.getUserName());
	    					replaceMap.put("val_order", rechargeFeeReconciliation.getRechargeNid());
	    					replaceMap.put("val_time", GetDate.timestamptoStrYYYYMMDD(rechargeFeeReconciliation.getStartTime())+"-"+ GetDate.timestamptoStrYYYYMMDD(rechargeFeeReconciliation.getEndTime()));
	    					replaceMap.put("val_fee", String.valueOf(rechargeFeeReconciliation.getRechargeFee()));
	    					MailUtil.sendMail(emails, "充值手续费对账", CustomConstants.PARAM_RECHARGE_FEE_RECONCILIATION_DELAY30, replaceMap, null);
	        				//更新数据库 发送邮件字段
	        				rechargeFeeReconciliation.setIsMail(2);
	        				this.rechargeFeeService.updateRechargeFeeReconciliation(rechargeFeeReconciliation);
	        			}
	        		}
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("--------------发送邮件end-------------------");
        LogUtil.endLog(RechargeFeeTask.class.toString(), "checkSendEmail");
    }
 
    /**
     * 转账记录  转账中数据 超过4小时更新为已过期
     * @author 李深强
     */
    public void checkTransferExpiredRecord() {
        LogUtil.startLog(RechargeFeeTask.class.toString(), "checkTransferExpiredRecord");
        try {
	        List<UserTransfer> delayList = rechargeFeeService.selectTransferingRecord();
	        UserTransfer userTransfer = null;
	        //4小时
	        int fourHoursTime = 60*60*4;
	        //当前日期
	        int nowTime = GetDate.getNowTime10();
	        if(delayList != null && delayList.size() > 0){
	        	for(int i=0;i<delayList.size();i++){
	        		userTransfer = delayList.get(i);
	        		//当前时间减去转账时间大于4小时
	        		if(nowTime - (userTransfer.getTransferTime().getTime()/1000) > fourHoursTime){
	        			rechargeFeeService.updateTransferRecord(userTransfer);
	        		}
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        LogUtil.endLog(RechargeFeeTask.class.toString(), "checkTransferExpiredRecord");
    }
 
}
