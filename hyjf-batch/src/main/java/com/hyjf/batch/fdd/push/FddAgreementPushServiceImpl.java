package com.hyjf.batch.fdd.push;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther:yangchangwei
 * @Date:2018/12/20
 * @Description:
 */
@Service
public class FddAgreementPushServiceImpl extends BaseServiceImpl implements FddAgreementPushService {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 获取需要推送法大大的标的
     * @return
     */
    @Override
    public List<BorrowApicron> getFddPushBorrowList() {

        BorrowApicronExample example = new BorrowApicronExample();
        example.createCriteria().andApiTypeEqualTo(0).andStatusEqualTo(6).andAgreementStatusEqualTo(0);
        List<BorrowApicron> borrowApicrons = this.borrowApicronMapper.selectByExample(example);
        return borrowApicrons;
    }

    /**
     * 开始推送法大大协议
     * @param borrowApicron
     */
    @Override
    public void updatePushFdd(BorrowApicron borrowApicron) {
        String borrowNid = borrowApicron.getBorrowNid();
        _log.info("----------开始推送放款后的法大大协议，标的号：" + borrowNid + "-------");
        BorrowRecoverExample example = new BorrowRecoverExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<BorrowRecover> borrowRecovers = this.borrowRecoverMapper.selectByExample(example);
        if(borrowRecovers != null && borrowRecovers.size() > 0){
            boolean falg = true;
            for (int i = 0; i < borrowRecovers.size(); i++) {
                BorrowRecover recover = borrowRecovers.get(i);
                boolean isRepeat = getIsRepeat(recover.getNid());
                if(isRepeat){
                    try {
                        Integer userId = recover.getUserId();
                        String signDate = GetDate.getDataString(GetDate.date_sdf);
                        FddGenerateContractBean bean = new FddGenerateContractBean();
                        bean.setTenderUserId(userId);
                        bean.setOrdid(recover.getNid());
                        bean.setTransType(1);
                        bean.setBorrowNid(borrowNid);
                        bean.setSignDate(signDate);
                        bean.setTenderType(0);
                        bean.setTenderInterest(recover.getRecoverInterest());
                        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                    }catch (Exception e){
                        falg = false;
                        _log.info("-----------------生成居间服务协议失败，标的号：" + borrowNid + "ordid:" + recover.getNid() + ",异常信息：" + e.getMessage());
                    }
                }

            }
            if(falg){
                BorrowApicron record = new BorrowApicron();
                record.setId(borrowApicron.getId());
                record.setAgreementStatus(1);
                this.borrowApicronMapper.updateByPrimaryKeySelective(record);
            }

        }
        _log.info("-----------放款推送法大大协议完成，标的号：" + borrowNid + "---------");

    }

    /**
     * 判断该条投资数据是否生成合同
     * @param nid
     * @return true 没有生成 false 已重复
     */
    private boolean getIsRepeat(String nid) {

        TenderAgreementExample example = new TenderAgreementExample();
        example.createCriteria().andTenderNidEqualTo(nid);
        List<TenderAgreement> tenderAgreements = this.tenderAgreementMapper.selectByExample(example);
        if(tenderAgreements != null && tenderAgreements.size() > 0){
            return false;
        }
        return true;
    }
}
