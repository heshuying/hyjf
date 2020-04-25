package com.hyjf.batch.exception.banktendercancel;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample;
import com.hyjf.mybatis.model.auto.FreezeHistory;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankTenderCancelExceptionServiceImpl extends BaseServiceImpl implements BankTenderCancelExceptionService{

	Logger _log = LoggerFactory.getLogger(BankTenderCancelExceptionTask.class);
	
	
	@Override
	public int updateTenderCancelData() {
		//查询前一天的出借临时数据并进行处理
		int dayStart10 = GetDate.getDayStart10(new Date());//当天开始时间
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		example.createCriteria().andIsBankTenderEqualTo(1).andAddtimeLessThan(dayStart10+"").andStatusNotEqualTo(3);//除上次处理异常数据
		example.setLimitStart(0);
		example.setLimitEnd(99);
		List<BorrowTenderTmp> tmpList = this.borrowTenderTmpMapper.selectByExample(example);
		int result = 0;
		if (tmpList != null && tmpList.size() > 0) {
			result = tmpList.size();
			for (int i = 0; i < tmpList.size(); i++) {
				boolean delFlag = false;
				BorrowTenderTmp info = tmpList.get(i);
				try {
					BankOpenAccount bankAccount = this.getBankOpenAccount(info.getUserId());
					String accountID = null;
					if (bankAccount != null) {
						accountID = bankAccount.getAccount();
					}else{
						delFlag = true;
						throw new RuntimeException();
					}
					BankCallBean callBean = bidCancel(info.getUserId(), accountID, info.getBorrowNid(), info.getNid(), info.getAccount().toString());
					if (Validator.isNotNull(callBean)) {
						String retCode = StringUtils.isNotBlank(callBean.getRetCode()) ? callBean.getRetCode() : "";
						//出借正常撤销或出借订单不存在则删除冗余数据
						if (retCode.equals(BankCallConstant.RESPCODE_SUCCESS) || retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) 
								|| retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2) || retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)){
							updateBidCancelRecord(info);
						}else{
							throw new RuntimeException("出借撤销接口返回错误!原订单号:" + info.getNid() + ",返回码:" + retCode);
						}
					}else{
						throw new RuntimeException("出借撤销接口异常!");
					}
				} catch (Exception e) {
					if (delFlag) {
						try {
							updateBidCancelRecord(info);
						} catch (Exception e1) {
							_log.error(e1.getMessage());
						}
					}else{
						//出借撤销出现异常,状态置为3,留待手工处理
						updateTenderCancelExceptionData(info);
						_log.error(e.getMessage());
					}
				}
			}
		}
		return result;
	}

	/**
	 * 处理撤销出现异常的数据
	 * @param info
	 */
	private void updateTenderCancelExceptionData(BorrowTenderTmp info) {
		
		BorrowTenderTmp record;
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		example.createCriteria().andNidEqualTo(info.getNid());
		List<BorrowTenderTmp> tempInfo = borrowTenderTmpMapper.selectByExample(example);
		record = tempInfo.get(0);
		record.setStatus(3);
		borrowTenderTmpMapper.updateByPrimaryKey(record);
	}

	/**
	 * 出借撤销历史数据处理
	 * @param tenderTmp
	 * @return
	 * @throws Exception
	 */
	private boolean updateBidCancelRecord(BorrowTenderTmp tenderTmp) throws Exception {
		Integer userId = tenderTmp.getUserId();
		boolean tenderTmpFlag = this.borrowTenderTmpMapper.deleteByPrimaryKey(tenderTmp.getId()) > 0 ? true : false;
		if (!tenderTmpFlag) {
			throw new Exception("删除出借日志表失败，出借订单号：" + tenderTmp.getNid());
		}
		FreezeHistory freezeHistory = new FreezeHistory();
		freezeHistory.setTrxId(tenderTmp.getNid());
		freezeHistory.setNotes("自动任务银行出借撤销");
		freezeHistory.setFreezeUser(this.getUsersByUserId(userId).getUsername());
		freezeHistory.setFreezeTime(GetDate.getNowTime10());
		boolean freezeHisLog = this.freezeHistoryMapper.insert(freezeHistory) > 0 ? true : false;
		if (!freezeHisLog) {
			throw new Exception("插入出借删除日志表失败，出借订单号：" + tenderTmp.getNid());
		}
		return true;
	}
	
	/**
	 * 查出非银行出借的数据并删除
	 */
	private int deleteRedundancyData() {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		example.createCriteria().andIsBankTenderIsNull();
		int count = this.borrowTenderTmpMapper.deleteByExample(example);
		return count;
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
		bean.setLogUserName(this.getUsersByUserId(userId).getUsername()); // 用户名
		bean.setLogRemark("投标申请撤销"); // 备注
		// 调用汇付接口
		BankCallBean result = BankCallUtils.callApiBg(bean);
		return result;
	}

	@Override
	public void start() {
		//查出非银行出借的数据并删除
		int count = deleteRedundancyData();
		_log.info("========删除出借临时冗余数据" + count + "条!");
		int updateCount = updateTenderCancelData();
		if (updateCount > 0) {
			start();
		}
	}
	
	
}
