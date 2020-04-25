package com.hyjf.batch.hjh.notice.sms;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 标的还款逾期短信提醒
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年8月15日
 * @see 5:00:27
 */
@Service
public class HjhSmsNoticeServiceImpl extends BaseServiceImpl implements HjhSmsNoticeService {
    
    Logger _log = LoggerFactory.getLogger(HjhSmsNoticeServiceImpl.class);

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    
    /**
     * 
     * 检索逾期的还款标的
     * @author hsy
     * @return
     */
    @Override
    public List<Borrow> selectOverdueBorrowList() {
    	BorrowExample example = new BorrowExample();
    	example.createCriteria().andRepayLastTimeLessThanOrEqualTo(String.valueOf(GetDate.getDayEnd10(GetDate.getTodayBeforeOrAfter(-1)))).andStatusEqualTo(4).andPlanNidIsNull();
    	List<Borrow> borrow = borrowMapper.selectByExample(example);
    	
    	if(borrow == null){
    		return new ArrayList<Borrow>();
    	}
    	
    	return borrow;
    }
    
    /**
     * 
     * 加入短信通知队列
     * @author hsy
     * @param borrowNid
     */
    @Override
    public void sendSmsForManager(String borrowNid, Integer userId) {
        // 管理员发送成功短信
        Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("val_title", borrowNid);
        SmsMessage smsMessage =
                new SmsMessage(userId, replaceStrs, null, null, MessageDefine.SMSSENDFORUSER, null,
                		CustomConstants.PARAM_TPL_NOTICE_BORROW_REPAY_OVERDUE, CustomConstants.CHANNEL_TYPE_NORMAL);
       smsProcesser.gather(smsMessage);
    }
    
    public static void main(String[] args) {
		System.out.println(GetDate.getDayEnd10(GetDate.getTodayBeforeOrAfter(-1)));
	}
    
}
