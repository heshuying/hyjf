package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrepay;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service(value="adminBatchBorrowRepayServiceImpl")
public class BatchBorrowRepayServiceImpl extends BaseServiceImpl implements BatchBorrowRepayService {

    @Override
    public Long countBatchCenter(BatchCenterCustomize batchCenterCustomize) {
        return batchCenterCustomizeMapper.countBatchCenter(batchCenterCustomize);
    }

    @Override
    public List<BatchCenterCustomize> selectBatchCenterList(BatchCenterCustomize batchCenterCustomize) {
        return batchCenterCustomizeMapper.selectBatchCenterList(batchCenterCustomize);
    }

    @Override
    public List<BankCallBean> queryBatchDetails(String id) {
    	BorrowApicronExample example = new BorrowApicronExample();
    	example.createCriteria().andIdEqualTo(new Integer(id));
		List<BorrowApicron> apicronList = this.borrowApicronMapper.selectByExample(example);
		BorrowApicron apicron = null;
		if (apicronList!=null && apicronList.size()>0) {
			apicron = apicronList.get(0);
		}else{
			return null;
		}
    	
		int txCounts = apicron.getTxCounts();// 总交易笔数
		String batchTxDate = String.valueOf(apicron.getTxDate());
//		String batchNo = apicron.getBatchNo();// 批次号
		int status  = apicron.getStatus();
		int pageSize = 50;// 每页笔数
		int pageTotal = txCounts / pageSize + 1;// 总页数
		List<BankCallBean> results = new ArrayList<BankCallBean>();
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		for (int i = 1; i <= pageTotal; i++) {
			String logOrderId = GetOrderIdUtils.getOrderId2(apicron.getUserId());
			String orderDate = GetOrderIdUtils.getOrderDate();
			String txDate = GetOrderIdUtils.getTxDate();
			String txTime = GetOrderIdUtils.getTxTime();
			String seqNo = GetOrderIdUtils.getSeqNo(6);
			BankCallBean loanBean = new BankCallBean();
			loanBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			loanBean.setTxCode(BankCallConstant.TXCODE_BATCH_DETAILS_QUERY);// 消息类型(批量放款)
			loanBean.setInstCode(instCode);// 机构代码
			loanBean.setBankCode(bankCode);
			loanBean.setTxDate(txDate);
			loanBean.setTxTime(txTime);
			loanBean.setSeqNo(seqNo);
			loanBean.setChannel(channel);
			loanBean.setBatchTxDate(batchTxDate);
			loanBean.setBatchNo(apicron.getBatchNo());
			if (4 == status) {//校验失败
				loanBean.setType(BankCallConstant.DEBT_BATCH_TYPE_VERIFYFAIL);
			}else{
				loanBean.setType(BankCallConstant.DEBT_BATCH_TYPE_ALL);
			}
			loanBean.setPageNum(String.valueOf(i));
			loanBean.setPageSize(String.valueOf(pageSize));
			loanBean.setLogUserId(String.valueOf(apicron.getUserId()));
			loanBean.setLogOrderId(logOrderId);
			loanBean.setLogOrderDate(orderDate);
			loanBean.setLogRemark("查询批次交易明细！");
			loanBean.setLogClient(0);
			// 调用放款接口
			BankCallBean loanResult = BankCallUtils.callApiBg(loanBean);
			if (Validator.isNotNull(loanResult)) {
				String retCode = StringUtils.isNotBlank(loanResult.getRetCode()) ? loanResult.getRetCode() : "";
				if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					results.add(loanResult);
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return results;
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
