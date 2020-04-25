/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transferstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;
import com.rabbitmq.client.Channel;

/**
 * @Description 合规数据上报 CERT 历史数据转让项目状态推送上报（延时队列）
 * @Author pcc
 * @Date 2018/11/26 17:57
 */
public class OldCertTransferStatusMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(OldCertTransferStatusMessageHadnle.class);

    private String thisMessName = "历史数据转让状态信息";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";


    @Autowired
    private OldCertTransferStatusService oldCertTransferStatusService;
    

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
    	Integer now=GetDate.getMyTimeInMillis();
    	logger.info(logHeader + " 开始。"+now);
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            logger.error(logHeader + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        logger.info(logHeader + "接收到的消息：" + msgBody);
        // --> 消息处理
        try {
        	for (int i = 1; i < 4; i++) {
        		logger.info(logHeader + "循环第：" + i + "次");
        		// --> 调用service组装数据
            	List<CertBorrow> certBorrowEntityList = oldCertTransferStatusService.insertCertBorrowEntityList();
                if(null!=certBorrowEntityList&&certBorrowEntityList.size()>0){
                    Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
                    for (CertBorrow certBorrow : certBorrowEntityList) {
                    	List<Map<String, Object>> list=oldCertTransferStatusService.createList(certBorrow.getBorrowNid());
                    	map.put(certBorrow.getBorrowNid(), list);
    				}
                    logger.info(logHeader + " 组装数据完成。"+(GetDate.getMyTimeInMillis()-now));
                    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
                    List<Integer> successIds = new ArrayList<>();
                    List<Integer> errorIds = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    for (CertBorrow certBorrow : certBorrowEntityList) {
                    	List<Map<String, Object>> entityList=map.get(certBorrow.getBorrowNid());
                    	if(entityList==null){
                    		successIds.add(certBorrow.getId());
                    		continue;
                    	}
                    	if((list.size()+entityList.size())>3000){
                    		JSONArray data =JSONArray.parseArray(JSON.toJSONString(list));
                    		List<CertReportEntity> entitys = CertCallUtil.groupByDate(data,thisMessName,CertCallConstant.CERT_INF_TYPE_TRANSFER_STATUS);
                    		for (CertReportEntity certReportEntity : entitys) {
                    			 try {
                                 	oldCertTransferStatusService.insertAndSendPost(certReportEntity);
                                     // 更新 is_transfer 将是否上送债权信息 赋值1
                                     // 批量修改状态  start
                                     if (certReportEntity != null && CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(certReportEntity.getReportStatus())) {
                                         // 成功
                                     	successIds.addAll(ids);
                                     } else {
                                         // 失败
                                     	errorIds.addAll(ids);
                                     }
                                     // 批量修改状态  end
                                 } catch (Exception e) {
                                     throw e;
                                 }
    						}
                            //上报完成后清空数据重新写入
                    		list=new ArrayList<Map<String,Object>>();
                            ids = new ArrayList<>();
                            list.addAll(entityList);
                    		ids.add(certBorrow.getId());
                    	}else{
                    		list.addAll(entityList);
                    		ids.add(certBorrow.getId());
                    	}
                    	
    				}
                    if(list!=null&&list.size()>0){
                		JSONArray data =JSONArray.parseArray(JSON.toJSONString(list));
                		List<CertReportEntity> entitys = CertCallUtil.groupByDate(data,thisMessName,CertCallConstant.CERT_INF_TYPE_TRANSFER_STATUS);
                		for (CertReportEntity certReportEntity : entitys) {
                			 try {
                             	oldCertTransferStatusService.insertAndSendPost(certReportEntity);
                                 // 更新 is_transfer 将是否上送债权信息 赋值1
                                 // 批量修改状态  start
                                 if (certReportEntity != null && CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(certReportEntity.getReportStatus())) {
                                     // 成功
                                 	successIds.addAll(ids);
                                 } else {
                                     // 失败
                                 	errorIds.addAll(ids);
                                 }
                                 // 批量修改状态  end
                             } catch (Exception e) {
                                 throw e;
                             }
    					}
                	}
                    // 批量修改
                    if (successIds.size() > 0) {
                        CertBorrowUpdate update = new CertBorrowUpdate();
                        update.setIds(successIds);
                        CertBorrow certBorrow = new CertBorrow();
                        certBorrow.setIsTransferStatus(1);
                        update.setCertBorrow(certBorrow);
                        // 批量修改
                        oldCertTransferStatusService.updateCertBorrowStatusBatch(update);
                    }
                    if (errorIds.size() > 0) {
                        CertBorrowUpdate update = new CertBorrowUpdate();
                        update.setIds(errorIds);
                        CertBorrow certBorrow = new CertBorrow();
                        // 失败
                        certBorrow.setIsTransferStatus(99);
                        update.setCertBorrow(certBorrow);
                        // 批量修改
                        oldCertTransferStatusService.updateCertBorrowStatusBatch(update);
                    }

                }
			}
            
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。"+(GetDate.getMyTimeInMillis()-now));
        }
       
    }
}
