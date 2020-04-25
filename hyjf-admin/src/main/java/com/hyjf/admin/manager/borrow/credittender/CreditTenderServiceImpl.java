package com.hyjf.admin.manager.borrow.credittender;

import java.util.ArrayList;
import java.util.List;

import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;

@Service
public class CreditTenderServiceImpl extends BaseServiceImpl implements CreditTenderService {



	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countBorrowCreditTenderList(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.countBorrowCreditInfoList(borrowCreditCustomize);
	}

	/**
	 * 汇转让详细列表
	 * 
	 * @return
	 */
	public List<BorrowCreditCustomize> selectBorrowCreditTenderList(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.selectBorrowCreditInfoList(borrowCreditCustomize);
	}

	/**
	 * 获取金额合计值
	 * @param borrowCreditCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BorrowCreditCustomize sumBorrowCreditInfo(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.sumBorrowCreditInfo(borrowCreditCustomize);	
	}

	/**
	 * 检索承接记录
	 * @param userId
	 * @param borrowNid
	 * @param assignNid
	 * @param creditTenderNid
	 * @param creditNid
	 * @return
	 */
	@Override
	public CreditTender selectCreditTenderRecord(String userId, String borrowNid, String assignNid, String creditTenderNid, String creditNid) {
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(Integer.parseInt(userId));
		cra.andBidNidEqualTo(borrowNid);
		cra.andAssignNidEqualTo(assignNid);
		cra.andCreditTenderNidEqualTo(creditTenderNid);
		cra.andCreditNidEqualTo(creditNid);
		List<CreditTender> list = this.creditTenderMapper.selectByExample(example);
		if (list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 调用江西银行查询单笔出借人投标申请
	 * @param userId
	 * @param orderId
	 * @param accountId
	 * @return
	 */
	@Override
	public List<BankCallBean> bidApplyQuery(String userId, String orderId, String accountId) {
        List<BankCallBean> resultList = new ArrayList<BankCallBean>();
		BankCallBean bean = new BankCallBean();
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);//机构代码
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);//银行代码
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		String channel = BankCallConstant.CHANNEL_PC;
		//设置查询需要参数
		bean.setInstCode(instCode);
		bean.setBankCode(bankCode);
		bean.setTxDate(txDate);
		bean.setTxTime(txTime);
		bean.setSeqNo(seqNo);
		bean.setChannel(channel);
		bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);
		bean.setAccountId(accountId);// 出借人电子账户号
        bean.setOrgOrderId(orderId);
        bean.setLogOrderId(GetOrderIdUtils.getOrderId0(Integer.parseInt(userId)));
        bean.setLogUserId(userId);
        BankCallBean result = BankCallUtils.callApiBg(bean);
        resultList.add(result);
        return resultList;
	}
}
