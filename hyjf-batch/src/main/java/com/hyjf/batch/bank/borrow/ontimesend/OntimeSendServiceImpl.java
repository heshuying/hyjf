package com.hyjf.batch.bank.borrow.ontimesend;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

@Service
public class OntimeSendServiceImpl extends BaseServiceImpl implements OntimeSendService {

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    Logger _log = LoggerFactory.getLogger(OntimeSendServiceImpl.class);
    
    /**
     * 资金明细（列表）
     *
     * @param accountManageBean
     * @return
     */
    @Override
    public List<Borrow> selectOntimeSendBorrowList() {
        int onTime = GetDate.getNowTime10();
        List<Borrow> list = this.ontimeTenderCustomizeMapper.queryOntimeTenderList(onTime);
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
    public boolean updateOntimeSendBorrow(int borrowId) {

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
        // 剩余可出借金额
        borrow.setBorrowAccountWait(borrow.getAccount());
        boolean flag = this.borrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
        if (flag) {
            RedisUtils.del(borrow.getBorrowNid() + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME);
            // 写入redis
            RedisUtils.set(borrow.getBorrowNid(), borrow.getBorrowAccountWait().toString());
            // 发送发标短信
            Map<String, String> params = new HashMap<String, String>();
            params.put("val_title", borrow.getBorrowNid());
            SmsMessage smsMessage = new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_DSFB, CustomConstants.CHANNEL_TYPE_NORMAL);
            smsProcesser.gather(smsMessage);
            return true;
        } else {
            return false;
        }
    }

}
