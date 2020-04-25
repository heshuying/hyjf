package com.hyjf.batch.fdd;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddcertificateauthority.FddCertificateAuthorityBean;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 法大大批量做CA认证处理Service实现类
 *
 * @author liuyang
 */
@Service
public class BatchFddCertificateAuthorityServiceImpl extends BaseServiceImpl implements BatchFddCertificateAuthorityService {

    Logger _log = LoggerFactory.getLogger(BatchFddCertificateAuthorityServiceImpl.class);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 检索已开户未做CA认证的用户
     *
     * @return
     */
    @Override
    public List<Users> selectCAUsersList() {
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria cra = usersExample.createCriteria();
        cra.andBankOpenAccountEqualTo(1);
        cra.andIsCaFlagEqualTo(0);
        cra.andRegTimeGreaterThanOrEqualTo(1520956800);
        //cra.andRegTimeLessThanOrEqualTo(1520956800);
        return this.usersMapper.selectByExample(usersExample);
    }

    /**
     * 发送消息队列
     * @param bean
     * @param routingkeyCertificateAuthority
     */
    @Override
    public void sendToMQ(FddCertificateAuthorityBean bean, String routingkeyCertificateAuthority) {
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("userId", String.valueOf(bean.getUserId()));
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingkeyCertificateAuthority, JSONObject.toJSONString(params));
    }
}
