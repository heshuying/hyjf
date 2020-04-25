package com.hyjf.batch.bank.borrow.apicron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 自动扣款(还款服务)
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BorrowRepayToRedisServiceImpl extends BaseServiceImpl implements BorrowRepayToRedisService {

	Logger _log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	@Override
	public List<BorrowApicron> getBorrowApicronList(Integer type) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(0);
		statusList.add(6);
		criteria.andStatusNotIn(statusList);
		criteria.andApiTypeEqualTo(type);// 放还款拆分 wgx 2018/11/30
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);
		return list;
	}
	
	@Override
	public BorrowApicron getBorrowApicron(Integer id) {
		BorrowApicron apicron = this.borrowApicronMapper.selectByPrimaryKey(id);
		return apicron;
	}

	@Override
	public Boolean updateBatchFailLoan(BorrowApicron apicron) {
		Integer failTimes = apicron.getFailTimes();
		String borrowNid = apicron.getBorrowNid();
		try {
			if (failTimes >= 3) {
				_log.info("---------" + borrowNid + "放款失败异常修复次数超出3次,请联系开发人员!---------");
				return false;
			}
			String bankSeqNo = apicron.getBankSeqNo();
			BankCallBean batchResult = batchQuery(apicron);
			if (Validator.isNull(batchResult)) {
				throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
			_log.info("标的编号："+ borrowNid +"，批次查询成功了！");
			apicron.setStatus(1);
			this.borrowApicronMapper.updateByPrimaryKey(apicron);
			return true;
		} catch (Exception e) {
			apicron.setFailTimes(failTimes + 1);
			this.borrowApicronMapper.updateByPrimaryKey(apicron);
			_log.info("放款失败处理异常:" + borrowNid + e.getMessage());
			//TODO 发送放款失败的短信
			sendSmsForManager(borrowNid);
		}
		return false;
	}
	
	private void sendSmsForManager(String borrowNid) {
		// 管理员发送成功短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("val_title", borrowNid);
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_FANGKUAN_SHIBAI, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}
	
	private BankCallBean batchQuery(BorrowApicron apicron) {
		//开始处理
		String orderId = apicron.getOrdid();
		_log.info("标的号:" + apicron.getBorrowNid() + "开始查询批次结果,orderId is :" + orderId);
		String channel = BankCallConstant.CHANNEL_PC;
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		// 根据借款人用户ID查询借款人用户电子账户号
		BankOpenAccount borrowUserAccount = this.getBankOpenAccount(apicron.getUserId());
		if(borrowUserAccount == null || StringUtils.isEmpty(borrowUserAccount.getAccount())){
			_log.info("根据借款人用户ID查询借款人电子账户号失败,借款人用户ID:["+apicron.getUserId()+"]");
			return null;
		}
		// 借款人用户ID
		String borrowUserAccountId = borrowUserAccount.getAccount();
		// 调用满标自动放款查询
		BankCallBean bean = new BankCallBean();
		// 版本号
		bean.setVersion(BankCallConstant.VERSION_10);
		// 交易代码
		bean.setTxCode(BankCallConstant.TXCODE_AUTOLEND_PAY_QUERY);
		// 渠道
		bean.setChannel(channel);
		// 交易日期
		bean.setTxDate(txDate);
		// 交易时间
		bean.setTxTime(txTime);
		// 流水号
		bean.setSeqNo(seqNo);
		// 借款人电子账号
		bean.setAccountId(borrowUserAccountId);
		// 申请订单号(满标放款交易订单号)
		bean.setLendPayOrderId(orderId);
		// 标的编号
		bean.setProductId(apicron.getBorrowNid());
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogUserId(String.valueOf(apicron.getUserId()));
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(apicron.getUserId()));
		bean.setLogRemark("满标自动放款查询");
		BankCallBean queryResult = BankCallUtils.callApiBg(bean);
		if (Validator.isNotNull(queryResult)) {
			String retCode = StringUtils.isNotBlank(queryResult.getRetCode()) ? queryResult.getRetCode() : "";
			if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				return queryResult;
			}
		} 
		return null;
	}
	
}
