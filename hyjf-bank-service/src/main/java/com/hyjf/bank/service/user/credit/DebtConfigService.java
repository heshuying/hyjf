package com.hyjf.bank.service.user.credit;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.DebtConfig;

import java.util.List;

public interface DebtConfigService extends BaseService {

	/**
	 * 债权配置查询
	 *
	 */
	public List<DebtConfig> selectDebtConfigList();


}
