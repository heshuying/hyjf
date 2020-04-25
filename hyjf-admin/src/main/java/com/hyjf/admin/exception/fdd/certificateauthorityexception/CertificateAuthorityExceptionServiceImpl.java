package com.hyjf.admin.exception.fdd.certificateauthorityexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.CertificateAuthorityExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CA认证异常Service实现类
 *
 * @author liuyang
 */
@Service
public class CertificateAuthorityExceptionServiceImpl extends BaseServiceImpl implements CertificateAuthorityExceptionService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 检索CA异常件数
     *
     * @param form
     * @return
     */
    @Override
    public Integer countCAExceptionList(CertificateAuthorityExceptionBean form) {

        CertificateAuthorityExample example = new CertificateAuthorityExample();
        CertificateAuthorityExample.Criteria cra = example.createCriteria();
        // 用户名不为空
        if (StringUtils.isNotBlank(form.getUserNameSrch())) {
            cra.andUserNameLike("%" + form.getUserNameSrch() + "%");
        }
        // 用户手机号
        if (StringUtils.isNotBlank(form.getMobileSrch())) {
            cra.andMobileLike("%" + form.getMobileSrch() + "%");
        }
        // 姓名
        if (StringUtils.isNotBlank(form.getTrueNameSrch())) {
            cra.andTrueNameLike("%" + form.getTrueNameSrch() + "%");
        }
        // 检索条件转账时间开始
        if (StringUtils.isNotBlank(form.getStartTimeSrch())) {
            cra.andCreateTimeGreaterThanOrEqualTo(GetDate.dateString2Timestamp(form.getStartTimeSrch() + " 00:00:00"));
        }
        // 检索条件转账时间结束
        if (StringUtils.isNotBlank(form.getEndTimeSrch())) {
            cra.andCreateTimeLessThanOrEqualTo(GetDate.dateString2Timestamp(form.getEndTimeSrch() + " 23:59:59"));
        }

        cra.andCodeNotEqualTo("1000");
        return this.certificateAuthorityMapper.countByExample(example);
    }


    /**
     * 检索CA异常列表
     *
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    @Override
    public List<CertificateAuthority> getCAExceptionList(CertificateAuthorityExceptionBean form, int limitStart, int limitEnd) {

        CertificateAuthorityExample example = new CertificateAuthorityExample();
        CertificateAuthorityExample.Criteria cra = example.createCriteria();
        // 用户名不为空
        if (StringUtils.isNotBlank(form.getUserNameSrch())) {
            cra.andUserNameLike("%" + form.getUserNameSrch() + "%");
        }
        // 用户手机号
        if (StringUtils.isNotBlank(form.getMobileSrch())) {
            cra.andMobileLike("%" + form.getMobileSrch() + "%");
        }
        // 姓名
        if (StringUtils.isNotBlank(form.getTrueNameSrch())) {
            cra.andTrueNameLike("%" + form.getTrueNameSrch() + "%");
        }
        // 检索条件转账时间开始
        if (StringUtils.isNotBlank(form.getStartTimeSrch())) {
            cra.andCreateTimeGreaterThanOrEqualTo(GetDate.dateString2Timestamp(form.getStartTimeSrch() + " 00:00:00"));
        }
        // 检索条件转账时间结束
        if (StringUtils.isNotBlank(form.getEndTimeSrch())) {
            cra.andCreateTimeLessThanOrEqualTo(GetDate.dateString2Timestamp(form.getEndTimeSrch() + " 23:59:59"));
        }
        cra.andCodeNotEqualTo("1000");
        if (limitStart >= 0) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        return this.certificateAuthorityMapper.selectByExample(example);
    }

    /**
     * 发送CA认证MQ
     * @param userId
     */
    @Override
    public void updateUserCAMQ(String userId) {
        // add by liuyang 20180209 开户成功后,将用户ID加入到CA认证消息队列 start
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("userId", userId);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_CERTIFICATE_AUTHORITY, JSONObject.toJSONString(params));
        // add by liuyang 20180209 开户成功后,将用户ID加入到CA认证消息队列 end
    }
}
