package com.hyjf.batch.smsnotice.redpacket;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.batch.BaseService;
import com.hyjf.common.util.PropUtils;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

public interface RedPacketSmsNoticeService extends BaseService {

	void queryAndSend();

}
