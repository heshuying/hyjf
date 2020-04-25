package com.hyjf.batch.hjh.borrow.ontimesend;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

@Service
public class HjhOntimeSendServiceImpl extends BaseServiceImpl implements HjhOntimeSendService {

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;
    
    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    Logger _log = LoggerFactory.getLogger(HjhOntimeSendServiceImpl.class);
    
    /**
     * 资金明细（列表）
     *
     * @param accountManageBean
     * @return
     */
    @Override
    public List<Borrow> selectHjhOntimeSendBorrowList() {
        int onTime = GetDate.getNowTime10();
        List<Borrow> list = this.ontimeTenderCustomizeMapper.queryHjhOntimeTenderList(onTime);
        return list;
    }

    /**
     * 发标
     *
     * @param borrow
     * @return
     * @author Administrator
     */

    @Override
    public boolean updateHjhOntimeSendBorrow(int borrowId) {

        // 当前时间
        int nowTime = GetDate.getNowTime10();
        BorrowWithBLOBs borrow = this.borrowMapper.selectByPrimaryKey(borrowId);
        // DB验证
        // 有出借金额发生异常
        BigDecimal zero = new BigDecimal("0");
        BigDecimal borrowAccountYes = borrow.getBorrowAccountYes();
        String borrowNid = borrow.getBorrowNid();
        if (!(borrowAccountYes == null || borrowAccountYes.compareTo(zero) == 0)) {
            _log.error(borrowNid + " 定时发标异常：标的已有出借人出借");
            return false;
        }
        
        borrow.setBorrowEndTime(String.valueOf(nowTime + borrow.getBorrowValidTime() * 86400));
        // 是否可以进行借款
        borrow.setBorrowStatus(1);
        // 是否可以进行借款
        borrow.setBorrowFullStatus(0);
        // 状态
        borrow.setStatus(2);
        // 初审时间
        borrow.setVerifyTime(String.valueOf(nowTime));
        // 0未交保证金 1已交保证金 2暂不发布 3定时发标 4立即发标（立即发标状态的标的才能进汇计划）
        borrow.setVerifyStatus(4);
        // 剩余可出借金额
        borrow.setBorrowAccountWait(borrow.getAccount());
        return this.borrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
    }

    @Override
    public void sendToMQ(Borrow borrow,String routingKey){
 		// 加入到消息队列 
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("borrowNid", borrow.getBorrowNid());
        params.put("instCode", borrow.getInstCode());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
 	}
    
    @Override
    public boolean updatePlanAsset(String borrowNid) {
		// 三方资产更新资产表状态
		HjhPlanAsset hjhPlanAssetnew = this.getHjhPlanAsset(borrowNid);
		if (null == hjhPlanAssetnew) {
		    return false;
        }
		// 受托支付，更新为待授权
		hjhPlanAssetnew.setStatus(7);//7 出借中
		//获取当前时间
		int nowTime = GetDate.getNowTime10();
		hjhPlanAssetnew.setUpdateTime(nowTime);
		hjhPlanAssetnew.setUpdateUserId(1);
		return this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew) >0 ?true:false;
    }
}
