/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.messagehandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;

/**
 * 信息处理程序
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月20日
 * @see 下午1:50:36
 */
@Component
public class MessageHandler {

    public static int timeout = 30;// 阻塞时间

    Logger _log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;// 短信处理器

    @Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailProcesser;// 邮件处理器

    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;// app消息处理器

    private SmsTread smsTread;

    private MailTread mailTread;

    private AppMsTread appMsTread;

    @PostConstruct
    public void Init() {

        if (!RedisUtils.exists(MessageDefine.MAILQUENEM)) {
            RedisUtils.lpush(MessageDefine.MAILQUENEM, "");
        }

        if (!RedisUtils.exists(MessageDefine.MAILQUENES)) {
            RedisUtils.lpush(MessageDefine.MAILQUENES, "");
        }

        if (!RedisUtils.exists(MessageDefine.SMSQUENEM)) {
            RedisUtils.lpush(MessageDefine.SMSQUENEM, "");
        }

        if (!RedisUtils.exists(MessageDefine.SMSQUENES)) {
            RedisUtils.lpush(MessageDefine.SMSQUENES, "");
        }
        if (!RedisUtils.exists(MessageDefine.APPMSQUENEM)) {
            RedisUtils.lpush(MessageDefine.APPMSQUENEM, "");
        }

        if (!RedisUtils.exists(MessageDefine.APPMSQUENES)) {
            RedisUtils.lpush(MessageDefine.APPMSQUENES, "");
        }
        if (smsTread == null) {
            smsTread = new SmsTread();
            smsTread.start();
        }

        if (mailTread == null) {
            mailTread = new MailTread();
            mailTread.start();
        }

        if (appMsTread == null) {
            appMsTread = new AppMsTread();
            appMsTread.start();
        }
    }

    @PreDestroy
    public void Destory() {
        if (null != smsTread && !smsTread.isInterrupted()) {
            smsTread.interrupt();
        }
        if (null != mailTread && !mailTread.isInterrupted()) {
            mailTread.interrupt();
        }

        if (null != appMsTread && !appMsTread.isInterrupted()) {
            appMsTread.interrupt();
        }
    }

    /**
     * 
     * 短信发送初始方法，跟随batch应用启动
     * @author renxingchen
     */
    public void smsSend() {
        try {
            // 检测副短信队列，是否有未发送的信息
            Long num = RedisUtils.llen(MessageDefine.SMSQUENES);
            _log.info("-------------------------cwyang 开始发送给短信,待发送数目:" + num + "--------");
            SmsMessage smsMessage;
            if (num > 0) {
                String valueTemp;
                for (int i = 0; i < num; i++) {
                    valueTemp = RedisUtils.rpop(MessageDefine.SMSQUENES);
                    _log.info("-------------------------cwyang 开始发送给短信,信息内容:" + valueTemp + "--------");
                    System.err.println("valueTemp++++++++++++++++++++++++++++" + valueTemp);
                    if (StringUtils.isNotBlank(valueTemp)) {
                        // 反序列化成短信对象
                        smsMessage = JSON.parseObject(valueTemp, SmsMessage.class);
                        try {
                            smsProcesser.send(smsMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            } else {// 尝试从主短信队列，获取短信进行发送
                String value = RedisUtils.brpoplpush(MessageDefine.SMSQUENEM, MessageDefine.SMSQUENES, timeout);
                if (StringUtils.isNotBlank(value)) {
                	_log.info("-------------------------cwyang 开始发送给短信,主短信内容给:" + value + "--------");
                    System.err.println("value++++++++++++++++++++++++++++" + value);
                    // 反序列化成短信对象
                    smsMessage = JSON.parseObject(value, SmsMessage.class);
                    boolean result = smsProcesser.send(smsMessage);
                    _log.info("result++++++++++++++++++++++++++++" + result);
                    if (result) {// 发送短信成功 从副队列删除数据
                        _log.info("删除副队列数据 result :" + result);
                        RedisUtils.lrem(MessageDefine.SMSQUENES, -1, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 邮件发送初始方法，跟随batch应用启动
     * @author renxingchen
     */
    public void mailSend() {
        try {
            // 检测副邮件队列，是否有未发送的信息
            Long num = RedisUtils.llen(MessageDefine.MAILQUENES);
            MailMessage mailMessage;
            if (num > 0) {
                String valueTemp;
                for (int i = 0; i < num; i++) {
                    valueTemp = RedisUtils.rpop(MessageDefine.MAILQUENES);
                    if (StringUtils.isNotBlank(valueTemp)) {
                        // 反序列化成短信对象
                        mailMessage = JSON.parseObject(valueTemp, MailMessage.class);
                        try {
                            mailProcesser.send(mailMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            } else {// 尝试从主邮件队列，获取邮件并进行发送
                String value = RedisUtils.brpoplpush(MessageDefine.MAILQUENEM, MessageDefine.MAILQUENES, timeout);
                if (StringUtils.isNotBlank(value)) {
                    // 反序列化成邮件对象
                    mailMessage = JSON.parseObject(value, MailMessage.class);
                    System.out.println(value+"++++++++++++++++++++++++++++++++++++++++++");
                    mailProcesser.send(mailMessage);
                    // 从副队列删除数据
                    RedisUtils.lrem(MessageDefine.MAILQUENES, -1, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * app消息推送初始方法
     * @author renxingchen
     */
    public void appMsSend() {
        try {
            // 检测副短信队列，是否有未发送的信息
            Long num = RedisUtils.llen(MessageDefine.APPMSQUENES);
            AppMsMessage appMsMessage;
            if (num > 0) {
                String valueTemp;
                for (int i = 0; i < num; i++) {
                    valueTemp = RedisUtils.rpop(MessageDefine.APPMSQUENES);
                    if (StringUtils.isNotBlank(valueTemp)) {
                        // 反序列化成短信对象
                        appMsMessage = JSON.parseObject(valueTemp, AppMsMessage.class);
                        try {
                            appMsProcesser.send(appMsMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            } else {// 尝试从主短信队列，获取短信进行发送
                String value = RedisUtils.brpoplpush(MessageDefine.APPMSQUENEM, MessageDefine.APPMSQUENES, timeout);
                if (StringUtils.isNotBlank(value)) {
                    // 反序列化成短信对象
                    appMsMessage = JSON.parseObject(value, AppMsMessage.class);
                    if (appMsProcesser.send(appMsMessage)) {// 发送短信成功 从副队列删除数据
                        RedisUtils.lrem(MessageDefine.APPMSQUENES, -1, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SmsTread extends Thread {
        public void run() {
            while (!this.isInterrupted()) {// 线程未中断执行循环
                smsSend();
            }
        }
    }

    class MailTread extends Thread {
        public void run() {
            while (!this.isInterrupted()) {// 线程未中断执行循环
                mailSend();
            }
        }
    }

    class AppMsTread extends Thread {
        public void run() {
            while (!this.isInterrupted()) {
                appMsSend();
            }
        }
    }

}
