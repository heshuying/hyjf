package com.hyjf.admin.exception.bankaccountcheck;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;
@Service
public class BankAccountCheckServiceImpl extends BaseServiceImpl implements BankAccountCheckService{

	@Override
	public List<AdminBankAccountCheckCustomize> queryAccountCheckList(AdminBankAccountCheckCustomize form) {
		// TODO Auto-generated method stub
		List<AdminBankAccountCheckCustomize> resultList = this.adminBankAccountCheckCustomizeMapper.queryBankAccountCheckList(form);
		if (resultList!=null) {
			for (int i = 0; i < resultList.size(); i++) {
				AdminBankAccountCheckCustomize ac = resultList.get(i);
				if (ac.getCheckStatus()!=null&&!"".equals(ac.getCheckStatus())) {
					ac.setAccountCheckStr(this.getParamName(BankAccountCheckDefine.ACCOUNT_CHECK, ac.getCheckStatus()));
				}
				if (ac.getTradeStatus()!=null&&!"".equals(ac.getTradeStatus())) {
					ac.setAccountCheckTradeStr(this.getParamName(BankAccountCheckDefine.ACCOUNT_CHECK_TRADE, ac.getTradeStatus()));
				}
			}
		}
		return resultList;
	}

	@Override
	public Integer queryAccountCheckListCount(AdminBankAccountCheckCustomize customize) {
		// TODO Auto-generated method stub
		Integer count = this.adminBankAccountCheckCustomizeMapper.queryBankAccountCheckListCount(customize);
		return count;
	}
	
}
