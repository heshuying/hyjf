package com.hyjf.admin.manager.config.htjconfig;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.auto.DebtPlanConfigExample;
import com.hyjf.mybatis.model.customize.AdminSystem;

/**
 * 汇添金配置Service实现类
 * 
 * @ClassName HtjConfigServiceImpl
 * @author liuyang
 * @date 2016年9月27日 上午9:10:52
 */
@Service
public class HtjConfigServiceImpl extends BaseServiceImpl implements HtjConfigService {

	/**
	 * 检索汇添金配置信息件数
	 * 
	 * @Title countDebtPlanConfig
	 * @param form
	 * @return
	 */
	@Override
	public int countDebtPlanConfig(HtjConfigBean form) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		if (form.getLimitStart() > 0) {
			example.setLimitStart(form.getLimitStart());
		}
		if (form.getLimitEnd() > 0) {
			example.setLimitEnd(form.getLimitEnd());
		}
		return this.debtPlanConfigMapper.countByExample(example);
	}

	/**
	 * 检索汇添金配置信息
	 * 
	 * @Title selectDebtPlanConfigList
	 * @param form
	 * @return
	 */
	@Override
	public List<DebtPlanConfig> selectDebtPlanConfigList(HtjConfigBean form) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		if (form.getLimitStart() > 0) {
			example.setLimitStart(form.getLimitStart());
		}
		if (form.getLimitEnd() > 0) {
			example.setLimitEnd(form.getLimitEnd());
		}
		example.setOrderByClause(" create_time DESC");
		return this.debtPlanConfigMapper.selectByExample(example);
	}

	/**
	 * 根据id检索汇添金配置信息
	 * 
	 * @Title getDebtPlanConfigById
	 * @param id
	 * @return
	 */
	@Override
	public DebtPlanConfig getDebtPlanConfigById(String id) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(id)) {
			cra.andIdEqualTo(Integer.parseInt(id));
		}
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExampleWithBLOBs(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 汇添金配置信息更新
	 * 
	 * @Title updateRecord
	 * @param form
	 */
	@Override
	public void updateRecord(HtjConfigBean form) {
		DebtPlanConfig debtPlanConfigInfo = this.getDebtPlanConfigById(form.getIds());
		BeanUtils.copyProperties(form, debtPlanConfigInfo);
		debtPlanConfigInfo.setDebtMaxInvestment(form.getDebtMaxInvestment() == null ? BigDecimal.ZERO : form.getDebtMaxInvestment());
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		debtPlanConfigInfo.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
		debtPlanConfigInfo.setUpdateUserName(adminSystem.getUsername());
		debtPlanConfigInfo.setUpdateTime(GetDate.getMyTimeInMillis());
		debtPlanConfigInfo.setId(Integer.parseInt(form.getIds()));
		debtPlanConfigInfo.setDebtQuitStyle(0);
		this.debtPlanConfigMapper.updateByPrimaryKeySelective(debtPlanConfigInfo);
	}

	/**
	 * 汇添金配置信息新增
	 * 
	 * @Title insertRecord
	 * @param form
	 */
	@Override
	public void insertRecord(HtjConfigBean form) {
		DebtPlanConfig debtPlanConfig = new DebtPlanConfig();
		BeanUtils.copyProperties(form, debtPlanConfig);
		debtPlanConfig.setDelFlag(0);
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		debtPlanConfig.setCreateUserId(Integer.parseInt(adminSystem.getId()));
		debtPlanConfig.setCreateTime(GetDate.getMyTimeInMillis());
		debtPlanConfig.setCreateUserName(adminSystem.getUsername());
		// 退出方式 0:到期退出
		debtPlanConfig.setDebtQuitStyle(0);
		this.debtPlanConfigMapper.insertSelective(debtPlanConfig);
	}

	/**
	 * 检索计划类型是否重复
	 * 
	 * @Title isExistsDebtPlanType
	 * @param debtPlanType
	 * @return
	 */
	@Override
	public boolean isExistsDebtPlanType(String debtPlanType) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanType)) {
			cra.andDebtPlanTypeEqualTo(Integer.parseInt(debtPlanType));
		}
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检索计划类型名称是否重复
	 * 
	 * @Title isExistsDebtPlanTypeName
	 * @param debtPlanTypeName
	 * @return
	 */
	@Override
	public boolean isExistsDebtPlanTypeName(String debtPlanTypeName) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanTypeName)) {
			cra.andDebtPlanTypeNameEqualTo(debtPlanTypeName);
		}
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 汇添金配置删除
	 * 
	 * @Title deleteRecord
	 * @param recordList
	 */
	@Override
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			this.debtPlanConfigMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 检索计划配置前缀是否重复
	 * 
	 * @Title isExistsDebtPlanPrefix
	 * @param debtPlanPrefix
	 * @return
	 */
	@Override
	public boolean isExistsDebtPlanPrefix(String debtPlanPrefix) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanPrefix)) {
			cra.andDebtPlanPrefixEqualTo(debtPlanPrefix);
		}
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检索计划配置锁定期数是否重复
	 * 
	 * @Title isExistsDebtLockPeriod
	 * @param debtLockPeriod
	 * @return
	 */
	@Override
	public boolean isExistsDebtLockPeriod(String debtLockPeriod) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtLockPeriod)) {
			cra.andDebtLockPeriodEqualTo(Integer.parseInt(debtLockPeriod));
		}
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}
}
