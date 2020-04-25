package com.hyjf.batch.borrow.credit;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.batch.borrow.loans.BorrowLoansService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债转有效定时任务
 * 
 * @author Michael
 */
public class BorrowCreditTask {
    @Autowired
    private MqService mqService;

	@Autowired
	private BorrowCreditService borrowCreditService;

    @Autowired
    BorrowLoansService borrowLoansService;
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;  

	public void creditExpiresTask(){
		System.out.println("----------------债转有效定时任务-------------");
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditStatusEqualTo(0);
        List<BorrowCredit> borrowCreditList = borrowCreditService.selectBorrowCreditList(borrowCreditExample);
        if(borrowCreditList!=null && borrowCreditList.size()>0){
            for(BorrowCredit borrowCredit : borrowCreditList){
                Integer nowTime = GetDate.getNowTime10();
                Integer creditAddTime = borrowCredit.getAddTime();
                Integer timeDifference = (nowTime-creditAddTime)/3600;
                if(timeDifference>=72){
                    borrowCredit.setCreditStatus(1);
                    borrowCreditService.updateBorrowCredit(borrowCredit);
                    
                    // 如果需要短信
                    UsersInfo usersinfo = this.borrowLoansService.getUsersInfoByUserId(borrowCredit.getCreditUserId());
                    if (usersinfo!=null) {
                        Map<String, String> param = new HashMap<String, String>();
                        if (usersinfo.getTruename()!=null&&usersinfo.getTruename().length()>1) {
                            param.put("val_name",usersinfo.getTruename().substring(0,1));
                        }else {
                            param.put("val_name", usersinfo.getTruename());
                        }
                        if (usersinfo.getSex()==1) {
                            param.put("val_sex", "先生");
                        }else if (usersinfo.getSex()==2) {
                            param.put("val_sex", "女士");
                        }else {
                            param.put("val_sex", "");
                        }
                        param.put("val_amount", borrowCredit.getCreditCapitalAssigned()+"");
                        param.put("val_profit", borrowCredit.getCreditInterestAdvance()+"");
                        if (borrowCredit.getCreditCapitalAssigned()!=null&&borrowCredit.getCreditCapitalAssigned().longValue()>0) {
                            // add 合规数据上报 埋点 liubin 20181122 start
                            // 推送数据到MQ 承接（完全）散
                            JSONObject params = new JSONObject();
                            params.put("creditNid", borrowCredit.getCreditNid()+"");
                            params.put("flag", "1"); //1（散）2（智投）
                            params.put("status", "2"); //2承接（成功）
                            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
                            // add 合规数据上报 埋点 liubin 20181122 end

                            // 发送短信验证码
                        	  SmsMessage smsMessage =
                                      new SmsMessage(borrowCredit.getCreditUserId(), param, null, null, MessageDefine.SMSSENDFORUSER, null,
                                      		CustomConstants.PARAM_TPL_ZZBFZRCG, CustomConstants.CHANNEL_TYPE_NORMAL);
                             smsProcesser.gather(smsMessage);
                            System.out.println("债转部分转让发送短信userid:"+borrowCredit.getCreditUserId()+",truename:"+usersinfo.getTruename());
                        }else {
                            // add 合规数据上报 埋点 liubin 20181122 start
                            //停止债转并且没有被承接过
                            JSONObject params = new JSONObject();
                            params.put("creditNid", borrowCredit.getCreditNid()+"");
                            params.put("flag", "1");//1（散）2（智投）
                            params.put("status", "3"); //3承接（失败）
                            // 推送数据到MQ 承接（失败）散
                            mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_FAIL_DELAY_KEY, JSONObject.toJSONString(params));
                            // add 合规数据上报 埋点 liubin 20181122 end

                        	   param.put("val_amount", "0");
                               param.put("val_profit", "0");
                         // 发送短信验证码
                     	   SmsMessage smsMessage =
                                   new SmsMessage(borrowCredit.getCreditUserId(), param, null, null, MessageDefine.SMSSENDFORUSER, null,
                                   		CustomConstants.PARAM_TPL_ZZDQ, CustomConstants.CHANNEL_TYPE_NORMAL);
                          smsProcesser.gather(smsMessage);
                            System.out.println("债转0转让发送短信userid:"+borrowCredit.getCreditUserId()+",truename:"+usersinfo.getTruename());
                        }
                        AppMsMessage appMsMessage = new AppMsMessage(borrowCredit.getCreditUserId(), param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_ZHUANRANGJIESHU);
                        appMsProcesser.gather(appMsMessage);
                        System.out.println("债转转让结束发送消息userid:"+borrowCredit.getCreditUserId()+",truename:"+usersinfo.getTruename());
                    }
                }
            }
        }
	}
	
	public void creditTenderLogExpiresTask(){
	        System.out.println("----------------债转提交与汇付交易有效定时任务-------------");
	        Integer nowTime = GetDate.getNowTime10();
	        CreditTenderLogExample creditTenderLogExample = new CreditTenderLogExample();
	        CreditTenderLogExample.Criteria creditTenderLogCra = creditTenderLogExample.createCriteria();
	        creditTenderLogCra.andStatusEqualTo(0);
	        creditTenderLogCra.andAddTimeBetween(String.valueOf(nowTime-12600), String.valueOf(nowTime-5400));
	        List<CreditTenderLog> creditTenderLogList = borrowCreditService.selectCreditTenderLogList(creditTenderLogExample);
	        if(creditTenderLogList!=null && creditTenderLogList.size()>0){
	            for(CreditTenderLog creditTenderLog : creditTenderLogList){
	                CreditTenderExample creditTenderExample = new CreditTenderExample();
	                CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
	                creditTenderCra.andUserIdEqualTo(creditTenderLog.getUserId()).andBidNidEqualTo(creditTenderLog.getBidNid())
	                .andCreditNidEqualTo(creditTenderLog.getCreditNid()).andCreditTenderNidEqualTo(creditTenderLog.getCreditTenderNid()).andAssignNidEqualTo(creditTenderLog.getAssignNid());
	                List<CreditTender> creditTenderList = borrowCreditService.selectCreditTenderList(creditTenderExample);
	                if(creditTenderList==null || creditTenderList.size()<=0){
	                    creditTenderLog.setStatus(1);
	                    borrowCreditService.updateCreditTenderLog(creditTenderLog);
	                }
	            }
	        }
	    }
}
