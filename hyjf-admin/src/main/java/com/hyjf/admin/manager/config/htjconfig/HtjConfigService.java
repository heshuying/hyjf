package com.hyjf.admin.manager.config.htjconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;

/**
 * 汇添金配置Serivce
 * 
 * @ClassName HtjConfigService
 * @author liuyang
 * @date 2016年9月27日 上午9:10:11
 */
public interface HtjConfigService extends BaseService {

	/**
	 * 检索汇添金配置信息件数
	 * 
	 * @Title countDebtPlanConfig
	 * @param form
	 * @return
	 */
	public int countDebtPlanConfig(HtjConfigBean form);

	/**
	 * 检索汇添金配置信息
	 * 
	 * @Title selectDebtPlanConfigList
	 * @param form
	 * @return
	 */
	public List<DebtPlanConfig> selectDebtPlanConfigList(HtjConfigBean form);

	/**
	 * 根据id检索汇添金配置信息
	 * 
	 * @Title getDebtPlanConfigById
	 * @param id
	 * @return
	 */
	public DebtPlanConfig getDebtPlanConfigById(String id);

	/**
	 * 汇添金配置信息更新
	 * 
	 * @Title updateRecord
	 * @param form
	 */
	public void updateRecord(HtjConfigBean form);

	/**
	 * 汇添金配置信息新增
	 * 
	 * @Title insertRecord
	 * @param form
	 */
	public void insertRecord(HtjConfigBean form);

	/**
	 * 检索计划类型是否重复
	 * 
	 * @Title isExistsDebtPlanType
	 * @param debtPlanType
	 * @return
	 */
	public boolean isExistsDebtPlanType(String debtPlanType);

	/**
	 * 检索计划类型名称是否重复
	 * 
	 * @Title isExistsDebtPlanTypeName
	 * @param debtPlanTypeName
	 * @return
	 */
	public boolean isExistsDebtPlanTypeName(String debtPlanTypeName);
	
	/**
	 * 检索计划前缀是否重复
	 * @Title isExistsDebtPlanPrefix
	 * @param debtPlanPrefix
	 * @return
	 */
	public boolean isExistsDebtPlanPrefix(String debtPlanPrefix);
	
	/**
	 * 计划锁定期限是否重复
	 * @Title isExistsDebtLockPeriod
	 * @param debtLockPeriod
	 * @return
	 */
	public boolean isExistsDebtLockPeriod(String debtLockPeriod);

	/**
	 * 汇添金配置删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<Integer> recordList);
}
