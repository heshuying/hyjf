package com.hyjf.batch.fdd;

import com.hyjf.bank.service.fdd.fddcertificateauthority.FddCertificateAuthorityBean;
import com.hyjf.bank.service.fdd.fddcertificateauthority.FddCertificateAuthorityService;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 法大大电子签章批量做CA认证
 *
 * @author liuyang
 */
public class FddCertificateAuthorityTask {

    @Autowired
    private BatchFddCertificateAuthorityService fddCertificateAuthorityService;


    @Autowired
    private FddCertificateAuthorityService caService;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static boolean isOver = true;

    Logger _log = LoggerFactory.getLogger(FddCertificateAuthorityTask.class);

    public void run() {
        if (isOver) {
            _log.info("------用户批量CA认证定时任务开始------");
            isOver = false;
            try {
                List<Users> usersList = this.fddCertificateAuthorityService.selectCAUsersList();
                if (usersList != null && usersList.size() > 0) {
                    _log.info("用户批量CA认证数:[" + usersList.size() + "].");
                    String startTime = GetDate.dateToString(new Date());
                    _log.info("处理开始时间:" + startTime);
                    // 循环去做CA认证
                    for (Users users : usersList) {
                        // 将用户ID加入CA认证消息队列
                        FddCertificateAuthorityBean bean = new FddCertificateAuthorityBean();
                        bean.setUserId(users.getUserId());
                        // this.caService.updateUserCAInfo(users.getUserId());
                        this.fddCertificateAuthorityService.sendToMQ(bean, RabbitMQConstants.ROUTINGKEY_CERTIFICATE_AUTHORITY);
                    }
                    // 处理结束时间
                    String endTime = GetDate.dateToString(new Date());
                    // 处理用时
                    String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
                    _log.info("处理结束时间:" + endTime);
                    _log.info("处理用时:" + startTime + "减去" + endTime + "等于" + consumeTime);
                }
                _log.info("------用户批量CA认证定时任务结束------");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isOver = true;
            }
        }
    }
}
