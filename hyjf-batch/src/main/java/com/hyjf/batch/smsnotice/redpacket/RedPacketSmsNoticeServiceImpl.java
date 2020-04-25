package com.hyjf.batch.smsnotice.redpacket;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

/**
 * 
 * 红包账户余额短信提醒
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月22日
 * @see 上午11:43:23
 */
@Service
public class RedPacketSmsNoticeServiceImpl extends BaseServiceImpl implements RedPacketSmsNoticeService {

    Logger _log = LoggerFactory.getLogger(RedPacketSmsNoticeServiceImpl.class);
    
    @Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
    
    /**
     * 查询红包账户余额并发送短信通知
     */
	@Override
	public void queryAndSend() {
		String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
		
		if(StringUtils.isEmpty(merrpAccount)){
			_log.error("【红包账户余额短信提醒】红包账户电子账号没有配置");
			return;
		}
		
		BigDecimal balance = this.getBankBalance(0, merrpAccount);
		if(balance == null){
			_log.error("【红包账户余额短信提醒】调用即信余额查询接口失败");
			return;
		}
		
		_log.info("【红包账户余额短信提醒】当前账户余额：" + balance);
		sendSms(balance);
		
		
	}
	
	/**
	 * 发送短信
	 * 
	 * @param userId
	 */
	private void sendSms(BigDecimal balance) {
		// 替换参数
		Map<String, String> messageStrMap = new HashMap<String, String>();
		messageStrMap.put("val_amount", balance.toString());
		// 发送短信
		SmsMessage smsMessage = new SmsMessage(null, messageStrMap, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_RED_PACKET,
				CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
	}

    
}
