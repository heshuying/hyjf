package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrecover;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BatchBorrowRecoverServiceImpl extends BaseServiceImpl implements BatchBorrowRecoverService {

    @Override
    public Long countBatchCenter(BatchCenterCustomize batchCenterCustomize) {
        return batchCenterCustomizeMapper.countBatchCenter(batchCenterCustomize);
    }

    @Override
    public List<BatchCenterCustomize> selectBatchCenterList(BatchCenterCustomize batchCenterCustomize) {
        return batchCenterCustomizeMapper.selectBatchCenterList(batchCenterCustomize);
    }

    @Override
    public BankCallBean queryBatchDetails(String borowNid) throws Exception{
    	BorrowApicronExample example = new BorrowApicronExample();
    	example.createCriteria().andBorrowNidEqualTo(borowNid).andApiTypeEqualTo(0);
		List<BorrowApicron> apicronList = this.borrowApicronMapper.selectByExample(example);
		BorrowApicron apicron = null;
		if (apicronList!=null && apicronList.size()>0) {
			apicron = apicronList.get(0);
		}else{
			return null;
		}
		// 借款人用户ID
		Integer borrowUserId = apicron.getUserId();
		// 根据借款人用户ID查询借款人用户电子账户号
		BankOpenAccount borrowUserAccount = this.getBankOpenAccount(borrowUserId);
		if(borrowUserAccount == null || StringUtils.isEmpty(borrowUserAccount.getAccount())){
			throw new Exception("根据借款人用户ID查询借款人电子账户号失败,借款人用户ID:["+borrowUserId+"]");
		}
		// 借款人用户ID
		String borrowUserAccountId = borrowUserAccount.getAccount();
		// 放款订单号
    	String orderId = apicron.getOrdid();
		String channel = BankCallConstant.CHANNEL_PC;
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
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
		bean.setProductId(borowNid);
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogUserId(String.valueOf(borrowUserId));
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(borrowUserId));
		bean.setLogRemark("满标自动放款查询");
		return BankCallUtils.callApiBg(bean);
	}
    
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param batchCenterCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BatchCenterCustomize sumBatchCenter(BatchCenterCustomize batchCenterCustomize) {
		return batchCenterCustomizeMapper.sumBatchCenter(batchCenterCustomize);	
	}
}
