package com.hyjf.admin.finance.bank.merchant.poundage;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.BankMerchantAccountListCustomize;

@Service
public class BankPoundageAccountServiceImpl extends BaseServiceImpl implements BankPoundageAccountService {

	/**
	 * 获取商户子账户列表
	 * 
	 * @return
	 */
	public List<BankMerchantAccountListCustomize> selectRecordList(BankPoundageAccountListBean form) {
	    BankMerchantAccountListCustomize bankMerchantAccountListCustomize=createBankMerchantAccountListCustomize(form);
		return bankMerchantAccountListCustomizeMapper.selectRecordList(bankMerchantAccountListCustomize);
	}

	

    /**
	 * 查询商户子账户表相应的数据总数
	 * 
	 * @param form
	 * @return
	 * @author Administrator
	 */

	@Override
	public int queryRecordTotal(BankPoundageAccountListBean form) {
	    BankMerchantAccountListCustomize bankMerchantAccountListCustomize=createBankMerchantAccountListCustomize(form);
		return bankMerchantAccountListCustomizeMapper.queryRecordTotal(bankMerchantAccountListCustomize);
	}

	private BankMerchantAccountListCustomize createBankMerchantAccountListCustomize(BankPoundageAccountListBean form) {
	    BankMerchantAccountListCustomize bankMerchantAccountListCustomize=new BankMerchantAccountListCustomize();
	    bankMerchantAccountListCustomize.setSeqNo(form.getSeqNo());
	    bankMerchantAccountListCustomize.setOrderId(form.getOrderId());
	    bankMerchantAccountListCustomize.setAccountId(form.getAccountId());
	    bankMerchantAccountListCustomize.setType(form.getType());
	    bankMerchantAccountListCustomize.setStatus(form.getStatus());
	    bankMerchantAccountListCustomize.setTransType(form.getTransType());
	    bankMerchantAccountListCustomize.setLimitStart(form.getLimitStart());
	    bankMerchantAccountListCustomize.setLimitEnd(form.getLimitEnd());
	    bankMerchantAccountListCustomize.setTimeStartSrch(form.getTimeStartSrch());
	    bankMerchantAccountListCustomize.setTimeEndSrch(form.getTimeEndSrch());
	    bankMerchantAccountListCustomize.setBankAccountCode(form.getBankAccountCode());
	    return bankMerchantAccountListCustomize;
    }
}
