package com.hyjf.admin.exception.tendercancelexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddDessenesitizationBean;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample.Criteria;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import java.util.List;

@Service
public class TenderCancelExceptionServiceImpl extends BaseServiceImpl implements TenderCancelExceptionService {
	
	Logger _log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */

	@Override
	public Integer queryCount(TenderCancelExceptionBean record) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andAddtimeLessThan(GetDate.getNowTime10()- 300+"");
		createCriteria.andTenderUserNameLike("%"+record.getUserName()+"%");
		createCriteria.andIsBankTenderEqualTo(1);
		if (StringUtils.isNotBlank(record.getBorrowNid())) {
			createCriteria.andBorrowNidEqualTo(record.getBorrowNid());
		}
		return this.borrowTenderTmpMapper.countByExample(example );
	}

	/**
	 * 复审记录
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowTenderTmp> queryRecordList(TenderCancelExceptionBean record) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andAddtimeLessThan(GetDate.getNowTime10()- 300+"");
		createCriteria.andTenderUserNameLike("%"+record.getUserName()+"%");
		createCriteria.andIsBankTenderEqualTo(1);
		if (StringUtils.isNotBlank(record.getBorrowNid())) {
			createCriteria.andBorrowNidEqualTo(record.getBorrowNid());
		}
		example.setLimitStart(record.getLimitStart());
		example.setLimitEnd(record.getLimitEnd());
		example.setOrderByClause("id");
		return this.borrowTenderTmpMapper.selectByExample(example);

	}

	
	@Override
	public boolean selectTenderIsExists(String orderId) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt = example.createCriteria();
		crt.andNidEqualTo(orderId);
		int count = this.borrowTenderMapper.countByExample(example);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据出借订单号获取相应的出借日志
	 * 
	 * @param orderId
	 * @return
	 */
	@Override
	public BorrowTenderTmp getBorrowTenderTmp(String orderId) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		BorrowTenderTmpExample.Criteria crt = example.createCriteria();
		crt.andNidEqualTo(orderId);
		List<BorrowTenderTmp> tenderTmpList = this.borrowTenderTmpMapper.selectByExample(example);
		if (tenderTmpList != null && tenderTmpList.size() > 0) {
			return tenderTmpList.get(0);
		}
		return null;
	}

	@Override
	public void putMessage() {
		TenderAgreementExample example = new TenderAgreementExample();
		TenderAgreementExample.Criteria criteria = example.createCriteria();
		criteria.andCreateTimeLessThan(1533830400);//创建时间小于8月10日
		criteria.andStatusEqualTo(2);//签署成功
		criteria.andPdfUrlNotEqualTo("");//脱敏PDF不为空，属于异常标的
		List<TenderAgreement> tenderAgreements = this.tenderAgreementMapper.selectByExample(example);
		_log.info("----------------------查询出需要修复的异常脱敏合同：" + tenderAgreements.size());
		if(tenderAgreements != null && tenderAgreements.size() > 0){
			for (TenderAgreement tenderAggement: tenderAgreements
				 ) {
				String borrowNid = tenderAggement.getBorrowNid();
				String contract_id = tenderAggement.getTenderNid();
				_log.info("-----------开始推送脱敏修复数据，borrownid:" + borrowNid + ",合同编号："  + contract_id);
				String savePath;
				String path = "/pdf_tem/";
				String ftpPath;
				savePath = path + "pdf/" + borrowNid ;
				ftpPath = "PDF/" + 10000000 + "/" + borrowNid + "/" + contract_id + "/";
				//下载协议并脱敏
				FddDessenesitizationBean bean = new FddDessenesitizationBean();
				bean.setAgrementID(tenderAggement.getId().toString());
				bean.setSavePath(savePath);
				bean.setTransType("1");
				bean.setFtpPath(ftpPath);
				bean.setDownloadUrl(tenderAggement.getDownloadUrl());
				bean.setOrdid(contract_id);
				bean.setTenderCompany(false);
				bean.setCreditCompany(false);
				rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_DOWNDESSENESITIZATION_CONTRACT, JSONObject.toJSONString(bean));
				_log.info("-----------推送脱敏修复数据结束，borrownid:" + borrowNid + ",合同编号："  + contract_id);

			}
		}
		_log.info("----------------------需要修复的异常脱敏合同，修复结束=================" );
	}


	@Override
	public boolean updateBidCancelRecord(BorrowTenderTmp tenderTmp) throws Exception {

		boolean tenderTmpFlag = this.borrowTenderTmpMapper.deleteByPrimaryKey(tenderTmp.getId()) > 0 ? true : false;
		if (!tenderTmpFlag) {
			throw new Exception("删除出借日志表失败，出借订单号：" + tenderTmp.getNid());
		}
		FreezeHistory freezeHistory = new FreezeHistory();
		freezeHistory.setTrxId(tenderTmp.getNid());
		freezeHistory.setNotes("银行出借撤销");
		freezeHistory.setFreezeUser(ShiroUtil.getLoginUsername());
		freezeHistory.setFreezeTime(GetDate.getNowTime10());
		boolean freezeHisLog = this.freezeHistoryMapper.insert(freezeHistory) > 0 ? true : false;
		if (!freezeHisLog) {
			throw new Exception("插入出借删除日志表失败，出借订单号：" + tenderTmp.getNid());
		}
		return true;
	}

	/**
	 * 银行出借撤销
	 * 
	 * @param userId
	 * @param accountId
	 * @param productId
	 * @param orgOrderId
	 * @param txAmount
	 * @return
	 */
	@Override
	public BankCallBean bidCancel(Integer userId, String accountId, String productId, String orgOrderId, String txAmount) {
		// 标的出借撤销
		BankCallBean bean = new BankCallBean();
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		bean.setVersion(BankCallConstant.VERSION_10); // 版本号(必须)
		bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL); // 交易代码
		bean.setInstCode(instCode);
		bean.setBankCode(bankCode);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6)); // 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setOrderId(orderId); // 订单号(必须)
		bean.setTxAmount(CustomUtil.formatAmount(txAmount));// 交易金额
		bean.setProductId(productId);// 标的号
		bean.setOrgOrderId(orgOrderId);// 原标的订单号
		bean.setLogOrderId(orderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
		bean.setLogUserId(String.valueOf(userId));// 用户Id
		bean.setLogUserName(ShiroUtil.getLoginUsername()); // 用户名
		bean.setLogRemark("投标申请撤销"); // 备注
		// 调用汇付接口
		BankCallBean result = BankCallUtils.callApiBg(bean);
		return result;
	}

}
