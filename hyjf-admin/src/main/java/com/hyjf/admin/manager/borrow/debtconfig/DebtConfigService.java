package com.hyjf.admin.manager.borrow.debtconfig;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtConfig;
import com.hyjf.mybatis.model.auto.DebtConfigLog;

import java.util.List;

public interface DebtConfigService extends BaseService {

	/**
	 * 债权配置插入
	 *
	 * @param record
	 */
	public void insertRecord(DebtConfig record);
	/**
	 * 债权配置修改
	 *
	 * @param record
	 */
	public void updateRecord(DebtConfig record, DebtConfigLog debtConfigLog);
	/**
	 * 债权配置查询
	 *
	 */
	public List<DebtConfig> selectDebtConfigList();
	/**
	 * 债权配置日志表查询总条数
	 *
	 */
	int countDebtConfigLogTotal();

	/**
	 * 债权配置日志表查询
	 *
	 */
	public List<DebtConfigLog> getDebtConfigLogList(DebtConfigBean form, int limitStart, int limitEnd);

}
