package com.hyjf.admin.manager.statis;

import java.util.List;

import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.customize.HjhAccountBalanceCustomize;


public interface AccountBalanceService {

	Integer getHjhAccountBalancecount(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

	List<HjhAccountBalanceCustomize> getHjhAccountBalanceMonthList(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

	HjhAccountBalanceCustomize getHjhAccountBalanceSum(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

	Integer getHjhAccountBalanceMonthCount(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

	HjhAccountBalanceCustomize getHjhAccountBalanceMonthSum(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

	List<HjhAccountBalanceCustomize> getHjhAccountBalanceList(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

}
