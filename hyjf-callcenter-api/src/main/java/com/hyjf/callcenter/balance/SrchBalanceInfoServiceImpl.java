package com.hyjf.callcenter.balance;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountManageCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBankAccountManageCustomize;
@Service
public class SrchBalanceInfoServiceImpl extends CustomizeMapper implements SrchBalanceInfoService {

	@Override
	public List<CallCenterBankAccountManageCustomize> queryBankAccountInfos(CallCenterBankAccountManageCustomize callCenterBankAccountManageCustomize) {
		List<CallCenterBankAccountManageCustomize> bankAccountInfos = this.callCenterBankAccountManageCustomizeMapper.queryAccountInfos(callCenterBankAccountManageCustomize);
		return bankAccountInfos;
	}

	@Override
	public List<CallCenterAccountManageCustomize> queryAccountInfos(CallCenterAccountManageCustomize callCenterAccountManageCustomize) {
		List<CallCenterAccountManageCustomize> accountInfos= this.callCenteraccountManageCustomizeMapper.queryAccountInfos(callCenterAccountManageCustomize);
		return accountInfos;
	}
}

