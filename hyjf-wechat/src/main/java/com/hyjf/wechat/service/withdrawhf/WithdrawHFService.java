package com.hyjf.wechat.service.withdrawhf;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.wechat.base.BaseService;

/**
 * 
 * 汇付提现
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 下午2:44:00
 */
public interface WithdrawHFService extends BaseService {

	BankConfig getBankInfo(String code);

	String getWithdrawFee(Integer userId, String bankId, BigDecimal amount, String type);

	ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id);

	int updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record);

	String checkCashResult(String ordId);

	JSONObject getMsgData(String ordId);

	Integer selectUserIdByUsrcustid(Long chinapnrUsrcustid);

	boolean updateBeforeCash(ChinapnrBean bean, Map<String, String> params);

	int updateAccountWithdrawByOrdId(String ordId, String reason);

	AccountBank getBankInfoHF(Integer userId, String bankId);

	boolean handlerAfterCash(ChinapnrBean bean, Map<String, String> params);

	int updateChinapnrExclusiveLogStatus(long uuid, String status);


}
